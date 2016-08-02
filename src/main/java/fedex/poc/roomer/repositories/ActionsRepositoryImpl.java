package fedex.poc.roomer.repositories;

import fedex.poc.roomer.model.Action;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;

/**
 * Created by TG on 21/07/2016.
 */
public class ActionsRepositoryImpl implements ActionsRepository {

    private final ExecutorService executorService;
    private final DAO<Action> dao;

    private final ConcurrentMap<String, ZonedDateTime> cachedRoomReservations;
    private static final Integer CACHED_TTL_IN_MINUTES = 1;

    public ActionsRepositoryImpl(DAO<Action> dao, ExecutorService executorService) {
        this.dao = dao;
        this.executorService = executorService;
        this.cachedRoomReservations = new ConcurrentHashMap<>();
    }
    @Override
    public void saveRoomReservation(String roomId, String from, String to) {
        Action reserveAction = new Action.Builder()
                .setId(roomId)
                .setType("RESERVE")
                .setUntilTime(to)
                .createAction();
        try {
            cachedRoomReservations.put(roomId, ZonedDateTime.now());
            dao.save(reserveAction);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveAsyncRoomReservation(String roomId, String from, String to) {
        executorService.submit(() -> this.saveRoomReservation(roomId, from, to));
    }

    @Override
    public Optional<ZonedDateTime> getLastRoomReservationTime(String roomId) {
        return Optional.ofNullable(cachedRoomReservations.getOrDefault(roomId, null))
                .filter(cachedTime -> cachedTime.plusMinutes(CACHED_TTL_IN_MINUTES)
                                        .isAfter(ZonedDateTime.now())
                );
    }
}

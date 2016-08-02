package fedex.poc.roomer.repositories;

import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * Created by TG on 21/07/2016.
 */
public interface ActionsRepository {
    void saveRoomReservation(String roomId, String from, String to);
    void saveAsyncRoomReservation(String roomId, String from, String to);
    Optional<ZonedDateTime> getLastRoomReservationTime(String roomId);
}

package fedex.poc.roomer.repositories;

import fedex.poc.roomer.model.Room;
import fedex.poc.roomer.model.RoomsSnapshot;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

/**
 * Created by TG on 20/07/2016.
 */
public class RoomsRepositoryImpl implements RoomsRepository {

    private DAO<RoomsSnapshot> dao;

    public RoomsRepositoryImpl(DAO<RoomsSnapshot> dao) {
        this.dao = dao;
    }

    @Override
    public Optional<Room> findById(String roomId) {
        return getAll().stream().filter(room -> room.id.equals(roomId)).findFirst();
    }

    @Override
    public Collection<Room> getAll() {
        Optional<RoomsSnapshot> roomsSnapshotOptional = dao.getById(RoomsSnapshot.SINGLE_ID);
        if (!roomsSnapshotOptional.isPresent()) {
            return Collections.emptyList();
        }
        RoomsSnapshot roomsSnapshot = roomsSnapshotOptional.get();
        return roomsSnapshot.getRooms();
    }
}

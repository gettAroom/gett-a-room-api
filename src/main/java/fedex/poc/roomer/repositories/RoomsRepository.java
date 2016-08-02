package fedex.poc.roomer.repositories;

import fedex.poc.roomer.model.Room;

import java.util.Collection;
import java.util.Optional;

/**
 * Created by TG on 20/07/2016.
 */
public interface RoomsRepository {
    Optional<Room> findById(String roomId);
    Collection<Room> getAll();
}

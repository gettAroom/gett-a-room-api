package fedex.poc.roomer.repositories;

import java.util.Collection;
import java.util.Optional;

/**
 * Created by tg60668 on 27/7/2016.
 */
public interface DAO<E> {
    Collection<E> getAll();
    Optional<E> getById(String id);
    void save(E element) throws Exception;
}

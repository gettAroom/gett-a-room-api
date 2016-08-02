package fedex.poc.roomer.mq.dao;

import java.util.Optional;

/**
 * Created by TG on 19/07/2016.
 */
public interface DataReader<T> {
    Optional<T> get();
}

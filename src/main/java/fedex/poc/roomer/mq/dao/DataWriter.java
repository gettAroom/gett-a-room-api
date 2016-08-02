package fedex.poc.roomer.mq.dao;

/**
 * Created by TG on 19/07/2016.
 */
public interface DataWriter<T> {
    void put(T data) throws Exception;
}

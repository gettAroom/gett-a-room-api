package fedex.poc.roomer.cache;

import fedex.poc.roomer.model.Cacheable;
import fedex.poc.roomer.mq.dao.DataReader;
import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by tg60668 on 20/7/2016.
 */
public class CacheManager<T extends Cacheable> implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(CacheManager.class);

    private ConcurrentMap<String, T> cache = new ConcurrentHashMap<>();

    private DataReader<T> dataReader;

    private AtomicBoolean isRunning;

    public CacheManager(DataReader<T> dataReader) {
        this.dataReader = dataReader;
        this.isRunning = new AtomicBoolean(false);
    }

    @Override
    public void run() {
        this.isRunning.set(true);
        LOGGER.info("Sync");
        while (this.isRunning.get()) {
            sync();
            LOGGER.info(String.format("current cache has %s elements", this.cache.size()));
        }
    }

    public void shutdown() {
        this.isRunning.set(false);
    }

    private void sync() {
        Optional<T> optionalData = dataReader.get();
        if (!optionalData.isPresent()) {
            LOGGER.info("Cache got nothing");
            return;
        }

        LOGGER.info("Cache got something to update");
        final T incomingData = optionalData.get();
        cache.compute(incomingData.getId(), (key, existsData) ->
                existsData == null || incomingData.isNewer(existsData) ? incomingData : existsData);
    }

    public Collection<T> getAll() {
        return this.cache.values();
    }

    public Optional<T> getById(String id) {
        return Optional.ofNullable(this.cache.getOrDefault(id, null));
    }
}

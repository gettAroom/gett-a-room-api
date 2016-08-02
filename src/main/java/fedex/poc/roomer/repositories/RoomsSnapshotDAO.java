package fedex.poc.roomer.repositories;

import fedex.poc.roomer.cache.CacheManager;
import fedex.poc.roomer.model.RoomsSnapshot;

import java.util.Collection;
import java.util.Optional;

/**
 * Created by tg60668 on 27/7/2016.
 */
public class RoomsSnapshotDAO implements DAO<RoomsSnapshot> {

    private final CacheManager<RoomsSnapshot> cacheManager;

    public RoomsSnapshotDAO(CacheManager<RoomsSnapshot> cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public Collection<RoomsSnapshot> getAll() {
        return cacheManager.getAll();
    }

    @Override
    public Optional<RoomsSnapshot> getById(String id) {
        return cacheManager.getById(id);
    }

    @Override
    public void save(RoomsSnapshot element) throws Exception {
        throw new UnsupportedOperationException();
    }
}

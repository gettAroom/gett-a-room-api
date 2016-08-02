package fedex.poc.roomer.repositories;

import fedex.poc.roomer.model.Action;
import fedex.poc.roomer.mq.dao.MQDataAccess;

import java.util.Collection;
import java.util.Optional;

/**
 * Created by tg60668 on 27/7/2016.
 */
public class ActionsDAO implements DAO<Action> {

    private final MQDataAccess<Action> mqDataAccess;

    public ActionsDAO(MQDataAccess<Action> mqDataAccess) {
        this.mqDataAccess = mqDataAccess;
    }

    @Override
    public Collection<Action> getAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Action> getById(String id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void save(Action element) throws Exception {
        mqDataAccess.put(element);
    }
}

package fedex.poc.roomer.mq.dao;

import com.amazonaws.services.sqs.model.Message;
import fedex.poc.roomer.mq.manager.MQManager;
import fedex.poc.roomer.mq.translators.MQMessageTranslator;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Optional;

/**
 * Created by TG on 19/07/2016.
 */
public class MQDataAccess<T> implements DataWriter<T>, DataReader<T> {
    private static final Logger LOGGER = Logger.getLogger(MQDataAccess.class);

    protected MQManager mqManager;
    protected String mqURL;
    protected MQMessageTranslator mqMessageTranslator;
    protected Class<T> clazz;

    public MQDataAccess(MQManager mqManager, String mqURL, Class<T> clazz) {
        this.mqManager = mqManager;
        this.mqURL = mqURL;
        this.clazz = clazz;
        this.mqMessageTranslator = MQMessageTranslator.DEFAULT;
    }

    @Override
    public final void put(T data) throws Exception {
        this.mqManager.putMessage(this.mqURL, mqMessageTranslator.<T>transformData(data));
    }

    @Override
    public final Optional<T> get() {
        Optional<Message> optionalMsg = this.mqManager.getMessage(this.mqURL);
        if (!optionalMsg.isPresent()) return Optional.empty();

        Message msg = optionalMsg.get();
        this.mqManager.deleteMessage(this.mqURL, msg);

        try {
            return Optional.ofNullable(mqMessageTranslator.<T>transformMessage(msg.getBody(), this.clazz));
        } catch (IOException e) {
            LOGGER.warn(String.format("Failed to transform the following message: %s", msg.getBody()));
            return Optional.empty();
        }

    }
}

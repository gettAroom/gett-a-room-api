package fedex.poc.roomer.mq.manager;

import com.amazonaws.services.sqs.model.Message;

import java.util.Optional;

/**
 * Created by TG on 19/07/2016.
 */
public interface MQReader {
    Optional<Message> getMessage(String queueUrl);
}

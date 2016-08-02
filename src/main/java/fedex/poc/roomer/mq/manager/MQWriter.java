package fedex.poc.roomer.mq.manager;

import com.amazonaws.services.sqs.model.Message;

/**
 * Created by TG on 19/07/2016.
 */
public interface MQWriter {
    void putMessage(String queueUrl, String messageContent);
    void deleteMessage(String queueUrl, Message message);
}

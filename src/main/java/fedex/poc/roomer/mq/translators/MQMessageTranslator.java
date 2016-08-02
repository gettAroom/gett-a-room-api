package fedex.poc.roomer.mq.translators;

import java.io.IOException;

/**
 * Created by tg60668 on 20/7/2016.
 */
public interface MQMessageTranslator {
    MQMessageTranslator DEFAULT = new JsonMQMessageTranslator();
    <T> T transformMessage(String message, Class<T> clazz) throws IOException;
    <T> String transformData(T data) throws Exception;
}

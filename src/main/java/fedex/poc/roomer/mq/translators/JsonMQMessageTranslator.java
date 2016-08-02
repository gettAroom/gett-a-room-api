package fedex.poc.roomer.mq.translators;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Created by tg60668 on 20/7/2016.
 */
public class JsonMQMessageTranslator implements MQMessageTranslator {

    @Override
    public <T> T transformMessage(String message, Class<T> clazz) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        T t = mapper.readValue(message, clazz);
        return t;
    }

    @Override
    public <T> String transformData(T data) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(data);
    }
}

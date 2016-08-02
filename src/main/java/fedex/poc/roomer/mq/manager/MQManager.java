package fedex.poc.roomer.mq.manager;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.google.common.collect.Iterables;
import fedex.poc.roomer.utils.CollectionsUtils;

import java.util.List;
import java.util.Optional;

/**
 * Created by TG on 19/07/2016.
 */
public class MQManager implements MQReader, MQWriter {
    private AmazonSQS sqs;

    /**
     *
     */
    public MQManager(boolean useProxy) {
        this.sqs = initClient(useProxy);
    }

    @Override
    public Optional<Message> getMessage(String queueUrl) {
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl)
                .withWaitTimeSeconds(20)
                .withVisibilityTimeout(20)
                .withMaxNumberOfMessages(1);

        // blocking request
        List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();

        if (CollectionsUtils.isEmpty(messages)) return Optional.<Message>empty();

        return Optional.ofNullable(Iterables.getFirst(messages, null));
    }

    @Override
    public void putMessage(String queueUrl, String messageContent) {
        SendMessageRequest myMessageRequest = new SendMessageRequest(queueUrl, messageContent);
        sqs.sendMessage(myMessageRequest);
    }

    @Override
    public void deleteMessage(String queueUrl, Message message) {
        String receiptHandle = message.getReceiptHandle();
        sqs.deleteMessage(queueUrl, receiptHandle);
    }

    private AmazonSQS initClient(boolean useProxy) {

        ClientConfiguration clientConfig = new ClientConfiguration();
        if (useProxy) {
            clientConfig.setProtocol(Protocol.HTTPS);
            clientConfig.setProxyHost("euwebgate.eu.ssmb.com");
            clientConfig.setProxyPort(80);
        }

        AWSCredentials credentials = new BasicAWSCredentials("AKIAILSPSZL6H4I5QN2Q", "1vRf+XzqH8dMBk7xqXQsJeulG0jBQwJsIOlrPxsA");
        AmazonSQS sqs = new AmazonSQSClient(credentials,clientConfig);
        Region usWest2 = Region.getRegion(Regions.US_WEST_2);
        sqs.setRegion(usWest2);

        return sqs;
    }
}

package dev.chrzaszcz.kafka;

import java.util.Arrays;
import java.util.List;

public class AssertKafka implements AutoCloseable {

    private final QueueConsumer queueConsumer;
    private final ReceivedMessages receivedMessages;

    public AssertKafka(List<String> topics) {
        receivedMessages = new ReceivedMessages();

        queueConsumer = new InMemoryQueueConsumer(receivedMessages);
    }

    public AssertKafka(ReceivedMessages receivedMessages, QueueConsumer queueConsumer) {
        this.receivedMessages = receivedMessages;
        this.queueConsumer = queueConsumer;
    }

    public void received(String message) {
        receivedMessages.findMessage(message).orElseThrow(() -> new MessageMissingException(message, List.of("TODO to be implemented")));
    }

    @Override
    public void close() throws Exception {
        queueConsumer.close();
    }

    public void receivedInAnyOrder(String... messages) {
        Arrays.stream(messages).forEach(this::received);
    }
}

class MessageMissingException extends RuntimeException {

    public MessageMissingException(String expectedMessage, List<String> receivedMessages) {
        super("Expected " + expectedMessage + ", but received: " + String.join(",", receivedMessages));
    }
}
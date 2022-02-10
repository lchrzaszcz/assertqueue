package dev.chrzaszcz.kafka;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        findMessage(message);
    }

    public void receivedInOrder(String... messages) {
        List<ReceivedMessage> foundMessages = Arrays.stream(messages).map(this::findMessage).collect(Collectors.toList());
        foundMessages.stream().reduce((a, b) -> {
            if (a.isAfter(b)) {
                throw new MessagesNotInOrderException(a, b);
            }
            if (a.isConcurrentTo(b)) {
                throw new UnorderableMessagesException(a, b);
            }
            return b;
        }).stream().count();
    }

    public void receivedInAnyOrder(String... messages) {
        Arrays.stream(messages).forEach(this::received);
    }

    public void receivedIndependently(String... messages) {
        List<ReceivedMessage> foundReceivedMessages = Arrays.stream(messages).map(this::findMessage).collect(Collectors.toList());

        foundReceivedMessages.forEach((receivedMessage) -> {
            List<ReceivedMessage> otherMessages = foundReceivedMessages.stream().filter((it) -> !it.equals(receivedMessage)).collect(Collectors.toList());
            otherMessages.forEach((otherMessage) -> {
                boolean isConcurrent = otherMessage.isConcurrentTo(receivedMessage);
                if (!isConcurrent) {
                    throw new MessagesPotentiallyInOrderException(receivedMessage, otherMessage);
                }
            });
        });
    }

    private ReceivedMessage findMessage(String message) {
        return receivedMessages.findMessage(message).orElseThrow(() -> new MessageMissingException(message, List.of("TODO to be implemented")));
    }

    @Override
    public void close() throws Exception {
        queueConsumer.close();
    }
}


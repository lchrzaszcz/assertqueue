package dev.chrzaszcz.kafka;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ReceivedMessages {
    private final Map<Integer, List<ReceivedMessage>> receivedMessages = new HashMap<>();

    public synchronized void addReceivedMessage(String messageKey, String messageBody) {
        receivedMessages.putIfAbsent(1, new LinkedList<>());

        List<ReceivedMessage> receivedMessagesOnPartition = this.receivedMessages.get(1);
        receivedMessagesOnPartition.add(new ReceivedMessage(receivedMessagesOnPartition.size(), messageKey, messageBody));
    }

    public Optional<ReceivedMessage> findMessage(String expectedMessage) {
        return receivedMessages.values().stream().flatMap(Collection::stream).filter(it -> it.hasMessage(expectedMessage)).findFirst();
    }
}

class ReceivedMessage {
    private final int offset;
    private final String message;
    private final String key;

    public ReceivedMessage(int offset, String key, String message) {
        this.offset = offset;
        this.key = key;
        this.message = message;
    }

    public boolean hasMessage(String expectedMessage) {
        return expectedMessage.equals(message);
    }

    public boolean isConcurrentTo(ReceivedMessage receivedMessage) {
        return this != receivedMessage && !isAfter(receivedMessage) && !isBefore(receivedMessage);
    }

    public boolean isAfter(ReceivedMessage receivedMessage) {
        return this.key.equals(receivedMessage.key) && offset > receivedMessage.offset;
    }

    public boolean isBefore(ReceivedMessage receivedMessage) {
        return this.key.equals(receivedMessage.key) && offset < receivedMessage.offset;
    }
}


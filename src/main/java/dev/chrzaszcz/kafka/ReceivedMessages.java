package dev.chrzaszcz.kafka;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ReceivedMessages {
    private final Map<Integer, List<String>> receivedMessages = new HashMap<>();

    public synchronized void addReceivedMessage(String receivedMessage) {
        receivedMessages.putIfAbsent(1, new LinkedList<>());
        receivedMessages.get(1).add(receivedMessage);

    }

    public Optional<String> findMessage(String expectedMessage) {
        return receivedMessages.values().stream().flatMap(Collection::stream).filter(it -> it.equals(expectedMessage)).findFirst();
    }
}


package dev.chrzaszcz.kafka;

import java.util.List;

public class MessageMissingException extends RuntimeException {

    public MessageMissingException(String expectedMessage, List<String> receivedMessages) {
        super("Expected " + expectedMessage + ", but received: " + String.join(",", receivedMessages));
    }
}

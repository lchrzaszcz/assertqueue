package dev.chrzaszcz.kafka;

public class UnorderableMessagesException extends RuntimeException {

    public UnorderableMessagesException(ReceivedMessage before, ReceivedMessage after) {
        super("Expected " + before + " to be received before " + after + " but they were received with different keys");
    }
}

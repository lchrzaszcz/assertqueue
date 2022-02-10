package dev.chrzaszcz.kafka;

public class MessagesPotentiallyInOrderException extends RuntimeException {

    public MessagesPotentiallyInOrderException(ReceivedMessage before, ReceivedMessage after) {
        super("Expected " + before + " to be received concurrently to " + after + " but it was received with the same key");
    }
}

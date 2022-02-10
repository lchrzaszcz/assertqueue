package dev.chrzaszcz.kafka;

public class MessagesNotInOrderException extends RuntimeException {

    public MessagesNotInOrderException(ReceivedMessage before, ReceivedMessage after) {
        super("Expected " + before + " to be received before " + after + " but it was received in opposite order");
    }
}

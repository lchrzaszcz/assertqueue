package dev.chrzaszcz.kafka;

public class InMemoryQueueConsumer implements QueueConsumer, AutoCloseable {

    private ReceivedMessages receivedMessages;

    public InMemoryQueueConsumer(ReceivedMessages receivedMessages) {
        this.receivedMessages = receivedMessages;
    }

    public void sendMessage(String message) {
        receivedMessages.addReceivedMessage(message);
    }

    @Override
    public void close() throws Exception {
        // nop
    }
}

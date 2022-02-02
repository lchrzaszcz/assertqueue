import dev.chrzaszcz.kafka.AssertKafka;
import dev.chrzaszcz.kafka.InMemoryQueueConsumer;
import dev.chrzaszcz.kafka.ReceivedMessages;
import org.junit.jupiter.api.Test;

public class AssertKafkaTests {

    @Test
    void shouldAssertThatMessageWasReceived() {
        // given
        ReceivedMessages receivedMessages = new ReceivedMessages();
        InMemoryQueueConsumer queueConsumer = new InMemoryQueueConsumer(receivedMessages);

        AssertKafka kafkaAssertions = new AssertKafka(receivedMessages, queueConsumer);

        // when
        queueConsumer.sendMessage("anyMessage");
        // doing something

        // then
        kafkaAssertions.received("anyMessage");
    }

    @Test
    void shouldAssertThatMessageWereReceivedInAnyOrder() {
        // given
        ReceivedMessages receivedMessages = new ReceivedMessages();
        InMemoryQueueConsumer queueConsumer = new InMemoryQueueConsumer(receivedMessages);

        AssertKafka kafkaAssertions = new AssertKafka(receivedMessages, queueConsumer);

        // when
        queueConsumer.sendMessage("anyMessage1");
        queueConsumer.sendMessage("anyMessage2");
        queueConsumer.sendMessage("anyMessage3");

        // doing something

        // then
        kafkaAssertions.receivedInAnyOrder("anyMessage2", "anyMessage1", "anyMessage3");
    }

    @Test
    void shouldAssertThatMessageWasReceived2() {
        // given

        // when
        // doing something

        // then
//        kafka.receivedOnTheSamePartition("anyMessage1", "anyMessage2", "anyMessage3").onTopic("anyTopic");
//        kafka.receivedOnDifferentPartitionss("anyMessage1", "anyMessage2", "anyMessage3").onTopic("anyTopic");
//        kafka.receivedOnPotentiallyDifferentPartitions("anyMessage1", "anyMessage2", "anyMessage3").onTopic("anyTopic");
    }
}

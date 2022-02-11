import dev.chrzaszcz.AssertQueue;
import dev.chrzaszcz.kafka.AssertKafka;
import dev.chrzaszcz.kafka.InMemoryQueueConsumer;
import dev.chrzaszcz.kafka.MessagesNotInOrderException;
import dev.chrzaszcz.kafka.MessagesPotentiallyInOrderException;
import dev.chrzaszcz.kafka.ReceivedMessages;
import dev.chrzaszcz.kafka.UnorderableMessagesException;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.Arrays;
import java.util.Properties;

import static org.apache.kafka.clients.producer.ProducerConfig.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AssertKafkaIntegrationTests {

    KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.1"));

    private KafkaProducer<String, String> createProducer(String bootstrapServers) {
        Properties properties = new Properties();
        properties[BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers;

        return new KafkaProducer<String, String>();
    }

    @Test
    void shouldAssertThatMessageWasReceived() {
        // given
        AssertKafka assertKafka = AssertQueue.kafka(kafka.getBootstrapServers(), Arrays.asList("anyTopic"));
        Producer<String, String> producer = createProducer(kafka.getBootstrapServers());

        // when
        queueConsumer.sendMessage("anyKey", "anyMessage");
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
        queueConsumer.sendMessage("anyKey1", "anyMessage1");
        queueConsumer.sendMessage("anyKey2", "anyMessage2");
        queueConsumer.sendMessage("anyKey3", "anyMessage3");

        // doing something

        // then
        kafkaAssertions.receivedInAnyOrder("anyMessage2", "anyMessage1", "anyMessage3");
    }

    @Test
    void shouldAssertThatMessageWereReceivedInOrder() {
        // given
        ReceivedMessages receivedMessages = new ReceivedMessages();
        InMemoryQueueConsumer queueConsumer = new InMemoryQueueConsumer(receivedMessages);

        AssertKafka kafkaAssertions = new AssertKafka(receivedMessages, queueConsumer);

        // when
        queueConsumer.sendMessage("anyKey", "anyMessage1");
        queueConsumer.sendMessage("anyKey", "anyMessage2");
        queueConsumer.sendMessage("anyKey", "anyMessage3");

        // then
        kafkaAssertions.receivedInOrder("anyMessage1", "anyMessage2", "anyMessage3");
    }

    @Test
    void shouldAssertThatExceptionIsThrownWhenExpectingMessageReceivedInOrderAfterSendingThemWithDifferentKeys() {
        // given
        ReceivedMessages receivedMessages = new ReceivedMessages();
        InMemoryQueueConsumer queueConsumer = new InMemoryQueueConsumer(receivedMessages);

        AssertKafka kafkaAssertions = new AssertKafka(receivedMessages, queueConsumer);

        // when
        queueConsumer.sendMessage("anyKey1", "anyMessage1");
        queueConsumer.sendMessage("anyKey2", "anyMessage2");
        queueConsumer.sendMessage("anyKey3", "anyMessage3");

        // then
        assertThatThrownBy(() -> {
            kafkaAssertions.receivedInOrder("anyMessage1", "anyMessage2", "anyMessage3");
        }).isInstanceOf(UnorderableMessagesException.class);
    }

    @Test
    void shouldRaiseAnExceptionIfMessagesWereReceivedInDifferentOrder() {
        // given
        ReceivedMessages receivedMessages = new ReceivedMessages();
        InMemoryQueueConsumer queueConsumer = new InMemoryQueueConsumer(receivedMessages);

        AssertKafka kafkaAssertions = new AssertKafka(receivedMessages, queueConsumer);

        // when
        queueConsumer.sendMessage("anyKey", "anyMessage1");
        queueConsumer.sendMessage("anyKey", "anyMessage2");

        // then
        assertThatThrownBy(() ->
                kafkaAssertions.receivedInOrder("anyMessage2", "anyMessage1")
        ).isInstanceOf(MessagesNotInOrderException.class);
    }

    @Test
    void shouldAssertThatExceptionIsThrownWhenExpectingMessagesToBeReceivedIndependentlyButTheyWereReceivedWithTheSameKey() {
        // given
        ReceivedMessages receivedMessages = new ReceivedMessages();
        InMemoryQueueConsumer queueConsumer = new InMemoryQueueConsumer(receivedMessages);

        AssertKafka kafkaAssertions = new AssertKafka(receivedMessages, queueConsumer);

        // when
        queueConsumer.sendMessage("anyKey","anyMessage1");
        queueConsumer.sendMessage("anyKey", "anyMessage2");
        queueConsumer.sendMessage("anyKey", "anyMessage3");

        // then
        assertThatThrownBy(() -> {
            kafkaAssertions.receivedIndependently("anyMessage1", "anyMessage2", "anyMessage3");
        }).isInstanceOf(MessagesPotentiallyInOrderException.class);
    }

    @Test
    void shouldAssertThatMessageWasReceivedIndependently() {
        // given
        ReceivedMessages receivedMessages = new ReceivedMessages();
        InMemoryQueueConsumer queueConsumer = new InMemoryQueueConsumer(receivedMessages);

        AssertKafka kafkaAssertions = new AssertKafka(receivedMessages, queueConsumer);

        // when
        queueConsumer.sendMessage("anyKey1","anyMessage1");
        queueConsumer.sendMessage("anyKey2", "anyMessage2");
        queueConsumer.sendMessage("anyKey3", "anyMessage3");

        // then
        kafkaAssertions.receivedIndependently("anyMessage1", "anyMessage2", "anyMessage3");
    }
}

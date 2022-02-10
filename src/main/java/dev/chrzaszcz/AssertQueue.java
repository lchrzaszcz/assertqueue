package dev.chrzaszcz;

import dev.chrzaszcz.kafka.AssertKafka;

import java.util.List;

public class AssertQueue {
    public static AssertKafka kafka(String bootstrapServer, List<String> topics) {
        return new AssertKafka(topics);
    }
}

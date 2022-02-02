# assertqueue

## What is assertqueue

**assertqueue** is a handy helper for testing your service integration with queue technologies. Currently I'm working on integrating Kafka in this library, but I hope many more systems will be included.

## Why assertqueue

In my experience, end-to-end testing your service with Kafka (or any other queueing technology) as an output is tricky. This library is a right tool to make things easier and less error prone.

## Setup

To be soon published in maven and setup included :)

## Usage

Simple test might look like this.

```java
// given
AssertKafka kafkaAssertions = AssertQueue.kafka(asList("topic1", "topic2"));

// when
// do something with your application to trigger sending event to kafka

// then
kafkaAssertions.receivedInAnyOrder("anyMessage2", "anyMessage1", "anyMessage3");
```

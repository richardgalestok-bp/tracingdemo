package com.example;

import com.example.kafka.RegularConsumer;
import io.micronaut.configuration.kafka.KafkaAcknowledgement;
import io.micronaut.messaging.Acknowledgement;
import io.micronaut.messaging.exceptions.MessageAcknowledgementException;
import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class MessageJob {

  private final RegularConsumer consumer;
  private int counter = 0;

  @Scheduled(fixedRate = "10s") // Adjust interval as needed
  public void sendPeriodicMessage() {
    String message = "Message " + counter++;
    String key = "key" + counter;
    System.out.println("Simulating message sending: " + message);

    // Directly invoke the listener
    consumer.receive(key, message, counter, (KafkaAcknowledgement) () -> log.info("Message acknowledged: {}", message));
  }
}

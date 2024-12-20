package com.example;

import com.example.kafka.RegularProducer;
import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class MessageJob {

  private final RegularProducer producer;
  private int counter = 0;

  @Scheduled(fixedRate = "10s") // Adjust interval as needed
  public void sendPeriodicMessage() {
    String message = "Message " + counter++;
    String key = "key" + counter;
    System.out.println("Simulating message sending: " + message);

    // Directly send the message to kafka topic
    producer.send(key, message).block();
  }
}

package com.example.kafka;

import static com.example.Constants.EXAMPLE_TOPIC_NAME;

import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;
import jakarta.inject.Singleton;
import reactor.core.publisher.Mono;

@Singleton
@KafkaClient(id = "regular-producer-id")
public interface RegularProducer {

  @Topic(EXAMPLE_TOPIC_NAME)
  Mono<String> send(@KafkaKey final String key, final String val);
}

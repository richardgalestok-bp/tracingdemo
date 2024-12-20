package com.example.kafka;

import static java.time.Duration.ofMillis;
import static reactor.core.publisher.Mono.delay;

import com.example.handler.RegularHandler;
import datadog.trace.api.Trace;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.messaging.Acknowledgement;
import io.micronaut.messaging.annotation.MessageBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@KafkaListener(groupId = "example-group-name", pollTimeout = "3000ms")
public class RegularConsumer {
  private final RegularHandler handler;

  @Topic("example-topic-name")
  public /* this is auto-traced; */
  void receive(@KafkaKey final String key, @MessageBody final String val,
               final long offset, final Acknowledgement ack) {
    delay(ofMillis(100))
      .flatMap(___ -> handle(val))
      .onErrorResume(t -> handleError(key, val, t))
      .doOnSuccess(msg -> log.info("handling offset manually here"))
      .block();
  }

  @Trace(operationName = "jobs", resourceName = "::success")
  Mono<String> handle(final String val) {
    return handler.handle(val);
  }

  @Trace(operationName = "jobs", resourceName = "::error")
  Mono<String> handleError(final String key, final String val, final Throwable t) {
    return handler.handleProcessingError(key, val, t);
  }
}

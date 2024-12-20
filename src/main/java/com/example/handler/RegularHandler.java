package com.example.handler;

import com.example.repository.AnyRepository;
import jakarta.inject.Singleton;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class RegularHandler {

  private final AnyRepository repository;

  public final Mono<String> handle(@NonNull final String message) {
    return repository.upsert(message);
  }

  public Mono<String> handleProcessingError(
    @NonNull final String key,
    @NonNull final String val,
    final Throwable t) {

    log.warn("handling error {}", val, t);
    return Mono.just(val);
  }
}

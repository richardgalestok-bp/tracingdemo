package com.example.repository;

import io.micronaut.core.annotation.NonNull;
import java.util.Optional;
import reactor.core.publisher.Mono;

public interface AnyRepository {

  Mono<Optional<String>> getById(@NonNull final String id);

  Mono<String> upsert(@NonNull final String asset);
}

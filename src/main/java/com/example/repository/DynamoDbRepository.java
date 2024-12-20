package com.example.repository;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.GetItemEnhancedRequest;

@Slf4j
public abstract class DynamoDbRepository<T> {

  @Getter
  private final DynamoDbAsyncTable<T> table;

  protected DynamoDbRepository(@NonNull final DynamoDbAsyncTable<T> table) {
    this.table = table;
  }

  protected Mono<T> put(@NonNull final T item) {
    return fromCompletionStageLazy(() -> getTable().putItem(item))
      .doOnSuccess(___ -> log.debug("Successfully put item {}:", item))
      .doOnError(t -> log.error("Failed to put item {}: {}", item, t.getMessage()))
      .thenReturn(item);
  }

  protected Mono<T> get(@NonNull final Key key) {
    return fromCompletionStageLazy(() -> getTable()
      .getItem(key))
      .doOnError(t -> log.error("Failed to fetch item for {}", key, t))
      .switchIfEmpty(Mono.empty()); // none found
  }

  protected Mono<T> get(@NonNull final Key key, final boolean useStronglyConsistentReads) {
    return get(GetItemEnhancedRequest.builder().key(key).consistentRead(useStronglyConsistentReads).build());
  }

  private Mono<T> get(@NonNull final GetItemEnhancedRequest getRequest) {
    return fromCompletionStageLazy(() -> getTable()
      .getItem(getRequest))
      .doOnError(t -> log.error("Failed to fetch item for {}", getRequest.key(), t))
      .switchIfEmpty(Mono.empty()); // none found
  }

  Mono<Void> delete(@NonNull final Key key) {
    return fromCompletionStageLazy(() -> getTable()
      .deleteItem(key))
      .doOnError(ex -> log.error("Failed to delete item {}", key, ex))
      .then();
  }

  private <X> Mono<X> fromCompletionStageLazy(final Supplier<CompletableFuture<X>> future) {
    return Mono.defer(() -> Mono.fromCompletionStage(future.get()));
  }
}

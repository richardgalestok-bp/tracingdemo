package com.example.repository;

import com.example.model.Item;
import datadog.trace.api.Trace;
import io.micronaut.context.annotation.Value;
import jakarta.inject.Singleton;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Singleton
public class RegularRepository extends DynamoDbRepository<Item> implements AnyRepository{

  public RegularRepository(@NonNull final DynamoDbEnhancedAsyncClient client,
                           @NonNull final @Value("${dynamodb.tableName}") String tableName) {
    super(client.table(tableName, TableSchema.fromBean(Item.class)));
  }

  @Override
  public Mono<Optional<String>> getById(String id) {
    return null;
  }

  @Trace
  @Override
  public Mono<String> upsert(String content) {
    final var id = UUID.randomUUID().toString();
    final var alt_id = id + "_alt";

    return upsert(id, content).then(upsert(alt_id, content)); // store once more, with an alternate key
  }

  private Mono<String> upsert(@NonNull final String id, @NonNull final String content) {
    final var item = new Item();
    item.setPartitionKey(id);
    item.setSortKey("tracingdemo");
    item.setContent(content);
    item.setTtl(Instant.now().getEpochSecond() + 10);
    return put(item).map(Item::getContent);
  }
}

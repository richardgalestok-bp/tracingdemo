package com.example.model;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
@NoArgsConstructor
public class Item {
  public static final String PK = "PK";

  public static final String SK = "SK";

  private String partitionKey;

  private String sortKey;

  @NonNull
  private String content;

  private Long ttl;

  @DynamoDbPartitionKey
  @DynamoDbAttribute(value = PK)
  public String getPartitionKey() {
    return partitionKey;
  }

  public void setPartitionKey(final String partitionKey) {
    this.partitionKey = partitionKey;
  }

  @DynamoDbSortKey
  @DynamoDbAttribute(value = SK)
  public String getSortKey() {
    return sortKey;
  }

  public void setSortKey(final String sortKey) {
    this.sortKey = sortKey;
  }

  @DynamoDbAttribute(value = "ttl")
  public Long getTtl() {
    return ttl;
  }

  public void setTtl(final Long ttl) {
    this.ttl = ttl;
  }

  public String getContent() {
    return content;
  }

  public void setContent(final String content) {
    this.content = content;
  }
}

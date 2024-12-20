package com.example.config;

import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Value;
import jakarta.inject.Singleton;
import java.net.URI;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

@Factory
public class DynamoDbFactory {

  @Value("${dynamodb.accessKey}")
  private String accessKey;

  @Value("${dynamodb.secretKey}")
  private String secretKey;

  @Value("${dynamodb.region}")
  private String region;

  @Value("${dynamodb.endpoint}")
  private String endpoint;

  @Singleton
  public DynamoDbEnhancedAsyncClient getDynamoDbEnhancedAsyncClient() {
    final var httpClientBuilder = NettyNioAsyncHttpClient.builder();

    final var clientBuilder = DynamoDbAsyncClient.builder()
      .httpClientBuilder(httpClientBuilder)
      .region(Region.of(region))
      .credentialsProvider(DefaultCredentialsProvider.create());

    final var basicCreds = AwsBasicCredentials.create(accessKey, secretKey);
    clientBuilder.credentialsProvider(StaticCredentialsProvider.create(basicCreds));
    clientBuilder.endpointOverride(URI.create(endpoint));

    return DynamoDbEnhancedAsyncClient.builder().dynamoDbClient(clientBuilder.build()).build();
  }
}

package com.example;

import static io.micronaut.core.util.StringUtils.SPACE;

public class Constants {
  public static final String SERVICE_NAME = "tracing-demo-service";
  public static final String EXAMPLE_TOPIC_NAME = "example-topic-name";
  public static final String JOBS = "Jobs";
  public static final String SEND = "::send";
  public static final String HANDLE = "::handle";

  // micronaut-kafka standard kafka.consume/produce traces and tags
  public static final String KAFKA_CONSUMER_OPERATION = "kafka.consume";
  public static final String KAFKA_PRODUCER_OPERATION = "kafka.produce";
  public static final String KAFKA_PRODUCER_RESOURCE_PREFIX = "Produce Topic" + SPACE;
  public static final String KAFKA_CONSUMER_GROUP_TAG = "kafka.group";
}

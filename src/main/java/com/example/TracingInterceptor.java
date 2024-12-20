package com.example;

import static com.example.Constants.HANDLE;
import static com.example.Constants.JOBS;
import static com.example.Constants.KAFKA_CONSUMER_GROUP_TAG;
import static com.example.Constants.KAFKA_CONSUMER_OPERATION;
import static com.example.Constants.KAFKA_PRODUCER_OPERATION;
import static com.example.Constants.KAFKA_PRODUCER_RESOURCE_PREFIX;
import static com.example.Constants.SEND;
import static com.example.Constants.SERVICE_NAME;

import datadog.trace.api.DDTags;
import datadog.trace.api.interceptor.MutableSpan;
import datadog.trace.api.interceptor.TraceInterceptor;
import datadog.trace.api.sampling.PrioritySampling;
import ddtrot.dd.trace.bootstrap.instrumentation.api.Tags;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TracingInterceptor implements TraceInterceptor {

  @Override
  public int priority() {
    return Integer.MAX_VALUE; // some high unique number so this interceptor is last
  }

  @Override
  public Collection<? extends MutableSpan> onTraceComplete(final Collection<? extends MutableSpan> originalTrace) {
    log.info("onTraceComplete running..");
    final List<MutableSpan> updatedTrace = new ArrayList<>();
    for (final MutableSpan span : originalTrace) {
      updatedTrace.add(
        switch (span.getOperationName().toString()) {
          case KAFKA_CONSUMER_OPERATION -> updateKafkaConsumerSpan(span);
          case KAFKA_PRODUCER_OPERATION -> updateKafkaProducerSpan(span);
          default -> updateSpan(span);
        }
      );
    }
    return updatedTrace;
  }

  @SuppressWarnings("deprecation")
  private MutableSpan updateSpan(@NonNull final MutableSpan span) {
    span.setSamplingPriority(PrioritySampling.USER_KEEP); // dd-java-agent sampling: never drop this
    span.setTag(DDTags.MANUAL_KEEP, true); // dd-java-agent sampling: never drop this
    span.setTag(DDTags.SERVICE_NAME, SERVICE_NAME);
    span.setTag(DDTags.SPAN_TYPE, Tags.SPAN_KIND_CLIENT);
    return span;
  }

  private MutableSpan updateKafkaConsumerSpan(@NonNull final MutableSpan span) {
    final var consumerGroup = span.getTags().get(KAFKA_CONSUMER_GROUP_TAG);
    if (consumerGroup != null) {
      span.setResourceName(consumerGroup + HANDLE);
      span.setOperationName(JOBS);
    }
    return updateSpan(span);
  }

  private MutableSpan updateKafkaProducerSpan(@NonNull final MutableSpan span) {
    span.setResourceName(span.getResourceName().toString().replace(KAFKA_PRODUCER_RESOURCE_PREFIX, "") + SEND);
    span.setOperationName(JOBS);
    return updateSpan(span);
  }
}
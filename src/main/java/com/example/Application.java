package com.example;

import datadog.trace.api.GlobalTracer;
import io.micronaut.runtime.Micronaut;

public class Application {

    public static void main(String[] args) {
        GlobalTracer.get().addTraceInterceptor(new TracingInterceptor());
        Micronaut.run(Application.class, args);
    }
}
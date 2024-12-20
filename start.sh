#!/bin/bash

# Exit immediately if a command exits with a non-zero status
set -e

# Clean and build the project using Gradle
./gradlew clean build

# Copy the built JAR file to a simpler name
cp build/libs/tracingdemo-*-all.jar tracingdemo.jar

export DD_AGENT_HOST=localhost
export DD_APM_ENABLED=true
export DD_ENV=local
export DD_SERVICE=tracing-demo-service
export DD_VERSION=0.0.1

# Run the application with the specified JVM options
java \
  -Duser.timezone=UTC \
  -Dcom.sun.management.jmxremote \
  -XX:MaxRAMPercentage=75.0 \
  -javaagent:local/dd-java-agent.jar \
  -Ddd.profiling.enabled=false \
  -XX:FlightRecorderOptions=stackdepth=256 \
  -jar tracingdemo.jar
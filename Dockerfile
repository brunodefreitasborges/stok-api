# Start with an Ubuntu base image
FROM ubuntu:latest AS build

# Install dependencies and required tools
RUN apt-get update && \
    apt-get install -y openjdk-21-jdk wget curl gnupg software-properties-common unzip && \
    rm -rf /var/lib/apt/lists/*

# Install Gradle 8.3
RUN wget https://services.gradle.org/distributions/gradle-8.3-bin.zip -P /tmp && \
    unzip /tmp/gradle-8.3-bin.zip -d /opt && \
    ln -s /opt/gradle-8.3/bin/gradle /usr/local/bin/gradle && \
    rm -rf /tmp/gradle-8.3-bin.zip

# Copy your application code
COPY . /app

# Set working directory
WORKDIR /app

# Clear Gradle cache and build application
RUN gradle --stop && \
    rm -rf ~/.gradle/caches && \
    gradle clean build -x test --refresh-dependencies

# Final image to run the app
FROM openjdk:21-jdk-slim

EXPOSE 8080

# Copy the JAR file from the build stage
COPY --from=build /app/build/libs/stokapi-0.0.1-SNAPSHOT.jar app.jar

# Entry point to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

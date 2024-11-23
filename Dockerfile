# Start with an Ubuntu base image
FROM ubuntu:latest AS build

# Install dependencies and required tools
RUN apt-get update && \
    apt-get install -y openjdk-21-jdk wget curl gnupg software-properties-common unzip && \
    rm -rf /var/lib/apt/lists/*

# Install Gradle 7.6
RUN wget https://services.gradle.org/distributions/gradle-7.6-bin.zip -P /tmp && \
    unzip /tmp/gradle-7.6-bin.zip -d /opt && \
    ln -s /opt/gradle-7.6/bin/gradle /usr/local/bin/gradle && \
    rm -rf /tmp/gradle-7.6-bin.zip

# Copy your application code
COPY . /app

# Build your application
WORKDIR /app
RUN gradle clean build -x test

# Final image to run the app
FROM openjdk:21-jdk-slim

EXPOSE 8080

# Copy the JAR file from the build stage
COPY --from=build /app/build/libs/stokapi-0.0.1-SNAPSHOT.jar app.jar

# Entry point to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

# Build stage
FROM ubuntu:latest AS build

# Install dependencies: OpenJDK 21 and Gradle
RUN apt-get update && \
    apt-get install -y wget curl gnupg software-properties-common && \
    # Install OpenJDK 21 from the official PPA (if not available, use another version or repository)
    wget -qO- https://adoptopenjdk.net/installers/ubuntu/deb/openssl1.1/ | tee /etc/apt/sources.list.d/adoptopenjdk.list && \
    apt-get update && \
    apt-get install -y openjdk-21-jdk && \
    # Install Gradle
    wget https://services.gradle.org/distributions/gradle-7.6-bin.zip -P /tmp && \
    unzip /tmp/gradle-7.6-bin.zip -d /opt && \
    ln -s /opt/gradle-7.6/bin/gradle /usr/local/bin/gradle && \
    # Clean up
    rm -rf /var/lib/apt/lists/* /tmp/gradle-7.6-bin.zip

# Set working directory
WORKDIR /app

# Copy source code into the container
COPY . .

# Build the application with stacktrace for better error logs
RUN gradle clean build -x test --stacktrace

# Final image stage
FROM openjdk:21-jdk-slim

# Expose the application port
EXPOSE 8080

# Copy the built jar from the build stage
COPY --from=build /app/build/libs/stokapi-0.0.1-SNAPSHOT.jar /app.jar

# Set the entry point for the container
ENTRYPOINT ["java", "-jar", "/app.jar"]

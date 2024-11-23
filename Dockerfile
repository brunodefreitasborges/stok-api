# Build stage
FROM ubuntu:latest AS build

# Install necessary dependencies
RUN apt-get update && \
    apt-get install -y openjdk-21-jdk gradle && \
    rm -rf /var/lib/apt/lists/*  # Clean up apt cache to reduce image size

# Set working directory
WORKDIR /app

# Copy source code into the container
COPY . .

# Build the application
RUN gradle build -x test  # You can skip tests for faster builds if needed

# Final image stage
FROM openjdk:21-jdk-slim

# Expose the application port
EXPOSE 8080

# Copy the built jar from the build stage
COPY --from=build /app/build/libs/stokapi-0.0.1-SNAPSHOT.jar /app.jar

# Set the entry point for the container
ENTRYPOINT ["java", "-jar", "/app.jar"]

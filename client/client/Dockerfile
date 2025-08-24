# Multi-stage Dockerfile for Spring Boot + Vaadin (Gradle) client
# Build stage
FROM eclipse-temurin:21-jdk AS build

WORKDIR /workspace

# Only copy files needed to download dependencies first (improves caching)
COPY gradlew ./
COPY gradle ./gradle
COPY build.gradle settings.gradle ./

# Ensure Gradle wrapper is executable (in Linux-based build container)
RUN chmod +x gradlew

# Pre-fetch dependencies (will be cached unless build.gradle changes)
RUN ./gradlew --no-daemon dependencies || true

# Copy the rest of the source
COPY src ./src

# Build the application JAR in production mode for Vaadin
RUN ./gradlew --no-daemon -Pvaadin.productionMode=true clean bootJar -x test

# Runtime stage
FROM eclipse-temurin:21-jre AS runtime

WORKDIR /app

# Copy the fat JAR from the build stage
# The filename contains version; use wildcard to simplify
COPY --from=build /workspace/build/libs/*.jar /app/app.jar

# Expose Spring Boot default port
EXPOSE 8080

# Optional: allow custom JVM options
ENV JAVA_OPTS=""

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]

# Stage 1: Build the application
FROM eclipse-temurin:17-jdk-jammy as builder

WORKDIR /app

COPY . .

RUN ./gradlew bootJar

# Stage 2: Create the final, secure, and minimal image
FROM eclipse-temurin:17-jre-jammy

# Create a non-root user for better security
RUN groupadd -r appgroup && useradd -r -g appgroup -s /bin/false appuser

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

# Switch to the non-root user
USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

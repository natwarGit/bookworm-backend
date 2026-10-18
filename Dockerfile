# ── Stage 1: Build ──────────────────────────────────────────────────────────
FROM registry.redhat.io/ubi9/openjdk-17:latest AS builder

WORKDIR /build

# Copy Maven wrapper and pom first (layer cache for dependencies)
COPY pom.xml ./
COPY src ./src

# Build the fat JAR (skip tests for faster Docker build)
RUN mvn clean package -DskipTests

# ── Stage 2: Runtime ─────────────────────────────────────────────────────────
FROM registry.redhat.io/ubi9/openjdk-17-runtime:latest

# Non-root user (security requirement)
USER 1001

WORKDIR /app

# Copy the built JAR from builder stage
COPY --from=builder /build/target/bookworm-backend-*.jar app.jar

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

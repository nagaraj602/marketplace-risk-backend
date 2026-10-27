# Multi-stage Dockerfile for Spring Boot Risk Backend (Java 25 LTS)
# Build Stage
FROM maven:3.9.9-eclipse-temurin-25-noble AS builder

WORKDIR /build

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests -B

# Runtime Stage (Java 25 LTS)
FROM eclipse-temurin:25-jre-noble

WORKDIR /app

# Security: Non-root user execution
RUN groupadd -g 10001 appgroup && \
    useradd -u 10001 -g appgroup -s /bin/bash -m appuser

COPY --from=builder --chown=appuser:appgroup /build/target/marketplace-risk-backend-*.jar app.jar

USER appuser

EXPOSE 8080

ENV JAVA_OPTS="-XX:+UseZGC -XX:+ZGenerational -Xms256m -Xmx768m"

HEALTHCHECK --interval=30s --timeout=5s --start-period=20s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

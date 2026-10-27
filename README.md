# Marketplace Risk Backend Microservices Suite

Core backend detection, velocity rule evaluation, and automated enforcement engine for the **Marketplace Seller Risk & Abuse Detection Platform**.

## 🚀 Tech Stack
- **Language**: Java 21 LTS (OpenJDK 21)
- **Framework**: Spring Boot 4.1.2 (Spring Data JPA, Web, Actuator, Validation)
- **Databases**: PostgreSQL 16+ / AWS RDS, Embedded H2 for isolated testing
- **Cache & Velocity Counters**: Redis sliding window engine
- **Integration**: Python ML Anomaly Service (`/score`) via HTTP RestClient
- **Containerization**: Multi-stage Docker (`eclipse-temurin:21-jdk-noble` -> `eclipse-temurin:21-jre-noble`)
- **Orchestration**: Kubernetes + Helm v3/v4 charts with HPA autoscaling
- **CI/CD**: Jenkins Shared Library Multibranch Pipelines (SonarQube & Trivy pre-merge quality gates)

## 📡 REST API Endpoints
- `POST /api/v1/risk/evaluate`: Ingests seller activity signals, runs velocity checks, ML anomaly scoring, composite aggregation, and automated enforcement routing.
- `GET /api/v1/cases?pendingOnly=true`: Returns prioritized Trust & Safety review queue.
- `PATCH /api/v1/cases/{id}/resolve`: Resolves or dismisses flagged seller cases.
- `POST /api/v1/enforcement/execute`: Manually or automatically executes `SUSPEND_SELLER`, `TAKEDOWN_LISTINGS`, or `HOLD_PAYOUT`.
- `GET /api/v1/risk/telemetry`: Real-time queue depth, automated takedown counts, and false positive metrics.
- `GET /actuator/health`: Kubernetes liveness and readiness probe.

## 🧪 Local Testing & Verification
```bash
mvn clean test
```

## 🐳 Docker Multi-Stage Build
```bash
docker build -t marketplace-risk-backend:latest .
docker run -p 8080:8080 marketplace-risk-backend:latest
```

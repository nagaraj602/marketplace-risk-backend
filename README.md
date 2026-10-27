# Marketplace Risk Backend Microservices Suite

Core backend detection, velocity rule evaluation, and automated enforcement engine for the **Marketplace Seller Risk & Abuse Detection Platform**.

---

## 🔗 Platform Repositories

| Repository | GitHub Link | Purpose & Tech Stack |
| :--- | :--- | :--- |
| **Platform & Infrastructure** | [nagaraj602/marketplace-risk-platform](https://github.com/nagaraj602/marketplace-risk-platform.git) | Terraform AWS IaC (VPC, EKS 1.32, RDS PostgreSQL 16, Redis 7, Jenkins), DB init scripts, Docker Compose |
| **Core Risk Backend** | [nagaraj602/marketplace-risk-backend](https://github.com/nagaraj602/marketplace-risk-backend.git) | Java 21, Spring Boot 4.1.2, JPA, Velocity Rule Engine, Risk Aggregation, Enforcement Orchestration |
| **ML Anomaly Model** | [nagaraj602/marketplace-risk-anomaly-model](https://github.com/nagaraj602/marketplace-risk-anomaly-model.git) | Python 3.12, FastAPI, scikit-learn Isolation Forest, behavioral anomaly inference |
| **Trust & Safety Portal** | [nagaraj602/marketplace-risk-frontend](https://github.com/nagaraj602/marketplace-risk-frontend.git) | React 19, Vite 8, Express 5, Lucide-React, live review queue & signal injection simulator |
| **Jenkins Shared Libraries** | [nagaraj602/jenkins-sharedlibraries](https://github.com/nagaraj602/jenkins-sharedlibraries.git) | Reusable Groovy pipeline vars (`trivyScan`, `sonarAnalysis`, `dockerECRImagePush`, `kubernetesEKSHelmDeployEnv`) |

---

## 🚀 Tech Stack
- **Language**: Java 21 LTS (OpenJDK 21)
- **Framework**: Spring Boot 4.1.2 (Spring Data JPA, Web, Actuator, Validation)
- **Databases**: PostgreSQL 16+ / AWS RDS, Embedded H2 for isolated testing
- **Cache & Velocity Counters**: Redis sliding window engine
- **Integration**: Python ML Anomaly Service (`/score`) via HTTP RestClient
- **Containerization**: Multi-stage Docker (`eclipse-temurin:21-jdk-noble` -> `eclipse-temurin:21-jre-noble`)
- **Orchestration**: Kubernetes + Helm v3/v4 charts with HPA autoscaling
- **CI/CD**: Jenkins Shared Library Multibranch Pipelines (SonarQube & Trivy pre-merge quality gates)

---

## 🛠️ How to Setup & Run

### Local Execution
```bash
# 1. Ensure PostgreSQL (port 5432) and Redis (port 6379) are running
# 2. Run automated test suite
mvn clean test

# 3. Start Spring Boot Application (port 8080)
mvn spring-boot:run
```

### Docker Execution
```bash
docker build -t marketplace-risk-backend:latest .
docker run -p 8080:8080 \
  -e DB_URL=jdbc:postgresql://host.docker.internal:5432/marketplace_risk \
  -e MODEL_SERVER_URL=http://host.docker.internal:5000 \
  marketplace-risk-backend:latest
```

---

## 🎮 How to Operate the Backend

### 1. Ingest & Evaluate Seller Signals
```bash
curl -X POST http://localhost:8080/api/v1/risk/evaluate \
  -H "Content-Type: application/json" \
  -d '{
    "sellerId": "seller_demo_01",
    "sellerName": "Rapid Deal Store",
    "accountAgeDays": 3,
    "listingsLastHour": 85,
    "uniqueDevicesCount": 4,
    "distinctIpCount": 5,
    "payoutAccountChangedRecently": true,
    "geoMismatchCountry": true
  }'
```

### 2. Retrieve Review Queue Cases
```bash
# Get only pending cases awaiting analyst review
curl http://localhost:8080/api/v1/cases?pendingOnly=true
```

### 3. Resolve or Dismiss a Case
```bash
curl -X PATCH http://localhost:8080/api/v1/cases/1/resolve \
  -H "Content-Type: application/json" \
  -d '{
    "decision": "RESOLVED",
    "reason": "Analyst verified merchant identity documentation"
  }'
```

### 4. Query Real-Time Telemetry Metrics
```bash
curl http://localhost:8080/api/v1/risk/telemetry
```

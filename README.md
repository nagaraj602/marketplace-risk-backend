# Marketplace Risk Backend Microservices Suite

Core backend detection, velocity rule evaluation, and automated enforcement engine for the **Marketplace Seller Risk & Abuse Detection Platform**.

---

## 🔗 Platform Repositories

| Repository | GitHub Link | Purpose & Tech Stack |
| :--- | :--- | :--- |
| **Platform & Infrastructure** | [nagaraj602/marketplace-risk-platform](https://github.com/nagaraj602/marketplace-risk-platform.git) | Terraform AWS IaC (VPC, EKS 1.37, RDS PostgreSQL 17, Redis 7.4, Jenkins), DB init scripts, Docker Compose |
| **Core Risk Backend** | [nagaraj602/marketplace-risk-backend](https://github.com/nagaraj602/marketplace-risk-backend.git) | Java 25 LTS, Spring Boot 4.1.2, JPA, Velocity Rule Engine, Risk Aggregation, Enforcement Orchestration |
| **ML Anomaly Model** | [nagaraj602/marketplace-risk-anomaly-model](https://github.com/nagaraj602/marketplace-risk-anomaly-model.git) | Python 3.15.2, FastAPI 0.162, scikit-learn 1.9.1 Isolation Forest, behavioral anomaly inference |
| **Trust & Safety Portal** | [nagaraj602/marketplace-risk-frontend](https://github.com/nagaraj602/marketplace-risk-frontend.git) | React 19, Vite 8, Node 24, Express 5, Lucide-React, live review queue & signal injection simulator |
| **Jenkins Shared Libraries** | [nagaraj602/jenkins-sharedlibraries](https://github.com/nagaraj602/jenkins-sharedlibraries.git) | Reusable Groovy pipeline vars (`trivyScan`, `sonarAnalysis`, `dockerECRImagePush`, `kubernetesEKSHelmDeployEnv`) |

---

## 🚀 Tech Stack
- **Language**: Java 25 LTS (OpenJDK 25 / Eclipse Temurin 25)
- **Framework**: Spring Boot 4.1.2 (Spring Data JPA, Web, Actuator, Validation)
- **Databases**: PostgreSQL 17+ / AWS RDS, Embedded H2 for isolated testing
- **Cache & Velocity Counters**: Redis sliding window engine
- **Integration**: Python ML Anomaly Service (`/score`) via HTTP RestClient
- **Containerization**: Multi-stage Docker (`eclipse-temurin:25-noble` builder -> `eclipse-temurin:25-jre-noble` runner)
- **Orchestration**: Kubernetes + Helm v3/v4 charts with HPA autoscaling
- **CI/CD**: Jenkins Shared Library Multibranch Pipelines (SonarQube & Trivy pre-merge quality gates)

---

## ⚙️ Prerequisites

Before setting up the backend service, verify the following prerequisites:
- **Java Development Kit (JDK 25 LTS)**: OpenJDK 25 or Eclipse Temurin 25 (`java -version`, `javac -version`)
- **Apache Maven 3.9+**: Java build tool configured with `JAVA_HOME` pointing to Java 25 (`mvn -version`)
- **Docker Engine 24.0+**: (Optional for local JAR execution; required for running PostgreSQL/Redis or container builds)
- **Running Datastores**: PostgreSQL 17 on port `5432` and Redis 7.4 on port `6379` (or use Docker Compose from `marketplace-risk-platform`)
- **ML Anomaly Service**: `marketplace-risk-anomaly-model` running on port `8282` (optional; fallback heuristic active if offline)

---

## 🛠️ How to Setup & Run

### Local Execution
```bash
# 1. Ensure PostgreSQL (port 5432) and Redis (port 6379) are running
# 2. Run automated test suite
mvn clean test

# 3. Start Spring Boot Application (port 8281)
mvn spring-boot:run
```

### Docker Execution
```bash
docker build -t marketplace-risk-backend:latest .
docker run -p 8281:8281 \
  -e DB_URL=jdbc:postgresql://host.docker.internal:5432/marketplace_risk \
  -e MODEL_SERVER_URL=http://host.docker.internal:8282 \
  marketplace-risk-backend:latest
```

---

## 🎮 How to Operate the Backend

### 1. Ingest & Evaluate Seller Signals
```bash
curl -X POST http://localhost:8281/api/v1/risk/evaluate \
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
curl http://localhost:8281/api/v1/cases?pendingOnly=true
```

### 3. Resolve or Dismiss a Case
```bash
curl -X PATCH http://localhost:8281/api/v1/cases/1/resolve \
  -H "Content-Type: application/json" \
  -d '{
    "decision": "RESOLVED",
    "reason": "Analyst verified merchant identity documentation"
  }'
```

### 4. Query Real-Time Telemetry Metrics
```bash
curl http://localhost:8281/api/v1/risk/telemetry
```

---

## 🛡️ CI/CD & Jenkins Integration

This service utilizes Jenkins Multibranch Pipelines powered by [jenkins-sharedlibraries](https://github.com/nagaraj602/jenkins-sharedlibraries.git):
- **Pull Request Quality Gate (`Jenkinsfile.pr`)**: Automatically triggered via GitHub webhook; runs Java 25 unit tests (`mvn clean test`), SonarQube quality gate analysis (`sonar-server`), local Docker builds, and Trivy vulnerability scans.
- **Continuous Deployment (`Jenkinsfile`)**: Triggered on branch push/merge; builds production image, pushes to Amazon ECR, updates Helm values, and deploys to AWS EKS (`dev`, `uat`, or `prod`).
- **Webhook Integration**: Configured with the **Multibranch Scan Webhook Trigger** plugin using trigger token `marketplace-risk-backend-token`. See the [Complete Jenkins Integration Guide](https://github.com/nagaraj602/marketplace-risk-platform#%EF%B8%8F-jenkins-cicd-setup--multibranch-integration-guide).

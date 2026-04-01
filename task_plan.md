# Project Plan: AI-Driven Smart Job Scout (Java Edition)

## Goal
Build a microservices system to ingest JDs, analyze them using AI (LangChain4j), and store them in AWS (LocalStack).

## Tech Stack
- **Backend**: Java 17+, Spring Boot 3.x
- **AI**: LangChain4j + Ollama (Local)
- **Messaging**: Kafka (Docker)
- **Cloud (Mock)**: LocalStack (S3, DynamoDB, Lambda, API Gateway)
- **Infrastructure**: Docker Compose

## Phases

### Phase 1: Foundation & Cloud Mocking (Complete)
- [x] Set up Docker Compose for LocalStack.
- [x] Create `job-ingestion-service` (Spring Boot).
- [x] Integrate AWS SDK v2 for S3 & DynamoDB.
- [x] Test CRUD operations with LocalStack.

### Phase 2: Event-Driven with Kafka
- [ ] Add Kafka to Docker Compose.
- [ ] Implement Producer in `job-ingestion-service`.
- [ ] Create `job-analyzer-service` (Spring Boot).
- [ ] Implement Consumer to receive "JobCreatedEvent".

### Phase 3: AI Integration (LangChain4j)
- [ ] Set up Ollama local (or OpenAI API key).
- [ ] Integrate LangChain4j into `job-analyzer-service`.
- [ ] Create Prompt Template for JD analysis.
- [ ] Update Job status and AI scores in DynamoDB.

### Phase 4: Serverless & Gateway
- [ ] Configure Spring Cloud Gateway.
- [ ] Create a simple AWS Lambda (LocalStack) for notifications.
- [ ] Final E2E Testing.

## Progress Tracking
| Phase | Status | Started | Completed |
|-------|--------|---------|-----------|
| Phase 1: Foundation | in_progress | 2026-04-01 | |
| Phase 2: Kafka | todo | | |
| Phase 3: AI | todo | | |
| Phase 4: Serverless | todo | | |

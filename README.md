# Global Vendor Payment Orchestration System

**Stack:** Java | Spring Boot | Kafka | PostgreSQL

---

## Project Overview

This repository contains an end-to-end reference implementation for a **Global Vendor Payment Orchestration System**. It demonstrates a distributed architecture that handles:

* Receiving payment requests for vendors worldwide
* Currency conversion (FX) and rate lookup
* Orchestrating same-day settlement through workflows with retries, audit logging, and compensation logic
* Auditing and investigation tooling

The goal is not a complete production product but a clear, extensible blueprint you can run locally and extend.

---

## High-level Architecture

```
+---------------+      +---------------+      +------------------+
| API Gateway / | ---> | Orchestrator  | ---> | Payment Processor|
| REST Service  |      | (Saga)        |      | (Bank / PSP)     |
+---------------+      +---------------+      +------------------+
         |                     |                    |
         v                     v                    v
      Kafka                 Kafka Topics         External
      Topics                (commands/events)    FX / Banks
         |                     |                 (simulated)
         v                     v                    
+---------------+  +-----------------+  +-----------------+
| FX Service    |  | Settlement      |  | Audit / Query   |
| (rates, calc) |  | Service         |  | Service         |
+---------------+  +-----------------+  +-----------------+
         |                  |                    |
         v                  v                    v
      PostgreSQL (state, audit, rates, ledger)
```

Key components:

* **API**: Accepts payment requests and exposes investigation endpoints.
* **Orchestrator**: Implements a Saga-like workflow to coordinate steps (validate -> reserve funds -> convert currency -> send payment -> confirm settlement) with retry and compensation.
* **Payment Processor**: Simulates external PSP / bank interactions.
* **FX Service**: Provides conversion and manages rates.
* **Settlement Service**: Handles same-day settlement batching.
* **Audit/Query**: Read-model for investigations, audit logs, and human tooling.
* **Kafka**: Event backbone (topics for commands/events, retries, DLQ).
* **Postgres**: Persistent state storage and audit logs.

---

## Project Structure (suggested)

```
global-payment-orchestration/
├─ api-gateway/                # Spring Boot REST API
├─ orchestrator/               # Spring Boot service with saga orchestration
├─ fx-service/                 # FX rates & conversion microservice
├─ payment-processor/          # Simulated external payment provider
├─ settlement-service/         # Settlement orchestration and ledger
├─ audit-service/              # Audit / read model for investigation
├─ shared/                     # Shared DTOs, events, kafka utils
├─ infra/                      # docker-compose, flyway migrations
└─ README.md
```

---

## Important Kafka Topics

* `payments.requests` — incoming payment command
* `payments.commands` — intermediate commands between services
* `payments.events` — domain events (PaymentCreated, FXReserved, PaymentSent, PaymentSettled, PaymentFailed)
* `payments.retries` — retry queue
* `payments.dlq` — dead-letter queue
* `settlement.batch` — batches for settlement service

---

## PostgreSQL Schema (core tables)

```sql
-- payments table
CREATE TABLE payments (
  id UUID PRIMARY KEY,
  vendor_id VARCHAR(128) NOT NULL,
  amount NUMERIC(18,4) NOT NULL,
  currency CHAR(3) NOT NULL,
  target_currency CHAR(3) NOT NULL,
  status VARCHAR(50) NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
  metadata JSONB
);

-- audit logs
CREATE TABLE audit_log (
  id BIGSERIAL PRIMARY KEY,
  payment_id UUID,
  event_type VARCHAR(100),
  payload JSONB,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

-- fx_rates
CREATE TABLE fx_rates (
  id SERIAL PRIMARY KEY,
  base_currency CHAR(3) NOT NULL,
  target_currency CHAR(3) NOT NULL,
  rate NUMERIC(20,10) NOT NULL,
  valid_from TIMESTAMP WITH TIME ZONE DEFAULT now()
);

-- ledger
CREATE TABLE ledger (
  id BIGSERIAL PRIMARY KEY,
  payment_id UUID,
  entry_type VARCHAR(20), -- reserve/debit/credit/compensation
  amount NUMERIC(18,4),
  currency CHAR(3),
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);
```

---

## Sample Workflow (Orchestrator)

1. API receives `CreatePaymentRequest` and stores a `Payment` record with status `PENDING`.
2. Produce `PaymentCreated` event to `payments.events`.
3. Orchestrator listens and sends `ReserveFunds` command to payment processor.
4. On success, orchestrator requests FX conversion from `fx-service` (`FXReserved`).
5. Orchestrator submits `SendPayment` to payment processor (which calls external PSP).
6. On success, `PaymentSettled` event produced and status moves to `SETTLED`.
7. On any step failure, orchestrator retries with exponential backoff via `payments.retries`; after N retries, it issues compensation commands (`ReleaseFunds`) and sets status `FAILED` and writes extensive audit logs.

---

## Retry + Compensation Strategy

* **Retries**: Implemented using Kafka and scheduled retry topics. Each failed processing includes a `retry_count` and `next_attempt_at`. A background consumer reads `payments.retries` and re-publishes to processing topics when `next_attempt_at <= now()`.
* **Compensation**: For business actions with side effects (e.g., fund reservation), define a compensating action (e.g., release reservation). The orchestrator issues compensating commands when the saga cannot complete.

---

## Audit Logging

* All services write domain events to `audit_log` table for immutable history.
* The `audit-service` materializes events into a read-model optimized for investigation (search by vendor, payment id, date).
* Keep `payload` JSONB with structured data and minimal PII.

---

## Automation & Cost Reduction Features

* Automated retry and DLQ reduce manual operator intervention.
* Alerting on DLQ spikes (Prometheus/Grafana + alertmanager) for fast reaction.
* Investigation tooling: `audit-service` offers endpoints to fetch timeline and perform ad-hoc commands (re-run saga, request manual compensation).

---

## Running Locally (docker-compose)

Create `infra/docker-compose.yml` with Postgres, Kafka (using Bitnami or Confluent), Zookeeper, and Schema Registry if needed. Example (concise):

```yaml
version: '3.7'
services:
  zookeeper:
    image: bitnami/zookeeper:latest
    environment:
      - ALLOW_ANONYMOUS_LOGIN=yes
  kafka:
    image: bitnami/kafka:latest
    environment:
      - KAFKA_CFG_ZOOKEEPER_CONNECT=zookeeper:2181
      - ALLOW_PLAINTEXT_LISTENER=yes
    depends_on: [zookeeper]
  postgres:
    image: postgres:15
    environment:
      POSTGRES_DB: payments
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    ports:
      - "5432:5432"
```

Start infra: `docker-compose -f infra/docker-compose.yml up --remove-orphans --build`

---

## Spring Boot Service: Quick Start (Orchestrator)

**pom.xml**: include dependencies: `spring-boot-starter-web`, `spring-boot-starter-data-jpa`, `spring-kafka`, `spring-boot-starter-actuator`, `postgresql`, `flyway-core`.

**application.yml** (snippet):

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/payments
    username: postgres
    password: postgres
  jpa:
    hibernate:
      ddl-auto: validate
    properties:
      hibernate:
        format_sql: true

kafka:
  bootstrap-servers: localhost:9092
  consumer:
    group-id: orchestrator-group
```

**Payment Entity (JPA)**

```java
@Entity
@Table(name = "payments")
public class Payment {
    @Id
    private UUID id;
    private String vendorId;
    private BigDecimal amount;
    private String currency;
    private String targetCurrency;
    private String status;
    @Column(columnDefinition = "jsonb")
    private String metadata; // store as JSON string
    // getters/setters
}
```

**Kafka Producer (utility)**

```java
@Service
public class KafkaPublisher {
    private final KafkaTemplate<String, Object> template;
    public void publish(String topic, Object event) {
        template.send(topic, event);
    }
}
```

**Orchestrator Listener (simplified)**

```java
@KafkaListener(topics = "payments.events")
public void onPaymentEvent(ConsumerRecord<String, PaymentCreated> record) {
    PaymentCreated evt = record.value();
    // begin saga: send ReserveFunds command
    kafkaPublisher.publish("payments.commands", new ReserveFundsCommand(evt.getPaymentId(), ...));
}
```

**Retry handling**: when catching exceptions, create `RetryMessage` with `retryCount` and `nextAttemptAt` and publish to `payments.retries`.

---

## Example: Compensation Logic (pseudo)

```java
public void handleReserveFailure(UUID paymentId) {
  // if retries exhausted -> compensate
  kafkaPublisher.publish("payments.commands", new ReleaseFundsCommand(paymentId));
  paymentRepository.updateStatus(paymentId, "FAILED");
  auditService.log(paymentId, "Reservation failed after retries");
}
```

---

## FX Service Example Endpoint

`GET /fx/convert?from=USD&to=EUR&amount=100`

Response:

```json
{ "from":"USD","to":"EUR","rate":"0.92","convertedAmount":"92.00" }
```

Rates stored in `fx_rates` table and updated from an external feed (cron job). For demo, seed a few rates.

---

## Investigation Tooling (Audit Service)

Endpoints:

* `GET /audit/payment/{id}` — returns timeline of events
* `GET /audit/vendor/{vendorId}` — recent payments and statuses
* `POST /audit/replay/{id}` — re-run orchestration for a given payment id (careful: idempotency required)

Build a small React UI later to query these endpoints.

---

## Idempotency and Exactly-once Considerations

* Use `payment.id` as a natural idempotency key for API (client-provided or generated by API gateway).
* Kafka consumers must persist offsets only after successful processing. Use transactions where possible (Kafka + DB transactions via outbox pattern).
* Outbox pattern: write domain event into `outbox` table in same DB transaction as state change; a separate process reads outbox and publishes to Kafka.

---

## Sample Flyway Migration (V1__init.sql)

Place under `infra/db/migrations`.

```sql
-- create core tables (see schema above)
```

---

## Dockerfiles

Each microservice should have a simple `Dockerfile`:

```dockerfile
FROM eclipse-temurin:17-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

---

## Observability & Monitoring

* Expose `/actuator/prometheus` and scrape with Prometheus.
* Track metrics: events processed, retries, DLQ count, service latencies.
* Distributed tracing: use OpenTelemetry and propagate trace ids through Kafka headers.

---

## Security & Compliance

* Mask PII in audit logs. Keep sensitive data in encrypted columns if necessary.
* Use TLS for all external communications.
* RBAC on audit tooling; keep audit trails immutable.

---

## Tests

* Unit tests for conversion logic, compensation flows.
* Integration tests using Testcontainers for Postgres and Kafka.
* End-to-end test that runs docker-compose and executes a happy-path payment through to settlement.

---

## CI/CD

* Build and test each microservice separately.
* Use image tags and deployment to staging with smoke tests.
* Migrate DB via Flyway in CI before deployment.

---

## Next Steps & Extensions

* Add rate-limiting and circuit breakers for external PSPs
* Add multi-currency accounting with base currency ledgering
* Add dynamic routing by vendor region/PSP
* Add stronger SLA-driven settlement windows and batch optimization

---

## Included Example Code Snippets

This document has example code for the Orchestrator, Kafka publisher/listener, entity, and docker-compose. Clone into microservice modules and expand.

---

## License

MIT

---

If you want, I can now:

* scaffold the full multi-module Maven project (generate `pom.xml` files and starter classes),
* produce runnable Dockerfiles + docker-compose with working wiring,
* create a single microservice (fully implemented orchestrator or fx-service) as a detailed code file you can run locally, or
* generate Postgres Flyway migrations and Testcontainers integration tests.

Tell me which one you want next and I will generate the code/files directly into this workspace.

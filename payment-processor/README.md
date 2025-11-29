# Payment Processor — quick start (local / k8s)
---
## Build
```bash
mvn -DskipTests package
````

### Local with Docker Compose
1. Start infra and service:

```bash
docker-compose up --build
```
2. Wait until service is healthy on http://localhost:8080/actuator/health.

### Run tests (unit + integration)
Integration tests use Testcontainers (ensure Docker running):

```bash
mvn verify
```
---
### Useful endpoints
Base: http://localhost:8080/api/v1/processor

#### 1) Reserve
```bash
curl -X POST http://localhost:8080/api/v1/processor/reserve \
  -H "Content-Type: application/json" \
  -d '{
    "paymentId":"p-123",
    "vendorId":"vendor-abc",
    "amount":100.00,
    "currency":"USD"
  }'
```

#### 2) Send (capture)
```bash
curl -X POST http://localhost:8080/api/v1/processor/send \
  -H "Content-Type: application/json" \
  -d '{
    "paymentId":"p-123",
    "amount":100.00,
    "currency":"USD"
  }'
```
#### 3) Release
```bash
curl -X POST http://localhost:8080/api/v1/processor/release \
  -H "Content-Type: application/json" \
  -d '{"paymentId":"p-123","reservationId":"<res-id>","reason":"compensate"}'
```

#### 4) Status
``` bash
Copy code
curl http://localhost:8080/api/v1/processor/status/p-123
```
#### 5) Admin: set failure mode
```bash
curl -X POST http://localhost:8080/api/v1/processor/admin/failure-mode \
  -H "Content-Type: application/json" \
  -d '{"mode":"latency","latencyMs":2000,"errorRate":0.0}'
```
#### 6) Simulate PSP webhook (async)
```bash
curl -X POST http://localhost:8080/api/v1/processor/webhook/callback \
  -H "Content-Type: application/json" \
  -d '{"paymentId":"p-123","status":"SUCCESS"}'
```
---
### Kubernetes
#### 1.Build & push image to your registry:

```bash
docker build -t your-registry/payment-processor:latest .
docker push your-registry/payment-processor:latest
```
#### 2. Apply manifests:

```bash
kubectl apply -f k8s/namespace-config.yaml
kubectl apply -f k8s/secret.yaml
kubectl apply -f k8s/deployment.yaml
kubectl apply -f k8s/service.yaml
kubectl apply -f k8s/hpa.yaml
```
#### 3. Access via cluster ingress / port-forward:

```bash
kubectl port-forward svc/payment-processor 8080:80 -n payment-processor
```
---
## Notes & TODOs
* Use an outbox pattern / transactional event publisher in production to guarantee DB -> Kafka atomicity.
* Replace plaintext secrets with sealed secrets / Vault.
* Enable TLS & mTLS for service-to-service calls.
* Add authentication (JWT or API key validation) on the REST endpoints.
* Configure proper Kafka topics with schemas (Confluent Schema Registry or equivalent).


---

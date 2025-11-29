# Audit Service


This is an initial implementation of the audit service described in the architecture notes. It listens to Kafka (payments.events), persists raw events into Postgres (jsonb payload), and exposes investigation APIs.


## How to run
1. Start Postgres and Kafka (Testcontainers or local)
2. Configure datasource and kafka in `application.yml`
3. Build: `mvn -DskipTests package`
4. Run: `java -jar target/audit-service-0.1.0.jar`


## Next steps
- Add schema migrations with Flyway (if desired)
- Harden error handling and retries
- Add security (authz/authn)
- Add paging projections / read-model builder
- Add observability (metrics/tracing)
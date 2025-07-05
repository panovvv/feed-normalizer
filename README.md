# Feed Normalizer

A Spring Boot service that ingests 3rd party provider feeds via REST API, normalizes them into internal message format, and publishes them to a queue. We follow a hexagonal (ports-and-adapters) architecture:

- **domain/**: core message types
- **application/**: use-case services (message processing) and ports (ie interfaces).
- **adapters/inbound/**: REST API, message normalizer implementations
- **adapters/outbound/**: Queue implementations

## Implementation notes:
- Could go with Jackson polymorphic deserialization to detect incoming msg type based on `msg_type` field and already get a proper request object type in controller, but JsonNode is more flexible (e.g. what if we want case-insensitive matching for `msg_type` going forward, or other ways of detecting message type not based on field value). The downside is obviously that Swagger can not infer request schema automatically and API 
schema has to be maintained manually. Can be a drag, but on the other hand those schemas don't seem to ever change. Anyway, it could be changed to polymorphic deserialization easily.


## 1) Running locally

### Prerequisites
- JDK 21+
- Docker & Docker Compose (optional)

### Via Gradle
From the project root:  
```shell
./gradlew bootRun
```

### Via Docker Compose
```shell
./gradlew clean build -x test # Needs to be run once to build a JAR
docker-compose up
```

Both methods will start the app on port 8080.


## 2) Deploying & Metrics

All Actuator endpoints (health, metrics, Prometheus, etc.) are exposed at:
http://localhost:8080/actuator. See the documentation for details on exposed metrics: https://docs.spring.io/spring-boot/reference/actuator/metrics.html.
Among others, the bundled metrics include response codes per endpoint, so that can be used to trigger 500 error alerts in Production, e.g. `increase(http_server_requests_seconds_count{status=~"5..", uri="/provider-alpha/feed"}[1m]) > 10`

We also have some custom metrics, see MicrometerMetrics et al.

Security is enabled:
- Basic auth with CSRF disabled
- Credentials are read from application.properties:
```
spring.security.user.name=<your-username>
spring.security.user.password=<your-password>
```

OpenAPI and Swagger UI are publicly accessible (no authentication required):
- OpenAPI JSON: http://localhost:8080/v3/api-docs
- Swagger UI: http://localhost:8080/swagger-ui.html


## 3) Contributing

### Tests

We got the following kinds of tests:

- **Unit tests**
- **Integration context tests** are marked with @SpringBootTest. Those bring up the full app context but nothing beside that.
- **REST API tests** extend `BaseRestApiTest` and make use of REST Assured to verify real API interactions with an app started on embedded server. 
- **Architecture Tests**: we use ArchUnit to enforce hexagonal boundaries via `HexArchitectureTest`:
  - **domain/** must not depend on Spring, Jackson, application, or adapters
  - **application/** must not depend on inbound or outbound adapters
  - **adapters/inbound/** must not call outbound adapters directly
  - **adapters/outbound/** must not depend on inbound adapters

Run all tests:
```shell
./gradlew check
```

Skip all tests:
```shell
./gradlew build -x test
```

### Code Formatting

We use Spotless to enforce consistent code style:

- Apply formatting: 
```shell
./gradlew spotlessApply
```
- Check formatting (should be ran by CI/CD pipeline):
```shell
./gradlew spotlessCheck  
```

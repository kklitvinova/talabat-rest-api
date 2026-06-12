# Talabat Order REST API

RESTful Web Service for managing food delivery orders based on the Talabat dataset.

## Author
**Karyna Litvinova** — Full Stack Developer + QA + Business Analyst  
VIKO EIF, Group PI24E, 2026

## Technology Stack
- Java 21 + Spring Boot 3.2
- JAX-RS (Jersey)
- Spring Security + JWT
- H2 In-Memory Database
- Swagger UI (OpenAPI 3.0)
- React.js (Frontend)
- Cucumber BDD Tests
- JUnit 5

## Richardson Maturity Level
This API conforms to **Level 3** (HATEOAS) — every response includes `_links` with `self` and `collection` hypermedia links.

## Getting Started

### Prerequisites
- Java 21+
- Maven 3.8+
- Node.js 20+ (for frontend)

### Run Backend
```bash
mvn spring-boot:run
```
Or run `RestApplication.java` in IntelliJ.

Backend starts on: `http://localhost:8080`

### Run Frontend
```bash
cd frontend
npm install
npm start
```
Frontend starts on: `http://localhost:3000`

## API Endpoints

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | /api/auth/login | Manager login | Public |
| GET | /api/orders | Get all orders | Bearer JWT |
| GET | /api/orders/{id} | Get order by ID | Bearer JWT |
| GET | /api/orders/city/{city} | Filter by city | Bearer JWT |
| GET | /api/orders/status/{delivered} | Filter by status | Bearer JWT |
| GET | /api/orders/payment/{method} | Filter by payment | Bearer JWT |
| POST | /api/orders | Create order | Bearer JWT |
| PUT | /api/orders/{id} | Update order | Bearer JWT |
| DELETE | /api/orders/{id} | Delete order | Bearer JWT |

## Authentication

Login with manager credentials:
- **Email:** manager@talabat.com
- **Password:** manager123

Returns JWT token. Use it in all requests:
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtYW5hZ2VyQHRhbGFiYXQuY29tIiwiaWF0IjoxNzgxMTg3NDcyLCJleHAiOjE3ODEyNzM4NzJ9.FR1nmpPwewwV32gg_d2q20DOPPdU1rCnjqbPh8tMIKY
## Documentation
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- H2 Console: `http://localhost:8080/h2-console`
    - JDBC URL: `jdbc:h2:mem:talabatdb`
    - Username: `sa`
    - Password: *(empty)*

## Testing
- Unit tests: run all tests in IntelliJ
- Cucumber BDD: run `CucumberRunnerTest.java`
- 18 BDD scenarios covering auth, CRUD and filters

## SOLID
- **SRP** — OrderService handles business logic separately from REST layer
- **OCP** — Resources extendable without modification
- **DIP** — Spring dependency injection throughout


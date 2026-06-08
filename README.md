# EcomMed

Spring Boot REST API for a medical e-commerce platform — JWT auth, shopping cart, orders, inventory, and Kafka-driven async processing.

![Java](https://img.shields.io/badge/Java-21-blue) ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.5-brightgreen) ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-gray) ![Kafka](https://img.shields.io/badge/Apache%20Kafka-black) ![Flyway](https://img.shields.io/badge/Flyway-red)

---

## Features

- JWT-based auth with role/permission access control (ADMIN & CUSTOMER)
- User registration & login; admin auto-seeded on startup
- Product & category management with dynamic search and filters
- Shopping cart with real-time stock validation
- Order placement from cart
- Kafka async: inventory deduction, invoice generation, email notifications
- OpenAPI / Swagger UI with JWT bearer support
- Request logging filter & health check endpoint

---

## Architecture

Layered monolith with event-driven async integration.

```
Client → LoggingFilter → JwtFilter → Controller → Service → Repository → PostgreSQL
                                                        ↓
                                              Spring Events → Kafka → Consumers
```

| Topic | Trigger | Consumers |
|---|---|---|
| `user-registered` | On signup | Welcome email |
| `product-added` | On product create | Create inventory |
| `order-placed` | On order place | Reduce stock · Send email · Generate invoice |

---

## API Endpoints

Base URL: `http://localhost:8080/api/v1` · Swagger: `/swagger-ui.html`

| Method | Path | Auth | Description |
|---|---|---|---|
| `POST` | `/auth/register` | Public | Register customer |
| `POST` | `/auth/login` | Public | Login, returns JWT |
| `GET` | `/users` | Admin | List users (paginated) |
| `POST` | `/products` | Admin | Create product |
| `GET` | `/products` | Customer | Search products (name, price range) |
| `POST` | `/category` | Admin | Create category |
| `GET` | `/category` | Customer | List categories |
| `POST` | `/cart` | Customer | Add item to cart |
| `DELETE` | `/cart` | Customer | Remove item from cart |
| `GET` | `/cart` | Customer | View cart |
| `POST` | `/orders` | Customer | Place order |
| `GET` | `/orders` | Admin | List orders (paginated, date filters) |
| `PUT` | `/inventory/update-stock` | Admin | Increase stock |
| `DELETE` | `/inventory/delete-stock` | Admin | Decrease stock |
| `GET` | `/health` | Public | Health check |

---

## Getting Started

### Prerequisites

- Java 21
- PostgreSQL
- Docker (for Kafka)

### 1. Start Kafka

```bash
docker compose up -d
```

### 2. Create `application-dev.yml`

Create `src/main/resources/application-dev.yml` (gitignored):

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/ecommed
    username: your_db_user
    password: your_db_password

kafka:
  bootstrap-servers: localhost:9092

utils:
  jwt:
    secret: your_base64_secret
    expiry: 60  # minutes
  admin:
    name: Admin
    username: admin@gmail.com
    password: admin123

send:
  from: your_email@gmail.com
```

### 3. Run

```bash
./gradlew bootRun
```

API available at `http://localhost:8080/api/v1`.

---

## Security

- Stateless JWT sessions
- BCrypt password encoding (strength 11)
- URL-based + `@PreAuthorize` method-level authorization
- Custom JSON 401 response
- CORS: `localhost:3000`, `localhost:5173`, `127.0.0.1:5500`

---

## Database

All entities extend `BaseEntity` (UUID PK, `createdAt`, `updatedAt`). Schema managed by **Flyway** — migrations live in `src/main/resources/db/migration`.

`User` · `Category` · `Product` · `Inventory` · `Cart` · `CartItem` · `Order` · `OrderItem` · `Invoice`

---

## Kafka Error Handling

All consumers share a `DefaultErrorHandler` with a fixed backoff and a dead-letter topic:

| Setting | Value |
|---|---|
| Retry attempts | 3 |
| Retry interval | 2000ms (fixed) |
| Dead-letter topic | `central-dlt-topic` (same partition as origin) |

Failed messages are retried 3 times before being published to `central-dlt-topic`.

---

## Notes

- Only Kafka is Dockerized — PostgreSQL and the app run locally
- `application-dev.yml` is gitignored — create it before running

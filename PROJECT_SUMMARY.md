# EcomMed — Structured Project Summary

## Project Name and Purpose

| Item | Detail |
|------|--------|
| **Name** | EcomMed (`rootProject.name`, `spring.application.name`) |
| **Purpose** | Backend REST API for a medical e-commerce platform: user auth, product/catalog management, cart, orders, inventory, and async post-order processing via Kafka |

---

## Short Project Description

EcomMed is a Spring Boot 3 monolith that exposes a versioned REST API (`/api/v1`) for a medical e-commerce system. It uses JWT-based security with role/permission checks, PostgreSQL via JPA, and Apache Kafka for asynchronous workflows (inventory updates, invoicing, email notifications). Swagger documents the API as *"Medical Ecommerce System API"*.

---

## Tech Stack

| Category | Technology |
|----------|------------|
| Language | Java 21 |
| Framework | Spring Boot 3.3.5 |
| Build | Gradle 9.5.1 |
| Web | Spring Web, Spring Validation |
| Security | Spring Security, JJWT 0.13.0, BCrypt |
| Persistence | Spring Data JPA, PostgreSQL, Hibernate |
| Messaging | Spring Kafka, Apache Kafka |
| Mapping | MapStruct 1.6.3 |
| API Docs | Springdoc OpenAPI 2.6.0 |
| Email | Spring Mail, Thymeleaf |
| Utilities | Lombok |
| Monitoring | Spring Actuator |
| Testing | JUnit 5, Spring Boot Test, Spring Security Test |
| Containerization | Docker Compose (Kafka only) |

**Not present in codebase:** Flyway, application Dockerfile, payment processing.

---

## Architecture

| Aspect | Implementation |
|--------|----------------|
| **Style** | Layered monolith |
| **Layers** | Controller → Service → Repository → Database |
| **Cross-cutting** | Security filters, global exception handler, logging filter |
| **Events** | Spring `ApplicationEventPublisher` → `@TransactionalEventListener` listeners → Kafka → `@KafkaListener` consumers |
| **Async** | `@EnableAsync` on main application |
| **Auditing** | `@EnableJpaAuditing` on `BaseEntity` |

**Request flow:**

```
Client → LoggingFilter → JwtFilter → SecurityFilterChain → Controller → Service → Repository → PostgreSQL
```

**Context path:** `/api/v1` (port `8080`)

---

## Main Features

1. **User registration** — Creates `CUSTOMER` users; publishes `UserRegisteredEvent`
2. **JWT login** — Authenticates via Spring Security; returns JWT token
3. **Admin seeding** — `AdminLoader` creates admin from `utils.admin` config on startup
4. **User listing** — Paginated search by name/email (admin)
5. **Category management** — Create and list categories
6. **Product management** — Create products; search with filters; triggers inventory creation via Kafka
7. **Inventory management** — Auto-create on product add; manual increase/decrease APIs
8. **Cart management** — Add, remove, view cart with stock validation
9. **Order placement** — Converts cart to order; clears cart; publishes `OrderPlacedEvent`
10. **Async order processing** — Kafka consumers: stock reduction, invoice generation, order email
11. **Registration email** — Welcome email via Kafka consumer
12. **Request logging** — `LoggingFilter` writes URIs to `filterlog.txt`
13. **Health endpoint** — Simple status check
14. **OpenAPI/Swagger** — JWT bearer documented

`ImageUploadService` exists but is not wired to any controller.

---

## Database Entities and Purpose

All entities extend `BaseEntity` (UUID `id`, `createdAt`, `updatedAt`).

| Entity | Table | Purpose |
|--------|-------|---------|
| **BaseEntity** | — (mapped superclass) | Shared PK and audit timestamps |
| **User** | `users` | Registered users (name, email, password, role); unique email |
| **Category** | `categories` | Product categories; unique name |
| **Product** | `products` | Sellable items (name, price); linked to category |
| **Inventory** | `inventories` | Stock quantity per product (OneToOne with Product) |
| **Cart** | `carts` | Per-user shopping cart with totals |
| **CartItem** | `cart_item` | Line items in a cart |
| **Order** | `orders` | Placed orders with status and total |
| **OrderItem** | `order_item` | Line items in an order |
| **Invoice** | `invoice` | Receipt generated after order placement |

**Key relationships:** User ↔ Cart (OneToOne), User ↔ Order (OneToOne), Product ↔ Inventory (OneToOne), Category → Products (OneToMany), Cart/Order → Items (OneToMany).

Schema is managed via JPA/Hibernate. No Flyway migrations. `application-dev.yml` (database/JWT/Kafka config) is gitignored.

---

## REST APIs (Grouped by Controller)

Base: `http://<host>:8080/api/v1`

### AuthController — `/auth`

| Method | Path | Description |
|--------|------|-------------|
| POST | `/auth/register` | Register customer |
| POST | `/auth/login` | Login; returns JWT |

### UserController — `/users`

| Method | Path | Description |
|--------|------|-------------|
| GET | `/users` | Paginated users; filters: `name`, `email`; pagination: `pageNo`, `pageSize`, `sortBy`, `sortDirection` |

### ProductController — `/products`

| Method | Path | Description |
|--------|------|-------------|
| POST | `/products` | Create product |
| GET | `/products` | List/search products; filters: `name`, `minPrice`, `maxPrice` + pagination |

### CategoryController — `/category`

| Method | Path | Description |
|--------|------|-------------|
| GET | `/category` | List categories (paginated) |
| POST | `/category` | Create category |

### CartController — `/cart`

| Method | Path | Description |
|--------|------|-------------|
| POST | `/cart` | Add to cart (`productId`, `quantity`) |
| DELETE | `/cart` | Remove from cart (`productId`, `quantity`) |
| GET | `/cart` | Get current user's cart |

### OrderController — `/orders`

| Method | Path | Description |
|--------|------|-------------|
| POST | `/orders` | Place order from cart |
| GET | `/orders` | Paginated orders with date filters (`startDate`, `endDate`) |
| GET | `/orders` | Get logged-in user's orders — duplicate `@GetMapping` in source |

### InventoryController — `/inventory`

| Method | Path | Description |
|--------|------|-------------|
| PUT | `/inventory/update-stock` | Increase stock |
| DELETE | `/inventory/delete-stock` | Decrease stock |

### HealthController — `/health`

| Method | Path | Description |
|--------|------|-------------|
| GET | `/health` | Returns `"Tomcat is running in 8080"` |

### Swagger (permitted without auth)

`/swagger-ui.html`, `/swagger-ui/**`, `/v3/api-docs/**`, `/swagger-resources/**`, `/webjars/**`

---

## Roles and Permissions

### Roles

| Role | Permissions |
|------|-------------|
| **ADMIN** | All `Permission` enum values |
| **CUSTOMER** | `product:get`, `order:place`, `order:fetch`, `cart:add`, `cart:update`, `cart:delete`, `cart:get`, `category:get` |

No `VENDOR` role exists despite vendor permissions being defined.

### Permissions (20 total)

| Permission | Name |
|------------|------|
| VENDOR_CREATE | `vendor:create` |
| VENDOR_UPDATE | `vendor:update` |
| VENDOR_DELETE | `vendor:delete` |
| PRODUCT_ADD | `product:add` |
| PRODUCT_UPDATE | `product:update` |
| PRODUCT_DELETE | `product:delete` |
| PRODUCT_GET | `product:get` |
| USERS_GET | `users:get` |
| CATEGORY_ADD | `category:add` |
| CATEGORY_DELETE | `category:delete` |
| CATEGORY_GET | `category:get` |
| ORDER_PLACE | `order:place` |
| ORDER_FETCH | `order:fetch` |
| CART_ADD | `cart:add` |
| CART_UPDATE | `cart:update` |
| CART_DELETE | `cart:delete` |
| CART_GET | `cart:get` |
| INVENTORY_UPDATE | `inventory:update` |
| INVENTORY_DELETE | `inventory:delete` |
| REMOVE_FROM_CART | `remove:from:cart` |

---

## Authentication and Authorization Flow

### Authentication (JWT)

1. **Login:** `POST /auth/login` → `AuthenticationManager` validates credentials via `DaoAuthenticationProvider` and `CustomUserDetailService` (loads user by email).
2. **Token:** `JwtService` builds JWT (HMAC, Base64 secret from `utils.jwt.secret`) with claims: `subject` (email), `id`, `name`, `role`, `username`; expiry from `utils.jwt.expiry` (minutes).
3. **Per-request:** `JwtFilter` reads `Authorization: Bearer <token>`, validates expiry and username, loads `UserPrincipal`, sets `SecurityContext`.
4. **Password encoding:** `BCryptPasswordEncoder(11)`.
5. **Sessions:** Stateless (`SessionCreationPolicy.STATELESS`).

### Authorization

- **URL-based:** `SecurityConfig` maps HTTP methods/paths to `hasAuthority(...)` checks.
- **Method-based:** `@PreAuthorize` on `OrderService.placeOrder()` and `CartService.addToCart()`.
- **Public:** `/auth/**`, Swagger paths.
- **Unauthorized:** `CustomAuthenticationEntryPoint` returns JSON 401.
- **CORS:** Allowed origins: `localhost:3000`, `localhost:5173`, `127.0.0.1:5500`.

---

## Kafka: Topics, Producers, Consumers, Event Flow

### Topics (`KafkaTopics`)

| Topic | Partitions | Replicas |
|-------|------------|----------|
| `order-placed` | 2 | 1 |
| `user-registered` | 2 | 1 |
| `product-added` | 2 | 1 |

### Events

| Event | Payload |
|-------|---------|
| `UserRegisteredEvent` | `id`, `name`, `email` |
| `ProductCreatedEvent` | `productId`, `quantity` |
| `OrderPlacedEvent` | `orderId`, `orderStatus`, `email`, `placedAt`, `userId`, `List<OrderItemEvent>` (`productId`, `quantity`) |

### Producers (in-process → Kafka bridge)

| Listener | Trigger | Topic | Phase |
|----------|---------|-------|-------|
| `UserCreatedEventListener` | `UserService.createUser()` | `user-registered` | `AFTER_COMMIT`, `@Async` |
| `ProductAddedEventListener` | `ProductService.createProduct()` | `product-added` | `@TransactionalEventListener` |
| `OrderPlacedEventListener` | `OrderService.placeOrder()` | `order-placed` | `@TransactionalEventListener`, `@Async` |

Serialization: `StringSerializer` key, `JsonSerializer` value. Bootstrap: `${kafka.bootstrap-servers}`.

### Consumers

| Consumer | Topic | Group ID | Action |
|----------|-------|----------|--------|
| `UserRegistrationEventConsumer` | `user-registered` | `notification-group` | Send welcome email |
| `ProductAddEventConsumer` | `product-added` | `decrease-stock` | Create inventory record |
| `OrderEventConsumer.decreaseInventory` | `order-placed` | `inventory-group` | Decrease stock per order item |
| `OrderEventConsumer.sendNotification` | `order-placed` | `notification-group` | Send order-placed email |
| `OrderEventConsumer.generateReceipt` | `order-placed` | `invoice-group` | Generate invoice |

### Event flow

**Registration:**

```
Register → UserService → UserRegisteredEvent → UserCreatedEventListener → Kafka (user-registered) → Email
```

**Product creation:**

```
Create Product → ProductCreatedEvent → ProductAddedEventListener → Kafka (product-added) → InventoryService.createInventory()
```

**Order placement:**

```
Place Order → OrderPlacedEvent → OrderPlacedEventListener → Kafka (order-placed)
  ├─ inventory-group → decrease stock
  ├─ notification-group → order email
  └─ invoice-group → generate invoice
```

**Retry/DLT:** Not configured. Producer failures logged via `whenComplete()`; consumer email errors caught and logged.

---

## Validation Rules

### Bean Validation

| DTO | Rules |
|-----|-------|
| `RegistrationRequest` | `name` @NotBlank; `email` @EmailValidator + @NotBlank; `password` @NotBlank |
| `LoginRequest` | `email` @EmailValidator; `password` @NotBlank |
| `ProductCreateRequest` | `name` @NotBlank; `price` @NotNull + @DecimalMin(0.01); `quantity` @NotNull + @Min(1); `categoryId` @NotNull |

### Custom validator

- **`@EmailValidator`:** Email must end with `@gmail.com` (`EmailValidatorConstraint`)

### Business validations (imperative)

| Check | Location |
|-------|----------|
| Email uniqueness | `UserService` |
| Product name uniqueness | `ProductService` |
| Category name uniqueness | `CategoryService` |
| Quantity ≥ 1 | `ProductService`, `InventoryService` |
| Stock availability | `CartService`, `InventoryService.decreaseStock()` |
| Invoice idempotency | `InvoiceService.existsByOrderId()` |

`AddToCartRequest`, `RemoveFromCartRequest`, `CategoryRequest`, `UpdateStockRequest` have no field-level validation annotations.

---

## Exception Handling

### Custom exceptions

| Exception | Usage |
|-----------|-------|
| `BusinessException` | Business rule violations (duplicates, invalid quantity, insufficient stock) |
| `ResourceNotFoundException` | Entity not found |

### GlobalExceptionHandler

| Handler | Status | Response |
|---------|--------|----------|
| `MethodArgumentNotValidException` | 400 | `{ success: false, message: "Validation failed", data: {field: message} }` |
| `Exception` (catch-all) | 400 if `BusinessException`, else 500 | `ApiResponse.error(message)` |

Other throws: `RuntimeException` on invalid login (`AuthService`); `UsernameNotFoundException` (`CustomUserDetailService`); `RuntimeException` on image upload failure (`ImageUploadService`).

---

## Filtering and Search

| Area | Mechanism | Filters |
|------|-----------|---------|
| **Products** | `ProductSpecification` + `JpaSpecificationExecutor` | `name` (product or category name, case-insensitive LIKE), `minPrice`, `maxPrice` (between/gte/lte) |
| **Orders** | `OrderSpecification` + `JpaSpecificationExecutor` | `startDate`, `endDate` on `createdAt` |
| **Users** | JPQL in `UserRepository.findPagedUsers()` | `name`, `email` (case-insensitive LIKE) |

Pagination via `PageableRequest` (`pageNo`, `pageSize`, `sortBy`, `sortDirection`; defaults: pageSize 10, sortBy `id`, direction `ASC`).

---

## Docker Support

| Item | Status |
|------|--------|
| **Dockerfile** | Not present |
| **docker-compose.yml** | Kafka only |

**Kafka service:**

- Image: `apache/kafka:latest`
- Port: `9092` (PLAINTEXT, `localhost:9092`)
- Internal controller: `9093`
- KRaft mode, single broker/controller node
- Volume: `kafka-data`

PostgreSQL and the Spring Boot app are not containerized in this repo.

---

## Configuration Details

### Committed (`application.yaml`)

- Server port: `8080`
- Context path: `/api/v1`
- Active profile: `dev`
- Multipart: max file `10MB`, max request `15MB`
- Spring Security default user: `root` / `root`

### Referenced in code (expected in gitignored `application-dev.yml`)

| Property | Used by |
|----------|---------|
| `kafka.bootstrap-servers` | Kafka producer/consumer config |
| `utils.jwt.secret`, `utils.jwt.expiry` | JWT generation/validation |
| `utils.admin.name/username/password` | `AdminLoader` |
| `send.from` | Email services |
| `file.upload-dir`, `app.base-url` | `ImageUploadService` |
| `spring.datasource.*` | Implied by JPA + PostgreSQL dependency |

---

## Notable Design Patterns and Best Practices

| Pattern / Practice | Where |
|--------------------|-------|
| **Layered architecture** | controller / service / repository separation |
| **DTO pattern** | Request/response DTOs separate from entities |
| **Repository pattern** | Spring Data JPA repositories |
| **Mapper pattern** | MapStruct (`ProductMapper`, etc.) + manual mappers |
| **RBAC with fine-grained permissions** | `Role` + `Permission` enums → `GrantedAuthority` |
| **JWT stateless auth** | `JwtFilter` + `JwtService` |
| **Event-driven architecture** | Spring events bridged to Kafka |
| **Transactional event listeners** | Publish to Kafka within/after transactions |
| **Multiple consumer groups** | Parallel order side-effects on one topic |
| **JPA Specification API** | Dynamic product/order queries |
| **Global exception handling** | `@RestControllerAdvice` |
| **API response wrapper** | `ApiResponse<T>` with success/error helpers |
| **JPA auditing** | `@CreationTimestamp`, `@UpdateTimestamp`, UUID PKs |
| **CommandLineRunner seeding** | `AdminLoader` for bootstrap admin |
| **OpenAPI security scheme** | Bearer JWT in Swagger |
| **Idempotent invoice creation** | `existsByOrderId()` guard |
| **Custom servlet filters** | JWT auth + request logging |
| **Async processing** | `@EnableAsync` on event listeners |

---

## README_DATA

```
Project Name:
EcomMed

Description:
Spring Boot medical e-commerce REST API with JWT security, shopping cart, order placement, inventory management, and Kafka-driven async processing for stock reduction, invoicing, and email notifications.

Features:
- User registration and JWT login
- Admin auto-seeding on startup
- Product create and search with dynamic filters
- Category create and list
- Inventory create (via Kafka), increase, and decrease
- Shopping cart add, remove, and view with stock validation
- Order placement from cart
- Kafka events for registration, product creation, and order placement
- Async inventory deduction, invoice generation, and email notifications
- Swagger/OpenAPI with JWT bearer auth
- Request logging filter
- Health check endpoint

Tech Stack:
Java 21, Spring Boot 3.3.5, Gradle 9.5.1, Spring Security, Spring Data JPA, PostgreSQL, Apache Kafka, Spring Kafka, JWT (JJWT 0.13.0), Lombok, MapStruct, Jakarta Validation, Springdoc OpenAPI, Thymeleaf, JavaMail, Spring Actuator, JUnit 5

Architecture:
Layered monolith with event-driven integration (Spring Application Events → Kafka → multiple consumer groups)

Entities:
User, Category, Product, Inventory, Cart, CartItem, Order, OrderItem, Invoice, BaseEntity

Roles:
ADMIN (all permissions), CUSTOMER (product:get, order:place, order:fetch, cart:add, cart:update, cart:delete, cart:get, category:get)

Permissions:
vendor:create, vendor:update, vendor:delete, product:add, product:update, product:delete, product:get, users:get, category:add, category:delete, category:get, order:place, order:fetch, cart:add, cart:update, cart:delete, cart:get, inventory:update, inventory:delete, remove:from:cart

APIs:
POST /api/v1/auth/register
POST /api/v1/auth/login
GET  /api/v1/users
POST /api/v1/products
GET  /api/v1/products
GET  /api/v1/category
POST /api/v1/category
POST /api/v1/cart
DELETE /api/v1/cart
GET  /api/v1/cart
POST /api/v1/orders
GET  /api/v1/orders
PUT  /api/v1/inventory/update-stock
DELETE /api/v1/inventory/delete-stock
GET  /api/v1/health

Kafka Topics:
order-placed (producers: OrderPlacedEventListener; consumers: inventory-group, notification-group, invoice-group)
user-registered (producer: UserCreatedEventListener; consumer: notification-group)
product-added (producer: ProductAddedEventListener; consumer: decrease-stock)

Security Features:
JWT bearer authentication (stateless), BCrypt password encoding (strength 11), role-based access control with permission-granular authorities, URL-based and method-level (@PreAuthorize) authorization, custom JWT filter, custom 401 JSON entry point, CORS for localhost frontends, public /auth/** and Swagger endpoints, admin bootstrap via CommandLineRunner
```

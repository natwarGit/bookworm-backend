# 📚 Book Worm — E-Commerce Backend

> **IBM BOB Capstone Project** | Spring Boot 3.3.2 | Java 17 | PostgreSQL

A complete REST API backend for an E-Bookstore platform supporting browse, cart, checkout, payment, and order management.

---

## 🚀 Quick Start

### Prerequisites

| Tool | Version | Download |
|---|---|---|
| Java JDK | 17+ | https://adoptium.net |
| Apache Maven | 3.8+ | https://maven.apache.org |
| PostgreSQL | 14+ | https://www.postgresql.org/download |
| Postman | Latest | https://www.postman.com/downloads |

---

## ⚙️ Step 1 — Database Setup

1. Open **pgAdmin 4**
2. Create a new database:
   - Right-click **Databases** → **Create** → **Database**
   - Name: `bookworm_db`
   - Click **Save**

> Tables are created **automatically** by Hibernate on first startup. No manual SQL needed.

---

## 🔧 Step 2 — Configure Application

Open `src/main/resources/application.properties` and verify:

```properties
spring.datasource.url=jdbc:postgresql://127.0.0.1:5432/bookworm_db
spring.datasource.username=${DB_USER:postgres}
spring.datasource.password=${DB_PASSWORD:######}
```



---

## ▶️ Step 3 — Run the Application

Open **PowerShell** in the project root directory:

```powershell
cd C:\Users\NatwarKumar\.bob\playground

# Clean build and start
mvn clean spring-boot:run
```

### ✅ Application Started Successfully When You See:
```
Started BookWormApplication in X.XXX seconds
Tomcat started on port 8080 (http) with context path ''
```

---

## 🌐 Step 4 — Access the Application

| URL | Purpose |
|---|---|
| http://127.0.0.1:8080/swagger-ui.html | Interactive API Documentation |
| http://127.0.0.1:8080/v3/api-docs | OpenAPI JSON spec |
| http://127.0.0.1:8080/api/v1/books | Books catalogue (test in browser) |

---

## 🧪 Step 5 — Test All APIs (Postman)

### Demo Credentials
```
Email    : customer@bookworm.com
Password : password123
User ID  : 44444444-4444-4444-4444-444444444401
```

### Base URL
```
http://127.0.0.1:8080/api/v1
```

---

### 🔐 1. Login
```
POST /auth/login
Content-Type: application/json

{
  "email": "customer@bookworm.com",
  "password": "password123"
}
```
**Expected:** `200 OK` — returns `userId`, `giftPointsBalance: 500`

---

### 📚 2. Browse Books
```
GET /books
GET /books/bestsellers
GET /books/new-launches
GET /books?format=PAPERBACK
GET /books?query=Art
```
**Expected:** `200 OK` — list of books with prices and delivery dates

---

### 📍 3. Get Delivery Addresses
```
GET /addresses?userId=44444444-4444-4444-4444-444444444401
```
**Expected:** `200 OK` — Home (Mumbai) and Work (Bangalore) addresses

---

### 🛒 4. Add to Cart
```
POST /cart?userId=44444444-4444-4444-4444-444444444401
Content-Type: application/json

{
  "bookId": "33333333-3333-3333-3333-333333333307",
  "quantity": 1
}
```
**Expected:** `200 OK` — cart with Joy of Minimalism (₹149)

Add second book:
```json
{
  "bookId": "33333333-3333-3333-3333-333333333303",
  "quantity": 1
}
```
**Expected:** Cart total = ₹508

---

### 🛒 5. View Cart
```
GET /cart?userId=44444444-4444-4444-4444-444444444401
```
**Expected:** 2 items, totalAmount: 508.00

---

### 📦 6. Place Order
```
POST /orders?userId=44444444-4444-4444-4444-444444444401
Content-Type: application/json

{
  "shippingAddressId": "55555555-5555-5555-5555-555555555501",
  "paymentMethod": "CREDIT_CARD",
  "redeemGiftPoints": 0
}
```
**Expected:** `201 Created` — `status: PLACED`, `eligibleForCancellation: true`

> 📌 Copy the `id` field from the response for the next step.

---

### 💳 7. Process Payment
```
POST /payments
Content-Type: application/json

{
  "orderId": "PASTE-ORDER-ID-FROM-STEP-6",
  "paymentMethod": "CREDIT_CARD"
}
```
**Expected:** `200 OK`
```json
{
  "status": "SUCCESS",
  "transactionId": "TXN-...",
  "message": "Payment successful! Your purchase is confirmed. 🎉"
}
```

---

### 📜 8. Order History
```
GET /orders?userId=44444444-4444-4444-4444-444444444401
```
**Expected:** Order with status `PROCESSING` (updated after payment)

---

### ❌ 9. Cancel Order
```
POST /orders/{orderId}/cancel?userId=44444444-4444-4444-4444-444444444401
```
**Expected:** `200 OK` — `status: CANCELLED` (only within 48 hours of placing)

---

## 📋 All API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/v1/auth/login` | User login |
| GET | `/api/v1/books` | Browse & filter catalogue |
| GET | `/api/v1/books/{id}` | Book detail + related books |
| GET | `/api/v1/books/recommended` | Recommendations |
| GET | `/api/v1/books/bestsellers` | Bestsellers |
| GET | `/api/v1/books/new-launches` | New arrivals |
| GET | `/api/v1/categories` | All categories |
| GET | `/api/v1/addresses?userId=` | Get delivery addresses |
| POST | `/api/v1/addresses` | Add new address |
| DELETE | `/api/v1/addresses/{id}` | Delete address |
| GET | `/api/v1/cart?userId=` | View cart |
| POST | `/api/v1/cart?userId=` | Add to cart |
| DELETE | `/api/v1/cart?userId=` | Clear cart |
| POST | `/api/v1/orders?userId=` | Place order |
| GET | `/api/v1/orders?userId=` | Order history |
| POST | `/api/v1/orders/{id}/cancel` | Cancel order (48h) |
| POST | `/api/v1/payments` | Process payment |
| GET | `/api/v1/payments/order/{id}` | Payment status |

---

## 🗄️ Database Tables

Tables created automatically by Hibernate:

| Table | Description |
|---|---|
| `users` | User accounts with gift point balance |
| `categories` | Book categories (Romance, Mystery, etc.) |
| `authors` | Book authors |
| `books` | Book catalogue with stock and pricing |
| `carts` | Per-user shopping cart |
| `cart_items` | Individual items in cart |
| `orders` | Placed orders with 48hr cancellation |
| `order_items` | Books within each order |
| `payments` | Payment transactions |
| `addresses` | User delivery addresses |

---

## 🏗️ Project Structure

```
src/main/java/com/bookworm/
├── BookWormApplication.java
├── controller/      AuthController, BookController, CartController,
│                    OrderController, PaymentController, AddressController,
│                    CategoryController
├── service/         AuthService, BookService, CartService,
│                    OrderService, PaymentService, AddressService
├── repository/      8 Spring Data JPA repositories
├── entity/          10 JPA entities (mapped to DB tables)
├── dto/             13 Request/Response records
└── exception/       GlobalExceptionHandler, BusinessException,
                     ResourceNotFoundException
```

---

## ❗ Troubleshooting

| Problem | Fix |
|---|---|
| Port 8080 in use | Add `server.port=8081` to application.properties |
| DB connection failed | Check PostgreSQL is running. Verify password in application.properties |
| Tables not created | Check `spring.jpa.hibernate.ddl-auto=update` is set |
| No books in response | Run data.sql manually in pgAdmin Query Tool |
| `lower(bytea)` error | Already fixed — uses safe JPQL `LIKE %:query%` syntax |

---

## 👨‍💻 IDE :  IBM BOB

 **IBM BOB** (AI-Assisted Agentic IDE) as part of the Applied AI Specialist Capstone Project.

## Github has two branches : 
---

*Book Worm Backend — IBM BOB Capstone Project*

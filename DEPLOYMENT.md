# 🚀 Deployment Guide — Book Worm E-Commerce Backend

## Option 1 — Run Locally (Current Setup) ✅ Already Working

```powershell
cd C:\Users\NatwarKumar\.bob\playground
mvn clean spring-boot:run
```
Access: http://127.0.0.1:8080/swagger-ui.html

---

## Option 2 — Docker Deployment (Local Container)

### Prerequisites
- Install Docker Desktop: https://www.docker.com/products/docker-desktop/

### Step 1 — Build and Start with Docker Compose
```bash
cd C:\Users\NatwarKumar\.bob\playground

# Start both PostgreSQL + Spring Boot app in containers
docker-compose up --build
```

### Step 2 — Access the Application
```
Swagger UI : http://localhost:8080/swagger-ui.html
API Base   : http://localhost:8080/api/v1
```

### Step 3 — Stop the Application
```bash
docker-compose down
```

### Step 4 — Stop and Remove Data
```bash
docker-compose down -v
```

---

## Option 3 — Build JAR and Run Anywhere

### Step 1 — Build the JAR
```powershell
mvn clean package -DskipTests
```

### Step 2 — Run the JAR
```powershell
java -jar target/bookworm-backend-1.0.0-SNAPSHOT.jar
```

### Step 3 — Run with Custom DB (environment variables)
```powershell
$env:DB_HOST="your-db-host"
$env:DB_PASSWORD="your-password"
java -jar target/bookworm-backend-1.0.0-SNAPSHOT.jar
```

---

## Option 4 — Cloud Deployment (IBM ROKS / AWS ROSA)

### Prerequisites
- Access to IBM Red Hat OpenShift (ROKS) or AWS ROSA cluster
- `oc` CLI or `kubectl` installed

### Step 1 — Build and Push Docker Image
```bash
# Tag your image
docker build -t bookworm-backend:1.0.0 .

# Push to a registry (e.g. Docker Hub)
docker tag bookworm-backend:1.0.0 YOUR-USERNAME/bookworm-backend:1.0.0
docker push YOUR-USERNAME/bookworm-backend:1.0.0
```

### Step 2 — Deploy to OpenShift
```bash
# Login to cluster
oc login --token=YOUR_TOKEN --server=YOUR_SERVER

# Create project
oc new-project bookworm

# Deploy app
oc new-app YOUR-USERNAME/bookworm-backend:1.0.0 \
  -e DB_HOST=your-postgres-host \
  -e DB_PASSWORD=your-password

# Expose route
oc expose svc/bookworm-backend
```

### Step 3 — Get Public URL
```bash
oc get routes
```

---

## Environment Variables Reference

| Variable | Default | Description |
|---|---|---|
| `DB_HOST` | `127.0.0.1` | PostgreSQL host |
| `DB_PORT` | `5432` | PostgreSQL port |
| `DB_NAME` | `bookworm_db` | Database name |
| `DB_USER` | `postgres` | Database username |
| `DB_PASSWORD` | `natwar` | Database password |

---

## Health Check

Once deployed, verify the app is running:
```
GET http://<host>:8080/api/v1/books
```
Expected: `200 OK` with list of 9 books.

---

*Book Worm Backend — IBM BOB Capstone Project*

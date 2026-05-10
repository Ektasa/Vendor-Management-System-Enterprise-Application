# Vendor Management System

Enterprise-grade full-stack application for managing vendor lifecycle with role-based access control.

## Tech Stack

| Layer | Technology |
|-------|------------|
| Backend | Spring Boot 3.2, Java 17 |
| Security | Spring Security, JWT |
| Database | MySQL 8.0 |
| Frontend | Angular 17 |
| Container | Docker, Docker Compose |

## Architecture

```
Angular UI → Spring Boot API → JWT Filter → RBAC Check → Service Layer → MySQL
```

## Features

- **Three Roles**: Admin, Manager, Vendor
- **JWT Authentication**: Stateless, scalable
- **RBAC**: Method-level + database row filtering
- **Vendor Lifecycle**: Onboarding, approval workflow, status tracking

## Quick Start

### Option 1: Docker Compose (Recommended)

```bash
# Start all services
docker-compose up --build

# Access the application
# Frontend: http://localhost:4200
# Backend API: http://localhost:8080
```

### Option 2: Local Development

**Backend:**
```bash
cd backend
mvn spring-boot:run
```

**Frontend:**
```bash
cd frontend
npm install
npm start
```

## Default Users

| Role | Email | Password |
|------|-------|----------|
| Admin | admin@vms.com | admin123 |
| Manager | manager@vms.com | manager123 |
| Vendor | vendor@vms.com | vendor123 |

## API Endpoints

### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login

### Vendors
- `GET /api/vendors` - List all (Admin/Manager)
- `GET /api/vendors/my-profile` - Get own profile (Vendor)
- `POST /api/vendors` - Create vendor (Vendor)
- `PUT /api/vendors/{id}/status` - Update status (Admin/Manager)

### Admin
- `GET /api/admin/users` - List all users (Admin)
- `PUT /api/admin/users/{id}/toggle` - Toggle user status (Admin)

## Project Structure

```
├── backend/
│   ├── src/main/java/com/vms/
│   │   ├── config/       # Security, CORS config
│   │   ├── controller/  # REST controllers
│   │   ├── entity/      # JPA entities
│   │   ├── repository/  # Data repositories
│   │   ├── service/     # Business logic
│   │   ├── security/    # JWT, filters
│   │   └── dto/         # Data transfer objects
│   └── pom.xml
│
├── frontend/
│   ├── src/app/
│   │   ├── components/  # UI components
│   │   ├── services/    # API services
│   │   ├── guards/     # Route guards
│   │   ├── models/     # TypeScript models
│   │   └── interceptors/ # HTTP interceptors
│   └── package.json
│
├── docker-compose.yml
└── README.md
```

## Environment Variables

### Backend (application.yml)
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/vms_db
    username: root
    password: root

jwt:
  secret: your-secret-key
  expiration: 86400000
```

## Security Features

1. **JWT Token**: Stateless authentication
2. **Password Encoding**: BCrypt
3. **Method Security**: @PreAuthorize annotations
4. **CORS Configuration**: Configured for Angular dev server

## License

MIT
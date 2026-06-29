# RegulaTrack

RegulaTrack is a full-stack application built with Java, Spring Boot, PostgreSQL, Flyway and Angular 22, focused on managing companies, branches and regulatory licenses.

The project was developed as a practical full-stack application to organize company data, manage operational branches, track license expiration dates, calculate license statuses automatically and provide a dashboard for regulatory control workflows.

## Features

### Backend

* Company management
* Branch management
* License management
* Relationship between companies, branches and licenses
* Automatic license status calculation based on expiration date
* License filtering by status
* Paginated license search with dynamic filters
* Dashboard summary endpoint
* Global exception handling
* DTO validation
* Health check endpoint
* API documentation with Swagger/OpenAPI
* Database migrations with Flyway
* PostgreSQL running in a containerized environment
* Backend containerized with Docker/Podman
* Demo data seeding for local development

### Frontend

* Angular 22 frontend
* Standalone components
* Angular Router navigation
* Dashboard page integrated with backend API
* Licenses page integrated with backend API
* Companies page integrated with backend API
* Branches page integrated with backend API
* Responsive layout using SCSS
* HTTP integration using Angular HttpClient
* Async data rendering using AsyncPipe

## Application Pages

```text
/           -> Dashboard summary
/licenses   -> Regulatory licenses list
/companies  -> Companies list
/branches   -> Branches list
```

## License Status Rules

The license status is automatically calculated based on the expiration date:

| Condition                                 | Status        |
| ----------------------------------------- | ------------- |
| No expiration date                        | PENDING       |
| Expiration date is in the past            | EXPIRED       |
| Expiration date is within 30 days         | EXPIRING_SOON |
| Expiration date is more than 30 days away | ACTIVE        |

## Technologies

### Backend

* Java 21
* Spring Boot
* Spring Web
* Spring Data JPA
* PostgreSQL
* Flyway
* Maven
* Bean Validation
* Swagger/OpenAPI
* Docker / Podman

### Frontend

* Angular 22
* TypeScript
* SCSS
* Angular Router
* HttpClient
* Standalone Components
* AsyncPipe
* Vite-based development server

### Tools

* Git
* GitHub
* IntelliJ IDEA
* PowerShell
* Docker / Podman Compose

## Project Structure

```text
regulatrack
├── backend
│   ├── src/main/java/com/regulatrack/backend
│   │   ├── config
│   │   ├── controller
│   │   │   ├── company
│   │   │   ├── branch
│   │   │   ├── license
│   │   │   ├── dashboard
│   │   │   └── health
│   │   │
│   │   ├── service
│   │   │   ├── company
│   │   │   ├── branch
│   │   │   ├── license
│   │   │   └── dashboard
│   │   │
│   │   ├── dto
│   │   │   ├── common
│   │   │   ├── company
│   │   │   ├── branch
│   │   │   ├── license
│   │   │   └── dashboard
│   │   │
│   │   ├── domain
│   │   │   ├── company
│   │   │   ├── branch
│   │   │   └── license
│   │   │
│   │   ├── repository
│   │   │   ├── company
│   │   │   ├── branch
│   │   │   └── license
│   │   │
│   │   ├── exception
│   │   └── seed
│   │
│   └── src/main/resources/db/migration
│
├── frontend
│   └── src/app
│       ├── pages
│       │   ├── dashboard
│       │   ├── licenses
│       │   ├── companies
│       │   └── branches
│       │
│       ├── services
│       │   ├── dashboard
│       │   ├── license
│       │   ├── company
│       │   └── branch
│       │
│       ├── app.routes.ts
│       ├── app.config.ts
│       ├── app.html
│       ├── app.scss
│       └── app.ts
│
├── docker-compose.yml
└── README.md
```

## API Endpoints

### Health Check

```http
GET /api/health
```

### Companies

```http
GET    /api/companies
GET    /api/companies/{id}
POST   /api/companies
PUT    /api/companies/{id}
DELETE /api/companies/{id}
```

Example company response:

```json
{
  "id": 1,
  "name": "RegulaTech Industries",
  "documentNumber": "12345678000199",
  "segment": "Tecnologia",
  "country": "Brasil",
  "city": "Curitiba",
  "createdAt": "2026-06-16T11:29:44.906134",
  "updatedAt": "2026-06-16T11:29:44.906134"
}
```

### Branches

```http
GET    /api/branches
GET    /api/branches/{id}
POST   /api/branches
PUT    /api/branches/{id}
DELETE /api/branches/{id}
```

Example branch response:

```json
{
  "id": 1,
  "companyId": 1,
  "companyName": "RegulaTech Industries",
  "name": "Unidade Curitiba",
  "address": "Rua Central, 100",
  "city": "Curitiba",
  "state": "PR",
  "country": "Brasil",
  "createdAt": "2026-06-16T11:29:45.077079",
  "updatedAt": "2026-06-16T11:29:45.077079"
}
```

### Licenses

```http
GET    /api/licenses
GET    /api/licenses/{id}
POST   /api/licenses
PUT    /api/licenses/{id}
DELETE /api/licenses/{id}
```

### License Filters

```http
GET /api/licenses/active
GET /api/licenses/expired
GET /api/licenses/expiring-soon
GET /api/licenses/pending
GET /api/licenses/company/{companyId}
GET /api/licenses/branch/{branchId}
```

### Paginated License Search

```http
GET /api/licenses/search
```

Optional query parameters:

```http
GET /api/licenses/search?page=0&size=5
GET /api/licenses/search?status=ACTIVE&page=0&size=5
GET /api/licenses/search?status=EXPIRED&page=0&size=5
GET /api/licenses/search?category=Ambiental&page=0&size=5
GET /api/licenses/search?name=Licenca&page=0&size=5
GET /api/licenses/search?companyId=1&page=0&size=5
GET /api/licenses/search?branchId=1&page=0&size=5
```

Example response:

```json
{
  "content": [
    {
      "id": 1,
      "companyId": 1,
      "branchId": 1,
      "name": "Licenca Ambiental Vencida",
      "description": "Licenca usada para demonstrar status automatico EXPIRED",
      "category": "Ambiental",
      "licenseNumber": "ENV-2024-001",
      "issuer": "Orgao Ambiental",
      "issuedAt": "2024-01-01",
      "expiresAt": "2024-12-31",
      "responsibleName": "Igor Andrade",
      "responsibleEmail": "igor@email.com",
      "status": "EXPIRED",
      "notes": "Status calculado automaticamente pelo vencimento",
      "createdAt": "2026-06-16T10:00:00",
      "updatedAt": "2026-06-16T10:00:00"
    }
  ],
  "page": 0,
  "size": 5,
  "totalElements": 1,
  "totalPages": 1,
  "last": true
}
```

### Dashboard

```http
GET /api/dashboard/summary
```

Example response:

```json
{
  "totalCompanies": 1,
  "totalBranches": 1,
  "totalLicenses": 4,
  "activeLicenses": 1,
  "expiringSoonLicenses": 1,
  "expiredLicenses": 1,
  "pendingLicenses": 1
}
```

## API Documentation

Swagger UI is available at:

```text
http://localhost:8080/swagger-ui.html
```

Alternative Swagger UI path:

```text
http://localhost:8080/swagger-ui/index.html
```

OpenAPI JSON:

```text
http://localhost:8080/v3/api-docs
```

## Error Handling

The API uses a global exception handler to return standardized error responses.

Example:

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "License not found",
  "path": "/api/licenses/999",
  "timestamp": "2026-06-16T10:00:00"
}
```

Validation errors return a structured response with invalid fields:

```json
{
  "status": 400,
  "error": "Validation Error",
  "fields": {
    "name": "License name is required",
    "responsibleEmail": "Invalid responsible email"
  },
  "timestamp": "2026-06-16T10:00:00"
}
```

## Running with Docker or Podman Compose

The project includes a containerized setup for both the backend API and PostgreSQL database.

### 1. Start the containers

Using Podman:

```powershell
podman compose up -d --build
```

Using Docker:

```powershell
docker compose up -d --build
```

### 2. Check running containers

Using Podman:

```powershell
podman ps
```

Using Docker:

```powershell
docker ps
```

Expected containers:

```text
regulatrack_postgres
regulatrack_backend
```

### 3. Test the API

```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/health" -Method Get
```

The API will be available at:

```text
http://localhost:8080
```

## Running the Backend Locally with Maven

### 1. Start PostgreSQL with Podman

```powershell
podman run --name regulatrack_postgres `
  -e POSTGRES_DB=regulatrack `
  -e POSTGRES_USER=regulatrack `
  -e POSTGRES_PASSWORD=regulatrack `
  -p 5433:5432 `
  -d postgres:16
```

### 2. Configure the application

In `backend/src/main/resources/application.properties`:

```properties
spring.application.name=regulatrack

spring.datasource.url=${SPRING_DATASOURCE_URL:jdbc:postgresql://localhost:5433/regulatrack}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME:regulatrack}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD:regulatrack}
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

spring.flyway.enabled=true

server.port=${SERVER_PORT:8080}

app.seed.enabled=${APP_SEED_ENABLED:false}

springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
```

### 3. Run the application

```powershell
cd backend
.\mvnw.cmd clean spring-boot:run
```

The backend API will be available at:

```text
http://localhost:8080
```

## Running the Frontend Locally

### 1. Install dependencies

```powershell
cd frontend
npm install
```

### 2. Start the Angular application

```powershell
ng serve
```

If the development server has reload or file watcher issues on Windows, use:

```powershell
ng serve --poll=2000 --live-reload=false
```

The frontend will be available at:

```text
http://localhost:4200
```

## Demo Data Seed

The project includes an optional demo data seeder for local development.

When enabled, it creates:

* 1 company
* 1 branch
* 4 licenses with different statuses:

  * ACTIVE
  * EXPIRED
  * EXPIRING_SOON
  * PENDING

To enable seed data, set:

```properties
APP_SEED_ENABLED=true
```

In the containerized environment, this can be configured in `docker-compose.yml`.

## Database Migrations

The database schema is managed using Flyway.

Migration files are located in:

```text
backend/src/main/resources/db/migration
```

## Tests

The project includes automated tests for the license status calculation rule.

Run tests locally:

```powershell
cd backend
.\mvnw.cmd test
```

## Current Status

The project currently includes a complete backend foundation with CRUD operations, automated license status calculation, paginated search with filters, dashboard summary, validation, global error handling, Swagger/OpenAPI documentation, containerized backend setup and demo data seeding.

The frontend currently includes Angular 22 pages integrated with the backend API for dashboard, licenses, companies and branches.

## Next Steps

* Improve frontend layout and user experience
* Add create, edit and delete forms in the Angular frontend
* Add pagination and search filters for companies and branches
* Add advanced filters for licenses in the frontend
* Add authentication and authorization
* Add document upload for licenses
* Add audit logs
* Add automated integration tests
* Add production deployment configuration

## License

This project is publicly available for portfolio and educational purposes.

Commercial use, redistribution, resale or use as a base for commercial products is not permitted without explicit authorization from the author.

Copyright © 2026 Igor Andrade. All rights reserved.

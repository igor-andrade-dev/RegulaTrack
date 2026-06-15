# RegulaTrack

RegulaTrack is a backend API built with Java, Spring Boot, PostgreSQL and Flyway, focused on managing companies, branches and regulatory licenses.

The project was developed as a practical backend application to organize company data, track license expiration dates and provide automated status calculation for regulatory documents.

## Features

* Company management
* Branch management
* License management
* Relationship between companies, branches and licenses
* Automatic license status calculation based on expiration date
* License filtering by status
* Dashboard summary endpoint
* Global exception handling
* DTO validation
* Database migrations with Flyway
* PostgreSQL running in a containerized environment

## License Status Rules

The license status is automatically calculated based on the expiration date:

| Condition                                 | Status        |
| ----------------------------------------- | ------------- |
| No expiration date                        | PENDING       |
| Expiration date is in the past            | EXPIRED       |
| Expiration date is within 30 days         | EXPIRING_SOON |
| Expiration date is more than 30 days away | ACTIVE        |

## Technologies

* Java
* Spring Boot
* Spring Web
* Spring Data JPA
* PostgreSQL
* Flyway
* Maven
* Bean Validation
* Podman
* Git

## Project Structure

```text
src/main/java/com/regulatrack/backend
├── controller
│   ├── company
│   ├── branch
│   ├── license
│   └── dashboard
│
├── service
│   ├── company
│   ├── branch
│   ├── license
│   └── dashboard
│
├── dto
│   ├── company
│   ├── branch
│   ├── license
│   └── dashboard
│
├── domain
│   ├── company
│   ├── branch
│   └── license
│
├── repository
│   ├── company
│   ├── branch
│   └── license
│
└── exception
```

## API Endpoints

### Companies

```http
GET    /api/companies
GET    /api/companies/{id}
POST   /api/companies
PUT    /api/companies/{id}
DELETE /api/companies/{id}
```

### Branches

```http
GET    /api/branches
GET    /api/branches/{id}
POST   /api/branches
PUT    /api/branches/{id}
DELETE /api/branches/{id}
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

### Dashboard

```http
GET /api/dashboard/summary
```

Example response:

```json
{
  "totalCompanies": 2,
  "totalBranches": 2,
  "totalLicenses": 5,
  "activeLicenses": 2,
  "expiringSoonLicenses": 1,
  "expiredLicenses": 1,
  "pendingLicenses": 1
}
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
  "timestamp": "2026-06-15T19:18:32"
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
  "timestamp": "2026-06-15T16:41:05"
}
```

## Running Locally

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

In `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5433/regulatrack
spring.datasource.username=regulatrack
spring.datasource.password=regulatrack

spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true

spring.flyway.enabled=true

server.port=8083
```

### 3. Run the application

```powershell
cd backend
.\mvnw.cmd clean spring-boot:run
```

The API will be available at:

```text
http://localhost:8083
```

## Database Migrations

The database schema is managed using Flyway.

Migration files are located in:

```text
src/main/resources/db/migration
```

Current migrations:

```text
V1__create_companies_and_branches_tables.sql
V2__create_licenses_table.sql
```

## Current Status

The project currently includes the core backend structure, CRUD operations, automated license status calculation, dashboard summary, validation and global error handling.

## Next Steps

* Add authentication and authorization
* Add document upload for licenses
* Add advanced search and pagination
* Add audit logs
* Add frontend dashboard
* Add automated tests
* Add Docker Compose configuration

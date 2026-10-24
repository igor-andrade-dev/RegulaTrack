# RegulaTrack

<p align="center">
  A web application for managing companies, branches, regulatory licenses, users, and permissions.
</p>

<p align="center">
  <a href="https://regulatrack.duckdns.org">🌐 Live Application</a>
  •
  <a href="https://regulatrack-api.duckdns.org/swagger-ui/index.html">📚 API / Swagger</a>
  •
  <a href="https://github.com/igor-andrade-dev/RegulaTrack">💻 Source Code</a>
</p>

---

## 📌 About the Project

**RegulaTrack** is a full-stack web application designed to centralize the management of companies, branches, regulatory licenses, and user permissions.

The project was developed using a decoupled architecture between the frontend, backend, and database, with JWT-based authentication, role and permission management, REST APIs, and versioned database migrations.

The application is also deployed to a production environment using AWS infrastructure, Nginx, HTTPS, and PostgreSQL.

---

## 🚀 Live Demo

### Application

🌐 **Frontend:**  
https://regulatrack.duckdns.org

### Backend API

🔗 **API:**  
https://regulatrack-api.duckdns.org

### API Documentation

📚 **Swagger / OpenAPI:**  
https://regulatrack-api.duckdns.org/swagger-ui/index.html

---

## 🧰 Tech Stack

### Frontend

- Angular 22
- TypeScript
- RxJS
- Angular Router
- Tailwind CSS
- HTML5
- SCSS

### Backend

- Java 21
- Spring Boot
- Spring Security
- JWT
- Hibernate / JPA
- Bean Validation
- REST API
- Flyway

### Database

- PostgreSQL

### Infrastructure

- AWS EC2
- AWS RDS PostgreSQL
- Nginx
- Linux
- HTTPS / SSL
- Let's Encrypt
- Certbot
- Git / GitHub

---

## 🏗️ Architecture

The application follows a decoupled architecture between the frontend, backend, and database.

```text
                         ┌───────────────────────┐
                         │         User          │
                         └───────────┬───────────┘
                                     │
                                     ▼
                         ┌───────────────────────┐
                         │   Angular 22 +        │
                         │      Tailwind         │
                         │       Frontend        │
                         └───────────┬───────────┘
                                     │
                                   HTTPS
                                     │
                                     ▼
                         ┌───────────────────────┐
                         │         Nginx         │
                         │  Reverse Proxy / SSL  │
                         └───────────┬───────────┘
                                     │
                                     ▼
                         ┌───────────────────────┐
                         │   Spring Boot REST    │
                         │        API            │
                         │       Java 21         │
                         └───────────┬───────────┘
                                     │
                                     ▼
                         ┌───────────────────────┐
                         │      PostgreSQL       │
                         │        AWS RDS        │
                         └───────────────────────┘
🔐 Authentication and Security

RegulaTrack uses Spring Security and JWT for authentication and authorization.

Authentication Flow
User Login
    ↓
Spring Security
    ↓
Credential Validation
    ↓
JWT Token
    ↓
Frontend sends authenticated requests
    ↓
JWT validation
    ↓
Authorized resources

The application also implements permission-based access control for different modules and operations.

👥 User Management and Permissions

The application supports user management with granular permissions.

Roles
ADMIN

Has full access to the system and its resources.

USER

Access is limited according to the permissions assigned to the user.

Permission Areas
Dashboard
Companies
Branches
Licenses
Users

Supported operations include:

View
Create
Update
Delete
🏢 Main Features
Companies
Create companies
Update companies
Delete companies
View company information
Associate branches with companies
Branches
Create branches
Update branches
Delete branches
View branch information
Associate branches with companies
Regulatory Licenses
Create licenses
Update licenses
Delete licenses
View licenses
Track license lifecycle
Monitor expiration status
License Status
🟢 Active
🟡 Expiring Soon
🔴 Expired
⚪ Pending
Dashboard

The dashboard provides consolidated operational information, including:

Total companies
Total branches
Total licenses
Active licenses
Licenses expiring soon
Expired licenses
Pending licenses
🔄 Password Recovery

The system provides a password recovery workflow.

User requests password recovery
            ↓
API generates recovery token
            ↓
Token is stored
            ↓
Recovery email is sent
            ↓
User accesses recovery link
            ↓
New password is registered
📡 REST API

The backend follows REST principles and provides API documentation through OpenAPI / Swagger.

Authentication
POST /auth/login
POST /auth/register
POST /auth/password-reset/request
POST /auth/password-reset/confirm
GET  /auth/me
System
GET /api/health
GET /api/dashboard/summary
Companies
GET    /api/companies
POST   /api/companies
PUT    /api/companies/{id}
DELETE /api/companies/{id}
Branches
GET    /api/branches
POST   /api/branches
PUT    /api/branches/{id}
DELETE /api/branches/{id}
Licenses
GET    /api/licenses
POST   /api/licenses
PUT    /api/licenses/{id}
DELETE /api/licenses/{id}
Users
GET    /api/users
POST   /api/users
PUT    /api/users/{id}
DELETE /api/users/{id}
Swagger

https://regulatrack-api.duckdns.org/swagger-ui/index.html

🗄️ Database

RegulaTrack uses PostgreSQL as its relational database.

Database schema changes are managed with Flyway, providing versioned and repeatable database migrations.

Example migration structure:

V1
V2
V3
V4
...

This approach helps keep database schemas consistent across development and production environments.

⚙️ Local Development
Prerequisites

Make sure the following tools are installed:

Java 21
Node.js 22.22.3+ (required by Angular 22)
npm
PostgreSQL
Git
Clone the Repository
git clone https://github.com/igor-andrade-dev/RegulaTrack.git

cd RegulaTrack
▶️ Running the Backend

Navigate to the backend directory:

cd backend

Set a unique signing key in the same terminal before starting the backend.

Linux/macOS:

```bash
export JWT_SECRET="$(openssl rand -base64 48)"
./mvnw spring-boot:run
```

Windows PowerShell:

```powershell
$env:JWT_SECRET = [Convert]::ToBase64String([Security.Cryptography.RandomNumberGenerator]::GetBytes(48))
./mvnw.cmd spring-boot:run
```

The API will be available at:

http://localhost:8080
▶️ Running the Frontend

Open another terminal:

cd frontend

Install dependencies:

npm install

Start the development server:

npm start

The frontend will be available at:

http://localhost:4200

### Optional local demo data

Seeding is disabled by default. Docker Compose also requires a signing key. Generate one in the repository root and then add your local seed settings to the untracked `.env` file:

```bash
printf 'JWT_SECRET=%s\n' "$(openssl rand -base64 48)" > .env
cat >> .env <<'EOF'
APP_SEED_ENABLED=true
APP_SEED_ADMIN_PASSWORD=replace-with-a-unique-password-of-at-least-12-characters
APP_SEED_USER_PASSWORD=replace-with-another-unique-password-of-at-least-12-characters
EOF
```

Replace both example passwords before starting Docker Compose. The seed process refuses to create demo accounts if either password is missing or shorter than 12 characters. Do not enable seeding in production.

🐘 PostgreSQL Configuration

Configure the required environment variables:

SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5433/regulatrack
SPRING_DATASOURCE_USERNAME=regulatrack
SPRING_DATASOURCE_PASSWORD=regulatrack

Never commit real credentials, passwords, tokens, or secrets to Git.

📦 Production Build
Backend
cd backend
./mvnw clean package -DskipTests

The generated JAR file will be placed under:

backend/target/
Frontend
cd frontend
npm run build

The production build will be generated under:

frontend/dist/
☁️ Production Deployment

RegulaTrack is deployed in an AWS environment.

Infrastructure
                       AWS EC2
                          │
          ┌───────────────┴───────────────┐
          │                               │
       Nginx                         Spring Boot
          │                               │
    ┌─────┴─────┐                         │
    │           │                         │
Frontend      HTTPS                       │
    │                                     │
    └──────────────► Reverse Proxy ◄──────┘
                                          │
                                          ▼
                                   AWS RDS PostgreSQL
AWS / Infrastructure Components
Amazon EC2
Amazon RDS PostgreSQL
Nginx
Linux
HTTPS
Let's Encrypt
Certbot
DNS

The Spring Boot backend runs as a system service, allowing the application to automatically start after server reboots.

🔒 Environment Variables

Production configuration uses environment variables rather than hard-coded secrets.

Main configuration variables include:

SPRING_PROFILES_ACTIVE=prod

SPRING_DATASOURCE_URL=
SPRING_DATASOURCE_USERNAME=
SPRING_DATASOURCE_PASSWORD=
JWT_SECRET=

APP_FRONTEND_URL=
APP_FRONTEND_BASE_URL=

APP_SEED_ENABLED=false
APP_SEED_ADMIN_PASSWORD=
APP_SEED_USER_PASSWORD=

MAIL_HOST=
MAIL_PORT=
MAIL_USERNAME=
MAIL_PASSWORD=
MAIL_SMTP_AUTH=
MAIL_STARTTLS=
APP_MAIL_FROM=
Security Notice

Never commit production credentials to GitHub.

Sensitive configuration must remain outside the repository. Production startup now requires a `JWT_SECRET` of at least 32 bytes. Generate a unique high-entropy value (for example, with `openssl rand -base64 48`) and store it in the deployment secret manager or environment; never reuse the local development value.

Additional production requirements:

- Set `SPRING_PROFILES_ACTIVE=prod` and `APP_SEED_ENABLED=false`.
- Configure `APP_FRONTEND_URL` and `APP_FRONTEND_BASE_URL` to the exact HTTPS frontend origin.
- Enable SMTP with `APP_MAIL_ENABLED=true` and valid `MAIL_*` values so password recovery can actually deliver mail. Reset links are never written to application logs.
- The reset link expires after 30 minutes and is single-use. A successful password reset or administrator-initiated password change invalidates tokens already issued to the affected user. The Flyway migration `V8__add_user_token_version.sql` adds this token-version check; existing sessions without the new claim must sign in again after deployment.
- Keep the database and backend ports private in production. CORS is not a rate limiter; sample Nginx limits are provided in `deploy/nginx/`. Include the `*-zones` file inside `http {}` and the `*-locations` file inside the HTTPS API `server {}` after adapting the upstream, then run `nginx -t` before reloading Nginx.
- Serve the frontend and API only over HTTPS and rotate any secrets that may previously have been committed or shared.

🧪 Engineering Practices

The project applies several software engineering practices, including:

Layered architecture
DTO pattern
REST API design
Dependency Injection
Spring Security
JWT authentication
Permission-based authorization
Bean Validation
JPA / Hibernate
Flyway database migrations
Angular componentization
Route guards
HTTP interceptors
Environment-based configuration
Reverse proxy
HTTPS
Linux deployment
Separation of frontend, backend, and database responsibilities
📊 Project Status
Implemented
 JWT authentication
 User management
 Permission management
 Company management
 Branch management
 License management
 Dashboard
 Password recovery
 PostgreSQL
 Flyway migrations
 REST API
 Swagger / OpenAPI
 Angular frontend
 AWS deployment
 HTTPS
 Nginx
 Production environment
Future Improvements
 Expanded automated test coverage
 Advanced pagination
 Advanced filtering
 Observability improvements
 CI/CD pipeline
 Infrastructure monitoring
 UI/UX improvements
🎯 Project Goals

RegulaTrack was developed as a practical full-stack SaaS project to demonstrate experience with:

Full-stack development
Software engineering
REST APIs
Application security
Relational databases
Software architecture
Cloud deployment
Linux server administration
Modern frontend development
Production-oriented application development

The main goal is to build a realistic application that goes beyond a traditional CRUD project.

👨‍💻 Author

Igor Andrade

Full Stack Developer

Areas of Interest
Java
Spring Boot
Angular
PostgreSQL
REST APIs
Cloud Computing
Software Architecture
📄 License

This project is intended for educational, portfolio, and demonstration purposes.

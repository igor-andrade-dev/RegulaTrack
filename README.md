# 📊 RegulaTrack

Modern SaaS system for regulatory license management built with **Angular + Spring Boot + PostgreSQL**.

---

## 🚀 Stack

**Frontend**
- Angular
- TypeScript
- SCSS

**Backend**
- Java 21
- Spring Boot
- Spring Security (JWT)
- JPA / Hibernate
- PostgreSQL

**Infra**
- Docker
- REST API
- JWT Authentication

---

## 🎯 Features

- JWT Login
- Dashboard overview
- Companies CRUD
- Branches CRUD
- Licenses CRUD
- License status tracking:
  - Active
  - Expiring Soon
  - Expired
  - Pending

---

## 🎨 UI

- SaaS layout (Stripe-inspired)
- Sidebar navigation
- Card-based design
- Tables with filters
- Action buttons (view/edit/delete)
- Responsive layout

---

## 🔐 Auth

Login endpoint:


POST /auth/login


Example:

```json
{
  "username": "admin",
  "password": "123"
}

Response:

{
  "token": "JWT_TOKEN"
}
⚙️ Run Project
Backend
docker-compose up

Runs at:

http://localhost:8080
Frontend
npm install
ng serve

Runs at:

http://localhost:4200
📦 Architecture
Angular Frontend
      ↓
JWT Auth
      ↓
Spring Boot API
      ↓
PostgreSQL
📌 Status

MVP completed with core SaaS features.

👨‍💻 Author

Igor Andrade


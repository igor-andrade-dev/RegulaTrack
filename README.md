# 📊 RegulaTrack

A modern **SaaS platform for regulatory license management**, built with **Angular + Spring Boot + PostgreSQL**.

The system helps organizations manage companies, branches, and regulatory licenses in a centralized and scalable way.

---

## 🚀 Tech Stack

### Frontend
- Angular
- TypeScript
- SCSS (Design System)
- RxJS
- Angular Router

### Backend
- Java 21
- Spring Boot 3+
- Spring Security (JWT Authentication)
- Hibernate / JPA
- PostgreSQL
- Flyway Migrations

### Infrastructure
- Docker
- REST API architecture
- Stateless authentication (JWT)
- CORS configuration

---

## 🎯 Core Features

### 🔐 Authentication
- JWT-based login system
- Secure API access
- Route protection (Angular guards)

### 🏢 Companies Management
- Create, update, delete companies
- Company details view

### 🏬 Branches Management
- Manage operational units (branches)
- Link branches to companies

### 📄 Regulatory Licenses
- Full CRUD system
- License lifecycle tracking:
  - Active
  - Expiring Soon
  - Expired
  - Pending

### 📊 Dashboard
- System overview
- Aggregated metrics (licenses, companies, branches)

---

## 🎨 UI / UX Design

- Modern SaaS layout inspired by Stripe/Linear
- Responsive interface
- Sidebar navigation system
- Reusable SCSS design system
- Card-based layout structure
- Data tables with actions (View / Edit / Delete)
- Clean and minimal visual hierarchy

---

## 🔐 Authentication Flow

### Login Endpoint

POST /auth/login


### Request Example
```json
{
  "username": "admin",
  "password": "123"
}
Response Example
{
  "token": "JWT_TOKEN"
}

⚙️ How to Run
Backend
docker-compose up

Backend runs at:

http://localhost:8080
Frontend
npm install
ng serve

Frontend runs at:

http://localhost:4200

🧱 Architecture
Angular Frontend
      ↓
JWT Authentication
      ↓
Spring Boot REST API
      ↓
PostgreSQL Database

📌 Project Status

✔ MVP completed
⚠ UI refinements in progress
⚠ Advanced filtering & pagination planned

🧠 Key Engineering Concepts

Clean layered architecture
RESTful API design
Stateless authentication (JWT)
Reactive frontend architecture (RxJS)
Component-based UI design
Reusable SCSS design system
Dockerized environment

👨‍💻 Author

Developed by Igor Andrade

Full Stack Developer focused on SaaS systems, scalable architectures, and modern web applications.

📌 Goal of This Project

This project simulates a real-world SaaS platform for regulatory compliance management, focusing on:

Scalability
Maintainability
Security
Clean UI/UX
Production-like architecture

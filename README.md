# JWT Authentication System using Keycloak, Spring Boot & PostgreSQL

## 📌 Overview

This project is a secure authentication and authorization system built using **Spring Boot**, **Keycloak**, **JWT (JSON Web Token)**, **PostgreSQL**, and **REST APIs**.

The application allows users to:

- Register a new account
- Login using username and password
- Receive a JWT Access Token after successful authentication
- Access secured REST APIs using the generated JWT
- Store user information securely in PostgreSQL
- Manage authentication and authorization through Keycloak

---

## 🚀 Tech Stack

- Java 17
- Spring Boot
- Spring Security
- Keycloak
- JWT (JSON Web Token)
- PostgreSQL
- Spring Data JPA
- REST API
- Maven

---

## 📂 Project Architecture

```
Client
   │
   ▼
REST API
   │
   ▼
Spring Boot Application
   │
   ├──────── User Registration
   │
   ├──────── User Login
   │
   ├──────── JWT Token Generation
   │
   ├──────── JWT Validation
   │
   └──────── Protected APIs
   │
   ▼
Keycloak Authentication Server
   │
   ▼
PostgreSQL Database
```

---

## ✨ Features

- User Registration
- User Login
- JWT Authentication
- Secure Password Handling
- Role-Based Authentication (via Keycloak)
- Protected REST Endpoints
- PostgreSQL Database Integration
- Stateless Authentication
- Token Validation
- Exception Handling

---

## 🔐 Authentication Flow

### 1. User Registration

- User sends registration details.
- Spring Boot validates the request.
- User details are stored.
- User is created in Keycloak.

### 2. User Login

- User enters:
  - Username
  - Password

- Spring Boot forwards the credentials to Keycloak.

- If credentials are valid:
  - Keycloak authenticates the user.
  - Generates a JWT Access Token.
  - Returns the token to the client.

### 3. Access Protected APIs

The client includes the JWT token in every request:

```
Authorization: Bearer <JWT_TOKEN>
```

Spring Security validates the token before allowing access.

---

## 📁 Project Structure

```
src
├── controller
│
├── service
│
├── repository
│
├── entity
│
├── dto
│
├── config
│
├── security
│
├── exception
│
└── util
```

---

## ⚙️ Configuration

### PostgreSQL

Configure the database in:

```properties
application.properties
```

Example:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/authdb
spring.datasource.username=postgres
spring.datasource.password=your_password
```

---

### Keycloak

Configure:

```properties
keycloak.server-url=
keycloak.realm=
keycloak.client-id=
keycloak.client-secret=
```

---

## 📡 REST APIs

### Register User

```
POST /api/auth/register
```

Request

```json
{
  "username": "john",
  "email": "john@example.com",
  "password": "password123"
}
```

Response

```json
{
  "message": "User Registered Successfully"
}
```

---

### Login User

```
POST /api/auth/login
```

Request

```json
{
  "username": "john",
  "password": "password123"
}
```

Response

```json
{
  "access_token": "...",
  "refresh_token": "...",
  "expires_in": 300
}
```

---

### Access Protected API

```
GET /api/users/profile
```

Header

```
Authorization: Bearer <JWT_TOKEN>
```

---

## 🔒 Security

- Password-based authentication
- JWT Access Token
- Stateless Sessions
- Spring Security Integration
- Keycloak Authentication
- Role-Based Authorization
- Secure REST APIs

---

## ▶️ Running the Project

### Clone Repository

```bash
git clone https://github.com/your-username/your-repository.git
```

### Navigate

```bash
cd your-repository
```

### Build

```bash
mvn clean install
```

### Run

```bash
mvn spring-boot:run
```

---

## 🧪 Testing

Use tools like:

- Postman
- Insomnia
- cURL

### Registration

```
POST /api/auth/register
```

### Login

```
POST /api/auth/login
```

### Protected Endpoint

```
GET /api/users/profile
```

Include:

```
Authorization: Bearer <JWT_TOKEN>
```

---

## 📈 Future Enhancements

- Email Verification
- Refresh Token Rotation
- Forgot Password
- Password Reset
- OAuth2 Login (Google/GitHub)
- Docker Support
- Kubernetes Deployment
- API Documentation with Swagger/OpenAPI
- Unit and Integration Tests
- CI/CD Pipeline

---

## 👨‍💻 Author

**Priyabrat Swain**

Java Full Stack Developer

### Skills

- Java
- Spring Boot
- Spring Security
- JWT
- Keycloak
- REST APIs
- PostgreSQL
- Hibernate
- JPA
- Maven
- Git
- Docker

---

## 📜 License

This project is intended for learning and demonstration purposes.

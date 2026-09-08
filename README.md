# Banfico Training Program — Spring Boot Banking API

A REST API backend for a banking system built with Spring Boot 4.1.0. Handles customers, accounts, transactions, beneficiaries, and fund transfers. Secured with Keycloak JWT authentication and role-based access control.

## Architecture

```
Next.js (frontend)
      |
      |  Bearer JWT
      v
Spring Boot (this API) ──── PostgreSQL
      |
      |  Admin REST API
      v
Keycloak (identity provider)
```

Spring Boot acts as an **OAuth2 resource server** — it never stores passwords. Every request must carry a valid Keycloak JWT. Spring verifies the token's signature using Keycloak's public key and extracts roles from it.

## Roles

| Role | Access |
|---|---|
| `user` | Own accounts, own transactions, own beneficiaries, transfers |
| `BankManager` | All customers, all accounts, all transactions, register new users |
| `admin` | Everything — including delete operations |

Roles are assigned in Keycloak and embedded in the JWT. The `KeycloakJwtConverter` maps them to Spring Security authorities (`ROLE_USER`, `ROLE_BANKMANAGER`, `ROLE_ADMIN`).

## Endpoints

### Public / System

| Method | URL | Access | Description |
|---|---|---|---|
| GET | `/api/health` | Public | Returns `"UP"` |
| GET | `/api/info` | Public | App version, git branch, commit ID |

### Registration

| Method | URL | Access | Description |
|---|---|---|---|
| POST | `/api/register` | Admin, BankManager | Creates Keycloak user + Customer DB record |

**Request body:**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "pan": "ABCDE1234F",
  "phoneNumber": "9876543210",
  "username": "johndoe",
  "temporaryPassword": "Pass@1234"
}
```

On success → Keycloak user created with `temporary: true` password (forces password change on first login) + Customer record saved in DB.
If DB save fails → Keycloak user is automatically rolled back (deleted).

### Customers

| Method | URL | Access | Description |
|---|---|---|---|
| GET | `/api/customers` | Admin, BankManager | Get all customers |
| GET | `/api/customers/{id}` | Admin, BankManager | Get customer by ID |
| POST | `/api/customers` | Admin | Create customer (DB only) |
| PUT | `/api/customers/{id}` | Admin | Update customer |
| DELETE | `/api/customers/{id}` | Admin | Delete customer |

### Accounts

| Method | URL | Access | Description |
|---|---|---|---|
| GET | `/api/accounts` | Admin, BankManager | Get all accounts |
| GET | `/api/accounts/{id}` | Admin, BankManager | Get account by ID |
| POST | `/api/accounts` | Admin | Create account |
| PUT | `/api/accounts/{id}` | Admin | Update account |
| DELETE | `/api/accounts/{id}` | Admin | Delete account |
| GET | `/api/accounts/lookup/{accountNo}` | Authenticated | Look up account by account number |

### Transactions

| Method | URL | Access | Description |
|---|---|---|---|
| GET | `/api/accounts/{accountId}/transactions` | Admin, BankManager | Get transactions for account |
| POST | `/api/accounts/{accountId}/transactions` | Admin | Create transaction |

### Beneficiaries

| Method | URL | Access | Description |
|---|---|---|---|
| GET | `/api/customers/{customerId}/beneficiaries` | Admin, BankManager | Get beneficiaries for customer |
| POST | `/api/customers/{customerId}/beneficiaries` | Admin | Add beneficiary |
| PUT | `/api/customers/{customerId}/beneficiaries/{id}` | Admin | Update nickname |
| DELETE | `/api/customers/{customerId}/beneficiaries/{id}` | Admin | Remove beneficiary |

### Fund Transfer

| Method | URL | Access | Description |
|---|---|---|---|
| POST | `/api/transfer` | User | Transfer between accounts |

### Me (Current User)

| Method | URL | Access | Description |
|---|---|---|---|
| GET | `/api/me/accounts` | User | Get own accounts |
| GET | `/api/me/accounts/{accountId}/transactions` | User | Get transactions for own account |
| GET | `/api/me/beneficiaries` | User | Get own beneficiaries |
| POST | `/api/me/beneficiaries` | User | Add beneficiary to own list |
| DELETE | `/api/me/beneficiaries/{id}` | User | Remove own beneficiary |

## Global Exception Handling

All errors return a consistent JSON shape:

```json
{
  "status": 400,
  "error": "Invalid Data",
  "message": "A customer with this email or PAN already exists.",
  "timestamp": "2026-09-08T10:00:00"
}
```

`GlobalExceptionHandler` (`@RestControllerAdvice`) intercepts every exception thrown from any controller.

| Exception | Cause | HTTP Status |
|---|---|---|
| `ResourceNotFoundException` | Entity not found in DB | 404 |
| `InsufficientBalanceException` | DEBIT exceeds account balance | 400 |
| `MethodArgumentNotValidException` | `@Valid` check fails on request body | 400 |
| `DataIntegrityViolationException` | Duplicate PAN / email, or value too long | 400 |
| `HttpClientErrorException` (409) | Username or email already exists in Keycloak | 409 |
| `RestClientException` | Cannot reach Keycloak | 502 |
| `Exception` | Any other unexpected error | 500 |

## Project Structure

```
src/main/java/com/riyaz/banficotrainingprogram/
├── BanficoTrainingProgramApplication.java
├── Controller/
│   ├── CustomerController.java
│   ├── AccountController.java
│   ├── TransactionController.java
│   ├── BeneficiaryController.java
│   ├── MeController.java           — current-user endpoints
│   ├── RegistrationController.java — Keycloak + DB user registration
│   └── SystemController.java       — health, info
├── Service/
│   ├── CustomerService.java        — interface
│   ├── AccountService.java
│   ├── TransactionService.java
│   ├── BeneficiaryService.java
│   ├── SystemService.java
│   ├── KeycloakAdminService.java   — Keycloak Admin REST API calls
│   └── impl/                       — interface implementations
│       ├── CustomerServiceImpl.java
│       ├── AccountServiceImpl.java
│       ├── TransactionServiceImpl.java
│       ├── BeneficiaryServiceImpl.java
│       └── SystemServiceImpl.java
├── Entity/
│   ├── Customer.java
│   ├── Account.java
│   ├── Transactions.java
│   └── Beneficiary.java
├── dto/
│   ├── CustomerRequest.java / CustomerResponse.java
│   ├── AccountRequest.java / AccountResponse.java
│   ├── AccountLookupResponse.java
│   ├── TransactionRequest.java / TransactionResponse.java
│   ├── BeneficiaryRequest.java / BeneficiaryResponse.java
│   ├── TransferRequest.java / TransferResponse.java
│   ├── RegisterRequest.java        — with @Valid constraints
│   ├── RegisterResponse.java
│   ├── ErrorResponse.java
│   ├── Healthresponse.java
│   └── InfoResponse.java
├── security/
│   ├── SecurityConfig.java         — endpoint access rules per role
│   └── KeycloakJwtConverter.java   — extracts roles from JWT
├── exception/
│   ├── GlobalExceptionHandler.java
│   ├── ResourceNotFoundException.java
│   └── InsufficientBalanceException.java
├── metadata/
│   └── GitInfoProvider.java
└── repository/
    ├── CustomerRepo.java
    ├── AccountRepo.java
    ├── TransactionsRepo.java
    └── BeneficiaryRepo.java
```

## How Layers Work

```
Request → Controller → Service (interface) → ServiceImpl → Repository → DB
                          ↑
                     Business logic,
                     Entity ↔ DTO mapping,
                     exception throwing
```

- **Controller** — receives HTTP request, calls service, returns `ResponseEntity`
- **Service interface** — defines the contract (what operations exist)
- **ServiceImpl** — implements business rules (validation, balance checks, mapping)
- **Repository** — extends `JpaRepository`; Spring generates all SQL automatically
- **Entity** — maps to DB table via Hibernate; never sent directly to the client
- **DTO** — plain classes for request input and response output; carries `@Valid` constraints

## Technology Stack

- Java 17
- Spring Boot 4.1.0
- Spring Web MVC
- Spring Data JPA / Hibernate
- Spring Security — OAuth2 Resource Server
- Keycloak — Identity and access management
- PostgreSQL (Neon DB in prod, Docker in dev)
- Maven
- Docker / Docker Compose

## Environment Variables

| Variable | Description |
|---|---|
| `DB_URL` | JDBC connection URL (`jdbc:postgresql://...`) |
| `DB_USERNAME` | Database username |
| `DB_PASSWORD` | Database password |
| `KEYCLOAK_ISSUER_URI` | Keycloak realm URL (`http://keycloak:8080/realms/bankapp`) |
| `KEYCLOAK_ADMIN_URL` | Keycloak base URL for Admin API |
| `KEYCLOAK_ADMIN_USERNAME` | Master realm admin username |
| `KEYCLOAK_ADMIN_PASSWORD` | Master realm admin password |

## Running with Docker

```bash
# Start all services (Keycloak, PostgreSQL, Spring Boot, Next.js)
docker compose up --build -d

# Rebuild Spring Boot only after code changes
docker compose up --build -d app
```

Services:
- Spring Boot API: `http://localhost:8081`
- Keycloak: `http://localhost:8180`
- PostgreSQL: `localhost:5432`

## Author

Riyaz

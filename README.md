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

All account endpoints are unified — the response is filtered by the caller's role at runtime.

| Method | URL | Access | Behaviour |
|---|---|---|---|
| GET | `/api/accounts` | Authenticated | Staff → all accounts; User → own accounts only |
| GET | `/api/accounts/{id}` | Authenticated | Returns the account by ID |
| GET | `/api/accounts/lookup?accountNo=` | Authenticated | Look up account by account number |
| POST | `/api/accounts` | Admin | Create account |
| PUT | `/api/accounts/{id}` | Admin | Update account |
| DELETE | `/api/accounts/{id}` | Admin | Delete account |

### Transactions

| Method | URL | Access | Behaviour |
|---|---|---|---|
| GET | `/api/transactions?accountId=` | Authenticated | Staff → any account; User → own accounts only (ownership enforced) |
| POST | `/api/transactions` | Admin | Create a CREDIT or DEBIT transaction |

### Beneficiaries

| Method | URL | Access | Behaviour |
|---|---|---|---|
| GET | `/api/beneficiaries` | Authenticated | Staff + `?customerId=` → specific customer; User → own list |
| POST | `/api/beneficiaries` | Authenticated | Staff + `?customerId=` → add for customer; User → add to own list |
| PUT | `/api/beneficiaries/{id}` | Authenticated | Update nickname |
| DELETE | `/api/beneficiaries/{id}` | Authenticated | Staff → any; User → own beneficiaries only |

### Fund Transfer

| Method | URL | Access | Description |
|---|---|---|---|
| POST | `/api/transfer` | Authenticated | Transfer funds between accounts (sender resolved from JWT) |

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

The project is organised as a **modular monolith** — each domain owns its full vertical slice (controller → service → repository → entity → dto) inside its own package. Shared concerns (`exception`, `security`) live at the top level.

```
src/main/java/com/riyaz/banficotrainingprogram/
├── BanficoTrainingProgramApplication.java
│
├── customer/
│   ├── controller/   CustomerController.java, RegistrationController.java
│   ├── dto/          CustomerRequest/Response, RegisterRequest/Response
│   ├── entity/       Customer.java
│   ├── repository/   CustomerRepo.java
│   └── service/      CustomerService.java, KeycloakAdminService.java
│       └── impl/     CustomerServiceImpl.java
│
├── account/
│   ├── controller/   AccountController.java
│   ├── dto/          AccountRequest/Response, AccountLookupResponse
│   ├── entity/       Account.java
│   ├── repository/   AccountRepo.java
│   └── service/      AccountService.java
│       └── impl/     AccountServiceImpl.java
│
├── beneficiary/
│   ├── controller/   BeneficiaryController.java
│   ├── dto/          BeneficiaryRequest/Response
│   ├── entity/       Beneficiary.java
│   ├── repository/   BeneficiaryRepo.java
│   └── service/      BeneficiaryService.java
│       └── impl/     BeneficiaryServiceImpl.java
│
├── transaction/
│   ├── controller/   TransactionController.java, TransferController.java
│   ├── dto/          TransactionRequest/Response, TransferRequest/Response
│   ├── entity/       Transactions.java
│   ├── repository/   TransactionsRepo.java
│   └── service/      TransactionService.java, TransferService.java
│       └── impl/     TransactionServiceImpl.java, TransferServiceImpl.java
│
├── system/
│   ├── controller/   SystemController.java
│   ├── dto/          HealthResponse.java, InfoResponse.java
│   ├── metadata/     GitInfoProvider.java
│   └── service/      SystemService.java
│       └── impl/     SystemServiceImpl.java
│
├── exception/
│   ├── GlobalExceptionHandler.java
│   ├── ErrorResponse.java
│   ├── ResourceNotFoundException.java
│   └── InsufficientBalanceException.java
│
└── security/
    ├── SecurityConfig.java         — endpoint access rules per role
    └── KeycloakJwtConverter.java   — extracts Keycloak roles from JWT
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

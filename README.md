# Banfico Training Program — Spring Boot Banking API

A REST API backend for a banking system built with Spring Boot 4.1.0. Handles customers, accounts, transactions, beneficiaries, payments, and fund transfers. Secured with Keycloak JWT authentication and role-based access control.

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

Spring Boot acts as an **OAuth2 resource server** — it never stores passwords. Every request must carry a valid Keycloak JWT. Spring verifies the token's signature using Keycloak's public key (`KEYCLOAK_JWK_URI`) and extracts roles from the `realm_access.roles` claim.

## Roles

| Role | Access |
|---|---|
| `user` | Own accounts, own transactions, own beneficiaries, transfers, own payments, own profile |
| `BankManager` | All customers, all accounts (including opening new ones), all transactions, all payments, register new users |
| `admin` | Everything — including delete, create managers, view managers |

Roles are assigned in Keycloak and embedded in the JWT. `KeycloakJwtConverter` maps them to Spring Security authorities (`ROLE_USER`, `ROLE_BANKMANAGER`, `ROLE_ADMIN`).

## Endpoints

### Public / System

| Method | URL | Access |
|---|---|---|
| GET | `/api/v1/health` | Public |
| GET | `/api/v1/info` | Public |

### Customers

| Method | URL | Access | Description |
|---|---|---|---|
| POST | `/api/v2/customers` | Admin, BankManager | Create Keycloak account + Customer DB record atomically |
| GET | `/api/v1/customers` | Admin, BankManager | List all customers |
| GET | `/api/v1/customers/{id}` | Admin, BankManager | Get customer by ID |
| PUT | `/api/v1/customers/{id}` | Admin | Update customer details |
| DELETE | `/api/v1/customers/{id}` | Admin | Delete customer |

`POST /api/v2/customers` creates both the Keycloak user (with `user` role and temporary password) and the DB customer record in a single call. If the DB save fails, the Keycloak user is automatically rolled back.

### Managers

| Method | URL | Access | Description |
|---|---|---|---|
| GET | `/api/v1/managers` | Admin | List all BankManager accounts from Keycloak |
| POST | `/api/v1/managers` | Admin | Create a Keycloak account with BankManager role |

### Accounts

| Method | URL | Access | Behaviour |
|---|---|---|---|
| GET | `/api/v1/accounts` | Authenticated | Staff → all accounts; User → own accounts only |
| GET | `/api/v1/accounts/{id}` | Authenticated | Get account by ID |
| GET | `/api/v1/accounts/lookup?accountNo=` | Authenticated | Look up account by account number |
| POST | `/api/v1/accounts` | Admin, BankManager | Open a new account for a customer |
| PUT | `/api/v1/accounts/{id}` | Admin | Update account |
| DELETE | `/api/v1/accounts/{id}` | Admin | Delete account |

### Transactions

Paginated — supports `?page=0&size=20&sort=transactionTime,desc`. Default: 20 per page, newest first. Response header `X-Total-Count` carries the total record count.

| Method | URL | Access | Behaviour |
|---|---|---|---|
| GET | `/api/v1/transactions?accountId=` | Authenticated | Staff → any account; User → own accounts only. Paginated. |

### Beneficiaries

| Method | URL | Access | Behaviour |
|---|---|---|---|
| GET | `/api/v1/beneficiaries` | Authenticated | Staff + `?customerId=` → specific customer; User → own list |
| POST | `/api/v1/beneficiaries` | User only | Add to own beneficiary list |
| PUT | `/api/v1/beneficiaries/{id}` | User only | Update nickname |
| DELETE | `/api/v1/beneficiaries/{id}` | User only | Remove beneficiary |

Staff can view beneficiaries but cannot add, edit, or delete them.

### Fund Transfer

| Method | URL | Access | Description |
|---|---|---|---|
| GET | `/api/v2/transfer/preview?fromAccountId=&recipientAccountNo=&amount=` | User only | Preview transfer — returns from account, recipient name, amount, estimated time for confirmation screen |
| POST | `/api/v2/transfer` | User only | Idempotent transfer — client provides a `paymentId` UUID as idempotency key |

**Payment confirmation flow:** Call the preview endpoint first, show the customer "Send £X from ACC-XXXX to John Doe (ACC-001) at 14:32?", then POST to execute. Idempotency: if a transfer with the same `paymentId` already completed, the cached result is returned. If it is still `PENDING`, a 409 is returned.

### Payments

Paginated — supports `?page=0&size=20&sort=initiatedAt,desc`. Default: 20 per page, newest first. Response header `X-Total-Count` carries the total record count.

| Method | URL | Access | Behaviour |
|---|---|---|---|
| GET | `/api/v1/payments` | Authenticated | Staff → all payments (optional `?accountId=`); User → own payments. Paginated. |
| GET | `/api/v1/payments/{paymentId}` | Authenticated | Get a single payment by ID |

Payment lifecycle: `PENDING` → `COMPLETED` / `FAILED`.

### Profile

| Method | URL | Access | Description |
|---|---|---|---|
| GET | `/api/v1/profile` | User only | Get own profile |
| PUT | `/api/v1/profile` | User only | Update firstName, lastName, phoneNumber (email and PAN are read-only) |

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

| Exception | HTTP Status |
|---|---|
| `ResourceNotFoundException` | 404 |
| `InsufficientBalanceException` | 400 |
| `MethodArgumentNotValidException` | 400 |
| `DataIntegrityViolationException` | 400 |
| `HttpClientErrorException` (409) | 409 |
| `RestClientException` | 502 |
| `Exception` | 500 |

## Project Structure

```
src/main/java/com/riyaz/banficotrainingprogram/
├── BanficoTrainingProgramApplication.java
│
├── customer/
│   ├── controller/   CustomerController.java, CustomerV2Controller.java, ManagerController.java, ProfileController.java
│   ├── dto/          CustomerRequest/Response, RegisterRequest/Response, CreateManagerRequest/Response, ManagerResponse.java
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
│   ├── dto/          TransactionRequest/Response, TransferRequest.java
│   ├── entity/       Transactions.java
│   ├── repository/   TransactionsRepo.java
│   └── service/      TransactionService.java, TransferService.java
│       └── impl/     TransactionServiceImpl.java, TransferServiceImpl.java
│
├── payment/
│   ├── controller/   PaymentController.java
│   ├── dto/          PaymentResponse.java
│   ├── entity/       Payment.java, PaymentStatus.java
│   ├── repository/   PaymentRepo.java
│   └── service/      PaymentService.java
│       └── impl/     PaymentServiceImpl.java
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

## Environment Variables

| Variable | Description |
|---|---|
| `DB_URL` | JDBC connection URL (e.g. `jdbc:postgresql://localhost:5432/bankapp`) |
| `DB_USERNAME` | Database username |
| `DB_PASSWORD` | Database password |
| `KEYCLOAK_JWK_URI` | Keycloak public key endpoint for JWT verification |
| `KEYCLOAK_ADMIN_URL` | Keycloak base URL for Admin API calls |
| `KEYCLOAK_ADMIN_USERNAME` | Master realm admin username |
| `KEYCLOAK_ADMIN_PASSWORD` | Master realm admin password |

Copy `.env.example` to `.env` and fill in your values.

## Running with Docker

```bash
# From the SpringBOOOO/ directory (where docker-compose.yml lives)
docker compose up --build -d

# Rebuild Spring Boot only
docker compose up --build -d spring-boot
```

Services after startup:
- Spring Boot API: `http://localhost:8080`
- Keycloak admin console: `http://localhost:8180`
- PostgreSQL: `localhost:5432`

## Author

Riyaz

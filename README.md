# Banfico Training Program

## Project Overview

This is a Spring Boot 4.1.0 application built with Java 17 that provides REST API endpoints for a Banking System, including customer management, account management, transactions, and beneficiaries.

## Current Task

Completed week 1. 
Current task: Banking CRUD API.

## Database and Environment Configuration

The application is connected to a PostgreSQL database hosted on Neon DB.

### Environment Variables

To run the application, configure the following environment variables (e.g., in a `.env` file or your system environment):

- DB_URL: The connection URL for the Neon DB database (e.g., jdbc:postgresql://...)
- DB_USERNAME: The database username
- DB_PASSWORD: The database password

## Entities

The system relies on the following core entities:

1. Customer
- Represents a bank customer.
- Fields: id, name, email, phone, etc.

2. Account
- Represents a customer's bank account.
- Fields: id, accountNo, accountType, balance, customer (Many-to-One mapping).

3. Transactions
- Represents a financial transaction on an account.
- Fields: id, amount, transactionType, timestamp, account (Many-to-One mapping).

4. Beneficiary
- Represents a beneficiary linked to a customer's account for easy transfers.
- Fields: id, customer (Many-to-One mapping), beneficiaryAccount (Many-to-One mapping), nickname.

## Implemented Endpoints and Responses

### System Endpoints

1. Health Endpoint
- URL: `/api/health`
- Method: GET
- Purpose: Returns the health status of the application
- Response: String "UP"
- Status Code: 200 OK

2. Info Endpoint
- URL: `/api/info`
- Method: GET
- Purpose: Returns detailed application and git information
- Response: JSON object containing application name, version, branch, commitId, and commitTime
- Status Code: 200 OK

### Customer Endpoints

1. Create Customer
- URL: `/api/customers`
- Method: POST
- Response: CustomerResponse object

2. Get All Customers
- URL: `/api/customers`
- Method: GET
- Response: List of CustomerResponse objects

3. Get Customer by ID
- URL: `/api/customers/{id}`
- Method: GET
- Response: CustomerResponse object

4. Update Customer
- URL: `/api/customers/{id}`
- Method: PUT
- Response: CustomerResponse object

5. Delete Customer
- URL: `/api/customers/{id}`
- Method: DELETE
- Response: String "Customer {id} has been Deleted"

### Account Endpoints

1. Create Account
- URL: `/api/accounts`
- Method: POST
- Response: AccountResponse object

2. Get All Accounts
- URL: `/api/accounts`
- Method: GET
- Response: List of AccountResponse objects

3. Get Account by ID
- URL: `/api/accounts/{id}`
- Method: GET
- Response: AccountResponse object

4. Update Account
- URL: `/api/accounts/{id}`
- Method: PUT
- Response: AccountResponse object

5. Delete Account
- URL: `/api/accounts/{id}`
- Method: DELETE
- Response: String "Account {id} has been Deleted"

### Transaction Endpoints

1. Create Transaction
- URL: `/api/accounts/{accountId}/transactions`
- Method: POST
- Response: TransactionResponse object

2. Get Transactions for Account
- URL: `/api/accounts/{accountId}/transactions`
- Method: GET
- Response: List of TransactionResponse objects

### Beneficiary Endpoints

1. Create Beneficiary
- URL: `/api/customers/{customerId}/beneficiaries`
- Method: POST
- Response: BeneficiaryResponse object

2. Get Beneficiaries for Customer
- URL: `/api/customers/{customerId}/beneficiaries`
- Method: GET
- Response: List of BeneficiaryResponse objects

3. Update Beneficiary Nickname
- URL: `/api/customers/{customerId}/beneficiaries/{beneficiaryId}`
- Method: PUT
- Response: BeneficiaryResponse object

4. Delete Beneficiary
- URL: `/api/customers/{customerId}/beneficiaries/{beneficiaryId}`
- Method: DELETE
- Response: String "Beneficiary {beneficiaryId} has been Deleted"

## Global Exception Handling

All errors across the API return a consistent JSON response shape:

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Account not found with id: 3f2e...",
  "timestamp": "2026-08-10T14:30:00"
}
```

### How it works

When a service cannot find an entity or a request is invalid, it throws a custom exception instead of returning null. `GlobalExceptionHandler` (annotated with `@RestControllerAdvice`) intercepts every exception thrown from any controller and maps it to the right HTTP response automatically — no changes needed in the controllers.

### Exception Types

| Exception | Thrown When | HTTP Status |
|---|---|---|
| `ResourceNotFoundException` | Entity not found in DB | 404 Not Found |
| `InsufficientBalanceException` | DEBIT amount exceeds account balance | 400 Bad Request |
| `MethodArgumentNotValidException` | `@Valid` check fails on request body | 400 Bad Request |
| `Exception` (catch-all) | Any other unexpected error | 500 Internal Server Error |

### Flow

```
Request → Controller → Service → throws Exception
                                        ↓
                          GlobalExceptionHandler catches it
                                        ↓
                          Returns ErrorResponse JSON to client
```

## Project Structure

```
src/main/java/com/riyaz/banficotrainingprogram/
├── BanficoTrainingProgramApplication.java
├── Controller/
│   ├── AccountController.java
│   ├── BeneficiaryController.java
│   ├── CustomerController.java
│   ├── SystemController.java
│   └── TransactionController.java
├── Entity/
│   ├── Account.java
│   ├── Beneficiary.java
│   ├── Customer.java
│   └── Transactions.java
├── Service/
│   ├── AccountService.java
│   ├── BeneficiaryService.java
│   ├── CustomerService.java
│   ├── SystemService.java
│   ├── TransactionService.java
│   └── impl/
│       ├── AccountServiceImpl.java
│       ├── BeneficiaryServiceImpl.java
│       ├── CustomerServiceImpl.java
│       ├── SystemServiceImpl.java
│       └── TransactionServiceImpl.java
├── dto/
│   ├── AccountRequest.java
│   ├── AccountResponse.java
│   ├── BeneficiaryRequest.java
│   ├── BeneficiaryResponse.java
│   ├── ErrorResponse.java
│   ├── Healthresponse.java
│   ├── InfoResponse.java
│   ├── TransactionRequest.java
│   └── TransactionResponse.java
├── exception/
│   ├── GlobalExceptionHandler.java
│   ├── InsufficientBalanceException.java
│   └── ResourceNotFoundException.java
├── metadata/
│   └── GitInfoProvider.java
└── repository/
    ├── AccountRepo.java
    ├── BeneficiaryRepo.java
    ├── CustomerRepo.java
    └── TransactionsRepo.java
```

## Technology Stack

- Java 17
- Spring Boot 4.1.0
- Spring Web MVC
- Spring Data JPA
- PostgreSQL (Neon DB)
- Maven
- Git Commit ID Maven Plugin (for automatic version tracking)

## Building and Running

### Build the Project
```bash
mvn clean package
```

### Run the Application
```bash
mvn spring-boot:run
(or)
java -jar target/Banfico-Training-program-0.0.1-SNAPSHOT.jar
```

The application starts on port 8080 by default.

## Dependencies

- spring-boot-starter-webmvc - Web MVC framework
- spring-boot-starter-data-jpa - JPA data access
- postgresql - PostgreSQL database driver
- spring-boot-starter-validation - Request validation
- spring-boot-starter-test - Testing framework
- git-commit-id-maven-plugin - Git metadata capture

## Version

Current Version: 0.0.1-SNAPSHOT

## Author
 Riyaz

# Banfico Training Program

## Project Overview

This is a Spring Boot 4.1.0 application built with Java 17 that provides REST API endpoints for system monitoring and application information retrieval.

## Current Task

Creating and implementing two main API endpoints for health checking and application information retrieval.

## Implemented Endpoints

### 1. Health Endpoint
- URL: `/api/health`
- Method: GET
- Purpose: Returns the health status of the application
- Response: String "UP"
- Status Code: 200 OK

### 2. Info Endpoint
- URL: `/api/info`
- Method: GET
- Purpose: Returns detailed application and git information
- Response: JSON object with the following fields:
  - application: Application name
  - version: Application version
  - branch: Git branch name
  - commitId: Git commit ID (hash)
  - commitTime: Timestamp of the commit (Instant format)
- Status Code: 200 OK
- Response Type: ResponseEntity<InfoResponse>

## Additional Endpoints

- GET `/api/` - Root endpoint returning "hello world"
- GET `/api/love` - Returns "LOVE"
- POST `/api/love` - Echo endpoint that appends request body to "LOVE"

## Technology Stack

- Java 17
- Spring Boot 4.1.0
- Spring Web MVC
- Maven
- Git Commit ID Maven Plugin (for automatic version tracking)

## Project Structure

```
src/main/java/com/riyaz/banficotrainingprogram/
├── BanficoTrainingProgramApplication.java
├── Controller/
│   └── SystemController.java          (Contains health and info endpoints)
├── Service/
│   ├── SystemService.java             (Interface)
│   └── impl/
│       └── SystemServiceImpl.java      (Implementation)
├── dto/
│   ├── InfoResponse.java              (Response DTO for info endpoint)
│   └── Healthresponse.java            (Response DTO for health endpoint)
└── metadata/
    └── GitInfoProvider.java           (Git metadata provider)
```

## What Has Been Done

1. Created SystemController with REST endpoints
   - Implemented /api/health endpoint
   - Implemented /api/info endpoint returning ResponseEntity


2. Created InfoResponse DTO
   - Fields: application, version, branch, commitId, commitTime
   - commitTime uses Instant type for proper timestamp handling


3. Created SystemService interface and implementation
   - Service layer handles business logic
   - getInfo() method retrieves application information


4. Integrated Git metadata
   - Git Commit ID Maven Plugin configured in pom.xml
   - Automatic capture of commit information at build time


5. Fixed Issues
   - Added @Service annotation to SystemServiceImpl
   - Removed duplicated plugin configuration
   - Changed response to ResponseEntity for proper HTTP response handling
   - Changed commitTime type to Instant (removed String-based approach)

## Building and Running

### Build the Project
```bash
mvn clean package
```

### Run the Application
```bash
mvn spring-boot:run
```

The application starts on port 8080 by default.



## Testing the Endpoints

### Test Health Endpoint
```bash
curl http://localhost:8080/api/health
```
Expected Response: UP

### Test Info Endpoint
```bash
curl http://localhost:8080/api/info
```
Expected Response:
```json
{
  "application": "Banfico-Training-program",
  "version": "0.0.1-SNAPSHOT",
  "branch": "feature/info",
  "commitId": "<commit-hash>",
  "commitTime": "<instant-timestamp>"
}
```

## Dependencies

- spring-boot-starter-webmvc - Web MVC framework
- spring-boot-starter-webmvc-test - Testing framework
- git-commit-id-maven-plugin - Git metadata capture

## Version

Current Version: 0.0.1-SNAPSHOT

## Author
 Riyaz

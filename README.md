# BankApp

Full-stack banking portal — Spring Boot 4.1 REST API, Next.js 16 frontend, Keycloak OIDC, PostgreSQL. Built as part of the Banfico training program.

---

## Architecture

```
Browser
  │  HTTPS (443)
  ▼
nginx / OpenResty (gateway)
  ├─ /auth/*        ──▶ Keycloak :8080 (OIDC, login page)
  ├─ /api/auth/*    ──▶ Next.js  :3000 (NextAuth callbacks)
  ├─ /api/*         ──JWT verify──▶ Spring Boot :8080 ──JDBC──▶ PostgreSQL
  └─ /*             ──▶ Next.js  :3000 (frontend)
```

JWT validation happens in the nginx Lua layer (`lua-resty-jwt`). Spring Boot never sees a raw JWT — it reads role claims from the `X-User-Roles` header injected by nginx. Nothing except nginx is exposed to the host.

### Local dev (single machine)

All five containers (`gateway`, `spring-boot`, `postgres`, `keycloak`, `nextjs`) run on a single Docker network. Only nginx is port-mapped to the host (`80`, `443`).

### Production (two VPC instances)

| Instance | Services | Exposed |
|----------|----------|---------|
| Instance 1 | Spring Boot + PostgreSQL | port 8080 on VPC private IP (gateway → Spring Boot) |
| Instance 2 | nginx + Next.js + Keycloak | 80/443 public; port 8080 on VPC private IP (Spring Boot → Keycloak Admin API) |

---

## Prerequisites

- Docker & Docker Compose
- A `.env` file in the root (copy from `.env.example`)
- A TLS certificate pair in `api-gateway/certs/` (see Quick Start)

---

## Quick Start (local dev)

```bash
# 1 — Generate a self-signed TLS cert (only needed once)
cd api-gateway/certs && bash generate-certs.sh && cd ../..

# 2 — Create .env from example
cp .env.example .env  # then fill in secrets

# 3 — Start everything
docker compose up --build -d

# Tail logs
docker compose logs -f gateway
docker compose logs -f spring-boot
```

App available at **https://localhost** (accept the self-signed cert warning in your browser).

| Path | Service |
|------|---------|
| `https://localhost` | Next.js frontend |
| `https://localhost/api/v1/health` | Spring Boot health |
| `https://localhost/auth/` | Keycloak admin / OIDC |
| `https://localhost/gateway/health` | nginx health |

---

## Testing with Postman

Import `Banfico-Training program/postman/BankApp-API.postman_collection.json`.

All requests go through the full gateway stack at `https://localhost`. JWT validation happens in nginx — Spring Boot never sees a raw token.

1. **Auth → Get Token** (pick any user) — test script saves `access_token` to `{{token}}`
2. Send any API request — `Authorization: Bearer {{token}}` is already set on every request
3. Token expired? Re-run step 1

> Disable SSL certificate verification in Postman Settings → General (self-signed cert on localhost).

Log in as different users to test RBAC. Same endpoint, different token → different result:

| Login as | Role | Example: GET /api/v1/accounts |
|----------|------|-------------------------------|
| riyaz | ADMIN, USER | All accounts bank-wide |
| manager | BANKMANAGER | All accounts bank-wide |
| priya / arjun / sneha / rahul | USER | Own accounts only |

---

## Production deploy

```bash
# Instance 1
cp .env.example .env.instance1
# set INSTANCE2_PRIVATE_IP in .env.instance1
docker compose -f docker-compose.instance1.yml --env-file .env.instance1 up -d

# Instance 2
cp .env.example .env.instance2
# set INSTANCE1_PRIVATE_IP, DOMAIN in .env.instance2
cd api-gateway/certs && bash generate-certs.sh && cd ../..   # or use real cert
docker compose -f docker-compose.instance2.yml --env-file .env.instance2 up -d
```

---

## Environment Variables (`.env`)

```env
DB_NAME=bankapp
DB_USERNAME=bankapp
DB_PASSWORD=<your-db-password>
KC_ADMIN_PASSWORD=<keycloak-admin-password>
KEYCLOAK_CLIENT_SECRET=<get-from-keycloak-admin-console>
NEXTAUTH_SECRET=<generate: openssl rand -base64 32>
```

---

## Default Users

Seeded via `keycloak/realm-export.json` on first start.

| Username | Password      | Role        |
|----------|---------------|-------------|
| riyaz    | Riyaz@1234    | admin, user |
| manager  | Manager@1234  | BankManager |
| priya    | Priya@1234    | user        |
| arjun    | Arjun@1234    | user        |
| sneha    | Sneha@1234    | user        |
| rahul    | Rahul@1234    | user        |

---

## API Overview

All API responses are wrapped in `ApiResponse<T>`:

```json
{ "status": 200, "message": "...", "data": {...}, "timestamp": "2026-10-04T10:30:00" }
```

| Group              | Endpoints                                                 | Role              |
|--------------------|-----------------------------------------------------------|-------------------|
| System             | `GET /api/v1/health`, `GET /api/v1/info`                  | Public            |
| Register (v2)      | `POST /api/v2/customers`                                  | ADMIN, BANKMANAGER|
| Customers (v1)     | `GET/PUT/DELETE /api/v1/customers/{id}`                   | ADMIN (MANAGER read) |
| Accounts (v1)      | `GET/POST/PUT/DELETE /api/v1/accounts`, `/lookup`         | ADMIN, BANKMANAGER|
| Transactions (v1)  | `GET /api/v1/transactions?accountId=&page=&size=`         | Authenticated     |
| Payments (v1)      | `GET /api/v1/payments`, `GET /api/v1/payments/{id}`       | Authenticated     |
| Transfer (v2)      | `GET /api/v2/transfer/preview`, `POST /api/v2/transfer`   | USER              |
| Beneficiaries (v1) | `GET/POST/PUT/DELETE /api/v1/beneficiaries`               | USER (GET: all)   |
| Profile (v1)       | `GET/PUT /api/v1/profile`                                 | USER              |
| Managers (v1)      | `GET/POST /api/v1/managers`                               | ADMIN             |

Import `Banfico-Training program/postman/BankApp-API.postman_collection.json` into Postman for full request/response examples.

For the complete product and technical specification see `Product.html`.

---

## Key Design Decisions

**nginx JWT gateway** — JWT validation (RS256, Keycloak public key) runs in the nginx Lua layer using `lua-resty-jwt`. Spring Boot trusts the `X-User-Roles` header injected by nginx rather than parsing JWTs itself. The public key is fetched once from `/auth/realms/bankapp` and cached for 1 hour in `lua_shared_dict`.

**Single issuer, routed through nginx** — Keycloak runs under the `/auth` relative path and reads `X-Forwarded-*` from nginx (`KC_PROXY_HEADERS: xforwarded`), so the JWT `iss` claim is `https://localhost/auth/realms/bankapp` — the same URL the browser uses. NextAuth's server-side token exchange and token refresh use that same public issuer; the Next.js container patches `/etc/hosts` at startup so `localhost` resolves to the gateway container IP, routing the backchannel call through nginx instead of hitting Keycloak directly. One hostname everywhere avoids OIDC discovery/issuer mismatches.

**nginx proxy buffers for Auth.js** — NextAuth v5 writes large encrypted session cookies (chunked `Set-Cookie` headers, ~5–10 KB) on every OIDC callback and protected-page render. The `/`, `/api/auth/`, and `/auth/` locations raise `proxy_buffer_size` to 128k (`proxy_buffers 4 256k`) so these responses don't overflow nginx's default 4k header buffer and return 502.

**Atomic customer onboarding** — `POST /api/v2/customers` creates a Keycloak user, Customer record, and first Account in a single `@Transactional` saga. If the DB save fails, the Keycloak user is deleted automatically.

**Auto-generated account numbers** — 12-digit unique numbers are generated server-side using `ThreadLocalRandom` with a collision check loop. Clients never supply `accountNo`.

**ApiResponse wrapper** — every controller returns `ResponseEntity<ApiResponse<T>>`. The frontend Axios interceptor in `BankApp/lib/api.ts` unwraps `response.data.data → response.data` transparently.

**Custom Keycloak login theme** — the `bankapp` theme (`keycloak/themes/bankapp/`) overrides the default Keycloak UI with FreeMarker templates and CSS that match BankApp's design. Mounted as a read-only volume.

**Idempotent transfers** — `POST /api/v2/transfer` uses a client-generated `paymentId` UUID as an idempotency key. Re-sending the same `paymentId` returns the existing payment without double-charging.

# Employee Data Service

A small REST service that stores employee records in PostgreSQL. The social
security number is encrypted before it reaches the database, and no endpoint
ever returns it. Built as a take-home assignment.

## Run locally

Requirements: Docker, JDK 21+ (no Maven needed - wrapper included).

```bash
# 1. start the database
docker compose up -d

# 2. set the encryption key for this terminal session
export SSN_ENCRYPTION_KEY=$(openssl rand -base64 32)

# 3. run the app on http://localhost:8080
./mvnw spring-boot:run
```

Tests (database must be running and the key set):

```bash
./mvnw test
```

(!) Keep the same key between restarts - records saved with one key can't be
read with another. To start fresh: `docker compose down -v`.

## API

| Method | Path | What it does | Status codes |
|---|---|---|---|
| POST | `/employees` | create an employee | 201, 400 |
| GET | `/employees/{id}` | get one employee | 200, 404 |
| GET | `/employees` | list employees, paginated (`?page=0&size=20&sort=lastName,asc`) | 200 |

```bash
curl -X POST http://localhost:8080/employees \
  -H "Content-Type: application/json" \
  -d '{"firstName":"Jan","lastName":"Kowalski","dateOfBirth":"1990-05-15","gender":"male","socialSecurityNumber":"123-45-6789"}'
```

```json
{"id":1,"firstName":"Jan","lastName":"Kowalski","dateOfBirth":"1990-05-15","gender":"male"}
```

Note there is no SSN in the response - that's on purpose. Invalid input returns
400 with a per-field error map, a missing record returns 404.

## Tech choices and why

- **Java 21 + Spring Boot** - the standard stack for a service like this - it
  gives me the web layer, validation, database access.
- **PostgreSQL in Docker Compose** - the task asks for a real database.
  Employee records have a fixed shape, so a relational database fits, and
  Docker Compose starts it with one command. I considered an embedded H2
  database (easier setup), but a real Postgres is closer to what the task
  asks for.
- **Encryption (AES-256-GCM) instead of hashing** - the key decision. Hashing
  is one-way: good for passwords, because you never need the original value
  back. An HR system does need the SSN back (payroll, tax reporting), so I
  chose encryption. AES-GCM also detects tampering - if someone changes the
  stored value, decryption fails instead of returning wrong data. The key
  comes from an environment variable and is never in the repo. Encryption is
  wired in as a JPA converter, so no code path can skip it - and the response
  DTO simply has no SSN field, so it can't leak by accident.
- **No Lombok** - for a project this size the boilerplate is small anyway, so
  I kept the code plain instead of adding another dependency and annotation
  magic.

## What I'd do differently with more time

- **Flyway migrations** instead of `ddl-auto=update` - schema changes as
  versioned scripts instead of Hibernate touching the schema on startup.
- **Real key management** - the env variable is fine for a demo; in production
  the key would live in a secrets manager (e.g. Vault/KMS) with key rotation,
  and the stored value would include a key version.
- **Integration tests with Testcontainers** - test the whole flow on a real
  Postgres, including checking that the stored column really holds ciphertext.

## AI usage

I used Claude (Claude Code) throughout, but I discussed each step with it, so I understand and can defend all of it.

Things I changed or rejected from AI suggestions:

- **Didn't take the first answer on SSN protection** - I asked for the
  hashing vs. encryption tradeoff first and made the call myself (encryption,
  for the reasons above).
- **Changed the random generator in the encryptor** - the suggested code
  called `SecureRandom.getInstanceStrong()` on every encryption. That variant
  can block on Linux, and there is no reason to build a new generator per
  call, so I replaced it with a single shared `new SecureRandom()`.
- **Rejected Lombok** even though most examples use it - I preferred plain
  code I can explain over annotation magic.
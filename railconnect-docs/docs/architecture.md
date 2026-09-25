# RAILCONNECT — System Architecture & Topology

## 1. Architectural Philosophy: The Modular Micro-Monolith

RAILCONNECT is architected as an enterprise-grade **Modular Micro-Monolith**. While the codebase is structured across isolated, self-contained Maven modules distributed in dedicated Git repositories to enforce strict boundaries of concern, the system compiles and links together into a single high-performance runnable Spring Boot artifact (`railconnect-backend-1.0.0.jar`).

This architectural choice provides key engineering advantages:
1. **Zero Distributed Network Latency**: Internal inter-module operations (such as seat locking before booking creation, or atomic berth swapping) execute within the JVM boundary via type-safe Spring service injection and ACID-compliant relational transactions, avoiding distributed two-phase commit overhead and network partitions.
2. **Strict Module Isolation**: Each repository maintains its own Maven POM, independent data models, DTOs, and domain services. Circular dependencies are strictly eliminated through a linear dependency hierarchy.
3. **Seamless Zero-Setup Execution**: The unified server boots in seconds using an embedded in-memory H2 database in MySQL compatibility mode, while supporting instantaneous zero-code change migration to production MySQL instances.

---

## 2. Dependency Hierarchy & Repository Flow

The modular dependencies flow in a strict, acyclic directed graph:

```
[ railconnect-frontend ] (Client Browser)
        │
        │ HTTP / REST / JSON (Port 8080)
        ▼
[ railconnect-backend ] (Runnable Runner & ZXing QR Signer)
        │
        ├──► [ railconnect-admin ] (KPI Aggregation & TTE Terminal)
        │           │
        │           ├──► [ railconnect-berth-exchange ] (10-Rule Swap Engine)
        │           │           │
        │           │           └──► [ railconnect-booking ] (Fare Formula, PNR, Search)
        │           │                       │
        │           │                       ├──► [ railconnect-seat-engine ] (Locks & Allocation)
        │           │                       │           │
        │           │                       │           └──► [ railconnect-auth ] (RBAC, JWT)
        │           │                       │
        │           │                       └──► [ railconnect-auth ]
        │           │
        │           └──► [ railconnect-weather ] (Live Route Telemetry)
        │
        └──► [ railconnect-database ] (DDL Schemas, Seeds & Fixtures)
```

### Module Responsibilities:

* **`railconnect-auth`**:
  * Spring Security 6 integration with stateless JWT filter.
  * Role-Based Access Control: `ROLE_PASSENGER`, `ROLE_INSPECTOR`, `ROLE_ADMIN`.
  * BCrypt password hashing and user repository.
* **`railconnect-seat-engine`**:
  * Coach layouts and physical seat models.
  * 10-minute thread-safe dynamic seat locking with ConcurrentHashMap and automated eviction.
  * Intelligent seat allocation heuristics utilizing `PriorityQueue`, `HashMap`, and `Queue`.
* **`railconnect-booking`**:
  * Train and station routing graph.
  * Master fare formula calculation ($Base + Reservation + Superfast + GST - Concession$).
  * Collision-free 10-digit numerical PNR generation algorithm.
  * Reservation creation, passenger ticket persistence, and cancellation refund processing.
* **`railconnect-weather`**:
  * Journey weather advisory service.
  * External OpenWeatherMap API telemetry client.
  * Deterministic Indian station meteorological fallback with `"Demo Weather Data"` classification.
* **`railconnect-berth-exchange`**:
  * Peer-to-peer co-passenger berth exchange engine.
  * 10 strict railway business validation rules.
  * Two-phase atomic seat exchange with audit logging.
* **`railconnect-admin`**:
  * Central analytics and KPI aggregation.
  * Train scheduling, station master data, and dynamic fare rule management.
  * Onboard Traveling Ticket Examiner (TTE) digital pass verification terminal.
* **`railconnect-backend`**:
  * Main application entry point (`RailConnectApplication.java`).
  * ZXing QR code image generation for Digital Travel Passes.
  * Automated entity hydration and sample fixture loader (`DataLoader.java`).
  * Global exception handling and cross-origin resource sharing (CORS) filter.

---

## 3. Multi-Tier Layered Architecture

Each backend module follows a clean, decoupled 4-tier layered architecture:

```
┌─────────────────────────────────────────────────────────────┐
│                      Presentation Layer                     │
│  (HTML5, CSS3, JavaScript SPA, i18n Dictionary Engine)       │
└──────────────────────────────┬──────────────────────────────┘
                               │ HTTPS / JSON / REST
┌──────────────────────────────▼──────────────────────────────┐
│                      REST Controller Layer                  │
│  (@RestController, @RequestMapping, @PreAuthorize, DTOs)    │
└──────────────────────────────┬──────────────────────────────┘
                               │ Service Invocation / DTOs
┌──────────────────────────────▼──────────────────────────────┐
│                      Service / Business Layer               │
│  (@Service, @Transactional, Rule Engine, Algorithms, DSA)   │
└──────────────────────────────┬──────────────────────────────┘
                               │ Entities / Spring Data JPA
┌──────────────────────────────▼──────────────────────────────┐
│                      Data Access Layer                      │
│  (@Repository, JpaRepository, Derived Queries, Native SQL)  │
└──────────────────────────────┬──────────────────────────────┘
                               │ JDBC / Hibernate ORM
┌──────────────────────────────▼──────────────────────────────┐
│                      Persistence Layer                      │
│  (Embedded H2 MySQL Mode or Standalone MySQL 8.0 Engine)    │
└─────────────────────────────────────────────────────────────┘
```

---

## 4. Zero-Setup Dual-Database Strategy

To satisfy testing and deployment requirements without demanding local database installation, RAILCONNECT implements a dual-profile configuration:

### Mode A: Embedded H2 Engine (Default Zero-Setup)
* Activated by default in `application.properties`.
* Database URL: `jdbc:h2:mem:railconnectdb;MODE=MySQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1`
* Schema is initialized automatically via Hibernate `ddl-auto: update`, and populated with realistic railway master data via `DataLoader.java` on application startup.
* Provides instant testing out of the box with zero external dependencies.

### Mode B: Enterprise MySQL 8.0 Engine (Production Profile)
* Activated with Spring profile: `--spring.profiles.active=mysql`.
* Database URL: `jdbc:mysql://localhost:3306/railconnect_db?useSSL=false&serverTimezone=UTC`
* Uses official DDL schemas from `railconnect-database/database/schema.sql` and master seed data from `railconnect-database/database/seed.sql`.
* Fully supports persistent InnoDB relational transactions, foreign key cascades, and row-level locking.

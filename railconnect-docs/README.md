# RAILCONNECT — Central Documentation Repository

**Repository 10 of 10** in the **RAILCONNECT** Railway Ticket Booking & Digital Travel Pass Platform.

---

## 🚆 System Overview

**RAILCONNECT** is an enterprise-grade, full-stack railway booking, digital pass issuing, and intelligent journey assistance platform engineered for college 2nd-year *Advanced Programming Practice*. It demonstrates rigorous Object-Oriented Programming (OOP) design patterns, advanced Data Structures and Algorithms (DSA), high-concurrency seat locking, graph-based routing, real-time meteorological journey warnings, peer-to-peer berth exchange, and multi-role operations management.

### The 10-Repository Modular Architecture

In strict adherence to project requirements, the entire platform is partitioned across **exactly 10 Git repositories**:

| # | Repository Name | Type / Technology | Core Responsibility |
|---|-----------------|-------------------|---------------------|
| 1 | `railconnect-frontend` | HTML5, CSS3, ES6 JS | Passenger portal, dynamic seat map, multilingual UI (EN, HI, TE, TA), and admin console |
| 2 | `railconnect-backend` | Java 17, Spring Boot 3.3.4 | Central runnable service, orchestration layer, ZXing QR generation, data loader |
| 3 | `railconnect-database` | SQL, DDL, DML | 18 relational tables, schema constraints, indexes, master seeds, demo fixtures |
| 4 | `railconnect-auth` | Spring Security 6, JWT | RBAC (Passenger, Inspector, Admin), HMAC SHA-256 tokens, BCrypt password hashing |
| 5 | `railconnect-booking` | Spring Data JPA, REST | Graph train search, fare formula calculation, 10-digit PNR generator, cancellations |
| 6 | `railconnect-seat-engine` | Java Concurrency, DSA | 10-minute dynamic locking, PriorityQueue/HashMap intelligent allocation, coach layouts |
| 7 | `railconnect-weather` | REST, OpenWeatherMap API | Live route telemetry, weather severity analysis, Indian station mock fallback |
| 8 | `railconnect-berth-exchange` | Java Rule Engine | 10-rule passenger-to-passenger berth swap validator and atomic two-phase seat swap |
| 9 | `railconnect-admin` | Spring Data JPA, REST | Executive KPI dashboard, train & fare configuration, TTE verification terminal |
| 10 | `railconnect-docs` | Markdown Documentation | Complete architectural, algorithmic, OOP, API, testing, and deployment documentation |

---

## 📚 Table of Contents

Explore the detailed technical documentation sections below:

1. [**System Architecture & Topology**](docs/architecture.md) — Multi-tier micro-monolith design, module dependency graph, zero-setup dual-database strategy.
2. [**Relational Database Schema**](docs/database.md) — 18 relational tables, primary/foreign keys, indexes, triggers, and sample data.
3. [**Comprehensive REST API Reference**](docs/api.md) — Detailed endpoint specifications, request/response JSON payloads, and HTTP status codes.
4. [**Data Structures & Algorithms (DSA)**](docs/algorithms.md) — Dijkstra graph routing, collision-free PNR generator, dynamic concurrency seat locking, PriorityQueue allocation, and 10-rule swap engine.
5. [**Object-Oriented Programming (OOP) Concepts**](docs/oop-concepts.md) — Encapsulation, inheritance, polymorphism, abstraction, design patterns, and Java code citations.
6. [**Testing & Quality Assurance**](docs/testing.md) — Unit tests (JUnit 5, Mockito), integration tests, test execution matrices, and verification commands.
7. [**Installation & Deployment Guide**](docs/installation.md) — Zero-setup one-click run instructions for H2 and production MySQL setup instructions.
8. [**Passenger User Guide**](docs/user-guide.md) — Step-by-step traveler walkthrough from train search to QR travel pass and berth exchange.
9. [**Admin & TTE Operations Manual**](docs/admin-guide.md) — Guide for station managers, administrators, and Traveling Ticket Examiners (TTEs).
10. [**UI Wireframes & Screenshots Guide**](docs/screenshots.md) — Markdown wireframes and visual UI layout breakdowns for all 14 frontend pages.

---

## ⚡ Quick Start

```bash
# 1. Compile and package the entire backend system
cd railconnect-backend
mvn clean install

# 2. Run the unified Spring Boot application (Zero-Setup with embedded H2)
mvn spring-boot:run

# 3. Serve the frontend application
cd ../railconnect-frontend
python -m http.server 3000
```
Open [http://localhost:3000](http://localhost:3000) in your web browser. Default accounts:
* **Passenger**: `rahul_sharma` / `password123`
* **Inspector (TTE)**: `inspector_anand` / `password123`
* **Admin**: `admin` / `password123`

# RAILCONNECT — Main Central Backend (`railconnect-backend`)

The central integration engine, Spring Boot runtime orchestrator, and Digital Travel Pass QR generator for **RailConnect: Intelligent Railway Booking, Digital Travel Pass & Journey Assistance System**.

## 📌 Architectural Hub
`railconnect-backend` brings together all micro-modules into a coherent, production-ready railway application:
* `railconnect-auth` (Authentication, JWT Security, RBAC)
* `railconnect-seat-engine` (10-min Dynamic Locking, Intelligent Auto-Allocation)
* `railconnect-booking` (Train Graph Routing, Fare Formula Engine, PNR Allocator)
* `railconnect-weather` (Journey Weather Forecasts & Advisory Alerts)
* `railconnect-berth-exchange` (10-Rule P2P Berth Exchange State Machine)
* `railconnect-admin` (Executive KPI Dashboard, Operations & Inspector Verifier)

```
                ┌─────────────────────┐
                │  RailConnect UI     │
                │  Frontend Repo      │
                └──────────┬──────────┘
                           │
                           ▼
                ┌─────────────────────┐
                │  Java Spring Boot   │
                │  Backend Repo       │
                └──────────┬──────────┘
                           │
      ┌────────────────────┼─────────────────────┐
      │                    │                     │
      ▼                    ▼                     ▼
  Auth Module         Booking Module       Seat Engine
  Repo 4              Repo 5               Repo 6
      │                    │                     │
      │                    ▼                     │
      │               Weather Module            │
      │               Repo 7                     │
      │                                           │
      │                    ▼                     │
      │             Berth Exchange               │
      │             Repo 8                        │
      │                                           │
      └────────────────────┬─────────────────────┘
                           ▼
                ┌─────────────────────┐
                │      MySQL          │
                │   Database Repo     │
                │       Repo 3        │
                └─────────────────────┘
```

## 🛠️ Complete Consolidated API Endpoints
* `/api/auth/**` — Registration, JWT login, authentication status
* `/api/trains/**` — Station listings, train route searches, class availability
* `/api/seats/**` — Visual coach layouts, dynamic 10-minute locking, auto-allocation
* `/api/bookings/**` — Reservation issuance, ticket history, cancellations
* `/api/pnr/**` — 10-digit PNR lookup and journey telemetry
* `/api/weather/**` — Departure & destination weather advisories
* `/api/berth-exchange/**` — P2P berth swap proposals, responses, and review
* `/api/admin/**` — Train/station/fare operations, system metrics, and audit logs
* `/api/tickets/verify` — Inspector pass verification terminal

## 🚀 Running the Central System
```bash
# Clean, compile, and run the backend
mvn spring-boot:run
```
The server will start on port `8080` with sample data pre-populated and ready for UI interaction.

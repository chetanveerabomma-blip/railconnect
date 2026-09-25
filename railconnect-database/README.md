# RAILCONNECT — Database Repository (`railconnect-database`)

Complete relational MySQL database design and migration scripts for the **RailConnect Intelligent Railway Booking, Digital Travel Pass & Journey Assistance System**.

## 📌 Repository Overview
This repository contains the complete DDL schema, master seed data, and realistic demo data powering the RailConnect platform.

### Files
* `database/schema.sql` — Complete schema DDL (18 relational tables, primary keys, foreign keys, unique constraints, performance indexes).
* `database/seed.sql` — Core reference data including stations, fare calculation rules, train classifications, coach configurations, and berth blueprints.
* `database/sample-data.sql` — Realistic demonstration data including test accounts (Passenger, Admin, Inspector), confirmed bookings with 10-digit PNRs, live weather alerts, and berth exchange requests.

---

## 🗄️ Relational Schema & Tables

| Table | Description | Key Constraints |
|---|---|---|
| `users` | Passenger, Admin, and Ticket Inspector user credentials | `UNIQUE(username)`, `UNIQUE(email)`, `UNIQUE(phone)` |
| `roles` | RBAC role definitions (`ROLE_PASSENGER`, `ROLE_ADMIN`, `ROLE_INSPECTOR`) | `UNIQUE(name)` |
| `passengers` | Extended passenger profile details & language preference | `FOREIGN KEY (user_id)` |
| `admins` | Administrative personnel records & departments | `FOREIGN KEY (user_id)`, `UNIQUE(employee_id)` |
| `stations` | Railway stations across India with geo-coordinates | `UNIQUE(code)` |
| `trains` | Trains (Vande Bharat, Rajdhani, Express, Shatabdi) | `UNIQUE(train_number)` |
| `train_routes` | Ordered sequence of station stops & distance (km) | `UNIQUE(train_id, stop_sequence)` |
| `coaches` | Train coaches (1A, 2A, 3A, SL, CC, 2S) | `UNIQUE(train_id, coach_number)` |
| `seats` | Berths/Seats (LOWER, MIDDLE, UPPER, SIDE_LOWER, etc.) | `UNIQUE(coach_id, seat_number)` |
| `fare_rules` | Distance-based base fare, reservation & superfast charges | `UNIQUE(train_type, coach_type)` |
| `bookings` | Ticket reservations with 10-digit unique PNR | `UNIQUE(pnr_number)` |
| `booking_passengers` | Individual passengers per booking & allocated seat | `FOREIGN KEY (booking_id, seat_id)` |
| `payments` | Simulated payment transactions & gateways | `UNIQUE(transaction_id)` |
| `weather_alerts` | Station-wise meteorological alerts and severity | `FOREIGN KEY (station_id)` |
| `berth_exchange_requests` | P2P berth swap state machine | `FOREIGN KEY (requester_booking_id, ...)` |
| `ticket_verifications` | Inspector / TTE live ticket scans | `FOREIGN KEY (booking_id, inspector_id)` |
| `notifications` | In-app alerts for weather, booking, and swaps | `FOREIGN KEY (user_id)` |
| `audit_logs` | Immutable audit trail for administrative tracking | Indexed by action & user |

---

## 🚀 Execution & Setup Guide

### Option 1: Using MySQL CLI
```bash
mysql -u root -p < database/schema.sql
mysql -u root -p < database/seed.sql
mysql -u root -p < database/sample-data.sql
```

### Option 2: Using MySQL Workbench
1. Open MySQL Workbench and connect to your local MySQL instance.
2. Execute `database/schema.sql` to initialize `railconnect_db`.
3. Execute `database/seed.sql` to populate master train and station routes.
4. Execute `database/sample-data.sql` to load demo bookings and users.

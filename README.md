# 🚆 RAILCONNECT

### Intelligent Railway Booking, Digital Travel Pass & Journey Assistance System
> **College 2nd Year Advanced Programming Practice (APP) Project**  
> *A unified full-stack monorepo featuring Spring Boot 3.3, Java 17, Relational Graph Routing, Real-Time Dynamic Concurrency Locking, and Multi-Tier Scannable QR Digital Passes.*

[![Live GitHub Pages Deployment](https://img.shields.io/badge/Live%20Demo-GitHub%20Pages-success?style=for-the-badge&logo=github)](https://chetanveerabomma-blip.github.io/railconnect/)
[![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3.4-6DB33F?style=for-the-badge&logo=spring&logoColor=white)](https://spring.io/projects/spring-boot)
[![Frontend](https://img.shields.io/badge/Frontend-HTML5%20%2F%20CSS3%20%2F%20JavaScript-blue?style=for-the-badge&logo=javascript)](https://developer.mozilla.org/en-US/docs/Web/JavaScript)
[![License](https://img.shields.io/badge/License-MIT-purple?style=for-the-badge)](LICENSE)

---

## 🌐 Live Access

* 🚀 **Public Deployed Website**: **[https://chetanveerabomma-blip.github.io/railconnect/](https://chetanveerabomma-blip.github.io/railconnect/)**
* 💻 **Localhost Unified Server**: **[http://localhost:8080/](http://localhost:8080/)**

---

## 🌟 Key Highlights & Engineering Features

1. **Unified Monorepo Architecture**:
   - Single repository containing the full passenger frontend, all Java micro-monolith domain modules, database schemas, test harnesses, and technical documentation.

2. **Flagship Vande Bharat Express (Train 20608 / 20607)**:
   - High-speed corridor connecting **SBC (Bengaluru)** ➔ **KPD (Katpadi)** ➔ **MAS (Chennai Central)**.
   - Live seat inventory in **Executive Chair Car (EC)** with 180° rotatable seats and **AC Chair Car (CC)**.
   - Prominently featured on the login portal and fast search widgets.

3. **Multi-Tier Scannable QR Digital Travel Pass**:
   - Instant cryptographic QR code pass generation (`/api/tickets/qr`).
   - Dual-tier rendering: server-side ZXing PNG generator + on-device HTML5 Canvas fallback for zero-dependency inspection.
   - Real-time ticket verification terminal for Ticket Examiners (TTE).

4. **Advanced Railway Algorithms & DSA (Java & JavaScript)**:
   - **`FamilyClusteringSeatAllocator`**: 2D spatial clustering algorithm placing families/groups together in adjacent berths with automatic Senior Citizen (60+) lower berth prioritization.
   - **`DijkstraRouteOptimizer`**: Graph-based multi-hop transit pathfinder discovering connecting train itineraries when direct trains are full.
   - **`SurgePricingAlgorithm`**: Exponential dynamic pricing curve factoring coach occupancy tiers (Tier 1: 1.0x, Tier 2: 1.1x, Tier 3: 1.25x, Tier 4: 1.4x) and departure proximity.
   - **`MultiPassengerCycleDetector`**: Directed graph cycle detection finding 3-way circular seat swaps ($A \rightarrow B \rightarrow C \rightarrow A$).
   - **Dynamic 10-Minute Seat Locking**: Concurrent lock window with live countdown timer preventing race conditions.

5. **Complete Admin Operations Console**:
   - Add, edit, and decommission trains from the fleet (`/api/admin/trains`).
   - Manage intermediate route stops, sequences, distances, and halt durations.
   - Configure dynamic fare rules (base rate per km, reservation fees, superfast fees, GST).
   - Real-time operational KPI dashboards (occupancy rates, revenue trends, cancellation rates).

6. **Dual Mode Execution**:
   - **Full Backend Mode**: Connects to Spring Boot REST APIs and embedded H2 database at `http://localhost:8080`.
   - **Standalone GitHub Pages Mode**: Auto-activates `railconnect-mock-engine.js` with client-side simulated database and `localStorage` persistence, making the hosted GitHub Pages site 100% interactive!

---

## 📂 Repository Structure

```
railconnect/
├── index.html                     # Portal Homepage with quick search & Vande Bharat hero
├── login.html                     # Multi-role Sign In & Vande Bharat Fleet showcase
├── register.html                  # Passenger registration portal
├── search-trains.html             # Train search with live availability & weather telemetry
├── train-details.html             # Full train route, schedule, and amenities
├── seat-selection.html            # Interactive coach seat map & 10-min countdown lock
├── passenger-details.html         # Passenger manifest & concession selector
├── payment.html                   # Payment gateway simulator (UPI, Cards, NetBanking)
├── ticket.html                    # Digital Travel Pass with scannable QR code
├── pnr-status.html                # Real-time PNR enquiry & ticket tracking
├── weather-alert.html             # Route meteorological warnings & telemetry
├── berth-exchange.html            # P2P passenger berth swap marketplace
├── booking-history.html           # Passenger journey history & cancellation
├── profile.html                   # User profile & saved co-passengers
├── admin-dashboard.html           # Operations console (Fleet, Routes, Fares, TTE terminal)
├── css/                           # Saffron & Navy design system
├── js/                            # Client-side logic & engines
│   ├── api.js                     # Hybrid API client (Live REST + In-Browser Engine)
│   ├── auth.js                    # JWT session manager & demo switcher
│   ├── i18n.js                    # Multilingual translation engine (EN, HI, TA, TE, KN)
│   ├── railconnect-mock-engine.js # Zero-setup in-browser simulated DB for GitHub Pages
│   ├── seat-layout-engine.js      # Interactive SVG/DOM seat maps & countdown locks
│   └── journey-assistant.js       # Dynamic fare breakdown & journey telemetry
├── assets/                        # High-resolution media (Vande Bharat train)
│
├── railconnect-backend/           # Unified runnable Spring Boot server & QR controller
├── railconnect-auth/              # JWT authentication, BCrypt, RBAC, SecurityConfig
├── railconnect-booking/           # Booking engine, Dijkstra pathfinder, surge pricing
├── railconnect-seat-engine/       # Dynamic seat locks, family clustering, seat visualizer
├── railconnect-weather/           # Route weather alert engine & severity classifier
├── railconnect-berth-exchange/    # 10-rule swap engine & 3-way circular cycle detector
├── railconnect-admin/             # Admin management endpoints & TTE inspection
├── railconnect-database/          # 18 normalized tables, DDL schemas, demo seeds
├── railconnect-docs/              # 10 comprehensive architectural & algorithmic docs
│
├── pom.xml                        # Root Maven multi-module aggregator
├── .github/workflows/deploy.yml   # Automated GitHub Pages publishing workflow
└── README.md                      # Project documentation
```

---

## 🚀 How to Run Locally

### Prerequisites
* Java JDK 17 or higher
* Apache Maven 3.8+ (optional, bundled wrappers supported)
* Modern web browser (Chrome, Edge, Firefox)

### Step 1: Clone the Unified Repository
```bash
git clone https://github.com/chetanveerabomma-blip/railconnect.git
cd railconnect
```

### Step 2: Build the Monorepo
```bash
mvn clean package -DskipTests
```

### Step 3: Run the Single Unified Server
```bash
cd railconnect-backend
java -jar target/railconnect-backend-1.0.0.jar
```

### Step 4: Open in Browser
Visit **[http://localhost:8080](http://localhost:8080)**. Both the complete frontend and backend REST APIs are served from this single link!

---

## 🔑 Demo Credentials

| Role | Username | Password | Privileges |
| :--- | :--- | :--- | :--- |
| **System Administrator** | `admin` | `password123` | Train CRUD, Route Halts, Dynamic Fare Matrix, Audit Logs |
| **Ticket Examiner (TTE)** | `inspector_anand` | `password123` | Onboard QR Inspection Terminal, Gate Verification |
| **Passenger** | `rahul_sharma` | `password123` | Ticket booking, Seat Selection, Berth Exchange Marketplace |
| **Passenger** | `priya_patel` | `password123` | Ticket booking, Eligible Berth Swap Participant |

---

## 📜 License
This project is open-source under the MIT License. Developed for Advanced Programming Practice (APP).

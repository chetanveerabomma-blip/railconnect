# RAILCONNECT — Seat Engine Module (`railconnect-seat-engine`)

Advanced seat and berth selection, dynamic 10-minute temporary seat locking, and intelligent multi-criteria seat allocation for **RailConnect**.

## 📌 Core Capabilities
* **Dynamic Seat Locking**: Locks selected seats for a 10-minute countdown window using concurrent memory maps. Automatically reconciles into `BOOKED` upon payment or `AVAILABLE` upon expiration.
* **Intelligent Auto-Allocation (APP Core)**:
  - Leverages `PriorityQueue`, `HashMap`, `HashSet`, `Queue`, and `ArrayList`.
  - Automatically identifies same-coach clustering for groups.
  - Places families into adjacent cabins.
  - Automatically accords senior citizens (age 60+) `LOWER` berth preference.
* **Real-time Coach Layout Generator**: Returns interactive seating matrices for 1A, 2A, 3A, Sleeper (SL), and Chair Car (CC).

## 🛠️ REST Endpoints
* `GET /api/seats/coaches/{trainId}` — Fetch coach listing for a train.
* `GET /api/seats/layout/{coachId}` — Retrieve live visual seating grid with lock statuses.
* `POST /api/seats/lock` — Acquire temporary 10-minute lock on a seat.
* `POST /api/seats/unlock` — Voluntarily release seat lock.
* `POST /api/seats/auto-allocate` — Algorithmic multi-passenger seat assignment.

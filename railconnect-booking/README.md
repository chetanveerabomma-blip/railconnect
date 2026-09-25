# RAILCONNECT — Booking Engine Module (`railconnect-booking`)

Train routing, real-time availability querying, exact fare formula calculation, 10-digit collision-free PNR allocation, and ticket cancellation engine for **RailConnect**.

## 📌 Features
* **Exact Fare Formula**:
  $$\text{Final Fare} = \text{Base Fare} + \text{Reservation Charge} + \text{Service Charge} + \text{Tax} - \text{Discount}$$
  Reads distance coefficients and administrative rules directly from MySQL `fare_rules`.
* **Collision-Free 10-Digit PNR Engine**: Generates standard 10-digit PNRs with proactive database uniqueness verification.
* **Smart Route Graph Search**: Identifies direct trains and computes route stop intervals and halts across intermediate stations.
* **Transactional Reservation**: Atomic seat locking, passenger assignment, simulated payment recording, and cancellation refund tracking.

## 🛠️ REST Endpoints
* `GET /api/trains/stations` — List all registered stations.
* `GET /api/trains/search?from=TPJ&to=MAS&date=2026-10-10` — Route search with class availability.
* `POST /api/bookings/create` — Issue confirmed ticket and generate Digital Travel Pass.
* `GET /api/bookings/my` — Fetch authenticated passenger booking portfolio.
* `POST /api/bookings/cancel/{pnr}` — Cancel reservation and calculate automated refund.
* `GET /api/pnr/{pnr}` — Instant 10-digit PNR status enquiry.

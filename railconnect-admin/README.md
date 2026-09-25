# RAILCONNECT — Administration & Inspection Module (`railconnect-admin`)

Central administrative control room, train inventory management, dynamic tariff configurations, inspector verification terminals, and system analytics for **RailConnect**.

## 📌 Capabilities
* **Executive Metrics Dashboard**: Real-time KPI aggregation:
  - Total Active Trains & Operational Stations
  - Real-time Revenue & Daily Booking Trends
  - Coach Class Utilization (1A, 2A, 3A, SL, CC)
  - Train Occupancy percentages & Cancellation Rates
* **Train & Route Management**: Adding and updating trains, intermediate stops, distance offsets, and timings.
* **Fare Rule Configuration**: Live tuning of base rates per km, reservation tariffs, superfast charges, and Tatkal multipliers.
* **On-Board Ticket Inspector Terminal**: Real-time validation of Digital Travel Passes via QR Code payload parsing or 10-digit PNR lookup.
* **Immutable Audit Trail**: Tracks administrative modifications with timestamped action logs.

## 🛠️ Endpoints
* `GET /api/admin/dashboard` — Analytical statistics and charting data.
* `POST /api/admin/trains` — Register a new scheduled train.
* `PUT /api/admin/trains/{id}` — Modify train schedule or active status.
* `DELETE /api/admin/trains/{id}` — Safe deactivation of train schedule.
* `POST /api/admin/stations` — Add a new railway junction or terminal.
* `POST /api/admin/fares` — Update class and distance tariff formulas.
* `GET /api/admin/audits` — Query immutable system audit events.
* `POST /api/tickets/verify` — Inspect and validate digital travel passes (Inspector / Admin).

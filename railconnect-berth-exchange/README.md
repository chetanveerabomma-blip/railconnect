# RAILCONNECT — Berth Exchange Module (`railconnect-berth-exchange`)

Passenger-to-passenger intelligent berth swap platform with strict 10-point railway rule validation and administrative oversight for **RailConnect**.

## 📌 Exchange Workflow
```
Passenger A (Requester)
       │
       ▼
Find Eligible Passenger on Same Train & Date
       │
       ▼
Send Exchange Request
       │
       ▼
Passenger B (Accept / Reject)
       │
       ▼
Rule Engine Validation (10 Rules)
       │
       ▼
Admin Approval (Compliant with TTE Guidelines)
       │
       ▼
Atomic Seat Swap & Audit Log Recorded
```

## 📜 10 Strict Railway Business Rules
1. Both tickets must have `CONFIRMED` status.
2. Must belong to the exact same train.
3. Must have matching journey dates.
4. Journey route segments must overlap.
5. Travel class must be compatible (e.g., 3A $\leftrightarrow$ 3A).
6. Cancelled tickets cannot initiate or participate in an exchange.
7. Used tickets cannot exchange.
8. Expired journeys cannot exchange.
9. One active pending exchange request per ticket at any time.
10. All actions, acceptances, and approvals must be permanently logged in `audit_logs`.

## 🛠️ Endpoints
* `POST /api/berth-exchange/request` — Initiate a swap proposal.
* `POST /api/berth-exchange/respond/{id}` — Accept or decline an incoming request.
* `GET /api/berth-exchange/my` — View user's exchange activity.
* `GET /api/berth-exchange/eligible` — Query candidate co-passengers.
* `POST /api/berth-exchange/admin/review/{id}` — Railway administration review and final execution.

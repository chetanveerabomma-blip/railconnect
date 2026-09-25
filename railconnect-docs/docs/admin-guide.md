# RAILCONNECT — Admin & TTE Operations Manual

This manual provides operational instructions for railway administrators, division managers, and Traveling Ticket Examiners (TTEs) using the **RAILCONNECT Operations Console** (`admin-dashboard.html`).

---

## 1. Authentication & Role Permissions

The Operations Console requires elevated role credentials:
* **Administrator (`ROLE_ADMIN`)**: Unrestricted access to system KPIs, train fleet creation, dynamic fare rule updates, and security audit logs.
* **Inspector / TTE (`ROLE_INSPECTOR`)**: Access to the onboard ticket verification terminal, PNR passenger manifest lookup, and attendance stamping.

### Quick Sign In
On `admin-dashboard.html` or `login.html`, click:
* **Quick Admin Mode**: Logs in as `admin` (`ROLE_ADMIN`).
* **Quick Inspector Mode**: Logs in as `inspector_anand` (`ROLE_INSPECTOR`).

---

## 2. Executive KPI Dashboard

The Executive KPI view provides high-level telemetry across the railway division:
* **Active Trains**: Total scheduled train services operating on the network.
* **Tracked Stations**: Network nodes with active timetables and meteorological sensors.
* **Total Bookings**: Ledger count of confirmed and completed reservations.
* **Gross Fare Revenue**: Total INR value processed through simulated payment gateways.
* **Coach Class Occupancy**: Real-time progress bars indicating capacity utilization across Sleeper (SL), AC 3-Tier (3A), AC 2-Tier (2A), and Chair Car (CC).
* **System Daemon Health**: Verification of background threads (10-minute seat lock reaper, weather telemetry poller, swap engine auditor).

---

## 3. Onboard TTE Ticket Verification Terminal

Traveling Ticket Examiners verify passenger legitimacy onboard moving trains using the **TTE Ticket Inspection** tab:

1. Click the **TTE Ticket Inspection** tab.
2. **Scan / Enter Ticket Identity**:
   * Enter the passenger's **10-Digit PNR** (e.g. `4827193056`) or paste the scannable ZXing QR payload.
   * Add optional inspector remarks (e.g. *"Physical Aadhaar card verified onboard at Villupuram Jn"*).
3. Click **Verify Pass**:
   * The backend validates the cryptographic signature, checks if the ticket is active/confirmed, confirms passenger identity and assigned coach/seat.
   * Returns a **Green "VALID PASSENGER TICKET"** verification stamp.
   * Commits the inspection timestamp, station, and inspector username into the immutable `ticket_verifications` table.

---

## 4. Train Fleet & Schedule Management

Administrators can introduce new railway schedules or modify existing services:

1. Click the **Train Fleet Management** tab.
2. In the **Add New Train Schedule** form, specify:
   * **Train Number**: 5-digit Indian railway identifier (e.g. `12638`).
   * **Train Name**: Official designation (e.g. `Vaigai Superfast Express`).
   * **Origin & Destination Codes**: (e.g. `MDU` to `MS`).
   * **Total Distance**: Track distance in kilometers.
3. Click **Add to Fleet**.
4. The service is instantly registered in the network routing graph and becomes bookable by passengers.

---

## 5. Dynamic Fare Rule Engine Configuration

RAILCONNECT empowers administrators to adjust fare formulas dynamically to accommodate fuel pricing or peak holiday demands:

1. Click the **Dynamic Fare Engine** tab.
2. Configure parameters:
   * **Sleeper (SL) Base Rate**: Cost per passenger kilometer (default ₹0.45/km).
   * **AC 3-Tier (3A) Base Rate**: Cost per passenger kilometer (default ₹1.25/km).
   * **AC 2-Tier (2A) Base Rate**: Cost per passenger kilometer (default ₹1.85/km).
   * **Reservation Surcharge**: Fixed charge per reservation (default ₹40).
   * **Senior Citizen Concession**: Percentage discount for age 60+ (default 40%).
   * **Clerkage / Cancellation Fee**: Fixed deduction on user cancellation (default ₹120).
3. Click **Update Master Fare Parameters**. The changes propagate immediately across all new booking transactions.

---

## 6. Security Audit Logs

The **Security Audit Logs** tab maintains an immutable chronological record of all critical mutations across the platform:
* Event Types: `BOOKING_CREATED`, `SEAT_LOCKED`, `SEAT_UNLOCKED`, `TICKET_CANCELLED`, `BERTH_EXCHANGE_REQUESTED`, `BERTH_EXCHANGE_EXECUTED`, `TICKET_VERIFIED`.
* Logged Attributes: Precise timestamp, actor username, IP address, target entity ID, outcome status (`SUCCESS` / `FAILURE`), and structured event metadata.

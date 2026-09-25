# RAILCONNECT — UI Wireframes & Layout Specification

This document presents structural wireframes and interface breakdowns for the **15 pages** comprising the RAILCONNECT frontend suite.

---

## 1. Landing Page (`index.html`)

```
+-----------------------------------------------------------------------------+
| 🚆 RAILCONNECT      [Home] [Search Trains] [PNR Status] [Weather] [Login] EN |
+-----------------------------------------------------------------------------+
|                                                                             |
|      🚆 Next-Generation Intelligent Railway Booking & Digital Travel Pass    |
|               Algorithmic Seat Allocations • Live Weather Telemetry          |
|                                                                             |
|   +---------------------------------------------------------------------+   |
|   | From: [TPJ - Trichy Jn ▼]   To: [MS - Chennai Egmore ▼]  Date: [15 Oct] |
|   | Quota: [General (GN) ▼]            [ Find Available Trains ➔ ]      |   |
|   +---------------------------------------------------------------------+   |
|                                                                             |
|   +-------------------+  +-------------------+  +-------------------+       |
|   | ⏱️ Dynamic Locking|  | 🌦️ Weather Alerts |  | 🔄 Berth Exchange |       |
|   | 10-min thread-safe|  | Live track sensors|  | 10-rule P2P seat  |       |
|   | lock countdown    |  | with severity     |  | swap engine       |       |
|   +-------------------+  +-------------------+  +-------------------+       |
+-----------------------------------------------------------------------------+
```

---

## 2. Interactive Seat Selection (`seat-selection.html`)

```
+-----------------------------------------------------------------------------+
| 🚆 RAILCONNECT                          Coach B1 (AC 3-Tier) | Class: 3A    |
+-----------------------------------------------------------------------------+
|  ⚠️ 10-MINUTE SEAT LOCK ACTIVE: [ ⏳ 09:42 remaining ]                      |
|  [⚡ Auto-Allocate Optimal Seats (DSA)] [Reset Selection]                   |
|                                                                             |
|  +-----------------------------------------------------------------------+  |
|  | [BAY 1]                                              [SIDE BERTHS]    |  |
|  |  +-------+  +-------+  +-------+                       +-------+      |  |
|  |  | 01 LB |  | 02 MB |  | 03 UB |                       | 07 SL |      |  |
|  |  | [AVAIL]  | [BOOK] |  | [AVAIL]                      | [AVAIL|      |  |
|  |  +-------+  +-------+  +-------+                       +-------+      |  |
|  |  ==================== AISLE ===========================               |  |
|  |  +-------+  +-------+  +-------+                       +-------+      |  |
|  |  | 04 LB |  | 05 MB |  | 06 UB |                       | 08 SU |      |  |
|  |  | [LOCKED  | [AVAIL]  | [AVAIL]                      | [AVAIL]      |  |
|  |  +-------+  +-------+  +-------+                       +-------+      |  |
|  +-----------------------------------------------------------------------+  |
|                                                                             |
|  Selected Seats: B1-01 (Lower Berth), B1-04 (Lower Berth)                   |
|  Subtotal Base Fare: ₹1,690.00                   [ Proceed to Details ➔ ]   |
+-----------------------------------------------------------------------------+
```

---

## 3. Dynamic Fare Payment (`payment.html`)

```
+-----------------------------------------------------------------------------+
| 💳 Secure Railway Checkout                                                  |
+-----------------------------------------------------------------------------+
|  Select Payment Gateway:                                                    |
|  (•) Unified Payments Interface (UPI - GPay / PhonePe / Paytm)              |
|  ( ) Credit / Debit Card (Visa, MasterCard, RuPay)                          |
|  ( ) Internet Banking                                                       |
|                                                                             |
|  +-----------------------------------------------------------------------+  |
|  | Dynamic Fare Formula Calculation:                                     |  |
|  | Base Distance Fare (336 km x ₹1.25/km x 2 pax):             ₹840.00   |  |
|  | Fixed Class Reservation Fee:                                  ₹80.00   |  |
|  | Superfast Express Surcharge:                                  ₹90.00   |  |
|  | Applicable GST (5% on AC Tier):                               ₹50.50   |  |
|  | Senior Citizen Concession (-40% on Senior Base):             -₹168.00   |  |
|  | --------------------------------------------------------------------- |  |
|  | Total Fare Payable:                                          ₹892.50   |  |
|  +-----------------------------------------------------------------------+  |
|                                                                             |
|  [ Pay ₹892.50 & Issue Digital Travel Pass ➔ ]                              |
+-----------------------------------------------------------------------------+
```

---

## 4. Digital Travel Pass (`ticket.html`)

```
+-----------------------------------------------------------------------------+
|  🚆 RAILCONNECT DIGITAL TRAVEL PASS                       STATUS: CONFIRMED |
+-----------------------------------------------------------------------------+
|  PNR: 4827193056          TRAIN: 12638 — Pandian Superfast Express          |
|  JOURNEY DATE: 15-OCT-2026   FROM: TPJ (Trichy)  ➔  TO: MS (Chennai Egmore) |
+-----------------------------------------------------------------------------+
|  PASSENGERS:                                                                |
|  1. Rahul Sharma (Age 29, M)       Coach B1, Seat 21 (Lower Berth)          |
|  2. Sunita Sharma (Age 58, F)      Coach B1, Seat 22 (Middle Berth)         |
+-----------------------------------------------------------------------------+
|  +---------------------+   SCANNABLE ONBOARD VERIFICATION TOKEN             |
|  | [■■■■■■■■■■■■■■■■■] |   Payload: RAILCONNECT:PNR=4827193056:TR=12638...  |
|  | [■■  ■■■■■■  ■■  ■] |   Encrypted SHA-256 HMAC Signature                 |
|  | [■■■■■■■■■■■■■■■■■] |   Valid for Travel. Please carry photo ID.         |
|  +---------------------+                                                    |
|  [ 🖨️ Print / Save as PDF ]                 [ 🔄 Propose Berth Exchange ]   |
+-----------------------------------------------------------------------------+
```

---

## 5. Live Journey Weather Telemetry (`weather-alert.html`)

```
+-----------------------------------------------------------------------------+
| 🌦️ Live Journey Weather Telemetry & Route Safety Advisories                  |
+-----------------------------------------------------------------------------+
|  Journey Corridor: TPJ (Tiruchchirappalli Jn) ➔ MS (Chennai Egmore)         |
|  Overall Severity: [ 🟡 ADVISORY ] Moderate rainfall near coastal section   |
|                                                                             |
|  +---------------------------+       +---------------------------+          |
|  | Tiruchchirappalli (TPJ)   |       | Villupuram Jn (VM)        |          |
|  | 32°C • Clear Sky          | ────► | 28°C • Light Rain Shower  |          |
|  | Severity: [ NORMAL 🟢 ]   |       | Severity: [ ADVISORY 🟡 ] |          |
|  +---------------------------+       +---------------------------+          |
|                                                    │                        |
|                                      +-------------▼-------------+          |
|                                      | Chennai Egmore (MS)       |          |
|                                      | 26°C • Heavy Showers      |          |
|                                      | Severity: [ WARNING 🟠 ]  |          |
|                                      +---------------------------+          |
|  Telemetry Source: OpenWeatherMap / Deterministic Railway Station Sensors   |
+-----------------------------------------------------------------------------+
```

---

## 6. Co-Passenger Berth Exchange (`berth-exchange.html`)

```
+-----------------------------------------------------------------------------+
| 🔄 Peer-to-Peer Berth Exchange Engine                                       |
+-----------------------------------------------------------------------------+
|  My Current Allotment: Coach B1, Seat 34 (Upper Berth)                      |
|  Eligible Verified Co-Passengers on Train 12638 (Same Date / Same Class):   |
|                                                                             |
|  +-----------------------------------------------------------------------+  |
|  | Passenger: Priya Patel (Age 26)   Allotment: Coach B1, Seat 21 (Lower)|  |
|  | Corridor: TPJ ➔ MS (Exact Match)  Status: Confirmed                   |  |
|  | Compliance: Passes all 10 Railway Business Rules                      |  |
|  |                                                                       |  |
|  | [ Propose Seat Swap Request ➔ ]                                       |  |
|  +-----------------------------------------------------------------------+  |
|                                                                             |
|  Incoming Swap Requests for Me:                                             |
|  * Ramesh Kumar (Seat 15 MB) requests swap with your Seat 34 (UB).          |
|    Reason: "Senior citizen knee pain, seeking lower/middle berth"           |
|    [ Accept Swap (Atomic) ]       [ Decline ]                               |
+-----------------------------------------------------------------------------+
```

---

## 7. Operations Console & TTE Inspection (`admin-dashboard.html`)

```
+-----------------------------------------------------------------------------+
| 🛡️ RAILCONNECT Central Operations & TTE Terminal                            |
+-----------------------------------------------------------------------------+
|  [Executive KPIs] [TTE Inspection] [Fleet Manager] [Fares] [Audit Logs]     |
|                                                                             |
|  ACTIVE TRAINS: 4     STATIONS: 12     BOOKINGS: 28     REVENUE: ₹24,850    |
|                                                                             |
|  TTE INSPECTION TERMINAL:                                                   |
|  Scan / Enter PNR: [ 4827193056        ]   Remarks: [ Aadhaar Verified ]   |
|  [ Verify Ticket Onboard ]                                                  |
|                                                                             |
|  +-----------------------------------------------------------------------+  |
|  | ✅ VALID PASSENGER TICKET — ONBOARD VERIFIED                           |  |
|  | PNR: 4827193056 | Train 12638 (Pandian Superfast)                     |  |
|  | Coach: B1 | Seats: 21 (LB), 22 (MB) | Passenger: Rahul Sharma          |  |
|  | Verified By: inspector_anand | Timestamp: 2026-09-25 21:42:15 UTC     |  |
|  +-----------------------------------------------------------------------+  |
+-----------------------------------------------------------------------------+
```

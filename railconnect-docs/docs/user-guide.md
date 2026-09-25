# RAILCONNECT — Passenger User Guide

Welcome to the **RAILCONNECT** intelligent railway booking, digital travel pass, and journey assistance system. This user guide walks you through every feature of the passenger journey.

---

## 1. Getting Started & Sign In

1. Open your browser and navigate to `http://localhost:3000` (or `index.html`).
2. Click **Login** in the top navigation bar.
3. You can log in using your registered credentials or click one of the quick test accounts (e.g. **Rahul (Passenger)**) to auto-fill `rahul_sharma` / `password123`.
4. If you are a new traveler, click **Register as Passenger** to create a verified digital identity.

---

## 2. Multilingual Platform Switching

RAILCONNECT is designed for India's diverse linguistic demographics. At any point in your session:
* Locate the **Language Selector** dropdown in the top right corner of the navigation bar.
* Select between:
  * **English (EN)**
  * **हिन्दी (HI)**
  * **తెలుగు (TE)**
  * **தமிழ் (TA)**
* All navigation items, search cards, and buttons will translate instantaneously without reloading the page.

---

## 3. Searching Trains & Checking Route Weather

1. Navigate to **Search Trains** (`search-trains.html`).
2. Select your **Origin Station** (e.g., `TPJ - Tiruchchirappalli Jn`) and **Destination Station** (e.g., `MS - Chennai Egmore`).
3. Select your travel date and click **Find Trains**.
4. The search results display:
   * Train Number and Name (e.g., `12638 Pandian Superfast Express`).
   * Scheduled departure and arrival times with journey duration.
   * Real-time seat availability and base fares across classes (`SL`, `3A`, `2A`, `CC`).
   * **Route Weather Telemetry**: An inline badge alerts you if severe rain, fog, or cyclone advisories exist along the train tracks.
5. Click **View Schedule & Route** to inspect intermediate halt stations, platform numbers, and distance.

---

## 4. Interactive Seat Selection & 10-Minute Lock Timer

1. On the train details or search card, click **Select Seats**.
2. You will be presented with an interactive visual coach layout (e.g., Coach B1 for AC 3-Tier):
   * `LOWER`, `MIDDLE`, `UPPER`, `SIDE_LOWER`, `SIDE_UPPER` berths are visually distinguished.
   * Color codes show **Available** (Green), **Booked** (Gray), and **Selected/Locked** (Deep Blue).
3. **Dynamic Locking Timer**:
   * When you click an available seat, a 10-minute dynamic lock countdown timer activates (`09:59... 09:58...`).
   * This locks the seat exclusively for you across the entire system. Other users cannot book it while your timer is running.
4. **Intelligent Auto-Allocation (Alternative)**:
   * Don't want to choose manually? Click **"Auto-Allocate Optimal Seats (DSA)"**.
   * If you have senior citizens in your group (age 60+), the algorithm automatically prioritizes lower berths.
   * If traveling with family, the algorithm clusters all seats in the same bay.

---

## 5. Passenger Details & Dynamic Fare Calculation

1. On `passenger-details.html`, enter passenger names, ages, and genders.
2. The system automatically detects travelers aged 60+ and applies a **40% Senior Citizen Concession** on base fares.
3. Click **Proceed to Payment**. On `payment.html`, the dynamic fare formula breakdown is shown:
   * **Base Distance Fare**: $336\text{ km} \times \text{Class Rate}$
   * **Reservation Surcharge**: ₹40.00
   * **Superfast Fee**: ₹45.00
   * **GST (5%)**: Calculated on taxable components
   * **Concession**: Subtracted from total
4. Select your preferred payment method: **UPI (Google Pay / PhonePe)**, **Credit/Debit Card**, or **Net Banking**.
5. Click **Complete Payment & Issue Pass**.

---

## 6. Digital Travel Pass & Scannable QR Code

Upon payment confirmation, RAILCONNECT generates your official **Digital Travel Pass** (`ticket.html`):
* Displays your **10-Digit Collision-Free PNR** (e.g. `4827193056`).
* Displays allotted Coach (e.g. `B1`), Berth numbers, and berth types.
* **Scannable QR Code**: Contains a cryptographically signed payload that Traveling Ticket Examiners (TTEs) can scan onboard for contactless identity verification.
* Click **Print / Save as PDF** to save an offline copy.

---

## 7. Tracking PNR & Cancellation Refunds

1. Go to **PNR Status** (`pnr-status.html`).
2. Enter your 10-digit PNR to retrieve real-time reservation status.
3. If your plans change, click **Cancel Ticket**:
   * A transparent refund modal displays your original fare, standard railway clerkage deduction (₹120), and net refund amount.
   * Confirming cancellation releases the seat immediately to RAC/Waitlisted travelers and updates the status to `CANCELLED`.

---

## 8. Peer-to-Peer Berth Exchange

If you were allocated an upper berth but prefer a lower berth (or wish to exchange with a co-passenger traveling in the same coach):
1. Navigate to **Berth Exchange** (`berth-exchange.html`).
2. The 10-Rule Exchange Engine displays verified co-passengers on your train.
3. Click **Propose Swap** to send a formal seat exchange request.
4. The target co-passenger receives the request in their dashboard. When they click **Accept Swap**, RAILCONNECT executes an atomic two-phase seat swap transaction in the database and re-issues updated travel passes to both passengers instantly.

# RAILCONNECT — Frontend (Passenger Web Portal & Operations Console)

**Repository 1 of 10** in the **RAILCONNECT** Railway Ticket Booking & Digital Travel Pass Platform.

---

## 📖 Overview

The `railconnect-frontend` repository provides the responsive, passenger-facing web application and operations console for the RAILCONNECT system. Built using clean **HTML5, CSS3, and modern Vanilla JavaScript**, it delivers a fast, zero-bundle-overhead user experience with real-time dynamic seat selection, 10-minute lock countdown timers, interactive digital travel passes, live journey weather advisory widgets, peer-to-peer berth exchange requests, and multilingual switching across **English, Hindi, Telugu, and Tamil**.

---

## 🎨 Design System & Color Palette

To adhere strictly to professional standards without copying official Indian Railways / IRCTC branding:
* **Primary Deep Blue**: `#1A365D` (Trust, authority, safety)
* **Secondary Slate**: `#2D3748`
* **Clean White / Card Background**: `#FFFFFF`
* **Subtle Slate Gray Surface**: `#F7FAFC`
* **Subtle Saffron Highlight**: `#E27D60`
* **Subtle Emerald Green Accent**: `#2E8540`
* **Status Badges & Borders**: `#CBD5E0`

---

## 📁 Repository Structure

```
railconnect-frontend/
├── index.html               # Landing page, quick train search, features & innovations
├── login.html               # Multi-role authentication (Passenger, TTE Inspector, Admin)
├── register.html            # Passenger onboarding with identity verification
├── search-trains.html       # Origin-Destination train search with live weather badges
├── train-details.html       # Train timetable, intermediate halt stations, and coach layout
├── seat-selection.html      # Interactive coach seat map with 10-min dynamic locking timer
├── passenger-details.html   # Passenger manifest, senior citizen concession auto-detection
├── payment.html             # Dynamic fare formula breakdown, simulated UPI / Card / NetBanking
├── ticket.html              # Digital Travel Pass with scannable ZXing QR code & PDF/print
├── pnr-status.html          # 10-digit collision-free PNR tracking and cancellation refund modal
├── weather-alert.html       # Live journey weather telemetry along route stops with advisory badges
├── berth-exchange.html      # 10-rule passenger-to-passenger berth exchange engine
├── booking-history.html     # Historical and upcoming bookings with pass download & cancellation
├── profile.html             # User profile, KYC status, and travel preferences
├── admin-dashboard.html     # Operations console, fleet management, and TTE inspection terminal
├── css/
│   └── style.css            # Responsive CSS variables, typography, seat grid, and pass layout
├── js/
│   ├── api.js               # Central asynchronous REST client communicating with port 8080
│   ├── auth.js              # Session state, role badges, toasts, and dynamic navbar
│   └── i18n.js              # Multilingual switcher engine with reactive DOM updates
├── i18n/
│   ├── en.json              # English translation dictionary
│   ├── hi.json              # Hindi translation dictionary
│   ├── te.json              # Telugu translation dictionary
│   └── ta.json              # Tamil translation dictionary
└── assets/                  # Brand assets, icons, and diagrams
```

---

## 🚀 Running the Frontend

The frontend is completely static and can be served with any HTTP web server:

### Option 1: Python Built-in HTTP Server
```bash
cd railconnect-frontend
python -m http.server 3000
```
Open [http://localhost:3000](http://localhost:3000) in your web browser.

### Option 2: Node.js `serve` / `http-server`
```bash
npx serve -l 3000 railconnect-frontend
```

### Option 3: Direct File Opening
You can directly double-click `index.html` to open it in Chrome, Edge, or Firefox. The API client includes fallback mock data so all features can be demonstrated even when the backend is offline.

---

## 🌐 Multilingual Support (i18n)

The interface supports on-the-fly language switching without page reloads using a centralized `data-i18n` attribute system:
* **English (EN)**: Default international locale.
* **हिन्दी (HI)**: Devanagari script for national Hindi connectivity.
* **తెలుగు (TE)**: Telugu script for South Central railway passengers.
* **தமிழ் (TA)**: Tamil script for Southern railway corridor passengers.

Translations are stored in modular JSON files inside `i18n/` and cached locally in `localStorage`.

---

## 🔗 Backend Connectivity

The frontend connects to the Spring Boot REST backend running on `http://localhost:8080/api`:
* Authentication: `POST /api/auth/login`, `POST /api/auth/register`
* Search: `GET /api/trains/search`
* Dynamic Seat Locks: `POST /api/seats/lock`, `POST /api/seats/unlock`, `POST /api/seats/auto-allocate`
* Bookings & PNR: `POST /api/bookings/create`, `GET /api/pnr/{pnr}`
* Weather: `GET /api/weather/journey`
* Berth Swap: `GET /api/berth-exchange/eligible`, `POST /api/berth-exchange/request`
* Operations & TTE: `GET /api/admin/dashboard`, `POST /api/tickets/verify`

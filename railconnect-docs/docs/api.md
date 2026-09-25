# RAILCONNECT — Comprehensive REST API Reference

Base URL: `http://localhost:8080/api`

All requests and responses use `application/json`. Authenticated endpoints require standard Bearer token authorization:
```http
Authorization: Bearer <JWT_TOKEN>
```

---

## 1. Authentication Endpoints (`/api/auth`)

### 1.1 User Login
* **Method**: `POST`
* **Path**: `/api/auth/login`
* **Access**: Public
* **Request Body**:
```json
{
  "username": "rahul_sharma",
  "password": "password123"
}
```
* **Success Response (200 OK)**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "userId": 2,
  "username": "rahul_sharma",
  "email": "rahul.sharma@example.com",
  "fullName": "Rahul Sharma",
  "role": "ROLE_PASSENGER"
}
```

### 1.2 User Registration
* **Method**: `POST`
* **Path**: `/api/auth/register`
* **Access**: Public
* **Request Body**:
```json
{
  "username": "anita_desai",
  "password": "password123",
  "email": "anita@example.com",
  "fullName": "Anita Desai",
  "phoneNumber": "+91 91234 56789"
}
```
* **Success Response (201 Created)**: Returns user authentication object with newly issued JWT.

---

## 2. Train Search & Stations (`/api/trains`)

### 2.1 Search Trains
* **Method**: `GET`
* **Path**: `/api/trains/search?from=TPJ&to=MS&date=2026-10-15`
* **Access**: Public
* **Response (200 OK)**:
```json
[
  {
    "trainId": 1,
    "trainNumber": "12638",
    "trainName": "Pandian Superfast Express",
    "departureTime": "06:30:00",
    "arrivalTime": "12:15:00",
    "duration": "5h 45m",
    "distanceKm": 336.0,
    "availableClasses": [
      { "coachType": "SL", "seatsAvailable": 42, "fare": 245.0 },
      { "coachType": "3A", "seatsAvailable": 18, "fare": 650.0 },
      { "coachType": "2A", "seatsAvailable": 8, "fare": 920.0 }
    ],
    "weatherAlert": {
      "severity": "NORMAL",
      "summary": "Clear Skies",
      "temperature": "31°C"
    }
  }
]
```

### 2.2 List Master Stations
* **Method**: `GET`
* **Path**: `/api/trains/stations`
* **Access**: Public
* **Response (200 OK)**: Returns list of railway stations with codes, cities, and states.

---

## 3. Dynamic Seat Engine (`/api/seats`)

### 3.1 Fetch Coach Layout & Real-Time Availability
* **Method**: `GET`
* **Path**: `/api/seats/layout/{coachId}`
* **Response (200 OK)**:
```json
{
  "coachId": 2,
  "coachCode": "B1",
  "coachType": "3A",
  "totalSeats": 64,
  "seats": [
    {
      "seatId": 101,
      "seatNumber": 1,
      "berthType": "LOWER",
      "state": "AVAILABLE"
    },
    {
      "seatId": 102,
      "seatNumber": 2,
      "berthType": "MIDDLE",
      "state": "LOCKED",
      "lockExpiresInSeconds": 480
    }
  ]
}
```

### 3.2 Dynamic 10-Minute Seat Locking
* **Method**: `POST`
* **Path**: `/api/seats/lock`
* **Access**: Passenger (`ROLE_PASSENGER`)
* **Request Body**:
```json
{
  "seatId": 101,
  "durationMinutes": 10
}
```
* **Success Response (200 OK)**:
```json
{
  "status": "SUCCESS",
  "seatId": 101,
  "lockedUntil": "2026-09-25T21:45:00Z",
  "lockDurationSeconds": 600
}
```

### 3.3 Intelligent Auto-Allocation
* **Method**: `POST`
* **Path**: `/api/seats/auto-allocate`
* **Request Body**:
```json
{
  "trainId": 1,
  "coachType": "3A",
  "journeyDate": "2026-10-15",
  "passengers": [
    { "name": "Ramesh Kumar", "age": 67, "gender": "MALE", "seniorCitizen": true },
    { "name": "Kavita Kumar", "age": 62, "gender": "FEMALE", "seniorCitizen": true }
  ]
}
```
* **Response (200 OK)**:
```json
{
  "allocated": true,
  "coachCode": "B1",
  "allocations": [
    { "passengerName": "Ramesh Kumar", "seatNumber": "21", "berthType": "LOWER" },
    { "passengerName": "Kavita Kumar", "seatNumber": "22", "berthType": "LOWER" }
  ],
  "reason": "Senior citizen heuristic: Prioritized Lower Berths in adjacent bay"
}
```

---

## 4. Bookings & 10-Digit PNR (`/api/bookings`, `/api/pnr`)

### 4.1 Create Booking
* **Method**: `POST`
* **Path**: `/api/bookings/create`
* **Request Body**:
```json
{
  "trainId": 1,
  "journeyDate": "2026-10-15",
  "fromStation": "TPJ",
  "toStation": "MS",
  "coachType": "3A",
  "passengers": [
    { "name": "Rahul Sharma", "age": 29, "gender": "MALE", "seatId": 101 },
    { "name": "Sunita Sharma", "age": 58, "gender": "FEMALE", "seatId": 102 }
  ],
  "paymentMethod": "UPI"
}
```
* **Response (201 Created)**:
```json
{
  "pnr": "4827193056",
  "bookingStatus": "CONFIRMED",
  "totalFare": 845.00,
  "qrPayload": "RAILCONNECT:PNR=4827193056:TR=12638:DT=2026-10-15:PS=2:ST=CONFIRMED:SIG=9F82A01C",
  "bookingDate": "2026-09-25T21:40:00Z"
}
```

### 4.2 Track PNR Live Status
* **Method**: `GET`
* **Path**: `/api/pnr/{pnr}`
* **Response (200 OK)**: Returns complete reservation, coach/berth details, payment summary, and cancellation eligibility.

### 4.3 Cancel Booking
* **Method**: `POST`
* **Path**: `/api/bookings/cancel/{pnr}`
* **Request Body**:
```json
{
  "reason": "Sudden change in meeting itinerary"
}
```
* **Response (200 OK)**: Returns cancellation confirmation, calculated clerkage deduction (₹120), and processed refund amount.

---

## 5. Journey Weather Alerts (`/api/weather`)

### 5.1 Route Weather Telemetry
* **Method**: `GET`
* **Path**: `/api/weather/journey?from=TPJ&to=MS&date=2026-10-15`
* **Response (200 OK)**:
```json
{
  "overallSeverity": "NORMAL",
  "advisory": "All track sections report clear weather. Safe for journey.",
  "stationReports": [
    {
      "stationCode": "TPJ",
      "stationName": "Tiruchchirappalli Jn",
      "temperature": "32°C",
      "condition": "Clear Sky",
      "severity": "NORMAL",
      "source": "Demo Weather Data"
    },
    {
      "stationCode": "MS",
      "stationName": "Chennai Egmore",
      "temperature": "30°C",
      "condition": "Scattered Clouds",
      "severity": "NORMAL",
      "source": "Demo Weather Data"
    }
  ]
}
```

---

## 6. Co-Passenger Berth Exchange (`/api/berth-exchange`)

### 6.1 Discover Eligible Swap Passengers
* **Method**: `GET`
* **Path**: `/api/berth-exchange/eligible?trainId=1&journeyDate=2026-10-15&coachType=3A&excludeBookingId=101`
* **Response (200 OK)**: List of co-passengers on the same train/date who satisfy exchange eligibility.

### 6.2 Propose Berth Swap Request
* **Method**: `POST`
* **Path**: `/api/berth-exchange/request`
* **Request Body**:
```json
{
  "requesterPassengerId": 501,
  "targetPassengerId": 508,
  "reason": "Traveling with elderly parent, requesting lower berth swap"
}
```
* **Response (201 Created)**: Returns exchange request with status `PENDING` and audit log trace.

### 6.3 Respond to Berth Swap Request (Accept/Decline)
* **Method**: `POST`
* **Path**: `/api/berth-exchange/respond/{requestId}`
* **Request Body**:
```json
{
  "accept": true,
  "reason": "Happy to swap with lower berth"
}
```
* **Response (200 OK)**: Executes atomic seat swap transaction between the two passengers in the database and updates both tickets.

---

## 7. Operations & TTE Inspection (`/api/admin`, `/api/tickets`)

### 7.1 Admin Executive Dashboard KPIs
* **Method**: `GET`
* **Path**: `/api/admin/dashboard`
* **Access**: `ROLE_ADMIN`
* **Response (200 OK)**:
```json
{
  "totalTrains": 4,
  "totalStations": 12,
  "totalBookings": 28,
  "totalPassengers": 54,
  "totalRevenue": 24850.00,
  "occupancyRate": 84.5
}
```

### 7.2 Onboard TTE Ticket Inspection
* **Method**: `POST`
* **Path**: `/api/tickets/verify`
* **Access**: `ROLE_INSPECTOR` or `ROLE_ADMIN`
* **Request Body**:
```json
{
  "pnrOrTicketId": "4827193056",
  "comments": "Physical Aadhaar verified onboard at Villupuram Jn"
}
```
* **Response (200 OK)**:
```json
{
  "verified": true,
  "pnr": "4827193056",
  "trainNumber": "12638",
  "coach": "B1",
  "seats": "21, 22",
  "verifiedBy": "inspector_anand",
  "verificationTimestamp": "2026-09-25T21:42:15Z"
}
```

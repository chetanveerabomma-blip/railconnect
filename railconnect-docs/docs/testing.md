# RAILCONNECT — Testing & Quality Assurance Report

This document details the test suite, verification methodologies, and test coverage implemented across all RAILCONNECT repositories.

---

## 1. Testing Frameworks & Tools

* **JUnit 5 (Jupiter)**: Modern assertion engine and test lifecycle management.
* **Mockito**: Mocking dependencies, isolating service logic, and stubbing repository queries.
* **Spring Boot Test**: In-memory Spring context verification and mock MVC endpoint assertions.
* **Maven Surefire Plugin**: Automated test runner and build-time test reporting.

---

## 2. Test Execution Matrix

All modules include dedicated unit and integration tests. Below is the test verification summary:

| Test Class | Module / Repository | Purpose | Test Cases | Status |
|---|---|---|:---:|:---:|
| `AuthServiceTest` | `railconnect-auth` | User registration, password hashing, JWT creation & validation | 5 | PASSED |
| `SeatEngineTest` | `railconnect-seat-engine` | 10-min dynamic seat lock, concurrency conflicts, auto-allocation heuristics | 6 | PASSED |
| `BookingServiceTest` | `railconnect-booking` | 10-digit collision-free PNR, fare formula calculation, cancellation refund | 6 | PASSED |
| `WeatherServiceTest` | `railconnect-weather` | Weather severity calculation, station telemetry, demo data fallback | 4 | PASSED |
| `BerthExchangeTest` | `railconnect-berth-exchange` | 10 railway rules verification, atomic seat swap transaction | 5 | PASSED |
| `AdminServiceTest` | `railconnect-admin` | KPI metric aggregations, TTE onboard pass inspection | 4 | PASSED |
| `RailConnectApplicationTests` | `railconnect-backend` | Complete Spring Boot application context load & integration | 2 | PASSED |

**Total Test Suite**: 32 Test Cases | **Success Rate**: 100%

---

## 3. Key Unit Test Scenarios

### 3.1 Dynamic Seat Locking Test (`SeatEngineTest.java`)
* **Scenario**: Two concurrent threads attempt to lock the same seat simultaneously.
* **Assertion**: First thread acquires lock successfully with 10-minute expiry; second thread receives `SeatAlreadyLockedException` or `false`.
* **Expiry Assertion**: After simulated TTL expiration, subsequent acquisition succeeds.

### 3.2 Senior Citizen Lower Berth Priority Test (`SeatEngineTest.java`)
* **Scenario**: Passenger manifest with two senior citizens (age 68 and 64) and two young adults (age 24 and 26).
* **Assertion**: Auto-allocation engine assigns both senior citizens `LOWER` berths, while young adults receive `MIDDLE` or `UPPER` berths in the adjacent bay.

### 3.3 Dynamic Fare Formula Test (`BookingServiceTest.java`)
* **Scenario**: Route distance 336 km, class `3A` (Base rate ₹1.25/km), reservation fee ₹40, superfast surcharge ₹45, GST 5%.
* **Assertion**: Base fare = $336 \times 1.25 = 420$. Subtotal = $420 + 40 + 45 = 505$. GST = $505 \times 0.05 = 25.25$. Total fare = ₹530.25 (matches calculation to within 0.01 precision).

### 3.4 The 10-Rule Berth Exchange Engine Test (`BerthExchangeTest.java`)
* **Positive Scenario**: Co-passengers on Train 12638, same journey date, matching 3A class, mutual consent.
  * **Assertion**: Rule engine evaluates all 10 rules to `VALID`, swaps seat foreign keys atomically, and commits swap audit log.
* **Negative Scenario**: Requesters on different train numbers or cross-class (SL to 2A).
  * **Assertion**: Engine immediately rejects with specific violation: `"Rule 4 Violation: Cross-class berth exchanges are not permitted"`.

---

## 4. Running the Tests

To execute tests across all modules from the terminal:

```bash
# Run unit tests across all backend modules
cd railconnect-backend
mvn test

# Run a specific module test suite
cd ../railconnect-seat-engine
mvn test -Dtest=SeatEngineTest
```

Expected output:
```
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.railconnect.backend.RailConnectApplicationTests
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 2.14 s
[INFO] 
[INFO] Results:
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

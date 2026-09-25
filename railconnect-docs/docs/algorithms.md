# RAILCONNECT — Data Structures & Algorithms (DSA) Specification

This document details the core computational algorithms, algorithmic complexities, and data structure choices implemented across the RAILCONNECT backend.

---

## 1. Intelligent Seat Allocation Algorithm (DSA Heuristic Engine)

**Location**: `railconnect-seat-engine/src/main/java/com/railconnect/seatengine/service/SeatAllocationService.java`

### 1.1 Objective
To automatically assign optimal seats to a passenger manifest while fulfilling three simultaneous real-world railway constraints:
1. **Senior Citizen Accessibility**: Travelers aged 60+ must be allocated `LOWER` berths wherever physically available.
2. **Family & Group Adjacency**: Passengers in the same booking must be clustered together within the same physical bay (coupe/cabin).
3. **Female Traveler Safety**: Lone female passengers or female groups are clustered in adjacent berths.

### 1.2 Data Structures Employed
* **`PriorityQueue<SeatCandidate>`**:
  * Min-heap ordered by a custom comparator that computes a penalty score based on distance from lower berths and bay separation.
  * Ensures senior citizens extract lower berths in $O(\log N)$ time.
* **`HashMap<Integer, List<Seat>>` (Bay Map)**:
  * Partitions the coach seats by physical bay number ($Bay = \lceil SeatNo / 8 \rceil$ in Indian 3A/SL coaches).
  * Enables $O(1)$ lookup for contiguous vacant clusters within the same compartment.
* **`Queue<PassengerRequest>` (FIFO Queue)**:
  * Traverses the passenger manifest to process high-priority constrained requests first before assigning general unconstrained travelers.

### 1.3 Algorithmic Steps
```
Input: List<Passenger> passengers, CoachLayout layout
Output: Map<Passenger, Seat> assignment

1. Partition passengers:
   - HighPriorityQueue: Senior Citizens (Age >= 60)
   - StandardQueue: General adults & minors
2. Group layout seats by Bay ID into HashMap<Integer, List<Seat>>:
   - Filter out already BOOKED or LOCKED seats.
3. For each senior in HighPriorityQueue:
   - Query layout PriorityQueue for available LOWER berths.
   - If available: assign LOWER berth, mark seat as ALLOCATED, remove from BayMap.
   - Else: fallback to lowest available berth level.
4. For remaining passengers in StandardQueue:
   - Find bay with max(available_adjacent_seats >= remaining_count).
   - If cluster found: allocate contiguous seats in that bay.
   - Else: greedily allocate closest available seats in adjacent bays.
5. Return complete assignment map.
```
* **Time Complexity**: $O(P \log S + B)$, where $P$ is passenger count, $S$ is total coach seats, and $B$ is bay count.
* **Space Complexity**: $O(S)$ auxiliary space for heap and bay hash indices.

---

## 2. Dynamic 10-Minute Seat Locking & Concurrency Management

**Location**: `railconnect-seat-engine/src/main/java/com/railconnect/seatengine/service/SeatLockService.java`

### 2.1 Concurrency Challenge
Multiple passengers browsing the interactive seat map simultaneously must not book the same seat at the same instant (race conditions). When a user selects a seat, it must be locked exclusively for **10 minutes** (600 seconds) while they enter passenger details and complete payment. If payment completes, the lock converts to `BOOKED`. If payment times out or the tab is closed, the lock must expire cleanly and return to `AVAILABLE`.

### 2.2 Thread-Safe Architecture
* **`ConcurrentHashMap<String, SeatLock>`**:
  * Composite key: `"{seatId}:{journeyDate}"`.
  * Guarantees atomic lock acquisition via `putIfAbsent()`.
* **Atomic CAS Execution**:
  ```java
  public boolean tryLockSeat(Long seatId, LocalDate date, Long userId, int durationMinutes) {
      String lockKey = seatId + ":" + date;
      Instant expiry = Instant.now().plus(durationMinutes, ChronoUnit.MINUTES);
      SeatLock newLock = new SeatLock(seatId, date, userId, expiry);

      SeatLock existing = activeLocks.putIfAbsent(lockKey, newLock);
      if (existing != null) {
          if (existing.isExpired()) {
              // Atomically replace stale expired lock
              return activeLocks.replace(lockKey, existing, newLock);
          }
          return false; // Currently locked by another concurrent thread
      }
      return true;
  }
  ```
* **Scheduled Lock Reaper Daemon**:
  * An asynchronous background cron triggers every 60 seconds (`@Scheduled(fixedRate = 60000)`) to sweep the map and evict expired locks, emitting an event to restore the seat state in database storage.

---

## 3. Collision-Free 10-Digit PNR Generation Algorithm

**Location**: `railconnect-booking/src/main/java/com/railconnect/booking/service/PNRService.java`

### 3.1 Design Requirements
* Must be strictly **10 numeric digits** (no letters or special characters).
* Must be non-sequential and cryptographically unpredictable to prevent enumeration attacks.
* Must guarantee mathematical uniqueness across millions of bookings with zero collisions.

### 3.2 Algorithm Specification
1. **Digits 1–3 (Railway Zone & Origin Node)**:
   * Deterministically mapped from the departure railway division (e.g., `482` for Southern Zone / TPJ division).
2. **Digits 4–6 (Julian Microsecond Counter)**:
   * Time-based entropy derived from Julian day modulo 1,000.
3. **Digits 7–9 (Cryptographic Pseudo-Random Entropy)**:
   * Generated using Java `SecureRandom.getInstanceStrong()`.
4. **Digit 10 (Luhn Checksum)**:
   * Computed via Luhn modulo 10 algorithm over the first 9 digits for self-validation.
5. **Database Collision Loop**:
   * Wrapped in a verification loop `while (bookingRepository.existsByPnr(candidatePnr))` ensuring collision-free persistence.

---

## 4. Graph-Based Train Route Finding & Stop Sequence

**Location**: `railconnect-booking/src/main/java/com/railconnect/booking/service/TrainSearchService.java`

### 4.1 Railway Network Graph
* Represented as a directed acyclic graph (DAG) where **Stations** are vertices $V$, and **Train Routes** are weighted directed edges $E$.
* Edge weights represent distance (km) and scheduled travel duration.

### 4.2 Route Traversal Logic
When searching for trains between station $S_{from}$ and $S_{to}$:
1. Query all routes passing through $S_{from}$ with sequence number $Seq_{from}$.
2. Intersect with routes of the same train passing through $S_{to}$ with sequence number $Seq_{to}$.
3. Filter only trains where $Seq_{from} < Seq_{to}$ (enforcing forward travel direction).
4. Interpolate intermediate stops, arrival times, and calculate net journey distance:
   $$Distance = Dist(S_{to}) - Dist(S_{from})$$

---

## 5. Dynamic Master Fare Calculation Formula

**Location**: `railconnect-booking/src/main/java/com/railconnect/booking/service/FareCalculationService.java`

The ticket fare is computed through a standardized multi-variable linear equation:

$$Fare = Base + ResFee + Surcharge + GST - Concession$$

Where:
* **$Base = Distance(km) \times Rate_{class}$** (e.g., ₹0.45/km for SL, ₹1.25/km for 3A, ₹1.85/km for 2A)
* **$ResFee$** = Class-specific fixed reservation fee (₹20 to ₹60)
* **$Surcharge$** = Superfast / Express surcharge if average speed exceeds 55 km/h
* **$GST = (Base + ResFee + Surcharge) \times 0.05$** (5% applicable only on AC classes: 3A, 2A, 1A, CC; exempt on Sleeper)
* **$Concession$** = 40% discount applied to Senior Citizens ($Age \ge 60$) on Base Fare.

---

## 6. The 10-Rule Berth Exchange Validation Engine

**Location**: `railconnect-berth-exchange/src/main/java/com/railconnect/berthexchange/service/ExchangeRuleEngine.java`

Before two passengers can swap berths, the engine verifies **10 strict railway business rules**:

1. **Rule 1 — Same Train Identity**: Requester and target must be booked on identical train numbers.
2. **Rule 2 — Identical Journey Date**: Journey travel dates must match exactly.
3. **Rule 3 — Station Route Overlap**: The target passenger's boarding and destination must span the requester's journey corridor.
4. **Rule 4 — Same Travel Class**: Swaps are strictly intra-class (e.g., 3A to 3A only; cross-class upgrades are prohibited).
5. **Rule 5 — Booking State Confirmation**: Both tickets must be in `CONFIRMED` status (RAC/Waitlist tickets cannot swap).
6. **Rule 6 — Onboard Verification Status**: Neither passenger's pass may be already marked as `CHECKED_OUT` by TTE.
7. **Rule 7 — Mutual Active Consent**: Target passenger must explicitly approve via `/respond` endpoint.
8. **Rule 8 — No Double Swapping Pending**: Neither passenger may have an unresolved concurrent swap request.
9. **Rule 9 — Quota Integrity**: Ladies quota seats cannot be exchanged to unverified male passengers.
10. **Rule 10 — Two-Phase Commit**: The seat assignment swap executes inside an `@Transactional(isolation = Isolation.SERIALIZABLE)` boundary ensuring atomic pointer swap with zero seat loss.

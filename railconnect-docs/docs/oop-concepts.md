# RAILCONNECT — Object-Oriented Programming (OOP) Architecture

This document demonstrates how the four pillars of Object-Oriented Programming (**Encapsulation**, **Inheritance**, **Polymorphism**, and **Abstraction**), along with classical GoF Software Design Patterns, are implemented in the RAILCONNECT Java backend.

---

## 1. Encapsulation

Encapsulation conceals the internal state of objects and enforces data integrity through controlled accessors, mutators, and business invariants.

### 1.1 Private Fields & Access Control
All domain entity classes declare attributes with `private` visibility, disallowing direct field manipulation from external packages.

```java
// File: railconnect-seat-engine/.../model/SeatLock.java
public class SeatLock {
    private Long id;
    private Long seatId;
    private LocalDate journeyDate;
    private Long userId;
    private Instant lockedAt;
    private Instant expiresAt;
    private boolean active;

    // Controlled accessor
    public boolean isExpired() {
        return Instant.now().isAfter(this.expiresAt);
    }

    // Business mutator encapsulating state transition
    public void release() {
        this.active = false;
    }
}
```

### 1.2 Immutability in DTOs and Value Objects
Data Transfer Objects (e.g., `FareCalculationDto`, `AuthResponse`) protect against accidental side effects by utilizing final fields or Java records to enforce immutability across network boundaries.

---

## 2. Inheritance

Inheritance facilitates code reuse, establishes hierarchical relationships, and avoids code duplication across distinct modules.

### 2.1 Base Entity Mapping
Common auditory and identifier attributes are abstracted into a shared base mapped superclass inherited by domain entities.

```java
// File: railconnect-booking/.../model/BaseEntity.java
@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Getters and Setters
}

// Concrete subclass inheriting identity and audit timestamps
@Entity
@Table(name = "bookings")
public class Booking extends BaseEntity {
    @Column(nullable = false, unique = true, length = 10)
    private String pnr;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;
    // Specific booking fields...
}
```

### 2.2 Hierarchical Custom Exceptions
A custom application exception hierarchy permits granular error catching and unified HTTP translation:
* `RailConnectException` (Abstract Root)
  * `SeatAlreadyLockedException` (HTTP 409 Conflict)
  * `InvalidBerthExchangeException` (HTTP 422 Unprocessable Entity)
  * `ResourceNotFoundException` (HTTP 404 Not Found)

---

## 3. Polymorphism

Polymorphism allows objects of different types to be treated through a unified interface, executing specialized runtime behavior dynamically.

### 3.1 Strategy Pattern for Payment Processing
Payment calculation and simulated gateways implement a common polymorphic strategy interface:

```java
public interface PaymentStrategy {
    PaymentResult processPayment(PaymentRequest request);
    PaymentMode getSupportedMode();
}

@Service
public class UpiPaymentStrategy implements PaymentStrategy {
    @Override
    public PaymentResult processPayment(PaymentRequest request) {
        // Validate VPA handle, generate UPI intent reference
        return new PaymentResult(true, "UPI-REF-" + UUID.randomUUID());
    }
    @Override
    public PaymentMode getSupportedMode() { return PaymentMode.UPI; }
}

@Service
public class CardPaymentStrategy implements PaymentStrategy {
    @Override
    public PaymentResult processPayment(PaymentRequest request) {
        // Card tokenization & 3D Secure simulation
        return new PaymentResult(true, "CARD-TXN-" + UUID.randomUUID());
    }
    @Override
    public PaymentMode getSupportedMode() { return PaymentMode.CREDIT_CARD; }
}
```

### 3.2 Dynamic Fare Calculation Discounts
Different passenger demographics (Standard, Senior Citizen, Student) implement polymorphic concession calculators:

```java
public interface ConcessionStrategy {
    double applyDiscount(double baseFare, Passenger passenger);
}

public class SeniorCitizenConcessionStrategy implements ConcessionStrategy {
    @Override
    public double applyDiscount(double baseFare, Passenger passenger) {
        return (passenger.getAge() >= 60) ? (baseFare * 0.40) : 0.0;
    }
}
```

---

## 4. Abstraction

Abstraction defines high-level contracts while concealing complex implementation details from callers.

### 4.1 Interface-Driven Services
Services are defined through clean Java interfaces, decoupling controllers from database or third-party API dependencies:

```java
// Abstract Contract
public interface WeatherService {
    JourneyWeatherResponse getJourneyWeather(String fromStation, String toStation, LocalDate date);
    StationWeatherDto getStationWeather(String stationCode);
}

// Concrete Implementation with Fallback Resilience
@Service
public class WeatherServiceImpl implements WeatherService {
    // Conceals OpenWeatherMap HTTP calls, JSON parsing, API key security,
    // and automatic fallback to "Demo Weather Data" simulation
}
```

### 4.2 Spring Data Repository Abstraction
By extending `JpaRepository<Booking, Long>`, data access queries are generated abstractly at runtime without writing low-level JDBC SQL or connection pool boilerplates.

---

## 5. Software Design Patterns Summary

| Pattern | Category | Implementation in RAILCONNECT |
|---|---|---|
| **Strategy Pattern** | Behavioral | `PaymentStrategy` (UPI, Card, NetBanking) and `ConcessionStrategy` |
| **Factory Pattern** | Creational | `SeatFactory` generates seats with position types (`LOWER`, `UPPER`, `SIDE_LOWER`) |
| **Builder Pattern** | Creational | DTO creation via Lombok `@Builder` (`TrainSearchResponse.builder()...build()`) |
| **Singleton Pattern** | Creational | Spring container manages all `@Service` and `@Component` singletons |
| **Observer Pattern** | Behavioral | Spring `@EventListener` for audit logs and seat lock release notifications |
| **Repository Pattern** | Architectural | Spring Data JPA interfaces decoupling business logic from relational storage |
| **DTO Pattern** | Structural | Decouples internal database entities from external REST JSON payloads |

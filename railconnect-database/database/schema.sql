-- =============================================================================
-- RAILCONNECT: Intelligent Railway Booking, Digital Travel Pass & Journey Assistance
-- Repository 3: railconnect-database
-- File: database/schema.sql
-- Relational Schema DDL for MySQL 8.0+
-- =============================================================================

DROP DATABASE IF EXISTS railconnect_db;
CREATE DATABASE railconnect_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE railconnect_db;

-- -----------------------------------------------------------------------------
-- 1. ROLES TABLE
-- -----------------------------------------------------------------------------
CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
) ENGINE=InnoDB;

-- -----------------------------------------------------------------------------
-- 2. USERS TABLE
-- -----------------------------------------------------------------------------
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'ROLE_PASSENGER',
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_users_username (username),
    INDEX idx_users_email (email),
    INDEX idx_users_role (role)
) ENGINE=InnoDB;

-- -----------------------------------------------------------------------------
-- 3. PASSENGERS TABLE (Extended Profile)
-- -----------------------------------------------------------------------------
CREATE TABLE passengers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    id_card_type VARCHAR(30) DEFAULT 'AADHAAR',
    id_card_number VARCHAR(50),
    age INT,
    gender VARCHAR(10),
    address VARCHAR(255),
    city VARCHAR(100),
    state VARCHAR(100),
    pincode VARCHAR(10),
    preferred_language VARCHAR(10) DEFAULT 'en',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- -----------------------------------------------------------------------------
-- 4. ADMINS TABLE
-- -----------------------------------------------------------------------------
CREATE TABLE admins (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    department VARCHAR(100) NOT NULL,
    employee_id VARCHAR(50) NOT NULL UNIQUE,
    access_level VARCHAR(30) DEFAULT 'SUPER_ADMIN',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- -----------------------------------------------------------------------------
-- 5. STATIONS TABLE
-- -----------------------------------------------------------------------------
CREATE TABLE stations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(10) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    city VARCHAR(100) NOT NULL,
    state VARCHAR(100) NOT NULL,
    zone VARCHAR(20) NOT NULL,
    platform_count INT DEFAULT 4,
    latitude DECIMAL(10, 6),
    longitude DECIMAL(10, 6),
    INDEX idx_station_code (code),
    INDEX idx_station_city (city)
) ENGINE=InnoDB;

-- -----------------------------------------------------------------------------
-- 6. TRAINS TABLE
-- -----------------------------------------------------------------------------
CREATE TABLE trains (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    train_number VARCHAR(10) NOT NULL UNIQUE,
    train_name VARCHAR(100) NOT NULL,
    train_type VARCHAR(50) NOT NULL DEFAULT 'EXPRESS', -- VANDE_BHARAT, RAJDHANI, SHATABDI, EXPRESS, SUPERFAST
    source_station_id BIGINT NOT NULL,
    destination_station_id BIGINT NOT NULL,
    departure_time TIME NOT NULL,
    arrival_time TIME NOT NULL,
    duration_hours DECIMAL(4, 2) NOT NULL,
    running_days VARCHAR(50) NOT NULL DEFAULT 'MON,TUE,WED,THU,FRI,SAT,SUN',
    active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (source_station_id) REFERENCES stations(id),
    FOREIGN KEY (destination_station_id) REFERENCES stations(id),
    INDEX idx_train_number (train_number),
    INDEX idx_train_type (train_type)
) ENGINE=InnoDB;

-- -----------------------------------------------------------------------------
-- 7. TRAIN_ROUTES TABLE (Stops & Sequence)
-- -----------------------------------------------------------------------------
CREATE TABLE train_routes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    train_id BIGINT NOT NULL,
    station_id BIGINT NOT NULL,
    stop_sequence INT NOT NULL,
    distance_from_source_km DECIMAL(7, 2) NOT NULL DEFAULT 0.0,
    arrival_time TIME,
    departure_time TIME,
    halt_minutes INT DEFAULT 2,
    day_count INT DEFAULT 1,
    FOREIGN KEY (train_id) REFERENCES trains(id) ON DELETE CASCADE,
    FOREIGN KEY (station_id) REFERENCES stations(id),
    UNIQUE KEY uk_train_stop_seq (train_id, stop_sequence),
    UNIQUE KEY uk_train_station (train_id, station_id),
    INDEX idx_route_lookup (station_id, train_id)
) ENGINE=InnoDB;

-- -----------------------------------------------------------------------------
-- 8. COACHES TABLE
-- -----------------------------------------------------------------------------
CREATE TABLE coaches (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    train_id BIGINT NOT NULL,
    coach_number VARCHAR(10) NOT NULL, -- e.g. B1, B2, A1, S1, C1
    coach_type VARCHAR(20) NOT NULL,   -- 1A, 2A, 3A, SL, CC, 2S
    total_seats INT NOT NULL DEFAULT 72,
    FOREIGN KEY (train_id) REFERENCES trains(id) ON DELETE CASCADE,
    UNIQUE KEY uk_train_coach (train_id, coach_number),
    INDEX idx_coach_type (coach_type)
) ENGINE=InnoDB;

-- -----------------------------------------------------------------------------
-- 9. SEATS TABLE
-- -----------------------------------------------------------------------------
CREATE TABLE seats (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    coach_id BIGINT NOT NULL,
    seat_number INT NOT NULL,
    berth_type VARCHAR(30) NOT NULL, -- LOWER, MIDDLE, UPPER, SIDE_LOWER, SIDE_UPPER, WINDOW, AISLE
    cabin_number INT DEFAULT 1,
    FOREIGN KEY (coach_id) REFERENCES coaches(id) ON DELETE CASCADE,
    UNIQUE KEY uk_coach_seat (coach_id, seat_number),
    INDEX idx_seat_berth (berth_type)
) ENGINE=InnoDB;

-- -----------------------------------------------------------------------------
-- 10. FARE_RULES TABLE
-- -----------------------------------------------------------------------------
CREATE TABLE fare_rules (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    train_type VARCHAR(50) NOT NULL,
    coach_type VARCHAR(20) NOT NULL,
    base_fare_per_km DECIMAL(6, 2) NOT NULL,
    reservation_charge DECIMAL(6, 2) NOT NULL DEFAULT 40.0,
    superfast_charge DECIMAL(6, 2) NOT NULL DEFAULT 30.0,
    gst_percentage DECIMAL(4, 2) NOT NULL DEFAULT 5.0,
    tatkal_multiplier DECIMAL(4, 2) NOT NULL DEFAULT 1.30,
    UNIQUE KEY uk_train_coach_fare (train_type, coach_type)
) ENGINE=InnoDB;

-- -----------------------------------------------------------------------------
-- 11. BOOKINGS TABLE
-- -----------------------------------------------------------------------------
CREATE TABLE bookings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pnr_number VARCHAR(10) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    train_id BIGINT NOT NULL,
    from_station_id BIGINT NOT NULL,
    to_station_id BIGINT NOT NULL,
    journey_date DATE NOT NULL,
    booking_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    coach_type VARCHAR(20) NOT NULL,
    total_fare DECIMAL(10, 2) NOT NULL,
    base_fare DECIMAL(10, 2) NOT NULL,
    reservation_charge DECIMAL(8, 2) NOT NULL DEFAULT 0.0,
    service_charge DECIMAL(8, 2) NOT NULL DEFAULT 0.0,
    tax_amount DECIMAL(8, 2) NOT NULL DEFAULT 0.0,
    discount_amount DECIMAL(8, 2) NOT NULL DEFAULT 0.0,
    status VARCHAR(30) NOT NULL DEFAULT 'CONFIRMED', -- CONFIRMED, RAC, WAITLISTED, CANCELLED
    cancellation_reason VARCHAR(255),
    cancellation_date TIMESTAMP NULL,
    refund_amount DECIMAL(10, 2) DEFAULT 0.0,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (train_id) REFERENCES trains(id),
    FOREIGN KEY (from_station_id) REFERENCES stations(id),
    FOREIGN KEY (to_station_id) REFERENCES stations(id),
    INDEX idx_pnr (pnr_number),
    INDEX idx_booking_user (user_id),
    INDEX idx_booking_date (journey_date),
    INDEX idx_booking_status (status)
) ENGINE=InnoDB;

-- -----------------------------------------------------------------------------
-- 12. BOOKING_PASSENGERS TABLE
-- -----------------------------------------------------------------------------
CREATE TABLE booking_passengers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    booking_id BIGINT NOT NULL,
    seat_id BIGINT NULL,
    passenger_name VARCHAR(100) NOT NULL,
    passenger_age INT NOT NULL,
    passenger_gender VARCHAR(10) NOT NULL,
    berth_preference VARCHAR(30) DEFAULT 'NO_PREFERENCE',
    allocated_berth_type VARCHAR(30),
    allocated_coach VARCHAR(10),
    allocated_seat_number INT,
    status VARCHAR(30) NOT NULL DEFAULT 'CONFIRMED', -- CONFIRMED, RAC, WAITLISTED, CANCELLED
    FOREIGN KEY (booking_id) REFERENCES bookings(id) ON DELETE CASCADE,
    FOREIGN KEY (seat_id) REFERENCES seats(id) ON DELETE SET NULL,
    INDEX idx_bp_booking (booking_id),
    INDEX idx_bp_seat (seat_id)
) ENGINE=InnoDB;

-- -----------------------------------------------------------------------------
-- 13. PAYMENTS TABLE
-- -----------------------------------------------------------------------------
CREATE TABLE payments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    booking_id BIGINT NOT NULL,
    transaction_id VARCHAR(100) NOT NULL UNIQUE,
    payment_method VARCHAR(50) NOT NULL, -- UPI, CREDIT_CARD, DEBIT_CARD, NET_BANKING
    amount DECIMAL(10, 2) NOT NULL,
    payment_status VARCHAR(30) NOT NULL DEFAULT 'SUCCESS', -- SUCCESS, FAILED, REFUNDED
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    gateway_response VARCHAR(500),
    FOREIGN KEY (booking_id) REFERENCES bookings(id) ON DELETE CASCADE,
    INDEX idx_payment_tx (transaction_id),
    INDEX idx_payment_booking (booking_id)
) ENGINE=InnoDB;

-- -----------------------------------------------------------------------------
-- 14. WEATHER_ALERTS TABLE
-- -----------------------------------------------------------------------------
CREATE TABLE weather_alerts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    station_id BIGINT NOT NULL,
    temperature_c DECIMAL(4, 1) NOT NULL,
    weather_condition VARCHAR(100) NOT NULL,
    rain_probability INT NOT NULL DEFAULT 0,
    humidity_percent INT NOT NULL DEFAULT 50,
    wind_speed_kmh DECIMAL(5, 1) NOT NULL DEFAULT 10.0,
    visibility_km DECIMAL(4, 1) NOT NULL DEFAULT 10.0,
    severity VARCHAR(20) NOT NULL DEFAULT 'NORMAL', -- NORMAL, CAUTION, WARNING, SEVERE
    alert_message VARCHAR(255) NOT NULL,
    recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (station_id) REFERENCES stations(id) ON DELETE CASCADE,
    INDEX idx_weather_station (station_id),
    INDEX idx_weather_severity (severity)
) ENGINE=InnoDB;

-- -----------------------------------------------------------------------------
-- 15. BERTH_EXCHANGE_REQUESTS TABLE
-- -----------------------------------------------------------------------------
CREATE TABLE berth_exchange_requests (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    requester_booking_id BIGINT NOT NULL,
    requester_passenger_id BIGINT NOT NULL,
    target_booking_id BIGINT NOT NULL,
    target_passenger_id BIGINT NOT NULL,
    train_id BIGINT NOT NULL,
    journey_date DATE NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'REQUESTED',
    -- REQUESTED, ACCEPTED, REJECTED, PENDING_ADMIN, APPROVED, COMPLETED, CANCELLED
    request_reason VARCHAR(255),
    admin_notes VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (requester_booking_id) REFERENCES bookings(id),
    FOREIGN KEY (requester_passenger_id) REFERENCES booking_passengers(id),
    FOREIGN KEY (target_booking_id) REFERENCES bookings(id),
    FOREIGN KEY (target_passenger_id) REFERENCES booking_passengers(id),
    FOREIGN KEY (train_id) REFERENCES trains(id),
    INDEX idx_berth_status (status),
    INDEX idx_berth_journey (train_id, journey_date)
) ENGINE=InnoDB;

-- -----------------------------------------------------------------------------
-- 16. TICKET_VERIFICATIONS TABLE (TTE / Inspector)
-- -----------------------------------------------------------------------------
CREATE TABLE ticket_verifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    booking_id BIGINT NOT NULL,
    inspector_id BIGINT NOT NULL,
    station_id BIGINT NOT NULL,
    verification_status VARCHAR(30) NOT NULL DEFAULT 'VERIFIED', -- VERIFIED, ABSENT, FRAUDULENT, UNVERIFIED
    comments VARCHAR(255),
    verification_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (booking_id) REFERENCES bookings(id) ON DELETE CASCADE,
    FOREIGN KEY (inspector_id) REFERENCES users(id),
    FOREIGN KEY (station_id) REFERENCES stations(id),
    INDEX idx_tv_booking (booking_id),
    INDEX idx_tv_inspector (inspector_id)
) ENGINE=InnoDB;

-- -----------------------------------------------------------------------------
-- 17. NOTIFICATIONS TABLE
-- -----------------------------------------------------------------------------
CREATE TABLE notifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(150) NOT NULL,
    message TEXT NOT NULL,
    notification_type VARCHAR(50) DEFAULT 'INFO', -- BOOKING, WEATHER, BERTH_EXCHANGE, SYSTEM
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_notif_user (user_id, is_read)
) ENGINE=InnoDB;

-- -----------------------------------------------------------------------------
-- 18. AUDIT_LOGS TABLE
-- -----------------------------------------------------------------------------
CREATE TABLE audit_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NULL,
    action VARCHAR(100) NOT NULL,
    entity_type VARCHAR(50) NOT NULL,
    entity_id VARCHAR(100),
    old_value TEXT,
    new_value TEXT,
    ip_address VARCHAR(45),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_audit_user (user_id),
    INDEX idx_audit_action (action)
) ENGINE=InnoDB;

-- =============================================================================
-- RAILCONNECT: Intelligent Railway Booking, Digital Travel Pass & Journey Assistance
-- Repository 3: railconnect-database
-- File: database/sample-data.sql
-- Realistic Demonstration Data
-- =============================================================================

USE railconnect_db;

-- 1. Insert Demo Users (BCrypt hashed 'password123' -> $2a$10$wT08Z8G8oXwF05I2jWzZfuvgP1gD5Z1uW8D4xQcE6n8r5Hq2v3q2.)
-- We will store standard BCrypt hash for testing: $2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.
INSERT INTO users (id, username, email, phone, password_hash, role, first_name, last_name, status) VALUES
(1, 'admin', 'admin@railconnect.com', '9876543299', '$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.', 'ROLE_ADMIN', 'Rajesh', 'Verma', 'ACTIVE'),
(2, 'inspector_anand', 'inspector@railconnect.com', '9876543288', '$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.', 'ROLE_INSPECTOR', 'Anand', 'Mohan', 'ACTIVE'),
(3, 'rahul_sharma', 'rahul@railconnect.com', '9876543210', '$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.', 'ROLE_PASSENGER', 'Rahul', 'Sharma', 'ACTIVE'),
(4, 'priya_patel', 'priya@railconnect.com', '9876543211', '$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.', 'ROLE_PASSENGER', 'Priya', 'Patel', 'ACTIVE'),
(5, 'suresh_kumar', 'suresh@railconnect.com', '9876543212', '$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.', 'ROLE_PASSENGER', 'Suresh', 'Kumar', 'ACTIVE');

-- 2. Insert Admin Details
INSERT INTO admins (user_id, department, employee_id, access_level) VALUES
(1, 'Northern Railway Headquarters', 'NR-ADM-1001', 'SUPER_ADMIN');

-- 3. Insert Passenger Profiles
INSERT INTO passengers (user_id, id_card_type, id_card_number, age, gender, address, city, state, pincode, preferred_language) VALUES
(3, 'AADHAAR', '9876-1234-5678', 29, 'MALE', 'Flat 402, Green Valley Apartments', 'New Delhi', 'Delhi', '110001', 'en'),
(4, 'AADHAAR', '8765-2345-6789', 27, 'FEMALE', 'B-12, Heritage Enclave', 'Chennai', 'Tamil Nadu', '600001', 'ta'),
(5, 'PAN_CARD', 'ABCDE1234F', 62, 'MALE', 'Plot 88, Anna Nagar', 'Tiruchirappalli', 'Tamil Nadu', '620001', 'ta');

-- 4. Insert Demo Bookings with unique 10-digit PNR
-- Booking 1: Rahul Sharma traveling TPJ -> MAS on 12638 Pandian Express
INSERT INTO bookings (id, pnr_number, user_id, train_id, from_station_id, to_station_id, journey_date, coach_type, total_fare, base_fare, reservation_charge, service_charge, tax_amount, discount_amount, status) VALUES
(1, '4827193056', 3, 2, 4, 3, DATE_ADD(CURRENT_DATE, INTERVAL 2 DAY), '3A', 612.00, 420.00, 40.00, 30.00, 22.00, 0.00, 'CONFIRMED'),
-- Booking 2: Priya Patel traveling TPJ -> MAS on 12638 Pandian Express (Same train, eligible for Berth Exchange!)
(2, '8192304851', 4, 2, 4, 3, DATE_ADD(CURRENT_DATE, INTERVAL 2 DAY), '3A', 612.00, 420.00, 40.00, 30.00, 22.00, 0.00, 'CONFIRMED'),
-- Booking 3: Suresh Kumar traveling SBC -> MAS on 20608 Vande Bharat
(3, '9201847562', 5, 1, 5, 3, DATE_ADD(CURRENT_DATE, INTERVAL 5 DAY), 'CC', 820.00, 680.00, 40.00, 30.00, 35.00, 0.00, 'CONFIRMED');

-- 5. Insert Booking Passengers
-- Rahul has Seat 1 (LOWER) in Coach B1
INSERT INTO booking_passengers (id, booking_id, seat_id, passenger_name, passenger_age, passenger_gender, berth_preference, allocated_berth_type, allocated_coach, allocated_seat_number, status) VALUES
(1, 1, 1, 'Rahul Sharma', 29, 'MALE', 'LOWER', 'LOWER', 'B1', 1, 'CONFIRMED'),
-- Priya has Seat 3 (UPPER) in Coach B1 (wants lower berth)
(2, 2, 3, 'Priya Patel', 27, 'FEMALE', 'UPPER', 'UPPER', 'B1', 3, 'CONFIRMED'),
-- Suresh has Seat 1 (WINDOW) in Coach C1
(3, 3, 17, 'Suresh Kumar', 62, 'MALE', 'WINDOW', 'WINDOW', 'C1', 1, 'CONFIRMED');

-- 6. Insert Payments
INSERT INTO payments (booking_id, transaction_id, payment_method, amount, payment_status, gateway_response) VALUES
(1, 'TXN-RAIL-20260925-001', 'UPI', 612.00, 'SUCCESS', '{"status":"SUCCESS","upi_ref":"992817201"}'),
(2, 'TXN-RAIL-20260925-002', 'CREDIT_CARD', 612.00, 'SUCCESS', '{"status":"SUCCESS","card_network":"VISA"}'),
(3, 'TXN-RAIL-20260925-003', 'NET_BANKING', 820.00, 'SUCCESS', '{"status":"SUCCESS","bank":"SBI"}');

-- 7. Insert Weather Alerts (as specified in prompt)
INSERT INTO weather_alerts (station_id, temperature_c, weather_condition, rain_probability, humidity_percent, wind_speed_kmh, visibility_km, severity, alert_message) VALUES
(4, 32.0, 'Partly Cloudy', 20, 62, 14.5, 9.5, 'NORMAL', 'Clear skies with pleasant travelling conditions in Tiruchirappalli.'),
(3, 29.0, 'Heavy Rain', 80, 92, 28.0, 4.0, 'WARNING', 'High precipitation forecast. Coastal gusts expected near Chennai Central.'),
(1, 34.5, 'Sunny', 10, 45, 12.0, 10.0, 'NORMAL', 'Normal dry weather in New Delhi area.'),
(2, 28.0, 'Moderate Rain', 65, 88, 22.0, 6.0, 'CAUTION', 'Scattered showers expected in Mumbai Central vicinity.');

-- 8. Insert Berth Exchange Requests
-- Priya requests to exchange her UPPER berth (Seat 3) for Rahul's LOWER berth (Seat 1)
INSERT INTO berth_exchange_requests (id, requester_booking_id, requester_passenger_id, target_booking_id, target_passenger_id, train_id, journey_date, status, request_reason, admin_notes) VALUES
(1, 2, 2, 1, 1, 2, DATE_ADD(CURRENT_DATE, INTERVAL 2 DAY), 'REQUESTED', 'Prefers lower berth due to slight knee sprain. Both in same coach B1.', NULL);

-- 9. Insert Ticket Verification
INSERT INTO ticket_verifications (booking_id, inspector_id, station_id, verification_status, comments) VALUES
(1, 2, 4, 'VERIFIED', 'Passenger verified at departure gate with valid Photo ID.');

-- 10. Insert Notifications
INSERT INTO notifications (user_id, title, message, notification_type, is_read) VALUES
(3, 'Booking Confirmed!', 'Your ticket for Train 12638 Pandian Express is confirmed. PNR: 4827193056.', 'BOOKING', TRUE),
(3, 'New Berth Exchange Request', 'Passenger Priya Patel requested to exchange berth with you on Train 12638.', 'BERTH_EXCHANGE', FALSE),
(4, 'Weather Warning for Chennai', 'Heavy Rain expected at destination Chennai Central (Rain: 80%, Severity: WARNING).', 'WEATHER', FALSE);

-- 11. Insert Audit Logs
INSERT INTO audit_logs (user_id, action, entity_type, entity_id, old_value, new_value, ip_address) VALUES
(3, 'CREATE_BOOKING', 'BOOKING', '4827193056', NULL, 'STATUS: CONFIRMED, SEAT: B1-1', '192.168.1.10'),
(4, 'REQUEST_BERTH_EXCHANGE', 'BERTH_EXCHANGE', '1', NULL, 'REQUESTED: Seat 3 -> Seat 1', '192.168.1.15');

-- =============================================================================
-- RAILCONNECT: Intelligent Railway Booking, Digital Travel Pass & Journey Assistance
-- Repository 3: railconnect-database
-- File: database/seed.sql
-- Master Seed Reference Data
-- =============================================================================

USE railconnect_db;

-- 1. Insert Roles
INSERT INTO roles (name) VALUES 
('ROLE_PASSENGER'),
('ROLE_ADMIN'),
('ROLE_INSPECTOR');

-- 2. Insert Stations
INSERT INTO stations (id, code, name, city, state, zone, platform_count, latitude, longitude) VALUES
(1, 'NDLS', 'New Delhi Railway Station', 'New Delhi', 'Delhi', 'NR', 16, 28.6429, 77.2195),
(2, 'BCT', 'Mumbai Central', 'Mumbai', 'Maharashtra', 'WR', 8, 18.9696, 72.8193),
(3, 'MAS', 'Chennai Central (Puratchi Thalaivar Dr. MGR)', 'Chennai', 'Tamil Nadu', 'SR', 12, 13.0827, 80.2707),
(4, 'TPJ', 'Tiruchchirappalli Junction', 'Tiruchirappalli', 'Tamil Nadu', 'SR', 8, 10.7938, 78.6864),
(5, 'SBC', 'KSR Bengaluru City Junction', 'Bengaluru', 'Karnataka', 'SWR', 10, 12.9781, 77.5694),
(6, 'HWH', 'Howrah Junction', 'Kolkata', 'West Bengal', 'ER', 23, 22.5840, 88.3426),
(7, 'HYB', 'Hyderabad Deccan (Nampally)', 'Hyderabad', 'Telangana', 'SCR', 6, 17.3916, 78.4685),
(8, 'ADI', 'Ahmedabad Junction (Kalupur)', 'Ahmedabad', 'Gujarat', 'WR', 12, 23.0238, 72.6009),
(9, 'CNB', 'Kanpur Central', 'Kanpur', 'Uttar Pradesh', 'NCR', 10, 26.4537, 80.3512),
(10, 'BSB', 'Varanasi Junction', 'Varanasi', 'Uttar Pradesh', 'NR', 9, 25.3284, 82.9863);

-- 3. Insert Fare Rules
INSERT INTO fare_rules (train_type, coach_type, base_fare_per_km, reservation_charge, superfast_charge, gst_percentage, tatkal_multiplier) VALUES
('EXPRESS', 'SL', 0.45, 20.00, 30.00, 0.00, 1.30),
('EXPRESS', '3A', 1.25, 40.00, 45.00, 5.00, 1.30),
('EXPRESS', '2A', 1.80, 50.00, 45.00, 5.00, 1.30),
('EXPRESS', '1A', 2.80, 60.00, 75.00, 5.00, 1.30),
('EXPRESS', '2S', 0.25, 15.00, 15.00, 0.00, 1.15),
('EXPRESS', 'CC', 0.95, 40.00, 45.00, 5.00, 1.25),
('VANDE_BHARAT', 'CC', 1.45, 40.00, 75.00, 5.00, 1.35),
('VANDE_BHARAT', 'EC', 2.65, 60.00, 75.00, 5.00, 1.35),
('RAJDHANI', '3A', 1.45, 40.00, 75.00, 5.00, 1.30),
('RAJDHANI', '2A', 2.10, 50.00, 75.00, 5.00, 1.30),
('RAJDHANI', '1A', 3.20, 60.00, 75.00, 5.00, 1.30);

-- 4. Insert Trains
INSERT INTO trains (id, train_number, train_name, train_type, source_station_id, destination_station_id, departure_time, arrival_time, duration_hours, running_days, active) VALUES
(1, '20608', 'Vande Bharat Express', 'VANDE_BHARAT', 5, 3, '05:45:00', '10:10:00', 4.42, 'MON,TUE,WED,THU,FRI,SUN', TRUE),
(2, '12638', 'Pandian Superfast Express', 'EXPRESS', 4, 3, '21:35:00', '05:15:00', 7.67, 'MON,TUE,WED,THU,FRI,SAT,SUN', TRUE),
(3, '12952', 'Mumbai Rajdhani Express', 'RAJDHANI', 1, 2, '16:55:00', '08:35:00', 15.67, 'MON,TUE,WED,THU,FRI,SAT,SUN', TRUE),
(4, '12622', 'Tamil Nadu Express', 'EXPRESS', 1, 3, '21:05:00', '06:35:00', 33.50, 'MON,TUE,WED,THU,FRI,SAT,SUN', TRUE),
(5, '12002', 'Bhopal Shatabdi Express', 'EXPRESS', 1, 9, '06:00:00', '11:20:00', 5.33, 'MON,TUE,WED,THU,FRI,SAT,SUN', TRUE);

-- 5. Insert Train Routes (Intermediate Stops)
-- Train 1: 20608 (SBC -> MAS)
INSERT INTO train_routes (train_id, station_id, stop_sequence, distance_from_source_km, arrival_time, departure_time, halt_minutes, day_count) VALUES
(1, 5, 1, 0.0, NULL, '05:45:00', 0, 1),
(1, 3, 2, 359.0, '10:10:00', NULL, 0, 1);

-- Train 2: 12638 (TPJ -> MAS)
INSERT INTO train_routes (train_id, station_id, stop_sequence, distance_from_source_km, arrival_time, departure_time, halt_minutes, day_count) VALUES
(2, 4, 1, 0.0, NULL, '21:35:00', 0, 1),
(2, 3, 2, 336.0, '05:15:00', NULL, 0, 2);

-- Train 3: 12952 (NDLS -> BCT)
INSERT INTO train_routes (train_id, station_id, stop_sequence, distance_from_source_km, arrival_time, departure_time, halt_minutes, day_count) VALUES
(3, 1, 1, 0.0, NULL, '16:55:00', 0, 1),
(3, 8, 2, 935.0, '03:15:00', '03:25:00', 10, 2),
(3, 2, 3, 1384.0, '08:35:00', NULL, 0, 2);

-- Train 4: 12622 (NDLS -> CNB -> BSB -> MAS)
INSERT INTO train_routes (train_id, station_id, stop_sequence, distance_from_source_km, arrival_time, departure_time, halt_minutes, day_count) VALUES
(4, 1, 1, 0.0, NULL, '21:05:00', 0, 1),
(4, 9, 2, 440.0, '02:30:00', '02:40:00', 10, 2),
(4, 7, 3, 1660.0, '20:10:00', '20:25:00', 15, 2),
(4, 3, 4, 2182.0, '06:35:00', NULL, 0, 3);

-- Train 5: 12002 (NDLS -> CNB)
INSERT INTO train_routes (train_id, station_id, stop_sequence, distance_from_source_km, arrival_time, departure_time, halt_minutes, day_count) VALUES
(5, 1, 1, 0.0, NULL, '06:00:00', 0, 1),
(5, 9, 2, 440.0, '11:20:00', NULL, 0, 1);

-- 6. Insert Coaches
INSERT INTO coaches (id, train_id, coach_number, coach_type, total_seats) VALUES
-- Train 1 (20608 Vande Bharat)
(1, 1, 'C1', 'CC', 78),
(2, 1, 'C2', 'CC', 78),
(3, 1, 'E1', 'EC', 52),
-- Train 2 (12638 Pandian Express)
(4, 2, 'S1', 'SL', 72),
(5, 2, 'S2', 'SL', 72),
(6, 2, 'B1', '3A', 64),
(7, 2, 'A1', '2A', 48),
-- Train 3 (12952 Mumbai Rajdhani)
(8, 3, 'B1', '3A', 64),
(9, 3, 'B2', '3A', 64),
(10, 3, 'A1', '2A', 48),
(11, 3, 'H1', '1A', 24);

-- 7. Insert Sample Seats for Coach 6 (B1 of Train 2, 3A Sleeper: 8 seats per compartment)
INSERT INTO seats (coach_id, seat_number, berth_type, cabin_number) VALUES
(6, 1, 'LOWER', 1),
(6, 2, 'MIDDLE', 1),
(6, 3, 'UPPER', 1),
(6, 4, 'LOWER', 1),
(6, 5, 'MIDDLE', 1),
(6, 6, 'UPPER', 1),
(6, 7, 'SIDE_LOWER', 1),
(6, 8, 'SIDE_UPPER', 1),
(6, 9, 'LOWER', 2),
(6, 10, 'MIDDLE', 2),
(6, 11, 'UPPER', 2),
(6, 12, 'LOWER', 2),
(6, 13, 'MIDDLE', 2),
(6, 14, 'UPPER', 2),
(6, 15, 'SIDE_LOWER', 2),
(6, 16, 'SIDE_UPPER', 2);

-- Insert Seats for Coach 1 (C1 of Train 1, CC Chair Car: Window, Aisle, Middle)
INSERT INTO seats (coach_id, seat_number, berth_type, cabin_number) VALUES
(1, 1, 'WINDOW', 1),
(1, 2, 'MIDDLE', 1),
(1, 3, 'AISLE', 1),
(1, 4, 'AISLE', 1),
(1, 5, 'WINDOW', 1),
(1, 6, 'WINDOW', 2),
(1, 7, 'MIDDLE', 2),
(1, 8, 'AISLE', 2),
(1, 9, 'AISLE', 2),
(1, 10, 'WINDOW', 2);

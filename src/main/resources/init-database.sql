-- ATRS Database Initialization Script
-- Run this script to create the database and tables manually

CREATE DATABASE IF NOT EXISTS atrs DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE atrs;

-- User table
CREATE TABLE IF NOT EXISTS atrs_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    real_name VARCHAR(50),
    phone VARCHAR(20),
    email VARCHAR(100),
    id_card VARCHAR(18),
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Flight table
CREATE TABLE IF NOT EXISTS atrs_flight (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    flight_no VARCHAR(20) NOT NULL,
    airline VARCHAR(50) NOT NULL,
    departure_city VARCHAR(50) NOT NULL,
    arrival_city VARCHAR(50) NOT NULL,
    departure_airport VARCHAR(100) NOT NULL,
    arrival_airport VARCHAR(100) NOT NULL,
    departure_time TIME NOT NULL,
    arrival_time TIME NOT NULL,
    duration_minutes INT NOT NULL,
    flight_date DATE NOT NULL,
    base_price DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'SCHEDULED',
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    INDEX idx_city_date (departure_city, arrival_city, flight_date),
    INDEX idx_date (flight_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Flight seat inventory table
CREATE TABLE IF NOT EXISTS atrs_flight_seat (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    flight_id BIGINT NOT NULL,
    seat_class VARCHAR(20) NOT NULL,
    seat_count INT NOT NULL,
    booked_count INT NOT NULL DEFAULT 0,
    price DECIMAL(10,2) NOT NULL,
    version INT NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    UNIQUE KEY uk_flight_class (flight_id, seat_class),
    FOREIGN KEY (flight_id) REFERENCES atrs_flight(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Booking order table
CREATE TABLE IF NOT EXISTS atrs_booking_order (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_no VARCHAR(50) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    flight_id BIGINT NOT NULL,
    seat_class VARCHAR(20) NOT NULL,
    total_price DECIMAL(10,2) NOT NULL,
    passenger_count INT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    contact_name VARCHAR(50) NOT NULL,
    contact_phone VARCHAR(20) NOT NULL,
    contact_email VARCHAR(100),
    remark VARCHAR(500),
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    FOREIGN KEY (user_id) REFERENCES atrs_user(id),
    FOREIGN KEY (flight_id) REFERENCES atrs_flight(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Order passenger table
CREATE TABLE IF NOT EXISTS atrs_order_passenger (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    name VARCHAR(50) NOT NULL,
    id_card VARCHAR(18) NOT NULL,
    phone VARCHAR(20),
    ticket_no VARCHAR(50) UNIQUE,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    FOREIGN KEY (order_id) REFERENCES atrs_booking_order(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Payment record table
CREATE TABLE IF NOT EXISTS atrs_payment_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL UNIQUE,
    transaction_no VARCHAR(50) NOT NULL UNIQUE,
    payment_method VARCHAR(20) NOT NULL DEFAULT 'SIMULATED',
    amount DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'SUCCESS',
    paid_at DATETIME NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    FOREIGN KEY (order_id) REFERENCES atrs_booking_order(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

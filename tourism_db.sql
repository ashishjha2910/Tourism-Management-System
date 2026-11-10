CREATE DATABASE IF NOT EXISTS tourism_db
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE tourism_db;

CREATE TABLE IF NOT EXISTS users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  email VARCHAR(190) NOT NULL UNIQUE,
  password_hash VARCHAR(100) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS hotels (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(150) NOT NULL,
  destination VARCHAR(120) NOT NULL,
  available_rooms INT NOT NULL DEFAULT 0,
  price_per_night DECIMAL(10,2) NOT NULL DEFAULT 0,
  CHECK (available_rooms >= 0),
  CHECK (price_per_night >= 0)
);

CREATE TABLE IF NOT EXISTS bookings (
  id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  hotel_id INT NOT NULL,
  booked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  status ENUM('CONFIRMED','CANCELLED') NOT NULL DEFAULT 'CONFIRMED',
  CONSTRAINT fk_booking_user FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT fk_booking_hotel FOREIGN KEY (hotel_id) REFERENCES hotels(id)
);

INSERT INTO hotels(name,destination,available_rooms,price_per_night)
SELECT 'Lake View Resort','Udaipur',12,4500.00
WHERE NOT EXISTS (SELECT 1 FROM hotels WHERE name='Lake View Resort' AND destination='Udaipur');

INSERT INTO hotels(name,destination,available_rooms,price_per_night)
SELECT 'Mountain Escape','Manali',8,3200.00
WHERE NOT EXISTS (SELECT 1 FROM hotels WHERE name='Mountain Escape' AND destination='Manali');

INSERT INTO hotels(name,destination,available_rooms,price_per_night)
SELECT 'Royal Heritage Hotel','Jaipur',10,2800.00
WHERE NOT EXISTS (SELECT 1 FROM hotels WHERE name='Royal Heritage Hotel' AND destination='Jaipur');

INSERT INTO hotels(name,destination,available_rooms,price_per_night)
SELECT 'Coastal Breeze','Goa',15,3900.00
WHERE NOT EXISTS (SELECT 1 FROM hotels WHERE name='Coastal Breeze' AND destination='Goa');

INSERT INTO hotels(name,destination,available_rooms,price_per_night)
SELECT 'City Comfort Inn','Bhopal',6,1800.00
WHERE NOT EXISTS (SELECT 1 FROM hotels WHERE name='City Comfort Inn' AND destination='Bhopal');

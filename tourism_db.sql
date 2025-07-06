CREATE DATABASE IF NOT EXISTS tourism_db;
USE tourism_db;

CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(100) UNIQUE,
    password VARCHAR(100)
);

CREATE TABLE hotels (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100),
    destination VARCHAR(100),
    available_rooms INT
);

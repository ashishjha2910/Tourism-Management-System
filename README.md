# Tourism Management System

A Java-based desktop application for managing hotel discovery and room reservations. Built with Java Swing and MySQL, the application provides registration, authentication, hotel browsing, booking management and booking history.

## Overview

The Tourism Management System demonstrates a desktop-based hotel booking workflow. Users can create accounts, browse available hotels, reserve rooms, view their bookings and cancel active reservations.

This project is intended for learning and demonstrating Java desktop application development and database integration.

## Features

- User registration and login
- BCrypt password hashing
- Hotel and destination browsing
- Room availability and booking
- Personal booking history
- Booking cancellation and room inventory updates
- MySQL database integration using JDBC
- Prepared SQL statements for database operations

## Technology Stack

| Component | Technology |
|---|---|
| Language | Java 17 |
| Desktop UI | Java Swing |
| Database | MySQL 8+ |
| Database connectivity | JDBC |
| Password security | BCrypt |
| Build tool | Maven |
| Testing | JUnit 5 |

## Prerequisites

- JDK 17 or later
- Maven
- MySQL 8 or later

## Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/ashishjha2910/Tourism-Management-System.git
cd Tourism-Management-System
```

### 2. Create the database

Run the supplied SQL script:

```bash
mysql -u root -p < tourism_db.sql
```

The script creates the `tourism_db` database, its tables and sample hotel records.

### 3. Configure database credentials

Set the environment variables in the terminal where you launch the application:

```bash
export DB_URL='jdbc:mysql://localhost:3306/tourism_db?useSSL=false&serverTimezone=UTC'
export DB_USER='your_mysql_username'
export DB_PASSWORD='your_mysql_password'
```

Use a MySQL account with access to the application database. Keep credentials private and never commit real passwords.

### 4. Build and test

```bash
mvn clean test
```

To package the project:

```bash
mvn clean package
```

### 5. Run the application

```bash
mvn exec:java -Dexec.mainClass=com.tourism.Main
```

The application opens a Java Swing interface and requires a graphical desktop session.

## Application Workflow

1. Register a new user account.
2. Log in with the registered credentials.
3. Browse the available sample hotels.
4. Select a room and make a booking.
5. Review booking history.
6. Cancel an active booking when required.

## Project Structure

```text
Tourism-Management-System/
├── src/
│   ├── main/
│   │   └── java/com/tourism/
│   │       ├── Main.java
│   │       ├── Database.java
│   │       └── PasswordUtil.java
│   └── test/
│       └── java/com/tourism/
│           └── PasswordUtilTest.java
├── tourism_db.sql
├── pom.xml
├── .env.example
└── README.md
```

## Security and Database Practices

- Passwords are hashed using BCrypt.
- Prepared statements are used for database operations.
- Database credentials are configurable through environment variables.
- Do not store production credentials in source code.

## Limitations

- Hotel and destination information comes from sample database records.
- The application does not represent live hotel inventory.
- Bookings are local application records and are not real-world reservations.
- A graphical desktop environment is required to run the Swing interface.

## Author

**Ashish Kumar**

- GitHub: [ashishjha2910](https://github.com/ashishjha2910)

---

Feedback and suggestions are welcome through GitHub Issues.

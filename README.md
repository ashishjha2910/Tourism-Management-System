# Tourism Management System

A Java Swing desktop application backed by MySQL. It supports account registration and login, hotel browsing, room booking, booking history, and cancellation.

## Tech stack

- Java 17
- Java Swing
- MySQL 8+
- JDBC / MySQL Connector/J
- BCrypt password hashing
- Maven and JUnit 5

## Prerequisites

Install Java 17+, Maven, and MySQL 8+.

## Database setup

Run the supplied SQL script:

```bash
mysql -u root -p < tourism_db.sql
```

This creates the `tourism_db` database, tables, and sample hotel records. Run it once before starting the application.

## Configure database access

Set these environment variables in the terminal where you launch the app:

```bash
export DB_URL='jdbc:mysql://localhost:3306/tourism_db?useSSL=false&serverTimezone=UTC'
export DB_USER='root'
export DB_PASSWORD='your_mysql_password'
```

The application defaults to `root` with an empty password if `DB_PASSWORD` is not set. Prefer setting your own local credentials. Never commit real passwords.

## Build and test

```bash
mvn clean test package
```

## Run

```bash
mvn exec:java -Dexec.mainClass=com.tourism.Main
```

Or run the compiled classes with the Maven dependency classpath. The Swing interface requires a graphical desktop session.

## Main features

- Register and log in with BCrypt-hashed passwords
- Browse sample hotels and destinations
- Book an available room
- View personal booking history
- Cancel an active booking and return the room to inventory
- Prepared SQL statements for database operations

## Project structure

```text
src/
  main/java/com/tourism/
    Main.java
    Database.java
    PasswordUtil.java
  test/java/com/tourism/
    PasswordUtilTest.java
tourism_db.sql
pom.xml
```

## Notes

This is an educational desktop application. The included hotel records are sample data, not live inventory or real reservations.

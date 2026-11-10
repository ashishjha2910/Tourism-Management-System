package com.tourism;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class Database {
    private Database() {}

    private static final String URL = System.getenv().getOrDefault(
        "DB_URL", "jdbc:mysql://localhost:3306/tourism_db?useSSL=false&serverTimezone=UTC");
    private static final String USER = System.getenv().getOrDefault("DB_USER", "root");
    private static final String PASSWORD = System.getenv().getOrDefault("DB_PASSWORD", "");

    private static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public record Hotel(int id, String name, String destination, int rooms, double price) {}
    public record Booking(int id, String hotel, String destination, Timestamp bookedAt, String status) {}

    public static boolean register(String name, String email, String password) throws SQLException {
        String sql = "INSERT INTO users(name,email,password_hash) VALUES(?,?,?)";
        try (Connection c = connect(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, name.trim());
            p.setString(2, email.trim().toLowerCase());
            p.setString(3, PasswordUtil.hash(password));
            p.executeUpdate();
            return true;
        }
    }

    public static int login(String email, String password) throws SQLException {
        String sql = "SELECT id,password_hash FROM users WHERE email=?";
        try (Connection c = connect(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, email.trim().toLowerCase());
            try (ResultSet r = p.executeQuery()) {
                if (!r.next() || !PasswordUtil.verify(password, r.getString("password_hash"))) return -1;
                return r.getInt("id");
            }
        }
    }

    public static List<Hotel> hotels() throws SQLException {
        List<Hotel> result = new ArrayList<>();
        String sql = "SELECT id,name,destination,available_rooms,price_per_night FROM hotels ORDER BY destination,name";
        try (Connection c = connect(); PreparedStatement p = c.prepareStatement(sql);
             ResultSet r = p.executeQuery()) {
            while (r.next()) result.add(new Hotel(r.getInt("id"), r.getString("name"),
                r.getString("destination"), r.getInt("available_rooms"), r.getDouble("price_per_night")));
        }
        return result;
    }

    public static void book(int userId, int hotelId) throws SQLException {
        String sql = "INSERT INTO bookings(user_id,hotel_id,status) " +
                     "SELECT ?,id,'CONFIRMED' FROM hotels WHERE id=? AND available_rooms>0";
        try (Connection c = connect()) {
            c.setAutoCommit(false);
            try (PreparedStatement p = c.prepareStatement(sql)) {
                p.setInt(1, userId);
                p.setInt(2, hotelId);
                if (p.executeUpdate() != 1) throw new SQLException("Hotel is unavailable. Refresh the hotel list.");
            }
            try (PreparedStatement p = c.prepareStatement(
                    "UPDATE hotels SET available_rooms=available_rooms-1 WHERE id=? AND available_rooms>0")) {
                p.setInt(1, hotelId);
                if (p.executeUpdate() != 1) {
                    c.rollback();
                    throw new SQLException("No rooms are currently available.");
                }
            }
            c.commit();
        }
    }

    public static List<Booking> bookings(int userId) throws SQLException {
        List<Booking> result = new ArrayList<>();
        String sql = "SELECT b.id,h.name,h.destination,b.booked_at,b.status " +
                     "FROM bookings b JOIN hotels h ON h.id=b.hotel_id " +
                     "WHERE b.user_id=? ORDER BY b.booked_at DESC";
        try (Connection c = connect(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, userId);
            try (ResultSet r = p.executeQuery()) {
                while (r.next()) result.add(new Booking(r.getInt("id"), r.getString("name"),
                    r.getString("destination"), r.getTimestamp("booked_at"), r.getString("status")));
            }
        }
        return result;
    }

    public static void cancel(int userId, int bookingId) throws SQLException {
        try (Connection c = connect()) {
            c.setAutoCommit(false);
            int hotelId;
            try (PreparedStatement p = c.prepareStatement(
                    "SELECT hotel_id FROM bookings WHERE id=? AND user_id=? AND status='CONFIRMED' FOR UPDATE")) {
                p.setInt(1, bookingId);
                p.setInt(2, userId);
                try (ResultSet r = p.executeQuery()) {
                    if (!r.next()) throw new SQLException("No active booking found for this account.");
                    hotelId = r.getInt(1);
                }
            }
            try (PreparedStatement p = c.prepareStatement(
                    "UPDATE bookings SET status='CANCELLED' WHERE id=? AND user_id=?")) {
                p.setInt(1, bookingId);
                p.setInt(2, userId);
                p.executeUpdate();
            }
            try (PreparedStatement p = c.prepareStatement(
                    "UPDATE hotels SET available_rooms=available_rooms+1 WHERE id=?")) {
                p.setInt(1, hotelId);
                p.executeUpdate();
            }
            c.commit();
        }
    }
}

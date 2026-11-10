package com.tourism;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new LoginFrame().setVisible(true);
        });
    }
}

class LoginFrame extends JFrame {
    private final JTextField loginEmail = new JTextField(22);
    private final JPasswordField loginPassword = new JPasswordField(22);
    private final JTextField registerName = new JTextField(22);
    private final JTextField registerEmail = new JTextField(22);
    private final JPasswordField registerPassword = new JPasswordField(22);

    LoginFrame() {
        setTitle("Tourism Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(440, 340);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        JPanel login = new JPanel(new GridBagLayout());
        JPanel registration = new JPanel(new GridBagLayout());
        addField(login, 0, "Email", loginEmail);
        addField(login, 1, "Password", loginPassword);
        JButton loginButton = new JButton("Login");
        addField(login, 2, "", loginButton);
        loginButton.addActionListener(e -> login());

        addField(registration, 0, "Full name", registerName);
        addField(registration, 1, "Email", registerEmail);
        addField(registration, 2, "Password (8+ characters)", registerPassword);
        JButton registerButton = new JButton("Create account");
        addField(registration, 3, "", registerButton);
        registerButton.addActionListener(e -> register());

        tabs.addTab("Login", login);
        tabs.addTab("Register", registration);
        add(tabs);
    }

    private void addField(JPanel panel, int row, String label, JComponent field) {
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 10, 8, 10);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.gridx = 0; g.gridy = row; g.weightx = 0.3;
        panel.add(new JLabel(label), g);
        g.gridx = 1; g.weightx = 0.7;
        panel.add(field, g);
    }

    private void login() {
        try {
            int id = Database.login(loginEmail.getText(), new String(loginPassword.getPassword()));
            if (id < 0) {
                JOptionPane.showMessageDialog(this, "Invalid email or password.");
                return;
            }
            openDashboard(id);
        } catch (SQLException ex) { showError(ex); }
    }

    private void register() {
        String name = registerName.getText().trim();
        String email = registerEmail.getText().trim();
        String password = new String(registerPassword.getPassword());
        if (name.isEmpty() || email.isEmpty() || !email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            JOptionPane.showMessageDialog(this, "Enter a name and valid email address.");
            return;
        }
        try {
            Database.register(name, email, password);
            JOptionPane.showMessageDialog(this, "Account created. You can now log in.");
        } catch (SQLException ex) {
            if ("23000".equals(ex.getSQLState()))
                JOptionPane.showMessageDialog(this, "An account with this email already exists.");
            else showError(ex);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void openDashboard(int userId) {
        dispose();
        new DashboardFrame(userId).setVisible(true);
    }

    private void showError(Exception ex) {
        JOptionPane.showMessageDialog(this,
            ex.getMessage() + "\nCheck MySQL is running and DB_URL, DB_USER and DB_PASSWORD are configured.",
            "Application error", JOptionPane.ERROR_MESSAGE);
    }
}

class DashboardFrame extends JFrame {
    private final int userId;
    private final javax.swing.table.DefaultTableModel hotelModel =
        new javax.swing.table.DefaultTableModel(new Object[]{"ID","Hotel","Destination","Rooms","Price / night"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
    private final javax.swing.table.DefaultTableModel bookingModel =
        new javax.swing.table.DefaultTableModel(new Object[]{"Booking ID","Hotel","Destination","Booked at","Status"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
    private final JTable hotelTable = new JTable(hotelModel);
    private final JTable bookingTable = new JTable(bookingModel);

    DashboardFrame(int userId) {
        this.userId = userId;
        setTitle("Tourism Management System | Dashboard");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(850, 520);
        setLocationRelativeTo(null);

        JButton refresh = new JButton("Refresh hotels");
        JButton book = new JButton("Book selected hotel");
        JButton myBookings = new JButton("My bookings");
        JButton cancel = new JButton("Cancel selected booking");
        JButton logout = new JButton("Logout");
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        for (JButton b : new JButton[]{refresh, book, myBookings, cancel, logout}) actions.add(b);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Available hotels", new JScrollPane(hotelTable));
        tabs.addTab("My bookings", new JScrollPane(bookingTable));
        add(actions, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);

        refresh.addActionListener(e -> loadHotels());
        book.addActionListener(e -> bookHotel());
        myBookings.addActionListener(e -> loadBookings());
        cancel.addActionListener(e -> cancelBooking());
        logout.addActionListener(e -> { dispose(); new LoginFrame().setVisible(true); });
        loadHotels();
    }

    private void loadHotels() {
        try {
            hotelModel.setRowCount(0);
            for (Database.Hotel h : Database.hotels())
                hotelModel.addRow(new Object[]{h.id(), h.name(), h.destination(), h.rooms(),
                    String.format("₹%.2f", h.price())});
        } catch (SQLException ex) { error(ex); }
    }

    private void bookHotel() {
        int row = hotelTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a hotel first.");
            return;
        }
        int modelRow = hotelTable.convertRowIndexToModel(row);
        int hotelId = (int) hotelModel.getValueAt(modelRow, 0);
        try {
            Database.book(userId, hotelId);
            JOptionPane.showMessageDialog(this, "Booking confirmed.");
            loadHotels();
            loadBookings();
        } catch (SQLException ex) { error(ex); }
    }

    private void loadBookings() {
        try {
            bookingModel.setRowCount(0);
            for (Database.Booking b : Database.bookings(userId))
                bookingModel.addRow(new Object[]{b.id(), b.hotel(), b.destination(), b.bookedAt(), b.status()});
        } catch (SQLException ex) { error(ex); }
    }

    private void cancelBooking() {
        int row = bookingTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a booking first.");
            return;
        }
        int modelRow = bookingTable.convertRowIndexToModel(row);
        int bookingId = (int) bookingModel.getValueAt(modelRow, 0);
        if (JOptionPane.showConfirmDialog(this, "Cancel booking #" + bookingId + "?",
                "Confirm cancellation", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        try {
            Database.cancel(userId, bookingId);
            JOptionPane.showMessageDialog(this, "Booking cancelled.");
            loadBookings();
            loadHotels();
        } catch (SQLException ex) { error(ex); }
    }

    private void error(Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Database error", JOptionPane.ERROR_MESSAGE);
    }
}

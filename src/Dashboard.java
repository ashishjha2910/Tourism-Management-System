import javax.swing.*;

public class Dashboard extends JFrame {
    public Dashboard() {
        setTitle("Tourism Dashboard");
        setSize(400, 300);
        setLayout(null);

        JLabel label = new JLabel("Welcome to the Tourism Booking System");
        label.setBounds(50, 100, 300, 30);
        add(label);

        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}

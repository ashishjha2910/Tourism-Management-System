import javax.swing.*;
import java.awt.event.*;

public class LoginForm extends JFrame {
    JTextField emailField;
    JPasswordField passwordField;

    public LoginForm() {
        setTitle("Tourist Login");
        setSize(300, 200);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel userLabel = new JLabel("Email:");
        userLabel.setBounds(20, 30, 80, 25);
        add(userLabel);

        emailField = new JTextField();
        emailField.setBounds(100, 30, 160, 25);
        add(emailField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(20, 65, 80, 25);
        add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(100, 65, 160, 25);
        add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(100, 100, 80, 25);
        add(loginButton);

        loginButton.addActionListener(e -> {
            // Call DB to verify
            new Dashboard();
            dispose();
        });

        setVisible(true);
    }
}

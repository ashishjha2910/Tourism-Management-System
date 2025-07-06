import java.sql.*;

public class DatabaseConnection {
    public static Connection getConnection() throws Exception {
        String url = "jdbc:mysql://localhost:3306/tourism_db";
        String user = "root";
        String password = "your_password";
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url, user, password);
    }
}

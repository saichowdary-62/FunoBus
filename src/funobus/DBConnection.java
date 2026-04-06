package funobus;

import java.sql.*;

public class DBConnection {
    private static final String PASSWORD = "Saiamar@49";
    private static final String URL = "jdbc:mysql://localhost:3306/funobus_db?useSSL=false&serverTimezone=Asia/Kolkata";
    private static final String USERNAME = "root";
    
    private static Connection connection = null;
    
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ MySQL Driver loaded!");
        } catch (ClassNotFoundException e) {
            System.out.println("❌ MySQL Driver not found!");
            e.printStackTrace();
        }
    }
    
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("✅ Database connected!");
            }
            return connection;
        } catch (SQLException e) {
            System.out.println("❌ Connection failed!");
            e.printStackTrace();
            return null;
        }
    }
}
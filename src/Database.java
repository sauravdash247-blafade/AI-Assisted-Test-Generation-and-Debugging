import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    public static Connection connect() throws SQLException {
        // String url = "jdbc:mysql://localhost:3306/library_db";
        String url = "jdbc:mysql://localhost:3306/library_db";

        String user = "root"; // Replace with your MySQL username
        String password = "Sahithi@2005"; // Replace with your MySQL password

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
        }

        return DriverManager.getConnection(url, user, password);
    }

    public static void main(String[] args) {
        try (Connection conn = connect()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println(" Database connection successful.");
            } else {
                System.out.println(" Failed to connect to the database.");
            }
        } catch (SQLException e) {
            System.out.println(" SQL Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

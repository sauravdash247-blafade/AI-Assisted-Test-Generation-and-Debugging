
import java.sql.*;
import java.time.LocalDate;

public class DatabaseHelper {

    // Method to add a user to the database
    public static void addUser(String username, boolean isAdmin) throws SQLException {
        try (Connection conn = Database.connect()) {
            String query = "INSERT INTO users (username, is_admin) VALUES (?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setString(1, username);
                ps.setBoolean(2, isAdmin);
                ps.executeUpdate();
            }
        }
    }

    // Method to remove a user from the database
    public static void removeUser(String username) throws SQLException {
        try (Connection conn = Database.connect()) {
            String query = "DELETE FROM users WHERE username = ?";
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setString(1, username);
                ps.executeUpdate();
            }
        }
    }

    // Method to add a book to the database
    public static void addBook(String title, String author, double price) throws SQLException {
        try (Connection conn = Database.connect()) {
            String query = "INSERT INTO books (title, author, price) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setString(1, title);
                ps.setString(2, author);
                ps.setDouble(3, price);
                ps.executeUpdate();
            }
        }
    }

    // Method to remove a book from the database
    public static void removeBook(int bookId) throws SQLException {
        try (Connection conn = Database.connect()) {
            String query = "DELETE FROM books WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setInt(1, bookId);
                ps.executeUpdate();
            }
        }
    }

    // Method to get the list of books from the database
    public static ResultSet getAllBooks() throws SQLException {
        Connection conn = Database.connect();
        String query = "SELECT * FROM books";
        Statement stmt = conn.createStatement();
        return stmt.executeQuery(query);
    }

    // Method to check if a book is already borrowed by a user
    public static boolean isBookBorrowed(int bookId, String username) throws SQLException {
        try (Connection conn = Database.connect()) {
            String query = "SELECT COUNT(*) FROM borrowed_books WHERE book_id = ? AND user_id = (SELECT id FROM users WHERE username = ?)";
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setInt(1, bookId);
                ps.setString(2, username);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Returns true if the book is borrowed
                }
            }
        }
        return false;
    }

    // Method to borrow a book for a user
    public static void borrowBook(int bookId, String username) throws SQLException {
        try (Connection conn = Database.connect()) {
            String query = "INSERT INTO borrowed_books (user_id, book_id, return_date) VALUES ((SELECT id FROM users WHERE username = ?), ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setString(1, username);
                ps.setInt(2, bookId);
                ps.setDate(3, Date.valueOf(LocalDate.now().plusDays(14))); // Default return date in 14 days
                ps.executeUpdate();
            }
        }
    }

    // Method to return a book
    public static void returnBook(int bookId, String username) throws SQLException {
        try (Connection conn = Database.connect()) {
            String query = "DELETE FROM borrowed_books WHERE user_id = (SELECT id FROM users WHERE username = ?) AND book_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setString(1, username);
                ps.setInt(2, bookId);
                ps.executeUpdate();
            }
        }
    }

    // New method to get the due date of a borrowed book
    public static LocalDate getDueDate(int bookId, String username) throws SQLException {
        try (Connection conn = Database.connect()) {
            String query = "SELECT return_date FROM borrowed_books WHERE book_id = ? AND user_id = (SELECT id FROM users WHERE username = ?)";
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setInt(1, bookId);
                ps.setString(2, username);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getDate("return_date").toLocalDate();
                }
            }
        }
        return null; // Return null if the book is not borrowed by the user
    }
}
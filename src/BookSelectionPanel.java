import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class BookSelectionPanel extends JFrame {
    private String action;
    private String currentUser;
    private DefaultListModel<String> bookModel = new DefaultListModel<>();
    private JList<String> bookList = new JList<>(bookModel);

    public BookSelectionPanel(String action, String currentUser) {
        this.action = action;
        this.currentUser = currentUser;

        setTitle(action + " Books");
        setSize(400, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel options = new JPanel(new GridLayout(1, 1));
        JButton selectBtn = new JButton("Select Book");

        options.add(selectBtn);
        add(options, BorderLayout.NORTH);

        add(new JScrollPane(bookList), BorderLayout.CENTER);

        refreshBookList();

        selectBtn.addActionListener(e -> {
            String selection = bookList.getSelectedValue();
            if (selection != null) {
                int id = Integer.parseInt(selection.split(" - ")[0]);
                if ("Borrow".equals(action)) {
                    borrowBook(id);
                } else if ("Buy".equals(action)) {
                    buyBook(id);
                }
            }
        });

        setVisible(true);
    }

    private void refreshBookList() {
        bookModel.clear();
        try (Connection conn = Database.connect()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM books");
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                double price = rs.getDouble("price");
                bookModel.addElement(id + " - " + title + " by " + author + " [$" + price + "]");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void borrowBook(int bookId) {
        try (Connection conn = Database.connect()) {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO borrowed_books (user_id, book_id, return_date) VALUES ((SELECT id FROM users WHERE username=?), ?, ?)");
            ps.setString(1, currentUser);
            ps.setInt(2, bookId);
            ps.setDate(3, java.sql.Date.valueOf(java.time.LocalDate.now().plusDays(14)));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Book borrowed. Return by: " + java.time.LocalDate.now().plusDays(14));
            dispose();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void buyBook(int bookId) {
        try (Connection conn = Database.connect()) {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO purchases (user_id, book_id) VALUES ((SELECT id FROM users WHERE username=?), ?)");
            ps.setString(1, currentUser);
            ps.setInt(2, bookId);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Book added to cart.");
            dispose();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}

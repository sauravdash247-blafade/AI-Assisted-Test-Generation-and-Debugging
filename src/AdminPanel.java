
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.geom.RoundRectangle2D;

public class AdminPanel extends JFrame {
    private DefaultListModel<String> userModel = new DefaultListModel<>();
    private JList<String> userList = new JList<>(userModel);
    private DefaultListModel<String> bookModel = new DefaultListModel<>();
    private JList<String> bookList = new JList<>(bookModel);

    // In-memory data storage
    private List<User> users = new ArrayList<>();
    private List<Book> books = new ArrayList<>();

    private BufferedImage backgroundImage; // Background image
    private static final Color LIGHT_BLUE = new Color(220, 220, 220); // Light blue for lists
    private static final Color DARKER_BLUE = new Color(176, 196, 222);
    private static final Font LIST_FONT = new Font("Arial", Font.BOLD, 16);
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 18);

    public AdminPanel() {
        setTitle("Admin Panel");
        setSize(800, 600); // Increased size for better layout
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Load background image
        try {
            // Replace with the actual path to your image file
            backgroundImage = ImageIO.read(new File("admin_background.jpg")); // Ensure the path is correct
        } catch (IOException e) {
            e.printStackTrace();
            // If the image fails to load, the background will be default gray
            System.out.println(
                    "Failed to load background image.  Make sure admin_background.jpg is in the project root.");
        }

        JPanel options = new JPanel(new GridLayout(3, 2));
        options.setOpaque(false); // Make the options panel transparent
        JButton addUserBtn = createStyledButton("Add User");
        JButton removeUserBtn = createStyledButton("Remove User");
        JButton addBookBtn = createStyledButton("Add Book");
        JButton removeBookBtn = createStyledButton("Remove Book");
        JButton logoutBtn = createStyledButton("Logout");

        options.add(addUserBtn);
        options.add(removeUserBtn);
        options.add(addBookBtn);
        options.add(removeBookBtn);
        options.add(logoutBtn);
        add(options, BorderLayout.NORTH);

        // Use a JTabbedPane to organize user and book lists
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setOpaque(false);
        tabbedPane.setForeground(Color.BLACK);
        tabbedPane.setFont(BUTTON_FONT);

        // create a JPanel for the userList and bookList
        JPanel userPanel = new JPanel();
        userPanel.setOpaque(false);
        userPanel.setLayout(new BorderLayout());
        userList.setOpaque(false);
        userList.setForeground(Color.BLACK);
        userList.setFont(LIST_FONT);
        userPanel.add(new JScrollPane(userList), BorderLayout.CENTER);

        JPanel bookPanel = new JPanel();
        bookPanel.setOpaque(false);
        bookPanel.setLayout(new BorderLayout());
        bookList.setOpaque(false);
        bookList.setForeground(Color.BLACK);
        bookList.setFont(LIST_FONT);
        bookPanel.add(new JScrollPane(bookList), BorderLayout.CENTER);

        tabbedPane.addTab("Users", userPanel);
        tabbedPane.addTab("Books", bookPanel);
        add(tabbedPane, BorderLayout.CENTER);

        addUserBtn.addActionListener(e -> addUser());
        removeUserBtn.addActionListener(e -> removeUser());
        addBookBtn.addActionListener(e -> addBook());
        removeBookBtn.addActionListener(e -> removeBook());
        logoutBtn.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new LoginPanel());
        });

        refreshUserList();
        refreshBookList();
        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setForeground(Color.BLACK);
        button.setBackground(LIGHT_BLUE); // Light blue background
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding
        button.setFocusPainted(false);
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(DARKER_BLUE); // Darker blue on hover
                button.setForeground(Color.WHITE);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(LIGHT_BLUE); // Return to light blue
                button.setForeground(Color.BLACK);
            }
        });
        return button;
    }

    private void addUser() {
        String username = JOptionPane.showInputDialog("Enter Username:");
        if (username == null || username.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        boolean isAdmin = JOptionPane.showConfirmDialog(this, "Is this user an Admin?") == JOptionPane.YES_OPTION;

        // Check if user already exists
        for (User user : users) {
            if (user.username.equals(username)) {
                JOptionPane.showMessageDialog(this, "User already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        users.add(new User(username, isAdmin));
        JOptionPane.showMessageDialog(this, "User added.");
        refreshUserList();
    }

    private void removeUser() {
        String usernameToRemove = JOptionPane.showInputDialog("Enter username of user to remove:");
        if (usernameToRemove == null || usernameToRemove.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No username entered.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).username.equals(usernameToRemove)) {
                users.remove(i);
                JOptionPane.showMessageDialog(this, "User removed.");
                refreshUserList();
                return; // Important: Exit the method after removing the user
            }
        }
        JOptionPane.showMessageDialog(this, "User not found.", "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void addBook() {
        String title = JOptionPane.showInputDialog("Enter Book Title:");
        String author = JOptionPane.showInputDialog("Enter Book Author:");
        String priceStr = JOptionPane.showInputDialog("Enter Book Price:");

        if (title == null || title.trim().isEmpty() || author == null || author.trim().isEmpty() || priceStr == null
                || priceStr.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields (Title, Author, Price) are required.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double price = Double.parseDouble(priceStr);
            if (price < 0) {
                JOptionPane.showMessageDialog(this, "Price must be a non-negative number.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            for (Book book : books) {
                if (book.title.equals(title)) {
                    JOptionPane.showMessageDialog(this, "Book with this title already exists.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            books.add(new Book(title, author, price));
            JOptionPane.showMessageDialog(this, "Book added.");
            refreshBookList();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid price format. Please enter a valid number.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeBook() {
        String bookTitleToRemove = JOptionPane.showInputDialog("Enter title of the book to remove:");
        if (bookTitleToRemove == null || bookTitleToRemove.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No book title entered.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).title.equals(bookTitleToRemove)) {
                books.remove(i);
                JOptionPane.showMessageDialog(this, "Book removed.");
                refreshBookList();
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Book not found.", "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void refreshUserList() {
        userModel.clear();
        for (User user : users) {
            userModel.addElement(user.username + (user.isAdmin ? " (Admin)" : " (User)"));
        }
    }

    private void refreshBookList() {
        bookModel.clear();
        for (Book book : books) {
            bookModel.addElement(book.title + " by " + book.author + " - $" + book.price);
        }
    }

    // Inner class for User
    private static class User {
        String username;
        boolean isAdmin;

        public User(String username, boolean isAdmin) {
            this.username = username;
            this.isAdmin = isAdmin;
        }
    }

    // Inner class for Book
    private static class Book {
        String title;
        String author;
        double price;

        public Book(String title, String author, double price) {
            this.title = title;
            this.author = author;
            this.price = price;
        }
    }
}

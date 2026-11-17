
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class UserPanel extends JFrame {
    public String currentUser;
    public DefaultListModel<String> bookModel = new DefaultListModel<>();
    public JList<String> bookList = new JList<>(bookModel);
    public ArrayList<Integer> cart = new ArrayList<>();

    public Map<Integer, Book> books = new HashMap<>();
    public Map<Integer, Booklist> booklists = new HashMap<>();
    public Map<Integer, LocalDate> borrowedBooks = new HashMap<>();
    public int nextBookId = 1;
    public int nextBooklistId = 1;

    private BufferedImage backgroundImage;
    private static final Color LIGHT_BLUE = new Color(220, 220, 220);
    private static final Color DARKER_BLUE = new Color(176, 196, 222);
    private static final Font LIST_FONT = new Font("Arial", Font.BOLD, 16);
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 18);

    public static class Book {
        public String title;
        public String author;
        public double price;

        public Book(String title, String author, double price) {
            this.title = title;
            this.author = author;
            this.price = price;
        }

        @Override
        public String toString() {
            return title + " by " + author + " [$" + price + "]";
        }
    }

    public static class Booklist {
        public String name;
        public boolean isPublic;
        public ArrayList<Integer> bookIds = new ArrayList<>();

        public Booklist(String name, boolean isPublic) {
            this.name = name;
            this.isPublic = isPublic;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public UserPanel(String currentUser) {
        this.currentUser = currentUser;

        setTitle("User Panel");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Load background image
        try {
            backgroundImage = ImageIO.read(new File(
                    "C:\\Users\\91960\\OneDrive\\Documents\\CSE-SEM-4\\library\\LIbraryManagementSystem\\resources\\user_background.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out
                    .println("Failed to load background image.  Make sure user_background.jpg is in the project root.");
        }

        JPanel options = new JPanel(new GridLayout(3, 2));
        options.setOpaque(false);
        JButton borrowBtn = createStyledButton("Borrow Book");
        JButton returnBtn = createStyledButton("Return Book");
        JButton buyBtn = createStyledButton("Buy Book");
        JButton booklistsBtn = createStyledButton("View Booklists");
        JButton logoutBtn = createStyledButton("Logout");

        options.add(borrowBtn);
        options.add(returnBtn);
        options.add(buyBtn);
        options.add(booklistsBtn);
        options.add(new JLabel());
        options.add(logoutBtn);
        add(options, BorderLayout.NORTH);

        // Make the book list panel
        JPanel bookPanel = new JPanel() { // Anonymous subclass to override paintComponent
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(new Color(240, 240, 240));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        bookPanel.setOpaque(false);
        bookPanel.setLayout(new BorderLayout());
        bookList.setOpaque(false);
        bookList.setForeground(Color.BLACK);
        bookList.setFont(LIST_FONT);
        bookPanel.add(new JScrollPane(bookList), BorderLayout.CENTER);
        add(bookPanel, BorderLayout.CENTER);

        initializeData();
        refreshBookList();

        borrowBtn.addActionListener(e -> borrowBook());
        returnBtn.addActionListener(e -> returnBook());
        buyBtn.addActionListener(e -> buyBook());
        booklistsBtn.addActionListener(e -> viewBooklists());
        logoutBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserPanel.this.dispose();
                new LoginPanel();
            }
        });

        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setForeground(Color.BLACK);
        button.setBackground(LIGHT_BLUE);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(DARKER_BLUE);
                button.setForeground(Color.WHITE);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(LIGHT_BLUE);
                button.setForeground(Color.BLACK);
            }
        });
        return button;
    }

    public void initializeData() {
        addBook("The Lord of the Rings", "J.R.R. Tolkien", 29.99);
        addBook("Pride and Prejudice", "Jane Austen", 12.50);
        addBook("To Kill a Mockingbird", "Harper Lee", 15.75);
        addBook("1984", "George Orwell", 18.00);
        addBook("The Great Gatsby", "F. Scott Fitzgerald", 11.20);
        addBook("Moby Dick", "Herman Melville", 22.00);
        addBook("One Hundred Years of Solitude", "Gabriel Garcia Marquez", 19.50);

        addBooklist("Fantasy Favorites", true);
        addBooklist("Classic Novels", true);
        addBooklist("Staff Picks", false);
        addBooklist("Bestsellers", true);

        addBookToBooklist(1, 1);
        addBookToBooklist(1, 4);
        addBookToBooklist(2, 2);
        addBookToBooklist(2, 5);
        addBookToBooklist(3, 1);
        addBookToBooklist(3, 3);
        addBookToBooklist(4, 1);
        addBookToBooklist(4, 6);
        addBookToBooklist(4, 7);
    }

    public void addBook(String title, String author, double price) {
        Book book = new Book(title, author, price);
        books.put(nextBookId, book);
        nextBookId++;
    }

    public void addBooklist(String name, boolean isPublic) {
        Booklist booklist = new Booklist(name, isPublic);
        booklists.put(nextBooklistId, booklist);
        nextBooklistId++;
    }

    public void addBookToBooklist(int booklistId, int bookId) {
        Booklist booklist = booklists.get(booklistId);
        if (booklist != null) {
            booklist.bookIds.add(bookId);
        }
    }

    public void refreshBookList() {
        bookModel.clear();
        for (Map.Entry<Integer, Book> entry : books.entrySet()) {
            int id = entry.getKey();
            Book book = entry.getValue();
            String availability = borrowedBooks.containsKey(id) ? " [BORROWED]" : " [AVAILABLE]";
            bookModel.addElement(id + " - " + book + availability);
        }
    }

    public void borrowBook() {
        String selection = bookList.getSelectedValue();
        if (selection == null || selection.contains("[BORROWED]")) {
            JOptionPane.showMessageDialog(this, "Please select an available book to borrow.");
            return;
        }
        int id = Integer.parseInt(selection.split(" - ")[0]);
        borrowedBooks.put(id, LocalDate.now().plusDays(14));
        JOptionPane.showMessageDialog(this, "Book borrowed. Return by: " + LocalDate.now().plusDays(14));
        refreshBookList();
    }

    public void returnBook() {
        String bookIdStr = JOptionPane.showInputDialog("Enter Book ID to return:");
        if (bookIdStr == null || bookIdStr.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Book ID cannot be empty.");
            return;
        }
        try {
            int bookId = Integer.parseInt(bookIdStr);
            if (borrowedBooks.containsKey(bookId)) {
                LocalDate dueDate = borrowedBooks.get(bookId);
                JOptionPane.showMessageDialog(this, "Due Date: " + dueDate);
                borrowedBooks.remove(bookId);
                JOptionPane.showMessageDialog(this, "Book returned successfully.");
                refreshBookList();
            } else {
                JOptionPane.showMessageDialog(this, "Book not found in your borrowed list.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Book ID.");
        }
    }

    public void buyBook() {
        String selection = bookList.getSelectedValue();
        if (selection == null)
            return;
        int id = Integer.parseInt(selection.split(" - ")[0]);
        cart.add(id);
        JOptionPane.showMessageDialog(this, "Book added to cart.");
    }

    public void viewBooklists() {
        DefaultListModel<String> booklistModel = new DefaultListModel<>();
        JList<String> booklistDisplay = new JList<>(booklistModel);
        JScrollPane scrollPane = new JScrollPane(booklistDisplay);
        booklistDisplay.setFont(LIST_FONT);

        for (Map.Entry<Integer, Booklist> entry : booklists.entrySet()) {
            int id = entry.getKey();
            Booklist booklist = entry.getValue();
            if (booklist.isPublic) {
                booklistModel.addElement("ID: " + id + ", Name: " + booklist.name);
            }
        }

        if (booklistModel.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No public booklists available.", "Information",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this, scrollPane, "Public Booklists", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (choice == JOptionPane.OK_OPTION) {
            String selectedBooklist = booklistDisplay.getSelectedValue();
            if (selectedBooklist != null) {
                int booklistId = Integer.parseInt(selectedBooklist.split(",")[0].split(": ")[1].trim());
                openBooklistDetails(booklistId);
            }
        }
    }

    public void openBooklistDetails(int booklistId) {
        DefaultListModel<String> bookDetailsModel = new DefaultListModel<>();
        JList<String> bookDetailsList = new JList<>(bookDetailsModel);
        JScrollPane detailsScrollPane = new JScrollPane(bookDetailsList);
        bookDetailsList.setFont(LIST_FONT);

        Booklist booklist = booklists.get(booklistId);
        if (booklist != null) {
            for (int bookId : booklist.bookIds) {
                Book book = books.get(bookId);
                if (book != null) {
                    bookDetailsModel.addElement(book.toString());
                }
            }
        }

        if (bookDetailsModel.isEmpty()) {
            JOptionPane.showMessageDialog(this, "This booklist is empty.", "Information",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, detailsScrollPane, "Booklist Details", JOptionPane.PLAIN_MESSAGE);
    }
}

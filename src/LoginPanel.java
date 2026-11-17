import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginPanel extends JFrame {
    private JTextField usernameField; // Add a field for username input

    public LoginPanel() {
        setTitle("Library Login");
        setSize(400, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 2));

        JLabel roleLabel = new JLabel("Login as:");
        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(); // Initialize the username field
        JButton adminBtn = new JButton("Admin");
        JButton userBtn = new JButton("User");

        add(usernameLabel);
        add(usernameField);
        add(roleLabel);
        add(new JLabel()); // Empty cell
        add(adminBtn);
        add(userBtn);

        adminBtn.addActionListener(e -> {
            dispose();
            new AdminLogin(); // another class you should create
        });

        userBtn.addActionListener(e -> {
            String username = usernameField.getText(); // Get the username from the text field
            if (!username.isEmpty()) {
                dispose();
                new UserPanel(username); // Pass the username to the UserPanel constructor
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a username.");
            }
        });

        setVisible(true);
    }
}

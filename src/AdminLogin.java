
// AdminLogin.java
import javax.swing.*;
import java.awt.*;

public class AdminLogin extends JFrame {
    public AdminLogin() {
        setTitle("Admin Login");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 1));
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton loginBtn = new JButton("Login");

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(loginBtn);

        add(panel);

        loginBtn.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.equals("admin") && password.equals("admin123")) {
                dispose();
                SwingUtilities.invokeLater(() -> new AdminPanel());
            } else {
                JOptionPane.showMessageDialog(this, "Invalid admin credentials");
            }
        });

        setVisible(true);
    }
}

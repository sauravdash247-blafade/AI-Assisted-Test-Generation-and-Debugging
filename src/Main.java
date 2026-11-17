
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Library Management System");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(3, 1));

        JButton adminButton = new JButton("Admin Login");
        JButton userButton = new JButton("User Login");

        panel.add(adminButton);
        panel.add(userButton);

        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);

        // Admin button logic
        adminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String password = JOptionPane.showInputDialog(frame, "Enter Admin Password:");
                if ("admin123".equals(password)) {
                    new AdminPanel(); // Admin panel code should be implemented
                    frame.dispose();
                } else {
                    JOptionPane.showMessageDialog(frame, "Incorrect Password");
                }
            }
        });

        // User button logic
        userButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Prompt user for username
                String username = JOptionPane.showInputDialog(frame, "Enter Username:");
                if (username != null && !username.trim().isEmpty()) {
                    new UserPanel(username); // Pass the username to the UserPanel constructor
                    frame.dispose();
                } else {
                    JOptionPane.showMessageDialog(frame, "Username cannot be empty");
                }
            }
        });
    }
}

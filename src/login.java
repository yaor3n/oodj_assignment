import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class login extends JFrame implements ActionListener {
    private JLabel titleLabel, usernameLabel, passwordLabel;
    private JTextField usernameInput;
    private JPasswordField passwordInput;
    private JButton loginButton, backButton;

    public login() {
        // Window setup
        setTitle("University Login");
        setLayout(null);

        titleLabel = new JLabel("Login Page");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 25));
        titleLabel.setBounds(400, 50, 300, 50);
        this.add(titleLabel);

        usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 25));
        usernameLabel.setBounds(300, 150, 200, 50);
        this.add(usernameLabel);

        usernameInput = new JTextField();
        usernameInput.setFont(new Font("Arial", Font.PLAIN, 25));
        usernameInput.setBounds(300, 200, 400, 55);
        this.add(usernameInput);

        passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 25));
        passwordLabel.setBounds(300, 260, 200, 50);
        this.add(passwordLabel);

        passwordInput = new JPasswordField();
        passwordInput.setFont(new Font("Arial", Font.PLAIN, 25));
        passwordInput.setBounds(300, 310, 400, 55);
        this.add(passwordInput);

        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 25));
        loginButton.setBounds(420, 440, 200, 55);
        loginButton.addActionListener(this);
        this.add(loginButton);

        backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 25));
        backButton.setBounds(420, 510, 200, 55);
        backButton.addActionListener(this);
        this.add(backButton);

        reusable.windowSetup(this);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String username = usernameInput.getText().trim();
            String password = new String(passwordInput.getPassword()).trim();

            // OOP: Call the handler to find the user in the 9-column CSV
            User user = AccountFileHandler.authenticate(username, password);

            if (user != null) {
                Session.currentUsername = user.getUsername();
                String role = user.getRole();

                // Redirect based on role in index 7 of accounts.txt
                switch (role) {
                    case "Admin":
                        new adminDashboard();
                        break;
                    case "Lecturer":
                        new lecturerDashboard();
                        break;
                    case "Student":
                        new studentDashboard();
                        break;
                    case "AcademicLeader": // Matches U004 role in your file
                        new academicStaffDashboard();
                        break;
                    default:
                        JOptionPane.showMessageDialog(this, "Unknown Role: " + role);
                        return;
                }
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials or incomplete account data!");
            }
        } else if (e.getSource() == backButton) {
            new userSelect();
            this.dispose();
        }
    }
}
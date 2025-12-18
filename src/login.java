import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class login extends JFrame implements ActionListener {
    private JLabel titleLabel, usernameLabel, passwordLabel;
    private JTextField usernameInput;
    private JPasswordField passwordInput;
    private JButton loginButton, backButton;

    login() {

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
        backButton.addActionListener(this);
        backButton.setBounds(420, 510, 200, 55);
        this.add(backButton);

        reusable.windowSetup(this);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String username = usernameInput.getText();
            String password = new String(passwordInput.getPassword());
            boolean found = false;

            try (BufferedReader br = new BufferedReader(new FileReader("accounts.txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 3) {
                        String fileUsername = parts[0].trim();
                        String filePassword = parts[1].trim();
                        String role = parts[2].trim();

                        if (username.equals(fileUsername) && password.equals(filePassword)) {
                            found = true;

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
                                case "AcademicStaff":
                                    new academicLeaderDashboard();
                                    break;
                                default:
                                    JOptionPane.showMessageDialog(this,
                                            "Unknown role in account file!",
                                            "Error",
                                            JOptionPane.ERROR_MESSAGE);
                                    return;
                            }

                            this.dispose();
                            return;
                        }
                    }
                }

                if (!found) {
                    JOptionPane.showMessageDialog(this,
                            "Invalid username or password!",
                            "Login Failed",
                            JOptionPane.WARNING_MESSAGE);
                }

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error reading account file!",
                        "File Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

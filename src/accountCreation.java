import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class accountCreation extends JFrame implements ActionListener {
    public static void main(String[] args) {
        new accountCreation();
    }

    private JLabel welcomeLabel, usernameLabel, passwordLabel, userTypeLabel;
    private JButton createButton, backButton;
    private JTextField usernameInput;
    private JPasswordField passwordInput;
    private JRadioButton lecRadioButton, studRadioButton;
    private ButtonGroup userTypeGroup;

    accountCreation() {

        welcomeLabel = new JLabel("Account creation page");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 25));
        welcomeLabel.setBounds(400, 50, 300, 55);
        this.add(welcomeLabel);

        usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 25));
        usernameLabel.setBounds(150, 150, 300, 55);
        this.add(usernameLabel);

        usernameInput = new JTextField();
        usernameInput.setFont(new Font("Arial", Font.BOLD, 25));
        usernameInput.setBounds(150, 220, 350, 55);
        this.add(usernameInput);

        passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 25));
        passwordLabel.setBounds(150, 290, 300, 50);
        this.add(passwordLabel);

        passwordInput = new JPasswordField();
        passwordInput.setFont(new Font("Arial", Font.BOLD, 25));
        passwordInput.setBounds(150, 360, 350, 55);
        this.add(passwordInput);

        userTypeLabel = new JLabel("Select User Type:");
        userTypeLabel.setFont(new Font("Arial", Font.BOLD, 22));
        userTypeLabel.setBounds(600, 150, 250, 40);
        this.add(userTypeLabel);

        lecRadioButton = new JRadioButton("Lecturer");
        lecRadioButton.setFont(new Font("Arial", Font.PLAIN, 20));
        lecRadioButton.setBounds(600, 200, 200, 40);
        this.add(lecRadioButton);

        studRadioButton = new JRadioButton("Student");
        studRadioButton.setFont(new Font("Arial", Font.PLAIN, 20));
        studRadioButton.setBounds(600, 250, 200, 40);
        this.add(studRadioButton);

        userTypeGroup = new ButtonGroup();
        userTypeGroup.add(lecRadioButton);
        userTypeGroup.add(studRadioButton);

        createButton = new JButton("Submit");
        createButton.setFont(new Font("Arial", Font.BOLD, 25));
        createButton.addActionListener(this);
        createButton.setBounds(420, 440, 200, 60);
        this.add(createButton);

        backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 25));
        backButton.addActionListener(this);
        backButton.setBounds(420, 510, 200, 60);
        this.add(backButton);

        reusable.windowSetup(this);
        this.setVisible(true);
    }

    private void saveAccountToFile(String username, String password, String role) {
        try {
            FileWriter writer = new FileWriter("accounts.txt", true); // append mode
            writer.write(username + "," + password + "," + role + "\n");
            writer.close();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error saving account!",
                    "File Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean usernameExists(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader("accounts.txt"))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length >= 1 && parts[0].equals(username)) {
                    return true; // true if username already exists
                }
            }
        } catch (FileNotFoundException e) {
            return false;

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error reading account file!",
                    "File Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        return false;
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == backButton) {
            new userSelect();
            this.dispose();
        }

        if (e.getSource() == createButton) {
            String username = usernameInput.getText();
            String password = new String(passwordInput.getPassword());
            String userType = lecRadioButton.isSelected() ? "Lecturer" :
                    studRadioButton.isSelected() ? "Student" : "None";

            if (username.isEmpty() || password.isEmpty() || userType.equals("None")) {
                JOptionPane.showMessageDialog(this,
                        "Please fill all fields!",
                        "Input Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (usernameExists(username)) {
                JOptionPane.showMessageDialog(this,
                        "Username already exists!\nPlease choose another.",
                        "Duplicate Username",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            saveAccountToFile(username, password, userType);

            JOptionPane.showMessageDialog(this,
                    "Account Created Successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            new userSelect();
            this.dispose();

            usernameInput.setText("");
            passwordInput.setText("");
            userTypeGroup.clearSelection();
        }
    }
}

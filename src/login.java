import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class login extends JFrame implements ActionListener {
    public static void main(String[] args) {
        new login();
    }

    private JLabel welcomeLabel, usernameLabel, passwordLabel;
    private JButton submitButton;
    private JTextField usernameInput;
    private JPasswordField passwordInput;

    login() {
        welcomeLabel = new JLabel("Enter credentials to login");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 25));
        welcomeLabel.setBounds(400,50,300,55);
        welcomeLabel.setForeground(new Color(0x000000));
        this.add(welcomeLabel);

        usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 25));
        usernameLabel.setBounds(300,150,300,55);
        usernameLabel.setForeground(new Color(0x000000));
        this.add(usernameLabel);

        usernameInput = new JTextField();
        usernameInput.setFont(new Font("Arial", Font.BOLD, 25));
        usernameInput.setBounds(300, 220, 500, 55);
        this.add(usernameInput);

        passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 25));
        passwordLabel.setBounds(300,290,300,50);
        passwordLabel.setForeground(new Color(0x000000));
        this.add(passwordLabel);

        passwordInput = new JPasswordField();
        passwordInput.setFont(new Font("Arial", Font.BOLD, 25));
        passwordInput.setBounds(300, 360, 500, 55);
        this.add(passwordInput);


        submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Arial", Font.BOLD, 25));
        submitButton.addActionListener(this);
        submitButton.setBounds(400, 450, 300, 75);
        submitButton.setForeground(new Color(0x000000));
        submitButton.setBackground(new Color(0xffffff));
        this.add(submitButton);

        reusable.windowSetup(this);
        this.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {
            new userSelect();
            this.dispose();
        }
    }

}
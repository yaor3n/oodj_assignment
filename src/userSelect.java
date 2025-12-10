import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class userSelect extends JFrame implements ActionListener {

    private JLabel selectRole;
    private JButton loginButton, createAccountButton;

    userSelect() {
        selectRole = new JLabel("Welcome! Please select your login method:");
        selectRole.setFont(new Font("Arial", Font.BOLD, 25));
        selectRole.setBounds(290,20,1000,250);
        selectRole.setForeground(new Color(0x000000));
        this.add(selectRole);

        reusable.windowSetup(this);

        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 25));
        loginButton.addActionListener(this);
        loginButton.setBounds(400,225,300,75);
        loginButton.setForeground(new Color(0x000000));
        loginButton.setBackground(new Color(0xffffff));
        this.add(loginButton);

        createAccountButton = new JButton("Create Account");
        createAccountButton.setFont(new Font("Arial", Font.BOLD, 25));
        createAccountButton.addActionListener(this);
        createAccountButton.setBounds(400,325,300,75);
        createAccountButton.setForeground(new Color(0x000000));
        createAccountButton.setBackground(new Color(0xffffff));
        this.add(createAccountButton);

        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            new login();
            this.dispose();
        } else if (e.getSource() == createAccountButton) {
            new accountCreation();
            this.dispose();
        }
    }
}

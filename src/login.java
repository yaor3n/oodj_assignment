import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class login extends JFrame implements ActionListener {

    private JLabel welcomeLabel;
    private JButton loginButton;

    login() {
        welcomeLabel = new JLabel("Welcome to Better APSpace");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 25));
        welcomeLabel.setBounds(280,30,800,300);
        welcomeLabel.setForeground(new Color(0x000000));
        this.add(welcomeLabel);

        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 25));
        loginButton.addActionListener(this);
        loginButton.setBounds(225, 275, 300, 100);
        loginButton.setForeground(new Color(0x000000));
        loginButton.setBackground(new Color(0xffffff));
        this.add(loginButton);

        reusable.windowSetup(this);


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            new userSelect();
            this.dispose();
        }
    }

}

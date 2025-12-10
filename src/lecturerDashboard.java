import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class lecturerDashboard extends JFrame implements ActionListener {

    private JLabel titleLabel;
    private JButton logoutButton;

    lecturerDashboard() {

        titleLabel = new JLabel("Lecturer Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 25));
        titleLabel.setBounds(400, 50, 300, 50);
        this.add(titleLabel);

        logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 25));
        logoutButton.addActionListener(this);
        logoutButton.setBounds(420, 510, 200, 60);
        this.add(logoutButton);

        reusable.windowSetup(this);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed (ActionEvent e) {
        if (e.getSource() == logoutButton) {
            new userSelect();
            this.dispose();
        }
    }
}

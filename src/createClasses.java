import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class createClasses extends JFrame implements ActionListener {

    private JLabel titleLabel;
    private JButton backButton;

    createClasses() {

        titleLabel = new JLabel("Create Classes");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 25));
        titleLabel.setBounds(400, 50, 300, 50);
        this.add(titleLabel);

        backButton = new JButton("back");
        backButton.setFont(new Font("Arial", Font.BOLD, 25));
        backButton.addActionListener(this);
        backButton.setBounds(420, 510, 200, 60);
        this.add(backButton);

        reusable.windowSetup(this);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new crudUsers();
    }

    @Override
    public void actionPerformed (ActionEvent e) {
        if (e.getSource() == backButton) {
            new adminDashboard();
            this.dispose();
        }
    }


}
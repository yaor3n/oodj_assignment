import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class adminDashboard extends JFrame implements ActionListener {

    private JLabel titleLabel;
    private JButton logoutButton, crudUsersButton, assignLecButton, createClassesButton, defineGradingSystemButton;

    adminDashboard() {

        titleLabel = new JLabel("Admin Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 25));
        titleLabel.setBounds(400, 50, 300, 50);
        this.add(titleLabel);

        crudUsersButton = new JButton("CRUD Users");
        crudUsersButton.setFont(new Font("Arial", Font.BOLD, 25));
        crudUsersButton.addActionListener(this);
        crudUsersButton.setBounds(370,125,300,75);
        crudUsersButton.setForeground(new Color(0x000000));
        crudUsersButton.setBackground(new Color(0xffffff));
        this.add(crudUsersButton);

        assignLecButton = new JButton("Assign Lecturers");
        assignLecButton.setFont(new Font("Arial", Font.BOLD, 25));
        assignLecButton.addActionListener(this);
        assignLecButton.setBounds(370,225,300,75);
        assignLecButton.setForeground(new Color(0x000000));
        assignLecButton.setBackground(new Color(0xffffff));
        this.add(assignLecButton);

        createClassesButton = new JButton("Create Classes");
        createClassesButton.setFont(new Font("Arial", Font.BOLD, 25));
        createClassesButton.addActionListener(this);
        createClassesButton.setBounds(370,325,300,75);
        createClassesButton.setForeground(new Color(0x000000));
        createClassesButton.setBackground(new Color(0xffffff));
        this.add(createClassesButton);

        defineGradingSystemButton = new JButton("Define Grading System");
        defineGradingSystemButton.setFont(new Font("Arial", Font.BOLD, 25));
        defineGradingSystemButton.addActionListener(this);
        defineGradingSystemButton.setBounds(370,425,300,75);
        defineGradingSystemButton.setForeground(new Color(0x000000));
        defineGradingSystemButton.setBackground(new Color(0xffffff));
        this.add(defineGradingSystemButton);

        logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 25));
        logoutButton.addActionListener(this);
        logoutButton.setBounds(420, 510, 200, 60);
        this.add(logoutButton);

        reusable.windowSetup(this);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new adminDashboard();
    }

    @Override
    public void actionPerformed (ActionEvent e) {
        if (e.getSource() == logoutButton) {
            new userSelect();
            this.dispose();
        } else if (e.getSource() == crudUsersButton) {
            new crudUsers();
            this.dispose();
        } else if (e.getSource() == createClassesButton) {
            new createClasses();
            this.dispose();
        } else if (e.getSource() == assignLecButton) {
            new assignLecturer();
            this.dispose();
        } else if (e.getSource() == defineGradingSystemButton) {
            new defineGradingSystem();
            this.dispose();
        }
    }
}
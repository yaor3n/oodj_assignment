import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class studentDashboard extends JFrame implements ActionListener {

    private JLabel titleLabel;
    private JButton logoutButton;

    // Student feature buttons
    private JButton editProfileBtn;
    private JButton registerClassBtn;
    private JButton viewResultsBtn;
    private JButton commentLecturerBtn;
    private JButton viewCommentBtn;

    studentDashboard() {

        titleLabel = new JLabel("Student Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 25));
        titleLabel.setBounds(380, 40, 400, 50);
        this.add(titleLabel);

        editProfileBtn = new JButton("Edit Profile");
        registerClassBtn = new JButton("Register Class");
        viewResultsBtn = new JButton("Check Results");
        commentLecturerBtn = new JButton("Lecturer Comments");
        viewCommentBtn = new JButton("View Given Comments");

        JButton[] buttons = {
                editProfileBtn,
                registerClassBtn,
                viewResultsBtn,
                commentLecturerBtn,
                viewCommentBtn
        };

        int y = 150;
        for (JButton btn : buttons) {
            btn.setFont(new Font("Arial", Font.BOLD, 18));
            btn.setBounds(350, y, 300, 60);
            btn.addActionListener(this);
            this.add(btn);
            y += 80;
        }

        logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 20));
        logoutButton.setBounds(420, 520, 200, 60);
        logoutButton.addActionListener(this);
        this.add(logoutButton);

        reusable.windowSetup(this);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == editProfileBtn) {
            new studentEditProfile();   // new JFrame
            this.dispose();
        }

        else if (e.getSource() == registerClassBtn) {

            studentRegisterClass page = new studentRegisterClass();
            page.setVisible(true);
            this.dispose();
        }

        else if (e.getSource() == viewResultsBtn) {
            new studentViewResults();
            this.dispose();
        }

        else if (e.getSource() == commentLecturerBtn) {
            new studentFeedbackClass();
            this.dispose();
        }

        else if (e.getSource() == viewCommentBtn) {
            new studentViewFeedbackClass();
            this.dispose();
        }

        else if (e.getSource() == logoutButton) {
            new userSelect();
            this.dispose();
        }
    }
}

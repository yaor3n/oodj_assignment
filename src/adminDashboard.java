import javax.swing.*;
import java.awt.*;

public class adminDashboard extends JFrame {

    private JPanel sidebar;
    private boolean sidebarVisible = false;

    public adminDashboard() {

        setLayout(null);
        reusable.windowSetup(this);

        // ===== TOP BAR =====
        JLabel titleLabel = new JLabel("Admin Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setBounds(120, 20, 400, 40);
        add(titleLabel);

        JButton toggleSidebarBtn = new JButton("☰");
        toggleSidebarBtn.setFont(new Font("Arial", Font.BOLD, 20));
        toggleSidebarBtn.setBounds(20, 20, 50, 40);
        toggleSidebarBtn.addActionListener(e -> toggleSidebar());
        add(toggleSidebarBtn);

        // ===== SIDEBAR =====
        sidebar = new JPanel(null);
        sidebar.setBackground(new Color(0xf0f0f0));
        sidebar.setBounds(-220, 0, 220, 650);
        add(sidebar);

        JLabel sidebarTitle = new JLabel("Menu");
        sidebarTitle.setFont(new Font("Arial", Font.BOLD, 18));
        sidebarTitle.setBounds(20, 30, 180, 30);
        sidebar.add(sidebarTitle);

        JButton editProfileBtn = sidebarButton("Edit Profile", 80);
        editProfileBtn.addActionListener(e -> {
            new adminEditProfile();
            dispose();
        });

        JButton viewFeedbackBtn = sidebarButton("View Feedback", 130);
        viewFeedbackBtn.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Feedback page not implemented yet")
        );

        JButton logoutBtn = sidebarButton("Logout", 180);
        logoutBtn.addActionListener(e -> {
            new userSelect();
            dispose();
        });

        sidebar.add(editProfileBtn);
        sidebar.add(viewFeedbackBtn);
        sidebar.add(logoutBtn);

        // ===== MAIN BUTTONS (2x2 GRID) =====
        add(mainButton("CRUD Users", 300, 120, e -> {
            new crudUsers();
            dispose();
        }));

        add(mainButton("Assign Lecturers", 650, 120, e -> {
            new assignLecturer();
            dispose();
        }));

        add(mainButton("Create Classes", 300, 260, e -> {
            new createClasses();
            dispose();
        }));

        add(mainButton("Define Grading System", 650, 260, e -> {
            new defineGradingSystem();
            dispose();
        }));

        setVisible(true);
    }

    // ===== HELPERS =====
    private JButton mainButton(String text, int x, int y, java.awt.event.ActionListener a) {
        JButton btn = new JButton(text);
        btn.setBounds(x, y, 300, 90);
        btn.setFont(new Font("Arial", Font.BOLD, 20));
        btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.addActionListener(a);
        return btn;
    }

    private JButton sidebarButton(String text, int y) {
        JButton btn = new JButton(text);
        btn.setBounds(20, y, 180, 35);
        btn.setFocusPainted(false);
        return btn;
    }

    private void toggleSidebar() {
        sidebar.setBounds(sidebarVisible ? -220 : 0, 0, 220, 650);
        sidebarVisible = !sidebarVisible;
        repaint();
    }

    public static void main(String[] args) {
        new adminDashboard();
    }
}

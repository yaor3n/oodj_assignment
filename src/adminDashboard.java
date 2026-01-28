import javax.swing.*;
import java.awt.*;

public class adminDashboard extends JFrame {

    private JPanel sidebar;
    private boolean sidebarVisible = false;

    private JButton[] mainButtons;

    private final int BUTTON_WIDTH = 300;
    private final int BUTTON_HEIGHT = 180; // taller for icon + text
    private final int SIDEBAR_WIDTH = 220;

    public adminDashboard() {

        setLayout(null);
        reusable.windowSetup(this);

        // ===== SIDEBAR TOGGLE =====
        JButton toggleSidebarBtn = new JButton("\u2630"); // Unicode hamburger
        toggleSidebarBtn.setBounds(20, 20, 50, 40);
        toggleSidebarBtn.setBackground(new Color(241, 245, 249));
        toggleSidebarBtn.setFocusPainted(false);
        toggleSidebarBtn.addActionListener(e -> toggleSidebar());
        add(toggleSidebarBtn);

        // ===== SIDEBAR =====
        sidebar = new JPanel(null);
        sidebar.setBackground(new Color(241, 245, 249));
        sidebar.setBounds(-SIDEBAR_WIDTH, 0, SIDEBAR_WIDTH, 650);
        add(sidebar);

        // ===== APU LOGO =====
        ImageIcon logoIcon = loadIcon("images/APUlogo.png", 120, 110);
        JLabel logoLabel = new JLabel(logoIcon);
        logoLabel.setBounds(50, 20, 120, 110);
        sidebar.add(logoLabel);

        JButton editProfileBtn = sidebarButton("Edit Profile", 170);
        editProfileBtn.setBackground(new Color(30, 41, 59));
        editProfileBtn.setForeground(Color.WHITE);
        editProfileBtn.addActionListener(e -> {
            new adminEditProfile();
            dispose();
        });

        JButton manageFeedbackBtn = sidebarButton("Manage Feedback", 220);
        manageFeedbackBtn.setBackground(new Color(30, 41, 59));
        manageFeedbackBtn.setForeground(Color.WHITE);
        manageFeedbackBtn.addActionListener(e -> {
            new manageFeedback();
            dispose();
        });

        JButton logoutBtn = sidebarButton("Logout", 560);
        logoutBtn.setBackground(new Color(220, 53, 69));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.addActionListener(e -> {
            new login();
            dispose();
        });

        sidebar.add(editProfileBtn);
        sidebar.add(manageFeedbackBtn);
        sidebar.add(logoutBtn);

        // ===== HERO BAR =====
        JPanel heroBar = new JPanel(null);
        heroBar.setBackground(new Color(30, 41, 59));
        heroBar.setBounds(0, 0, getWidth(), 80);
        add(heroBar);

        JLabel titleLabel = new JLabel("Admin Dashboard", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setBounds(0, 20, getWidth(), 40);
        heroBar.add(titleLabel);

        // ===== MAIN BUTTONS (ICON + TEXT) =====
        mainButtons = new JButton[]{
                mainButton(
                        "CRUD Users",
                        "images/crudUsers.png",
                        e -> { new adminCrudUsers(); dispose(); }
                ),
                mainButton(
                        "Assign Lecturers",
                        "images/assignLecturers.png",
                        e -> { new adminAssignLecturer(); dispose(); }
                ),
                mainButton(
                        "Create Classes",
                        "images/createClasses.png",
                        e -> { new adminCreateClasses(); dispose(); }
                ),
                mainButton(
                        "Define Grading System",
                        "images/defineGradingSystem.png",
                        e -> { new adminDefineGradingSystem(); dispose(); }
                )
        };

        for (JButton btn : mainButtons) {
            add(btn);
        }

        positionMainButtons(false);
        setVisible(true);
    }

    // ===== BUTTON AND ICON  =====
    private JButton mainButton(String text, String iconPath, java.awt.event.ActionListener a) {
        JButton btn = new JButton(text);

        btn.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        btn.setFont(new Font("Arial", Font.BOLD, 18));
        btn.setBackground(new Color(241, 245, 249));
        btn.setFocusPainted(false);

        ImageIcon icon = loadIcon(iconPath, 64, 64);
        btn.setIcon(icon);

        btn.setHorizontalTextPosition(SwingConstants.CENTER);
        btn.setVerticalTextPosition(SwingConstants.BOTTOM);
        btn.setIconTextGap(12);

        btn.addActionListener(a);
        return btn;
    }

    // ===== SIDEBAR BUTTON =====
    private JButton sidebarButton(String text, int y) {
        JButton btn = new JButton(text);
        btn.setBounds(20, y, 180, 35);
        btn.setFocusPainted(false);
        return btn;
    }

    // ===== ICON LOADER =====
    private ImageIcon loadIcon(String path, int w, int h) {
        ImageIcon raw = new ImageIcon(path);
        Image scaled = raw.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    // ===== BUTTON POSITIONING =====
    private void positionMainButtons(boolean sidebarOpen) {
        int frameCenterX = getWidth() / 2;
        int offsetX = sidebarOpen ? SIDEBAR_WIDTH / 2 : 0;

        int startX = frameCenterX - (BUTTON_WIDTH + 50) + offsetX;

        int[][] positions = {
                {0, 120},
                {BUTTON_WIDTH + 100, 120},
                {0, 330},
                {BUTTON_WIDTH + 100, 330}
        };

        for (int i = 0; i < mainButtons.length; i++) {
            mainButtons[i].setLocation(
                    startX + positions[i][0],
                    positions[i][1]
            );
        }
    }

    // ===== TOGGLE SIDEBAR =====
    private void toggleSidebar() {
        sidebar.setBounds(sidebarVisible ? -SIDEBAR_WIDTH : 0, 0, SIDEBAR_WIDTH, 650);
        sidebarVisible = !sidebarVisible;
        positionMainButtons(sidebarVisible);
        repaint();
    }

    public static void main(String[] args) {
        new adminDashboard();
    }
}

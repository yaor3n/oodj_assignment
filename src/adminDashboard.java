import javax.swing.*;
import java.awt.*;

public class adminDashboard extends JFrame {

    private JPanel sidebar;
    private boolean sidebarVisible = false;

    private JButton[] mainButtons;

    private final int BUTTON_WIDTH = 300;
    private final int BUTTON_HEIGHT = 90;
    private final int SIDEBAR_WIDTH = 220;

    public adminDashboard() {

        setLayout(null);
        reusable.windowSetup(this);

        JButton toggleSidebarBtn = new JButton("☰");
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

        JLabel sidebarTitle = new JLabel("Menu");
        sidebarTitle.setFont(new Font("Arial", Font.BOLD, 18));
        sidebarTitle.setBounds(20, 30, 180, 30);
        sidebar.add(sidebarTitle);

        JButton editProfileBtn = sidebarButton("Edit Profile", 200);
        editProfileBtn.setBackground(new Color(30,41,59));
        editProfileBtn.setForeground(new Color(0xFFFFFF));
        editProfileBtn.addActionListener(e -> {
            new adminEditProfile();
            dispose();
        });

        JButton viewFeedbackBtn = sidebarButton("View Feedback", 250);
        viewFeedbackBtn.setBackground(new Color(30,41,59));
        viewFeedbackBtn.setForeground(new Color(0xFFFFFF));
        viewFeedbackBtn.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Feedback page not implemented yet")
        );

        JButton logoutBtn = sidebarButton("Logout", 550);
        logoutBtn.setBackground(new Color(220,53,69));
        logoutBtn.setForeground(new Color(0xFFFFFF));
        logoutBtn.addActionListener(e -> {
            new userSelect();
            dispose();
        });

        sidebar.add(editProfileBtn);
        sidebar.add(viewFeedbackBtn);
        sidebar.add(logoutBtn);

        // ===== APU LOGO =====
        ImageIcon rawIcon = new ImageIcon("APUlogo.png");
        Image scaledImg = rawIcon.getImage().getScaledInstance(
                120, 105, Image.SCALE_SMOOTH
        );
        ImageIcon logoIcon = new ImageIcon(scaledImg);

        JLabel logoLabel = new JLabel(logoIcon);
        logoLabel.setBounds(20, 30, 180, 165);
        sidebar.add(logoLabel);


        // ===== HERO HEADER ===== must be below sidebar and sidebar button if not it will be blocked
        JPanel heroBar = new JPanel(null);
        heroBar.setBackground(new Color(30,41,59));
        heroBar.setBounds(0, 0, getWidth(), 80);
        add(heroBar);

        JLabel titleLabel = new JLabel("Admin Dashboard", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setBounds(0, 20, getWidth(), 40);

        heroBar.add(titleLabel);


        // ===== MAIN BUTTONS =====
        mainButtons = new JButton[]{
                mainButton("CRUD Users", e -> {
                    new crudUsers();
                    dispose();
                }),
                mainButton("Assign Lecturers", e -> {
                    new assignLecturer();
                    dispose();
                }),
                mainButton("Create Classes", e -> {
                    new createClasses();
                    dispose();
                }),
                mainButton("Define Grading System", e -> {
                    new defineGradingSystem();
                    dispose();
                })
        };

        for (JButton btn : mainButtons) {
            add(btn);
        }

        // Position buttons centered initially
        positionMainButtons(false);

        setVisible(true);
    }

    // ===== HELPERS =====

    private JButton mainButton(String text, java.awt.event.ActionListener a) {
        JButton btn = new JButton(text);
        btn.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        btn.setFont(new Font("Arial", Font.BOLD, 20));
        btn.setBackground(new Color(241, 245, 249));
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

    private void positionMainButtons(boolean sidebarOpen) {
        int frameCenterX = getWidth() / 2;
        int offsetX = sidebarOpen ? SIDEBAR_WIDTH / 2 : 0;

        int startX = frameCenterX - (BUTTON_WIDTH + 50) + offsetX;

        int[][] positions = {
                {0, 120},
                {BUTTON_WIDTH + 100, 120},
                {0, 260},
                {BUTTON_WIDTH + 100, 260}
        };

        for (int i = 0; i < mainButtons.length; i++) {
            mainButtons[i].setLocation(
                    startX + positions[i][0],
                    positions[i][1]
            );
        }
    }

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

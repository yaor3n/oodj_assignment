import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class studentDashboard extends JFrame implements ActionListener {

    private final Color SIDEBAR_BG = new Color(241, 245, 249);
    private final Color NAV_BTN_COLOR = new Color(30, 41, 59);
    private final Color LOGOUT_RED = new Color(220, 53, 69);
    private final Color PRIMARY_TEXT = Color.BLACK;
    private final Color BACKGROUND_GREY = new Color(209, 213, 219);

    private JPanel sidebar, navPanel, bottomPanel;
    private boolean isCollapsed = false;
    private final int EXPANDED_WIDTH = 250;
    private final int COLLAPSED_WIDTH = 65;

    private JButton editProfileBtn, registerClassBtn, viewResultsBtn, commentLecturerBtn, viewCommentBtn, logoutButton, toggleBtn;
    private String studentFullName;

    public studentDashboard() {
        Student currentStudent = AccountFileHandler.getStudent(Session.currentUsername);
        this.studentFullName = (currentStudent != null) ? currentStudent.getfullName() : Session.currentUsername;

        setTitle("Student Dashboard");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(EXPANDED_WIDTH, 800));
        sidebar.setBackground(SIDEBAR_BG);

        setupSidebarTop();
        setupSidebarCenter();
        setupSidebarBottom();

        add(sidebar, BorderLayout.WEST);

        JPanel mainWrapper = new JPanel(new BorderLayout());
        mainWrapper.setBackground(BACKGROUND_GREY);
        mainWrapper.setBorder(new EmptyBorder(40, 50, 40, 50));

        setupMainHeader(mainWrapper);

        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 0, 30));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(30, 0, 0, 0));

        centerPanel.add(createDisplayBox("My Classes", getStudentClasses()));
        centerPanel.add(createDisplayBox("Top Comment", getRandomFeedback()));

        mainWrapper.add(centerPanel, BorderLayout.CENTER);
        add(mainWrapper, BorderLayout.CENTER);

        setVisible(true);
    }

    private void setupSidebarTop() {
        JPanel sidebarTop = new JPanel(new BorderLayout());
        sidebarTop.setOpaque(false);
        sidebarTop.setPreferredSize(new Dimension(EXPANDED_WIDTH, 80));

        toggleBtn = new JButton("☰");
        toggleBtn.setFont(new Font("Arial", Font.BOLD, 28));
        toggleBtn.setForeground(PRIMARY_TEXT);
        toggleBtn.setBorderPainted(false);
        toggleBtn.setContentAreaFilled(false);
        toggleBtn.setFocusPainted(false);
        toggleBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        toggleBtn.addActionListener(e -> toggleSidebar());

        sidebarTop.add(toggleBtn, BorderLayout.WEST);
        sidebar.add(sidebarTop, BorderLayout.NORTH);
    }

    private void setupSidebarCenter() {
        navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setOpaque(false);

        registerClassBtn = new JButton("Class Registration");
        viewResultsBtn = new JButton("Check Results");
        commentLecturerBtn = new JButton("Give Feedback");
        viewCommentBtn = new JButton("View Feedback");

        JButton[] navButtons = {registerClassBtn, viewResultsBtn, commentLecturerBtn, viewCommentBtn};
        for (JButton btn : navButtons) {
            styleSidebarButton(btn, navPanel);
            navPanel.add(Box.createVerticalStrut(10));
        }

        sidebar.add(navPanel, BorderLayout.CENTER);
    }

    private void setupSidebarBottom() {
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setOpaque(false);

        editProfileBtn = new JButton("User Profile");
        styleSidebarButton(editProfileBtn, bottomPanel);
        bottomPanel.add(Box.createVerticalStrut(15));

        logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoutButton.setBackground(LOGOUT_RED);
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setOpaque(true);
        logoutButton.setBorderPainted(false);
        logoutButton.setContentAreaFilled(true);
        logoutButton.setMaximumSize(new Dimension(200, 45));
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.addActionListener(this);

        bottomPanel.add(logoutButton);
        bottomPanel.add(Box.createVerticalStrut(30));

        sidebar.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void styleSidebarButton(JButton btn, JPanel container) {
        btn.setMaximumSize(new Dimension(220, 50));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(NAV_BTN_COLOR);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.addActionListener(this);

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(new Color(51, 65, 85)); }
            public void mouseExited(MouseEvent e) { btn.setBackground(NAV_BTN_COLOR); }
        });

        container.add(btn);
    }

    private void toggleSidebar() {
        isCollapsed = !isCollapsed;
        sidebar.setPreferredSize(new Dimension(isCollapsed ? COLLAPSED_WIDTH : EXPANDED_WIDTH, 800));
        navPanel.setVisible(!isCollapsed);
        bottomPanel.setVisible(!isCollapsed);
        sidebar.revalidate();
        sidebar.repaint();
    }

    private void setupMainHeader(JPanel wrapper) {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel titleLabel = new JLabel("Student Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
        header.add(titleLabel, BorderLayout.WEST);

        JPanel profileSpace = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        profileSpace.setOpaque(false);

        JLabel userLabel = new JLabel(studentFullName);
        userLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JLabel profileIcon = new JLabel("👤");
        profileIcon.setFont(new Font("Arial", Font.PLAIN, 50));

        profileSpace.add(userLabel);
        profileSpace.add(profileIcon);
        header.add(profileSpace, BorderLayout.EAST);
        wrapper.add(header, BorderLayout.NORTH);
    }

    private JScrollPane getStudentClasses() {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        java.util.HashSet<String> uniqueModules = new java.util.HashSet<>();

        // Retrieve the student's ID (e.g., U011)
        Student currentStudent = AccountFileHandler.getStudent(Session.currentUsername);
        String studentId = (currentStudent != null) ? currentStudent.id.trim() : Session.currentUsername;

        try (BufferedReader br = new BufferedReader(new FileReader("student_courses.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    // parts[0] is the ID (U011) based on your file structure
                    String idInFile = parts[0].trim();
                    String moduleName = parts[2].trim();

                    if (idInFile.equalsIgnoreCase(studentId)) {
                        if (uniqueModules.add(moduleName)) {
                            listModel.addElement("  • " + moduleName);
                        }
                    }
                }
            }
        } catch (IOException e) {
            listModel.addElement("  Error loading file.");
        }

        if (listModel.isEmpty()) {
            listModel.addElement("  No classes found for ID: " + studentId);
        }

        JList<String> list = new JList<>(listModel);
        list.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        list.setFixedCellHeight(40);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(list);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Color.WHITE);
        return scroll;
    }

    private JTextArea getRandomFeedback() {
        ArrayList<String> feedbacks = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("feedback.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    feedbacks.add("\"" + parts[3].trim() + "\"\n- Feedback for " + parts[1]);
                }
            }
        } catch (IOException e) { feedbacks.add("Could not load feedback."); }

        String displayTxt = feedbacks.isEmpty() ? "No feedback available." :
                feedbacks.get(new Random().nextInt(feedbacks.size()));

        JTextArea area = new JTextArea(displayTxt);
        area.setFont(new Font("Segoe UI", Font.ITALIC, 20));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setEditable(false);
        area.setOpaque(false);
        return area;
    }

    private JPanel createDisplayBox(String title, JComponent content) {
        JPanel box = new JPanel(new BorderLayout());
        box.setBackground(Color.WHITE);
        box.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        JLabel label = new JLabel(title);
        label.setFont(new Font("Arial", Font.BOLD, 30));
        label.setBorder(new EmptyBorder(15, 25, 10, 0));
        box.add(label, BorderLayout.NORTH);
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(0, 25, 20, 25));
        wrapper.add(content);
        box.add(wrapper, BorderLayout.CENTER);
        return box;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object s = e.getSource();
        this.dispose();
        if (s == logoutButton) new userSelect();
        else if (s == editProfileBtn) new studentEditProfile();
        else if (s == registerClassBtn) new studentRegisterClass();
        else if (s == viewResultsBtn) new studentViewResults();
        else if (s == commentLecturerBtn) new studentFeedbackClass();
        else if (s == viewCommentBtn) new studentViewFeedbackClass();
    }
}
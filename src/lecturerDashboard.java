import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.ArrayList;
import utils.NicerButton;

public class lecturerDashboard extends JFrame {

  private JPanel sidebar;
  private JPanel contentPanel;
  private JPanel cardContentArea;
  private CardLayout cardLayout;
  private JButton btnAss;
  private JButton btnFeed;
  private JButton backBtn;
  private int sidebarWidth = 250;
  private JButton editProfileBtn;

  private boolean modulesVisible = true;
  private String username;
  private String lecturerID;
  private String lecturerName;
  private String selectedCourse;
  private String lecturerModule;

  Color colorSidebar = new Color(241, 245, 249);
  Color colorHeader = new Color(90, 90, 90);
  Color colorBackground = new Color(255, 255, 255);
  Color googleBlue = new Color(66, 133, 244);
  Color colorBorder = new Color(200, 200, 200);
  Color darkNavy = new Color(30, 41, 59);
  Color hoverNavy = new Color(51, 65, 85);

  public lecturerDashboard(String username) {
    this.username = username;
    loadLecturerData(username);

    setTitle("Lecturer Dashboard");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(1100, 750);
    setLocationRelativeTo(null);
    setLayout(new BorderLayout());

    sidebar = new JPanel();
    sidebar.setBackground(colorSidebar);
    sidebar.setPreferredSize(new Dimension(0, 0));
    sidebar.setLayout(null);
    add(sidebar, BorderLayout.WEST);

    JPanel header = new JPanel(new BorderLayout());
    header.setBackground(colorHeader);
    header.setPreferredSize(new Dimension(0, 50));
    header.setBorder(new EmptyBorder(0, 10, 0, 10));

    JButton toggleSidebar = new JButton("☰");
    toggleSidebar.setFont(new Font("Arial", Font.BOLD, 20));
    toggleSidebar.setForeground(Color.WHITE);
    toggleSidebar.setBorderPainted(false);
    toggleSidebar.setContentAreaFilled(false);
    toggleSidebar.setFocusPainted(false);
    toggleSidebar.addActionListener(e -> toggleSidebar());

    JLabel headerTitle = new JLabel("Lecturer Dashboard", SwingConstants.CENTER);
    headerTitle.setForeground(Color.WHITE);
    headerTitle.setFont(new Font("Arial", Font.PLAIN, 16));

    header.add(toggleSidebar, BorderLayout.WEST);
    header.add(headerTitle, BorderLayout.CENTER);
    add(header, BorderLayout.NORTH);

    contentPanel = new JPanel(new BorderLayout());
    contentPanel.setBackground(colorBackground);
    add(contentPanel, BorderLayout.CENTER);

    showCourseSelection();
    setVisible(true);
  }

  private JButton sidebarButton(String text, int y) {
    NicerButton btn = new NicerButton(text, darkNavy, hoverNavy, 15);
    btn.setBounds(25, y, 200, 45);
    return btn;
  }

  private void loadLecturerData(String username) {
    try (BufferedReader r = new BufferedReader(new FileReader("accounts.txt"))) {
      String line;
      while ((line = r.readLine()) != null) {
        String[] p = line.split(",", 9);
        if (p.length == 9 && p[5].equals(username) && p[7].equals("Lecturer")) {
          lecturerID = p[0];
          lecturerName = p[1];
          return;
        }
      }
    } catch (IOException e) {
    }
  }

  private List<String> loadCoursesForLecturer() {
    List<String> list = new ArrayList<>();
    try (BufferedReader r = new BufferedReader(new FileReader("lecturerCourse.txt"))) {
      String line;
      while ((line = r.readLine()) != null) {
        String[] p = line.split(",", 4);
        if (p.length == 4 && p[2].equals(username) && p[3] != null) {
          if (!list.contains(p[3]))
            list.add(p[3]);
        }
      }
    } catch (IOException e) {
    }
    return list;
  }

  private List<String[]> loadModulesForCourse(String course) {
    List<String[]> list = new ArrayList<>();
    try (BufferedReader r = new BufferedReader(new FileReader("academicLeaderModule.txt"))) {
      String line;
      while ((line = r.readLine()) != null) {
        String[] p = line.split(",", -1);
        if (p.length >= 10 && p[3].equals(username) && p[9].equals(course))
          list.add(p);
      }
    } catch (IOException e) {
    }
    return list;
  }

  private void showCourseSelection() {
    lecturerModule = null;
    setupNavigationOnlySidebar();

    contentPanel.removeAll();

    JPanel mainContainer = new JPanel(new BorderLayout(20, 20));
    mainContainer.setBackground(colorBackground);
    mainContainer.setBorder(new EmptyBorder(40, 60, 40, 60));

    JLabel label = new JLabel("Select a Course", SwingConstants.CENTER);
    label.setFont(new Font("Arial", Font.BOLD, 28));
    label.setBorder(new EmptyBorder(0, 0, 20, 0));
    mainContainer.add(label, BorderLayout.NORTH);

    JPanel gridPanel = new JPanel(new GridLayout(0, 3, 25, 25));
    gridPanel.setBackground(colorBackground);

    for (String courseName : loadCoursesForLecturer()) {
      gridPanel.add(createCourseCard(courseName));
    }

    JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
    wrapper.setBackground(colorBackground);
    wrapper.add(gridPanel);

    JScrollPane scrollPane = new JScrollPane(wrapper);
    scrollPane.setBorder(null);
    scrollPane.getVerticalScrollBar().setUnitIncrement(16);
    scrollPane.getViewport().setBackground(colorBackground);

    mainContainer.add(scrollPane, BorderLayout.CENTER);
    contentPanel.add(mainContainer, BorderLayout.CENTER);

    refresh();
  }

  private void setupNavigationOnlySidebar() {
    sidebar.removeAll();
    sidebar.setPreferredSize(new Dimension(0, 750));

    try {
      File imgFile = new File("images/APUlogo.png");
      if (imgFile.exists()) {
        ImageIcon rawIcon = new ImageIcon(imgFile.getAbsolutePath());
        Image scaledImg = rawIcon.getImage().getScaledInstance(120, 105, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(scaledImg));
        logoLabel.setBounds(65, 20, 120, 105);
        sidebar.add(logoLabel);
      }
    } catch (Exception e) {
    }

    NicerButton editProfileBtn = new NicerButton("Profile", darkNavy, hoverNavy, 15);
    editProfileBtn.setBounds(25, 140, 200, 45);
    editProfileBtn.addActionListener(e -> new lecturerEditProfile(username));
    sidebar.add(editProfileBtn);

    NicerButton logoutBtn = new NicerButton("Logout", new Color(220, 53, 69), new Color(180, 40, 50), 15);
    logoutBtn.setBounds(25, 610, 200, 45);
    logoutBtn.addActionListener(e -> {
      int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout",
          JOptionPane.YES_NO_OPTION);
      if (confirm == JOptionPane.YES_OPTION) {
        new login();
        this.dispose();
      }
    });
    sidebar.add(logoutBtn);

    sidebar.revalidate();
    sidebar.repaint();
  }

  private JPanel createCourseCard(String courseName) {
    JPanel card = new JPanel(new GridBagLayout());
    card.setPreferredSize(new Dimension(250, 250));
    card.setMinimumSize(new Dimension(250, 250));
    card.setMaximumSize(new Dimension(250, 250));
    card.setBackground(Color.WHITE);
    card.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
    card.setCursor(new Cursor(Cursor.HAND_CURSOR));

    JLabel nameLabel = new JLabel(
        "<html><body style='width: 150px; text-align: center;'>" + courseName + "</body></html>");
    nameLabel.setFont(new Font("Arial", Font.BOLD, 22));
    nameLabel.setForeground(new Color(50, 50, 50));
    card.add(nameLabel);

    card.addMouseListener(new java.awt.event.MouseAdapter() {
      @Override
      public void mouseEntered(java.awt.event.MouseEvent e) {
        card.setBackground(new Color(250, 250, 250));
        card.setBorder(BorderFactory.createLineBorder(googleBlue, 2));
      }

      @Override
      public void mouseExited(java.awt.event.MouseEvent e) {
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
      }

      @Override
      public void mousePressed(java.awt.event.MouseEvent e) {
        selectedCourse = courseName;
        showModuleSidebar();
      }
    });

    return card;
  }

  private void showModuleSidebar() {
    List<String[]> modules = loadModulesForCourse(selectedCourse);

    if (modules == null || modules.isEmpty()) {
      JOptionPane.showMessageDialog(this, "No modules found for this course", "Empty Course",
          JOptionPane.WARNING_MESSAGE);
      return;
    }

    sidebar.removeAll();
    sidebar.setPreferredSize(new Dimension(sidebarWidth, 750));

    try {
      File imgFile = new File("images/APUlogo.png");
      if (imgFile.exists()) {
        ImageIcon rawIcon = new ImageIcon(imgFile.getAbsolutePath());
        Image scaledImg = rawIcon.getImage().getScaledInstance(120, 105, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(scaledImg));
        logoLabel.setBounds(65, 20, 120, 105);
        sidebar.add(logoLabel);
      }
    } catch (Exception e) {
    }

    NicerButton editBtn = new NicerButton("Edit Profile", darkNavy, hoverNavy, 15);
    editBtn.setBounds(25, 140, 200, 45);
    editBtn.addActionListener(e -> new lecturerEditProfile(username));
    sidebar.add(editBtn);

    String arrow = modulesVisible ? "▼ " : "▶ ";
    JLabel moduleHeader = new JLabel(arrow + "Modules", SwingConstants.LEFT);
    moduleHeader.setFont(new Font("Arial", Font.BOLD, 16));
    moduleHeader.setBounds(35, 205, 180, 30);
    moduleHeader.setForeground(new Color(71, 85, 105));
    moduleHeader.setCursor(new Cursor(Cursor.HAND_CURSOR));
    moduleHeader.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent e) {
        modulesVisible = !modulesVisible;
        showModuleSidebar();
      }
    });
    sidebar.add(moduleHeader);

    int yOffset = 240;
    if (modulesVisible) {
      for (String[] m : modules) {
        JButton btn = sidebarButton(m[1], yOffset);
        btn.addActionListener(e -> loadModuleDashboard(m));
        sidebar.add(btn);
        yOffset += 55;
      }
    }

    NicerButton logoutBtn = new NicerButton("Logout", new Color(220, 53, 69), new Color(180, 40, 50), 15);
    logoutBtn.setBounds(25, 610, 200, 45);
    logoutBtn.addActionListener(e -> {
      int confirm = JOptionPane.showConfirmDialog(this, "Logout?", "Confirm", JOptionPane.YES_NO_OPTION);
      if (confirm == JOptionPane.YES_OPTION) {
        new login();
        dispose();
      }
    });
    sidebar.add(logoutBtn);

    if (lecturerModule == null && !modules.isEmpty()) {
      loadModuleDashboard(modules.get(0));
    }

    sidebar.revalidate();
    sidebar.repaint();
  }

  private void loadModuleDashboard(String[] module) {
    lecturerModule = module[1];
    contentPanel.removeAll();

    JPanel wrapper = new JPanel(new BorderLayout());
    wrapper.setBackground(colorBackground);
    wrapper.setBorder(new EmptyBorder(20, 40, 20, 40));

    JPanel topArea = new JPanel(new BorderLayout());
    topArea.setOpaque(false);
    backBtn = new JButton("Back");
    backBtn.addActionListener(e -> showCourseSelection());
    JPanel backContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
    backContainer.setOpaque(false);
    backContainer.add(backBtn);

    JPanel moduleHeader = new JPanel(new BorderLayout());
    moduleHeader.setBackground(colorSidebar);
    moduleHeader.setBorder(BorderFactory.createLineBorder(Color.GRAY, 3));
    moduleHeader.setPreferredSize(new Dimension(0, 45));
    JLabel nameLbl = new JLabel(lecturerModule, SwingConstants.CENTER);
    nameLbl.setFont(new Font("Arial", Font.BOLD, 18));
    moduleHeader.add(nameLbl);

    topArea.add(backContainer, BorderLayout.NORTH);
    topArea.add(Box.createVerticalStrut(15), BorderLayout.CENTER);
    topArea.add(moduleHeader, BorderLayout.SOUTH);

    JPanel tabRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
    tabRow.setOpaque(false);
    btnAss = createTabButton("Assessment", true);
    btnFeed = createTabButton("Student Feedback", false);
    btnAss.addActionListener(e -> switchTab("ASSESSMENT"));
    btnFeed.addActionListener(e -> switchTab("FEEDBACK"));
    tabRow.add(btnAss);
    tabRow.add(btnFeed);

    cardLayout = new CardLayout();
    cardContentArea = new JPanel(cardLayout);
    cardContentArea.setBackground(Color.WHITE);
    cardContentArea.setBorder(BorderFactory.createLineBorder(colorBorder));

    lecturerAssessment assessmentView = new lecturerAssessment(selectedCourse, lecturerModule, lecturerID, lecturerName,
        username);
    lecturerStudentFeedback feedbackView = new lecturerStudentFeedback(lecturerModule);

    cardContentArea.add(assessmentView, "ASSESSMENT");
    cardContentArea.add(feedbackView, "FEEDBACK");

    JPanel mainContent = new JPanel(new BorderLayout());
    mainContent.setOpaque(false);
    mainContent.add(Box.createVerticalStrut(20), BorderLayout.NORTH);
    JPanel midContainer = new JPanel(new BorderLayout());
    midContainer.setOpaque(false);
    midContainer.add(tabRow, BorderLayout.NORTH);
    midContainer.add(cardContentArea, BorderLayout.CENTER);
    mainContent.add(midContainer, BorderLayout.CENTER);

    wrapper.add(topArea, BorderLayout.NORTH);
    wrapper.add(mainContent, BorderLayout.CENTER);
    contentPanel.add(wrapper, BorderLayout.CENTER);
    refresh();
  }

  private JButton createTabButton(String text, boolean active) {
    JButton btn = new JButton(text);
    btn.setPreferredSize(new Dimension(150, 40));
    applyTabStyle(btn, active);
    return btn;
  }

  private void applyTabStyle(JButton btn, boolean active) {
    btn.setFont(new Font("Arial", active ? Font.BOLD : Font.PLAIN, 13));
    if (active) {
      btn.setBackground(Color.WHITE);
      btn.setForeground(googleBlue);
      btn.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 1, colorBorder));
    } else {
      btn.setBackground(new Color(240, 240, 240));
      btn.setForeground(Color.GRAY);
      btn.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, colorBorder));
    }
  }

  private void switchTab(String tabName) {
    cardLayout.show(cardContentArea, tabName);
    applyTabStyle(btnAss, tabName.equals("ASSESSMENT"));
    applyTabStyle(btnFeed, tabName.equals("FEEDBACK"));
  }

  private void toggleSidebar() {
    int currentWidth = sidebar.getPreferredSize().width;
    int newWidth = (currentWidth == 0) ? sidebarWidth : 0;
    sidebar.setPreferredSize(new Dimension(newWidth, 0));
    this.getContentPane().revalidate();
    this.getContentPane().repaint();
  }

  private void refresh() {
    revalidate();
    repaint();
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> new lecturerDashboard("lecturer1"));
  }
}

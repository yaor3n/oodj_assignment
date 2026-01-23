import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.List;

public class studentRegisterClass extends JFrame implements ActionListener {
    private JComboBox<String> moduleCombo;
    private JTextPane detailsPane;
    private JLabel imageLabel;
    private JButton registerBtn, backBtn;
    private List<Module> availableModules;
    private Student currentStudent;

    private final Color BACKGROUND = new Color(209, 213, 219);
    private final Color NAVY_SLATE = new Color(30, 41, 59);
    private final Color WHITE_CARD = Color.WHITE;
    private final Color TEXT_MAIN = new Color(30, 41, 59);

    public studentRegisterClass() {
        currentStudent = AccountFileHandler.getStudent(Session.currentUsername);
        String studentCourse = currentStudent.getCourse();
        availableModules = ClassFileHandler.getModulesForStudent(studentCourse);

        setTitle("Module Registration");
        setSize(950, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainWrapper = new JPanel(new BorderLayout());
        mainWrapper.setBackground(BACKGROUND);
        mainWrapper.setBorder(new EmptyBorder(30, 40, 30, 40));
        add(mainWrapper);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel title = new JLabel("Course Enrollment");
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setForeground(TEXT_MAIN);

        JLabel subTitle = new JLabel("Available modules for: " + studentCourse);
        subTitle.setFont(new Font("Arial", Font.PLAIN, 16));
        subTitle.setForeground(new Color(71, 85, 105));

        headerPanel.add(title, BorderLayout.NORTH);
        headerPanel.add(subTitle, BorderLayout.SOUTH);
        mainWrapper.add(headerPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 20));
        centerPanel.setOpaque(false);

        moduleCombo = new JComboBox<>();
        moduleCombo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        for (Module m : availableModules) {
            moduleCombo.addItem(m.getName());
        }
        moduleCombo.addActionListener(this);
        centerPanel.add(moduleCombo, BorderLayout.NORTH);

        JPanel infoCard = new JPanel(new BorderLayout(20, 0));
        infoCard.setBackground(WHITE_CARD);
        infoCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(148, 163, 184), 1),
                new EmptyBorder(20, 20, 20, 20)
        ));

        imageLabel = new JLabel("Loading Image...", SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(300, 300));
        imageLabel.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));
        infoCard.add(imageLabel, BorderLayout.WEST);

        detailsPane = new JTextPane();
        detailsPane.setContentType("text/html");
        detailsPane.setEditable(false);
        detailsPane.setBackground(WHITE_CARD);

        JScrollPane scroll = new JScrollPane(detailsPane);
        scroll.setBorder(null);
        infoCard.add(scroll, BorderLayout.CENTER);

        centerPanel.add(infoCard, BorderLayout.CENTER);
        mainWrapper.add(centerPanel, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(20, 0, 0, 0));

        backBtn = new JButton("Back");
        backBtn.setPreferredSize(new Dimension(120, 45));
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backBtn.addActionListener(this);

        registerBtn = new JButton("Confirm Registration");
        registerBtn.setPreferredSize(new Dimension(220, 45));
        registerBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        registerBtn.setBackground(NAVY_SLATE);
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setOpaque(true);
        registerBtn.setBorderPainted(false);
        registerBtn.setContentAreaFilled(true);
        registerBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerBtn.addActionListener(this);

        footer.add(backBtn);
        footer.add(registerBtn);
        mainWrapper.add(footer, BorderLayout.SOUTH);

        if (!availableModules.isEmpty()) updateDisplay(0);
        setVisible(true);
    }

    private void updateDisplay(int index) {
        Module m = availableModules.get(index);

        String html = "<html><body style='font-family: Arial; padding: 10px;'>" +
                "<h2 style='color: #1e293b; margin-bottom: 0;'>" + m.getName() + "</h2>" +
                "<p style='color: #64748b;'><b>Lecturer:</b> " + m.getLecturer() + "</p>" +
                "<hr color='#e2e8f0'>" +
                "<div style='color: #334155; font-size: 12px;'>" +
                "<b>Schedule Details:</b><br>" + m.getDetails() +
                "</div></body></html>";
        detailsPane.setText(html);

        try {
            ImageIcon icon = new ImageIcon(m.getImagePath());
            Image img = icon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(img));
            imageLabel.setText("");
        } catch (Exception e) {
            imageLabel.setIcon(null);
            imageLabel.setText("Image not available");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == moduleCombo) {
            updateDisplay(moduleCombo.getSelectedIndex());
        } else if (e.getSource() == registerBtn) {
            int index = moduleCombo.getSelectedIndex();
            if (index != -1) {
                Module selected = availableModules.get(index);
                String studentId = currentStudent.id.trim();
                String moduleName = selected.getName().trim();

                boolean alreadyRegistered = false;
                File file = new File("student_courses.txt");

                if (file.exists()) {
                    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            String[] parts = line.split(",");
                            if (parts.length >= 3) {
                                if (parts[0].trim().equalsIgnoreCase(studentId) &&
                                        parts[2].trim().equalsIgnoreCase(moduleName)) {
                                    alreadyRegistered = true;
                                    break;
                                }
                            }
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }

                if (alreadyRegistered) {
                    JOptionPane.showMessageDialog(this,
                            "You are already registered for: " + moduleName,
                            "Duplicate Registration",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int choice = JOptionPane.showConfirmDialog(this,
                        "Register for " + moduleName + "?", "Confirm", JOptionPane.YES_NO_OPTION);

                if (choice == JOptionPane.YES_OPTION) {
                    boolean success = ClassFileHandler.registerStudentToModule(
                            studentId, currentStudent.fullName, moduleName);

                    if (success) {
                        JOptionPane.showMessageDialog(this, "Registration successful!");
                    } else {
                        JOptionPane.showMessageDialog(this, "Error updating registration.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } else if (e.getSource() == backBtn) {
            new studentDashboard();
            dispose();
        }
    }
}
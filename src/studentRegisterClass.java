import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.io.File;

public class studentRegisterClass extends JFrame implements ActionListener {

    private JComboBox<String> courseCombo;
    private JTextPane detailsPane; // Changed from JTextArea for HTML support
    private JLabel imageLabel;     // New label for course image
    private JButton registerBtn, backBtn;

    private List<Course> courses;

    public studentRegisterClass() {
        setTitle("Class Registration");
        setSize(900, 600);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        courses = CourseFileHandler.getAllCourses();

        // Title
        JLabel title = new JLabel("Class Registration");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(new Color(43, 80, 110)); // Dark Blue color
        title.setBounds(300, 20, 300, 40);
        add(title);

        // ===== Course Selection =====
        JLabel selectLbl = new JLabel("Select Course:");
        selectLbl.setFont(new Font("Arial", Font.PLAIN, 14));
        selectLbl.setBounds(100, 85, 120, 30);
        add(selectLbl);

        courseCombo = new JComboBox<>();
        for (Course c : courses) {
            courseCombo.addItem(c.getCode() + " - " + c.getName());
        }
        courseCombo.setBounds(210, 85, 590, 35);
        courseCombo.addActionListener(this);
        add(courseCombo);

        //  container for course info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(null);
        infoPanel.setBounds(100, 140, 700, 300);
        infoPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        infoPanel.setBackground(Color.WHITE);
        add(infoPanel);

        // image box
        imageLabel = new JLabel("No Image", SwingConstants.CENTER);
        imageLabel.setBounds(15, 15, 270, 270);
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        imageLabel.setOpaque(true);
        imageLabel.setBackground(new Color(245, 245, 245));
        infoPanel.add(imageLabel);

        // Right Side: Details (Scrollable
        detailsPane = new JTextPane();
        detailsPane.setEditable(false);
        detailsPane.setContentType("text/html"); // Allows HTML styling

        JScrollPane scrollPane = new JScrollPane(detailsPane);
        scrollPane.setBounds(300, 15, 385, 270);
        scrollPane.setBorder(null); // Cleaner look
        infoPanel.add(scrollPane);

        // buttons
        registerBtn = new JButton("Register Course");
        registerBtn.setBounds(300, 470, 140, 45);
        registerBtn.setBackground(new Color(76, 175, 80)); // Green
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setFocusPainted(false);
        registerBtn.addActionListener(this);
        add(registerBtn);

        backBtn = new JButton("Back");
        backBtn.setBounds(460, 470, 140, 45);
        backBtn.addActionListener(this);
        add(backBtn);

        // initialize display
        if (courses != null && !courses.isEmpty()) {
            updateDescription(0);
        } else {
            JOptionPane.showMessageDialog(this, "No courses available. Check courses.txt path.");
        }

        setVisible(true);
    }

    private void updateDescription(int index) {
        if (index < 0 || index >= courses.size()) return;

        Course c = courses.get(index);

        // 1. Update Image Logic
        displayImage(c.getImageFilePath());

        // 2. Update Details with HTML
        String html = "<html><body style='font-family: Arial; margin: 10px;'>" +
                "<h2 style='color: #2b506e; margin-bottom: 0;'>" + c.getName() + "</h2>" +
                "<p style='color: #555;'><b>Code:</b> " + c.getCode() + "</p>" +
                "<hr>" +
                "<b>Lecturer:</b> " + c.getLecturer() + "<br>" +
                "<b>Qualification:</b> " + c.getQualification() + "<br>" +
                "<b>Intake:</b> " + c.getIntakeMonth() + " " + c.getYear() + "<br><br>" +
                "<b>Course Description:</b><br>" +
                "<p style='font-style: italic;'>" + c.getDescription() + "</p>" +
                "</body></html>";

        detailsPane.setText(html);
        detailsPane.setCaretPosition(0); // Scroll back to top
    }

    private void displayImage(String path) {
        try {
            File imgFile = new File(path);
            if (imgFile.exists()) {
                ImageIcon icon = new ImageIcon(path);
                // Scale to fit label: 270x270
                Image img = icon.getImage().getScaledInstance(270, 270, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(img));
                imageLabel.setText("");
            } else {
                imageLabel.setIcon(null);
                imageLabel.setText("<html><center>Image Not Found<br>(" + path + ")</center></html>");
            }
        } catch (Exception e) {
            imageLabel.setIcon(null);
            imageLabel.setText("Error loading image");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == courseCombo) {
            updateDescription(courseCombo.getSelectedIndex());
        }

        if (e.getSource() == registerBtn) {
            int index = courseCombo.getSelectedIndex();
            if (index != -1) {
                Course selected = courses.get(index);

                String warningMsg = "<html>Are you sure you want to register for <b>" + selected.getName() + "</b>?<br>" + "Once you register, you cannot undo it.</html>";

                int response =JOptionPane.showConfirmDialog(
                        this,
                        warningMsg,
                        "Confirm Registration",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

                if (response == JOptionPane.YES_OPTION) {
                    CourseFileHandler.registerStudent(Session.currentUsername, selected);
                    JOptionPane.showMessageDialog(this, "Successfully registered for: " + selected.getName());
                }
            }
        }

        if (e.getSource() == backBtn) {
            new studentDashboard();
            dispose();
        }
    }
}
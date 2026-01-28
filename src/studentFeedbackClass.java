import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class studentFeedbackClass extends JFrame implements ActionListener {
    private JComboBox<String> moduleCombo;
    private JLabel lecturerLbl;
    private JTextArea commentArea;
    private JButton submitBtn, backBtn;
    private List<Module> availableModules;
    private Student currentStudent;
    private JRadioButton[] ratingButtons;
    private ButtonGroup ratingGroup;

    private final Color BACKGROUND = new Color(209, 213, 219);
    private final Color NAVY_SLATE = new Color(30, 41, 59);
    private final Color WHITE_CARD = Color.WHITE;
    private final Color TEXT_MAIN = new Color(30, 41, 59);
    private final Color SUBTITLE_TEXT = new Color(71, 85, 105);

    public studentFeedbackClass() {
        currentStudent = AccountFileHandler.getStudent(Session.currentUsername);
        if (currentStudent == null) {
            JOptionPane.showMessageDialog(null, "Session expired.");
            dispose();
            return;
        }

        availableModules = ClassFileHandler.getModulesForStudent(currentStudent.getCourse());

        setTitle("Give Feedback");
        setSize(600, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND);
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
        add(mainPanel);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel title = new JLabel("Module Feedback");
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setForeground(TEXT_MAIN);

        JLabel subtitle = new JLabel("Your insights help us improve the learning experience.");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitle.setForeground(SUBTITLE_TEXT);

        headerPanel.add(title, BorderLayout.NORTH);
        headerPanel.add(subtitle, BorderLayout.SOUTH);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(WHITE_CARD);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(148, 163, 184), 1),
                new EmptyBorder(25, 25, 25, 25)
        ));

        addStyledLabel(formPanel, "Select Module");
        moduleCombo = new JComboBox<>();
        moduleCombo.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        moduleCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        moduleCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        for (Module m : availableModules) moduleCombo.addItem(m.getName());
        moduleCombo.addActionListener(this);
        formPanel.add(moduleCombo);
        formPanel.add(Box.createVerticalStrut(20));

        addStyledLabel(formPanel, "Assigned Lecturer");
        lecturerLbl = new JLabel("---");
        lecturerLbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lecturerLbl.setForeground(NAVY_SLATE);
        lecturerLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(lecturerLbl);
        formPanel.add(Box.createVerticalStrut(20));

        addStyledLabel(formPanel, "Overall Rating (1-5)");
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        radioPanel.setOpaque(false);
        radioPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        ratingButtons = new JRadioButton[5];
        ratingGroup = new ButtonGroup();
        for (int i = 0; i < 5; i++) {
            ratingButtons[i] = new JRadioButton(String.valueOf(i + 1));
            ratingButtons[i].setBackground(WHITE_CARD);
            ratingButtons[i].setFont(new Font("Segoe UI", Font.PLAIN, 14));
            ratingButtons[i].setMargin(new Insets(0, 0, 0, 20));
            ratingGroup.add(ratingButtons[i]);
            radioPanel.add(ratingButtons[i]);
        }
        ratingButtons[4].setSelected(true);
        formPanel.add(radioPanel);
        formPanel.add(Box.createVerticalStrut(20));

        addStyledLabel(formPanel, "Additional Comments");
        commentArea = new JTextArea(6, 20);
        commentArea.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        commentArea.setLineWrap(true);
        commentArea.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(commentArea);
        scroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(203, 213, 225)));
        formPanel.add(scroll);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 20));
        btnPanel.setOpaque(false);

        backBtn = new JButton("Back");
        backBtn.setPreferredSize(new Dimension(100, 45));
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backBtn.addActionListener(this);

        submitBtn = new JButton("Submit Feedback");
        submitBtn.setPreferredSize(new Dimension(180, 45));
        submitBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        submitBtn.setBackground(NAVY_SLATE);
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setOpaque(true);
        submitBtn.setBorderPainted(false);
        submitBtn.setContentAreaFilled(true);
        submitBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        submitBtn.addActionListener(this);

        btnPanel.add(backBtn);
        btnPanel.add(submitBtn);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        if (!availableModules.isEmpty()) updateLecturer();
        setVisible(true);
    }

    private void addStyledLabel(JPanel panel, String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(SUBTITLE_TEXT);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lbl);
        panel.add(Box.createVerticalStrut(5));
    }

    private void updateLecturer() {
        int idx = moduleCombo.getSelectedIndex();
        if (idx != -1) {
            lecturerLbl.setText(availableModules.get(idx).getLecturer());
        }
    }

    private int getSelectedRating() {
        for (int i = 0; i < ratingButtons.length; i++) {
            if (ratingButtons[i].isSelected()) return i + 1;
        }
        return 5;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == moduleCombo) {
            updateLecturer();
        } else if (e.getSource() == submitBtn) {
            int idx = moduleCombo.getSelectedIndex();
            String comment = commentArea.getText().trim();
            if (idx != -1 && !comment.isEmpty()) {
                Module m = availableModules.get(idx);
                Feedback fb = new Feedback(Session.currentUsername, m.getName(), m.getLecturer(), comment, getSelectedRating(), "");
                CourseFileHandler.saveFeedback(fb);
                JOptionPane.showMessageDialog(this, "Thank you! Your feedback has been recorded.");
                dispose();
                new studentDashboard();
            } else {
                JOptionPane.showMessageDialog(this, "Please provide a comment before submitting.");
            }
        } else if (e.getSource() == backBtn) {
            dispose();
            new studentDashboard();
        }
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class studentFeedbackClass extends JFrame implements ActionListener {
    private JComboBox<String> courseCombo;
    private JLabel lecturerLbl;
    private JTextArea commentArea;
    private JButton submitBtn, backBtn;
    private List<Course> myCourses;

    // Using an array of Radio Buttons for the 1-5 scale
    private JRadioButton[] ratingButtons;
    private ButtonGroup ratingGroup;

    public studentFeedbackClass() {
        setTitle("Lecturer Feedback");
        setSize(600, 550);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        myCourses = CourseFileHandler.getRegisteredCoursesForStudent(Session.currentUsername);

        JLabel title = new JLabel("Lecturer Feedback");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBounds(200, 20, 250, 30);
        add(title);

        // 1. Course Selection
        addLabel("Select Course:", 50, 80);
        courseCombo = new JComboBox<>();
        for (Course c : myCourses) {
            courseCombo.addItem(c.getCode() + " - " + c.getName());
        }
        courseCombo.setBounds(180, 80, 350, 30);
        courseCombo.addActionListener(this);
        add(courseCombo);

        // 2. Rating Selection (Radio Buttons in a line)
        addLabel("Rating (1-5):", 50, 130);

        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        radioPanel.setBounds(175, 130, 350, 40);
        radioPanel.setOpaque(false);

        ratingButtons = new JRadioButton[5];
        ratingGroup = new ButtonGroup();

        for (int i = 0; i < 5; i++) {
            int score = i + 1;
            ratingButtons[i] = new JRadioButton(String.valueOf(score));
            ratingGroup.add(ratingButtons[i]); // Grouping for mutual exclusion
            radioPanel.add(ratingButtons[i]);
        }
        ratingButtons[4].setSelected(true); // Default to 5 stars
        add(radioPanel);

        // 3. Lecturer Display
        addLabel("Lecturer:", 50, 180);
        lecturerLbl = new JLabel("Select a course first");
        lecturerLbl.setBounds(180, 180, 350, 30);
        lecturerLbl.setFont(new Font("Arial", Font.BOLD, 14));
        add(lecturerLbl);

        // 4. Comment Box
        addLabel("Comment:", 50, 230);
        commentArea = new JTextArea();
        commentArea.setLineWrap(true);
        commentArea.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(commentArea);
        scroll.setBounds(180, 230, 350, 150);
        add(scroll);

        // Buttons
        submitBtn = new JButton("Submit Feedback");
        submitBtn.setBounds(180, 420, 150, 40);
        submitBtn.addActionListener(this);
        add(submitBtn);

        backBtn = new JButton("Back");
        backBtn.setBounds(340, 420, 100, 40);
        backBtn.addActionListener(this);
        add(backBtn);

        if (!myCourses.isEmpty()) updateLecturer();
        setVisible(true);
    }

    private void addLabel(String text, int x, int y) {
        JLabel lbl = new JLabel(text);
        lbl.setBounds(x, y, 150, 30);
        add(lbl);
    }

    private void updateLecturer() {
        int idx = courseCombo.getSelectedIndex();
        if (idx != -1) {
            lecturerLbl.setText(myCourses.get(idx).getLecturer());
        }
    }

    // Helper method to find which radio button is selected
    private int getSelectedRating() {
        for (int i = 0; i < ratingButtons.length; i++) {
            if (ratingButtons[i].isSelected()) {
                return i + 1;
            }
        }
        return 5; // Fallback
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == courseCombo) {
            updateLecturer();
        } else if (e.getSource() == submitBtn) {
            int idx = courseCombo.getSelectedIndex();
            String comment = commentArea.getText().trim();
            int rating = getSelectedRating();

            if (idx != -1 && !comment.isEmpty()) {
                Course c = myCourses.get(idx);
                Feedback fb = new Feedback(Session.currentUsername, c.getCode(), c.getLecturer(), comment, rating);
                CourseFileHandler.saveFeedback(fb);

                JOptionPane.showMessageDialog(this, "Feedback and " + rating + "-star rating submitted!");
                dispose();
                new studentDashboard();
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a comment before submitting.");
            }
        } else if (e.getSource() == backBtn) {
            dispose();
            new studentDashboard();
        }
    }
}
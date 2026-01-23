import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;

public class studentViewFeedbackClass extends JFrame {
    private JPanel cardsContainer;
    private JScrollPane scrollPane;
    private JButton backBtn;

    private final Color BACKGROUND = new Color(209, 213, 219);
    private final Color NAVY_SLATE = new Color(30, 41, 59);
    private final Color TEXT_MAIN = new Color(30, 41, 59);

    public studentViewFeedbackClass() {
        setTitle("My Feedback History");
        setSize(900, 700);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND);
        headerPanel.setBorder(new EmptyBorder(30, 50, 20, 50));

        JLabel title = new JLabel("My Past Feedback & Ratings");
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setForeground(TEXT_MAIN);
        headerPanel.add(title, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        cardsContainer = new JPanel();
        cardsContainer.setLayout(new BoxLayout(cardsContainer, BoxLayout.Y_AXIS));
        cardsContainer.setBackground(BACKGROUND);
        cardsContainer.setBorder(new EmptyBorder(10, 50, 10, 50));

        loadFeedbackCards();

        scrollPane = new JScrollPane(cardsContainer);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
        scrollPane.setBackground(BACKGROUND);
        scrollPane.getViewport().setBackground(BACKGROUND);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(BACKGROUND);
        bottomPanel.setBorder(new EmptyBorder(15, 50, 30, 50));

        backBtn = new JButton("Back to Dashboard");
        backBtn.setPreferredSize(new Dimension(200, 45));
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backBtn.setBackground(NAVY_SLATE);
        backBtn.setForeground(Color.WHITE);
        backBtn.setOpaque(true);
        backBtn.setBorderPainted(false);
        backBtn.setContentAreaFilled(true);
        backBtn.setFocusPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> {
            new studentDashboard();
            dispose();
        });

        bottomPanel.add(backBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void loadFeedbackCards() {
        List<Feedback> feedbackList = CourseFileHandler.getFeedbackForStudent(Session.currentUsername);

        if (feedbackList.isEmpty()) {
            JLabel emptyMsg = new JLabel("No feedback found. Your submitted feedback will appear here.");
            emptyMsg.setFont(new Font("Segoe UI", Font.ITALIC, 16));
            emptyMsg.setForeground(new Color(71, 85, 105));
            emptyMsg.setAlignmentX(Component.CENTER_ALIGNMENT);
            cardsContainer.add(Box.createVerticalGlue());
            cardsContainer.add(emptyMsg);
            cardsContainer.add(Box.createVerticalGlue());
        } else {
            for (Feedback fb : feedbackList) {
                cardsContainer.add(new FeedbackCard(fb));
                cardsContainer.add(Box.createRigidArea(new Dimension(0, 20)));
            }
        }
    }

    private class FeedbackCard extends JPanel {
        public FeedbackCard(Feedback fb) {
            setLayout(new BorderLayout(15, 10));
            setBackground(Color.WHITE);

            setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(Color.BLACK, 3),
                    new EmptyBorder(20, 25, 20, 25)
            ));

            setMaximumSize(new Dimension(800, 180));
            setAlignmentX(Component.CENTER_ALIGNMENT);

            JPanel topRow = new JPanel(new BorderLayout());
            topRow.setOpaque(false);

            JLabel moduleLbl = new JLabel(fb.getCourseCode());
            moduleLbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
            moduleLbl.setForeground(NAVY_SLATE);

            JLabel ratingLbl = new JLabel("Rating: " + fb.getRating() + "/5 ⭐");
            ratingLbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
            ratingLbl.setForeground(new Color(234, 179, 8));

            topRow.add(moduleLbl, BorderLayout.WEST);
            topRow.add(ratingLbl, BorderLayout.EAST);

            JLabel lecturerLbl = new JLabel("Lecturer: " + fb.getLecturerName());
            lecturerLbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            lecturerLbl.setForeground(new Color(100, 116, 139));

            JTextArea commentArea = new JTextArea(fb.getComment());
            commentArea.setFont(new Font("Segoe UI", Font.ITALIC, 15));
            commentArea.setLineWrap(true);
            commentArea.setWrapStyleWord(true);
            commentArea.setEditable(false);
            commentArea.setOpaque(false);
            commentArea.setForeground(new Color(51, 65, 85));

            JPanel contentPanel = new JPanel(new BorderLayout(0, 10));
            contentPanel.setOpaque(false);
            contentPanel.add(lecturerLbl, BorderLayout.NORTH);
            contentPanel.add(commentArea, BorderLayout.CENTER);

            add(topRow, BorderLayout.NORTH);
            add(contentPanel, BorderLayout.CENTER);
        }
    }
}

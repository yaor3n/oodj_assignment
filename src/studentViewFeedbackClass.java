import javax.swing.*;
import java.awt.*;
import java.util.List;

public class studentViewFeedbackClass extends JFrame {
    private JPanel cardsContainer;
    private JScrollPane scrollPane;
    private JButton backBtn;

    public studentViewFeedbackClass() {
        setTitle("My Feedback History");
        setSize(850, 600); // Increased height for better card viewing
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // --- Header Section ---
        JLabel title = new JLabel("My Past Feedback & Ratings", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setBorder(BorderFactory.createEmptyBorder(25, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        // --- Cards Container ---
        // We use BoxLayout on the Y_AXIS to stack cards vertically
        cardsContainer = new JPanel();
        cardsContainer.setLayout(new BoxLayout(cardsContainer, BoxLayout.Y_AXIS));
        cardsContainer.setBackground(new Color(245, 245, 245));
        cardsContainer.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));

        // Populate cards
        loadFeedbackCards();

        // Put cards in a ScrollPane
        scrollPane = new JScrollPane(cardsContainer);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Smooth scrolling
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        // --- Footer ---
        backBtn = new JButton("Back to Dashboard");
        backBtn.setPreferredSize(new Dimension(200, 45));
        backBtn.setFont(new Font("Arial", Font.BOLD, 14));
        backBtn.addActionListener(e -> {
            new studentDashboard();
            dispose();
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        bottomPanel.add(backBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void loadFeedbackCards() {
        List<Feedback> feedbackList = CourseFileHandler.getFeedbackForStudent(Session.currentUsername);

        if (feedbackList.isEmpty()) {
            JLabel emptyMsg = new JLabel("No feedback found. Start by submitting one!");
            emptyMsg.setAlignmentX(Component.CENTER_ALIGNMENT);
            cardsContainer.add(Box.createVerticalGlue());
            cardsContainer.add(emptyMsg);
            cardsContainer.add(Box.createVerticalGlue());
        } else {
            for (Feedback fb : feedbackList) {
                // Instantiate the Card Component (OOP Composition)
                cardsContainer.add(new FeedbackCard(fb));
                // Add a small gap between cards
                cardsContainer.add(Box.createRigidArea(new Dimension(0, 15)));
            }
        }
    }
}
import javax.swing.*;
import java.awt.*;

public class FeedbackCard extends JPanel {
    public FeedbackCard(Feedback fb) {
        setLayout(new BorderLayout(10, 5));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        setBackground(Color.WHITE);
        setMaximumSize(new Dimension(700, 150)); // Keeps cards uniform

        // Top: Course and Rating
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JLabel courseLbl = new JLabel(fb.getCourseCode() + " - " + fb.getLecturerName());
        courseLbl.setFont(new Font("Arial", Font.BOLD, 16));

        // Visual Star Rating String
        String stars = "⭐".repeat(fb.getRating());
        JLabel ratingLbl = new JLabel(stars);
        ratingLbl.setForeground(new Color(255, 165, 0));

        topPanel.add(courseLbl, BorderLayout.WEST);
        topPanel.add(ratingLbl, BorderLayout.EAST);

        // Center: Comment
        JTextArea commentArea = new JTextArea(fb.getComment());
        commentArea.setEditable(false);
        commentArea.setLineWrap(true);
        commentArea.setWrapStyleWord(true);
        commentArea.setFont(new Font("Arial", Font.ITALIC, 14));
        commentArea.setBackground(new Color(250, 250, 250));

        add(topPanel, BorderLayout.NORTH);
        add(commentArea, BorderLayout.CENTER);
    }
}
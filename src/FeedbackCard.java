import javax.swing.*;
import java.awt.*;

public class FeedbackCard extends JPanel {
    public FeedbackCard(Feedback fb) {
         final Color NAVY_SLATE = new Color(30, 41, 59);

        setLayout(new BorderLayout(15, 10));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        setBackground(Color.WHITE);
        setMaximumSize(new Dimension(800, 250)); // Keeps cards uniform

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

        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));
        centerPanel.setOpaque(false);
        centerPanel.add(commentArea, BorderLayout.NORTH);

        String replyText = fb.getReply();
        if (replyText != null && !replyText.trim().isEmpty() && !replyText.equalsIgnoreCase("null")) {
            JPanel replyPanel = new JPanel(new BorderLayout());
            replyPanel.setBackground(new Color(241, 245, 249)); // Light slate background
            replyPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 4, 0, 0, NAVY_SLATE), // Navy left accent line
                    new javax.swing.border.EmptyBorder(10, 15, 10, 15)
            ));

            JLabel replyHeader = new JLabel("Reply from Lecturer:");
            replyHeader.setFont(new Font("Segoe UI", Font.BOLD, 13));
            replyHeader.setForeground(NAVY_SLATE);

            JTextArea replyBody = new JTextArea(replyText);
            replyBody.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            replyBody.setLineWrap(true);
            replyBody.setWrapStyleWord(true);
            replyBody.setEditable(false);
            replyBody.setOpaque(false);

            replyPanel.add(replyHeader, BorderLayout.NORTH);
            replyPanel.add(replyBody, BorderLayout.CENTER);

            centerPanel.add(replyPanel, BorderLayout.CENTER);
        }

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }
}


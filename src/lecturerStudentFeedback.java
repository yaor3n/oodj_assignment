import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class lecturerStudentFeedback extends JPanel {
    private JPanel feedbackListPanel;
    private String lecturerModule;

    public lecturerStudentFeedback(String lecturerModule) {
        this.lecturerModule = lecturerModule;
        setLayout(new BorderLayout());
        setOpaque(false);

        JPanel cardHeader = new JPanel(new BorderLayout());
        cardHeader.setOpaque(false);
        cardHeader.setBorder(new EmptyBorder(15, 20, 10, 20));

        JLabel subTabLabel = new JLabel("Student Feedback");
        subTabLabel.setFont(new Font("Arial", Font.BOLD, 15));
        subTabLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)));

        cardHeader.add(subTabLabel, BorderLayout.WEST);

        feedbackListPanel = new JPanel();
        feedbackListPanel.setLayout(new BoxLayout(feedbackListPanel, BoxLayout.Y_AXIS));
        feedbackListPanel.setBackground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(feedbackListPanel);
        scroll.setBorder(new EmptyBorder(10, 15, 15, 15));
        scroll.getViewport().setBackground(Color.WHITE);

        add(cardHeader, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        refreshFeedbackList();
    }

    public void refreshFeedbackList() {
        feedbackListPanel.removeAll();
        List<String[]> feedbacks = new ArrayList<>();
        try (BufferedReader r = new BufferedReader(new FileReader("feedback.txt"))) {
            String line;
            while ((line = r.readLine()) != null) {
                String[] p = line.split(",", 5);
                if (p.length >= 5 && p[2].trim().equalsIgnoreCase(lecturerModule.trim())) {
                    feedbacks.add(p);
                }
            }
        } catch (IOException e) { }

        if (feedbacks.isEmpty()) {
            feedbackListPanel.add(Box.createVerticalGlue());
            JLabel empty = new JLabel("No Feedback Received", SwingConstants.CENTER);
            empty.setAlignmentX(Component.CENTER_ALIGNMENT);
            feedbackListPanel.add(empty);
            feedbackListPanel.add(Box.createVerticalGlue());
        } else {
            for (String[] f : feedbacks) {
                JPanel item = new JPanel(new BorderLayout());
                item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
                item.setBackground(Color.WHITE);
                item.setBorder(new EmptyBorder(5, 15, 5, 15));

                JLabel titleLbl = new JLabel(f[1] + " - " + f[3]);
                titleLbl.setFont(new Font("Arial", Font.PLAIN, 14));
                item.add(titleLbl, BorderLayout.WEST);

                JButton viewBtn = new JButton("View");
                viewBtn.setFocusPainted(false);
                viewBtn.addActionListener(e -> {
                    JOptionPane.showMessageDialog(this,
                        "Student: " + f[1] + "\nTitle: " + f[3] + "\n\nFeedback:\n" + f[4],
                        "Feedback Details", JOptionPane.INFORMATION_MESSAGE);
                });

                JPanel btnWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                btnWrapper.setOpaque(false);
                btnWrapper.add(viewBtn);
                item.add(btnWrapper, BorderLayout.EAST);

                feedbackListPanel.add(item);
                feedbackListPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
            }
        }
        feedbackListPanel.revalidate();
        feedbackListPanel.repaint();
    }
}

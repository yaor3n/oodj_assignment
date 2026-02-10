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

    try (BufferedReader r = new BufferedReader(new FileReader("student_feedback.txt"))) {
      String line;
      while ((line = r.readLine()) != null) {
        String[] p = line.split(",", -1);
        if (p.length >= 6 && p[1].trim().equalsIgnoreCase(lecturerModule.trim())) {
          feedbacks.add(p);
        }
      }
    } catch (IOException e) {
    }

    if (feedbacks.isEmpty()) {
      feedbackListPanel.add(Box.createVerticalGlue());
      JLabel empty = new JLabel("No Feedback Received", SwingConstants.CENTER);
      empty.setAlignmentX(Component.CENTER_ALIGNMENT);
      feedbackListPanel.add(empty);
      feedbackListPanel.add(Box.createVerticalGlue());
    } else {
      for (String[] f : feedbacks) {
        JPanel itemRow = new JPanel(new BorderLayout());
        itemRow.setBackground(Color.WHITE);
        itemRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        itemRow.setBorder(new EmptyBorder(10, 20, 10, 20));

        String starRating = f[3] + " ★";
        String studentComment = f[4];
        String lecturerReply = f[5];

        StringBuilder htmlContent = new StringBuilder(
            "<html><b>" + f[0] + "</b> (" + starRating + ")<br/>" + studentComment);

        if (!lecturerReply.isEmpty()) {
          htmlContent.append("<br/><div style='margin-top: 5px; color: #555555;'>")
              .append("➥ <b>Reply:</b> ")
              .append(lecturerReply)
              .append("</div>");
        }
        htmlContent.append("</html>");

        JLabel titleLbl = new JLabel(htmlContent.toString());
        titleLbl.setFont(new Font("Arial", Font.PLAIN, 13));
        itemRow.add(titleLbl, BorderLayout.CENTER);

        JPanel btnWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        btnWrapper.setOpaque(false);

        JButton replyBtn = new JButton(f[5].isEmpty() ? "Reply" : "Edit Reply");
        replyBtn.setFocusPainted(false);
        replyBtn.addActionListener(e -> openReplyDialog(f));

        btnWrapper.add(replyBtn);
        itemRow.add(btnWrapper, BorderLayout.EAST);

        feedbackListPanel.add(itemRow);
        feedbackListPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
      }

      feedbackListPanel.add(Box.createVerticalGlue());
    }
    feedbackListPanel.revalidate();
    feedbackListPanel.repaint();
  }

  private void openReplyDialog(String[] feedbackData) {
    JTextArea replyArea = new JTextArea(5, 20);
    replyArea.setText(feedbackData[5]);
    replyArea.setLineWrap(true);
    replyArea.setWrapStyleWord(true);
    JScrollPane scrollPane = new JScrollPane(replyArea);

    int result = JOptionPane.showConfirmDialog(this, scrollPane, "Reply to " + feedbackData[0],
        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

    if (result == JOptionPane.OK_OPTION) {
      String newResponse = replyArea.getText().trim().replace(",", ";");
      updateFeedbackFile(feedbackData, newResponse);
      refreshFeedbackList();
    }
  }

  private void updateFeedbackFile(String[] oldData, String response) {
    List<String> lines = new ArrayList<>();
    try (BufferedReader r = new BufferedReader(new FileReader("student_feedback.txt"))) {
      String line;
      while ((line = r.readLine()) != null) {
        String[] p = line.split(",", -1);
        if (p.length >= 6 && p[0].equals(oldData[0]) && p[1].equals(oldData[1]) && p[4].equals(oldData[4])) {
          line = p[0] + "," + p[1] + "," + p[2] + "," + p[3] + "," + p[4] + "," + response;
        }
        lines.add(line);
      }
    } catch (IOException e) {
    }

    try (PrintWriter w = new PrintWriter(new FileWriter("student_feedback.txt"))) {
      for (String l : lines) {
        w.println(l);
      }
    } catch (IOException e) {
    }
  }
}
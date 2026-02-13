import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class lecturerViewPastAssessment extends JPanel {

  private JPanel listPanel;
  private String selectedCourse;
  private String lecturerModule;

  public lecturerViewPastAssessment(String selectedCourse, String lecturerModule) {

    this.selectedCourse = selectedCourse;
    this.lecturerModule = lecturerModule;

    setLayout(new BorderLayout());
    setOpaque(false);

    JPanel cardHeader = new JPanel(new BorderLayout());
    cardHeader.setOpaque(false);
    cardHeader.setBorder(new EmptyBorder(15, 20, 10, 20));

    JLabel subTabLabel = new JLabel("Past Assessment");
    subTabLabel.setFont(new Font("Arial", Font.BOLD, 15));
    subTabLabel.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(Color.LIGHT_GRAY),
        BorderFactory.createEmptyBorder(5, 15, 5, 15)));

    cardHeader.add(subTabLabel, BorderLayout.WEST);

    add(cardHeader, BorderLayout.NORTH);

    listPanel = new JPanel();
    listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
    listPanel.setBackground(Color.WHITE);

    JScrollPane scroll = new JScrollPane(listPanel);
    scroll.setBorder(new EmptyBorder(10, 15, 15, 15));
    scroll.getViewport().setBackground(Color.WHITE);

    add(scroll, BorderLayout.CENTER);

    refreshPastAssessmentList();
  }

  private void refreshPastAssessmentList() {
    listPanel.removeAll();
    List<String[]> assessments = readAssessments(selectedCourse, lecturerModule);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate today = LocalDate.now();
    boolean found = false;

    for (String[] a : assessments) {
      try {
        LocalDate dueDate = LocalDate.parse(a[7].trim(), formatter);
        if (!dueDate.isBefore(today)) {
          continue;
        }
      } catch (DateTimeParseException e) {
        continue;
      }
      found = true;

      JPanel item = new JPanel(new BorderLayout());
      item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));
      item.setBackground(Color.WHITE);
      item.setBorder(new EmptyBorder(8, 15, 8, 15));

      JLabel titleLbl = new JLabel(a[5] + "  |  Due: " + a[7]);
      titleLbl.setFont(new Font("Arial", Font.PLAIN, 14));

      JButton viewBtn = new JButton("View");
      viewBtn.setFocusPainted(false);
      viewBtn.setBackground(new Color(240, 240, 240));
      viewBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

      viewBtn.addActionListener(e -> showPastAssessmentDialog(a));

      item.add(titleLbl, BorderLayout.WEST);
      item.add(viewBtn, BorderLayout.EAST);

      listPanel.add(item);
      listPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
    }

    if (!found) {
      listPanel.add(Box.createVerticalGlue());
      JLabel empty = new JLabel("No Past Assessments Found", SwingConstants.CENTER);
      empty.setAlignmentX(Component.CENTER_ALIGNMENT);
      listPanel.add(empty);
      listPanel.add(Box.createVerticalGlue());
    }

    listPanel.revalidate();
    listPanel.repaint();
  }

  private void showPastAssessmentDialog(String[] a) {

    JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
    mainPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

    JPanel detailsPanel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.gridx = 0;
    gbc.weightx = 1.0;

    gbc.gridy = 0;
    detailsPanel.add(new JLabel("Title: " + a[5]), gbc);

    gbc.gridy = 1;
    detailsPanel.add(new JLabel("Submission Date: " + a[7]), gbc);

    gbc.gridy = 2;
    detailsPanel.add(new JLabel("Type: " + a[8]), gbc);

    gbc.gridy = 3;
    detailsPanel.add(new JLabel("Description: " + a[9]), gbc);

    mainPanel.add(detailsPanel, BorderLayout.NORTH);

    // Student Results Table (same as Assessment view)
    String[] cols = { "Student ID", "Student Name", "Mark", "Grade" };
    javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(cols, 0);

    List<String[]> results = getExistingResults(a[5]);
    List<String[]> students = getEnrolledStudents(lecturerModule);

    for (String[] s : students) {
      String mark = "0";
      String grade = "-";

      for (String[] res : results) {
        if (res[0].trim().equals(s[0].trim())) {
          mark = res[4].trim();
          grade = res[5].trim();
          break;
        }
      }

      model.addRow(new Object[] { s[0], s[1], mark, grade });
    }

    JTable table = new JTable(model);
    mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);

    mainPanel.setPreferredSize(new Dimension(500, 500));

    JOptionPane.showConfirmDialog(
        this,
        mainPanel,
        "Assessment Sheet",
        JOptionPane.DEFAULT_OPTION);
  }

  private List<String[]> getEnrolledStudents(String moduleName) {
    List<String[]> students = new ArrayList<>();
    try (BufferedReader r = new BufferedReader(new FileReader("student_courses.txt"))) {
      String line;
      while ((line = r.readLine()) != null) {
        String[] p = line.split(",", 3);
        if (p.length == 3 && p[2].trim().equalsIgnoreCase(moduleName.trim()))
          students.add(p);
      }
    } catch (IOException e) {
    }
    return students;
  }

  private List<String[]> getExistingResults(String assessmentTitle) {
    List<String[]> results = new ArrayList<>();
    File file = new File("Grades.txt");

    if (file.exists()) {
      try (BufferedReader r = new BufferedReader(new FileReader(file))) {
        String line;
        while ((line = r.readLine()) != null) {
          String[] p = line.split(",");
          if (p.length >= 6 &&
              p[3].trim().equalsIgnoreCase(assessmentTitle.trim())) {
            results.add(p);
          }
        }
      } catch (IOException e) {
      }
    }
    return results;
  }

  private List<String[]> readAssessments(String course, String module) {
    List<String[]> list = new ArrayList<>();
    try (BufferedReader r = new BufferedReader(new FileReader("assessment.txt"))) {
      String line;
      while ((line = r.readLine()) != null) {
        if (line.startsWith("#"))
          continue;
        String[] p = line.split(",", 10);
        if (p.length == 10 &&
            p[3].trim().equals(course.trim()) &&
            p[4].trim().equals(module.trim())) {
          list.add(p);
        }
      }
    } catch (IOException e) {
    }
    return list;
  }
}

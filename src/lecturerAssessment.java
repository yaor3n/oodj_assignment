import javax.swing.border.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class lecturerAssessment extends JPanel {
  private JPanel listPanel;
  private JTextField searchField;
  private String selectedCourse;
  private String lecturerModule;
  private String lecturerID;
  private String lecturerName;
  private String username;

  String[] types = {
      "Group Assignment",
      "Exam",
      "Mock Test",
      "Individual Assignment"
  };
  JTextField titleField = null;
  JTextField dateField = null;
  JComboBox<String> typeCombo = null;
  JTextArea descArea = null;

  public lecturerAssessment(String selectedCourse, String lecturerModule, String lecturerID, String lecturerName,
      String username) {
    this.selectedCourse = selectedCourse;
    this.lecturerModule = lecturerModule;
    this.lecturerID = lecturerID;
    this.lecturerName = lecturerName;
    this.username = username;

    setLayout(new BorderLayout());
    setOpaque(false);

    JPanel cardHeader = new JPanel(new BorderLayout(15, 0));
    cardHeader.setOpaque(false);
    cardHeader.setBorder(new EmptyBorder(15, 20, 10, 20));

    JLabel subTabLabel = new JLabel("Assessment");
    subTabLabel.setFont(new Font("Arial", Font.BOLD, 15));
    subTabLabel.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(Color.LIGHT_GRAY),
        BorderFactory.createEmptyBorder(5, 15, 5, 15)));

    searchField = new JTextField();
    searchField.setPreferredSize(new Dimension(200, 30));
    searchField.setToolTipText("Search assessment title...");
    searchField.getDocument().addDocumentListener(new DocumentListener() {
      public void insertUpdate(DocumentEvent e) {
        filter();
      }

      public void removeUpdate(DocumentEvent e) {
        filter();
      }

      public void changedUpdate(DocumentEvent e) {
        filter();
      }

      private void filter() {
        refreshAssessmentList();
      }
    });

    JButton addBtn = new JButton("+");
    addBtn.setPreferredSize(new Dimension(50, 30));
    addBtn.setFocusPainted(false);
    addBtn.addActionListener(e -> showCreateAssessmentDialog());

    JPanel rightActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
    rightActions.setOpaque(false);
    rightActions.add(new JLabel("🔍"));
    rightActions.add(searchField);
    rightActions.add(addBtn);

    cardHeader.add(subTabLabel, BorderLayout.WEST);
    cardHeader.add(rightActions, BorderLayout.EAST);

    listPanel = new JPanel();
    listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
    listPanel.setBackground(Color.WHITE);

    JScrollPane scroll = new JScrollPane(listPanel);
    scroll.setBorder(new EmptyBorder(10, 15, 15, 15));
    scroll.getViewport().setBackground(Color.WHITE);

    add(cardHeader, BorderLayout.NORTH);
    add(scroll, BorderLayout.CENTER);

    refreshAssessmentList();
  }

  public void refreshAssessmentList() {
    listPanel.removeAll();
    String query = searchField != null ? searchField.getText().toLowerCase().trim() : "";
    List<String[]> assessments = readAssessments(selectedCourse, lecturerModule);

    boolean found = false;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate today = LocalDate.now();

    for (String[] a : assessments) {
      try {
        LocalDate dueDate = LocalDate.parse(a[7].trim(), formatter);
        if (dueDate.isBefore(today)) {
          continue;
        }
      } catch (DateTimeParseException e) {
        continue;
      }
      if (!query.isEmpty() && !a[5].toLowerCase().contains(query)) {
        continue;
      }
      found = true;

      JPanel item = new JPanel(new BorderLayout());
      item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
      item.setBackground(Color.WHITE);
      item.setBorder(new EmptyBorder(5, 15, 5, 15));

      JLabel titleLbl = new JLabel(a[5]);
      titleLbl.setFont(new Font("Arial", Font.PLAIN, 14));
      item.add(titleLbl, BorderLayout.WEST);

      JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 0));
      buttonPanel.setOpaque(false);
      buttonPanel.setPreferredSize(new Dimension(360, 30));

      JButton view = new JButton("View");
      JButton edit = new JButton("Edit");
      JButton delete = new JButton("Delete");
      JButton mark = new JButton("Mark");

      JButton[] actionBtns = { view, edit, delete, mark };
      for (JButton b : actionBtns) {
        b.setFocusPainted(false);
        buttonPanel.add(b);
      }

      view.addActionListener(e -> showAssessmentDialog(a, false));
      edit.addActionListener(e -> showAssessmentDialog(a, true));
      delete.addActionListener(e -> deleteAssessment(a[0]));
      mark.addActionListener(e -> showMarkDialog(lecturerModule, a));

      item.add(buttonPanel, BorderLayout.EAST);
      listPanel.add(item);
      listPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
    }

    if (!found) {
      listPanel.add(Box.createVerticalGlue());
      JLabel empty = new JLabel(query.isEmpty() ? "No Assessments Found" : "No matches for '" + query + "'",
          SwingConstants.CENTER);
      empty.setAlignmentX(Component.CENTER_ALIGNMENT);
      listPanel.add(empty);
      listPanel.add(Box.createVerticalGlue());
    }

    listPanel.revalidate();
    listPanel.repaint();
  }

  private List<String[]> readAssessments(String course, String module) {
    List<String[]> list = new ArrayList<>();
    try (BufferedReader r = new BufferedReader(new FileReader("assessment.txt"))) {
      String line;
      while ((line = r.readLine()) != null) {
        if (line.startsWith("#"))
          continue;
        String[] p = line.split(",", 10);
        if (p.length == 10 && p[3].trim().equals(course.trim()) && p[4].trim().equals(module.trim())) {
          list.add(p);
        }
      }
    } catch (IOException e) {
    }
    return list;
  }

  private void showCreateAssessmentDialog() {
    JTextField title = new JTextField();
    JTextArea desc = new JTextArea(5, 20);
    JComboBox<String> typeCombo = new JComboBox<>(types);
    typeCombo.setSelectedIndex(0);

    SpinnerDateModel dateModel = new SpinnerDateModel();
    JSpinner dateSpinner = new JSpinner(dateModel);
    JSpinner.DateEditor editor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
    dateSpinner.setEditor(editor);

    Object[] form = {
        "Title", title,
        "Submission Date (yyyy-MM-dd)", dateSpinner,
        "Type", typeCombo,
        "Description", new JScrollPane(desc)
    };

    while (true) {
      int result = JOptionPane.showConfirmDialog(
          this,
          form,
          "New Assessment",
          JOptionPane.OK_CANCEL_OPTION);
      if (result != JOptionPane.OK_OPTION) {
        return;
      }

      if (title.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Title cannot be empty.");
        continue;
      }

      LocalDate enteredDate = null;
      try {
        Date selectedDate = (Date) dateSpinner.getValue();
        enteredDate = selectedDate
            .toInstant()
            .atZone(java.time.ZoneId.systemDefault())
            .toLocalDate();
        LocalDate today = LocalDate.now();
        if (enteredDate.isBefore(today)) {
          JOptionPane.showMessageDialog(this,
              "Invalid Date");
          continue;
        }
      } catch (DateTimeParseException e) {
        JOptionPane.showMessageDialog(this,
            "Invalid date format. Use yyyy-MM-dd.");
        continue;
      }

      writeAssessment(
          title.getText().trim(),
          enteredDate.toString(),
          typeCombo.getSelectedItem().toString(),
          desc.getText().trim());

      refreshAssessmentList();
      JOptionPane.showMessageDialog(this, "Assessment created successfully!");
      break;
    }
  }

  private void writeAssessment(String title, String date, String type, String desc) {
    String id = generateAssessmentId();
    String record = String.join(",", id, lecturerID, username, selectedCourse, lecturerModule,
        title, username, date, type, desc.replace(",", " "));
    try (BufferedWriter w = new BufferedWriter(new FileWriter("assessment.txt", true))) {
      w.write(record);
      w.newLine();
    } catch (IOException e) {
    }
  }

  private String generateAssessmentId() {
    int max = 0;
    try (BufferedReader r = new BufferedReader(new FileReader("assessment.txt"))) {
      String line;
      while ((line = r.readLine()) != null) {
        if (line.startsWith("ASS")) {
          try {
            int n = Integer.parseInt(line.substring(3, 6));
            max = Math.max(max, n);
          } catch (Exception e) {
          }
        }
      }
    } catch (IOException e) {
    }
    return String.format("ASS%03d", max + 1);
  }

  private void showAssessmentDialog(String[] a, boolean editable) {
    JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
    mainPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
    JPanel detailsPanel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.gridx = 0;
    gbc.weightx = 1.0;

    if (!editable) {
      gbc.gridy = 0;
      detailsPanel.add(new JLabel("Title: " + a[5]), gbc);
      gbc.gridy = 1;
      detailsPanel.add(new JLabel("Submission Date: " + a[7]), gbc);
      gbc.gridy = 2;
      detailsPanel.add(new JLabel("Type: " + a[8]), gbc);
      gbc.gridy = 3;
      detailsPanel.add(new JLabel("Description: " + a[9]), gbc);
    } else {
      titleField = new JTextField(a[5], 20);
      dateField = new JTextField(a[7], 20);
      typeCombo = new JComboBox<>(types);
      typeCombo.setSelectedItem(a[8]);
      descArea = new JTextArea(a[9], 4, 20);
      descArea.setLineWrap(true);
      descArea.setWrapStyleWord(true);

      gbc.gridy = 0;
      detailsPanel.add(new JLabel("Title:"), gbc);
      gbc.gridy = 1;
      detailsPanel.add(titleField, gbc);

      gbc.gridy = 2;
      detailsPanel.add(new JLabel("Submission Date:"), gbc);
      gbc.gridy = 3;
      detailsPanel.add(dateField, gbc);

      gbc.gridy = 4;
      detailsPanel.add(new JLabel("Type:"), gbc);
      gbc.gridy = 5;
      detailsPanel.add(typeCombo, gbc);

      gbc.gridy = 6;
      detailsPanel.add(new JLabel("Description:"), gbc);
      gbc.gridy = 7;
      detailsPanel.add(new JScrollPane(descArea), gbc);
    }

    mainPanel.add(detailsPanel, BorderLayout.NORTH);

    if (!editable) {
      String[] cols = { "Student Name", "Module Name", "Mark", "Grade" };
      DefaultTableModel model = new DefaultTableModel(cols, 0);
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
    } else {
      mainPanel.setPreferredSize(new Dimension(400, 450));
    }

    int result = JOptionPane.showConfirmDialog(this, mainPanel, editable ? "Edit Assessment" : "Assessment Sheet",
        editable ? JOptionPane.OK_CANCEL_OPTION : JOptionPane.DEFAULT_OPTION);

    if (editable && result == JOptionPane.OK_OPTION) {
      a[5] = titleField.getText();
      a[7] = dateField.getText();
      a[8] = typeCombo.getSelectedItem().toString();
      a[9] = descArea.getText().replace(",", " ");

      updateAssessment(a);
      refreshAssessmentList();
    }
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

  private void updateAssessment(String[] updated) {
    List<String> lines = new ArrayList<>();
    try (BufferedReader r = new BufferedReader(new FileReader("assessment.txt"))) {
      String line;
      while ((line = r.readLine()) != null) {
        if (line.startsWith(updated[0] + ","))  
          lines.add(String.join(",", updated));
        else
          lines.add(line);
      }
    } catch (IOException e) {
      return;
    }
    try (BufferedWriter w = new BufferedWriter(new FileWriter("assessment.txt"))) {
      for (String l : lines) {
        w.write(l);
        w.newLine();
      }
    } catch (IOException e) {
    }
  }

  private void deleteAssessment(String id) {
    if (JOptionPane.showConfirmDialog(this, "Delete?", "Confirm", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION)
      return;
    List<String> lines = new ArrayList<>();
    try (BufferedReader r = new BufferedReader(new FileReader("assessment.txt"))) {
      String line;
      while ((line = r.readLine()) != null) {
        if (!line.startsWith(id + ","))
          lines.add(line);
      }
    } catch (IOException e) {
      return;
    }
    try (BufferedWriter w = new BufferedWriter(new FileWriter("assessment.txt"))) {
      for (String l : lines) {
        w.write(l);
        w.newLine();
      }
    } catch (IOException e) {
    }
    refreshAssessmentList();
  }

  private void showMarkDialog(String moduleName, String[] assessment) {
    List<String[]> students = getEnrolledStudents(moduleName);

    if (students.isEmpty()) {
      JOptionPane.showMessageDialog(this, "No students enrolled.");
      return;
    }
    String assessmentTitle = assessment[5].trim();
    List<String[]> existingResults = getExistingResults(assessmentTitle);

    while (true) {
      JPanel marksPanel = new JPanel(new GridLayout(0, 2, 10, 5));
      JTextField[] markFields = new JTextField[students.size()];
      for (int i = 0; i < students.size(); i++) {
        marksPanel.add(new JLabel(
            students.get(i)[1] + " (" + students.get(i)[0] + ")"));
        String currentMark = "";
        for (String[] res : existingResults) {
          if (res[0].trim().equals(students.get(i)[0].trim())) {
            currentMark = res[4].trim();
            break;
          }
        }

        markFields[i] = new JTextField(currentMark);
        marksPanel.add(markFields[i]);
      }

      int result = JOptionPane.showConfirmDialog(
          this,
          new JScrollPane(marksPanel),
          "Enter Marks (0-100)",
          JOptionPane.OK_CANCEL_OPTION);
      if (result != JOptionPane.OK_OPTION) {
        return;
      }

      boolean valid = true;

      for (int i = 0; i < students.size(); i++) {
        String markVal = markFields[i].getText().trim();
        if (markVal.isEmpty())
          markVal = "0";
        try {
          int score = Integer.parseInt(markVal);
          if (score < 0 || score > 100) {
            JOptionPane.showMessageDialog(this,
                "Mark for " + students.get(i)[1]
                    + " must be between 0 and 100.");
            valid = false;
            break;
          }
        } catch (NumberFormatException e) {
          JOptionPane.showMessageDialog(this,
              "Invalid number for "
                  + students.get(i)[1]);
          valid = false;
          break;
        }
      }

      if (!valid) {
        continue;
      }

      saveMarks(students, markFields, assessmentTitle, moduleName);
      break;
    }
  }

  private void saveMarks(List<String[]> students, JTextField[] markFields,
      String assessmentTitle, String moduleName) {

    List<String[]> allData = new ArrayList<>();
    File file = new File("Grades.txt");

    if (file.exists()) {
      try (BufferedReader r = new BufferedReader(new FileReader(file))) {
        String line;
        while ((line = r.readLine()) != null)
          allData.add(line.split(","));
      } catch (IOException e) {
      }
    }

    for (int i = 0; i < students.size(); i++) {
      String markVal = markFields[i].getText().trim();
      if (markVal.isEmpty())
        markVal = "0";

      int score = Integer.parseInt(markVal);
      if (score < 0 || score > 100) {
        JOptionPane.showMessageDialog(this,
            "Mark for " + students.get(i)[1] + " must be between 0 and 100.");
        return;
      }

      String grade = GradeCalculationHandler.calculateGrade(score);

      boolean updated = false;

      for (String[] row : allData) {
        if (row.length >= 6 &&
            row[0].trim().equals(students.get(i)[0].trim()) &&
            row[3].trim().equalsIgnoreCase(assessmentTitle)) {

          row[4] = markVal;
          row[5] = grade;
          updated = true;
          break;
        }
      }

      if (!updated) {
        allData.add(new String[] {
            students.get(i)[0].trim(),
            students.get(i)[1].trim(),
            lecturerName,
            assessmentTitle,
            markVal,
            grade
        });
      }
    }

    try (BufferedWriter w = new BufferedWriter(new FileWriter(file))) {
      for (String[] row : allData) {
        w.write(String.join(",", row));
        w.newLine();
      }
      JOptionPane.showMessageDialog(this, "Marks saved successfully!");
    } catch (IOException e) {
    }
  }
}

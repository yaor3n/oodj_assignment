import javax.swing.*;
import java.awt.*;
import java.io.*;

public class CreateModule extends JFrame {

  private JTextField codeField;
  private JTextField nameField;
  private JTextField qualificationField;
  private JTextField lecturerField;
  private JTextArea descriptionArea;
  private String imagePath = "null";
  private JLabel imagePreview;

  public CreateModule() {
    setTitle("Create New Module");
    setSize(900, 650);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setLayout(new BorderLayout());

    JPanel header = new JPanel(new BorderLayout());
    header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

    JLabel title = new JLabel("Create New Module");
    title.setFont(new Font("Arial", Font.BOLD, 22));

    JButton backBtn = new JButton("Back");
    backBtn.addActionListener(e -> dispose());

    header.add(backBtn, BorderLayout.WEST);
    header.add(title, BorderLayout.CENTER);
    add(header, BorderLayout.NORTH);

    JPanel mainPanel = new JPanel(new BorderLayout());
    mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

    JPanel leftPanel = new JPanel();
    leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
    leftPanel.setPreferredSize(new Dimension(250, 0));
    leftPanel.setBorder(BorderFactory.createTitledBorder("Module Image"));

    JButton uploadBtn = new JButton("Select Image");
    uploadBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
    uploadBtn.addActionListener(e -> uploadImage());

    imagePreview = new JLabel("No Image Selected");
    imagePreview.setPreferredSize(new Dimension(400, 400));
    imagePreview.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    imagePreview.setHorizontalAlignment(SwingConstants.CENTER);
    imagePreview.setVerticalAlignment(SwingConstants.CENTER);
    imagePreview.setAlignmentX(Component.CENTER_ALIGNMENT);

    leftPanel.add(uploadBtn);
    leftPanel.add(Box.createVerticalStrut(20));
    leftPanel.add(imagePreview);

    JPanel rightPanel = new JPanel();
    rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
    rightPanel.setBorder(BorderFactory.createTitledBorder("Module Details"));

    codeField = new JTextField();
    nameField = new JTextField();
    qualificationField = new JTextField();
    lecturerField = new JTextField();
    descriptionArea = new JTextArea(5, 20);
    descriptionArea.setLineWrap(true);
    descriptionArea.setWrapStyleWord(true);

    JButton createBtn = new JButton("Create Module");
    createBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
    createBtn.addActionListener(e -> createModule());

    rightPanel.add(createField("Module Code", codeField));
    rightPanel.add(createField("Module Name", nameField));
    rightPanel.add(createField("Qualification", qualificationField));
    rightPanel.add(createField("Lecturer", lecturerField));
    rightPanel.add(Box.createVerticalStrut(10));
    rightPanel.add(new JLabel("Module Description"));
    rightPanel.add(new JScrollPane(descriptionArea));
    rightPanel.add(Box.createVerticalStrut(20));
    rightPanel.add(createBtn);

    mainPanel.add(leftPanel, BorderLayout.WEST);
    mainPanel.add(rightPanel, BorderLayout.CENTER);

    add(mainPanel, BorderLayout.CENTER);
    setVisible(true);
  }

  private JPanel createField(String label, JTextField field) {
    JPanel panel = new JPanel(new BorderLayout(5, 5));
    panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
    panel.add(new JLabel(label), BorderLayout.NORTH);
    panel.add(field, BorderLayout.CENTER);
    return panel;
  }

  private void uploadImage() {
    JFileChooser chooser = new JFileChooser();
    int result = chooser.showOpenDialog(this);

    if (result == JFileChooser.APPROVE_OPTION) {
      imagePath = chooser.getSelectedFile().getAbsolutePath();
      ImageIcon icon = new ImageIcon(imagePath);
      Image img = icon.getImage();

      int labelWidth = imagePreview.getPreferredSize().width;
      int labelHeight = imagePreview.getPreferredSize().height;

      double imgRatio = (double) img.getWidth(null) / img.getHeight(null);
      double labelRatio = (double) labelWidth / labelHeight;

      int newWidth, newHeight;

      if (imgRatio > labelRatio) {
        newWidth = labelWidth;
        newHeight = (int) (labelWidth / imgRatio);
      } else {
        newHeight = labelHeight;
        newWidth = (int) (labelHeight * imgRatio);
      }

      Image scaled = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
      imagePreview.setIcon(new ImageIcon(scaled));
      imagePreview.setText("");
      JOptionPane.showMessageDialog(this, "Image selected");
    }
  }

  private void createModule() {
    if (codeField.getText().isEmpty() || nameField.getText().isEmpty()) {
      JOptionPane.showMessageDialog(this, "Module Code and Name are required");
      return;
    }

    try {
      File dir = new File("src/Module");
      if (!dir.exists())
        dir.mkdir();

      File file = new File("src/Module/" + codeField.getText().trim() + ".txt");

      try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
        writer.write("# Module Code, Module Name, Qualification, Lecturer, Description, Image Path");
        writer.newLine();
        writer.write(codeField.getText().trim() + ",");
        writer.write(nameField.getText().trim() + ",");
        writer.write(qualificationField.getText().trim() + ",");
        writer.write(lecturerField.getText().trim() + ",");
        writer.write(descriptionArea.getText().trim() + ",");
        writer.write(imagePath == null || imagePath.isEmpty() ? "null" : imagePath);
      }

      JOptionPane.showMessageDialog(this, "Module created successfully");
      dispose();

    } catch (IOException e) {
      JOptionPane.showMessageDialog(this, "Failed to save module");
    }
  }
}

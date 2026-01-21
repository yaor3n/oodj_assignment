import javax.swing.*;
import java.awt.*;
import java.io.*;

public class EditModule extends JFrame {
  private JTextField codeField;
  private JTextField nameField;
  private JTextField qualificationField;
  private JTextField lecturerField;
  private JTextArea descriptionArea;
  private String imagePath = "null";
  private File moduleFile;
  private JLabel imagePreview;

  public EditModule(File fileToEdit) {
    this.moduleFile = fileToEdit;
    setTitle("Edit Module");
    setSize(900, 650);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setLayout(new BorderLayout());

    JPanel header = new JPanel(new BorderLayout());
    header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    JLabel title = new JLabel("Edit Module");
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
    leftPanel.setBorder(BorderFactory.createTitledBorder("Module Photo"));

    JButton uploadBtn = new JButton("Select Photo");
    uploadBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

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

    JButton saveBtn = new JButton("Save Changes");
    saveBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

    rightPanel.add(createField("Module Code", codeField));
    rightPanel.add(createField("Module Name", nameField));
    rightPanel.add(createField("Qualification", qualificationField));
    rightPanel.add(createField("Lecturer", lecturerField));
    rightPanel.add(Box.createVerticalStrut(10));
    rightPanel.add(new JLabel("Module Description"));
    rightPanel.add(new JScrollPane(descriptionArea));
    rightPanel.add(Box.createVerticalStrut(20));
    rightPanel.add(saveBtn);

    mainPanel.add(leftPanel, BorderLayout.WEST);
    mainPanel.add(rightPanel, BorderLayout.CENTER);

    add(mainPanel, BorderLayout.CENTER);

    uploadBtn.addActionListener(e -> uploadImage());
    saveBtn.addActionListener(e -> saveModule());

    loadModuleData();
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

  private void loadModuleData() {
    try (BufferedReader reader = new BufferedReader(new FileReader(moduleFile))) {
      String line;
      while ((line = reader.readLine()) != null) {
        line = line.trim();
        if (line.isEmpty() || line.startsWith("#"))
          continue;
        String[] parts = line.split(",", 6);
        if (parts.length >= 6) {
          codeField.setText(parts[0].trim());
          nameField.setText(parts[1].trim());
          qualificationField.setText(parts[2].trim());
          lecturerField.setText(parts[3].trim());
          descriptionArea.setText(parts[4].trim());
          imagePath = parts[5].trim();
          if (!imagePath.equals("null") && !imagePath.isEmpty())
            uploadImagePreview(imagePath);
          break;
        }
      }
    } catch (IOException e) {
      JOptionPane.showMessageDialog(this, "Failed to load module data");
    }
  }

  private void uploadImagePreview(String path) {
    ImageIcon icon = new ImageIcon(path);
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
  }

  private void saveModule() {
    if (codeField.getText().isEmpty() || nameField.getText().isEmpty()) {
      JOptionPane.showMessageDialog(this, "Module Code and Name are required");
      return;
    }

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(moduleFile))) {
      writer.write("# Module Code, Module Name, Qualification, Lecturer, Description, Image Path");
      writer.newLine();
      writer.write(codeField.getText().trim() + ",");
      writer.write(nameField.getText().trim() + ",");
      writer.write(qualificationField.getText().trim() + ",");
      writer.write(lecturerField.getText().trim() + ",");
      writer.write(descriptionArea.getText().trim() + ",");
      writer.write(imagePath == null || imagePath.isEmpty() ? "null" : imagePath);
    } catch (IOException e) {
      JOptionPane.showMessageDialog(this, "Failed to save module");
      return;
    }

    JOptionPane.showMessageDialog(this, "Module updated successfully");
    dispose();
  }
}

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import utils.NicerButton;

public class lecturerEditProfile extends JFrame {

  private JTextField usernameField;
  private JPasswordField passwordField;
  private JTextField roleField;
  private JTextField displayNameField;
  private JCheckBox showPasswordBox;
  private NicerButton editBtn, saveBtn, backBtn;

  private String username;
  private String currentPassword;
  private String currentDisplayName;

  Color colorBackground = new Color(255, 255, 255);
  Color colorSidebar = new Color(241, 245, 249);
  Color darkNavy = new Color(30, 41, 59);
  Color hoverNavy = new Color(51, 65, 85);
  Color googleBlue = new Color(66, 133, 244);

  public lecturerEditProfile(String username) {
    this.username = username;
    loadLecturerData();

    setLayout(null);
    setTitle("Edit Profile - " + username);
    setSize(1024, 750);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    getContentPane().setBackground(colorBackground);

    JLabel title = new JLabel("Profile");
    title.setFont(new Font("Arial", Font.BOLD, 28));
    title.setBounds(60, 40, 300, 50);
    add(title);

    RoundedBackgroundPanel bgPanel = new RoundedBackgroundPanel(colorSidebar, 30);
    bgPanel.setBounds(60, 110, 900, 500);
    bgPanel.setLayout(null);
    add(bgPanel);

    AvatarPanel avatar = new AvatarPanel(currentDisplayName != null ? currentDisplayName : username);
    avatar.setBounds(50, 50, 150, 150);
    bgPanel.add(avatar);

    createField(bgPanel, "Display Name:", displayNameField = new JTextField(currentDisplayName), 100);
    createField(bgPanel, "Username:", usernameField = new JTextField(username), 170);
    createField(bgPanel, "Password:", passwordField = new JPasswordField(currentPassword), 240);
    createField(bgPanel, "Role:", roleField = new JTextField("Lecturer"), 310);

    showPasswordBox = new JCheckBox("Show Password");
    showPasswordBox.setBounds(760, 240, 130, 40);
    showPasswordBox.setOpaque(false);
    showPasswordBox.setVisible(false);
    showPasswordBox.addActionListener(e -> {
      if (showPasswordBox.isSelected()) {
        passwordField.setEchoChar((char) 0);
      } else {
        passwordField.setEchoChar('•');
      }
    });
    bgPanel.add(showPasswordBox);

    displayNameField.setEditable(false);
    usernameField.setEditable(false);
    passwordField.setEditable(false);
    roleField.setEditable(false);

    editBtn = new NicerButton("Edit", darkNavy, hoverNavy, 15);
    editBtn.setBounds(300, 400, 120, 45);
    editBtn.addActionListener(e -> toggleEdit(true));
    bgPanel.add(editBtn);

    saveBtn = new NicerButton("Save", googleBlue, new Color(50, 110, 210), 15);
    saveBtn.setBounds(440, 400, 120, 45);
    saveBtn.setEnabled(false);
    saveBtn.addActionListener(e -> saveProfile());
    bgPanel.add(saveBtn);

    backBtn = new NicerButton("Back", Color.GRAY, Color.DARK_GRAY, 15);
    backBtn.setBounds(580, 400, 120, 45);
    backBtn.addActionListener(e -> {
      dispose();
    });
    bgPanel.add(backBtn);

    setVisible(true);
  }

  private void createField(JPanel p, String labelText, JTextField field, int y) {
    JLabel lbl = new JLabel(labelText);
    lbl.setBounds(300, y - 25, 200, 25);
    lbl.setFont(new Font("Arial", Font.BOLD, 12));
    p.add(lbl);

    field.setBounds(300, y, 450, 40);
    field.setFont(new Font("Arial", Font.PLAIN, 14));
    field.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(200, 200, 200)),
        BorderFactory.createEmptyBorder(5, 10, 5, 10)));
    p.add(field);
  }

  private void toggleEdit(boolean editing) {
    displayNameField.setEditable(editing);
    passwordField.setEditable(editing);
    saveBtn.setEnabled(editing);
    editBtn.setEnabled(!editing);
    showPasswordBox.setVisible(editing);

    if (!editing) {
      showPasswordBox.setSelected(false);
      passwordField.setEchoChar('•');
    }
  }

  private void loadLecturerData() {
    try (BufferedReader r = new BufferedReader(new FileReader("accounts.txt"))) {
      String line;
      while ((line = r.readLine()) != null) {
        String[] p = line.split(",");
        if (p.length >= 10 && p[7].equals(username)) {
          currentDisplayName = p[1];
          currentPassword = p[8];
          return;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void saveProfile() {
    String newName = displayNameField.getText();
    String newPass = new String(passwordField.getPassword());
    List<String> lines = new ArrayList<>();

    try (BufferedReader r = new BufferedReader(new FileReader("accounts.txt"))) {
      String line;
      while ((line = r.readLine()) != null) {
        String[] p = line.split(",");
        if (p.length >= 10 && p[7].equals(username)) {
          p[1] = newName;
          p[8] = newPass;
          line = String.join(",", p);
        }
        lines.add(line);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    try (PrintWriter w = new PrintWriter(new FileWriter("accounts.txt"))) {
      for (String l : lines)
        w.println(l);
      JOptionPane.showMessageDialog(this, "Profile updated successfully!");
      toggleEdit(false);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  class RoundedBackgroundPanel extends JPanel {
    private Color bgColor;
    private int radius;

    RoundedBackgroundPanel(Color bgColor, int radius) {
      this.bgColor = bgColor;
      this.radius = radius;
      setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
      Graphics2D g2 = (Graphics2D) g;
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      g2.setColor(bgColor);
      g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
    }
  }

  class AvatarPanel extends JPanel {
    private String initial;

    AvatarPanel(String name) {
      this.initial = (name == null || name.isEmpty()) ? "?" : name.substring(0, 1).toUpperCase();
      setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
      Graphics2D g2 = (Graphics2D) g;
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      g2.setColor(darkNavy);
      g2.fillOval(0, 0, getWidth(), getHeight());
      g2.setColor(Color.WHITE);
      g2.setFont(new Font("Arial", Font.BOLD, 60));
      FontMetrics fm = g2.getFontMetrics();
      int x = (getWidth() - fm.stringWidth(initial)) / 2;
      int y = (getHeight() + fm.getAscent()) / 2 - 10;
      g2.drawString(initial, x, y);
    }
  }
}

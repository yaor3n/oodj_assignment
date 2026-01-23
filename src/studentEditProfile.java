import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// ===== Main Frame =====
public class studentEditProfile extends JFrame implements ActionListener {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField roleField;
    private JTextField displayNameField;


    private JButton editBtn, saveBtn, backBtn;

    private Student student;

    public studentEditProfile() {
        // Basic frame setup
        setLayout(null);
        setTitle("Edit Profile");
        setSize(1024, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Load current student
        student = AccountFileHandler.getStudent(Session.currentUsername);

        // ===== Title =====
        JLabel title = new JLabel("Edit Profile");
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setBounds(420, 40, 300, 50);
        add(title);

        // ===== Rounded Background Panel =====
        RoundedBackgroundPanel bgPanel = new RoundedBackgroundPanel(new Color(160, 190, 170), 60);
        bgPanel.setBounds(60, 120, 900, 450);
        bgPanel.setLayout(null);
        add(bgPanel);

        // ===== Avatar =====
        AvatarPanel avatar = new AvatarPanel(student.getDisplayName());
        avatar.setBounds(80, 140, 150, 150);
        bgPanel.add(avatar);

        // ===== Fields =====

        displayNameField = new JTextField(student.getDisplayName());
        displayNameField.setBounds(300, 100, 450, 50);
        displayNameField.setEditable(false);
        bgPanel.add(displayNameField);

        usernameField = new JTextField(student.getUsername());
        usernameField.setBounds(300, 170, 450, 50);
        usernameField.setEditable(false);
        bgPanel.add(usernameField);

        passwordField = new JPasswordField(student.getPassword());
        passwordField.setBounds(300, 240, 450, 50);
        passwordField.setEditable(false);
        bgPanel.add(passwordField);

        roleField = new JTextField(student.getRole());
        roleField.setBounds(300, 310, 450, 50);
        roleField.setEditable(false);
        bgPanel.add(roleField);

        //RoundedBorder fieldBorder = new RoundedBorder(20, new Color(90, 120, 100));
        //usernameField.setBorder(fieldBorder);
        //passwordField.setBorder(fieldBorder);
        //roleField.setBorder(fieldBorder);

        // ===== Buttons =====
        editBtn = new JButton("Edit");
        editBtn.setBounds(300, 370, 120, 50);
        editBtn.addActionListener(this);
        bgPanel.add(editBtn);

        saveBtn = new JButton("Save");
        saveBtn.setBounds(440, 370, 120, 50);
        saveBtn.setEnabled(false);
        saveBtn.addActionListener(this);
        bgPanel.add(saveBtn);

        backBtn = new JButton("Back");
        backBtn.setBounds(580, 370, 120, 50);
        backBtn.addActionListener(this);
        bgPanel.add(backBtn);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == editBtn) {
            displayNameField.setEditable(true);
            passwordField.setEditable(true);
            saveBtn.setEnabled(true);
        } else if (e.getSource() == saveBtn) {
            student.setDisplayName(displayNameField.getText());
            student.setPassword(new String(passwordField.getPassword()));
            AccountFileHandler.updateStudent(student);

            JOptionPane.showMessageDialog(this, "Profile updated successfully!");

            passwordField.setEditable(false);
            saveBtn.setEnabled(false);
        } else if (e.getSource() == backBtn) {
            new studentDashboard();
            dispose();
        }
    }

    // ===== Rounded Background Panel =====
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
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        }
    }

    // ===== Rounded Border for Fields =====
    class RoundedBorder implements javax.swing.border.Border {

        private int radius;
        private Color color;

        RoundedBorder(int radius, Color color) {
            this.radius = radius;
            this.color = color;
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius + 1, this.radius + 1, this.radius + 1, this.radius + 1);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(color);
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }
    }

    // ===== Avatar Panel with Initial =====
    class AvatarPanel extends JPanel {

        private String initial;

        AvatarPanel(String username) {
            this.initial = username.substring(0, 1).toUpperCase();
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Background Circle
            g2.setColor(new Color(120, 160, 140));
            g2.fillOval(0, 0, getWidth(), getHeight());

            // Initial Letter
            g2.setColor(Color.BLACK);
            int fontSize = Math.min(getWidth(), getHeight()) / 2;
            g2.setFont(new Font("Arial", Font.BOLD, fontSize));

            FontMetrics fm = g2.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(initial)) / 2;
            int y = (getHeight() + fm.getAscent()) / 2 - 5;

            g2.drawString(initial, x, y);
        }
    }

    // ===== Main Method for Testing =====
    public static void main(String[] args) {
        SwingUtilities.invokeLater(studentEditProfile::new);
    }
}

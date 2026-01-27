import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class studentEditProfile extends JFrame implements ActionListener {

    private JTextField nameField, emailField, dobField, genderField, usernameField, courseField;
    private JLabel idLabel, roleLabel;
    private JPasswordField passwordField;
    private JButton editBtn, saveBtn, backBtn, cancelBtn;
    private JPanel buttonContainer;
    private CardLayout buttonCardLayout;
    private Student student;

    public studentEditProfile() {
        // Load current student from Session
        this.student = AccountFileHandler.getStudent(Session.currentUsername);

        if (this.student == null) {
            JOptionPane.showMessageDialog(null, "User data not found. Please log in again.");
            this.dispose();
            return;
        }

        // Basic frame setup
        setTitle("Student Profile Management");
        setSize(800, 800);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // 1. PAGE TITLE
        JLabel pageTitle = new JLabel("👤 My Student Profile", SwingConstants.CENTER);
        pageTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        pageTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(pageTitle, BorderLayout.NORTH);

        // 2. CONTENT PANEL (SCROLLABLE)
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 30, 50));

        // --- Personal Info Card ---
        JPanel personalCard = createCardPanel("📝 Personal Information");
        idLabel = new JLabel(student.id);
        nameField = createStyledTextField(student.fullName);
        emailField = createStyledTextField(student.email);
        genderField = createStyledTextField(student.gender);
        dobField = createStyledTextField(student.dob);

        addProfileRow(personalCard, "Student ID", idLabel, 0);
        addProfileRow(personalCard, "Full Name", nameField, 1);
        addProfileRow(personalCard, "Email", emailField, 2);
        addProfileRow(personalCard, "Gender", genderField, 3);
        addProfileRow(personalCard, "Date of Birth", dobField, 4);

        // --- Account Credentials Card ---
        JPanel accountCard = createCardPanel("🔐 Account & Course");
        usernameField = createStyledTextField(student.getUsername());
        courseField = createStyledTextField(student.getCourse());
        roleLabel = new JLabel(student.getRole());

        // Password with Eye Toggle
        passwordField = new JPasswordField(student.getPassword());
        passwordField.setBorder(null);
        passwordField.setOpaque(false);
        JPanel passwordWrapper = createPasswordWrapper(passwordField);

        addProfileRow(accountCard, "Username", usernameField, 0);
        addProfileRow(accountCard, "Password", passwordWrapper, 1);
        addProfileRow(accountCard, "Course", courseField, 2);
        addProfileRow(accountCard, "Access Role", roleLabel, 3);

        // 3. BUTTONS (CardLayout)
        buttonCardLayout = new CardLayout();
        buttonContainer = new JPanel(buttonCardLayout);
        buttonContainer.setOpaque(true);

        // View Mode Buttons
        JPanel viewPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        viewPanel.setOpaque(true);
        editBtn = createActionButton("Edit Profile", new Color(51, 65, 85));
        backBtn = createActionButton("Back", new Color(108, 117, 125));
        viewPanel.add(backBtn);
        viewPanel.add(editBtn);

        // Edit Mode Buttons
        JPanel editPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        editPanel.setOpaque(true);
        saveBtn = createActionButton("Save Changes", new Color(40, 167, 69));
        cancelBtn = createActionButton("Cancel", new Color(220, 53, 69));
        editPanel.add(cancelBtn);
        editPanel.add(saveBtn);

        buttonContainer.add(viewPanel, "VIEW");
        buttonContainer.add(editPanel, "EDIT");

        // Assembly
        contentPanel.add(personalCard);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(accountCard);
        contentPanel.add(Box.createVerticalStrut(30));
        contentPanel.add(buttonContainer);

        JScrollPane scroll = new JScrollPane(contentPanel);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);

        // Initial State
        setEditMode(false);

        editBtn.addActionListener(this);
        saveBtn.addActionListener(this);
        cancelBtn.addActionListener(this);
        backBtn.addActionListener(this);

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void setEditMode(boolean editing) {
        JTextField[] fields = {nameField, emailField, dobField, genderField, usernameField, courseField};
        for (JTextField f : fields) {
            f.setEditable(editing);
            f.setBackground(editing ? Color.WHITE : new Color(245, 245, 245));
        }
        passwordField.setEditable(editing);
        passwordField.getParent().setBackground(editing ? Color.WHITE : new Color(245, 245, 245));

        buttonCardLayout.show(buttonContainer, editing ? "EDIT" : "VIEW");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == editBtn) {
            setEditMode(true);
        } else if (e.getSource() == cancelBtn) {
            setEditMode(false);
            // Revert fields to original student data
            nameField.setText(student.fullName);
            passwordField.setText(student.getPassword());
            // ... add others as needed
        } else if (e.getSource() == saveBtn) {
            // Update Student object
            student.fullName = nameField.getText().trim();
            student.email = emailField.getText().trim();
            student.gender = genderField.getText().trim();
            student.dob = dobField.getText().trim();
            student.username = usernameField.getText().trim();
            student.setPassword(new String(passwordField.getPassword()).trim());
            student.course = courseField.getText().trim();

            AccountFileHandler.updateStudent(student);
            JOptionPane.showMessageDialog(this, "Profile Updated Successfully!");
            setEditMode(false);
        } else if (e.getSource() == backBtn) {
            new studentDashboard();
            dispose();
        }
    }

    // --- UI Helper Methods ---

    private JPanel createCardPanel(String title) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel header = new JLabel(title);
        header.setFont(new Font("Segoe UI", Font.BOLD, 16));
        header.setForeground(new Color(40, 167, 69));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = -1; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 15, 0);
        panel.add(header, gbc);
        return panel;
    }

    private void addProfileRow(JPanel panel, String labelText, JComponent comp, int row) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridy = row + 1;
        gbc.gridx = 0; gbc.weightx = 0.3;
        panel.add(new JLabel(labelText + ":"), gbc);

        gbc.gridx = 1; gbc.weightx = 0.7;
        panel.add(comp, gbc);
    }

    private JTextField createStyledTextField(String text) {
        JTextField tf = new JTextField(text);
        tf.setPreferredSize(new Dimension(300, 35));
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 210)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return tf;
    }

    private JPanel createPasswordWrapper(JPasswordField pf) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210)));
        wrapper.setBackground(new Color(245, 245, 245));

        JButton eye = new JButton("👁");
        eye.setBorderPainted(false);
        eye.setContentAreaFilled(false);
        eye.addActionListener(e -> {
            if (pf.getEchoChar() == (char)0) pf.setEchoChar('•');
            else pf.setEchoChar((char)0);
        });

        wrapper.add(pf, BorderLayout.CENTER);
        wrapper.add(eye, BorderLayout.EAST);
        return wrapper;
    }

    private JButton createActionButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(150, 40));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return btn;
    }
}

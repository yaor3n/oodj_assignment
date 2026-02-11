import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class lecturerEditProfile extends JFrame implements ActionListener {

    private JTextField idField, nameField, emailField, dobField, genderField, usernameField, roleField;
    private JPasswordField passwordField;
    private JButton editBtn, saveBtn, backBtn, cancelBtn;
    private JPanel buttonContainer;
    private CardLayout buttonCardLayout;

    private String lecturerID;
    private String currentID, currentFullName, currentEmail, currentGender, currentDob, currentUsername, currentPassword;

    public lecturerEditProfile(String lecturerID) {
        this.lecturerID = lecturerID;
        loadLecturerData();

        setTitle("Lecturer Profile Management");
        setSize(800, 850);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        JLabel pageTitle = new JLabel("👤 My Lecturer Profile", SwingConstants.CENTER);
        pageTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        pageTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(pageTitle, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 30, 50));

        JPanel personalCard = createCardPanel("📝 Personal Information");
        idField = createStyledTextField(currentID);
        idField.setEditable(false);
        nameField = createStyledTextField(currentFullName);
        emailField = createStyledTextField(currentEmail);
        genderField = createStyledTextField(currentGender);
        dobField = createStyledTextField(currentDob);

        addProfileRow(personalCard, "Lecturer ID", idField, 0);
        addProfileRow(personalCard, "Full Name", nameField, 1);
        addProfileRow(personalCard, "Email", emailField, 2);
        addProfileRow(personalCard, "Gender", genderField, 3);
        addProfileRow(personalCard, "Date of Birth", dobField, 4);

        JPanel accountCard = createCardPanel("🔐 Account Security");
        usernameField = createStyledTextField(currentUsername);
        usernameField.setEditable(false);
        roleField = createStyledTextField("Lecturer");
        roleField.setEditable(false);

        passwordField = new JPasswordField(currentPassword);
        passwordField.setBorder(null);
        passwordField.setOpaque(false);
        JPanel passwordWrapper = createPasswordWrapper(passwordField);

        addProfileRow(accountCard, "Username", usernameField, 0);
        addProfileRow(accountCard, "Password", passwordWrapper, 1);
        addProfileRow(accountCard, "Access Role", roleField, 2);

        buttonCardLayout = new CardLayout();
        buttonContainer = new JPanel(buttonCardLayout);
        buttonContainer.setOpaque(false);

        JPanel viewPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        viewPanel.setOpaque(false);
        editBtn = createActionButton("Edit Profile", new Color(51, 65, 85));
        backBtn = createActionButton("Back", new Color(108, 117, 125));
        viewPanel.add(backBtn);
        viewPanel.add(editBtn);

        JPanel editPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        editPanel.setOpaque(false);
        saveBtn = createActionButton("Save Changes", new Color(40, 167, 69));
        cancelBtn = createActionButton("Cancel", new Color(220, 53, 69));
        editPanel.add(cancelBtn);
        editPanel.add(saveBtn);

        buttonContainer.add(viewPanel, "VIEW");
        buttonContainer.add(editPanel, "EDIT");

        contentPanel.add(personalCard);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(accountCard);
        contentPanel.add(Box.createVerticalStrut(30));
        contentPanel.add(buttonContainer);

        JScrollPane scroll = new JScrollPane(contentPanel);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Color.WHITE);
        add(scroll, BorderLayout.CENTER);

        editBtn.addActionListener(this);
        saveBtn.addActionListener(this);
        cancelBtn.addActionListener(this);
        backBtn.addActionListener(this);

        setEditMode(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void setEditMode(boolean editing) {
        JTextField[] fields = {nameField, emailField, genderField, dobField};
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
            nameField.setText(currentFullName);
            emailField.setText(currentEmail);
            genderField.setText(currentGender);
            dobField.setText(currentDob);
            passwordField.setText(currentPassword);
        } else if (e.getSource() == saveBtn) {
            saveProfile();
        } else if (e.getSource() == backBtn) {
            dispose();
        }
    }

    private void loadLecturerData() {
        try (BufferedReader r = new BufferedReader(new FileReader("accounts.txt"))) {
            String line;
            while ((line = r.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length >= 9 && p[0].equals(lecturerID)) {
                    currentID = p[0];
                    currentFullName = p[1];
                    currentEmail = p[2];
                    currentGender = p[3];
                    currentDob = p[4];
                    currentUsername = p[7];
                    currentPassword = p[8];
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveProfile() {
        List<String> lines = new ArrayList<>();
        try (BufferedReader r = new BufferedReader(new FileReader("accounts.txt"))) {
            String line;
            while ((line = r.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length >= 9 && p[0].equals(lecturerID)) {
                    p[1] = nameField.getText().trim();
                    p[2] = emailField.getText().trim();
                    p[3] = genderField.getText().trim();
                    p[4] = dobField.getText().trim();
                    p[8] = new String(passwordField.getPassword()).trim();
                    line = String.join(",", p);
                }
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (PrintWriter w = new PrintWriter(new FileWriter("accounts.txt"))) {
            for (String l : lines) w.println(l);
            loadLecturerData();
            JOptionPane.showMessageDialog(this, "Profile Updated Successfully!");
            setEditMode(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
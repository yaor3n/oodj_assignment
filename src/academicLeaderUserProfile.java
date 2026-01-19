import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class academicLeaderUserProfile extends JPanel {
    private JTextField nameField, emailField, dobField, genderField, userField;
    private JLabel staffIDField,roleField;
    private JPasswordField passField;
    //private final Color SUCCESS_GREEN = new Color(40, 167, 69);
    
    // Panel to hold buttons and layout to swap them
    private JPanel buttonContainer;
    private CardLayout buttonCardLayout;

    public academicLeaderUserProfile() {
        this.setLayout(new BorderLayout());
        this.setBackground(Color.WHITE);

        // 1. INITIALIZE COMPONENTS
        staffIDField = new JLabel("AL6767");
        nameField = createProfileTextField();
        emailField = createProfileTextField();
        genderField = createProfileTextField();
        dobField = createProfileTextField();
        userField = createProfileTextField();
        
        roleField = new JLabel("Pending");
        roleField.setFont(new Font("Segoe UI", Font.BOLD, 13));
    
        passField = new JPasswordField();
        passField.setBorder(null); 
        passField.setOpaque(false);
        //stylePasswordField(passField);
        
        JButton eyeButton = new JButton("👁️");
        eyeButton.setFont(new Font("Segoe UI Emoji", Font.PLAIN,14));
        eyeButton.setBorderPainted(false);
        eyeButton.setContentAreaFilled(false);
        eyeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        eyeButton.addActionListener(e -> {
            if (passField.getEchoChar() == (char)0){
                passField.setEchoChar('•');
            }else{
                passField.setEchoChar((char)0);
            }
        });
        
        JPanel passwordWrapper = new JPanel(new BorderLayout(5, 0));
        passwordWrapper.setBackground(new Color(245, 245, 245)); // Default grey
        passwordWrapper.setPreferredSize(new Dimension(380, 38));
        passwordWrapper.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(215, 220, 225), 1),
            BorderFactory.createEmptyBorder(0, 12, 0, 5) // Internal padding
        ));

        passwordWrapper.add(passField, BorderLayout.CENTER);
        passwordWrapper.add(eyeButton, BorderLayout.EAST);
        
        setEditMode(false);

        JPanel userProfileContent = new JPanel();
        userProfileContent.setLayout(new BoxLayout(userProfileContent, BoxLayout.Y_AXIS));
        userProfileContent.setOpaque(false);
        userProfileContent.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        // PAGE TITLE
        JLabel pageTitle = new JLabel("👤 User Profile Management");
        pageTitle.setFont(new Font("Segoe UI Emoji", Font.BOLD, 24));
        pageTitle.setForeground(new Color(30, 41, 59));
        pageTitle.setHorizontalAlignment(SwingConstants.CENTER);
        pageTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0)); // Add spacing

        this.add(pageTitle, BorderLayout.NORTH);

        // personal information
        JPanel personalCard = new JPanel(new GridBagLayout());
        styleProfileCard(personalCard);
        personalCard.setMaximumSize(new Dimension(650, 350));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 20, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel personalHeader = new JLabel("📝 Personal Information");
        personalHeader.setFont(new Font("Segoe UI Emoji", Font.BOLD, 15));
        personalHeader.setForeground(new Color(40, 167, 69));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        personalCard.add(personalHeader, gbc);
        gbc.gridwidth = 1;

        addProfileRow(personalCard, "Staff ID", staffIDField, gbc, 1);
        addProfileRow(personalCard, "Full Name", nameField, gbc, 2);
        addProfileRow(personalCard, "Email Address", emailField, gbc, 3);
        addProfileRow(personalCard, "Gender", genderField, gbc, 4);
        addProfileRow(personalCard, "Date of Birth", dobField, gbc, 5);
        

        // account credential
        JPanel credentialsCard = new JPanel(new GridBagLayout());
        styleProfileCard(credentialsCard);
        credentialsCard.setMaximumSize(new Dimension(650, 250));
        gbc.insets = new Insets(0, 0, 25, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel credsHeader = new JLabel("🔐 Account Credentials");
        credsHeader.setFont(new Font("Segoe UI Emoji", Font.BOLD, 15));
        credsHeader.setForeground(new Color(40, 167, 69));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        credentialsCard.add(credsHeader, gbc);
        gbc.gridwidth = 1;
        
        addProfileRow(credentialsCard, "Username", userField, gbc, 1);
        addProfileRow(credentialsCard, "Password", passwordWrapper, gbc, 2);
        addProfileRow(credentialsCard, "Access Role", roleField, gbc, 3);

        // --- 2. BUTTON LOGIC (CardLayout for swapping) ---
        buttonCardLayout = new CardLayout();
        buttonContainer = new JPanel(buttonCardLayout);
        buttonContainer.setOpaque(false);
        buttonContainer.setMaximumSize(new Dimension(650, 60));
        buttonContainer.setPreferredSize(new Dimension(650,80));

        // VIEW MODE PANEL (Just "Update Profile")
        JPanel viewModePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        viewModePanel.setOpaque(false);
        JButton updateBtn = new JButton("Update Profile");
        styleActionButton(updateBtn, new Color(51,65,85));
        updateBtn.addActionListener(e -> setEditMode(true));
        viewModePanel.add(updateBtn);

        // EDIT MODE PANEL ("Save" and "Cancel")
        JPanel editModePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        editModePanel.setOpaque(false);
        JButton saveBtn = new JButton("Save Changes");
        styleActionButton(saveBtn, new Color(40, 167, 69));
        saveBtn.addActionListener(e -> updateProfile());
        
        JButton cancelBtn = new JButton("Cancel");
        styleActionButton(cancelBtn, new Color(108, 117, 125));
        cancelBtn.addActionListener(e -> {
            loadProfileFromFile();
            setEditMode(false);
        });

        editModePanel.add(cancelBtn);
        editModePanel.add(saveBtn);

        buttonContainer.add(viewModePanel, "VIEW");
        buttonContainer.add(editModePanel, "EDIT");

        // ASSEMBLY
        userProfileContent.add(personalCard);
        userProfileContent.add(Box.createVerticalStrut(20));
        userProfileContent.add(credentialsCard);
        userProfileContent.add(Box.createVerticalStrut(30));
        userProfileContent.add(buttonContainer);

        JPanel centeringWrapper = new JPanel(new GridBagLayout());
        centeringWrapper.setBackground(Color.WHITE);
        centeringWrapper.add(userProfileContent);

        JScrollPane scroll = new JScrollPane(centeringWrapper);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.getVerticalScrollBar().setUnitIncrement(25);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        this.add(scroll, BorderLayout.CENTER);

        loadProfileFromFile(); 
    }

    private void setEditMode(boolean editing) {
        userField.setEditable(editing);
        passField.setEditable(editing);

        Color fieldBg = editing ? Color.WHITE : new Color(245, 245, 245);
        userField.setBackground(fieldBg);

        // Find the password wrapper and change its color
        Component wrapper = passField.getParent();
        if (wrapper instanceof JPanel) {
            wrapper.setBackground(fieldBg);
        }

        if (buttonCardLayout != null) {
            buttonCardLayout.show(buttonContainer, editing ? "EDIT" : "VIEW");
        }
    }

    private void styleActionButton(JButton updateButton, Color bg) {
        updateButton.setBackground(bg);
        updateButton.setForeground(Color.WHITE);
        updateButton.setPreferredSize(new Dimension(180, 45));
        updateButton.setOpaque(true);
        updateButton.setBorderPainted(false);
        updateButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        updateButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        updateButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                updateButton.setBackground(bg.darker());
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                updateButton.setBackground(bg);
            }
        });
    }

    // (Remaining methods updateProfile, styleProfileCard, createProfileTextField, addProfileRow, loadProfileFromFile stay exactly the same as previous)
    
    private void updateProfile() {
        String newUsername = userField.getText().trim();
        String newPassword = new String(passField.getPassword()).trim();

        if (newUsername.isEmpty() || newPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and Password cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<String> accounts = new ArrayList<>();
        boolean updated = false;

        try {
            BufferedReader reader = new BufferedReader(new FileReader("accounts.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3 && parts[2].trim().equalsIgnoreCase("Academic Leader")) {
                    accounts.add(newUsername + "," + newPassword + "," + parts[2].trim());
                    updated = true;
                } else {
                    accounts.add(line);
                }
            }
            reader.close();

            BufferedWriter writer = new BufferedWriter(new FileWriter("accounts.txt"));
            for (String acc : accounts) {
                writer.write(acc);
                writer.newLine();
            }
            writer.close();

            if (updated) {
                JOptionPane.showMessageDialog(this, "Profile updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                setEditMode(false); // Return to view mode after saving
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving data: " + ex.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void styleProfileCard(JPanel panel) {
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(40, 167, 69), 1, true), // Soft Slate
            BorderFactory.createEmptyBorder(30, 30, 15, 30) // Reduced internal padding
        ));
    }

    private JTextField createProfileTextField() {
        JTextField tf = new JTextField();
        tf.setPreferredSize(new Dimension(380, 38));
        tf.setEditable(false); // Default
        tf.setBackground(new Color(245, 245, 245)); // Visual feedback for read-only
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(215, 220, 225), 1),
            BorderFactory.createEmptyBorder(5, 12, 5, 12)
        ));
        return tf;
    }
    
    private void stylePasswordField(JPasswordField pf) {
        pf.setPreferredSize(new Dimension(380, 38));
        pf.setEditable(false);
        pf.setBackground(new Color(245, 245, 245));
        pf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(215, 220, 225), 1),
            BorderFactory.createEmptyBorder(5, 12, 5, 12)
        ));
    }

    private void addProfileRow(JPanel panel, String label, JComponent comp, GridBagConstraints gbc, int row) {
        gbc.gridy = row;
        gbc.gridx = 0; gbc.weightx = 0.25;
        gbc.insets = new Insets(0, 0, 15, 30);
        gbc.anchor = GridBagConstraints.WEST;
        JLabel contentLabel = new JLabel(label + ":");
        contentLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panel.add(contentLabel, gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.8;
        gbc.insets = new Insets(0,0,15,0);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(comp, gbc);
    }

    private void loadProfileFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("accounts.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3 && parts[2].trim().equalsIgnoreCase("Academic Leader")) {
                    userField.setText(parts[0].trim());
                    passField.setText(parts[1].trim());
                    roleField.setText(parts[2].trim());
                    nameField.setText("Ong Szi Kai");
                    emailField.setText("ongszikai@apu.edu.my");
                    genderField.setText("Male");
                    dobField.setText("1 January 2025");
                    break; 
                }
            }
        } catch (Exception e) {
            roleField.setText("Error reading file");
        }
    }
}
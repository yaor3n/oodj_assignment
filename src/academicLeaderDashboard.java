import javax.swing.*;
import java.awt.*;
import java.util.List;

public class academicLeaderDashboard extends JFrame {

    private JPanel centerPanel;
    private JPanel sidebar;
    private boolean sidebarVisible = false;
    private final Color BACKGROUND_COLOR = new Color(240, 244, 248);

    public academicLeaderDashboard() {

        reusable.windowSetup(this);
        this.setLayout(new BorderLayout());

        // Sidebar
        sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(new Color(180, 180, 180));
        sidebar.setPreferredSize(new Dimension(200, 0));

        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(new Color(120, 120, 120));
        logoPanel.setPreferredSize(new Dimension(0, 60));
        logoPanel.add(new JLabel("Logo"));
        sidebar.add(logoPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(0, 1));
        buttonPanel.add(new JButton("Dashboard"));
        buttonPanel.add(new JButton("Report"));
        sidebar.add(buttonPanel, BorderLayout.CENTER);

        sidebar.setVisible(false);
        this.add(sidebar, BorderLayout.WEST);

        // Main dashboard panel
        JPanel dashboard = new JPanel(new BorderLayout());
        this.add(dashboard, BorderLayout.CENTER);

        // Top container
        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.Y_AXIS));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.DARK_GRAY);
        header.setPreferredSize(new Dimension(0, 50));

        JButton hamburgerButton = new JButton("☰");
        hamburgerButton.setForeground(Color.WHITE);
        hamburgerButton.setBorderPainted(false);
        hamburgerButton.setContentAreaFilled(false);
        hamburgerButton.addActionListener(e -> toggleSidebar());
        header.add(hamburgerButton, BorderLayout.WEST);

        JLabel headerTitle = new JLabel("Academic Leader Dashboard", SwingConstants.CENTER);
        headerTitle.setForeground(Color.WHITE);
        header.add(headerTitle, BorderLayout.CENTER);
        topContainer.add(header);

        // Action row
        JPanel actionRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        JButton createButton = new JButton("Create Modules");
        createButton.setPreferredSize(new Dimension(150, 40));
        createButton.addActionListener(e -> showModuleDialog(null)); // null = new module
        actionRow.add(createButton);
        topContainer.add(actionRow);

        dashboard.add(topContainer, BorderLayout.NORTH);

        // Center panel for module cards
        centerPanel = new JPanel(new GridLayout(0, 3, 20, 20));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(centerPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);
        dashboard.add(scrollPane, BorderLayout.CENTER);

        refreshDashboard();

        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void refreshDashboard() {
        centerPanel.removeAll();
        List<academicLeaderModule> modules = academicLeaderFileManager.loadModules();

        for (academicLeaderModule m : modules) {
            JPanel moduleCard = new JPanel(new BorderLayout());
            moduleCard.setPreferredSize(new Dimension(280, 180));
            moduleCard.setBackground(Color.WHITE);
            moduleCard.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 2));

            JPanel cardHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            cardHeader.setOpaque(false);
            JButton dotBtn = new JButton("...");
            dotBtn.setBorderPainted(false);
            dotBtn.setContentAreaFilled(false);
            cardHeader.add(dotBtn);

            moduleCard.add(cardHeader, BorderLayout.NORTH);

            JLabel nameLabel = new JLabel(m.getName(), SwingConstants.CENTER);
            nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
            moduleCard.add(nameLabel, BorderLayout.CENTER);

            // Right-click menu for edit/delete
            moduleCard.setComponentPopupMenu(createCardPopup(m));

            centerPanel.add(moduleCard);
        }

        centerPanel.revalidate();
        centerPanel.repaint();
    }

    private JPopupMenu createCardPopup(academicLeaderModule m) {
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem editItem = new JMenuItem("Edit Module");
        JMenuItem deleteItem = new JMenuItem("Delete Module");

        editItem.addActionListener(e -> showModuleDialog(m));
        deleteItem.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete " + m.getName() + "?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                academicLeaderFileManager.deleteModule(m.getCode());
                refreshDashboard();
            }
        });

        popupMenu.add(editItem);
        popupMenu.add(new JSeparator());
        popupMenu.add(deleteItem);

        return popupMenu;
    }

    private void showModuleDialog(academicLeaderModule editModule) {
        boolean isEdit = (editModule != null);

        JDialog dialog = new JDialog(this, isEdit ? "Edit Module" : "Create Module", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField codeField = new JTextField(isEdit ? editModule.getCode() : "", 20);
        if (isEdit) codeField.setEditable(false);

        JTextField nameField = new JTextField(isEdit ? editModule.getName() : "", 20);
        JComboBox<String> qualificationBox = new JComboBox<>(new String[]{"Diploma", "Bachelor's Degree", "Master's Degree", "PhD"});

        List<String> lecturers = academicLeaderFileManager.checkLecturerNames();
        JComboBox<String> lecturerBox = new JComboBox<>(lecturers.toArray(new String[0]));
        if (lecturers.isEmpty()) lecturerBox.addItem("No Lecturers Found");

        JTextArea descriptionArea = new JTextArea(isEdit ? editModule.getDescription() : "", 5, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descriptionScroll = new JScrollPane(descriptionArea);

        gbc.gridx = 0; gbc.gridy = 0; dialog.add(new JLabel("Code:"), gbc);
        gbc.gridx = 1; dialog.add(codeField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; dialog.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; dialog.add(nameField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; dialog.add(new JLabel("Qualification:"), gbc);
        gbc.gridx = 1; dialog.add(qualificationBox, gbc);
        gbc.gridx = 0; gbc.gridy = 3; dialog.add(new JLabel("Lecturer:"), gbc);
        gbc.gridx = 1; dialog.add(lecturerBox, gbc);
        gbc.gridx = 0; gbc.gridy = 4; gbc.anchor = GridBagConstraints.NORTHWEST; dialog.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1; gbc.weighty = 1.0; dialog.add(descriptionScroll, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton(isEdit ? "Save Changes" : "Create");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            String code = codeField.getText().trim();
            String name = nameField.getText().trim();
            String qualification = (String) qualificationBox.getSelectedItem();
            String lecturer = (String) lecturerBox.getSelectedItem();
            String description = descriptionArea.getText().trim();

            if (code.isEmpty() || name.isEmpty() || description.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "All fields are required.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            academicLeaderModule newModule = new academicLeaderModule(code, name, qualification, lecturer, description);

            if (isEdit) {
                academicLeaderFileManager.updateModule(newModule);
            } else {
                academicLeaderFileManager.saveModule(newModule);
            }

            refreshDashboard();
            dialog.dispose();
        });

        cancelButton.addActionListener(e -> dialog.dispose());
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; gbc.weighty = 0;
        dialog.add(buttonPanel, gbc);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void toggleSidebar() {
        sidebarVisible = !sidebarVisible;
        sidebar.setVisible(sidebarVisible);
        this.revalidate();
        this.repaint();
    }

    public static void main(String[] args) {
        new academicLeaderDashboard();
    }
}

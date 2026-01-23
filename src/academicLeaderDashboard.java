import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.List;

public class academicLeaderDashboard extends JFrame{

    private JPanel centerPanel;
    private JPanel gridWrapper;
    private JPanel sidebar;
    private boolean sidebarVisible = false;
    private final Color BACKGROUND_COLOR = new Color(240, 244, 248);

    public academicLeaderDashboard() {

        reusable.windowSetup(this);
        this.setLayout(new BorderLayout());
        //sidebar
        sidebar = new JPanel();
        sidebar.setBackground(new Color(180, 180, 180)); // Light grey as per sketch
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setLayout(new BorderLayout());
        //logo area
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(new Color(120, 120, 120));
        logoPanel.setPreferredSize(new Dimension(0, 60));
        logoPanel.add(new JLabel("Logo"));
        sidebar.add(logoPanel, BorderLayout.NORTH);
        //sidelabel
        sidebar.add(new JButton("Dashboard"));
        sidebar.add(new JButton("Report"));
        sidebar.setVisible(false);
        this.add(sidebar, BorderLayout.WEST);

        //main
        JPanel dashboard = new JPanel(new BorderLayout());
        this.add(dashboard, BorderLayout.CENTER);

        //top area (header+button row)
        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.Y_AXIS));

        //header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.DARK_GRAY);
        header.setPreferredSize(new Dimension(0, 50));

        //hamburger
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

        JPanel actionRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        JButton createButton = new JButton("Create Modules");
        createButton.setPreferredSize(new Dimension(150, 40)); // Fixed Button Size
        createButton.addActionListener(e -> showCreateDialog());
        actionRow.add(createButton);
        topContainer.add(actionRow);
        dashboard.add(topContainer, BorderLayout.NORTH);
        //card
        centerPanel = new JPanel(new GridLayout(0, 3, 20, 20));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JPanel gridWrapper = new JPanel(new BorderLayout());
        gridWrapper.add(centerPanel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(centerPanel);
        scrollPane.setBorder(null);
        dashboard.add(scrollPane, BorderLayout.CENTER);

        refreshDashboard();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new academicLeaderDashboard();
    }

    private void refreshDashboard() {
        centerPanel.removeAll();
        List<academicLeaderModule> modules = academicLeaderModuleFileManager.loadModules();

        for (academicLeaderModule m : modules) {
            // Card Header with 3-dot button
            JPanel moduleCard = new JPanel(new BorderLayout());
            moduleCard.setPreferredSize(new Dimension(280, 180));
            moduleCard.setBackground(Color.WHITE);
            // Hide the code: Using a plain line border instead of TitledBorder
            moduleCard.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 2));

            JPanel cardHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            cardHeader.setOpaque(false);
            JButton dotBtn = new JButton("...");
            dotBtn.setBorderPainted(false);
            dotBtn.setContentAreaFilled(false);

            // WE WILL LINK THE POPUP MENU HERE LATER
            cardHeader.add(dotBtn);
            moduleCard.add(cardHeader, BorderLayout.NORTH);

            // Card Center showing ONLY the Module Name
            JLabel nameLabel = new JLabel(m.getName(), SwingConstants.CENTER);
            nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
            moduleCard.add(nameLabel, BorderLayout.CENTER);

            centerPanel.add(moduleCard);
        }
        centerPanel.revalidate();
        centerPanel.repaint();
    }

    public void showCreateDialog(){
        JDialog dialog = new JDialog(this, "Create New Module",true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Components
        JTextField codeField = new JTextField("", 20);
        JTextField nameField = new JTextField("", 20);

        String[] qualifications = {"Diploma", "Bachelor's Degree", "Master's Degree", "PhD"};
        JComboBox<String> qualificationBox = new JComboBox<>(qualifications);

        List<String> lecturerList = academicLeaderModuleFileManager.checkLecturerNames();
        JComboBox<String> lecturerBox = new JComboBox<>(lecturerList.toArray(new String[0]));
        if (lecturerList.isEmpty()){
            lecturerBox.addItem("No Lecturers Found");
        }

        JTextArea descriptionArea = new JTextArea("", 5, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descriptionScroll = new JScrollPane(descriptionArea);

        // Labels and Fields Layout
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

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Create");
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
            academicLeaderModuleFileManager.saveModule(newModule);

            JOptionPane.showMessageDialog(dialog, "Module Created Successfully!");
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
        sidebarVisible = !sidebarVisible; // Flip the state
        sidebar.setVisible(sidebarVisible); // Show or hide

        // IMPORTANT: This tells Swing to recalculate the layout now that WEST is visible
        this.revalidate();
        this.repaint();
    }
}

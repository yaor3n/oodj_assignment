import javax.swing.*;
import java.awt.*;
import java.util.List;

public class academicLeaderDashboard extends JFrame {
    private JPanel centerPanel;
    private JPanel sidebar;
    private JPanel glassPane;
    private JLayeredPane layeredPane;
    private boolean sidebarVisible = false;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private academicLeaderDashboardSidebar sidebarPanel;

    private final Color activeColor = new Color(30, 30, 30);
    private final Color defaultColor = new Color(70, 70, 70);
    private final Color BACKGROUND_COLOR = new Color(240, 244, 248);
    //private final Color SUCCESS_GREEN = new Color(40, 167, 69);

    public academicLeaderDashboard() {
        reusable.windowSetup(this);
        layeredPane = new JLayeredPane();
        this.setContentPane(layeredPane);

        // --- MAIN UI STRUCTURE ---
        JPanel mainHeader = new JPanel(new BorderLayout());

        // Header Bar
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 50));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));

        JButton hamburgerButton = new JButton("☰");
        hamburgerButton.setForeground(new Color(50, 50, 50));
        hamburgerButton.setBorderPainted(false);
        hamburgerButton.setContentAreaFilled(false);
        hamburgerButton.setFocusPainted(false);
        hamburgerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        hamburgerButton.addActionListener(e -> toggleSidebar());
        header.add(hamburgerButton, BorderLayout.WEST);

        JLabel headerTitle = new JLabel("Academic Leader Dashboard", SwingConstants.CENTER);
        headerTitle.setForeground(new Color(33, 37, 41));
        header.add(headerTitle, BorderLayout.CENTER);

        mainHeader.add(header, BorderLayout.NORTH);

        // Navigation Card Panel
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // --- DASHBOARD PAGE ---
        JPanel dashboardPage = new JPanel(new BorderLayout());
        dashboardPage.setBackground(BACKGROUND_COLOR);

        // Top Action Row
        JPanel actionRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        actionRow.setOpaque(false);
        JButton createButton = new JButton("Create New Module");
        createButton.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        createButton.setBackground(new Color(40, 167, 69));
        createButton.setForeground(Color.WHITE);
        createButton.setOpaque(true);
        createButton.setBorderPainted(false);
        createButton.setFocusPainted(false);
        createButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        createButton.setPreferredSize(new Dimension(180, 35));
        createButton.addActionListener(e -> showModuleDialog(null));
        actionRow.add(createButton);
        dashboardPage.add(actionRow, BorderLayout.NORTH);

        // Module Grid Assembly
        centerPanel = new JPanel(new GridLayout(0, 3, 20, 20));
        centerPanel.setOpaque(false);
        centerPanel.setBackground(BACKGROUND_COLOR);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        JPanel gridWrapper = new JPanel(new BorderLayout());
        gridWrapper.setOpaque(false);
        gridWrapper.setBackground(BACKGROUND_COLOR);
        gridWrapper.add(centerPanel, BorderLayout.NORTH);
        
        JScrollPane scrollPane = new JScrollPane(gridWrapper);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);
        scrollPane.getVerticalScrollBar().setUnitIncrement(15);
        dashboardPage.add(scrollPane, BorderLayout.CENTER);

        // --- REPORT PAGE ---
        academicLeaderReport reportPageObject = new academicLeaderReport();

        cardPanel.add(dashboardPage, "DASHBOARD");
        cardPanel.add(reportPageObject, "REPORT");
        mainHeader.add(cardPanel, BorderLayout.CENTER);

        layeredPane.add(mainHeader, JLayeredPane.DEFAULT_LAYER);

        // --- SIDEBAR SETUP ---
        sidebarPanel = new academicLeaderDashboardSidebar(() -> toggleSidebar());
        sidebar = sidebarPanel;
        sidebar.setVisible(false);
        layeredPane.add(sidebar, JLayeredPane.MODAL_LAYER);

        sidebarPanel.getDashboardBtn().addActionListener(e -> showPage("DASHBOARD"));
        sidebarPanel.getReportBtn().addActionListener(e -> showPage("REPORT"));

        // Glass Pane for Dimming
        glassPane = new JPanel();
        glassPane.setBackground(new Color(0, 0, 0, 50));
        glassPane.setOpaque(false);
        glassPane.setVisible(false);
        glassPane.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (sidebarVisible) toggleSidebar();
            }
        });
        layeredPane.add(glassPane, JLayeredPane.PALETTE_LAYER);

        // Responsive Resize Logic
        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                int w = layeredPane.getWidth();
                int h = layeredPane.getHeight();
                mainHeader.setBounds(0, 0, w, h);
                glassPane.setBounds(0, 0, w, h);
                sidebar.setBounds(0, 0, 240, h);
            }
        });

        refreshDashboard();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new academicLeaderDashboard();
    }

    private void refreshDashboard() {
        centerPanel.removeAll();
        centerPanel.setBackground(BACKGROUND_COLOR);  
        
        List<academicLeaderModule> modules = academicLeaderModuleFileManager.loadModules();

        for (academicLeaderModule m : modules) {
            // Card Border Layout (NORTH: Image, CENTER: Text Info)
            JPanel moduleCard = new JPanel(new BorderLayout());
            moduleCard.setPreferredSize(new Dimension(280, 340));
            moduleCard.setBackground(Color.WHITE);
            moduleCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true),
                BorderFactory.createEmptyBorder(0, 0, 10, 0)
            ));

            // Card Image (NORTH)
            JLabel cardImg = new JLabel("", SwingConstants.CENTER);
            String path = m.getImageFilePath();
            ImageIcon icon = (path == null || path.equals("default") || path.equals("ModuleDefaultPic.png")) 
                             ? new ImageIcon("ModuleDefaultPic.png") : new ImageIcon(path);

            if (icon.getImage() != null) {
                Image scaled = icon.getImage().getScaledInstance(280, 180, Image.SCALE_SMOOTH);
                cardImg.setIcon(new ImageIcon(scaled));
            }

            // Info Section (CENTER)
            JPanel contentPanel = new JPanel(new BorderLayout());
            contentPanel.setBackground(Color.WHITE);
            contentPanel.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));

            // Title Row: Contains Name (Left) and 3-Dot Button (Right)
            JPanel titleRow = new JPanel(new BorderLayout());
            titleRow.setOpaque(false);

            JLabel nameLabel = new JLabel(m.getName());
            nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 17));
            nameLabel.setForeground(new Color(33, 37, 41));

            JButton dotBtn = new JButton("...");
            dotBtn.setFont(new Font("Segoe UI", Font.BOLD, 20));
            dotBtn.setForeground(new Color(180, 180, 180));
            dotBtn.setBorderPainted(false);
            dotBtn.setContentAreaFilled(false);
            dotBtn.setFocusPainted(false);
            dotBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            dotBtn.addActionListener(e -> {
                JPopupMenu popupMenu = createCardPopup(m);
                popupMenu.show(dotBtn, -50, dotBtn.getHeight());
            });

            titleRow.add(nameLabel, BorderLayout.CENTER);
            titleRow.add(dotBtn, BorderLayout.EAST);

            // Sub-info: Qualification and Date
            JPanel subInfo = new JPanel(new GridLayout(2, 1, 0, 0));
            subInfo.setOpaque(false);

            JLabel qualLabel = new JLabel(m.getQualification());
            qualLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            qualLabel.setForeground(new Color(108, 117, 125));

            JLabel timeLabel = new JLabel(m.getIntakeMonth() + " " + m.getYear());
            timeLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
            timeLabel.setForeground(new Color(40, 167, 69)); // Matches report design

            subInfo.add(qualLabel);
            subInfo.add(timeLabel);

            contentPanel.add(titleRow, BorderLayout.NORTH);
            contentPanel.add(subInfo, BorderLayout.CENTER);

            moduleCard.add(cardImg, BorderLayout.NORTH);
            moduleCard.add(contentPanel, BorderLayout.CENTER);

            centerPanel.add(moduleCard);
        }
        centerPanel.revalidate();
        centerPanel.repaint();
    }

    private JPopupMenu createCardPopup(academicLeaderModule m) {
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

        JMenuItem editItem = new JMenuItem("Edit Module");
        JMenuItem deleteItem = new JMenuItem("Delete Module");

        editItem.addActionListener(e -> showModuleDialog(m));
        deleteItem.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to delete " + m.getName() + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                academicLeaderModuleFileManager.deleteModule(m.getCode());
                refreshDashboard();
            }
        });

        popupMenu.add(editItem);
        popupMenu.add(deleteItem);
        return popupMenu;
    }

    public void showModuleDialog(academicLeaderModule editModule) {
        boolean isEdit = (editModule != null);
        JDialog dialog = new JDialog(this, isEdit ? "Edit Module" : "Create New Module", true);
        dialog.setSize(800, 550);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints Gbc = new GridBagConstraints();
        dialog.getContentPane().setBackground(new Color(248, 249, 250));

        final String[] defaultPics = {"ModuleDefaultPic.png", "ModuleDefaultPic2.png", "ModuleDefaultPic3.png"};
        final int[] currentIndex = {0};
        final String[] selectedPath = {isEdit ? editModule.getImageFilePath() : defaultPics[0]};

        // --- LEFT PANEL (IMAGE SELECTION) ---
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setOpaque(false);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        GridBagConstraints leftGbc = new GridBagConstraints();

        leftGbc.gridx = 0; leftGbc.gridy = 0; leftGbc.gridwidth = 3;
        leftGbc.insets = new Insets(0, 0, 20, 0);
        leftPanel.add(new JLabel("Module Cover Image", SwingConstants.CENTER), leftGbc);

        JLabel imagePlaceholder = new JLabel("", SwingConstants.CENTER);
        imagePlaceholder.setPreferredSize(new Dimension(240, 190));
        imagePlaceholder.setOpaque(true);
        imagePlaceholder.setBackground(new Color(230, 230, 230));

        updateImagePreview(imagePlaceholder, selectedPath[0]);

        JButton leftArrow = new JButton("<");
        JButton rightArrow = new JButton(">");
        for (JButton btn : new JButton[]{leftArrow, rightArrow}) {
            btn.setContentAreaFilled(false);
            btn.setFocusPainted(false);
            btn.setPreferredSize(new Dimension(30, 120));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        leftArrow.addActionListener(e -> {
            currentIndex[0] = (currentIndex[0] - 1 + defaultPics.length) % defaultPics.length;
            selectedPath[0] = defaultPics[currentIndex[0]];
            updateImagePreview(imagePlaceholder, selectedPath[0]);
        });

        rightArrow.addActionListener(e -> {
            currentIndex[0] = (currentIndex[0] + 1) % defaultPics.length;
            selectedPath[0] = defaultPics[currentIndex[0]];
            updateImagePreview(imagePlaceholder, selectedPath[0]);
        });

        leftGbc.gridwidth = 1; leftGbc.gridy = 1;
        leftGbc.gridx = 0; leftPanel.add(leftArrow, leftGbc);
        leftGbc.gridx = 1; leftPanel.add(imagePlaceholder, leftGbc);
        leftGbc.gridx = 2; leftPanel.add(rightArrow, leftGbc);

        JButton uploadBtn = new JButton("Upload Custom Image");
        leftGbc.gridx = 0; leftGbc.gridy = 2; leftGbc.gridwidth = 3;
        leftGbc.insets = new Insets(15, 0, 0, 0);
        leftPanel.add(uploadBtn, leftGbc);

        uploadBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(dialog);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedPath[0] = fileChooser.getSelectedFile().getAbsolutePath();
                updateImagePreview(imagePlaceholder, selectedPath[0]);
            }
        });

        // --- RIGHT PANEL (FORM) ---
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setOpaque(false);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 20));
        GridBagConstraints rgbc = new GridBagConstraints();
        rgbc.insets = new Insets(5, 5, 5, 5);
        rgbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField codeField = new JTextField(20);
        JTextField nameField = new JTextField(20);
        JComboBox<String> qualificationBox = new JComboBox<>(new String[]{"-Select-", "Foundation", "Certificate", "Diploma", "Bachelor's Degree", "Master's Degree", "PhD"});
        
        List<String> lecturerList = academicLeaderModuleFileManager.checkLecturerNames();
        JComboBox<String> lecturerBox = new JComboBox<>();
        lecturerBox.addItem("-Select-");
        for (String name : lecturerList) lecturerBox.addItem(name);

        String[] months = {"-Select-", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        JComboBox<String> intakeMonthBox = new JComboBox<>(months);

        int currentYear = java.time.LocalDate.now().getYear();
        JComboBox<String> yearBox = new JComboBox<>();
        for (int i = 0; i < 5; i++) yearBox.addItem(String.valueOf(currentYear + i));

        JTextArea descriptionArea = new JTextArea(4, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(descriptionArea);

        if (isEdit) {
            codeField.setText(editModule.getCode());
            codeField.setEditable(false);
            nameField.setText(editModule.getName());
            qualificationBox.setSelectedItem(editModule.getQualification());
            lecturerBox.setSelectedItem(editModule.getLecturerName());
            intakeMonthBox.setSelectedItem(editModule.getIntakeMonth());
            yearBox.setSelectedItem(String.valueOf(editModule.getYear()));
            descriptionArea.setText(editModule.getDescription());
        }

        rgbc.gridx = 0; rgbc.gridy = 0; rightPanel.add(new JLabel("Code:"), rgbc);
        rgbc.gridx = 1; rightPanel.add(codeField, rgbc);
        rgbc.gridx = 0; rgbc.gridy = 1; rightPanel.add(new JLabel("Name:"), rgbc);
        rgbc.gridx = 1; rightPanel.add(nameField, rgbc);
        rgbc.gridx = 0; rgbc.gridy = 2; rightPanel.add(new JLabel("Level:"), rgbc);
        rgbc.gridx = 1; rightPanel.add(qualificationBox, rgbc);
        rgbc.gridx = 0; rgbc.gridy = 3; rightPanel.add(new JLabel("Lecturer:"), rgbc);
        rgbc.gridx = 1; rightPanel.add(lecturerBox, rgbc);
        rgbc.gridx = 0; rgbc.gridy = 4; rightPanel.add(new JLabel("Month:"), rgbc);
        rgbc.gridx = 1; rightPanel.add(intakeMonthBox, rgbc);
        rgbc.gridx = 0; rgbc.gridy = 5; rightPanel.add(new JLabel("Year:"), rgbc);
        rgbc.gridx = 1; rightPanel.add(yearBox, rgbc);
        rgbc.gridx = 0; rgbc.gridy = 6; rgbc.anchor = GridBagConstraints.NORTH; rightPanel.add(new JLabel("Desc:"), rgbc);
        rgbc.gridx = 1; rightPanel.add(descScroll, rgbc);

        Gbc.gridx = 0; Gbc.gridy = 0; dialog.add(leftPanel, Gbc);
        Gbc.gridx = 1; dialog.add(rightPanel, Gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        JButton saveButton = new JButton(isEdit ? "Update" : "Create");
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);

        Gbc.gridx = 0; Gbc.gridy = 1; Gbc.gridwidth = 2; Gbc.insets = new Insets(20, 0, 0, 20);
        dialog.add(buttonPanel, Gbc);

        saveButton.addActionListener(e -> {
            if (codeField.getText().trim().isEmpty() || nameField.getText().trim().isEmpty() || intakeMonthBox.getSelectedIndex() <= 0) {
                JOptionPane.showMessageDialog(dialog, "Please fill all required fields.");
                return;
            }
            
            String selectedMonth = (String) intakeMonthBox.getSelectedItem();
            int selectedYear = Integer.parseInt((String) yearBox.getSelectedItem());
            if (isDateInPast(selectedMonth, selectedYear)) {
                JOptionPane.showMessageDialog(dialog, "Cannot create module for a past date.");
                return;
            }

            academicLeaderModule newM = new academicLeaderModule(
                codeField.getText().trim(), nameField.getText().trim(), 
                (String)qualificationBox.getSelectedItem(), (String)lecturerBox.getSelectedItem(), 
                selectedMonth, selectedYear, descriptionArea.getText().trim(), selectedPath[0]
            );

            if (isEdit) academicLeaderModuleFileManager.updateModule(newM);
            else academicLeaderModuleFileManager.saveModule(newM);
            
            refreshDashboard();
            dialog.dispose();
        });

        cancelButton.addActionListener(e -> dialog.dispose());
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void updateImagePreview(JLabel placeholder, String path) {
        ImageIcon icon = new ImageIcon(path);
        if (icon.getImage() != null) {
            Image scaled = icon.getImage().getScaledInstance(240, 190, Image.SCALE_SMOOTH);
            placeholder.setIcon(new ImageIcon(scaled));
        }
    }

    private void toggleSidebar() {
        sidebarVisible = !sidebarVisible;
        sidebar.setVisible(sidebarVisible);
        glassPane.setVisible(sidebarVisible);
        layeredPane.repaint();
    }

    private void showPage(String pageName) {
        cardLayout.show(cardPanel, pageName);
        if (sidebarPanel != null) {
            sidebarPanel.getDashboardBtn().setBackground(pageName.equals("DASHBOARD") ? activeColor : defaultColor);
            sidebarPanel.getReportBtn().setBackground(pageName.equals("REPORT") ? activeColor : defaultColor);
        }
        if (sidebarVisible) toggleSidebar();
    }

    private boolean isDateInPast(String selectedMonth, int selectedYear) {
        java.time.LocalDate now = java.time.LocalDate.now();
        int selectedMonthInt = java.time.Month.valueOf(selectedMonth.toUpperCase()).getValue();
        if (selectedYear < now.getYear()) return true;
        if (selectedYear == now.getYear()) return selectedMonthInt < now.getMonthValue();
        return false;
    }
}
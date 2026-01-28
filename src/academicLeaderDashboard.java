import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class academicLeaderDashboard extends JFrame {
    private JPanel centerPanel;
    private JPanel gridWrapper;
    private JPanel sidebar;
    private JPanel glassPane;
    private JLayeredPane layeredPane;
    private boolean sidebarVisible = false;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private academicLeaderDashboardSidebar sidebarPanel;
    private JTextField searchField;

    private final Color activeColor = new Color(30, 30, 30);
    private final Color defaultColor = new Color(70, 70, 70);
    private final Color BACKGROUND_COLOR = (Color.WHITE);

    public academicLeaderDashboard() {
        reusable.windowSetup(this);
        layeredPane = new JLayeredPane();
        this.setContentPane(layeredPane);

        // --- MAIN UI STRUCTURE ---
        JPanel mainHeader = new JPanel(new BorderLayout());

        // Header Bar (☰ + Title)
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 50));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));

        JButton hamburgerButton = new JButton("☰");
        hamburgerButton.setFocusPainted(false);
        hamburgerButton.setBorderPainted(false);
        hamburgerButton.setContentAreaFilled(false);
        hamburgerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        hamburgerButton.addActionListener(e -> toggleSidebar());
        header.add(hamburgerButton, BorderLayout.WEST);

        JLabel headerTitle = new JLabel("Academic Leader Dashboard", SwingConstants.CENTER);
        headerTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.add(headerTitle, BorderLayout.CENTER);
        mainHeader.add(header, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // --- DASHBOARD PAGE ---
        JPanel dashboardPage = new JPanel(new BorderLayout());
        dashboardPage.setBackground(BACKGROUND_COLOR);
        dashboardPage.setBorder(BorderFactory.createEmptyBorder(20, 30, 30, 30));

        // 1. TOP CONTROLS SECTION
        JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.Y_AXIS)); // Stack rows vertically
        controlsPanel.setOpaque(false);
        controlsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Search Bar
        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchBar.setOpaque(false);
        searchField = new JTextField("Search");
        searchField.setForeground(Color.GRAY);
        searchField.setPreferredSize(new Dimension(380, 38));
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            new javax.swing.border.LineBorder(new Color(210, 210, 210), 1, true),
            BorderFactory.createEmptyBorder(5, 30, 5, 15)
        ));

        JLabel searchIcon = new JLabel("🔍");
        searchIcon.setBounds(10, 0, 20, 38);
        searchIcon.setForeground(Color.GRAY);
        searchField.setLayout(null);
        searchField.add(searchIcon);

        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                // Change border to a darker grey/blue and icon to black when active
                searchField.setBorder(BorderFactory.createCompoundBorder(
                    new javax.swing.border.LineBorder(new Color(100, 100, 100), 2, true),
                    BorderFactory.createEmptyBorder(5, 30, 5, 15)
                ));
                searchIcon.setForeground(Color.BLACK); // Make icon pop

                if (searchField.getText().equals("Search")) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                // Revert to light grey border and icon when user clicks away
                searchField.setBorder(BorderFactory.createCompoundBorder(
                    new javax.swing.border.LineBorder(new Color(210, 210, 210), 1, true),
                    BorderFactory.createEmptyBorder(5, 30, 5, 15)
                ));
                searchIcon.setForeground(Color.GRAY);

                if (searchField.getText().isEmpty()) {
                    searchField.setForeground(Color.GRAY);
                    searchField.setText("Search");
                }
            }
        });

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { checkFilterField(); }
            @Override public void removeUpdate(DocumentEvent e) { checkFilterField(); }
            @Override public void changedUpdate(DocumentEvent e) { checkFilterField(); }
            private void checkFilterField() {
                if (!searchField.getText().equals("Search")) {
                    filterModules();
                }
            }
        });
        searchBar.add(searchField);

        // Create Button Row
        JPanel actionRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        actionRow.setOpaque(false);
        actionRow.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 43));
        JButton createButton = new JButton("➕ Create New Module");
        createButton.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        createButton.setBackground(new Color(40, 167, 69));
        createButton.setForeground(Color.WHITE);
        createButton.setOpaque(true);
        createButton.setBorderPainted(false);
        createButton.setFocusPainted(false);
        createButton.setPreferredSize(new Dimension(180, 35));
        createButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        createButton.addActionListener(e -> showModuleDialog(null));
        actionRow.add(createButton);

        controlsPanel.add(searchBar);
        controlsPanel.add(Box.createVerticalStrut(15));
        controlsPanel.add(actionRow);
        dashboardPage.add(controlsPanel, BorderLayout.NORTH);

        // 2. MODULE REGISTRY CONTAINER
        JPanel registryContainer = new JPanel(new BorderLayout());
        registryContainer.setBackground(new Color(248,250,252));
        //registryContainer.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1));

        centerPanel = new JPanel(new GridLayout(0, 3, 25, 25));
        centerPanel.setBackground(new Color(248,250,252));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 25, 25, 25));

        gridWrapper = new JPanel(new BorderLayout());
        gridWrapper.setBackground(new Color(248,250,252));
        gridWrapper.add(centerPanel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(gridWrapper);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(new Color(248,250,252));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        dashboardPage.add(scrollPane, BorderLayout.CENTER);

        // Click away logic
        java.awt.event.MouseAdapter clickAwayListener = new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setForeground(Color.GRAY);
                    searchField.setText("Search");
                }
                mainHeader.requestFocusInWindow();
            }
        };
        dashboardPage.addMouseListener(clickAwayListener);
        gridWrapper.addMouseListener(clickAwayListener);
        centerPanel.addMouseListener(clickAwayListener);

        // Pages Integration
        academicLeaderReport reportPage = new academicLeaderReport();
        academicLeaderUserProfile userProfilePage = new academicLeaderUserProfile();
        cardPanel.add(dashboardPage, "DASHBOARD");
        cardPanel.add(reportPage, "REPORT");
        cardPanel.add(userProfilePage, "PROFILE");
        mainHeader.add(cardPanel, BorderLayout.CENTER);
        layeredPane.add(mainHeader, JLayeredPane.DEFAULT_LAYER);

        // Sidebar
        sidebarPanel = new academicLeaderDashboardSidebar(() -> toggleSidebar());
        sidebar = sidebarPanel;
        sidebar.setVisible(false);
        layeredPane.add(sidebar, JLayeredPane.MODAL_LAYER);
        sidebarPanel.getDashboardBtn().addActionListener(e -> showPage("DASHBOARD"));
        sidebarPanel.getReportBtn().addActionListener(e -> {reportPage.refreshData(); showPage("REPORT");});
        sidebarPanel.getProfileBtn().addActionListener(e -> showPage("PROFILE"));
        sidebarPanel.getLogoutBtn().addActionListener(e -> {
            int confirmation = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirmation ==JOptionPane.YES_OPTION){
                new login();
                this.dispose();
            }
        });

        glassPane = new JPanel();
        glassPane.setBackground(new Color(0, 0, 0, 50));
        glassPane.setOpaque(false);
        glassPane.setVisible(false);
        glassPane.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) { if (sidebarVisible) toggleSidebar(); }
        });
        layeredPane.add(glassPane, JLayeredPane.PALETTE_LAYER);

        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override public void componentResized(java.awt.event.ComponentEvent e) {
                mainHeader.setBounds(0, 0, layeredPane.getWidth(), layeredPane.getHeight());
                glassPane.setBounds(0, 0, layeredPane.getWidth(), layeredPane.getHeight());
                sidebar.setBounds(0, 0, 240, layeredPane.getHeight());
            }
        });

        refreshDashboard();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new academicLeaderDashboard();
    }

    private void filterModules() {
        String filterText = searchField.getText().toLowerCase();
        List<academicLeaderModule> allModules = academicLeaderFileManager.loadModules();
        List<academicLeaderModule> filtered = allModules.stream().filter(m -> m.getName().toLowerCase().contains(filterText)).collect(Collectors.toList());
        displayModules(filtered);
    }

    private void refreshDashboard() {
        List<academicLeaderModule> modules = academicLeaderFileManager.loadModules();
        displayModules(modules);
    }

    private void displayModules(List<academicLeaderModule> modules) {
        centerPanel.removeAll();
        gridWrapper.removeAll();

        if (modules.isEmpty()){
            gridWrapper.setLayout(new GridBagLayout());

            JPanel emptyBox = new JPanel();
            emptyBox.setLayout(new BoxLayout(emptyBox, BoxLayout.Y_AXIS));
            emptyBox.setOpaque(false);

            JLabel noResultIconLabel = new JLabel("🔍", SwingConstants.CENTER);
            noResultIconLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 30));
            noResultIconLabel.setForeground(new Color(203, 213, 225));
            noResultIconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            noResultIconLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

            JLabel noResultLabel = new JLabel("No modules found matching your search.", SwingConstants.CENTER);
            noResultLabel.setFont(new Font("Segoe UI", Font.ITALIC, 18));
            noResultLabel.setForeground(Color.GRAY);
            noResultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel noResultSubMessage = new JLabel("Try adjusting your search or filters to find what you're looking for.");
            noResultSubMessage.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            noResultSubMessage.setForeground(new Color(148, 163, 184));
            noResultSubMessage.setAlignmentX(Component.CENTER_ALIGNMENT);

            emptyBox.add(noResultIconLabel);
            emptyBox.add(Box.createVerticalStrut(10));
            emptyBox.add(noResultLabel);
            emptyBox.add(Box.createVerticalStrut(8));
            emptyBox.add(noResultSubMessage);

            gridWrapper.add(emptyBox, new GridBagConstraints());

        } else {

            gridWrapper.setLayout(new BorderLayout());
            centerPanel.setLayout(new GridLayout(0, 3, 25, 25));
            gridWrapper.add(centerPanel, BorderLayout.NORTH);

            for (academicLeaderModule m : modules) {
                JPanel moduleCard = new JPanel(new BorderLayout());
                moduleCard.setPreferredSize(new Dimension(260, 287));
                moduleCard.setBackground(Color.WHITE);
                moduleCard.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.darkGray, 1, true),
                    BorderFactory.createEmptyBorder(15, 0, 10, 0)
                ));

                JLabel cardImg = new JLabel("", SwingConstants.CENTER);
                String path = m.getImageFilePath();
                ImageIcon icon = (path == null || path.equals("default") || path.equals("ModuleDefaultPic.png"))
                                 ? new ImageIcon("ModuleDefaultPic.png") : new ImageIcon(path);
                if (icon.getImage() != null) {
                    Image scaled = icon.getImage().getScaledInstance(260, 160, Image.SCALE_SMOOTH);
                    cardImg.setIcon(new ImageIcon(scaled));
                }

                // Info Content
                JPanel contentPanel = new JPanel(new BorderLayout());
                contentPanel.setOpaque(false);
                contentPanel.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));

                JPanel titleRow = new JPanel(new BorderLayout());
                titleRow.setOpaque(false);
                JLabel nameLabel = new JLabel(m.getName());
                nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));

                JButton dotBtn = new JButton("...");
                dotBtn.setFont(new Font("Segoe UI", Font.BOLD, 18));
                dotBtn.setForeground(new Color(180, 180, 180));
                dotBtn.setBorderPainted(false);
                dotBtn.setContentAreaFilled(false);
                dotBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                dotBtn.addActionListener(e -> createCardPopup(m).show(dotBtn, -50, dotBtn.getHeight()));

                titleRow.add(nameLabel, BorderLayout.CENTER);
                titleRow.add(dotBtn, BorderLayout.EAST);

                JPanel subInfo = new JPanel();
                subInfo.setLayout(new BoxLayout(subInfo, BoxLayout.Y_AXIS));
                subInfo.setOpaque(false);
                JLabel qualLabel = new JLabel(m.getQualification());
                qualLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                qualLabel.setForeground(new Color(110, 120, 130));
                qualLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

                JLabel timeLabel = new JLabel(m.getIntakeMonth() + " " + m.getYear());
                timeLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
                timeLabel.setOpaque(true);
                timeLabel.setBackground(new Color(240, 253 ,244));
                timeLabel.setForeground(new Color(22, 163, 74));
                timeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

                subInfo.add(qualLabel);
                subInfo.add(Box.createVerticalStrut(8));
                subInfo.add(timeLabel);

                contentPanel.add(titleRow, BorderLayout.NORTH);
                contentPanel.add(subInfo, BorderLayout.CENTER);

                moduleCard.add(cardImg, BorderLayout.NORTH);
                moduleCard.add(contentPanel, BorderLayout.CENTER);
                //JPanel moduleCard = createModuleCard(m);
                centerPanel.add(moduleCard);
            }
        }
            gridWrapper.revalidate();
            gridWrapper.repaint();
    }

    private JPopupMenu createCardPopup(academicLeaderModule m) {
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2, true));

        JMenuItem editItem = new JMenuItem("Edit Module");
        JMenuItem deleteItem = new JMenuItem("Delete Module");

        editItem.setBackground(Color.WHITE);
        deleteItem.setBackground(Color.WHITE);
        deleteItem.setForeground(new Color(220,53,69));

        JSeparator separator = new JSeparator();
        separator.setBackground(Color.LIGHT_GRAY);

        editItem.addActionListener(e -> showModuleDialog(m));
        deleteItem.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete " + m.getName() + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                academicLeaderFileManager.deleteModule(m.getCode());
                refreshDashboard();
            }
        });

        popupMenu.add(editItem);
        popupMenu.add(separator);
        popupMenu.add(deleteItem);
        return popupMenu;
    }

    public void showModuleDialog(academicLeaderModule editModule) {
        boolean isEdit = (editModule != null);
        JDialog dialog = new JDialog(this, isEdit ? "Edit Module" : "Create New Module", true);
        dialog.setSize(800, 600);
        dialog.getContentPane().setBackground(BACKGROUND_COLOR);

        // --- MAIN SCROLLABLE CONTAINER ---
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(BACKGROUND_COLOR);
        mainContent.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Data tracking
        final String[] defaultPics = {"ModuleDefaultPic.png", "ModuleDefaultPic2.png", "ModuleDefaultPic3.png"};
        final int[] currentIndex = {0};
        final String[] selectedPath = {isEdit ? editModule.getImageFilePath() : defaultPics[0]};

        // image profile
        JPanel imageContainer = new JPanel(new BorderLayout(0, 15));
        imageContainer.setBackground(Color.WHITE);
        imageContainer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel imageHeader = new JLabel("🖼️ Module Profile Picture", SwingConstants.LEFT);
        imageHeader.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        imageHeader.setForeground(new Color(40, 167, 69)); // Success Green

        JLabel imagePlaceholder = new JLabel("", SwingConstants.CENTER);
        imagePlaceholder.setPreferredSize(new Dimension(240, 160));
        imagePlaceholder.setBackground(new Color(245, 245, 245));
        imagePlaceholder.setOpaque(true);
        updateImagePreview(imagePlaceholder, selectedPath[0]);

        JPanel imagePickerRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        imagePickerRow.setOpaque(false);
        JButton leftArrow = new JButton("◀️");
        JButton rightArrow = new JButton("▶️");

        for (JButton btn : new JButton[]{leftArrow, rightArrow}) {
            btn.setFont(new Font("Segoe UI emoji", Font.BOLD, 20));
            btn.setBorderPainted(false);
            btn.setContentAreaFilled(false);
            btn.setFocusPainted(false);
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

        imagePickerRow.add(leftArrow);
        imagePickerRow.add(imagePlaceholder);
        imagePickerRow.add(rightArrow);

        JPanel uploadCustomButton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        uploadCustomButton.setOpaque(false);
        JButton uploadButton = new JButton("Upload Custom Image");
        uploadButton.setPreferredSize(new Dimension(165, 30));
        styleDialogButton(uploadButton);
        uploadButton.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(dialog) == JFileChooser.APPROVE_OPTION) {
                selectedPath[0] = fc.getSelectedFile().getAbsolutePath();
                updateImagePreview(imagePlaceholder, selectedPath[0]);
            }
        });
        uploadCustomButton.add(uploadButton);

        imageContainer.add(imageHeader, BorderLayout.NORTH);
        imageContainer.add(imagePickerRow, BorderLayout.CENTER);
        imageContainer.add(uploadCustomButton, BorderLayout.SOUTH);

        // DETAILS BORDER
        JPanel detailsContainer = new JPanel(new GridBagLayout());
        detailsContainer.setBackground(Color.WHITE);
        detailsContainer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 0, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel detailsHeader = new JLabel("📚 Module Details", SwingConstants.LEFT);
        detailsHeader.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        detailsHeader.setForeground(new Color(40, 167, 69)); // Success Green

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        detailsContainer.add(detailsHeader, gbc);
        gbc.gridwidth = 1;

        // Form Fields
        JTextField codeField = createStyledTextField(isEdit ? editModule.getCode() : "");
        if (isEdit) { codeField.setEditable(false); codeField.setBackground(new Color(245, 245, 245)); }
        JTextField nameField = createStyledTextField(isEdit ? editModule.getName() : "");
        JComboBox<String> qualificationBox = new JComboBox<>(new String[]{"-Select-", "Foundation", "Diploma", "Bachelor's Degree", "Master's Degree", "PhD"});

        List<String> lecturerList = academicLeaderFileManager.checkLecturerNames();
        JComboBox<String> lecturerBox = new JComboBox<>();
        if (lecturerList.isEmpty()) {
            lecturerBox.addItem("No Lecturers Found"); lecturerBox.setEnabled(false);
        } else {
            lecturerBox.addItem("-Select Lecturer-");
            for (String lect : lecturerList) lecturerBox.addItem(lect);
            if (isEdit) lecturerBox.setSelectedItem(editModule.getLecturerID() + " - " + editModule.getLecturerName());
        }

        JComboBox<String> monthBox = new JComboBox<>(new String[]{"-Select-", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"});
        JComboBox<String> yearBox = new JComboBox<>();
        int currentYear = java.time.LocalDate.now().getYear();
        for (int i = 0; i < 5; i++) yearBox.addItem(String.valueOf(currentYear + i));

        JComboBox<String> courseBox = new JComboBox<>();
        courseBox.addItem("-Select Course-");
        try (BufferedReader br = new BufferedReader(new FileReader("coursesInAPU.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) courseBox.addItem(line.trim());
            }
        } catch (IOException e) {
            courseBox.addItem("Error loading file");
        }
        if (isEdit) courseBox.setSelectedItem(editModule.getCourse());

        JTextArea descArea = new JTextArea(4, 20);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JScrollPane descScroll = new JScrollPane(descArea);
        descScroll.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

        if (isEdit) {
            qualificationBox.setSelectedItem(editModule.getQualification());
            lecturerBox.setSelectedItem(editModule.getLecturerName());
            monthBox.setSelectedItem(editModule.getIntakeMonth());
            yearBox.setSelectedItem(String.valueOf(editModule.getYear()));
            descArea.setText(editModule.getDescription());
        }

        addFormField(detailsContainer, "Module Code", codeField, gbc, 1);
        addFormField(detailsContainer, "Module Name", nameField, gbc, 2);
        addFormField(detailsContainer, "Qualification", qualificationBox, gbc, 3);
        addFormField(detailsContainer, "Assigned Lecturer", lecturerBox, gbc, 4);
        addFormField(detailsContainer, "Intake Month", monthBox, gbc, 5);
        addFormField(detailsContainer, "Intake Year", yearBox, gbc, 6);
        addFormField(detailsContainer, "Course", courseBox, gbc, 7);
        addFormField(detailsContainer, "Description", descScroll, gbc, 8);

        // --- BUTTON PANEL (CENTERED) ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setOpaque(false);
        JButton saveBtn = new JButton(isEdit ? "Update Module" : "Save Module");
        JButton cancelBtn = new JButton("Cancel");

        saveBtn.setBackground(new Color(40, 167, 69)); saveBtn.setForeground(Color.WHITE);
        saveBtn.setPreferredSize(new Dimension(140, 38)); saveBtn.setOpaque(true); saveBtn.setBorderPainted(false); saveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        cancelBtn.setBackground(Color.WHITE);
        cancelBtn.setForeground(new Color(70, 70, 70));
        cancelBtn.setPreferredSize(new Dimension(140, 38));
        cancelBtn.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        cancelBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        buttonPanel.add(cancelBtn);
        buttonPanel.add(saveBtn);

        // ASSEMBLY
        mainContent.add(imageContainer);
        mainContent.add(Box.createVerticalStrut(20));
        mainContent.add(detailsContainer);
        mainContent.add(buttonPanel);

        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        dialog.add(scrollPane);

        // Actions
        saveBtn.addActionListener(e -> {
            String code = codeField.getText().trim();
            String name = nameField.getText().trim();
            String description = descArea.getText().trim();
            String selectMonth = (String)monthBox.getSelectedItem();
            String selectQualification = (String)qualificationBox.getSelectedItem();
            String selectedLecturerFull = (String)lecturerBox.getSelectedItem();
            String selectCourse = (String)courseBox.getSelectedItem();
            int selectYear = Integer.parseInt((String)yearBox.getSelectedItem());

            if (code.isEmpty() || name.isEmpty() || description.isEmpty() || monthBox.getSelectedIndex() <= 0 || qualificationBox.getSelectedIndex() <= 0 || lecturerBox.getSelectedIndex() <= 0 || courseBox.getSelectedIndex() <= 0) {
                JOptionPane.showMessageDialog(dialog, "All fields are required!", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (isDateInPast(selectMonth, selectYear)) {
                JOptionPane.showMessageDialog(dialog, "Cannot set intake to a past date.", "Date Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String lectID = selectedLecturerFull.split(" - ")[0].trim();
            String lectName = selectedLecturerFull.split(" - ")[1].trim();

            academicLeaderModule newModule = new academicLeaderModule(code, name, selectQualification, lectID, lectName, selectMonth, selectYear, description, selectedPath[0], selectCourse);

            if (isEdit) {
                academicLeaderFileManager.updateModule(newModule);
                JOptionPane.showMessageDialog(dialog, "Successfully edited module!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                academicLeaderFileManager.saveModule(newModule);
                JOptionPane.showMessageDialog(dialog, "Successfully created new module!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }

            if (!lecturerBox.isEnabled()) {
                JOptionPane.showMessageDialog(dialog, "You cannot save a module without an assigned lecturer. Please register lecturers first.", "Missing Data", JOptionPane.WARNING_MESSAGE);
                return;
            }
            refreshDashboard();
            dialog.dispose();
        });

        cancelBtn.addActionListener(e -> dialog.dispose());
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    // --- HELPER METHODS FOR MODERN LOOK ---

    private void styleDialogButton(JButton btn) {
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private JTextField createStyledTextField(String text) {
        JTextField tf = new JTextField(text);
        tf.setPreferredSize(new Dimension(250, 35));
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return tf;
    }

    private void addFormField(JPanel panel, String label, JComponent field, GridBagConstraints gbc, int row) {
        gbc.gridy = row; gbc.gridx = 0; gbc.weightx = 0.3;
        panel.add(new JLabel(label + ":"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        panel.add(field, gbc);
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
            sidebarPanel.getDashboardBtn().setBackground(defaultColor);
            sidebarPanel.getReportBtn().setBackground(defaultColor);
            sidebarPanel.getProfileBtn().setBackground(defaultColor);
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

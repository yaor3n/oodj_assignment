import javax.swing.*;
import java.awt.*;
import java.util.List;

public class academicLeaderDashboard extends JFrame{
    private JPanel centerPanel;
    private JPanel sidebar;
    private JPanel glassPane;
    private JLayeredPane layeredPane; //for sidebar
    private boolean sidebarVisible = false;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private academicLeaderDashboardSidebar sidebarPanel;
    
    private final Color activeColor = new Color(30,30,30);
    private final Color defaultColor = new Color(70,70,70);
    private final Color BACKGROUND_COLOR = new Color(240, 244, 248); // Ghost White
    private final Color CARD_HOVER_COLOR = new Color(252, 252, 252);
    
    

    public academicLeaderDashboard() {
        reusable.windowSetup(this); 
        layeredPane = new JLayeredPane();
        this.setContentPane(layeredPane);
        
        //header for every page to use
        JPanel mainHeader = new JPanel(new BorderLayout());
        
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 50));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));

        JButton hamburgerButton = new JButton("☰");
        hamburgerButton.setForeground(new Color(50,50,50));
        hamburgerButton.setBorderPainted(false);
        hamburgerButton.setContentAreaFilled(false);
        hamburgerButton.setFocusPainted(false);
        hamburgerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        hamburgerButton.addActionListener(e -> toggleSidebar()); 
        header.add(hamburgerButton, BorderLayout.WEST);

        JLabel headerTitle = new JLabel("Academic Leader Dashboard", SwingConstants.CENTER);
        headerTitle.setForeground(new Color(33,37,41));
        header.add(headerTitle, BorderLayout.CENTER);
        
        mainHeader.add(header, BorderLayout.NORTH);
        
        //card panel
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // --- DASHBOARD ---
        JPanel dashboardPage = new JPanel(new BorderLayout());

        // Create module button
        JPanel actionRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        JButton createButton = new JButton("Create Modules");
        createButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        createButton.setBackground(Color.white);
        createButton.setForeground(Color.BLACK);
        createButton.setFocusPainted(false);
        createButton.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15)); // Padding
        createButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        createButton.setPreferredSize(new Dimension(150, 40));
        createButton.addActionListener(e -> showModuleDialog(null));
        actionRow.add(createButton);
        dashboardPage.add(actionRow, BorderLayout.NORTH);

        // Module Grid
        centerPanel = new JPanel(new GridLayout(0, 3, 20, 20));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JPanel gridWrapper = new JPanel(new BorderLayout());
        gridWrapper.add(centerPanel, BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(gridWrapper);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(15);
        dashboardPage.add(scrollPane, BorderLayout.CENTER);

        // --- REPORT ---
        academicLeaderReport reportPageObject = new academicLeaderReport();

        // --- 3. CONFIGURE CARDPANEL ---
        cardPanel.add(dashboardPage, "DASHBOARD");
        cardPanel.add(reportPageObject, "REPORT");
        mainHeader.add(cardPanel, BorderLayout.CENTER);
        layeredPane.add(mainHeader, JLayeredPane.DEFAULT_LAYER);
      
        // sidebar
        sidebarPanel = new academicLeaderDashboardSidebar(() -> toggleSidebar());
        sidebar = sidebarPanel; 
        sidebar.setVisible(false);
        layeredPane.add(sidebar, JLayeredPane.MODAL_LAYER);
        
        sidebarPanel.getDashboardBtn().addActionListener(e -> showPage("DASHBOARD"));
        sidebarPanel.getReportBtn().addActionListener(e -> showPage("REPORT"));

        // --- 5. SETUP DIMMER & RESIZE ---
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
            JPanel moduleCard = new JPanel(new BorderLayout(0,15));
            moduleCard.setPreferredSize(new Dimension(250, 320)); // Keep this fixed
            moduleCard.setBackground(Color.WHITE);
            moduleCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(240, 240, 240), 1,true), // Very light border
                BorderFactory.createEmptyBorder(12, 12, 12, 12) // Internal padding
            ));

            // Card Top: The 3-dot button
            JPanel cardHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT,5,2));
            cardHeader.setOpaque(false);
            JButton dotBtn = new JButton("...");
            dotBtn.setBorderPainted(false);
            dotBtn.setContentAreaFilled(false);
            dotBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            cardHeader.add(dotBtn);

            JPopupMenu popupMenu = new JPopupMenu();
            JMenuItem editButton = new JMenuItem("EDIT");
            JMenuItem deleteButton = new JMenuItem("DELETE");
            deleteButton.setBackground(Color.RED);

            //edit function
            editButton.addActionListener(e -> showModuleDialog(m));

            //delete function
            deleteButton.addActionListener(e -> {
                int deleteConfirmation = JOptionPane.showConfirmDialog(this,"Are you sure you want to delete " + m.getName() + "module ?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (deleteConfirmation==JOptionPane.YES_OPTION){
                    academicLeaderModuleFileManager.deleteModule(m.getCode());
                    refreshDashboard();
                }
            });

            popupMenu.add(editButton);
            popupMenu.add(deleteButton);

            dotBtn.addActionListener(e -> {
                popupMenu.show(dotBtn, 0, dotBtn.getHeight());
            });

            // Card Middle: Image
            JLabel cardImg = new JLabel("", SwingConstants.CENTER);
            String path = m.getImageFilePath();
            ImageIcon icon = (path == null || path.equals("default") || path.equals("ModuleDefaultPic.png")) ? new ImageIcon("ModuleDefaultPic.png") : new ImageIcon(path);

            if (icon.getImage() != null) {
                Image scaled = icon.getImage().getScaledInstance(250, 190, Image.SCALE_SMOOTH);
                cardImg.setIcon(new ImageIcon(scaled));
                cardImg.setBorder(BorderFactory.createEmptyBorder(-10, 0, 5, 0));
            }

            // Card Bottom: Info Section
            JPanel infoPanel = new JPanel(new GridLayout(3, 1, 0, 2));
            infoPanel.setBackground(Color.WHITE);

            JLabel nameLabel = new JLabel(m.getName(), SwingConstants.LEFT);
            nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            nameLabel.setForeground(new Color(33,37,41));

            JLabel qualLabel = new JLabel(m.getQualification(), SwingConstants.LEFT);
            qualLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            qualLabel.setForeground(new Color(108,117,125));
            
            JLabel timeLabel = new JLabel(m.getIntakeMonth() + " " + m.getYear(), SwingConstants.LEFT);
            timeLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            timeLabel.setForeground(new Color(0, 123, 255));

            infoPanel.add(nameLabel);
            infoPanel.add(qualLabel);
            infoPanel.add(timeLabel);

            // Final Assembly
            moduleCard.add(cardHeader, BorderLayout.NORTH);
            moduleCard.add(cardImg, BorderLayout.CENTER);
            moduleCard.add(infoPanel, BorderLayout.SOUTH);

            centerPanel.add(moduleCard);
        }
        centerPanel.revalidate();
        centerPanel.repaint();
    }

    public void showModuleDialog(academicLeaderModule editModule) {
       boolean isEdit=(editModule !=null);
       JDialog dialog = new JDialog(this, isEdit? "Edit Module":"Create New Module", true);
       dialog.setSize(800, 550);
       dialog.setLayout(new GridBagLayout());
       GridBagConstraints Gbc=new GridBagConstraints();
       dialog.setBackground(new Color(240,240,240));
       
       //default picture
       final String[] defaultPics = {"ModuleDefaultPic.png", "ModuleDefaultPic2.png", "ModuleDefaultPic3.png"};
       final int[] currentIndex = {0};
       final String[] selectedPath = {defaultPics[0]};

       // --- LEFT PANEL ---
       JPanel leftPanel = new JPanel(new GridBagLayout());
       leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
       GridBagConstraints leftGbc = new GridBagConstraints();

       leftGbc.gridx = 0; leftGbc.gridy = 0; leftGbc.gridwidth = 3;
       leftGbc.insets = new Insets(0, 0, 20, 0);
       leftPanel.add(new JLabel("Module Cover Image", SwingConstants.CENTER), leftGbc);

       JButton leftArrow = new JButton("<");
       JButton rightArrow = new JButton(">");
       JLabel imagePlaceholder = new JLabel("No Image", SwingConstants.CENTER);

       leftArrow.addActionListener(e -> {
            currentIndex[0] = (currentIndex[0] - 1 + defaultPics.length) % defaultPics.length; // Loops back to 2 after index 0
            selectedPath[0] = defaultPics[currentIndex[0]];
            updateImagePreview(imagePlaceholder, selectedPath[0]);
        });

        rightArrow.addActionListener(e -> {
            currentIndex[0] = (currentIndex[0] + 1) % defaultPics.length; // Loops back to 0 after index 2
            selectedPath[0] = defaultPics[currentIndex[0]];
            updateImagePreview(imagePlaceholder, selectedPath[0]);
        });
        
        // Styling the arrows (Only need one loop)
       for (JButton btn : new JButton[]{leftArrow, rightArrow}) {
           btn.setContentAreaFilled(false);
           btn.setBorder(BorderFactory.createLineBorder(new Color(240,240,240)));
           btn.setPreferredSize(new Dimension(30, 120));
        }

       imagePlaceholder.setPreferredSize(new Dimension(240, 190)); 
       imagePlaceholder.setOpaque(true);
       imagePlaceholder.setBackground(new Color(230, 230, 230));
       
       // update image preview
       Runnable updateImagePreview=()->{
          ImageIcon defaultIcon = new ImageIcon(selectedPath[0]);
          if (defaultIcon.getImage()!=null){
              Image img = defaultIcon.getImage().getScaledInstance(250, 190, Image.SCALE_SMOOTH);
              imagePlaceholder.setIcon(new ImageIcon(img));
          }
       };

       updateImagePreview.run();

       leftGbc.gridwidth = 1; leftGbc.gridy = 1;
       leftGbc.gridx = 0; leftPanel.add(leftArrow, leftGbc);
       leftGbc.gridx = 1; leftPanel.add(imagePlaceholder, leftGbc);
       leftGbc.gridx = 2; leftPanel.add(rightArrow, leftGbc);

       //Upload profile pic button
       JButton uploadBtn = new JButton("Upload profile picture");
       leftGbc.gridx = 0; leftGbc.gridy = 2; leftGbc.gridwidth = 3;
       leftPanel.add(uploadBtn, leftGbc);

       uploadBtn.addActionListener(e -> {
           JFileChooser fileChooser = new JFileChooser();
           fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Image Files", "jpg", "png", "jpeg"));
           int result = fileChooser.showOpenDialog(dialog);
           if (result == JFileChooser.APPROVE_OPTION) {
               selectedPath[0] = fileChooser.getSelectedFile().getAbsolutePath();
               ImageIcon icon = new ImageIcon(selectedPath[0]);
               Image scaled = icon.getImage().getScaledInstance(250, 200, Image.SCALE_SMOOTH);
               imagePlaceholder.setIcon(new ImageIcon(scaled));
               updateImagePreview(imagePlaceholder, selectedPath[0]);//use to overtake the default pic
               imagePlaceholder.setText("");
           }
       });

       // --- RIGHT PANEL ---
       JPanel rightPanel = new JPanel(new GridBagLayout());
       rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 20));
       GridBagConstraints rightgbc = new GridBagConstraints();
       rightgbc.insets = new Insets(5, 5, 5, 5);
       rightgbc.fill = GridBagConstraints.HORIZONTAL;

       JTextField codeField = new JTextField(20);
       JTextField nameField = new JTextField(20);
       JComboBox<String> qualificationBox = new JComboBox<>(new String[]{"-Select-", "Foundation", "Certificate", "Diploma", "Bachelor's Degree", "Master's Degree", "PhD"});

       List<String> lecturerList = academicLeaderModuleFileManager.checkLecturerNames();
       JComboBox<String> lecturerBox = new JComboBox<>();
       lecturerBox.addItem("-Select-"); // Add default first
        if (lecturerList.isEmpty()) {
            lecturerBox.addItem("No Lecturers Found");
        } else {
            for (String name : lecturerList) {lecturerBox.addItem(name);}
        }
        
       String[] months = {"-Select-", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
       JComboBox<String> intakeMonthBox = new JComboBox<>(months);
       
       int currentYear = java.time.LocalDate.now().getYear();
        DefaultComboBoxModel<String> yearModel = new DefaultComboBoxModel<>();
        for (int i = 0; i < 5; i++) {
            yearModel.addElement(String.valueOf(currentYear + i));
        }
        JComboBox<String> yearBox = new JComboBox<>(yearModel);

        if (editModule != null) {
            yearBox.setSelectedItem(String.valueOf(editModule.getYear()));
        } else {
            yearBox.setSelectedItem(String.valueOf(currentYear));
        }

       JTextArea descriptionArea = new JTextArea("", 5, 20);
       descriptionArea.setLineWrap(true);
       descriptionArea.setWrapStyleWord(true);
       JScrollPane descriptionScroll = new JScrollPane(descriptionArea);

       rightgbc.gridx = 0; rightgbc.gridy = 0; rightPanel.add(new JLabel("Module Code:"), rightgbc);
       rightgbc.gridx = 1; rightPanel.add(codeField, rightgbc);
       rightgbc.gridx = 0; rightgbc.gridy = 1; rightPanel.add(new JLabel("Module Name:"), rightgbc);
       rightgbc.gridx = 1; rightPanel.add(nameField, rightgbc);
       rightgbc.gridx = 0; rightgbc.gridy = 2; rightPanel.add(new JLabel("Qualification:"), rightgbc);
       rightgbc.gridx = 1; rightPanel.add(qualificationBox, rightgbc);
       rightgbc.gridx = 0; rightgbc.gridy = 3; rightPanel.add(new JLabel("Lecturer:"), rightgbc);
       rightgbc.gridx = 1; rightPanel.add(lecturerBox, rightgbc);
       rightgbc.gridx = 0; rightgbc.gridy = 4; rightPanel.add(new JLabel("Intake Months:"), rightgbc);
       rightgbc.gridx = 1; rightPanel.add(intakeMonthBox, rightgbc);
       rightgbc.gridx = 0; rightgbc.gridy = 5; rightPanel.add(new JLabel("Intake Year:"), rightgbc);
       rightgbc.gridx = 1; rightPanel.add(yearBox, rightgbc);
       rightgbc.gridx = 0; rightgbc.gridy = 6; rightgbc.anchor = GridBagConstraints.NORTHWEST; rightPanel.add(new JLabel("Description:"), rightgbc);
       rightgbc.gridx = 1; rightgbc.weighty = 1.0; rightPanel.add(descriptionScroll, rightgbc);

       Gbc.gridx=0; Gbc.gridy=0; Gbc.anchor=GridBagConstraints.CENTER;

       dialog.add(leftPanel,Gbc);
       Gbc.gridx=1;
       dialog.add(rightPanel,Gbc);
       
       if(isEdit){
           codeField.setText(editModule.getCode());
           codeField.setEditable(false);
           codeField.setBackground(new Color(230,230,230));
           
           nameField.setText(editModule.getName());
           qualificationBox.setSelectedItem(editModule.getQualification());
           lecturerBox.setSelectedItem(editModule.getLecturerName());
           descriptionArea.setText(editModule.getDescription());
       }
       // --- BUTTON PANEL ---

       JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
       JButton saveButton = new JButton(isEdit?"Update":"Create");
       JButton cancelButton = new JButton("Cancel");

       Gbc.gridx = 0; Gbc.gridy = 1; Gbc.gridwidth = 2;
       Gbc.fill = GridBagConstraints.HORIZONTAL;
       Gbc.insets = new Insets(20, 0, 0, 20); // Adds spacing above buttons
       dialog.add(buttonPanel, Gbc);

       saveButton.addActionListener(e -> {
           String code = codeField.getText().trim();
           String name = nameField.getText().trim();
           String description = descriptionArea.getText().trim();

           if (code.isEmpty() || name.isEmpty() || description.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "All fields are required !", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
           }
           if (qualificationBox.getSelectedIndex()<=0){
               JOptionPane.showMessageDialog(dialog, "Please select a Qualification.", "Selection Error", JOptionPane.ERROR_MESSAGE);
               return;
           }
           if (lecturerBox.getSelectedIndex()<=0){
               JOptionPane.showMessageDialog(dialog, "Please select a Lecturer.", "Selection Error", JOptionPane.ERROR_MESSAGE);
               return;
           }
           if (intakeMonthBox.getSelectedIndex()<=0){
               JOptionPane.showMessageDialog(dialog, "Please select a Module Intake Month.", "Selection Error", JOptionPane.ERROR_MESSAGE);
               return;
           }
//           if (yearBox.getSelectedIndex()<=0){
//               JOptionPane.showMessageDialog(dialog, "Please select a year for this module.", "Selection Error", JOptionPane.ERROR_MESSAGE);
//               return;
//           }
           
           String selectedMonth = (String) intakeMonthBox.getSelectedItem();
            int selectedYear = Integer.parseInt((String) yearBox.getSelectedItem());

            // date validation
            if (isDateInPast(selectedMonth, selectedYear)) {
                JOptionPane.showMessageDialog(dialog, 
                    "Error: You cannot create a module for a date that has already passed (" + selectedMonth + " " + selectedYear + ").", 
                    "Invalid Date", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
           academicLeaderModule newModule = new academicLeaderModule(code, name, (String)qualificationBox.getSelectedItem(), (String)lecturerBox.getSelectedItem(), (String)intakeMonthBox.getSelectedItem(), selectedYear , description, selectedPath[0]);
           
           if(isEdit){
               academicLeaderModuleFileManager.updateModule(newModule);
               JOptionPane.showMessageDialog(dialog, "Module Updated!");
           }else{
               academicLeaderModuleFileManager.saveModule(newModule);
               JOptionPane.showMessageDialog(dialog, "Module Created!");
           }
           //JOptionPane.showMessageDialog(dialog, "Module Created Successfully!");
           refreshDashboard();
           dialog.dispose();
       });

       cancelButton.addActionListener(e -> dialog.dispose());
       buttonPanel.add(cancelButton);
       buttonPanel.add(saveButton);

       dialog.setLocationRelativeTo(this);
       dialog.setVisible(true);
   }

    private void toggleSidebar() {
        sidebarVisible = !sidebarVisible;
        sidebar.setVisible(sidebarVisible); // Show menu
        glassPane.setVisible(sidebarVisible); // Show dimmer/click-area
        layeredPane.repaint();
    }

    // Helper to handle scaling and updating the label icon
    private void updateImagePreview(JLabel placeholder, String path) {
        ImageIcon icon = new ImageIcon(path);
        if (icon.getImage() != null) {
            // Ensure the scaling (250, 190) matches your imagePlaceholder.setPreferredSize exactly
            Image scaled = icon.getImage().getScaledInstance(250, 190, Image.SCALE_SMOOTH);
            placeholder.setIcon(new ImageIcon(scaled));
        }
    }
    
    private void showPage(String pageName) {
        cardLayout.show(cardPanel, pageName);

        // Safety check to prevent NullPointerException
        if (sidebarPanel != null) {
            sidebarPanel.getDashboardBtn().setBackground(defaultColor);
            sidebarPanel.getReportBtn().setBackground(defaultColor);

            if (pageName.equals("DASHBOARD")) {
                sidebarPanel.getDashboardBtn().setBackground(activeColor);
            } else {
                sidebarPanel.getReportBtn().setBackground(activeColor);
            }
            sidebarPanel.repaint();
        }

        if (sidebarVisible) toggleSidebar(); 
    }
    
    private boolean isDateInPast(String selectedMonth, int selectedYear) {
        // 1. Get current month and year
        java.time.LocalDate now = java.time.LocalDate.now();
        int currentMonth = now.getMonthValue(); // 1 - 12
        int currentYear = now.getYear();

        // 2. Convert month name to number
        java.time.format.DateTimeFormatter parser = java.time.format.DateTimeFormatter.ofPattern("MMMM", java.util.Locale.ENGLISH);
        int selectedMonthInt = java.time.Month.valueOf(selectedMonth.toUpperCase()).getValue();

        // 3. Compare
        if (selectedYear < currentYear) {
            return true; // Year is definitely in the past
        } else if (selectedYear == currentYear) {
            return selectedMonthInt < currentMonth; // Same year, check if month has passed
        }

        return false; // Future year
    }
    

    
 }
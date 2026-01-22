//master-detail layout
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class academicLeaderReport extends JPanel {
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private int count;
    private String topIntakeMonth;
    private int maxCount;
    private DefaultTableModel moduleTableModel; 
    private DefaultTableModel lecturerTableModel;
    private JLabel totalModuleValue;
    private JLabel intakeValue;
    private JLabel totalLecturerValue;
    private int LecturerCount;
    
    
    public academicLeaderReport() {
      this.setLayout(new BorderLayout());
      
      //left navigation panel
      JPanel leftNavigation=new JPanel();
      leftNavigation.setLayout(new BoxLayout(leftNavigation,BoxLayout.Y_AXIS));
      leftNavigation.setPreferredSize(new Dimension(280,0));
      leftNavigation.setBackground(new Color(248, 250, 252));
      leftNavigation.setBorder(BorderFactory.createMatteBorder(0,0,0,1,new Color(226,232,240)));
      leftNavigation.add(leftNavigationTitle("🗂️ MODULE ANALYTICS"));
      
      //dropdown
      leftNavigation.add(dropdownGroup("📈 Module Performance Summary", new String[]{"Total Modules Created"}));
      leftNavigation.add(dropdownGroup("👥 Student Enrollment Summary", new String[]{"Enrollment Trends"}));
      leftNavigation.add(dropdownGroup("📝 Assessment Performance", new String[]{"Pass/Fail Rates"}));
      leftNavigation.add(dropdownGroup("👨‍🏫 Lecturer Performance", new String[]{"Lecturer Overview", "Lecturer Rating"}));
      this.add(new JScrollPane(leftNavigation),BorderLayout.WEST);
      
      leftNavigation.add(Box.createVerticalStrut(20)); 
      leftNavigation.add(leftNavigationTitle("💬 STUDENT FEEDBACK"));
      
      //right panel
      cardLayout=new CardLayout();
      cardPanel=new JPanel(cardLayout);
      cardPanel.setBackground(Color.WHITE);
      
      //right default page
      JPanel defaultPage=new JPanel(new GridBagLayout());
      defaultPage.setBackground(Color.WHITE);
      JLabel defaultLabel=new JLabel("Select a report to view");
      defaultLabel.setFont(new Font("Segoe UI",Font.PLAIN,16));
      defaultLabel.setForeground(Color.GRAY);
      defaultPage.add(defaultLabel);
      
      cardPanel.add(defaultPage,"EMPTY");
      cardPanel.add(totalModuleReport(),"Total Modules Created");
      
      cardPanel.add(defaultPage,"EMPTY");
      cardPanel.add(lecturerReport(),"Lecturer Overview");
      
      this.add(cardPanel, BorderLayout.CENTER);
      cardLayout.show(cardPanel, "EMPTY");
    }
    
    private JLabel leftNavigationTitle(String text){
        JLabel reportTypeTitle=new JLabel(text);
        reportTypeTitle.setOpaque(true);
        reportTypeTitle.setFont(new Font("Segoe UI Emoji",Font.BOLD,14));
        reportTypeTitle.setBackground(new Color(226, 232, 240));
        reportTypeTitle.setForeground(new Color(100,116,139));
        //reportTypeTitle.setBorder(BorderFactory.createEmptyBorder(25,24,10,10));
        
        reportTypeTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        reportTypeTitle.setHorizontalAlignment(SwingConstants.LEFT);
        reportTypeTitle.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35)); 
        reportTypeTitle.setBorder(BorderFactory.createEmptyBorder(10, 24, 10, 0));
        return reportTypeTitle;
    }
    
    private JPanel dropdownGroup(String title,String[]items){
        JPanel dropdown=new JPanel();
        //dropdown
        dropdown.setLayout(new BoxLayout(dropdown, BoxLayout.Y_AXIS));
        dropdown.setAlignmentX(Component.LEFT_ALIGNMENT);
        dropdown.setOpaque(false);
        
        JButton mainReportType=new JButton (title);
        styleNavigationButton(mainReportType,true);
        
        JPanel itemPanel=new JPanel();
        itemPanel.setLayout(new BoxLayout(itemPanel,BoxLayout.Y_AXIS));
        itemPanel.setOpaque(false);
        itemPanel.setVisible(false);
        
        for (String item : items) {
            JButton subButton = new JButton("      " + item);
            styleNavigationButton(subButton, false);
            subButton.addActionListener(e -> cardLayout.show(cardPanel, item));
            addHoverEffect(subButton);
            itemPanel.add(subButton);
            itemPanel.add(Box.createVerticalStrut(2));
        }
        
        mainReportType.addActionListener(e -> {
            boolean isVisible = itemPanel.isVisible();
            itemPanel.setVisible(!isVisible);
            // Highlight the parent when open
            mainReportType.setOpaque(!isVisible);
            mainReportType.setBackground(new Color(241, 245, 249)); 
        });
        
        dropdown.add(mainReportType);
        dropdown.add(itemPanel);
        addHoverEffect(mainReportType);       
        return dropdown;
    }
    
    private void styleNavigationButton(JButton btn, boolean isParent) {
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        if (isParent) {
            btn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
            btn.setForeground(new Color(30, 41, 59)); 
        } else {
            btn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
            btn.setForeground(new Color(71, 85, 105)); 
        }

        btn.setContentAreaFilled(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 24, 8, 16));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    private void addHoverEffect(JButton hover) {
        // Very light grey tint for hover
        Color hoverBackground = new Color(241, 245, 249); 

        hover.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                hover.setOpaque(true);
                hover.setBackground(hoverBackground);
                hover.repaint();
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                hover.setOpaque(false);
                hover.repaint();
            }
        });
    }

    private JButton createPlaceholderBtn(String text) {
        JButton btn = new JButton(text);
        styleNavigationButton(btn, false);
        btn.setEnabled(false);
        return btn;
    }
    
    private JPanel totalModuleReport(){
        JPanel totalModule = new JPanel(new BorderLayout(0,20));
        totalModule.setBackground(Color.WHITE);
        totalModule.setBorder(BorderFactory.createEmptyBorder(10, 30, 30, 30));
        
        JPanel topSection = new JPanel();
        topSection.setLayout(new BoxLayout(topSection, BoxLayout.Y_AXIS));
        topSection.setOpaque(false);
        topSection.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel("Total Modules Created Details");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        topSection.add(title);
        topSection.add(Box.createVerticalStrut(15));
        
        List<academicLeaderModule> modules = academicLeaderFileManager.loadModules();
        count = modules.size();
        
        JPanel cardAligner = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        cardAligner.setOpaque(false);
        cardAligner.setAlignmentX(Component.LEFT_ALIGNMENT);

        // REUSING REUSABLE METHOD
        totalModuleValue = new JLabel(String.valueOf(count));
        cardAligner.add(createSummaryCard("TOTAL MODULES CREATED", totalModuleValue, new Color(40, 167, 69)));
        cardAligner.add(Box.createHorizontalStrut(15));

        // Intake logic variables stay the same
        intakeValue = new JLabel("Loading..."); 
        cardAligner.add(createSummaryCard("PEAK INTAKE MONTH", intakeValue, new Color(40, 167, 69)));

        topSection.add(cardAligner);
        topSection.add(Box.createVerticalStrut(25));

        //filter
        JPanel filterContainer=new JPanel(new BorderLayout());
        filterContainer.setOpaque(false);
        filterContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        filterContainer.setBorder(BorderFactory.createEmptyBorder(30,0,0,0));
              
        JPanel filter=new JPanel(new FlowLayout(FlowLayout.RIGHT,0,0));
        filter.setOpaque(false);
        
        JLabel filterLabel=new JLabel("Filter by:");
        filterLabel.setFont(new Font("Segoe UI", Font.BOLD,12));
        filterLabel.setForeground(new Color(100, 100, 100));
        
        //year filter
        java.util.Set<String> yearFromList = new java.util.TreeSet<>();
        for (academicLeaderModule m: modules){
            yearFromList.add(String.valueOf(m.getYear()));
        }
        
        int currentYear = java.time.LocalDate.now().getYear();
        yearFromList.add(String.valueOf(currentYear));
        yearFromList.add(String.valueOf(currentYear + 1));
        
        java.util.List<String> yearList = new ArrayList<>();
        yearList.add("All Years");
        yearList.addAll(yearFromList);        
        JComboBox<String> yearFilter = new JComboBox<>(yearList.toArray(new String[0]));
        
        //month filter
        String[] months = {"All Months", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        JComboBox<String> monthFilter = new JComboBox<>(months);

        //filter.add(filterLabel);
        JLabel filterByIntake=new JLabel("Intake:");
        filterByIntake.setFont(new Font("Segoe UI", Font.BOLD,12));
        JLabel filterByYear=new JLabel("Year:");
        filterByYear.setFont(new Font("Segoe UI", Font.BOLD,12));
        
        //clear filter
        JButton clearFilterButton=new JButton("Clear Filters");
        clearFilterButton.setFont(new Font("Segoe UI",Font.BOLD,12));
        clearFilterButton.setBackground(new Color(40,167,69));
        clearFilterButton.setForeground(Color.WHITE);
        clearFilterButton.setOpaque(true);
        clearFilterButton.setContentAreaFilled(true);
        clearFilterButton.setBorderPainted(false);
        clearFilterButton.setFocusPainted(false);
        clearFilterButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        clearFilterButton.setPreferredSize(new Dimension(110, 30));
        
        clearFilterButton.addActionListener(e->{
            monthFilter.setSelectedIndex(0);
            yearFilter.setSelectedIndex(0);
        });
        
        filter.add(filterLabel);
        filter.add(Box.createHorizontalStrut(10));
        filter.add(filterByIntake);
        filter.add(Box.createHorizontalStrut(5));
        filter.add(monthFilter);
        filter.add(Box.createHorizontalStrut(15)); 
        filter.add(filterByYear);
        filter.add(Box.createHorizontalStrut(5));
        filter.add(yearFilter);
        filter.add(Box.createHorizontalStrut(15));      
        
        filter.add(clearFilterButton);
        filterContainer.add(filter,BorderLayout.EAST);
        filterContainer.add(Box.createVerticalStrut(44));
        topSection.add(filterContainer, BorderLayout.SOUTH);
        totalModule.add(topSection, BorderLayout.NORTH);
        
        //no result
        JPanel noResult = new JPanel(new GridBagLayout());
        noResult.setBackground(Color.WHITE);
        noResult.setOpaque(true);
        
        JPanel noResultContent=new JPanel();
        noResultContent.setLayout(new BoxLayout(noResultContent,BoxLayout.Y_AXIS));
        noResultContent.setOpaque(false);
        
        JLabel noResultIconLabel = new JLabel("🔍");
        noResultIconLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 24));
        noResultIconLabel.setForeground(new Color(203, 213, 225));
        noResultIconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);          
        noResultIconLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JLabel emptyText = new JLabel("NO RESULT FOUND");
        emptyText.setFont(new Font("Segoe UI", Font.BOLD, 14));
        emptyText.setForeground(new Color(148,163,184));
        emptyText.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subEmptyText = new JLabel("Try adjusting your filters to find what you're looking for.");
        subEmptyText.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subEmptyText.setForeground(new Color(160,174,192));
        subEmptyText.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        noResultContent.add(noResultIconLabel);
        noResultContent.add(emptyText);
        noResultContent.add(subEmptyText);
        noResult.add(noResultContent);
        
        // Inside totalModuleReport() (table)
        String[] columns = {"Module Code", "Module Name", "Level", "Lecturer","Intake Month","Year"};
        moduleTableModel = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int r,int c){return false;}
        };

        // Populate with data from your file manager
        for (academicLeaderModule m : modules) {
            moduleTableModel.addRow(new Object[]{m.getCode(), m.getName(), m.getQualification(), m.getLecturerName(), m.getIntakeMonth(), m.getYear()});
        }
        
        //table
        JTable moduleTable = new JTable(moduleTableModel) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component component = super.prepareRenderer(renderer, row, column);              
                if (!isRowSelected(row)) {
                    component.setBackground(row % 2 == 0 ? Color.WHITE : new Color(242, 242, 242));
                }
                
                if (component instanceof JComponent) {
                    ((JComponent) component).setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                }
                return component;
            }
        };
        
        moduleTable.setRowHeight(31); 
        moduleTable.setGridColor(new Color(230,230,230));
        moduleTable.setShowGrid(true);
        moduleTable.setIntercellSpacing(new Dimension(1, 1));
        moduleTable.getTableHeader().setPreferredSize(new Dimension(0, 25));      
        moduleTable.getTableHeader().setDefaultRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel header = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                header.setBackground(new Color(226, 232, 240)); 
                header.setFont(new Font("Segoe UI", Font.BOLD, 12));
                header.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(203, 213, 225)),BorderFactory.createEmptyBorder(0, 10, 0, 0)));
                return header;
            }
        });
        
        //make the table header move to left
        ((JLabel)moduleTable.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.LEFT);
        
        CardLayout tableCardLayout = new CardLayout();
        JPanel tableContainer = new JPanel(tableCardLayout);
        tableContainer.setOpaque(false);
        
        tableContainer.add(moduleTable, "TABLE");
        tableContainer.add(noResult, "EMPTY");
        
        JScrollPane scrollPane = new JScrollPane(tableContainer);
        //scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230,230,230),1));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        scrollPane.setColumnHeaderView(moduleTable.getTableHeader());
        
        JPanel headerCorner = new JPanel();
        headerCorner.setBackground(new Color(226, 232, 240));
        headerCorner.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(203, 213, 225)));
        scrollPane.setCorner(JScrollPane.UPPER_RIGHT_CORNER, headerCorner);
        
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(moduleTableModel);
        moduleTable.setRowSorter(sorter);
        
        java.awt.event.ActionListener filterAction = e -> {
            String selectedYear = (String) yearFilter.getSelectedItem();
            String selectedMonth = (String) monthFilter.getSelectedItem();
            
            List<RowFilter<Object, Object>> filters = new ArrayList<>();           
            if (!selectedYear.equals("All Years")) {filters.add(RowFilter.regexFilter(selectedYear, 5));}
            if (!selectedMonth.equals("All Months")) {filters.add(RowFilter.regexFilter(selectedMonth, 4));}
            
            sorter.setRowFilter(RowFilter.andFilter(filters));
            
            if (moduleTable.getRowCount()==0){
                tableCardLayout.show(tableContainer, "EMPTY");
            }else{
                tableCardLayout.show(tableContainer, "TABLE");
            }
            
            scrollPane.revalidate();
            scrollPane.repaint();
        };

        yearFilter.addActionListener(filterAction);
        monthFilter.addActionListener(filterAction);
        
        String currentTime = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(java.time.LocalDateTime.now());
        JLabel lastUpdatedLabel = new JLabel("Last synced: " + currentTime);
        lastUpdatedLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lastUpdatedLabel.setForeground(Color.GRAY);
        
        //export csv
        JButton exportButton = new JButton("\u2913 Export to CSV");
        exportButton.setFont(new Font("Segoe UI Symbol", Font.BOLD, 12));
        exportButton.setBackground(new Color(245, 245, 245));
        exportButton.setForeground(new Color(70, 70, 70));
        exportButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exportButton.setPreferredSize(new Dimension(130, 25));
        exportButton.setFocusPainted(false);

        // 2. Set the Action
        exportButton.addActionListener(e -> {
            exportToCSV(moduleTable, String.valueOf(count), topIntakeMonth + " (" + maxCount + ")");
        });
        // 3. Create a Bottom Action Row
        JPanel actionRow = new JPanel(new BorderLayout()); // Aligned with the table edge
        actionRow.setOpaque(false);
        actionRow.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));
        actionRow.add(lastUpdatedLabel, BorderLayout.WEST);
        actionRow.add(exportButton,BorderLayout.EAST);

        JPanel registryPanel = new JPanel(new BorderLayout(0,10));
        registryPanel.setOpaque(false);
        JLabel registryTitle = new JLabel("📊 Complete Module Registry");
        registryTitle.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        registryPanel.add(registryTitle, BorderLayout.NORTH);       
        registryPanel.add(scrollPane, BorderLayout.CENTER); 
        registryPanel.add(actionRow, BorderLayout.SOUTH);
        
        totalModule.add(topSection, BorderLayout.NORTH);
        totalModule.add(registryPanel, BorderLayout.CENTER);
        return totalModule;
    }
    
    private JPanel lecturerReport() {
        JPanel lecturerOverview = new JPanel(new BorderLayout(0, 20));
        lecturerOverview.setBackground(Color.WHITE);
        lecturerOverview.setBorder(BorderFactory.createEmptyBorder(10, 30, 30, 30));

        // --- 1. TOP SECTION (Dashboard Header) ---
        JPanel topSection = new JPanel();
        topSection.setLayout(new BoxLayout(topSection, BoxLayout.Y_AXIS));
        topSection.setOpaque(false);
        topSection.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel("Lecturer Performance Overview");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        topSection.add(title);
        topSection.add(Box.createVerticalStrut(15));

        totalLecturerValue = new JLabel("0");
        JPanel cardAligner = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        cardAligner.setOpaque(false);
        cardAligner.add(academicLeaderReportUITools.createSummaryCard("TOTAL LECTURERS", totalLecturerValue, new Color(0, 123, 255)));
        topSection.add(cardAligner);
        topSection.add(Box.createVerticalStrut(25));

        // --- 2. SEARCH BAR UI ---
        JPanel filterContainer = new JPanel(new BorderLayout());
        filterContainer.setOpaque(false);
        filterContainer.add(new JLabel("📊 Lecturer Workload Registry") {{ setFont(new Font("Segoe UI Emoji", Font.BOLD, 14)); }}, BorderLayout.WEST);

        JPanel searchGroup = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        searchGroup.setOpaque(false);
        
        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(200, 30));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(203, 213, 225)), 
            BorderFactory.createEmptyBorder(0, 10, 0, 10)
        ));
        searchGroup.add(new JLabel("Filter by Name:"));
        searchGroup.add(searchField);
        filterContainer.add(searchGroup, BorderLayout.EAST);
        topSection.add(filterContainer);
        //topSection.add(Box.createVerticalStrut(10));

        // --- 3. TABLE INITIALIZATION ---
        String[] columns = {"Staff ID", "Full Name", "Email Address", "Gender", "Date of Birth", "Status"};
        lecturerTableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable lecturerTable = new JTable(lecturerTableModel);
        JScrollPane scrollPane = new JScrollPane(lecturerTable);
        academicLeaderReportUITools.reportTable(lecturerTable, scrollPane);

        // --- 4. PERSISTENT HEADER SWITCHER LOGIC ---
        CardLayout lectCardLayout = new CardLayout();
        JPanel lectSwitcher = new JPanel(lectCardLayout);
        lectSwitcher.setOpaque(false);

        // View 1: Normal Table
        lectSwitcher.add(scrollPane, "TABLE");

        // View 2: Empty View that steals the Table Header to keep it visible
        JPanel emptyViewWithHeader = new JPanel(new BorderLayout());
        emptyViewWithHeader.setOpaque(false);
        emptyViewWithHeader.setBackground(Color.WHITE);
        emptyViewWithHeader.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        emptyViewWithHeader.add(lecturerTable.getTableHeader(), BorderLayout.NORTH); // Header fixed at top
        emptyViewWithHeader.add(academicLeaderReportUITools.noResultPanel(), BorderLayout.CENTER);
        lectSwitcher.add(emptyViewWithHeader, "EMPTY");

        // --- 5. SEARCH LISTENER ---
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(lecturerTableModel);
        lecturerTable.setRowSorter(sorter);

        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void updateFilter() {
                String text = searchField.getText();
                sorter.setRowFilter(text.trim().isEmpty() ? null : RowFilter.regexFilter("(?i)" + text));
                if (lecturerTable.getRowCount() == 0) lectCardLayout.show(lectSwitcher, "EMPTY");
                else lectCardLayout.show(lectSwitcher, "TABLE");

                lectSwitcher.revalidate();
                lectSwitcher.repaint();
            }
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateFilter(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateFilter(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateFilter(); }
        });

        // --- 6. ACTION ROW (Sync and Export) ---
        JButton exportButton = academicLeaderReportUITools.createExportButton();
        exportButton.addActionListener(e -> {
            List<String> summary = new ArrayList<>();
            summary.add("Total Lecturers Registered," + totalLecturerValue.getText());
            academicLeaderReportUITools.exportToCSV(lecturerTable, "Lecturer_Overview.csv", summary);
        });

        JPanel actionRow = academicLeaderReportUITools.createActionRow(academicLeaderReportUITools.syncTimeLabel(), exportButton);

        // --- 7. FINAL ASSEMBLY ---
        JPanel registryPanel = new JPanel(new BorderLayout(0, 10));
        registryPanel.setOpaque(false);
        registryPanel.add(lectSwitcher, BorderLayout.CENTER); // ADD Switcher Unit
        registryPanel.add(actionRow, BorderLayout.SOUTH);      // ADD Button Unit

        lecturerOverview.add(topSection, BorderLayout.NORTH);
        lecturerOverview.add(registryPanel, BorderLayout.CENTER);
        
        refreshData();
        return lecturerOverview;
    }
    
    public void refreshData() {
        List<academicLeaderModule> modules = academicLeaderFileManager.loadModules();

        if (moduleTableModel != null) {
            moduleTableModel.setRowCount(0);
            for (academicLeaderModule m : modules) {
                moduleTableModel.addRow(new Object[]{
                    m.getCode(), m.getName(), m.getQualification(), 
                    m.getLecturerName(), m.getIntakeMonth(), m.getYear()
                });
            }
        }
        
        count = modules.size();
        if (totalModuleValue != null) totalModuleValue.setText(String.valueOf(count));

        java.util.Map<String, Integer> intakeCounts = new java.util.HashMap<>();
        for (academicLeaderModule m : modules) {
            String month = m.getIntakeMonth() + " " + m.getYear();
            intakeCounts.put(month, intakeCounts.getOrDefault(month, 0) + 1);
        }
        topIntakeMonth = "N/A";
        maxCount = 0;
        for (var entry : intakeCounts.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                topIntakeMonth = entry.getKey();
            }
        }
        if (intakeValue != null) intakeValue.setText(topIntakeMonth + " (" + maxCount + ")");
        
        if (lecturerTableModel != null) {
            lecturerTableModel.setRowCount(0);
            int totalLecs = 0;

            // Use try-with-resources to read the accounts file
            try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader("accounts.txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;

                    String[] parts = line.split(","); // Define parts here

                    // Check if the role at index 9 is "Lecturer"
                    if (parts.length >= 10 && parts[9].trim().equalsIgnoreCase("Lecturer")) {
                        String id = parts[0].trim();
                        String name = parts[1].trim();
                        String email = parts[2].trim();
                        String gender = parts[3].trim();
                        String dob = parts[4].trim();

                        // Check if lecturer is assigned to any current modules
                        boolean isActive = modules.stream()
                            .anyMatch(m -> m.getLecturerName().equalsIgnoreCase(name));

                        lecturerTableModel.addRow(new Object[]{
                            id, name, email, gender, dob, (isActive ? "Active" : "Inactive")
                        });
                        totalLecs++;
                    }
                }
                if (totalLecturerValue != null) totalLecturerValue.setText(String.valueOf(totalLecs));
            } catch (java.io.IOException e) {
                System.err.println("Error reading accounts.txt: " + e.getMessage());
            }
        }
    }
    
    private JPanel createSummaryCard(String titleText, JLabel valueLabel, Color accentColor) {
        JPanel summaryCard = new JPanel(new BorderLayout());
        summaryCard.setPreferredSize(new Dimension(210, 80));
        summaryCard.setBackground(Color.WHITE);
        summaryCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(226, 232, 240)),
            BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 5, 0, 0, new Color(40,167,69)),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
            )
        ));

        JLabel titleLabel = new JLabel(titleText);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        titleLabel.setForeground(new Color(108, 117, 125));

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        valueLabel.setForeground(new Color(40,167,69));

        summaryCard.add(titleLabel, BorderLayout.NORTH);
        summaryCard.add(valueLabel, BorderLayout.CENTER);
        return summaryCard;
    }
    
    private void exportToCSV(JTable table, String totalModules, String peakIntakeMonth) {
        String home = System.getProperty("user.home");
        String downloadsPath= home + java.io.File.separator + "Downloads";
        String csvFileName = "Module Performance Summary.csv";
        java.io.File fileToSave = new java.io.File(downloadsPath, csvFileName);
        
        try(java.io.FileWriter fw=new java.io.FileWriter(fileToSave)){
            //top summary section
            fw.write("ACADEMIC LEADER DASHBOARD SUMMARY\n");
            fw.write("Total Modules Created," + totalModules + "\n");
            fw.write("Peak Intake Month," + peakIntakeMonth.replace(","," ") + "\n");
            fw.write("\n");
            //write headers
            for (int i=0; i<table.getColumnCount(); i++){
                String header = table.getColumnName(i);
                fw.write(header + (i == table.getColumnCount() - 1 ? "" : ","));
            }
            fw.write("\n");
            //write table data
            for (int row=0; row < table.getRowCount(); row++){
                for(int col=0;col < table.getColumnCount(); col++){
                    Object value = table.getValueAt(row, col);
                    String data = (value !=null)?value.toString().replace(","," "):"";
                    fw.write(data + (col == table.getColumnCount()-1 ? "" : ","));
                }
                fw.write("\n");
            }
            
            JOptionPane.showMessageDialog(this,"Successful download!", "Download Complete", JOptionPane.INFORMATION_MESSAGE);
            
        }catch (java.io.IOException ex){
            JOptionPane.showMessageDialog(this,"Error: Could not save the file.", "Download Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}

///
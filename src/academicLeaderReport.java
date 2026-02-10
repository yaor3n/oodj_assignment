import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import java.util.List;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class academicLeaderReport extends JPanel {
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private int count;
    private String topIntakeMonth;
    private int maxCount;
    private DefaultTableModel moduleTableModel, lecturerTableModel, feedbackTableModel, studentEnrollmentTableModel, resultsTableModel, lecturerRatingTableModel;
    private JLabel totalModuleValue, intakeValue, totalLecturerValue, averageRatingValue, totalFeedbackValue, topModule, overallPassRate, topPerformingModule, topLecturer, systemAverageRating;
    
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
      leftNavigation.add(dropdownGroup("👥 Student Enrollment Summary", new String[]{"Feedback Overview"}));
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
      cardPanel.add(studentEnrollmentReport(),"Enrollment Trends");
      
      cardPanel.add(defaultPage,"EMPTY");
      cardPanel.add(resultRateReport(),"Pass/Fail Rates");
      
      cardPanel.add(defaultPage,"EMPTY");
      cardPanel.add(lecturerReport(),"Lecturer Overview");
      
      cardPanel.add(defaultPage,"EMPTY");
      cardPanel.add(lecturerRatingReport(),"Lecturer Rating");
      
      cardPanel.add(defaultPage,"EMPTY");
      cardPanel.add(studentFeedbackReport(),"Feedback Overview");
      
      this.add(cardPanel, BorderLayout.CENTER);
      cardLayout.show(cardPanel, "EMPTY");
    }
    
    private JLabel leftNavigationTitle(String text){
        JLabel reportTypeTitle=new JLabel(text);
        reportTypeTitle.setOpaque(true);
        reportTypeTitle.setFont(new Font("Segoe UI Emoji",Font.BOLD,14));
        reportTypeTitle.setBackground(new Color(226, 232, 240));
        reportTypeTitle.setForeground(new Color(100,116,139));
        
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
    
    private JPanel totalModuleReport() {
        JPanel totalModule = new JPanel(new BorderLayout(0, 20));
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

        totalModuleValue = new JLabel("0");
        intakeValue = new JLabel("Loading...");

        JPanel cardAligner = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        cardAligner.setOpaque(false);
        cardAligner.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardAligner.add(academicLeaderReportUITools.createSummaryCard("TOTAL MODULES CREATED", totalModuleValue, new Color(40, 167, 69)));
        cardAligner.add(Box.createHorizontalStrut(15));
        cardAligner.add(academicLeaderReportUITools.createSummaryCard("PEAK INTAKE MONTH", intakeValue, new Color(40, 167, 69)));
        topSection.add(cardAligner);
        topSection.add(Box.createVerticalStrut(25));
        
        //filter
        JPanel filterContainer = new JPanel(new BorderLayout());
        filterContainer.setOpaque(false);
        filterContainer.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel filterGroup = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        filterGroup.setOpaque(false);
        
        int currentYear = java.time.LocalDate.now().getYear();
        List<String> yearList = new ArrayList<>();
        yearList.add("All Years");
        
        for (int i = 0; i < 3; i++) {
            yearList.add(String.valueOf(currentYear + i));
        }

        JComboBox<String> monthFilter = new JComboBox<>(new String[]{"All Months", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"});
        JComboBox<String> yearFilter = new JComboBox<>(yearList.toArray(new String[0]));

        JButton clearFilterButton = new JButton("Clear Filters");
        clearFilterButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        filterGroup.add(new JLabel("Intake:")); filterGroup.add(monthFilter);
        filterGroup.add(new JLabel("Year:")); filterGroup.add(yearFilter);
        filterGroup.add(clearFilterButton);
        filterContainer.add(filterGroup, BorderLayout.EAST);
        topSection.add(filterContainer);

        // table
        String[] columns = {"Module Code", "Module Name", "Level", "Lecturer", "Intake Month", "Year"};
        moduleTableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable moduleTable = new JTable(moduleTableModel);
        JScrollPane scrollPane = new JScrollPane(moduleTable);

        academicLeaderReportUITools.reportTable(moduleTable, scrollPane);

        // switch to no result
        CardLayout tableCardLayout = new CardLayout();
        JPanel tableSwitcher = new JPanel(tableCardLayout);
        tableSwitcher.setOpaque(false);
        tableSwitcher.add(scrollPane, "TABLE");

        JPanel emptyView = new JPanel(new BorderLayout());
        emptyView.setBackground(Color.WHITE);
        emptyView.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        emptyView.add(moduleTable.getTableHeader(), BorderLayout.NORTH);
        emptyView.add(academicLeaderReportUITools.noResultPanel(), BorderLayout.CENTER);
        tableSwitcher.add(emptyView, "EMPTY");

        // filter
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(moduleTableModel);
        moduleTable.setRowSorter(sorter);

        java.awt.event.ActionListener filterAction = e -> {
            String selectedYear = (String) yearFilter.getSelectedItem();
            String selectedMonth = (String) monthFilter.getSelectedItem();
            List<RowFilter<Object, Object>> filters = new ArrayList<>();

            if (!selectedYear.equals("All Years")) filters.add(RowFilter.regexFilter(selectedYear, 5));
            if (!selectedMonth.equals("All Months")) filters.add(RowFilter.regexFilter(selectedMonth, 4));

            sorter.setRowFilter(filters.isEmpty() ? null : RowFilter.andFilter(filters));

            if (moduleTable.getRowCount() == 0) tableCardLayout.show(tableSwitcher, "EMPTY");
            else tableCardLayout.show(tableSwitcher, "TABLE");
        };

        yearFilter.addActionListener(filterAction);
        monthFilter.addActionListener(filterAction);
        clearFilterButton.addActionListener(e -> { monthFilter.setSelectedIndex(0); yearFilter.setSelectedIndex(0); });

        // assembly
        JPanel registryPanel = new JPanel(new BorderLayout(0, 10));
        registryPanel.setOpaque(false);
        registryPanel.add(new JLabel("📊 Complete Module Registry") {{ setFont(new Font("Segoe UI Emoji", Font.BOLD, 14)); }}, BorderLayout.NORTH);
        registryPanel.add(tableSwitcher, BorderLayout.CENTER);

        // Sync + Export
        JButton exportButton = academicLeaderReportUITools.createExportButton();
        exportButton.addActionListener(e -> {
            List<String> summary = new ArrayList<>();
            summary.add("Total Modules," + totalModuleValue.getText());
            summary.add("Peak Intake," + intakeValue.getText());
            academicLeaderReportUITools.exportToCSV(moduleTable, "Module_Report.csv", summary);
        });
        registryPanel.add(academicLeaderReportUITools.createActionRow(academicLeaderReportUITools.syncTimeLabel(), exportButton), BorderLayout.SOUTH);

        totalModule.add(topSection, BorderLayout.NORTH);
        totalModule.add(registryPanel, BorderLayout.CENTER);

        refreshData();
        return totalModule;
    }
    
    private JPanel studentEnrollmentReport() {
        JPanel studentEnrollmentPanel = new JPanel(new BorderLayout(0, 20));
        studentEnrollmentPanel.setBackground(Color.WHITE);
        studentEnrollmentPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 30, 30));

        JPanel topSection = new JPanel();
        topSection.setLayout(new BoxLayout(topSection, BoxLayout.Y_AXIS));
        topSection.setOpaque(false);
        topSection.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel("Student Enrollment Trends");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        topSection.add(title);
        topSection.add(Box.createVerticalStrut(15));

        topModule = new JLabel("None");
        JPanel cardAligner = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        cardAligner.setOpaque(false);
        cardAligner.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardAligner.add(academicLeaderReportUITools.createSummaryCard("MOST POPULAR MODULE", topModule, new Color(13, 110, 253))); 
        topSection.add(cardAligner);
        topSection.add(Box.createVerticalStrut(25));

        // search
        JPanel filterContainer = new JPanel(new BorderLayout());
        filterContainer.setOpaque(false);
        filterContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        filterContainer.add(new JLabel("💬 Student Enrollment") {{ setFont(new Font("Segoe UI Emoji", Font.BOLD, 14)); }}, BorderLayout.WEST);

        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(250, 30));
        searchField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(203, 213, 225)), BorderFactory.createEmptyBorder(0, 10, 0, 10)));

        JPanel searchGroup = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        searchGroup.setOpaque(false);
        searchGroup.add(new JLabel("Search Module:"));
        searchGroup.add(searchField);
        filterContainer.add(searchGroup, BorderLayout.EAST);
        topSection.add(filterContainer);

        // table
        String[] columns = {"Module Name", "Total Students Enrolled", "Status"};
        studentEnrollmentTableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable studentEnrollmentTable = new JTable(studentEnrollmentTableModel);
        JScrollPane scrollPane = new JScrollPane(studentEnrollmentTable);
        academicLeaderReportUITools.reportTable(studentEnrollmentTable, scrollPane);

        // switch to no result panel
        CardLayout cardLayout = new CardLayout();
        JPanel switcher = new JPanel(cardLayout);
        switcher.setOpaque(false);
        switcher.add(scrollPane, "TABLE");

        JPanel emptyView = new JPanel(new BorderLayout());
        emptyView.setBackground(Color.WHITE);
        emptyView.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        emptyView.add(studentEnrollmentTable.getTableHeader(), BorderLayout.NORTH);
        emptyView.add(academicLeaderReportUITools.noResultPanel(), BorderLayout.CENTER);
        switcher.add(emptyView, "EMPTY");

        // filter
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(studentEnrollmentTableModel);
        studentEnrollmentTable.setRowSorter(sorter);
        
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void filter() {
                String text = searchField.getText();
                sorter.setRowFilter(text.trim().isEmpty() ? null : RowFilter.regexFilter("(?i)" + text));
                if (studentEnrollmentTable.getRowCount() == 0) cardLayout.show(switcher, "EMPTY");
                else cardLayout.show(switcher, "TABLE");
            }
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filter(); }
        });

        studentEnrollmentPanel.add(topSection, BorderLayout.NORTH);
        studentEnrollmentPanel.add(switcher, BorderLayout.CENTER);
        
        JButton exportBtn = academicLeaderReportUITools.createExportButton();
        exportBtn.addActionListener(e -> {
            List<String> summary = new ArrayList<>();
            summary.add("REPORT: Student Enrollment Trends");
            summary.add("Most Popular Module," + topModule.getText());

            academicLeaderReportUITools.exportToCSV(studentEnrollmentTable, "Enrollment_Trends_Report.csv", summary);
        });
        
        studentEnrollmentPanel.add(academicLeaderReportUITools.createActionRow(academicLeaderReportUITools.syncTimeLabel(), exportBtn), BorderLayout.SOUTH);
        return studentEnrollmentPanel;
    }
    
    private JPanel resultRateReport() {
        JPanel resultRatePanel = new JPanel(new BorderLayout(0, 20));
        resultRatePanel.setBackground(Color.WHITE);
        resultRatePanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 30, 30));

        JPanel topSection = new JPanel();
        topSection.setLayout(new BoxLayout(topSection, BoxLayout.Y_AXIS));
        topSection.setOpaque(false);
        topSection.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel("Module Pass/Fail Analysis");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        topSection.add(title);
        topSection.add(Box.createVerticalStrut(15));

        overallPassRate = new JLabel("0%");
        topPerformingModule = new JLabel("None");
        
        JPanel cardAligner = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        cardAligner.setOpaque(false);
        cardAligner.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardAligner.add(academicLeaderReportUITools.createSummaryCard("OVERALL PASS RATE", overallPassRate , new Color(40, 167, 69))); 
        cardAligner.add(Box.createHorizontalStrut(15));
        cardAligner.add(academicLeaderReportUITools.createSummaryCard("TOP PERFORMING MODULE", topPerformingModule, new Color(102, 16, 242))); 
        topSection.add(cardAligner);
        topSection.add(Box.createVerticalStrut(25));

        // search
        JPanel filterContainer = new JPanel(new BorderLayout());
        filterContainer.setOpaque(false);
        filterContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        filterContainer.add(new JLabel("📊 Module Analysis") {{ setFont(new Font("Segoe UI Emoji", Font.BOLD, 14)); }}, BorderLayout.WEST);

        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(250, 30));
        searchField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(203, 213, 225)), BorderFactory.createEmptyBorder(0, 10, 0, 10)));

        JPanel searchGroup = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        searchGroup.setOpaque(false);
        searchGroup.add(new JLabel("Search Module:"));
        searchGroup.add(searchField);
        filterContainer.add(searchGroup, BorderLayout.EAST);
        topSection.add(filterContainer);

        // table
        String[] columns = {"Module Name", "Total Students", "Passed", "Failed", "Pass Rate (%)"};
        resultsTableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable resultsTable = new JTable(resultsTableModel);
        JScrollPane scrollPane = new JScrollPane(resultsTable);
        academicLeaderReportUITools.reportTable(resultsTable, scrollPane);
        
        resultsTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                try {
                    int passed = Integer.parseInt(table.getValueAt(row, 2).toString());
                    int failed = Integer.parseInt(table.getValueAt(row, 3).toString());

                    if (!isSelected) {
                        if (failed > passed) {
                            component.setBackground(new Color(254, 226, 226)); 
                            component.setForeground(new Color(153, 27, 27)); 
                        } else {
                            component.setBackground(row % 2 == 0 ? Color.WHITE : new Color(242, 242, 242));
                            component.setForeground(Color.BLACK);
                        }
                    }
                } catch (Exception e) {
                    if (!isSelected) component.setBackground(row % 2 == 0 ? Color.WHITE : new Color(242, 242, 242));
                }

                return component;
            }
        });

        // switch to no result panel
        CardLayout cardLayout = new CardLayout();
        JPanel switcher = new JPanel(cardLayout);
        switcher.setOpaque(false);
        switcher.add(scrollPane, "TABLE");

        JPanel emptyView = new JPanel(new BorderLayout());
        emptyView.setBackground(Color.WHITE);
        emptyView.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        emptyView.add(resultsTable.getTableHeader(), BorderLayout.NORTH);
        emptyView.add(academicLeaderReportUITools.noResultPanel(), BorderLayout.CENTER);
        switcher.add(emptyView, "EMPTY");

        // filter
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(resultsTableModel);
        resultsTable.setRowSorter(sorter);
        
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void filter() {
                String text = searchField.getText();
                sorter.setRowFilter(text.trim().isEmpty() ? null : RowFilter.regexFilter("(?i)" + text));
                if (resultsTable.getRowCount() == 0) cardLayout.show(switcher, "EMPTY");
                else cardLayout.show(switcher, "TABLE");
            }
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filter(); }
        });

        resultRatePanel.add(topSection, BorderLayout.NORTH);
        resultRatePanel.add(switcher, BorderLayout.CENTER);
        
        JButton exportButton = academicLeaderReportUITools.createExportButton();
        exportButton.addActionListener(e -> {
            List<String> summary = new ArrayList<>();
            summary.add("REPORT: Module Pass/Fail Analysis");
            summary.add("Overall Pass Rate," + overallPassRate.getText());
            summary.add("Top Performing Module," + topPerformingModule.getText());

            academicLeaderReportUITools.exportToCSV(resultsTable, "Student_Pass_Module_Rates_Report.csv", summary);
        });
        
        resultRatePanel.add(academicLeaderReportUITools.createActionRow(academicLeaderReportUITools.syncTimeLabel(), exportButton), BorderLayout.SOUTH);

        return resultRatePanel;
    }
    
    private JPanel lecturerReport() {
        JPanel lecturerOverview = new JPanel(new BorderLayout(0, 20));
        lecturerOverview.setBackground(Color.WHITE);
        lecturerOverview.setBorder(BorderFactory.createEmptyBorder(10, 30, 30, 30));

        JPanel topSection = new JPanel();
        topSection.setLayout(new BoxLayout(topSection, BoxLayout.Y_AXIS));
        topSection.setOpaque(false);
        topSection.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel("Lecturer Performance Overview");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        topSection.add(title);
        topSection.add(Box.createVerticalStrut(15));

        totalLecturerValue = new JLabel("0");
        JPanel cardAligner = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        cardAligner.setOpaque(false);
        cardAligner.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardAligner.add(academicLeaderReportUITools.createSummaryCard("TOTAL LECTURERS", totalLecturerValue, new Color(0, 123, 255)));
        topSection.add(cardAligner);
        topSection.add(Box.createVerticalStrut(25));

        // search bar
        JPanel filterContainer = new JPanel(new BorderLayout());
        filterContainer.setOpaque(false);
        filterContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
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

        // table
        String[] columns = {"Staff ID", "Full Name", "Email Address", "Gender", "Date of Birth", "Status"};
        lecturerTableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable lecturerTable = new JTable(lecturerTableModel);
        JScrollPane scrollPane = new JScrollPane(lecturerTable);
        academicLeaderReportUITools.reportTable(lecturerTable, scrollPane);

        CardLayout lectCardLayout = new CardLayout();
        JPanel lectSwitcher = new JPanel(lectCardLayout);
        lectSwitcher.setOpaque(false);
        lectSwitcher.add(scrollPane, "TABLE");

        //no result found
        JPanel emptyViewWithHeader = new JPanel(new BorderLayout());
        emptyViewWithHeader.setOpaque(false);
        emptyViewWithHeader.setBackground(Color.WHITE);
        emptyViewWithHeader.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        emptyViewWithHeader.add(lecturerTable.getTableHeader(), BorderLayout.NORTH); // Header fixed at top
        emptyViewWithHeader.add(academicLeaderReportUITools.noResultPanel(), BorderLayout.CENTER);
        lectSwitcher.add(emptyViewWithHeader, "EMPTY");

        // search listener
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

        // actionrow
        JButton exportButton = academicLeaderReportUITools.createExportButton();
        exportButton.addActionListener(e -> {
            List<String> summary = new ArrayList<>();
            summary.add("Total Lecturers Registered," + totalLecturerValue.getText());
            academicLeaderReportUITools.exportToCSV(lecturerTable, "Lecturer_Overview.csv", summary);
        });

        JPanel actionRow = academicLeaderReportUITools.createActionRow(academicLeaderReportUITools.syncTimeLabel(), exportButton);

        // assembly
        JPanel registryPanel = new JPanel(new BorderLayout(0, 10));
        registryPanel.setOpaque(false);
        registryPanel.add(lectSwitcher, BorderLayout.CENTER); 
        registryPanel.add(actionRow, BorderLayout.SOUTH);     

        lecturerOverview.add(topSection, BorderLayout.NORTH);
        lecturerOverview.add(registryPanel, BorderLayout.CENTER);
        
        refreshData();
        return lecturerOverview;
    }
    
    private JPanel lecturerRatingReport() {
        JPanel lecturerRatingPanel = new JPanel(new BorderLayout(0, 20));
        lecturerRatingPanel.setBackground(Color.WHITE);
        lecturerRatingPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 30, 30));

        JPanel topSection = new JPanel();
        topSection.setLayout(new BoxLayout(topSection, BoxLayout.Y_AXIS));
        topSection.setOpaque(false);
        topSection.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel("Lecturer Rating Performance");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        topSection.add(title);
        topSection.add(Box.createVerticalStrut(15));

        topLecturer = new JLabel("N/A");

        JPanel cardAligner = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        cardAligner.setOpaque(false);
        cardAligner.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardAligner.add(academicLeaderReportUITools.createSummaryCard("TOP RATED LECTURER", topLecturer, new Color(255, 193, 7))); 
        topSection.add(cardAligner);
        topSection.add(Box.createVerticalStrut(25));

        JPanel filterContainer = new JPanel(new BorderLayout());
        filterContainer.setOpaque(false);
        filterContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        filterContainer.add(new JLabel("📊 Lecturer Performance Registry") {{ setFont(new Font("Segoe UI Emoji", Font.BOLD, 14)); }}, BorderLayout.WEST);

        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(250, 30));
        searchField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(203, 213, 225)), BorderFactory.createEmptyBorder(0, 10, 0, 10)));

        JPanel searchGroup = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        searchGroup.setOpaque(false);
        searchGroup.add(new JLabel("Search Lecturer:"));
        searchGroup.add(searchField);
        filterContainer.add(searchGroup, BorderLayout.EAST);
        topSection.add(filterContainer);

        String[] columns = {"Lecturer Name", "Number of Feedbacks", "Average Rating (⭐)", "Status"};
        lecturerRatingTableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable ratingTable = new JTable(lecturerRatingTableModel);
        JScrollPane scrollPane = new JScrollPane(ratingTable);
        academicLeaderReportUITools.reportTable(ratingTable, scrollPane);
        
        ratingTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                String ratingStr = table.getValueAt(row, 2).toString().split(" ")[0];
                double rating = Double.parseDouble(ratingStr);

                if (!isSelected) {
                    if (rating < 3.0) {
                        component.setBackground(new Color(254, 226, 226)); 
                        component.setForeground(new Color(153, 27, 27));  
                    } else {
                        component.setBackground(row % 2 == 0 ? Color.WHITE : new Color(242, 242, 242));
                        component.setForeground(Color.BLACK);
                    }
                }
                if (component instanceof JComponent) {
                    ((JComponent) component).setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                }
                return component;
            }
        });

        // Switcher Logic
        CardLayout cardLayout = new CardLayout();
        JPanel switcher = new JPanel(cardLayout);
        switcher.setOpaque(false);
        switcher.add(scrollPane, "TABLE");

        JPanel emptyView = new JPanel(new BorderLayout());
        emptyView.setBackground(Color.WHITE);
        emptyView.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        emptyView.add(ratingTable.getTableHeader(), BorderLayout.NORTH);
        emptyView.add(academicLeaderReportUITools.noResultPanel(), BorderLayout.CENTER);
        switcher.add(emptyView, "EMPTY");

        // Filter Logic
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(lecturerRatingTableModel);
        ratingTable.setRowSorter(sorter);
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void filter() {
                String text = searchField.getText();
                sorter.setRowFilter(text.trim().isEmpty() ? null : RowFilter.regexFilter("(?i)" + text));
                if (ratingTable.getRowCount() == 0) cardLayout.show(switcher, "EMPTY");
                else cardLayout.show(switcher, "TABLE");
            }
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filter(); }
        });

        // --- 4. ASSEMBLY ---
        lecturerRatingPanel.add(topSection, BorderLayout.NORTH);
        lecturerRatingPanel.add(switcher, BorderLayout.CENTER);

        JButton exportBtn = academicLeaderReportUITools.createExportButton();
        exportBtn.addActionListener(e -> {
            List<String> summary = new ArrayList<>();
            summary.add("Top Lecturer: " + topLecturer.getText());
            academicLeaderReportUITools.exportToCSV(ratingTable, "Lecturer_Rating_Report.csv", summary);
        });
        lecturerRatingPanel.add(academicLeaderReportUITools.createActionRow(academicLeaderReportUITools.syncTimeLabel(), exportBtn), BorderLayout.SOUTH);

        return lecturerRatingPanel;
    }
    
    private JPanel studentFeedbackReport() {
        JPanel feedbackPanel = new JPanel(new BorderLayout(0, 20));
        feedbackPanel.setBackground(Color.WHITE);
        feedbackPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 30, 30));

        JPanel topSection = new JPanel();
        topSection.setLayout(new BoxLayout(topSection, BoxLayout.Y_AXIS));
        topSection.setOpaque(false);
        topSection.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel("Student Feedback Analysis");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        topSection.add(title);
        topSection.add(Box.createVerticalStrut(15));

        averageRatingValue = new JLabel("0.0");
        totalFeedbackValue = new JLabel("0");

        JPanel cardAligner = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        cardAligner.setOpaque(false);
        cardAligner.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardAligner.add(academicLeaderReportUITools.createSummaryCard("AVERAGE RATING", averageRatingValue, new Color(255, 193, 7))); 
        cardAligner.add(Box.createHorizontalStrut(15));
        cardAligner.add(academicLeaderReportUITools.createSummaryCard("TOTAL FEEDBACKS", totalFeedbackValue, new Color(23, 162, 184))); 
        topSection.add(cardAligner);
        topSection.add(Box.createVerticalStrut(25));

        // search
        JPanel filterContainer = new JPanel(new BorderLayout());
        filterContainer.setOpaque(false);
        filterContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        filterContainer.add(new JLabel("💬 Feedback Registry") {{ setFont(new Font("Segoe UI Emoji", Font.BOLD, 14)); }}, BorderLayout.WEST);

        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(250, 30));
        searchField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(203, 213, 225)), BorderFactory.createEmptyBorder(0, 10, 0, 10)));

        JPanel searchGroup = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        searchGroup.setOpaque(false);
        searchGroup.add(new JLabel("Search Module/Lecturer:"));
        searchGroup.add(searchField);
        filterContainer.add(searchGroup, BorderLayout.EAST);
        topSection.add(filterContainer);

        // table
        String[] columns = {"Student", "Module", "Lecturer", "Rating (⭐)", "Comments"};
        feedbackTableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable feedbackTable = new JTable(feedbackTableModel);
        JScrollPane scrollPane = new JScrollPane(feedbackTable);
        academicLeaderReportUITools.reportTable(feedbackTable, scrollPane);

        // switch to no result panel
        CardLayout cardLayout = new CardLayout();
        JPanel switcher = new JPanel(cardLayout);
        switcher.setOpaque(false);
        switcher.add(scrollPane, "TABLE");

        JPanel emptyView = new JPanel(new BorderLayout());
        emptyView.setBackground(Color.WHITE);
        emptyView.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        emptyView.add(feedbackTable.getTableHeader(), BorderLayout.NORTH);
        emptyView.add(academicLeaderReportUITools.noResultPanel(), BorderLayout.CENTER);
        switcher.add(emptyView, "EMPTY");

        // filter
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(feedbackTableModel);
        feedbackTable.setRowSorter(sorter);
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void filter() {
                String text = searchField.getText();
                sorter.setRowFilter(text.trim().isEmpty() ? null : RowFilter.regexFilter("(?i)" + text));
                if (feedbackTable.getRowCount() == 0) cardLayout.show(switcher, "EMPTY");
                else cardLayout.show(switcher, "TABLE");
            }
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filter(); }
        });
        
        JButton exportButton = academicLeaderReportUITools.createExportButton();
        exportButton.addActionListener(e -> {
            List<String> summary = new ArrayList<>();
            summary.add("REPORT: Student Feedback Overview");
            summary.add("Average Rating," + averageRatingValue.getText());
            summary.add("Total Feedbacks Received," + totalFeedbackValue.getText());

            academicLeaderReportUITools.exportToCSV(feedbackTable, "Student_Feedback_Report.csv", summary);
        });

        feedbackPanel.add(topSection, BorderLayout.NORTH);
        feedbackPanel.add(switcher, BorderLayout.CENTER);
        feedbackPanel.add(academicLeaderReportUITools.createActionRow(academicLeaderReportUITools.syncTimeLabel(), exportButton), BorderLayout.SOUTH);

        return feedbackPanel;
    }
    
    public void refreshData() {
        List<academicLeaderModule> modules = academicLeaderFileManager.loadModules();
        
        //total module
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
        
        //student enrollment
        if (studentEnrollmentTableModel != null) {
            studentEnrollmentTableModel.setRowCount(0);
            java.util.Map<String, Integer> enrollmentMap = new java.util.HashMap<>();

            try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader("accounts.txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 11 && parts[9].trim().equalsIgnoreCase("Student")) {
                        String moduleName = parts[10].trim();
                        enrollmentMap.put(moduleName, enrollmentMap.getOrDefault(moduleName, 0) + 1);
                    }
                }

                String mostPopular = "N/A";
                int maxEnrollment = 0;

                for (java.util.Map.Entry<String, Integer> entry : enrollmentMap.entrySet()) {
                    String status = entry.getValue() > 3 ? "🔥 High Demand" : "Stable";
                    studentEnrollmentTableModel.addRow(new Object[]{entry.getKey(), entry.getValue() + " Students", status});

                    if (entry.getValue() > maxEnrollment) {
                        maxEnrollment = entry.getValue();
                        mostPopular = entry.getKey();
                    }
                }

                if (topModule != null) topModule.setText(mostPopular);

            } catch (java.io.IOException e) {
                System.err.println("Error reading enrollment data: " + e.getMessage());
            }
        }
        
        //student pass/fail 
        if (resultsTableModel != null) {
            resultsTableModel.setRowCount(0);
            java.util.Map<String, int[]> statsMap = new java.util.HashMap<>();

            try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader("Results.txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String[] parts = line.split(",");

                    if (parts.length >= 6) {
                        String moduleName = parts[4].trim();
                        int marks = Integer.parseInt(parts[6].trim()); 

                        statsMap.putIfAbsent(moduleName, new int[3]);
                        int[] stats = statsMap.get(moduleName);

                        stats[0]++; 
                        if (marks >= 50) stats[1]++; 
                        else stats[2]++;
                    }
                }

                double totalGlobalPassed = 0;
                double totalGlobalStudents = 0;
                String topModule = "N/A";
                double highestRate = -1;

                for (java.util.Map.Entry<String, int[]> entry : statsMap.entrySet()) {
                    String name = entry.getKey();
                    int[] s = entry.getValue();
                    double rate = (s[0] > 0) ? ((double) s[1] / s[0]) * 100 : 0;

                    resultsTableModel.addRow(new Object[]{
                        name, s[0], s[1], s[2], String.format("%.2f%%", rate)
                    });

                    totalGlobalPassed += s[1];
                    totalGlobalStudents += s[0];

                    if (rate > highestRate) {
                        highestRate = rate;
                        topModule = name;
                    }
                }

                if (overallPassRate != null && totalGlobalStudents > 0) {
                    overallPassRate.setText(String.format("%.1f%%", (totalGlobalPassed / totalGlobalStudents) * 100));
                }
                if (topPerformingModule != null) {
                    topPerformingModule.setText(topModule);
                }

            } catch (Exception e) {
                System.err.println("Error reading results: " + e.getMessage());
            }
        }
        
        //lecturer overview
        if (lecturerTableModel != null) {
            lecturerTableModel.setRowCount(0);
            int totalLecturer = 0;

            try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader("accounts.txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;

                    String[] parts = line.split(","); 

                    if (parts.length >= 10 && parts[9].trim().equalsIgnoreCase("Lecturer")) {
                        String id = parts[0].trim();
                        String name = parts[1].trim();
                        String email = parts[2].trim();
                        String gender = parts[3].trim();
                        String dob = parts[4].trim();

                        boolean isActive = modules.stream().anyMatch(m -> m.getLecturerName().equalsIgnoreCase(name));
                        lecturerTableModel.addRow(new Object[]{
                            id, name, email, gender, dob, (isActive ? "Active" : "Inactive")
                        });
                        totalLecturer++;
                    }
                }
                if (totalLecturerValue != null) totalLecturerValue.setText(String.valueOf(totalLecturer));
            } catch (java.io.IOException e) {
                System.err.println("Error reading accounts.txt: " + e.getMessage());
            }
        }
        
        // lecturer rating
        if (lecturerRatingTableModel != null) {
            lecturerRatingTableModel.setRowCount(0);
            java.util.Map<String, double[]> ratingMap = new java.util.HashMap<>();

            try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader("feedback.txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;

                    try {
                        String[] parts = line.split("\\|");
                        if (parts.length >= 4) {
                            String lecName = parts[2].trim();
                            double score = Double.parseDouble(parts[3].trim());

                            ratingMap.putIfAbsent(lecName, new double[2]);
                            double[] stats = ratingMap.get(lecName);
                            stats[0] += score; 
                            stats[1] += 1;     
                        }
                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {
                        System.err.println("Skipping malformed line: " + line);
                    }
                }

                String bestLecturer = "N/A"; 
                double highestAverage = -1;

                for (var entry : ratingMap.entrySet()) {
                    double[] stats = entry.getValue();
                    double average = stats[0] / stats[1];
                    String status = average >= 4.5 ? "Excellence" : (average >= 3.0 ? "Satisfactory" : "Needs Review!");

                    lecturerRatingTableModel.addRow(new Object[]{
                        entry.getKey(), (int)stats[1], String.format("%.1f / 5.0", average), status
                    });
                    
                    if (average > highestAverage) {
                        highestAverage = average;
                        bestLecturer = entry.getKey();
                    }
                }

                if (topLecturer != null) topLecturer.setText(bestLecturer);
                
            } catch (java.io.IOException e) {
                System.err.println("File Error: " + e.getMessage());
            }
        }
        
        //student feedback
        if (feedbackTableModel != null) {
            feedbackTableModel.setRowCount(0);
            double totalStars = 0;
            int feedbackCount = 0;

            try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader("feedback.txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    
                    String[] parts = line.split("\\|"); 

                    if (parts.length >= 5) {
                        String student = parts[0].trim();
                        String module = parts[1].trim();
                        String lecturer = parts[2].trim();
                        String rating = parts[3].trim();
                        String comment = parts[4].trim();

                        feedbackTableModel.addRow(new Object[]{student, module, lecturer, rating , comment});

                        try {
                            totalStars += Double.parseDouble(rating);
                            feedbackCount++;
                        } catch (NumberFormatException e) {}
                    }
                }

                if (totalFeedbackValue != null) totalFeedbackValue.setText(String.valueOf(feedbackCount));
                if (averageRatingValue != null && feedbackCount > 0) {
                    double average = totalStars / feedbackCount;
                    averageRatingValue.setText(String.format("%.1f / 5.0", average)); 
                }

            } catch (java.io.IOException e) {
                System.err.println("Error reading feedback.txt: " + e.getMessage());
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


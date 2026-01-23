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
    
    public academicLeaderReport() {
      this.setLayout(new BorderLayout());
      
      //left navigation panel
      JPanel leftNavigation=new JPanel();
      leftNavigation.setLayout(new BoxLayout(leftNavigation,BoxLayout.Y_AXIS));
      leftNavigation.setPreferredSize(new Dimension(280,0));
      leftNavigation.setBackground(new Color(248,250,252));
      leftNavigation.setBorder(BorderFactory.createMatteBorder(0,0,0,1,new Color(226,232,240)));
      
      leftNavigation.add(Box.createVerticalStrut(10));
      leftNavigation.add(createSectionTitle("🗂️ MODULE ANALYTICS"));
      
      //dropdown
      leftNavigation.add(dropdownGroup("📈 Module Performance Summary", new String[]{"Total Modules Created"}));
      leftNavigation.add(dropdownGroup("👥 Student Enrollment Summary", new String[]{"Enrollment Trends"}));
      leftNavigation.add(dropdownGroup("📝 Assessment Performance", new String[]{"Pass/Fail Rates"}));
      leftNavigation.add(dropdownGroup("👨‍🏫 Lecturer Performance", new String[]{"Workload Overview"}));
      this.add(new JScrollPane(leftNavigation),BorderLayout.WEST);
      
      leftNavigation.add(Box.createVerticalStrut(20)); 
      leftNavigation.add(createSectionTitle("💬 STUDENT FEEDBACK"));
      
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
      
      this.add(cardPanel, BorderLayout.CENTER);
      cardLayout.show(cardPanel, "EMPTY");
    }
    
    private JLabel createSectionTitle(String text){
        JLabel reportTypeTitle=new JLabel(text);
        reportTypeTitle.setFont(new Font("Segoe UI Emoji",Font.BOLD,11));
        reportTypeTitle.setForeground(new Color(100,116,139));
        reportTypeTitle.setBorder(BorderFactory.createEmptyBorder(20,24,8,10));
        return reportTypeTitle;
    }
    
    private JPanel dropdownGroup(String title,String[]items){
        JPanel dropdown=new JPanel();
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
            JButton subBtn = new JButton("      " + item);
            styleNavigationButton(subBtn, false);
            subBtn.addActionListener(e -> cardLayout.show(cardPanel, item));
            itemPanel.add(subBtn);
            itemPanel.add(Box.createVerticalStrut(2));
        }
        
        mainReportType.addActionListener(e -> {
            boolean isVisible = itemPanel.isVisible();
            itemPanel.setVisible(!isVisible);

            if (!isVisible) {
                // OPEN STATE: Modern Highlight
                mainReportType.setBackground(new Color(241, 245, 249)); // Light blue tint
                mainReportType.setOpaque(true);
            } else {
                // CLOSED STATE: Neutral
                mainReportType.setOpaque(false);
            }                 
        });
        
        dropdown.add(mainReportType);
        dropdown.add(itemPanel);
        addHoverEffect(mainReportType);
        //addHoverEffect(subBtn);
        return dropdown;
    }
    
    private void styleNavigationButton(JButton btn, boolean isParent) {
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(260, 40));
        btn.setFont(new Font("Segoe UI Emoji", isParent ? Font.BOLD : Font.PLAIN, 13));
        btn.setForeground(new Color(51, 65, 85));
        btn.setContentAreaFilled(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 24, 8, 16));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
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
        totalModule.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JPanel topSection = new JPanel(new BorderLayout(0, 15));
        topSection.setOpaque(false);

        JLabel title = new JLabel("Total Modules Created Details");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        totalModule.add(title, BorderLayout.NORTH);

        List<academicLeaderModule> modules = academicLeaderModuleFileManager.loadModules();
        count = modules.size();
        
        JPanel totalModuleCount=new JPanel(new BorderLayout());
        totalModuleCount.setPreferredSize(new Dimension(200,80));
        totalModuleCount.setBackground(new Color(248,249,250));
        totalModuleCount.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0,1,3,3, new Color(220,220,220)),
                BorderFactory.createEmptyBorder(15,20,15,20)
        ));
        
        //total module count card
        JLabel totalModuleLabel=new JLabel("TOTAL MODULES CREATED");
        totalModuleLabel.setFont(new Font("Segoe UI", Font.BOLD,12));
        totalModuleLabel.setForeground(new Color(108,117,125));
        
        JLabel totalModuleValue=new JLabel(String.valueOf(count));
        totalModuleValue.setFont(new Font("Segoe UI", Font.BOLD,22));
        totalModuleValue.setForeground(new Color(40,167,69));
        
        totalModuleCount.add(totalModuleLabel, BorderLayout.NORTH);
        totalModuleCount.add(totalModuleValue, BorderLayout.CENTER);
        
        //top intake
        java.util.Map<String, Integer> intakeCounts = new java.util.HashMap<>();
        for (academicLeaderModule m : modules) {
            String month = m.getIntakeMonth() + " " + m.getYear();
            intakeCounts.put(month, intakeCounts.getOrDefault(month, 0) + 1);
        }

        topIntakeMonth = "N/A";
        maxCount = 0;
        for (java.util.Map.Entry<String, Integer> entry : intakeCounts.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                topIntakeMonth = entry.getKey();
            }
        }
        
        JPanel peakIntakeMonth = new JPanel(new BorderLayout());
        peakIntakeMonth.setPreferredSize(new Dimension(200, 80));
        peakIntakeMonth.setBackground(new Color(248, 249, 250));
        peakIntakeMonth.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0,1,3,3, new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        JLabel intakeLabel = new JLabel("PEAK INTAKE MONTH");
        intakeLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        intakeLabel.setForeground(new Color(108, 117, 125));

        JLabel intakeValue = new JLabel(topIntakeMonth + " (" + maxCount + ")");
        intakeValue.setFont(new Font("Segoe UI", Font.BOLD, 20)); // Fits better
        intakeValue.setForeground(new Color(40, 167, 69)); // Success Green

        peakIntakeMonth.add(intakeLabel, BorderLayout.NORTH);
        peakIntakeMonth.add(intakeValue, BorderLayout.CENTER);
        
        
        JPanel cardAligner = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cardAligner.setOpaque(false);
        cardAligner.add(totalModuleCount);
        cardAligner.add(Box.createHorizontalStrut(10));
        cardAligner.add(peakIntakeMonth);
        
        topSection.add(cardAligner, BorderLayout.CENTER);
        //totalModule.add(topSection, BorderLayout.NORTH);
        
        //filter
        JPanel filterContainer=new JPanel(new BorderLayout());
        filterContainer.setOpaque(false);
        filterContainer.setBorder(BorderFactory.createEmptyBorder(30,0,0,0));
              
        JPanel filter=new JPanel(new FlowLayout(FlowLayout.RIGHT,0,0));
        filter.setOpaque(false);
        
        JLabel filterLabel=new JLabel("Filter by:");
        filterLabel.setFont(new Font("Segoe UI", Font.BOLD,12));
        filterLabel.setForeground(new Color(100, 100, 100));
        
        //year filter
        int currentYear = java.time.LocalDate.now().getYear();
        String[] years = {"All Years", String.valueOf(currentYear), String.valueOf(currentYear + 1)};
        JComboBox<String> yearFilter = new JComboBox<>(years);
        
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
        topSection.add(filterContainer, BorderLayout.SOUTH);
        totalModule.add(topSection, BorderLayout.NORTH);
        
        
        
        //no result
        JPanel noResult = new JPanel(new GridBagLayout());
        noResult.setBackground(Color.WHITE);
        noResult.setOpaque(true);
        noResult.setVisible(false);
        noResult.setBorder(BorderFactory.createEmptyBorder(25,0,0,0));
        
        
        JLabel emptyText = new JLabel("NO RESULT FOUND");
        emptyText.setFont(new Font("Segoe UI", Font.BOLD, 14));
        emptyText.setForeground(new Color(148,163,184));
        noResult.add(emptyText);
        
        // Inside totalModuleReport() (table)
        String[] columns = {"Module Code", "Module Name", "Level", "Lecturer","Intake Month","Year"};
        DefaultTableModel model = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int r,int c){return false;}
        };

        // Populate with data from your file manager
        for (academicLeaderModule m : modules) {
            model.addRow(new Object[]{m.getCode(), m.getName(), m.getQualification(), m.getLecturerName(), m.getIntakeMonth(), m.getYear()});
        }
        
        //table
        JTable moduleTable = new JTable(model) {
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
        
        moduleTable.setRowHeight(30); 
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
        //scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setColumnHeaderView(moduleTable.getTableHeader());
        
        JPanel headerCorner = new JPanel();
        headerCorner.setBackground(new Color(226, 232, 240));
        headerCorner.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(203, 213, 225)));
        scrollPane.setCorner(JScrollPane.UPPER_RIGHT_CORNER, headerCorner);
        
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
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
    
    private void addHoverEffect(JButton btn) {
        Color hoverBg = new Color(241, 245, 249);

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setOpaque(true);
                btn.setBackground(hoverBg);
                btn.repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setOpaque(false);
                btn.repaint();
            }
        });
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
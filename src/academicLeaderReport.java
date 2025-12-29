//master-detail
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class academicLeaderReport extends JPanel {
    private JPanel cardPanel;
    private CardLayout cardLayout;
    
    public academicLeaderReport() {
      this.setLayout(new BorderLayout());
      
      //left navigation panel
      JPanel leftNavigation=new JPanel();
      leftNavigation.setLayout(new BoxLayout(leftNavigation,BoxLayout.Y_AXIS));
      leftNavigation.setPreferredSize(new Dimension(280,0));
      leftNavigation.setBackground(new Color(240,244,248));
      leftNavigation.setBorder(BorderFactory.createMatteBorder(0,0,0,1,new Color(220,220,220)));
      
      leftNavigation.add(createSectionTitle("MODULE ANALYTICS"));
      
      //dropdown
      leftNavigation.add(dropdownGroup("Module Performance Summary", new String[]{"Total Modules Created"}));
      leftNavigation.add(dropdownGroup("Student Enrollment Summary", new String[]{"Enrollment Trends"}));
      leftNavigation.add(dropdownGroup("Assessment Performance", new String[]{"Pass/Fail Rates"}));
      leftNavigation.add(dropdownGroup("Lecturer Performance", new String[]{"Workload Overview"}));
      this.add(new JScrollPane(leftNavigation),BorderLayout.WEST);
      
      leftNavigation.add(createSectionTitle("STUDENT FEEDBACK"));
      
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
        reportTypeTitle.setFont(new Font("Segoe UI",Font.BOLD,11));
        reportTypeTitle.setForeground(new Color(120,130,140));
        reportTypeTitle.setBorder(BorderFactory.createEmptyBorder(15,20,5,10));
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
        }
        
        mainReportType.addActionListener(e -> {
            boolean isVisible = itemPanel.isVisible();
            itemPanel.setVisible(!isVisible);

            if (!isVisible) {
                // OPEN STATE: Modern Highlight
                mainReportType.setBackground(new Color(230, 240, 255)); // Light blue tint
                mainReportType.setOpaque(true);
            } else {
                // CLOSED STATE: Neutral
                mainReportType.setOpaque(false);
            }
        });
        
        dropdown.add(mainReportType);
        dropdown.add(itemPanel);
        return dropdown;
    }
    
    private void styleNavigationButton(JButton btn, boolean isParent) {
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(280, 40));
        btn.setFont(new Font("Segoe UI", isParent ? Font.BOLD : Font.PLAIN, 13));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        //btn.setForeground(isParent ? NAVY : new Color(80, 90, 100));
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

        // Placeholder for the JTable you will add later
//        JPanel tablePlaceholder = new JPanel();
//        tablePlaceholder.setBackground(new Color(250, 250, 250));
//        tablePlaceholder.setBorder(BorderFactory.createDashedBorder(Color.LIGHT_GRAY, 2, 2));
//        totalModule.add(tablePlaceholder, BorderLayout.CENTER);
        
        List<academicLeaderModule> modules = academicLeaderModuleFileManager.loadModules();
        int count = modules.size();
        
        JPanel totalModuleCount=new JPanel(new BorderLayout());
        totalModuleCount.setPreferredSize(new Dimension(240,100));
        totalModuleCount.setBackground(new Color(248,249,250));
        totalModuleCount.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230,230,230),1,true),
                BorderFactory.createEmptyBorder(15,20,15,20)
        ));
        
        //right side title
        JLabel totalModuleLabel=new JLabel("TOTAL MODULES CREATED");
        totalModuleLabel.setFont(new Font("Segoe UI", Font.BOLD,12));
        totalModuleLabel.setForeground(new Color(108,117,125));
        
        JLabel totalModuleValue=new JLabel(String.valueOf(count));
        totalModuleValue.setFont(new Font("Segoe UI", Font.BOLD,30));
        totalModuleLabel.setForeground(new Color(30,45,65));
        
        totalModuleCount.add(totalModuleLabel, BorderLayout.NORTH);
        totalModuleCount.add(totalModuleValue, BorderLayout.CENTER);
        
        JPanel cardAligner = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        cardAligner.setOpaque(false);
        cardAligner.add(totalModuleCount);
        
        topSection.add(cardAligner, BorderLayout.CENTER);
        totalModule.add(topSection, BorderLayout.NORTH);
        
        //filter
        JPanel filter=new JPanel(new FlowLayout(FlowLayout.RIGHT,15,10));
        filter.setOpaque(false);
        JLabel filterLabel=new JLabel("Filter by:");
        filterLabel.setFont(new Font("Segoe UI", Font.BOLD,12));
        
        //year filter
        int currentYear = java.time.LocalDate.now().getYear();
        String[] years = {"All Years", String.valueOf(currentYear), String.valueOf(currentYear + 1)};
        JComboBox<String> yearFilter = new JComboBox<>(years);
        
        //month filter
        String[] months = {"All Months", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        JComboBox<String> monthFilter = new JComboBox<>(months);

        filter.add(filterLabel);
        JLabel filterByIntake=new JLabel("Intake:");
        filterByIntake.setFont(new Font("Segoe UI", Font.BOLD,12));
        JLabel filterByYear=new JLabel("Year:");
        filterByYear.setFont(new Font("Segoe UI", Font.BOLD,12));
        
        filter.add(filterByIntake); filter.add(monthFilter);
        filter.add(filterByYear); filter.add(yearFilter);
        
        topSection.add(filter, BorderLayout.SOUTH);
        totalModule.add(topSection, BorderLayout.NORTH);
        
        // Inside totalModuleReport() (table)
        String[] columns = {"Code", "Module Name", "Level", "Lecturer","Intake Month","Year"};
        DefaultTableModel model = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int r,int c){return false;}
        };

        // Populate with data from your file manager
        for (academicLeaderModule m : modules) {
            model.addRow(new Object[]{m.getCode(), m.getName(), m.getQualification(), m.getLecturerName(), m.getIntakeMonth(), m.getYear()});
        }

        JTable moduleTable = new JTable(model);
        moduleTable.setRowHeight(30); 
        moduleTable.setShowGrid(false);
        moduleTable.setIntercellSpacing(new Dimension(0, 0));
        moduleTable.getTableHeader().setBackground(new Color(245, 247, 250));
        moduleTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        moduleTable.getTableHeader().setPreferredSize(new Dimension(0, 40));
        
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        moduleTable.setRowSorter(sorter);
        
        java.awt.event.ActionListener filterAction = e -> {
            String selectedYear = (String) yearFilter.getSelectedItem();
            String selectedMonth = (String) monthFilter.getSelectedItem();
            
            List<RowFilter<Object, Object>> filters = new ArrayList<>();
            
            if (!selectedYear.equals("All Years")) {
                filters.add(RowFilter.regexFilter(selectedYear, 5)); // Column index 5 is Year
            }
            if (!selectedMonth.equals("All Months")) {
                filters.add(RowFilter.regexFilter(selectedMonth, 4)); // Column index 4 is Intake
            }
            
            sorter.setRowFilter(RowFilter.andFilter(filters));
        };

        yearFilter.addActionListener(filterAction);
        monthFilter.addActionListener(filterAction);
        
        JScrollPane scrollPane = new JScrollPane(moduleTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        scrollPane.getViewport().setBackground(Color.WHITE);

        JPanel registryPanel = new JPanel(new BorderLayout(0,10));
        registryPanel.setOpaque(false);
        
        JLabel registryTitle = new JLabel("Complete Module Registry");
        registryTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        //registryTitle.setBorder(BorderFactory.createEmptyBorder(2,5,30,0)); // Spacing between filter and table title

        registryPanel.add(registryTitle, BorderLayout.NORTH);
        registryPanel.add(scrollPane, BorderLayout.CENTER);       

        totalModule.add(registryPanel, BorderLayout.CENTER);
        
        return totalModule;
    }
}

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class academicLeaderAuditLog extends JPanel {
    private JTable logTable;
    private DefaultTableModel tableModel;
    
    public academicLeaderAuditLog() {
        this.setLayout(new BorderLayout(0, 20));
        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createEmptyBorder(10, 30, 30, 30));
        
        JPanel topSection = new JPanel(new BorderLayout());
        topSection.setOpaque(false);
        JLabel titleLabel = new JLabel("📜 System Activity Audit Log");
        titleLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 18)); // Match report font
        topSection.add(titleLabel, BorderLayout.WEST);
        this.add(topSection, BorderLayout.NORTH);
        
        String[] columns = {"Date & Time", "User ID", "User Name", "Action", "Module Code", "Module Name"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        
        logTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(logTable);
        
        academicLeaderReportUITools.reportTable(logTable, scrollPane);
        
        logTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel component = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                component.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 5));
                if (!isSelected) {
                    component.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 250, 252));
                    component.setForeground(Color.BLACK);
                }
                if (column == 3 && value != null) {
                    String action = value.toString();
                    if (!isSelected) {
                        switch (action) {
                            case "Create":
                                component.setForeground(new Color(40, 167, 69)); 
                                break;
                            case "Edit":
                                component.setForeground(new Color(255, 140, 0)); 
                                break;
                            case "Delete":
                                component.setForeground(new Color(220, 53, 69)); 
                                break;
                        }
                        component.setFont(component.getFont().deriveFont(Font.BOLD));
                    }
                } else {
                    component.setFont(component.getFont().deriveFont(Font.PLAIN));
                }

                return component;
            }
        });
        
        this.add(scrollPane, BorderLayout.CENTER);
    }
    
    public void refreshLogs() {
        tableModel.setRowCount(0); 
        try (BufferedReader br = new BufferedReader(new FileReader("academicLeaderLog.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] data = line.split(","); 
                    tableModel.addRow(data); 
                }
            }
        } catch (IOException e) {
            System.err.println("Audit log file not found.");
        }
    }
}
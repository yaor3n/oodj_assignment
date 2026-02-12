import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.io.*;
import java.util.List;

public class academicLeaderReportUITools {
    public static JPanel createSummaryCard(String titleText, JLabel valueLabel, Color accentColor) {
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
    
    public static void reportTable(JTable table, JScrollPane scrollPane){
        table.setRowHeight(31); 
        table.setGridColor(new Color(230,230,230));
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(1, 1));
        table.getTableHeader().setPreferredSize(new Dimension(0, 25));      
        table.getTableHeader().setDefaultRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel header = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                header.setBackground(new Color(226, 232, 240)); 
                header.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
                header.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(203, 213, 225)),BorderFactory.createEmptyBorder(0, 10, 0, 0)));
                header.setHorizontalAlignment(SwingConstants.LEFT);
                return header;
            }
        });
            
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel component = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                component.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)), // Bottom Line
                    BorderFactory.createEmptyBorder(0, 10, 0, 0) // Left Padding
                ));

                if (!isSelected) {
                    component.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 250, 252));
                }
                return component;
            }
        });
        
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        scrollPane.setColumnHeaderView(table.getTableHeader());
        
        JPanel headerCorner = new JPanel();
        headerCorner.setBackground(new Color(226, 232, 240));
        headerCorner.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(203, 213, 225)));
        scrollPane.setCorner(JScrollPane.UPPER_RIGHT_CORNER, headerCorner);
        
    }
    
    public static JPanel noResultPanel() {
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
        return noResult;
    }
    
    public static JLabel syncTimeLabel(){
        String currentTime = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(java.time.LocalDateTime.now());
        JLabel lastUpdatedLabel = new JLabel("Last synced: " + currentTime);
        lastUpdatedLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lastUpdatedLabel.setForeground(Color.GRAY);
        return lastUpdatedLabel;
    }
    
    public static JButton createExportButton(){
        JButton exportButton = new JButton("\u2913 Export to CSV");
        exportButton.setFont(new Font("Segoe UI Symbol", Font.BOLD, 12));
        exportButton.setBackground(new Color(245, 245, 245));
        exportButton.setForeground(new Color(70, 70, 70));
        exportButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exportButton.setPreferredSize(new Dimension(130, 25));
        exportButton.setFocusPainted(false);
        return exportButton;
    }
    
    public static JPanel createActionRow(JLabel syncTimeLabel, JButton createExportButton){
        JPanel actionRow = new JPanel(new BorderLayout()); // Aligned with the table edge
        actionRow.setOpaque(false);
        actionRow.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));
        actionRow.add(syncTimeLabel, BorderLayout.WEST);
        actionRow.add(createExportButton,BorderLayout.EAST);
        return actionRow;
    }
    
    public static void exportToCSV(JTable table, String fileName, List<String> summaryLines) {
        String home = System.getProperty("user.home");
        String downloadsPath = home + File.separator + "Downloads";
        File fileToSave = new File(downloadsPath, fileName);

        try (FileWriter fw = new FileWriter(fileToSave)) {
            fw.write("REPORT: " + fileName.replace(".csv", "").toUpperCase() + "\n");
            for (String line : summaryLines) {
                fw.write(line + "\n");
            }
            fw.write("\n"); 

            for (int i = 0; i < table.getColumnCount(); i++) {
                fw.write(table.getColumnName(i) + (i == table.getColumnCount() - 1 ? "" : ","));
            }
            fw.write("\n");

            for (int row = 0; row < table.getRowCount(); row++) {
                for (int col = 0; col < table.getColumnCount(); col++) {
                    Object value = table.getValueAt(row, col);
                    String data = (value != null) ? value.toString().replace(",", " ") : "";
                    fw.write(data + (col == table.getColumnCount() - 1 ? "" : ","));
                }
                fw.write("\n");
            }

            JOptionPane.showMessageDialog(null, "Successful download: " + fileName, "Download Complete", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error: Could not save the file.", "Download Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
    
        
}
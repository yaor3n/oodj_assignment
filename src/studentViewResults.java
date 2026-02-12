import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.*;

public class studentViewResults extends JFrame {
    private JTable resultsTable;
    private DefaultTableModel model;

    private final Color BACKGROUND = new Color(209, 213, 219);
    private final Color NAVY_SLATE = new Color(30, 41, 59);
    private final Color HEADER_TEXT = new Color(30, 41, 59);
    private final Color TABLE_HEADER_BG = new Color(241, 245, 249);

    public studentViewResults() {
        try {
            GradeCalculationHandler.syncGrades();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error syncing grades: " + ex.getMessage());
        }

        setTitle("Academic Results Viewer");
        setSize(950, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND);
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
        add(mainPanel);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JLabel header = new JLabel("Assessment Results");
        header.setFont(new Font("Arial", Font.BOLD, 32));
        header.setForeground(HEADER_TEXT);

        JLabel subHeader = new JLabel("Viewing academic records for: " + Session.currentUsername);
        subHeader.setFont(new Font("Arial", Font.PLAIN, 14));
        subHeader.setForeground(new Color(71, 85, 105));

        topPanel.add(header, BorderLayout.NORTH);
        topPanel.add(subHeader, BorderLayout.SOUTH);
        topPanel.setBorder(new EmptyBorder(0, 0, 20, 0));
        mainPanel.add(topPanel, BorderLayout.NORTH);


        String[] columns = {"Module Code", "Assessment", "Lecturer", "Score", "Grade"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        resultsTable = new JTable(model);
        styleTable();

        JScrollPane scrollPane = new JScrollPane(resultsTable);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(148, 163, 184)));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        loadStudentGrades();

        JButton backBtn = new JButton("Return to Dashboard");
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backBtn.setBackground(NAVY_SLATE);
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setOpaque(true);
        backBtn.setBorderPainted(false);
        backBtn.setContentAreaFilled(true);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.setPreferredSize(new Dimension(220, 45));

        backBtn.addActionListener(e -> {
            new studentDashboard();
            dispose();
        });

        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        southPanel.setOpaque(false);
        southPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        southPanel.add(backBtn);
        mainPanel.add(southPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void styleTable() {
        resultsTable.setRowHeight(45);
        resultsTable.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        resultsTable.setGridColor(new Color(226, 232, 240));
        resultsTable.setShowVerticalLines(false);
        resultsTable.setSelectionBackground(new Color(241, 245, 249));

        JTableHeader tableHeader = resultsTable.getTableHeader();
        tableHeader.setPreferredSize(new Dimension(100, 45));
        tableHeader.setBackground(TABLE_HEADER_BG);
        tableHeader.setFont(new Font("Segoe UI", Font.BOLD, 14));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        // Center alignment for Score (Index 3) and Grade (Index 4)
        resultsTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        resultsTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
    }

    private void loadStudentGrades() {
        String targetID = Session.currentUsername.trim();

        try (BufferedReader br = new BufferedReader(new FileReader("Grades.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] p = line.split(",");
                if (p.length >= 6) {
                    if (p[0].trim().equalsIgnoreCase(targetID)) {

                        model.addRow(new Object[]{
                                p[1].trim(), // Module Code
                                p[3].trim(), // Module Name
                                p[2].trim(), // Lecturer
                                p[4].trim(), // Score
                                p[5].trim()  // Grade
                        });
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading Grades.txt");
        }
    }
}
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class defineGradingSystem extends JFrame implements ActionListener {

    private JTable table;
    private DefaultTableModel model;
    private JTextField gradeField, minField, maxField;
    private JButton addBtn, updateBtn, deleteBtn, refreshBtn, exportBtn, backBtn;
    private final String FILE_NAME = "GradingSystem.txt"; // store in TXT

    public defineGradingSystem() {
        setTitle("Define Grading System");
        setLayout(null);

        JLabel title = new JLabel("Grading System");
        title.setFont(new Font("Arial", Font.BOLD, 25));
        title.setBounds(350, 10, 300, 40);
        add(title);

        gradeField = new JTextField();
        gradeField.setBounds(150, 60, 100, 30);
        add(gradeField);

        minField = new JTextField();
        minField.setBounds(270, 60, 100, 30);
        add(minField);

        maxField = new JTextField();
        maxField.setBounds(390, 60, 100, 30);
        add(maxField);

        addBtn = new JButton("Add");
        addBtn.setBounds(510, 60, 80, 30);
        addBtn.addActionListener(this);
        add(addBtn);

        // Table
        model = new DefaultTableModel(new String[]{"Grade", "Min", "Max"}, 0);
        table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(150, 110, 440, 250);
        add(sp);

        updateBtn = new JButton("Update");
        updateBtn.setBounds(150, 380, 100, 30);
        updateBtn.addActionListener(this);
        add(updateBtn);

        deleteBtn = new JButton("Delete");
        deleteBtn.setBounds(270, 380, 100, 30);
        deleteBtn.addActionListener(this);
        add(deleteBtn);

        refreshBtn = new JButton("Refresh");
        refreshBtn.setBounds(390, 380, 100, 30);
        refreshBtn.addActionListener(this);
        add(refreshBtn);

        exportBtn = new JButton("Export CSV");
        exportBtn.setBounds(510, 380, 120, 30);
        exportBtn.addActionListener(this);
        add(exportBtn);

        backBtn = new JButton("Back");
        backBtn.setBounds(510, 430, 120, 30);
        backBtn.addActionListener(this);
        add(backBtn);

        // Ensure file exists
        File f = new File(FILE_NAME);
        try {
            if (!f.exists()) {
                f.createNewFile(); // create empty file if not exist
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error creating file.");
        }

        loadGrades();

        reusable.windowSetup(this);
        setVisible(true);
    }

    // Load grades from TXT (CSV-style)
    private void loadGrades() {
        model.setRowCount(0);
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3) {
                    model.addRow(data);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading file.");
        }
    }

    // Save grades to TXT
    private void saveGrades() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (int i = 0; i < model.getRowCount(); i++) {
                pw.println(model.getValueAt(i, 0) + "," +
                        model.getValueAt(i, 1) + "," +
                        model.getValueAt(i, 2));
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving file.");
        }
    }

    // Export to CSV
    private void exportToCSV() {
        String exportFile = "GradingSystem_export.csv";
        try (PrintWriter pw = new PrintWriter(new FileWriter(exportFile))) {
            for (int i = 0; i < model.getRowCount(); i++) {
                pw.println(model.getValueAt(i, 0) + "," +
                        model.getValueAt(i, 1) + "," +
                        model.getValueAt(i, 2));
            }
            JOptionPane.showMessageDialog(this, "Exported to " + exportFile);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error exporting CSV.");
        }
    }

    // Validate input
    private boolean validateInput(String grade, String minStr, String maxStr) {
        try {
            int min = Integer.parseInt(minStr);
            int max = Integer.parseInt(maxStr);
            if (min > max || min < 0 || max > 100) {
                JOptionPane.showMessageDialog(this, "Invalid min/max range.");
                return false;
            }
            // check duplicate grade name
            for (int i = 0; i < model.getRowCount(); i++) {
                if (model.getValueAt(i, 0).toString().equalsIgnoreCase(grade)) {
                    JOptionPane.showMessageDialog(this, "Grade name already exists.");
                    return false;
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Min/Max must be integers.");
            return false;
        }
        return true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addBtn) {
            String grade = gradeField.getText().trim();
            String min = minField.getText().trim();
            String max = maxField.getText().trim();
            if (grade.isEmpty() || min.isEmpty() || max.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields must be filled.");
                return;
            }
            if (validateInput(grade, min, max)) {
                model.addRow(new String[]{grade, min, max});
                saveGrades();
            }
        }

        if (e.getSource() == updateBtn) {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a row first.");
                return;
            }
            String grade = gradeField.getText().trim();
            String min = minField.getText().trim();
            String max = maxField.getText().trim();
            if (grade.isEmpty() || min.isEmpty() || max.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields must be filled.");
                return;
            }
            try {
                int minVal = Integer.parseInt(min);
                int maxVal = Integer.parseInt(max);
                if (minVal > maxVal || minVal < 0 || maxVal > 100) {
                    JOptionPane.showMessageDialog(this, "Invalid min/max range.");
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Min/Max must be integers.");
                return;
            }
            model.setValueAt(grade, row, 0);
            model.setValueAt(min, row, 1);
            model.setValueAt(max, row, 2);
            saveGrades();
        }

        if (e.getSource() == deleteBtn) {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a row first.");
                return;
            }
            model.removeRow(row);
            saveGrades();
        }

        if (e.getSource() == refreshBtn) {
            gradeField.setText("");
            minField.setText("");
            maxField.setText("");
            loadGrades();
        }

        if (e.getSource() == exportBtn) {
            exportToCSV();
        }

        if (e.getSource() == backBtn) {
            new adminDashboard();
            dispose();
        }
    }

    public static void main(String[] args) {
        new defineGradingSystem();
    }
}

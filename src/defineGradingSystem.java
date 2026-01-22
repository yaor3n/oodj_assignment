import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;

public class defineGradingSystem extends JFrame implements ActionListener {

    private JTable table;
    private DefaultTableModel model;
    private JTextField gradeField;
    private JSpinner minSpinner, maxSpinner;
    private JButton addBtn, deleteBtn, refreshBtn, exportBtn, backBtn;
    private final String FILE_NAME = "GradingSystem.txt"; // store in TXT

    public defineGradingSystem() {
        setTitle("Define Grading System");
        setLayout(null);

        JLabel title = new JLabel("Grading System");
        title.setFont(new Font("Arial", Font.BOLD, 25));
        title.setBounds(300, 10, 300, 40);
        add(title);

        // Grade input
        gradeField = new JTextField();
        gradeField.setBounds(150, 60, 100, 30);
        add(gradeField);

        // Min and Max inputs using spinner
        minSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        minSpinner.setBounds(270, 60, 60, 30);
        minSpinner.setEditor(new JSpinner.NumberEditor(minSpinner, "#"));
        add(minSpinner);

        maxSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        maxSpinner.setBounds(340, 60, 60, 30);
        maxSpinner.setEditor(new JSpinner.NumberEditor(maxSpinner, "#"));
        add(maxSpinner);

        addBtn = new JButton("Add");
        addBtn.setBounds(410, 60, 80, 30);
        addBtn.addActionListener(this);
        add(addBtn);

        // Table
        model = new DefaultTableModel(new String[]{"Grade", "Min", "Max"}, 0);
        table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(150, 110, 440, 250);
        add(sp);

        deleteBtn = new JButton("Delete");
        deleteBtn.setBounds(150, 380, 100, 30);
        deleteBtn.addActionListener(this);
        add(deleteBtn);

        refreshBtn = new JButton("Refresh");
        refreshBtn.setBounds(270, 380, 100, 30);
        refreshBtn.addActionListener(this);
        add(refreshBtn);

        exportBtn = new JButton("Export CSV");
        exportBtn.setBounds(390, 380, 120, 30);
        exportBtn.addActionListener(this);
        add(exportBtn);

        backBtn = new JButton("Back");
        backBtn.setBounds(390, 430, 120, 30);
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

    // Load grades from file
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
        sortGrades();
        detectMissingRanges();
    }

    // Save grades to file
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

    // Sort grades descending by min value (highest first)
    private void sortGrades() {
        java.util.List<Object[]> rows = new ArrayList<>();
        for (int i = 0; i < model.getRowCount(); i++) {
            Object[] r = new Object[3];
            r[0] = model.getValueAt(i, 0);
            r[1] = Integer.parseInt(model.getValueAt(i, 1).toString());
            r[2] = Integer.parseInt(model.getValueAt(i, 2).toString());
            rows.add(r);
        }
        rows.sort(Comparator.comparingInt((Object[] r) -> (int) r[1]).reversed());
        model.setRowCount(0);
        for (Object[] r : rows) {
            model.addRow(r);
        }
    }

    // Detect missing ranges and warn
    private void detectMissingRanges() {
        java.util.List<int[]> ranges = new ArrayList<>();
        for (int i = 0; i < model.getRowCount(); i++) {
            int min = Integer.parseInt(model.getValueAt(i, 1).toString());
            int max = Integer.parseInt(model.getValueAt(i, 2).toString());
            ranges.add(new int[]{min, max});
        }
        ranges.sort(Comparator.comparingInt(a -> a[0]));
        int lastMax = -1;
        for (int[] r : ranges) {
            if (r[0] > lastMax + 1) {
                JOptionPane.showMessageDialog(this, "Missing score range: " + (lastMax + 1) + " to " + (r[0] - 1));
            }
            lastMax = r[1];
        }
    }

    // Validate input
    private boolean validateInput(String grade, int min, int max) {
        if (min > max || min < 0 || max > 100) {
            JOptionPane.showMessageDialog(this, "Invalid min/max range.");
            return false;
        }
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 0).toString().equalsIgnoreCase(grade)) {
                JOptionPane.showMessageDialog(this, "Grade name already exists.");
                return false;
            }
        }
        return true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addBtn) {
            String grade = gradeField.getText().trim();
            int min = (int) minSpinner.getValue();
            int max = (int) maxSpinner.getValue();
            if (grade.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Grade name required.");
                return;
            }
            if (validateInput(grade, min, max)) {
                model.addRow(new Object[]{grade, min, max});
                sortGrades();
                detectMissingRanges();
                saveGrades();
            }
        }

        if (e.getSource() == deleteBtn) {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a row first.");
                return;
            }
            model.removeRow(row);
            sortGrades();
            detectMissingRanges();
            saveGrades();
        }

        if (e.getSource() == refreshBtn) {
            gradeField.setText("");
            minSpinner.setValue(0);
            maxSpinner.setValue(0);
            loadGrades();
        }

        if (e.getSource() == exportBtn) {
            String exportFile = "GradingSystem_export.csv";
            try (PrintWriter pw = new PrintWriter(new FileWriter(exportFile))) {
                for (int i = 0; i < model.getRowCount(); i++) {
                    pw.println(model.getValueAt(i, 0) + "," +
                            model.getValueAt(i, 1) + "," +
                            model.getValueAt(i, 2));
                }
                JOptionPane.showMessageDialog(this, "Exported to " + exportFile);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error exporting CSV.");
            }
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

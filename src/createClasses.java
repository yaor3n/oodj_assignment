import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class createClasses extends JFrame {

    private final String MODULE_FILE = "academicLeaderModule.txt";
    private final String CLASS_FILE = "Classes.txt";

    private JComboBox<String> moduleDropdown;
    private JTextField classField;
    private DefaultTableModel model;

    private ArrayList<String[]> modules = new ArrayList<>();

    public createClasses() {

        setTitle("Create Classes");
        setLayout(null);

        JLabel title = new JLabel("Create Classes for Modules");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBounds(300, 20, 400, 30);
        add(title);

        // ========= MODULE DROPDOWN =========
        JLabel moduleLabel = new JLabel("Select Module:");
        moduleLabel.setBounds(200, 80, 120, 25);
        add(moduleLabel);

        moduleDropdown = new JComboBox<>();
        moduleDropdown.setBounds(330, 80, 300, 25);
        add(moduleDropdown);

        // ========= CLASS NAME =========
        JLabel classLabel = new JLabel("Class Name:");
        classLabel.setBounds(200, 120, 120, 25);
        add(classLabel);

        classField = new JTextField();
        classField.setBounds(330, 120, 300, 25);
        add(classField);

        JButton createBtn = new JButton("Create Class");
        createBtn.setBounds(350, 160, 160, 35);
        createBtn.addActionListener(e -> createClass());
        add(createBtn);

        // ========= TABLE =========
        model = new DefaultTableModel(
                new String[]{"Module ID", "Module Name", "Class Name"}, 0
        );

        JTable table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(100, 220, 700, 250);
        add(sp);

        JButton deleteBtn = new JButton("Delete Selected");
        deleteBtn.setBounds(350, 490, 180, 35);
        deleteBtn.addActionListener(e -> deleteClass(table));
        add(deleteBtn);

        loadModules();
        loadClasses();

        reusable.windowSetup(this);
        setVisible(true);
    }

    // ================= LOAD MODULES =================
    private void loadModules() {
        modules.clear();
        moduleDropdown.removeAllItems();

        try (BufferedReader br = new BufferedReader(new FileReader(MODULE_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length >= 2) {
                    modules.add(p);
                    moduleDropdown.addItem(p[0] + " - " + p[1]);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading modules");
        }
    }

    // ================= CREATE CLASS =================
    private void createClass() {
        if (moduleDropdown.getSelectedIndex() == -1) return;

        String className = classField.getText().trim();
        if (className.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Class name cannot be empty");
            return;
        }

        String[] module = modules.get(moduleDropdown.getSelectedIndex());
        String moduleID = module[0];
        String moduleName = module[1];

        model.addRow(new Object[]{moduleID, moduleName, className});
        saveClasses();

        classField.setText("");
        JOptionPane.showMessageDialog(this, "Class created successfully");
    }

    // ================= LOAD CLASSES =================
    private void loadClasses() {
        model.setRowCount(0);

        try (BufferedReader br = new BufferedReader(new FileReader(CLASS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length == 3) {
                    model.addRow(p);
                }
            }
        } catch (IOException ignored) {}
    }

    // ================= SAVE CLASSES =================
    private void saveClasses() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(CLASS_FILE))) {
            for (int i = 0; i < model.getRowCount(); i++) {
                pw.println(
                        model.getValueAt(i, 0) + "," +
                                model.getValueAt(i, 1) + "," +
                                model.getValueAt(i, 2)
                );
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving classes");
        }
    }

    // ================= DELETE =================
    private void deleteClass(JTable table) {
        int row = table.getSelectedRow();
        if (row == -1) return;

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Delete selected class?",
                "Confirm",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            model.removeRow(row);
            saveClasses();
        }
    }

    public static void main(String[] args) {
        new createClasses();
    }
}

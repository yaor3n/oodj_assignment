import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class crudUsers extends JFrame implements ActionListener {

    private JTable table;
    private DefaultTableModel model;
    private JTextField searchField;
    private JButton searchBtn, refreshBtn, deleteBtn, saveBtn, backBtn;

    private final String FILE_NAME = "accounts.txt";

    // Allowed roles
    private final String[] VALID_ROLES = {
            "Lecturer",
            "Student",
            "AcademicLeader",
            "Admin"
    };

    public crudUsers() {

        setTitle("CRUD Users");
        setLayout(null);

        JLabel title = new JLabel("Manage Users");
        title.setFont(new Font("Arial", Font.BOLD, 25));
        title.setBounds(380, 20, 300, 40);
        add(title);

        // 🔍 Search bar
        searchField = new JTextField();
        searchField.setBounds(250, 80, 300, 30);
        add(searchField);

        searchBtn = new JButton("Search");
        searchBtn.setBounds(560, 80, 100, 30);
        searchBtn.addActionListener(this);
        add(searchBtn);

        // 📋 JTable (CORRECT column order)
        model = new DefaultTableModel(new String[]{"Username", "Password", "Role"}, 0);
        table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(150, 130, 600, 280);
        add(sp);

        // 🔘 Buttons
        refreshBtn = new JButton("Refresh");
        refreshBtn.setBounds(150, 430, 100, 40);
        refreshBtn.addActionListener(this);
        add(refreshBtn);

        deleteBtn = new JButton("Delete");
        deleteBtn.setBounds(270, 430, 100, 40);
        deleteBtn.addActionListener(this);
        add(deleteBtn);

        saveBtn = new JButton("Save");
        saveBtn.setBounds(390, 430, 100, 40);
        saveBtn.addActionListener(this);
        add(saveBtn);

        backBtn = new JButton("Back");
        backBtn.setBounds(650, 500, 100, 40);
        backBtn.addActionListener(this);
        add(backBtn);

        loadUsers("");

        reusable.windowSetup(this);
        setVisible(true);
    }

    // 📂 Load users from accounts.txt
    private void loadUsers(String keyword) {
        model.setRowCount(0);

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                // MUST be username,password,role
                if (data.length == 3) {
                    String username = data[0];
                    String password = data[1];
                    String role = data[2];

                    if (keyword.isEmpty()
                            || username.toLowerCase().contains(keyword)
                            || role.toLowerCase().contains(keyword)) {

                        model.addRow(new Object[]{username, password, role});
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading accounts.txt");
        }
    }

    // ❌ Delete user (table-only until Save)
    private void deleteUser() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a user first");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this user?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            model.removeRow(row);
        }
    }

    // 💾 Save users to accounts.txt with full validation
    private void saveUsers() {

        // 🔥 IMPORTANT: commit table edits first
        if (table.isEditing()) {
            table.getCellEditor().stopCellEditing();
        }

        ArrayList<String> usernames = new ArrayList<>();
        ArrayList<String> outputLines = new ArrayList<>();

        // 1️⃣ Validate everything FIRST
        for (int i = 0; i < model.getRowCount(); i++) {

            String username = model.getValueAt(i, 0).toString().trim();
            String password = model.getValueAt(i, 1).toString().trim();
            String role = model.getValueAt(i, 2).toString().trim();

            if (username.isEmpty() || password.isEmpty() || role.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Empty field at row " + (i + 1),
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            if (usernames.contains(username)) {
                JOptionPane.showMessageDialog(
                        this,
                        "Duplicate username detected: " + username,
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            if (!isValidRole(role)) {
                JOptionPane.showMessageDialog(
                        this,
                        "Invalid role: " + role +
                                "\n\nAllowed roles:\nLecturer, Student, AcademicLeader, Admin",
                        "Invalid Role",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            usernames.add(username);
            outputLines.add(username + "," + password + "," + role);
        }

        // 2️⃣ Write to temp file
        File tempFile = new File("accounts_temp.txt");

        try (PrintWriter pw = new PrintWriter(new FileWriter(tempFile))) {
            for (String line : outputLines) {
                pw.println(line);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error writing temp file");
            return;
        }

        // 3️⃣ Replace original file
        File originalFile = new File(FILE_NAME);

        if (!tempFile.renameTo(originalFile)) {
            JOptionPane.showMessageDialog(this, "Error replacing accounts.txt");
            return;
        }

        JOptionPane.showMessageDialog(
                this,
                "Details saved successfully",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
        );
    }




    // ✅ Role whitelist check
    private boolean isValidRole(String role) {
        for (String r : VALID_ROLES) {
            if (r.equalsIgnoreCase(role)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == searchBtn) {
            loadUsers(searchField.getText().toLowerCase());
        }

        if (e.getSource() == refreshBtn) {
            searchField.setText("");
            loadUsers("");
        }

        if (e.getSource() == deleteBtn) {
            deleteUser();
        }

        if (e.getSource() == saveBtn) {
            saveUsers();
        }

        if (e.getSource() == backBtn) {
            new adminDashboard();
            dispose();
        }
    }

    public static void main(String[] args) {
        new crudUsers();
    }
}

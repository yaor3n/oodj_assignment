import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class adminCrudUsers extends JFrame {

    private final String FILE_NAME = "accounts.txt";

    private ArrayList<User> allUsers = new ArrayList<>();
    private DefaultTableModel model;
    private JTable table;
    private JTextField searchField;

    public adminCrudUsers() {

        reusable.windowSetup(this);
        setLayout(new BorderLayout());

        JPanel heroBar = new JPanel(new BorderLayout());
        heroBar.setBackground(new Color(30, 41, 59));
        heroBar.setPreferredSize(new Dimension(0, 80));

        JLabel titleLabel = new JLabel("Admin - User Management", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));

        heroBar.add(titleLabel, BorderLayout.CENTER);

        // stays on top permanently
        add(heroBar, BorderLayout.NORTH);


        // ========= MAIN SCROLLABLE PANEL =========
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setPreferredSize(new Dimension(1000, 1000)); // taller than frame

        // ========= SEARCH =========
        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setBounds(100, 80, 80, 25);
        mainPanel.add(searchLabel);

        searchField = new JTextField();
        searchField.setBounds(170, 80, 250, 25);
        mainPanel.add(searchField);

        JButton searchBtn = new JButton("Search");
        searchBtn.setBackground(new Color(30,41,59));
        searchBtn.setForeground(new Color(0xFFFFFF));
        searchBtn.setBounds(440, 80, 100, 25);
        searchBtn.addActionListener(e ->
                refreshTable(searchField.getText().trim())
        );
        mainPanel.add(searchBtn);

        JButton resetBtn = new JButton("Reset");
        resetBtn.setBackground(new Color(30,41,59));
        resetBtn.setForeground(new Color(0xFFFFFF));
        resetBtn.setBounds(560, 80, 100, 25);
        resetBtn.addActionListener(e -> {
            searchField.setText("");
            refreshTable("");
        });
        mainPanel.add(resetBtn);

        // ========= TABLE =========
        model = new DefaultTableModel(new String[]{
                "ID", "Full Name", "Email", "Gender", "DOB",
                "Username", "Password", "Role", "Course"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return col != 0; // ID locked
            }
        };

        table = new JTable(model);
        table.setRowHeight(22);

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBounds(50, 130, 980, 280);
        mainPanel.add(tableScroll);

        // ========= UPDATE / DELETE =========
        JButton updateBtn = new JButton("Update Selected");
        updateBtn.setBackground(new Color(40, 167, 69));
        updateBtn.setForeground(new Color(0xFFFFFF));
        updateBtn.setBounds(250, 430, 160, 40);
        updateBtn.addActionListener(e -> updateUsers());
        mainPanel.add(updateBtn);

        JButton deleteBtn = new JButton("Delete Selected");
        deleteBtn.setBackground(new Color(220,53,69));
        deleteBtn.setForeground(new Color(0xFFFFFF));
        deleteBtn.setBounds(430, 430, 160, 40);
        deleteBtn.addActionListener(e -> deleteUser());
        mainPanel.add(deleteBtn);

        JButton backBtn = new JButton("Back");
        backBtn.setBackground(new Color(30,41,59));
        backBtn.setForeground(new Color(0xFFFFFF));
        backBtn.setBounds(610, 430, 160, 40);
        backBtn.addActionListener(e -> {
            new adminDashboard();
            this.dispose();
        });
        mainPanel.add(backBtn);


        // ========= CREATE USER SECTION =========
        JLabel createTitle = new JLabel("Create New User");
        createTitle.setFont(new Font("Arial", Font.BOLD, 18));
        createTitle.setBounds(420, 490, 250, 30);
        mainPanel.add(createTitle);

        int labelX = 200;
        int fieldX = 360;
        int y = 540;
        int gap = 35;

        JTextField fullName = addField(mainPanel, "Full Name", labelX, fieldX, y);
        y += gap;
        JTextField email = addField(mainPanel, "Email", labelX, fieldX, y);
        y += gap;
        JTextField gender = addField(mainPanel, "Gender", labelX, fieldX, y);
        y += gap;
        JTextField dob = addField(mainPanel, "DOB", labelX, fieldX, y);
        y += gap;
        JTextField phoneNo = addField(mainPanel, "DOB", labelX, fieldX, y);
        y += gap;
        JTextField age = addField(mainPanel, "DOB", labelX, fieldX, y);
        y += gap;
        JTextField username = addField(mainPanel, "Username", labelX, fieldX, y);
        y += gap;
        JTextField password = addField(mainPanel, "Password", labelX, fieldX, y);
        y += gap;
        JTextField role = addField(mainPanel, "Role", labelX, fieldX, y);
        y += gap;
        JTextField course = addField(mainPanel, "Course (- if none)", labelX, fieldX, y);

        JButton createBtn = new JButton("Create User");
        createBtn.setBackground(new Color(40, 167, 69));
        createBtn.setForeground(new Color(0xFFFFFF));
        createBtn.setBounds(420, y + 50, 180, 40);
        createBtn.addActionListener(e -> {
            User u = new User(
                    generateNextID(),
                    fullName.getText().trim(),
                    email.getText().trim(),
                    gender.getText().trim(),
                    dob.getText().trim(),
                    phoneNo.getText().trim(),
                    age.getText().trim(),
                    username.getText().trim(),
                    password.getText().trim(),
                    role.getText().trim(),
                    course.getText().trim().isEmpty() ? "-" : course.getText().trim()
            );

            allUsers.add(u);
            saveAll();
            refreshTable("");
            JOptionPane.showMessageDialog(this, "User created successfully");
        });
        mainPanel.add(createBtn);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBounds(0, 0, 1080, 600);
        add(scrollPane);

        scrollPane.getVerticalScrollBar().setPreferredSize(
                new Dimension(15, Integer.MAX_VALUE)
        );

        // faster scroll
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);

        loadUsers();
        refreshTable("");
        setVisible(true);
    }

    // ================= HELPER =================
    private JTextField addField(JPanel panel, String label, int lx, int fx, int y) {
        JLabel l = new JLabel(label);
        l.setBounds(lx, y, 150, 25);
        panel.add(l);

        JTextField f = new JTextField();
        f.setBounds(fx, y, 300, 25);
        panel.add(f);
        return f;
    }

    // ================= FILE =================
    private void loadUsers() {
        allUsers.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length == 11) {
                    allUsers.add(new User(
                            p[0], p[1], p[2], p[3], p[4],
                            p[5], p[6], p[7], p[8], p[9], p[10]
                    ));
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading accounts.txt");
        }
    }

    private void saveAll() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (User u : allUsers)
                pw.println(u.toFileString());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Save failed");
        }
    }

    // ================= LOGIC =================
    private void refreshTable(String keyword) {
        model.setRowCount(0);
        for (User u : allUsers) {
            if (keyword.isEmpty()
                    || u.fullName.toLowerCase().contains(keyword.toLowerCase())
                    || u.username.toLowerCase().contains(keyword.toLowerCase())
                    || u.role.toLowerCase().contains(keyword.toLowerCase())) {

                model.addRow(new Object[]{
                        u.id, u.fullName, u.email, u.gender, u.dob,
                        u.username, u.password, u.role, u.course
                });
            }
        }
    }

    private void updateUsers() {
        if (table.isEditing())
            table.getCellEditor().stopCellEditing();

        for (int i = 0; i < model.getRowCount(); i++) {
            String id = model.getValueAt(i, 0).toString();
            for (User u : allUsers) {
                if (u.id.equals(id)) {
                    u.fullName = model.getValueAt(i, 1).toString();
                    u.email = model.getValueAt(i, 2).toString();
                    u.gender = model.getValueAt(i, 3).toString();
                    u.dob = model.getValueAt(i, 4).toString();
                    u.username = model.getValueAt(i, 5).toString();
                    u.password = model.getValueAt(i, 6).toString();
                    u.role = model.getValueAt(i, 7).toString();
                    u.course = model.getValueAt(i, 8).toString();
                }
            }
        }
        saveAll();
        JOptionPane.showMessageDialog(this, "Users updated successfully");
    }

    private void deleteUser() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        String id = model.getValueAt(row, 0).toString();
        allUsers.removeIf(u -> u.id.equals(id));
        model.removeRow(row);
        saveAll();
    }

    private String generateNextID() {
        return "U" + String.format("%03d", allUsers.size() + 1);
    }

}

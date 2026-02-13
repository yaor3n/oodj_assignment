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

        // ================= HERO BAR =================
        JPanel heroBar = new JPanel(new BorderLayout());
        heroBar.setBackground(new Color(30, 41, 59));
        heroBar.setPreferredSize(new Dimension(0, 80));

        JLabel titleLabel = new JLabel("Admin - User Management", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        heroBar.add(titleLabel, BorderLayout.CENTER);

        add(heroBar, BorderLayout.NORTH);

        // ================= MAIN PANEL =================
        JPanel mainPanel = new JPanel(null);
        mainPanel.setPreferredSize(new Dimension(1100, 1100));

        // ================= SEARCH =================
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
        searchBtn.addActionListener(e -> refreshTable(searchField.getText().trim()));
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

        // ================= TABLE =================
        model = new DefaultTableModel(new String[]{
                "ID", "Full Name", "Email", "Gender", "DOB",
                "Phone", "Age", "Username", "Password", "Role", "Course"
        }, 0) {
            public boolean isCellEditable(int row, int col) {
                return col != 0;
            }
        };

        table = new JTable(model);
        table.setRowHeight(22);

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBounds(50, 130, 1000, 280);
        mainPanel.add(tableScroll);

        // ================= ACTION BUTTONS =================
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
            dispose();
        });
        mainPanel.add(backBtn);

        // ================= CREATE USER =================
        JLabel createTitle = new JLabel("Create New User");
        createTitle.setFont(new Font("Arial", Font.BOLD, 18));
        createTitle.setBounds(420, 490, 250, 30);
        mainPanel.add(createTitle);

        int lx = 200, fx = 360, y = 540, gap = 35;

        JTextField fullName = addField(mainPanel, "Full Name", lx, fx, y); y += gap;
        JTextField email = addField(mainPanel, "Email", lx, fx, y); y += gap;

        JComboBox<String> genderBox = addCombo(mainPanel, "Gender",
                new String[]{"Male", "Female"}, lx, fx, y); y += gap;

        JTextField dob = addField(mainPanel, "DOB", lx, fx, y); y += gap;
        JTextField phone = addField(mainPanel, "Phone No", lx, fx, y); y += gap;
        JTextField age = addField(mainPanel, "Age", lx, fx, y); y += gap;
        JTextField username = addField(mainPanel, "Username", lx, fx, y); y += gap;
        JTextField password = addField(mainPanel, "Password", lx, fx, y); y += gap;

        JComboBox<String> roleBox = addCombo(mainPanel, "Role",
                new String[]{"Admin", "Lecturer", "Student", "AcademicLeader"}, lx, fx, y); y += gap;

        JComboBox<String> courseBox = new JComboBox<>();
        courseBox.addItem("-");
        loadCourses(courseBox);

        JLabel courseLbl = new JLabel("Course");
        courseLbl.setBounds(lx, y, 150, 25);
        mainPanel.add(courseLbl);
        courseBox.setBounds(fx, y, 300, 25);
        mainPanel.add(courseBox);

        JButton clearBtn = new JButton("Clear");
        clearBtn.setBackground(new Color(30,41,59));
        clearBtn.setForeground(Color.WHITE);
        clearBtn.setBounds(300, y + 50, 180, 40);
        clearBtn.addActionListener(e -> {
            fullName.setText("");
            email.setText("");
            dob.setText("");
            phone.setText("");
            age.setText("");
            username.setText("");
            password.setText("");
            genderBox.setSelectedIndex(0);
            roleBox.setSelectedIndex(0);
            courseBox.setSelectedIndex(0);
        });
        mainPanel.add(clearBtn);


        JButton createBtn = new JButton("Create User");
        createBtn.setBackground(new Color(40, 167, 69));
        createBtn.setForeground(new Color(0xFFFFFF));
        createBtn.setBounds(520, y + 50, 180, 40);
        createBtn.addActionListener(e -> {

            if (fullName.getText().isEmpty()
                    || email.getText().isEmpty()
                    || dob.getText().isEmpty()
                    || phone.getText().isEmpty()
                    || age.getText().isEmpty()
                    || username.getText().isEmpty()
                    || password.getText().isEmpty()) {

                JOptionPane.showMessageDialog(this,
                        "All fields except Course are required.");
                return;
            }

            User u = new User(
                    generateNextID(),
                    fullName.getText().trim(),
                    email.getText().trim(),
                    genderBox.getSelectedItem().toString(),
                    dob.getText().trim(),
                    phone.getText().trim(),
                    age.getText().trim(),
                    username.getText().trim(),
                    password.getText().trim(),
                    roleBox.getSelectedItem().toString(),
                    courseBox.getSelectedItem().toString()
            );

            allUsers.add(u);
            saveAll();
            refreshTable("");
            JOptionPane.showMessageDialog(this, "User created successfully");
        });
        mainPanel.add(createBtn);

        JScrollPane scroll = new JScrollPane(mainPanel);
        scroll.getVerticalScrollBar().setUnitIncrement(20); // scroll faster
        add(scroll);

        loadUsers();
        refreshTable("");
        setVisible(true);
    }

    // ================= HELPERS =================
    private JTextField addField(JPanel p, String label, int lx, int fx, int y) {
        JLabel l = new JLabel(label);
        l.setBounds(lx, y, 150, 25);
        p.add(l);

        JTextField f = new JTextField();
        f.setBounds(fx, y, 300, 25);
        p.add(f);
        return f;
    }

    private JComboBox<String> addCombo(JPanel p, String label, String[] items,
                                       int lx, int fx, int y) {
        JLabel l = new JLabel(label);
        l.setBounds(lx, y, 150, 25);
        p.add(l);

        JComboBox<String> box = new JComboBox<>(items);
        box.setBounds(fx, y, 300, 25);
        p.add(box);
        return box;
    }

    private void loadCourses(JComboBox<String> box) {
        try (BufferedReader br = new BufferedReader(
                new FileReader("coursesInAPU.txt"))) {
            String line;
            while ((line = br.readLine()) != null)
                box.addItem(line.trim());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Failed to load coursesInAPU.txt");
        }
    }

    // ================= FILE =================
    private void loadUsers() {
        allUsers.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",", -1); //This tells Java to Keep empty fields, even at the end
                if (p.length == 11)
                    allUsers.add(new User(
                            p[0], p[1], p[2], p[3], p[4],
                            p[5], p[6], p[7], p[8], p[9], p[10]
                    ));
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
    private void refreshTable(String key) {
        model.setRowCount(0);
        for (User u : allUsers) {
            if (key.isEmpty()
                    || u.fullName.toLowerCase().contains(key.toLowerCase())
                    || u.username.toLowerCase().contains(key.toLowerCase())
                    || u.role.toLowerCase().contains(key.toLowerCase())) {

                model.addRow(new Object[]{
                        u.id, u.fullName, u.email, u.gender, u.dob,
                        u.phoneNo, u.age, u.username, u.password, u.role, u.course
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
                    u.phoneNo = model.getValueAt(i, 5).toString();
                    u.age = model.getValueAt(i, 6).toString();
                    u.username = model.getValueAt(i, 7).toString();
                    u.password = model.getValueAt(i, 8).toString();
                    u.role = model.getValueAt(i, 9).toString();
                    u.course = model.getValueAt(i, 10).toString();
                }
            }
        }
        saveAll();
        JOptionPane.showMessageDialog(this, "Users updated successfully");
    }

    private void deleteUser() {

    int row = table.getSelectedRow();

    if (row == -1) {
        JOptionPane.showMessageDialog(this,
                "Please select a user to delete.");
        return;
    }

    String id = model.getValueAt(row, 0).toString();
    String name = model.getValueAt(row, 1).toString();
    String role = model.getValueAt(row, 9).toString();

    int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this user?\n\n" +
            "Name: " + name + "\n" +
            "Role: " + role + "\n" +
            "ID: " + id,
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
    );

    if (confirm != JOptionPane.YES_OPTION) {
        return;
    }

    allUsers.removeIf(u -> u.id.equals(id));

    model.removeRow(row);

    saveAll();

    JOptionPane.showMessageDialog(this,
            "User deleted successfully.");
}


    private String generateNextID() {
        return "U" + String.format("%03d", allUsers.size() + 1);
    }
}

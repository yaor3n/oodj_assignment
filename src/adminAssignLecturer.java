import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class adminAssignLecturer extends JFrame {

    private final String ACCOUNTS_FILE = "accounts.txt";
    private final String ASSIGNED_FILE = "lecturersAssignedToAcademicLeaders.txt";

    private JComboBox<String> lecturerDropdown;
    private JComboBox<String> leaderDropdown;

    private DefaultTableModel tableModel;
    private JTable table;

    private ArrayList<String[]> lecturers = new ArrayList<>();
    private ArrayList<String[]> leaders = new ArrayList<>();

    public adminAssignLecturer() {

        reusable.windowSetup(this);
        setLayout(new BorderLayout());

        // ========= HERO BAR =========
        JPanel heroBar = new JPanel(new BorderLayout());
        heroBar.setBackground(new Color(30, 41, 59));
        heroBar.setPreferredSize(new Dimension(0, 80));

        JLabel titleLabel = new JLabel("Admin - Assign Lecturers", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        heroBar.add(titleLabel, BorderLayout.CENTER);

        add(heroBar, BorderLayout.NORTH);

        // ========= MAIN PANEL =========
        JPanel mainPanel = new JPanel(null);
        mainPanel.setPreferredSize(new Dimension(1000, 700));

        // ========= FORM =========
        JLabel lecLabel = new JLabel("Lecturer:");
        lecLabel.setBounds(250, 90, 150, 25);
        mainPanel.add(lecLabel);

        lecturerDropdown = new JComboBox<>();
        lecturerDropdown.setBounds(400, 90, 300, 25);
        mainPanel.add(lecturerDropdown);

        JLabel leaderLabel = new JLabel("Academic Leader:");
        leaderLabel.setBounds(250, 130, 150, 25);
        mainPanel.add(leaderLabel);

        leaderDropdown = new JComboBox<>();
        leaderDropdown.setBounds(400, 130, 300, 25);
        mainPanel.add(leaderDropdown);

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.setBackground(new Color(30, 41, 59));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setBounds(300, 180, 140, 35);
        refreshBtn.addActionListener(e -> loadAssignments());
        mainPanel.add(refreshBtn);

        JButton assignBtn = new JButton("Assign");
        assignBtn.setBackground(new Color(40, 167, 69));
        assignBtn.setForeground(Color.WHITE);
        assignBtn.setBounds(450, 180, 140, 35);
        assignBtn.addActionListener(e -> adminAssignLecturer());
        mainPanel.add(assignBtn);

        JButton unassignBtn = new JButton("Unassign");
        unassignBtn.setBackground(new Color(220, 53, 69));
        unassignBtn.setForeground(Color.WHITE);
        unassignBtn.setBounds(600, 180, 140, 35);
        unassignBtn.addActionListener(e -> unassignLecturer());
        mainPanel.add(unassignBtn);


        JButton backBtn = new JButton("Back");
        backBtn.setBackground(new Color(30, 41, 59));
        backBtn.setForeground(Color.WHITE);
        backBtn.setBounds(750, 180, 140, 35);
        backBtn.addActionListener(e -> {
            new adminDashboard();
            dispose();
        });
        mainPanel.add(backBtn);

        // ========= TABLE =========
        tableModel = new DefaultTableModel(new String[]{
                "Lecturer ID", "Lecturer Name", "Assigned Academic Leader"
        }, 0);

        table = new JTable(tableModel);
        table.setRowHeight(22);

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBounds(100, 250, 880, 280);
        mainPanel.add(tableScroll);

        // ========= SCROLL =========
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        add(scrollPane, BorderLayout.CENTER);

        // ========= LOAD DATA =========
        loadAccounts();
        loadAssignments();

        setVisible(true);
    }

    // ================= LOAD ACCOUNTS =================
    private void loadAccounts() {
        lecturers.clear();
        leaders.clear();
        lecturerDropdown.removeAllItems();
        leaderDropdown.removeAllItems();

        try (BufferedReader br = new BufferedReader(new FileReader(ACCOUNTS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",", -1); // Keep empty fields
                if (p.length < 11) continue; // full row

                String id = p[0].trim();
                String name = p[1].trim();
                String role = p[9].trim(); // role is at index 9

                if (role.equalsIgnoreCase("Lecturer")) {
                    lecturers.add(new String[]{id, name});
                } else if (role.equalsIgnoreCase("AcademicLeader")) {
                    leaders.add(new String[]{id, name});
                }
            }

            for (String[] l : lecturers)
                lecturerDropdown.addItem(l[0] + " - " + l[1]);

            for (String[] l : leaders)
                leaderDropdown.addItem(l[0] + " - " + l[1]);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to read accounts.txt");
        }
    }


    // ================= LOAD ASSIGNMENTS =================
    private void loadAssignments() {
        tableModel.setRowCount(0);

        ArrayList<String[]> assigned = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ASSIGNED_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length >= 4) assigned.add(p);
            }
        } catch (IOException ignored) {}

        for (String[] lec : lecturers) {
            String status = "None";
            for (String[] a : assigned) {
                if (lec[0].equals(a[0])) {
                    status = a[2] + " - " + a[3];
                    break;
                }
            }
            tableModel.addRow(new Object[]{lec[0], lec[1], status});
        }

        lecturerDropdown.removeAllItems();
        for (String[] lec : lecturers) {
            boolean used = false;
            for (String[] a : assigned) {
                if (lec[0].equals(a[0])) {
                    used = true;
                    break;
                }
            }
            if (!used)
                lecturerDropdown.addItem(lec[0] + " - " + lec[1]);
        }
    }

    // ================= ASSIGN =================
    private void adminAssignLecturer() {
        if (lecturerDropdown.getSelectedIndex() == -1 || leaderDropdown.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Select both Lecturer and Academic Leader");
            return;
        }

        String[] lec = lecturerDropdown.getSelectedItem().toString().split(" - ");
        String[] leader = leaderDropdown.getSelectedItem().toString().split(" - ");

        try (PrintWriter pw = new PrintWriter(new FileWriter(ASSIGNED_FILE, true))) {
            pw.println(lec[0] + "," + lec[1] + "," + leader[0] + "," + leader[1]);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Save failed");
            return;
        }

        JOptionPane.showMessageDialog(this, "Lecturer assigned successfully");
        loadAssignments();
    }

    private void unassignLecturer() {

        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a lecturer from the table");
            return;
        }

        String lecturerId = tableModel.getValueAt(selectedRow, 0).toString();
        String lecturerName = tableModel.getValueAt(selectedRow, 1).toString();
        String assignedLeader = tableModel.getValueAt(selectedRow, 2).toString();

        if (assignedLeader.equals("None")) {
            JOptionPane.showMessageDialog(this, "This lecturer is not assigned.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to unassign\n" +
                lecturerName + " from " + assignedLeader + "?",
                "Confirm Unassign",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        ArrayList<String> updatedLines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(ASSIGNED_FILE))) {

            String line;
            while ((line = br.readLine()) != null) {

                String[] p = line.split(",");

                if (!p[0].equals(lecturerId)) {
                    updatedLines.add(line);
                }
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to read file");
            return;
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(ASSIGNED_FILE))) {

            for (String l : updatedLines) {
                pw.println(l);
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to update file");
            return;
        }

        JOptionPane.showMessageDialog(this, "Lecturer unassigned successfully.");
        loadAssignments();
    }


}

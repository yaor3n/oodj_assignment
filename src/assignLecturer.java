import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class assignLecturer extends JFrame {

    private final String ACCOUNTS_FILE = "accounts.txt";
    private final String ASSIGNED_FILE = "lecturersAssignedToAcademicLeaders.txt";

    private JComboBox<String> lecturerDropdown;
    private JComboBox<String> leaderDropdown;
    private JButton assignBtn, refreshBtn, backBtn;

    private DefaultTableModel tableModel;
    private JTable table;

    private ArrayList<String[]> lecturers = new ArrayList<>();
    private ArrayList<String[]> leaders = new ArrayList<>();

    public assignLecturer() {

        setTitle("Assign Lecturers to Academic Leaders");
        setLayout(null);

        // Apply reusable frame setup (size, center, bg, etc.)
        reusable.windowSetup(this);

        // ===== TITLE =====
        JLabel title = new JLabel("Assign Lecturers");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setBounds(350, 20, 400, 40);
        add(title);

        // ===== LECTURER DROPDOWN =====
        addLabel("Lecturer:", 100, 90);
        lecturerDropdown = new JComboBox<>();
        lecturerDropdown.setBounds(250, 90, 300, 30);
        add(lecturerDropdown);

        // ===== LEADER DROPDOWN =====
        addLabel("Academic Leader:", 100, 140);
        leaderDropdown = new JComboBox<>();
        leaderDropdown.setBounds(250, 140, 300, 30);
        add(leaderDropdown);

        // ===== ASSIGN BUTTON =====
        assignBtn = new JButton("Assign");
        assignBtn.setBounds(580, 115, 120, 30);
        assignBtn.addActionListener(e -> assignLecturer());
        add(assignBtn);

        // ===== REFRESH BUTTON =====
        refreshBtn = new JButton("Refresh");
        refreshBtn.setBounds(580, 160, 120, 30);
        refreshBtn.addActionListener(e -> loadAssignments());
        add(refreshBtn);

        // ===== BACK BUTTON =====
        backBtn = new JButton("Back");
        backBtn.setBounds(580, 205, 120, 30);
        backBtn.addActionListener(e -> {
            new adminDashboard();
            this.dispose();
        });
        add(backBtn);

        // ===== TABLE =====
        tableModel = new DefaultTableModel(new String[]{
                "Lecturer ID", "Lecturer Name", "Assigned Academic Leader"
        }, 0);
        table = new JTable(tableModel);
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(100, 260, 600, 300);
        add(sp);

        // Load accounts and assignments
        loadAccounts();
        loadAssignments();

        setVisible(true);
    }

    // ===== ADD LABEL HELPER =====
    private void addLabel(String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        label.setBounds(x, y, 150, 25);
        add(label);
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
                String[] p = line.split(",");
                if (p.length < 8) continue; // skip invalid
                String role = p[7].trim(); // role is the 8th column
                String id = p[0].trim();
                String name = p[1].trim();

                if (role.equalsIgnoreCase("Lecturer")) {
                    lecturers.add(new String[]{id, name});
                } else if (role.equalsIgnoreCase("AcademicLeader")) {
                    leaders.add(new String[]{id, name});
                }
            }

            // Populate dropdowns
            for (String[] l : lecturers) {
                lecturerDropdown.addItem(l[0] + " - " + l[1]);
            }

            for (String[] l : leaders) {
                leaderDropdown.addItem(l[0] + " - " + l[1]);
            }

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
        } catch (IOException ignored) {} // may not exist yet

        // show all lecturers with assigned leader or "None"
        for (String[] lec : lecturers) {
            String status = "None";
            for (String[] a : assigned) {
                if (lec[0].equals(a[0])) {
                    status = a[2] + " - " + a[3]; // Leader ID - Name
                    break;
                }
            }
            tableModel.addRow(new Object[]{lec[0], lec[1], status});
        }

        // update lecturer dropdown to only include unassigned lecturers
        lecturerDropdown.removeAllItems();
        for (String[] lec : lecturers) {
            boolean isAssigned = false;
            for (String[] a : assigned) {
                if (lec[0].equals(a[0])) {
                    isAssigned = true;
                    break;
                }
            }
            if (!isAssigned) {
                lecturerDropdown.addItem(lec[0] + " - " + lec[1]);
            }
        }
    }

    // ================= ASSIGN =================
    private void assignLecturer() {
        if (lecturerDropdown.getSelectedIndex() == -1 || leaderDropdown.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Select both Lecturer and Academic Leader");
            return;
        }

        String lecturerSel = (String) lecturerDropdown.getSelectedItem();
        String leaderSel = (String) leaderDropdown.getSelectedItem();

        String[] lecturerParts = lecturerSel.split(" - ");
        String[] leaderParts = leaderSel.split(" - ");

        String lecturerID = lecturerParts[0];
        String lecturerName = lecturerParts[1];
        String leaderID = leaderParts[0];
        String leaderName = leaderParts[1];

        // append to file
        try (PrintWriter pw = new PrintWriter(new FileWriter(ASSIGNED_FILE, true))) {
            pw.println(lecturerID + "," + lecturerName + "," + leaderID + "," + leaderName);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to save assignment");
            return;
        }

        JOptionPane.showMessageDialog(this, "Lecturer assigned successfully!");
        loadAssignments();
    }

    public static void main(String[] args) {
        new assignLecturer();
    }
}

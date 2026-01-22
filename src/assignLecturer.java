import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class assignLecturer extends JFrame {

    private final String ACCOUNTS_FILE = "accounts.txt";
    private final String ASSIGNED_FILE = "AssignedLecturers.txt";

    private JComboBox<String> leaderDropdown;
    private JComboBox<String> lecturerDropdown;
    private JButton assignBtn, refreshBtn;

    private DefaultTableModel tableModel;
    private JTable table;

    private final ArrayList<String[]> leaders = new ArrayList<>();
    private final ArrayList<String[]> lecturers = new ArrayList<>();

    public assignLecturer() {

        setTitle("Assign Lecturers to Academic Leaders");
        setLayout(null);
        reusable.windowSetup(this);

        JLabel title = new JLabel("Assign Lecturers");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBounds(300, 10, 400, 40);
        add(title);

        // Leader dropdown
        add(new JLabel("Academic Leader:")).setBounds(50, 70, 150, 25);
        leaderDropdown = new JComboBox<>();
        leaderDropdown.setBounds(180, 70, 250, 25);
        add(leaderDropdown);

        // Lecturer dropdown
        add(new JLabel("Lecturer:")).setBounds(50, 110, 150, 25);
        lecturerDropdown = new JComboBox<>();
        lecturerDropdown.setBounds(180, 110, 250, 25);
        add(lecturerDropdown);

        // Assign button
        assignBtn = new JButton("Assign");
        assignBtn.setBounds(450, 90, 120, 30);
        assignBtn.addActionListener(e -> assignLecturer());
        add(assignBtn);

        // Refresh button
        refreshBtn = new JButton("Refresh");
        refreshBtn.setBounds(580, 90, 120, 30);
        refreshBtn.addActionListener(e -> loadAssignments());
        add(refreshBtn);

        // Table to show assigned/unassigned lecturers
        tableModel = new DefaultTableModel(new String[]{
                "Lecturer ID", "Lecturer Name", "Assigned Academic Leader"
        }, 0);
        table = new JTable(tableModel);
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(50, 160, 650, 300);
        add(sp);

        loadAccounts();
        loadAssignments();

        setVisible(true);
    }

    // ================= LOAD ACCOUNTS =================
    private void loadAccounts() {
        leaders.clear();
        lecturers.clear();
        leaderDropdown.removeAllItems();
        lecturerDropdown.removeAllItems();

        try (BufferedReader br = new BufferedReader(new FileReader(ACCOUNTS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length < 3) continue; // skip invalid
                String role = p[1].trim();
                if (role.equalsIgnoreCase("AcademicLeader")) {
                    leaders.add(p);
                    leaderDropdown.addItem(p[0] + " - " + p[2]); // ID - Name
                } else if (role.equalsIgnoreCase("Lecturer")) {
                    lecturers.add(p);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to read accounts.txt");
        }
    }

    // ================= LOAD ASSIGNMENTS =================
    private void loadAssignments() {
        tableModel.setRowCount(0);

        // Load assigned lecturers
        ArrayList<String[]> assigned = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ASSIGNED_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length >= 4) assigned.add(p); // LecturerID,LecturerName,LeaderID,LeaderName
            }
        } catch (IOException ignored) {} // file may not exist yet

        // Fill lecturer dropdown with unassigned lecturers
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
                lecturerDropdown.addItem(lec[0] + " - " + lec[2]);
            }
        }

        // Show all lecturers in table with their assigned leader or "None"
        for (String[] lec : lecturers) {
            String status = "None";
            for (String[] a : assigned) {
                if (lec[0].equals(a[0])) {
                    status = a[2] + " - " + a[3]; // LeaderID - LeaderName
                    break;
                }
            }
            tableModel.addRow(new Object[]{lec[0], lec[2], status});
        }
    }

    // ================= ASSIGN LECTURER =================
    private void assignLecturer() {
        if (leaderDropdown.getSelectedIndex() == -1 || lecturerDropdown.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Select both Academic Leader and Lecturer");
            return;
        }

        String leaderSelection = (String) leaderDropdown.getSelectedItem();
        String lecturerSelection = (String) lecturerDropdown.getSelectedItem();

        // Extract IDs and Names
        String[] leaderParts = leaderSelection.split(" - ");
        String leaderID = leaderParts[0];
        String leaderName = leaderParts[1];

        String[] lecturerParts = lecturerSelection.split(" - ");
        String lecturerID = lecturerParts[0];
        String lecturerName = lecturerParts[1];

        // Append to file
        try (PrintWriter pw = new PrintWriter(new FileWriter(ASSIGNED_FILE, true))) {
            pw.println(lecturerID + "," + lecturerName + "," + leaderID + "," + leaderName);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to save assignment");
            return;
        }

        JOptionPane.showMessageDialog(this, "Lecturer assigned successfully");
        loadAssignments();
    }

    public static void main(String[] args) {
        new assignLecturer();
    }
}

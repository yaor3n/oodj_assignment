import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class createClasses extends JFrame {

    private final String MODULE_FILE = "academicLeaderModule.txt";
    private final String CLASS_FILE = "Classes.txt";
    private final String ROOM_FILE = System.getProperty("user.dir") + File.separator + "classroomsInApu.txt";

    private JComboBox<String> moduleDropdown;
    private JComboBox<Integer> dayBox, yearBox;
    private JComboBox<String> monthBox, startTimeBox, endTimeBox, roomDropdown;
    private JTextField classNameField;

    private DefaultTableModel model;
    private JTable table;

    private final ArrayList<String[]> modules = new ArrayList<>();

    public createClasses() {

        reusable.windowSetup(this);

        JPanel panel = new JPanel(null);
        panel.setPreferredSize(new Dimension(1080, 900));

        JLabel title = new JLabel("Admin - Create & Manage Classes");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBounds(360, 20, 500, 30);
        panel.add(title);

        // MODULE
        panel.add(label("Module:", 120, 80));
        moduleDropdown = new JComboBox<>();
        moduleDropdown.setBounds(260, 80, 550, 25);
        panel.add(moduleDropdown);

        // CLASS NAME
        panel.add(label("Class Name:", 120, 120));
        classNameField = field(260, 120, panel);

        // ROOM
        panel.add(label("Room:", 120, 160));
        roomDropdown = new JComboBox<>();
        roomDropdown.setBounds(260, 160, 300, 25);
        panel.add(roomDropdown);

        // DATE
        panel.add(label("Date:", 120, 200));
        dayBox = new JComboBox<>();
        for (int i = 1; i <= 31; i++) dayBox.addItem(i);
        dayBox.setBounds(260, 200, 60, 25);
        panel.add(dayBox);

        monthBox = new JComboBox<>(new String[]{
                "January","February","March","April","May","June",
                "July","August","September","October","November","December"
        });
        monthBox.setBounds(330, 200, 120, 25);
        panel.add(monthBox);

        yearBox = new JComboBox<>();
        for (int y = 2026; y <= 2030; y++) yearBox.addItem(y);
        yearBox.setBounds(460, 200, 80, 25);
        panel.add(yearBox);

        // TIME
        panel.add(label("Start Time:", 120, 240));
        panel.add(label("End Time:", 120, 280));
        startTimeBox = new JComboBox<>();
        endTimeBox = new JComboBox<>();
        for (int h = 8; h <= 18; h++) {
            String t = String.format("%02d:00", h);
            startTimeBox.addItem(t);
            endTimeBox.addItem(t);
        }
        startTimeBox.setBounds(260, 240, 100, 25);
        endTimeBox.setBounds(260, 280, 100, 25);
        panel.add(startTimeBox);
        panel.add(endTimeBox);

        // CREATE BUTTON
        JButton createBtn = new JButton("Create Class");
        createBtn.setBounds(300, 330, 200, 40);
        createBtn.addActionListener(e -> createClass());
        panel.add(createBtn);

        JButton backBtn = new JButton("Back");
        backBtn.setBounds(550, 330, 200, 40);
        backBtn.addActionListener(e -> {
                new adminDashboard();
                this.dispose();
        });
        panel.add(backBtn);

        // TABLE
        model = new DefaultTableModel(
                new String[]{
                        "ModuleID","ModuleName","LevelOfEdu","LecturerID","LecturerName","Month","Year","Description","ModulePic","Course",
                        "ClassID","ClassName","Room","Date","StartTime","EndTime"
                }, 0
        );
        table = new JTable(model);
        table.setRowHeight(22);

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBounds(60, 400, 960, 220);
        panel.add(tableScroll);

        // DELETE BUTTON
        JButton deleteBtn = new JButton("Delete Selected");
        deleteBtn.setBounds(430, 650, 220, 40);
        deleteBtn.addActionListener(e -> deleteSelected());
        panel.add(deleteBtn);

        // SCROLL
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBounds(0, 0, 1080, 650);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        add(scrollPane);

        loadModules();
        loadRooms();
        loadClasses();

        setVisible(true);
    }

    // ---------------- HELPERS ----------------
    private JLabel label(String text, int x, int y) {
        JLabel l = new JLabel(text);
        l.setBounds(x, y, 120, 25);
        return l;
    }

    private JTextField field(int x, int y, JPanel p) {
        JTextField f = new JTextField();
        f.setBounds(x, y, 300, 25);
        p.add(f);
        return f;
    }

    // ---------------- LOAD MODULES ----------------
    private void loadModules() {
        modules.clear();
        moduleDropdown.removeAllItems();
        try (BufferedReader br = new BufferedReader(new FileReader(MODULE_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length >= 10) {
                    modules.add(p);
                    moduleDropdown.addItem(p[0] + " - " + p[1]);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Cannot open academicLeaderModule.txt");
        }
    }

    // ---------------- LOAD ROOMS ----------------
    private void loadRooms() {
        roomDropdown.removeAllItems();
        try (BufferedReader br = new BufferedReader(new FileReader(ROOM_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                roomDropdown.addItem(line.trim());
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Cannot open classroomsInApu.txt");
        }
    }

    // ---------------- GENERATE CLASS ID ----------------
    private String generateClassID() {
        int max = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(CLASS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("ModuleID")) continue; // skip header
                String[] p = line.split(",");
                if (p.length >= 11 && p[10].startsWith("C")) {
                    try {
                        int n = Integer.parseInt(p[10].substring(1));
                        if (n > max) max = n;
                    } catch (NumberFormatException ignored) {}
                }
            }
        } catch (IOException ignored) {}
        return "C" + String.format("%03d", max + 1);
    }

    // ---------------- CREATE CLASS ----------------
    private void createClass() {
        if (moduleDropdown.getSelectedIndex() == -1) return;

        String className = classNameField.getText().trim();
        if (className.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Class name required");
            return;
        }

        String start = startTimeBox.getSelectedItem().toString();
        String end = endTimeBox.getSelectedItem().toString();
        if (end.compareTo(start) <= 0) {
            JOptionPane.showMessageDialog(this, "End time must be after start time");
            return;
        }

        String date = dayBox.getSelectedItem() + " " +
                monthBox.getSelectedItem() + " " +
                yearBox.getSelectedItem();

        String[] m = modules.get(moduleDropdown.getSelectedIndex());
        String classID = generateClassID();
        String room = roomDropdown.getSelectedItem().toString();

        String line = String.join(",", m) + "," +
                classID + "," + className + "," +
                room + "," + date + "," + start + "," + end;

        // Write file with header if empty
        File f = new File(CLASS_FILE);
        boolean needHeader = !f.exists() || f.length() == 0;
        try (PrintWriter pw = new PrintWriter(new FileWriter(CLASS_FILE, true))) {
            if (needHeader) {
                StringBuilder header = new StringBuilder();
                for (int i = 0; i < model.getColumnCount(); i++) {
                    header.append(model.getColumnName(i));
                    if (i < model.getColumnCount() - 1) header.append(",");
                }
                pw.println(header);
            }
            pw.println(line);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to save class");
            return;
        }

        model.addRow(new Object[]{
                m[0], m[1], m[2], m[3], m[4], m[5], m[6], m[7], m[8], m[9],
                classID, className, room, date, start, end
        });

        classNameField.setText("");
    }

    // ---------------- DELETE CLASS ----------------
    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        int confirm = JOptionPane.showConfirmDialog(
                this, "Delete selected class?", "Confirm",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm != JOptionPane.YES_OPTION) return;

        model.removeRow(row);

        // Rewrite file with header
        try (PrintWriter pw = new PrintWriter(new FileWriter(CLASS_FILE))) {
            StringBuilder header = new StringBuilder();
            for (int i = 0; i < model.getColumnCount(); i++) {
                header.append(model.getColumnName(i));
                if (i < model.getColumnCount() - 1) header.append(",");
            }
            pw.println(header);

            for (int i = 0; i < model.getRowCount(); i++) {
                StringBuilder sb = new StringBuilder();
                for (int c = 0; c < model.getColumnCount(); c++) {
                    sb.append(model.getValueAt(i, c));
                    if (c < model.getColumnCount() - 1) sb.append(",");
                }
                pw.println(sb);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Save failed");
        }
    }

    // ---------------- LOAD CLASSES ----------------
    private void loadClasses() {
        model.setRowCount(0);
        try (BufferedReader br = new BufferedReader(new FileReader(CLASS_FILE))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) { firstLine = false; continue; } // skip header
                String[] p = line.split(",");
                if (p.length >= 16) {
                    model.addRow(p);
                }
            }
        } catch (IOException ignored) {}
    }

    public static void main(String[] args) {
        new createClasses();
    }
}

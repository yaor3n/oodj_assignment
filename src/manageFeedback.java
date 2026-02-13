import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class manageFeedback extends JFrame {

    private final String FILE_NAME = "student_feedback.txt";

    private JTable table;
    private DefaultTableModel model;
    private ArrayList<String[]> feedbackList = new ArrayList<>();

    public manageFeedback() {

        reusable.windowSetup(this);
        setLayout(null);

        JLabel title = new JLabel("Manage Feedback");
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setBounds(380, 20, 300, 40);
        add(title);

        model = new DefaultTableModel(
                new String[]{
                        "Student Username",
                        "Module Name",
                        "Lecturer Name",
                        "Rating",
                        "Student Comment",
                        "Lecturer Comment"
                }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false; // Admin can only delete
            }
        };

        table = new JTable(model);
        table.setRowHeight(24);
        table.getColumnModel().getColumn(5).setPreferredWidth(350);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(40, 90, 1000, 350);
        add(scrollPane);

        JButton deleteBtn = new JButton("Delete Selected Feedback");
        deleteBtn.setBackground(new Color(220,53,69));
        deleteBtn.setForeground(new Color(0xFFFFFF));
        deleteBtn.setBounds(300, 460, 260, 40);
        deleteBtn.setFont(new Font("Arial", Font.BOLD, 16));
        deleteBtn.addActionListener(e -> deleteFeedback());
        add(deleteBtn);

        JButton backBtn = new JButton("Back");
        backBtn.setBackground(new Color(30,41,59));
        backBtn.setForeground(new Color(0xFFFFFF));
        backBtn.setBounds(600, 460, 120, 40);
        backBtn.addActionListener(e -> {
            new adminDashboard();
            dispose();
        });
        add(backBtn);

        loadFeedback();
        setVisible(true);
    }

    private void loadFeedback() {
        feedbackList.clear();
        model.setRowCount(0);

        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = br.readLine()) != null) {

                String[] p = line.split(",",-1);

                String[] row = new String[6];
                for (int i = 0; i < 6; i++) {
                    row[i] = (i < p.length) ? p[i] : "";
                }

                feedbackList.add(row);
                model.addRow(row);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading feedback file");
        }
    }


    private void deleteFeedback() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a feedback entry");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this feedback?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        feedbackList.remove(row);
        model.removeRow(row);
        saveAll();

        JOptionPane.showMessageDialog(this, "Feedback deleted successfully");
    }

    private void saveAll() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (String[] f : feedbackList) {
                pw.println(String.join(",", f));
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to save feedback");
        }
    }
}

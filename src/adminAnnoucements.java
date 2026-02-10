import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class adminAnnoucements extends JFrame {

    private JComboBox<String> roleBox;
    private JTextField titleField;
    private JTextArea messageArea;

    private JPanel cardsPanel;
    private JPanel selectedCard = null;
    private String selectedLine = null;

    private static final String FILE_NAME = "announcements.txt";

    public adminAnnoucements() {
        reusable.windowSetup(this);

        JLabel header = new JLabel("Admin Announcements");
        header.setFont(new Font("Arial", Font.BOLD, 26));
        header.setBounds(30, 20, 500, 30);
        add(header);

        // ===== CREATE SECTION (TOP) =====
        JLabel roleLbl = new JLabel("Target Role:");
        roleLbl.setBounds(30, 80, 120, 25);
        add(roleLbl);

        roleBox = new JComboBox<>(new String[]{
                "All", "Student", "Lecturer", "AcademicLeader"
        });
        roleBox.setBounds(150, 80, 200, 25);
        add(roleBox);

        JLabel titleLbl = new JLabel("Title:");
        titleLbl.setBounds(30, 120, 120, 25);
        add(titleLbl);

        titleField = new JTextField();
        titleField.setBounds(150, 120, 350, 25);
        add(titleField);

        JLabel msgLbl = new JLabel("Message:");
        msgLbl.setBounds(30, 160, 120, 25);
        add(msgLbl);

        messageArea = new JTextArea();
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        JScrollPane msgScroll = new JScrollPane(messageArea);
        msgScroll.setBounds(150, 160, 350, 120);
        add(msgScroll);

        JButton publishBtn = new JButton("Publish Announcement");
        publishBtn.setBounds(150, 300, 220, 35);
        publishBtn.addActionListener(e -> publishAnnouncement());
        add(publishBtn);

        JSeparator sep = new JSeparator();
        sep.setBounds(30, 360, 1000, 10);
        add(sep);

        JLabel listLbl = new JLabel("Existing Announcements");
        listLbl.setFont(new Font("Arial", Font.BOLD, 18));
        listLbl.setBounds(30, 380, 400, 25);
        add(listLbl);

        cardsPanel = new JPanel(null);
        cardsPanel.setBackground(Color.WHITE);

        JScrollPane cardScroll = new JScrollPane(cardsPanel);
        cardScroll.setBounds(30, 420, 1000, 140);
        add(cardScroll);

        JButton deleteBtn = new JButton("Delete Selected Announcement");
        deleteBtn.setBounds(30, 570, 280, 35);
        deleteBtn.setBackground(new Color(220, 53, 69));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setFocusPainted(false);
        deleteBtn.addActionListener(e -> deleteAnnouncement());
        add(deleteBtn);

        JButton backBtn = new JButton("Back");
        backBtn.setBackground(new Color(30,41,59));
        backBtn.setForeground(new Color(0xFFFFFF));
        backBtn.setBounds(500, 570, 160, 40);
        backBtn.addActionListener(e -> {
            new adminDashboard();
            dispose();
        });
        add(backBtn);

        loadAnnouncements();
        setVisible(true);
    }

    private void publishAnnouncement() {
        String role = roleBox.getSelectedItem().toString();
        String title = titleField.getText().trim().replace(",", " ");
        String message = messageArea.getText().trim().replace(",", " ");

        if (title.isEmpty() || message.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Title and message cannot be empty.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to publish this announcement?",
                "Confirm",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        String id = "A" + System.currentTimeMillis();
        String time = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME, true))) {
            pw.println(id + "," + time + "," + role + "," + title + "," + message);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving announcement.");
        }

        titleField.setText("");
        messageArea.setText("");
        loadAnnouncements();
    }

    private void loadAnnouncements() {
        cardsPanel.removeAll();
        selectedCard = null;
        selectedLine = null;

        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        int y = 10;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",", 5);
                if (p.length != 5) continue;

                JPanel card = createCard(p, line);
                card.setBounds(10, y, 960, 100);
                cardsPanel.add(card);
                y += 115;
            }
        } catch (IOException ignored) {}

        cardsPanel.setPreferredSize(new Dimension(960, y));
        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    private JPanel createCard(String[] p, String rawLine) {
        JPanel card = new JPanel(null);
        card.setBackground(new Color(248, 250, 252));
        card.setBorder(BorderFactory.createLineBorder(new Color(203, 213, 225)));

        JLabel role = new JLabel(p[2]);
        role.setFont(new Font("Arial", Font.BOLD, 12));
        role.setForeground(new Color(37, 99, 235));
        role.setBounds(15, 8, 200, 20);
        card.add(role);

        JLabel date = new JLabel(p[1]);
        date.setBounds(750, 8, 200, 20);
        card.add(date);

        JLabel title = new JLabel(p[3]);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setBounds(15, 32, 900, 25);
        card.add(title);

        JTextArea msg = new JTextArea(p[4]);
        msg.setEditable(false);
        msg.setLineWrap(true);
        msg.setWrapStyleWord(true);
        msg.setBackground(card.getBackground());
        msg.setBounds(15, 60, 920, 30);
        card.add(msg);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (selectedCard != null) {
                    selectedCard.setBorder(
                            BorderFactory.createLineBorder(new Color(203, 213, 225))
                    );
                }
                selectedCard = card;
                selectedLine = rawLine;
                card.setBorder(
                        BorderFactory.createLineBorder(new Color(37, 99, 235), 2)
                );
            }
        });

        return card;
    }

    private void deleteAnnouncement() {
        if (selectedLine == null) {
            JOptionPane.showMessageDialog(this, "Select an announcement to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this announcement?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        ArrayList<String> remaining = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.equals(selectedLine)) remaining.add(line);
            }
        } catch (IOException ignored) {}

        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (String s : remaining) pw.println(s);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error deleting announcement.");
        }

        loadAnnouncements();
    }

    public static void main(String[] args) {
        new adminAnnoucements();
    }
}

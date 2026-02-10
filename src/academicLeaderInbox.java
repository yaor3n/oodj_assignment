import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class academicLeaderInbox extends JFrame {

    private JPanel cardsPanel;
    private String roleFilter1;
    private String roleFilter2;

    private static final String FILE_NAME = "announcements.txt";

    public academicLeaderInbox(String roleFilter1, String roleFilter2) {
        this.roleFilter1 = roleFilter1; // e.g., "Lecturer"
        this.roleFilter2 = roleFilter2; // e.g., "All"

        reusable.windowSetup(this);

        JLabel header = new JLabel("Notifications Inbox");
        header.setFont(new Font("Arial", Font.BOLD, 26));
        header.setBounds(30, 20, 600, 30);
        add(header);

        cardsPanel = new JPanel(null);
        cardsPanel.setBackground(Color.WHITE);

        JScrollPane cardScroll = new JScrollPane(cardsPanel);
        cardScroll.setBounds(30, 70, 1000, 400);
        add(cardScroll);

        loadNotifications();

        JButton backBtn = new JButton("Back");
        backBtn.setBackground(new Color(30, 41, 59));
        backBtn.setForeground(Color.WHITE);
        backBtn.setBounds(450, 500, 140, 35);
        backBtn.addActionListener(e -> {
            new academicLeaderDashboard();
            dispose(); 
        });
        this.add(backBtn);

        setVisible(true);
    }

    private void loadNotifications() {
        cardsPanel.removeAll();

        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        int y = 10;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",", 5); // id, timestamp, role, title, message
                if (p.length != 5) continue;

                String announcementRole = p[2].trim(); // trim spaces
                // Show if role matches either filter
                if (!announcementRole.equalsIgnoreCase(roleFilter1) &&
                    !announcementRole.equalsIgnoreCase(roleFilter2)) continue;

                JPanel card = createCard(p);
                card.setBounds(10, y, 960, 100);
                cardsPanel.add(card);
                y += 115;
            }
        } catch (IOException ignored) {}

        cardsPanel.setPreferredSize(new Dimension(960, y));
        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    private JPanel createCard(String[] p) {
        JPanel card = new JPanel(null);
        card.setBackground(new Color(248, 250, 252));
        card.setBorder(BorderFactory.createLineBorder(new Color(203, 213, 225)));

        JLabel role = new JLabel("Role: " + p[2].trim());
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

        return card;
    }

    public static void main(String[] args) {
        new academicLeaderInbox("AcademicLeader", "All");

    }
}

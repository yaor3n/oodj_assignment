import javax.swing.*;
import java.awt.*;
import java.io.*;

public class adminEditProfile extends JFrame {

    private JTextField usernameField, nameField;
    private JPasswordField passwordField;

    private final String ACCOUNTS_FILE = "accounts.txt";

    public adminEditProfile() {

        setLayout(null);
        reusable.windowSetup(this);

        JLabel title = new JLabel("Edit Profile");
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setBounds(420, 30, 300, 40);
        add(title);

        addLabel("Username:", 300, 120);
        usernameField = inputField(450, 120);
        usernameField.setEditable(false);

        addLabel("Name:", 300, 180);
        nameField = inputField(450, 180);

        addLabel("Password:", 300, 240);
        passwordField = new JPasswordField();
        passwordField.setBounds(450, 240, 250, 35);
        add(passwordField);

        JButton saveBtn = new JButton("Save");
        saveBtn.setBounds(450, 320, 120, 40);
        saveBtn.addActionListener(e -> saveProfile());
        add(saveBtn);

        JButton backBtn = new JButton("Back");
        backBtn.setBounds(580, 320, 120, 40);
        backBtn.addActionListener(e -> {
            new adminDashboard();
            dispose();
        });
        add(backBtn);

        loadAdminDetails();
        setVisible(true);
    }

    private void addLabel(String text, int x, int y) {
        JLabel l = new JLabel(text);
        l.setBounds(x, y, 120, 30);
        add(l);
    }

    private JTextField inputField(int x, int y) {
        JTextField t = new JTextField();
        t.setBounds(x, y, 250, 35);
        add(t);
        return t;
    }

    private void loadAdminDetails() {
        try (BufferedReader br = new BufferedReader(new FileReader(ACCOUNTS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length >= 3 && p[2].equalsIgnoreCase("Admin")) {
                    usernameField.setText(p[0]);
                    nameField.setText(p[0]); // adjust if you store real name elsewhere
                    break;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to load admin profile");
        }
    }

    private void saveProfile() {
        JOptionPane.showMessageDialog(this, "Profile updated (demo)");
        // You can later rewrite accounts.txt properly using temp file logic
    }

    public static void main(String[] args) {
        new adminEditProfile();
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class login extends JFrame implements ActionListener{
    private JTextField usernameInput;
    private JPasswordField passwordInput;
    private JButton loginButton;

    public login() {
        reusable.windowSetup(this);
        this.setLayout(new GridBagLayout());

        JPanel loginPage = new JPanel(new GridLayout(1,2));
        loginPage.setPreferredSize(new Dimension(1050,500));
        loginPage.setBackground(Color.WHITE);
        //loginPage.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        //left side
        JPanel leftPanel = new JPanel (new GridBagLayout());
        leftPanel.setOpaque(true);
        leftPanel.setBackground(Color.WHITE);
        GridBagConstraints gbcLeft = new GridBagConstraints();
        gbcLeft.gridx = 0;
        gbcLeft.anchor = GridBagConstraints.CENTER;

        //apu logo
        JLabel apuLogo = new JLabel();
        ImageIcon icon = new ImageIcon("images/APUlogo.png");
        if(icon.getImage() != null) {
            Image img = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            apuLogo.setIcon(new ImageIcon(img));
        }
        gbcLeft.gridy = 0;
        gbcLeft.insets = new Insets(0, 0, 20, 0);
        leftPanel.add(apuLogo, gbcLeft);

        JLabel leftLabel = new JLabel("Assessment Feedback System");
        leftLabel.setFont(new Font("Segoe UI", Font.BOLD, 18)); // Made it larger for hierarchy
        leftLabel.setForeground(new Color(30, 41, 59));
        gbcLeft.gridy = 1;
        gbcLeft.insets = new Insets(10, 0, 0, 0);
        leftPanel.add(leftLabel, gbcLeft);

        JTextArea leftContent = new JTextArea("A unified platform designed for Asia Pacific University (APU) to streamline assessment workflows and provide real-time, constructive feedback for academic excellence.");
        leftContent.setLineWrap(true);       // Moves text to next line at the edge
        leftContent.setWrapStyleWord(true);  // Prevents words from being cut in half
        leftContent.setEditable(false);      // Makes it read-only
        leftContent.setFocusable(false);     // Prevents cursor interaction
        leftContent.setFont(new Font("Segoe UI", Font.PLAIN, 12));

//
        leftContent.setForeground(new Color(100, 116, 139));
        leftContent.setPreferredSize(new Dimension(330, 100));

        gbcLeft.gridy = 2;
        gbcLeft.insets = new Insets(10, 40, 0, 40);
        leftPanel.add(leftContent, gbcLeft);


        //right side
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setOpaque(true);
        rightPanel.setBackground(Color.WHITE);
        //green color line beside right panel
        rightPanel.setBorder(BorderFactory.createMatteBorder(0, 3, 0, 0, new Color(4, 140, 90)));

        GridBagConstraints gbcRight = new GridBagConstraints();
        gbcRight.gridx = 0;
        gbcRight.fill = GridBagConstraints.HORIZONTAL;
        gbcRight.insets = new Insets(10, 40, 10, 40);

        JLabel welcomeLabel = new JLabel("Welcome");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 45));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbcRight.gridy = 0;
        gbcRight.anchor = GridBagConstraints.CENTER;
        gbcRight.fill = GridBagConstraints.NONE;
        rightPanel.add(welcomeLabel, gbcRight);

        JLabel subLabel = new JLabel("Login to continue");
        subLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subLabel.setForeground(Color.GRAY);
        gbcRight.gridy = 1;
        gbcRight.insets = new Insets(0, 40, 30, 40);
        rightPanel.add(subLabel, gbcRight);

        //username
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        gbcRight.gridy = 2;
        gbcRight.anchor = GridBagConstraints.WEST;
        gbcRight.insets = new Insets(10, 40, 0, 40);
        rightPanel.add(userLabel, gbcRight);

        usernameInput = new JTextField();
        usernameInput.setPreferredSize(new Dimension(300, 45));
        usernameInput.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK),BorderFactory.createEmptyBorder(0, 10, 0, 0)));
        gbcRight.gridy = 3;
        //gbcRight.fill = GridBagConstraints.HORIZONTAL;
        gbcRight.insets = new Insets(10, 40, 10, 40);
        rightPanel.add(usernameInput, gbcRight);

        //password
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        gbcRight.gridy = 4;
        gbcRight.anchor = GridBagConstraints.WEST;
        gbcRight.insets = new Insets(10, 40, 0, 40);
        rightPanel.add(passwordLabel, gbcRight);

        passwordInput = new JPasswordField();
        passwordInput.setPreferredSize(new Dimension(300, 40));
        passwordInput.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK),BorderFactory.createEmptyBorder(0, 10, 0, 0)));
        gbcRight.gridy = 5;
        gbcRight.insets = new Insets(5, 40, 15, 40);
        rightPanel.add(passwordInput, gbcRight);

        loginButton = new JButton("LOGIN");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.setBackground(new Color(4, 140, 90));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBorderPainted(false);
        loginButton.setFocusPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.setPreferredSize(new Dimension(300, 45));
        gbcRight.gridy = 6;
        gbcRight.insets = new Insets(30, 40, 15, 40);
        loginButton.addActionListener(this);
        rightPanel.add(loginButton, gbcRight);

        JButton exitButton = new JButton("Exit System");
        exitButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        exitButton.setFocusPainted(false);
        exitButton.setBorderPainted(false);
        exitButton.setOpaque(true);
        exitButton.setBackground(new Color(220, 38, 38));
        exitButton.setForeground(Color.WHITE);
        exitButton.setContentAreaFilled(true);
        exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exitButton.setPreferredSize(new Dimension(300, 35));
        exitButton.addActionListener(e -> {
            int confirmation = JOptionPane.showConfirmDialog(this, "Close the Assessment Feedback System?", "Shut Down", JOptionPane.YES_NO_OPTION);
            if (confirmation ==JOptionPane.YES_OPTION){
                System.exit(0);
            }
        });
        gbcRight.gridy = 7;
        gbcRight.insets = new Insets(0, 40, 10, 40);
        rightPanel.add(exitButton, gbcRight);

        loginPage.add(leftPanel);
        loginPage.add(rightPanel);
        this.add(loginPage);

        this.revalidate();
        this.repaint();
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String username = usernameInput.getText().trim();
            String password = new String(passwordInput.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty() ){
                JOptionPane.showMessageDialog(this, "Please enter both Username and Password!", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String[] userData = academicLeaderFileManager.authenticate(username, password);

            if (userData != null) {
                String role = userData[9].trim();
                Session.currentUsername = userData[7].trim();

                switch (role) {
                    case "Admin": new adminDashboard(); break;
                    case "Lecturer": new lecturerDashboard(username); break;
                    case "Student": new studentDashboard(); break;
                    case "AcademicLeader": new academicLeaderDashboard(); break;
                    default:
                        JOptionPane.showMessageDialog(this, "Role not recognized: "+role);
                        return;
                }
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Username or Password!", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

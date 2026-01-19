import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class login extends JFrame implements ActionListener{
    private JTextField usernameInput;
    private JPasswordField passwordInput;
    private JButton loginButton;

    public login() {
        reusable.windowSetup(this);
        this.setLayout(new GridLayout(1,2));
        
        //left side
        JPanel leftPanel = new JPanel (new GridBagLayout());
        leftPanel.setOpaque (false);
        GridBagConstraints gbcLeft = new GridBagConstraints();
        gbcLeft.gridx = 0;
        gbcLeft.gridy = 0; 
        
        //apu logo
        JLabel apuLogo = new JLabel();
        ImageIcon icon = new ImageIcon("APUlogo.png");
        if(icon.getImage() != null) {
            Image img = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            apuLogo.setIcon(new ImageIcon(img));
        }
        apuLogo.setHorizontalAlignment(SwingConstants.CENTER); 
        leftPanel.add(apuLogo, gbcLeft);
        
        //word under apu logo
        gbcLeft.gridy = 1;

        JTextArea leftContent = new JTextArea("A unified platform designed for Asia Pacific University (APU) to streamline assessment workflows and provide real-time, constructive feedback for academic excellence.");
        leftContent.setLineWrap(true);       // Moves text to next line at the edge
        leftContent.setWrapStyleWord(true);  // Prevents words from being cut in half
        leftContent.setEditable(false);      // Makes it read-only
        leftContent.setFocusable(false);     // Prevents cursor interaction
        leftContent.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        leftContent.setOpaque(false);        
        leftContent.setBackground(new Color(0,0,0,0)); 
        leftContent.setForeground(new Color(71, 85, 105)); 
        leftContent.setPreferredSize(new Dimension(300, 80)); 

        gbcLeft.insets = new Insets(20, 20, 0, 20); 
        leftPanel.add(leftContent, gbcLeft);
        
        
        //right side
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setOpaque(true);
        rightPanel.setBackground(Color.WHITE);
        //green color line beside right panel
        rightPanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, new Color(4, 140, 90)));
        
        GridBagConstraints gbcRight = new GridBagConstraints();
        gbcRight.gridx = 0;
        gbcRight.fill = GridBagConstraints.HORIZONTAL;
        gbcRight.insets = new Insets(10, 40, 10, 40);
        
        JLabel welcomeLabel = new JLabel("Welcome");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 45));
        gbcRight.gridy = 0;
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
        gbcRight.insets = new Insets(10, 40, 0, 40); 
        rightPanel.add(userLabel, gbcRight);
        
        usernameInput = new JTextField();
        usernameInput.setPreferredSize(new Dimension(300, 45));
        usernameInput.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK),BorderFactory.createEmptyBorder(0, 10, 0, 0)));
        gbcRight.gridy = 3;
        gbcRight.insets = new Insets(10, 40, 10, 40);
        rightPanel.add(usernameInput, gbcRight);
        
        //password
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        gbcRight.gridy = 4;
        gbcRight.insets = new Insets(10, 40, 0, 40); 
        rightPanel.add(passwordLabel, gbcRight);
        
        passwordInput = new JPasswordField();
        passwordInput.setPreferredSize(new Dimension(300, 40));
        passwordInput.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK),BorderFactory.createEmptyBorder(0, 10, 0, 0)));
        gbcRight.gridy = 5;
        gbcRight.insets = new Insets(5, 40, 15, 40);
        rightPanel.add(passwordInput, gbcRight);
            
        loginButton = new JButton("LOGIN");
        loginButton.setBackground(new Color(4, 140, 90)); 
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setPreferredSize(new Dimension(300, 45));
        gbcRight.gridy = 6;
        gbcRight.insets = new Insets(30, 40, 10, 40); 
        loginButton.addActionListener(this);
        rightPanel.add(loginButton, gbcRight);
        
        
        JButton createAccButton = new JButton("Create your account");
        createAccButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        createAccButton.setForeground(new Color(30, 41, 59));
        createAccButton.setContentAreaFilled(false);
        createAccButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbcRight.gridy = 7;
        gbcRight.insets = new Insets(20, 40, 10, 40); 
        rightPanel.add(createAccButton, gbcRight);
        
        this.add(leftPanel);
        this.add(rightPanel);
        
        this.revalidate();
        this.repaint();
        this.setVisible(true);     
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String username = usernameInput.getText().trim();
            String password = new String(passwordInput.getPassword()).trim();
            boolean found = false;

            // Use try-with-resources to automatically close the file
            try (BufferedReader br = new BufferedReader(new FileReader("accounts.txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    // Split the line by commas
                    String[] parts = line.split(",");
                    if (parts.length >= 3) {
                        String fileUsername = parts[0].trim();
                        String filePassword = parts[1].trim();
                        String role = parts[2].trim();

                        // Compare credentials
                        if (username.equals(fileUsername) && password.equals(filePassword)) {
                            found = true;

                            // Redirect based on the specific specialized roles
                            switch (role) {
                                case "Admin":
                                    new adminDashboard();
                                    break;
                                case "Lecturer":
                                    new lecturerDashboard();
                                    break;
                                case "Student":
                                    new studentDashboard();
                                    break;
                                case "AcademicLeader": 
                                    new academicLeaderDashboard();
                                    break;
                                default:
                                    JOptionPane.showMessageDialog(this, "Unknown role assigned!", "Error", JOptionPane.ERROR_MESSAGE);
                                    return;
                            }
                            this.dispose(); // Close login window after successful login
                            return;
                        }
                    }
                }

                if (!found) {
                    JOptionPane.showMessageDialog(this, "Invalid credentials!", "Login Failed", JOptionPane.WARNING_MESSAGE);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "System error: " + ex.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

//   

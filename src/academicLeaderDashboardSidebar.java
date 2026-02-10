import javax.swing.*;
import java.awt.*;


public class academicLeaderDashboardSidebar extends JPanel {
    private JButton dashboardBtn;
    private JButton reportBtn;
    private JButton profileBtn;
    private JButton logoutBtn;
    private JButton logBtn;
    private Runnable toggleAction;
    private JButton announcementsBtn;
    
    public academicLeaderDashboardSidebar(Runnable toggleAction){
        this.toggleAction = toggleAction;
        this.setBackground(new Color(241, 245, 249));
        this.setLayout(new BorderLayout());
        
        sidebarTopSection();
        sidebarContentSection();
        sidebarBottomSection();
    }
    
    private void sidebarTopSection(){
        JPanel sidebarTop = new JPanel(new BorderLayout());
        sidebarTop.setOpaque(false);
        sidebarTop.setPreferredSize(new Dimension(160, 50)); 
        
        JButton sidebarCloseBtn = new JButton("☰");      
        sidebarCloseBtn.setForeground(Color.BLACK);
        sidebarCloseBtn.setBorderPainted(false);
        sidebarCloseBtn.setContentAreaFilled(false);
        sidebarCloseBtn.setFocusPainted(false);
        sidebarCloseBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        sidebarCloseBtn.addActionListener(e -> toggleAction.run()); 
        sidebarTop.add(sidebarCloseBtn, BorderLayout.WEST);
        
        JLabel logoLabel = new JLabel();
        ImageIcon icon = new ImageIcon("APUlogo.png");
        if(icon.getImage() != null) {
            Image img = icon.getImage().getScaledInstance(65, 45, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(img));
        }
        
        logoLabel.setHorizontalAlignment(SwingConstants.LEFT); 
        logoLabel.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 0));
        
        sidebarTop.add(logoLabel, BorderLayout.CENTER);
        this.add(sidebarTop, BorderLayout.NORTH);
    }
    
    private void sidebarContentSection(){
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setOpaque(false);
        navPanel.add(Box.createVerticalStrut(20)); 
        
        dashboardBtn = new JButton("Dashboard");
        reportBtn = new JButton("Report");
        logBtn = new JButton("Audit Log");

        styleSidebarButton(dashboardBtn, navPanel); 
        navPanel.add(Box.createVerticalStrut(10));
        styleSidebarButton(reportBtn, navPanel);
        navPanel.add(Box.createVerticalStrut(10));
        styleSidebarButton(logBtn, navPanel);
        this.add(navPanel, BorderLayout.CENTER);
    }
    
    private void sidebarBottomSection(){
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setOpaque(false);
        
        profileBtn = new JButton("User Profile");
        styleSidebarButton(profileBtn, bottomPanel);
        bottomPanel.add(Box.createVerticalStrut(10));

        logoutBtn = new JButton("Logout");
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoutBtn.setBackground(new Color(220, 53, 69)); 
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setMaximumSize(new Dimension(200, 45));
        logoutBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        bottomPanel.add(logoutBtn);
        bottomPanel.add(Box.createVerticalStrut(20)); // Padding from bottom

        this.add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private void styleSidebarButton(JButton btn, JPanel container) {
        btn.setMaximumSize(new Dimension(200, 45));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(new Color(30, 41, 59));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(btn);
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(80, 95, 115)); // Slightly lighter on hover
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(60, 70, 85)); // Revert to original
            }
        });
    }
    
    public JButton getDashboardBtn() { return dashboardBtn; }
    public JButton getReportBtn() { return reportBtn; }
    public JButton getLogBtn() { return logBtn; }
    public JButton getProfileBtn() { return profileBtn; }
    public JButton getLogoutBtn() { return logoutBtn; }
}
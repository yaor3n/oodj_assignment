import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class lecturerDashboard extends JFrame {

  private JPanel moduleGrid;
  private JPanel sidebar;
  private int sidebarWidth = 200;

  public lecturerDashboard() {
    setTitle("Lecturer Dashboard");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(1100, 700);
    setLocationRelativeTo(null);
    setLayout(new BorderLayout());

    sidebar = new JPanel();
    sidebar.setBackground(Color.DARK_GRAY);
    sidebar.setPreferredSize(new Dimension(0, getHeight()));
    sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

    JButton btnDashboard = new JButton("Dashboard");
    btnDashboard.setAlignmentX(Component.CENTER_ALIGNMENT);
    btnDashboard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

    JButton btnProfile = new JButton("Profile");
    btnProfile.setAlignmentX(Component.CENTER_ALIGNMENT);
    btnProfile.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

    sidebar.add(Box.createVerticalStrut(20));
    sidebar.add(btnDashboard);
    sidebar.add(Box.createVerticalStrut(10));
    sidebar.add(btnProfile);

    add(sidebar, BorderLayout.WEST);

    JPanel header = new JPanel(new BorderLayout());
    header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

    JLabel title = new JLabel("Lecturer Dashboard");
    title.setFont(new Font("Arial", Font.BOLD, 20));

    JButton createModuleBtn = new JButton("Create Module");
    createModuleBtn.setFont(new Font("Arial", Font.BOLD, 14));
    createModuleBtn.addActionListener(e -> openCreateModule());

    JButton toggleSidebar = new JButton("☰");
    toggleSidebar.setFont(new Font("Arial", Font.BOLD, 16));
    toggleSidebar.addActionListener(e -> toggleSidebar());

    JPanel leftHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
    leftHeader.add(toggleSidebar);

    header.add(leftHeader, BorderLayout.WEST);
    header.add(title, BorderLayout.CENTER);
    header.add(createModuleBtn, BorderLayout.EAST);

    add(header, BorderLayout.NORTH);

    moduleGrid = new JPanel();
    moduleGrid.setLayout(new FlowLayout(FlowLayout.LEFT, 30, 30));
    moduleGrid.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    JScrollPane scrollPane = new JScrollPane(moduleGrid);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    add(scrollPane, BorderLayout.CENTER);

    loadModules();

    setVisible(true);
  }

  private void toggleSidebar() {
    if (sidebar.getPreferredSize().width == 0) {
      sidebar.setPreferredSize(new Dimension(sidebarWidth, getHeight()));
    } else {
      sidebar.setPreferredSize(new Dimension(0, getHeight()));
    }
    revalidate();
    repaint();
  }

  private void loadModules() {
    moduleGrid.removeAll();

    File dir = new File("src/Module");
    if (!dir.exists())
      dir.mkdir();

    File[] files = dir.listFiles((d, name) -> name.endsWith(".txt"));
    if (files != null) {
      for (File file : files) {
        String[] data = readModuleData(file);
        if (data != null) {
          String moduleCode = data[0];
          String moduleName = data[1];
          moduleGrid.add(createModuleCard(moduleCode, moduleName));
        }
      }
    }

    moduleGrid.revalidate();
    moduleGrid.repaint();
  }

  private String[] readModuleData(File file) {
    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
      String line;
      while ((line = reader.readLine()) != null) {
        line = line.trim();
        if (line.isEmpty() || line.startsWith("#"))
          continue;
        String[] parts = line.split(",", 6);
        if (parts.length >= 2) {
          return new String[] { parts[0].trim(), parts[1].trim() };
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  private JPanel createModuleCard(String moduleCode, String moduleName) {
    JPanel card = new JPanel(new BorderLayout());
    card.setPreferredSize(new Dimension(220, 180));
    card.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
    card.setBackground(Color.WHITE);

    JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    topPanel.setOpaque(false);

    JButton menuButton = new JButton("⋮");
    menuButton.setFocusPainted(false);
    menuButton.setBorderPainted(true);

    JPopupMenu popup = new JPopupMenu();
    JMenuItem editItem = new JMenuItem("Edit");
    JMenuItem deleteItem = new JMenuItem("Delete");

    editItem.addActionListener(e -> {
      File moduleFile = new File("src/Module/" + moduleCode + ".txt");
      if (moduleFile.exists()) {
        new EditModule(moduleFile).addWindowListener(new java.awt.event.WindowAdapter() {
          @Override
          public void windowClosed(java.awt.event.WindowEvent windowEvent) {
            loadModules();
          }
        });
      } else {
        JOptionPane.showMessageDialog(this, "Module file not found");
      }
    });

    deleteItem.addActionListener(e -> {
      int confirm = JOptionPane.showConfirmDialog(
          this,
          "Delete " + moduleName + "?",
          "Confirm Delete",
          JOptionPane.YES_NO_OPTION);
      if (confirm == JOptionPane.YES_OPTION) {
        deleteModuleFile(moduleCode);
        moduleGrid.remove(card);
        moduleGrid.revalidate();
        moduleGrid.repaint();
      }
    });

    popup.add(editItem);
    popup.add(deleteItem);

    menuButton.addActionListener(e -> popup.show(menuButton, 0, menuButton.getHeight()));
    topPanel.add(menuButton);

    JLabel nameLabel = new JLabel(moduleName, SwingConstants.CENTER);
    nameLabel.setFont(new Font("Arial", Font.BOLD, 16));

    card.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        JOptionPane.showMessageDialog(
            lecturerDashboard.this,
            moduleName + " clicked");
      }
    });

    card.add(topPanel, BorderLayout.NORTH);
    card.add(nameLabel, BorderLayout.CENTER);

    return card;
  }

  private void deleteModuleFile(String moduleCode) {
    File dir = new File("src/Module");
    File[] files = dir.listFiles((d, name) -> name.endsWith(".txt"));
    if (files != null) {
      for (File f : files) {
        try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
          String line;
          while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("#"))
              continue;
            String[] parts = line.split(",", 6);
            if (parts.length >= 2 && parts[0].trim().equals(moduleCode)) {
              f.delete();
              return;
            }
          }
        } catch (IOException ignored) {
        }
      }
    }
  }

  private void openCreateModule() {
    CreateModule createModuleWindow = new CreateModule();
    createModuleWindow.addWindowListener(new java.awt.event.WindowAdapter() {
      @Override
      public void windowClosed(java.awt.event.WindowEvent windowEvent) {
        loadModules();
      }
    });
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(lecturerDashboard::new);
  }
}

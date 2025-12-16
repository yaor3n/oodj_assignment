import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class lecturerDashboard extends JFrame implements ActionListener {

  private JLabel titleLabel;
  private JButton logoutButton;
  private JPanel[] moduleBox;
  private JButton createNewModuleButton;

  lecturerDashboard() {

    titleLabel = new JLabel("Lecturer Dashboard");
    titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
    titleLabel.setBounds(450, 10, 300, 50);
    this.add(titleLabel);

    createNewModuleButton = new JButton("Create Module");
    createNewModuleButton.setFont(new Font("Arial", Font.BOLD, 15));
    createNewModuleButton.addActionListener(this);
    createNewModuleButton.setBounds(800, 20, 150, 55);
    this.add(createNewModuleButton);

    moduleBox = new JPanel[6];
    int startX = 190;
    int startY = 120;
    int width = 200;
    int height = 180;
    int gapX = 250;
    int gapY = 220;

    for (int i = 0; i < 6; i++) {
      moduleBox[i] = new JPanel();
      moduleBox[i].setBounds(
          startX + (i % 3) * gapX,
          startY + (i / 3) * gapY,
          width,
          height);
      moduleBox[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

      JLabel label = new JLabel("Module " + (i + 1), JLabel.CENTER);
      label.setFont(new Font("Arial", Font.BOLD, 18));
      label.setBounds(0, 0, width, 40);
      moduleBox[i].add(label);
      this.add(moduleBox[i]);
    }

    logoutButton = new JButton("Logout");
    logoutButton.setFont(new Font("Arial", Font.BOLD, 15));
    logoutButton.addActionListener(this);
    logoutButton.setBounds(80, 10, 100, 40);
    this.add(logoutButton);

    reusable.windowSetup(this);
    this.setVisible(true);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == logoutButton) {
      new userSelect();
      this.dispose();
    }
  }
}

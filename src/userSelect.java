import javax.swing.*;
import java.awt.*;

public class userSelect extends JFrame{

    private JLabel selectRole;

    userSelect() {
        selectRole = new JLabel("Welcome :D");
        selectRole.setFont(new Font("Arial", Font.BOLD, 25));
        selectRole.setBounds(280,30,800,300);
        selectRole.setForeground(new Color(0x000000));
        this.add(selectRole);

        reusable.windowSetup(this);
    }
}

import javax.swing.*;
import java.awt.*;

public class reusable {
    public static void windowSetup(JFrame frame) {

        frame.setSize(1080, 650);
        frame.setTitle("Better APSpace");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.getContentPane().setBackground(new Color(0xffffff));
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);

        //ImageIcon icon = new ImageIcon("resources/icon1.png");
        //frame.setIconImage(icon.getImage());
    }
}
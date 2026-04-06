package funobus;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            JFrame frame = new JFrame("FunoBus - AP & Telangana Bus Ticketing");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 700);
            frame.setLocationRelativeTo(null);
            
            HomePage homePage = new HomePage(frame);
            frame.add(homePage);
            frame.setVisible(true);
            
            // Test database connection on startup
            DBConnection.getConnection();
        });
    }
}
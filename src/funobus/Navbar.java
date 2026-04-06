package funobus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Navbar extends JPanel {
    private JFrame parentFrame;
    private Color primaryColor = new Color(41, 128, 185);
    private Color darkColor = new Color(52, 73, 94);
    private Color lightColor = new Color(236, 240, 241);
    
    public Navbar(JFrame frame) {
        this.parentFrame = frame;
        setLayout(new BorderLayout());
        setBackground(darkColor);
        setPreferredSize(new Dimension(1200, 70));
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Logo/Brand
        JLabel brandLabel = new JLabel("🚍 FunoBus");
        brandLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        brandLabel.setForeground(Color.WHITE);
        
        // Navigation Buttons Panel
        JPanel navButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        navButtons.setBackground(darkColor);
        
        String[] navItems = {"Home", "Book Tickets", "Contact", "About"};
        Color[] buttonColors = {primaryColor, new Color(46, 204, 113), new Color(155, 89, 182), new Color(230, 126, 34)};
        
        for (int i = 0; i < navItems.length; i++) {
            JButton button = createNavButton(navItems[i], buttonColors[i]);
            final int index = i;
            button.addActionListener(e -> navigateToPage(navItems[index]));
            navButtons.add(button);
        }
        
        add(brandLabel, BorderLayout.WEST);
        add(navButtons, BorderLayout.CENTER);
    }
    
    private JButton createNavButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(120, 40));
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    private void navigateToPage(String pageName) {
        parentFrame.getContentPane().removeAll();
        
        switch (pageName) {
            case "Home":
                parentFrame.add(new HomePage(parentFrame));
                break;
            case "Book Tickets":
                parentFrame.add(new BookTicketsPage(parentFrame));
                break;
            case "Contact":
                parentFrame.add(new ContactPage(parentFrame));
                break;
            case "About":
                parentFrame.add(new AboutPage(parentFrame));
                break;
        }
        
        parentFrame.revalidate();
        parentFrame.repaint();
    }
}
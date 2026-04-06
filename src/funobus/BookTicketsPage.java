package funobus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class BookTicketsPage extends JPanel {
    private JFrame parentFrame;
    private Color primaryColor = new Color(41, 128, 185);
    private JComboBox<String> sourceCombo, destCombo, dateCombo;
    private JButton searchBtn;
    
    // AP and Telangana cities
    private String[] cities = {
        "Select City", "Hyderabad", "Vijayawada", "Visakhapatnam", "Tirupati", 
        "Warangal", "Guntur", "Nellore", "Kurnool", "Rajahmundry", 
        "Kakinada", "Karimnagar", "Nizamabad", "Khammam", "Anantapur", 
        "Kadapa", "Eluru", "Ongole", "Mahbubnagar", "Adilabad"
    };
    
    public BookTicketsPage(JFrame frame) {
        this.parentFrame = frame;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        add(new Navbar(parentFrame), BorderLayout.NORTH);
        
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(Color.WHITE);
        mainContent.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        
        // Header
        JLabel headerLabel = new JLabel("Book Your Bus Tickets");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        headerLabel.setForeground(new Color(44, 62, 80));
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        
        // Search Panel
        JPanel searchPanel = createSearchPanel();
        
        mainContent.add(headerLabel, BorderLayout.NORTH);
        mainContent.add(searchPanel, BorderLayout.CENTER);
        
        add(mainContent, BorderLayout.CENTER);
    }
    
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Source
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel sourceLabel = new JLabel("From:");
        sourceLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panel.add(sourceLabel, gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.gridwidth = 2;
        sourceCombo = new JComboBox<>(cities);
        sourceCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        sourceCombo.setPreferredSize(new Dimension(200, 40));
        panel.add(sourceCombo, gbc);
        
        // Destination
        gbc.gridx = 3; gbc.gridy = 0;
        gbc.gridwidth = 1;
        JLabel destLabel = new JLabel("To:");
        destLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panel.add(destLabel, gbc);
        
        gbc.gridx = 4; gbc.gridy = 0;
        gbc.gridwidth = 2;
        destCombo = new JComboBox<>(cities);
        destCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        destCombo.setPreferredSize(new Dimension(200, 40));
        panel.add(destCombo, gbc);
        
        // Date
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 1;
        JLabel dateLabel = new JLabel("Journey Date:");
        dateLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panel.add(dateLabel, gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.gridwidth = 2;
        String[] dates = {"Today", "Tomorrow", getNextDate(2), getNextDate(3), getNextDate(4), getNextDate(5)};
        dateCombo = new JComboBox<>(dates);
        dateCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateCombo.setPreferredSize(new Dimension(200, 40));
        panel.add(dateCombo, gbc);
        
        // Search Button
        gbc.gridx = 3; gbc.gridy = 1;
        gbc.gridwidth = 3;
        searchBtn = new JButton("🔍 SEARCH BUSES");
        searchBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        searchBtn.setBackground(primaryColor);
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setBorderPainted(false);
        searchBtn.setFocusPainted(false);
        searchBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchBtn.setPreferredSize(new Dimension(200, 45));
        searchBtn.addActionListener(e -> searchBuses());
        panel.add(searchBtn, gbc);
        
        return panel;
    }
    
    private String getNextDate(int days) {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.add(java.util.Calendar.DAY_OF_MONTH, days);
        return new java.text.SimpleDateFormat("dd/MM/yyyy").format(cal.getTime());
    }
    
    private void searchBuses() {
        String source = (String) sourceCombo.getSelectedItem();
        String destination = (String) destCombo.getSelectedItem();
        
        if (source.equals("Select City") || destination.equals("Select City") || source.equals(destination)) {
            JOptionPane.showMessageDialog(this,
                "Please select valid source and destination!",
                "Invalid Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Navigate to Seat Selection Page with route details
        parentFrame.getContentPane().removeAll();
        parentFrame.add(new SeatSelectionPage(parentFrame, source, destination, 
                        (String) dateCombo.getSelectedItem()));
        parentFrame.revalidate();
        parentFrame.repaint();
    }
}
package funobus;

import javax.swing.*;
import java.awt.*;

public class AboutPage extends JPanel {
    private JFrame parentFrame;
    private Color primaryColor = new Color(41, 128, 185);
    
    public AboutPage(JFrame frame) {
        this.parentFrame = frame;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        add(new Navbar(parentFrame), BorderLayout.NORTH);
        
        JPanel mainContent = new JPanel(new GridBagLayout());
        mainContent.setBackground(Color.WHITE);
        mainContent.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // About Header
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel titleLabel = new JLabel("About FunoBus");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        titleLabel.setForeground(new Color(44, 62, 80));
        mainContent.add(titleLabel, gbc);
        
        // Company Description
        gbc.gridy = 1;
        JTextArea descriptionArea = new JTextArea(
            "FunoBus is the premier bus ticketing platform serving Andhra Pradesh and Telangana. " +
            "Since 2020, we've been connecting cities and towns across both Telugu states with " +
            "comfortable, reliable, and affordable bus travel.\n\n" +
            "Our mission is to make bus travel as convenient as possible for millions of passengers " +
            "who travel daily between the beautiful cities of AP and Telangana. From the bustling " +
            "streets of Hyderabad to the serene beaches of Visakhapatnam, from the spiritual " +
            "corridors of Tirupati to the ancient heritage of Warangal - we're here to take you there."
        );
        descriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false);
        descriptionArea.setBackground(Color.WHITE);
        descriptionArea.setForeground(new Color(52, 73, 94));
        descriptionArea.setRows(8);
        descriptionArea.setColumns(40);
        mainContent.add(descriptionArea, gbc);
        
        // Stats Cards
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        
        // Routes Count
        JPanel routesCard = createStatCard("🚍", "50+", "Routes Across AP & TS");
        mainContent.add(routesCard, gbc);
        
        gbc.gridx = 1;
        JPanel citiesCard = createStatCard("🏙️", "20+", "Cities Connected");
        mainContent.add(citiesCard, gbc);
        
        gbc.gridy = 3; gbc.gridx = 0;
        JPanel passengersCard = createStatCard("👥", "1M+", "Happy Passengers");
        mainContent.add(passengersCard, gbc);
        
        gbc.gridx = 1;
        JPanel busesCard = createStatCard("⭐", "4.5", "Rating (5.0)");
        mainContent.add(busesCard, gbc);
        
        // Features Section
        gbc.gridy = 4; gbc.gridx = 0;
        gbc.gridwidth = 2;
        JPanel featuresPanel = createFeaturesPanel();
        mainContent.add(featuresPanel, gbc);
        
        add(mainContent, BorderLayout.CENTER);
    }
    
    private JPanel createStatCard(String icon, String value, String label) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(236, 240, 241));
        card.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        card.setPreferredSize(new Dimension(250, 150));
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 40));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        valueLabel.setForeground(primaryColor);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel labelLabel = new JLabel(label);
        labelLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        labelLabel.setForeground(Color.GRAY);
        labelLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        card.add(iconLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(valueLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(labelLabel);
        
        return card;
    }
    
    private JPanel createFeaturesPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 3, 20, 20));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
        
        String[][] features = {
            {"🎫", "Easy Booking", "Book tickets in seconds"},
            {"💺", "Seat Selection", "Choose your preferred seat"},
            {"💰", "Best Prices", "Guaranteed lowest fares"},
            {"🛡️", "Safe Travel", "COVID-19 safety protocols"},
            {"🔄", "Free Cancellation", "Cancel up to 1 hour before"},
            {"📱", "Mobile App", "Book on the go"}
        };
        
        for (String[] feature : features) {
            panel.add(createFeatureCard(feature[0], feature[1], feature[2]));
        }
        
        return panel;
    }
    
    private JPanel createFeatureCard(String icon, String title, String description) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 32));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(44, 62, 80));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLabel.setForeground(Color.GRAY);
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        card.add(iconLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(descLabel);
        
        return card;
    }
}
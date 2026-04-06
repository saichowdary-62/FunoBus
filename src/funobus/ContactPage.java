package funobus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ContactPage extends JPanel {
    private JFrame parentFrame;
    private Color primaryColor = new Color(41, 128, 185);
    private Color successColor = new Color(46, 204, 113);
    
    public ContactPage(JFrame frame) {
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
        
        // Title
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel titleLabel = new JLabel("Contact Us");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setForeground(new Color(44, 62, 80));
        mainContent.add(titleLabel, gbc);
        
        // Contact Cards
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        
        // Customer Support Card
        JPanel supportCard = createContactCard(
            "📞 Customer Support",
            "+91 98765 43210",
            "support@funobus.com",
            "24/7 Available",
            primaryColor
        );
        mainContent.add(supportCard, gbc);
        
        // Regional Offices
        gbc.gridx = 1;
        JPanel regionalCard = createContactCard(
            "🏢 Regional Office",
            "+91 98765 43211",
            "apoffice@funobus.com",
            "AP & Telangana",
            new Color(155, 89, 182)
        );
        mainContent.add(regionalCard, gbc);
        
        // Second Row
        gbc.gridy = 2; gbc.gridx = 0;
        JPanel hyderabadCard = createContactCard(
            "📍 Hyderabad Hub",
            "+91 98765 43212",
            "hyd@funobus.com",
            "Jubilee Hills, Hyderabad",
            new Color(230, 126, 34)
        );
        mainContent.add(hyderabadCard, gbc);
        
        gbc.gridx = 1;
        JPanel vizagCard = createContactCard(
            "📍 Vizag Hub",
            "+91 98765 43213",
            "vizag@funobus.com",
            "Dwaraka Nagar, Vizag",
            new Color(46, 204, 113)
        );
        mainContent.add(vizagCard, gbc);
        
        // Contact Form
        gbc.gridy = 3; gbc.gridx = 0;
        gbc.gridwidth = 2;
        JPanel formPanel = createContactForm();
        mainContent.add(formPanel, gbc);
        
        add(mainContent, BorderLayout.CENTER);
    }
    
    private JPanel createContactCard(String title, String phone, String email, String address, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        card.setPreferredSize(new Dimension(350, 180));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(color);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel phoneLabel = new JLabel(phone);
        phoneLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        phoneLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel emailLabel = new JLabel(email);
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        emailLabel.setForeground(primaryColor);
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel addressLabel = new JLabel(address);
        addressLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        addressLabel.setForeground(Color.GRAY);
        addressLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(15));
        card.add(phoneLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(emailLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(addressLabel);
        
        return card;
    }
    
    private JPanel createContactForm() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Send us a message"),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Name
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        JTextField nameField = new JTextField(20);
        nameField.setPreferredSize(new Dimension(300, 35));
        panel.add(nameField, gbc);
        
        // Email
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        JTextField emailField = new JTextField(20);
        emailField.setPreferredSize(new Dimension(300, 35));
        panel.add(emailField, gbc);
        
        // Message
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Message:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        JTextArea messageArea = new JTextArea(5, 20);
        messageArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        scrollPane.setPreferredSize(new Dimension(300, 100));
        panel.add(scrollPane, gbc);
        
        // Send Button
        gbc.gridx = 1; gbc.gridy = 3;
        gbc.gridwidth = 1;
        JButton sendBtn = new JButton("Send Message");
        sendBtn.setBackground(successColor);
        sendBtn.setForeground(Color.WHITE);
        sendBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        sendBtn.setBorderPainted(false);
        sendBtn.setFocusPainted(false);
        sendBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        sendBtn.setPreferredSize(new Dimension(150, 40));
        
        sendBtn.addActionListener(e -> {
            if (!nameField.getText().isEmpty() && !emailField.getText().isEmpty() && !messageArea.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Thank you for contacting us!\nWe'll get back to you soon.",
                    "Message Sent",
                    JOptionPane.INFORMATION_MESSAGE);
                nameField.setText("");
                emailField.setText("");
                messageArea.setText("");
            } else {
                JOptionPane.showMessageDialog(this,
                    "Please fill all fields!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        panel.add(sendBtn, gbc);
        
        return panel;
    }
}
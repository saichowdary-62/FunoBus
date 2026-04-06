package funobus;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MyBookingsPage extends JPanel {
    private JFrame parentFrame;
    private Color primaryColor = new Color(25, 118, 210);
    private Color successColor = new Color(56, 142, 60);
    
    public MyBookingsPage(JFrame frame) {
        this.parentFrame = frame;
        setLayout(new BorderLayout());
        setBackground(new Color(250, 250, 250));
        
        add(createNavbar(), BorderLayout.NORTH);
        
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(new Color(250, 250, 250));
        mainContent.setBorder(new EmptyBorder(30, 50, 30, 50));
        
        JPanel headerPanel = createHeader();
        mainContent.add(headerPanel, BorderLayout.NORTH);
        
        JPanel bookingsPanel = createBookingsList();
        JScrollPane scrollPane = new JScrollPane(bookingsPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainContent.add(scrollPane, BorderLayout.CENTER);
        
        add(mainContent, BorderLayout.CENTER);
    }
    
    private JPanel createNavbar() {
        JPanel navbar = new JPanel(new BorderLayout());
        navbar.setBackground(Color.WHITE);
        navbar.setBorder(BorderFactory.createEmptyBorder(15, 50, 15, 50));
        
        JLabel backButton = new JLabel("← Back to Home");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        backButton.setForeground(primaryColor);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                parentFrame.getContentPane().removeAll();
                parentFrame.add(new HomePage(parentFrame));
                parentFrame.revalidate();
                parentFrame.repaint();
            }
        });
        
        JLabel title = new JLabel("My Bookings", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(33, 33, 33));
        
        navbar.add(backButton, BorderLayout.WEST);
        navbar.add(title, BorderLayout.CENTER);
        
        return navbar;
    }
    
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JPanel userInfo = new JPanel();
        userInfo.setLayout(new BoxLayout(userInfo, BoxLayout.Y_AXIS));
        userInfo.setBackground(Color.WHITE);
        
        String userName = HomePage.getCurrentUser() != null ? HomePage.getCurrentUser() : "Guest";
        
        JLabel welcomeLabel = new JLabel("Welcome back, " + userName + "!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        
        userInfo.add(welcomeLabel);
        
        header.add(userInfo, BorderLayout.WEST);
        
        return header;
    }
    
    private JPanel createBookingsList() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(250, 250, 250));
        panel.setBorder(new EmptyBorder(20, 0, 20, 0));
        
        int userId = HomePage.getCurrentUserId();
        ResultSet rs = BookingDAO.getUserBookings(userId);
        
        try {
            if (rs == null || !rs.next()) {
                panel.add(createEmptyBookingsPanel());
            } else {
                do {
                    panel.add(createBookingCard(rs));
                    panel.add(Box.createVerticalStrut(15));
                } while (rs.next());
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            panel.add(createErrorPanel());
        }
        
        return panel;
    }
    
    private JPanel createBookingCard(ResultSet rs) throws SQLException {
        JPanel card = new JPanel(new BorderLayout(20, 0));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            new EmptyBorder(20, 25, 20, 25)
        ));
        
        String source = rs.getString("source_city");
        String destination = rs.getString("destination_city");
        String operator = rs.getString("operator_name");
        String busNumber = rs.getString("bus_number");
        String date = rs.getDate("journey_date").toString();
        String time = rs.getTime("departure_time").toString().substring(0, 5);
        String seats = rs.getString("seat_numbers");
        double fare = rs.getDouble("total_fare");
        String bookingRef = rs.getString("booking_reference");
        
        // Left Panel
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Color.WHITE);
        
        JLabel routeLabel = new JLabel(source + " → " + destination);
        routeLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        
        JLabel refLabel = new JLabel("Booking: " + bookingRef);
        refLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        refLabel.setForeground(Color.GRAY);
        refLabel.setBorder(new EmptyBorder(5, 0, 10, 0));
        
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.add(createIconText("📅", date));
        infoPanel.add(createIconText("🕐", time));
        infoPanel.add(createIconText("💺", seats));
        infoPanel.add(createIconText("🚍", operator + " (" + busNumber + ")"));
        
        leftPanel.add(routeLabel);
        leftPanel.add(refLabel);
        leftPanel.add(infoPanel);
        
        // Right Panel
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setPreferredSize(new Dimension(150, 80));
        
        JLabel fareLabel = new JLabel("₹" + (int) fare);
        fareLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        fareLabel.setForeground(successColor);
        fareLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        JLabel statusLabel = new JLabel("✅ Confirmed");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        statusLabel.setForeground(successColor);
        statusLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        rightPanel.add(Box.createVerticalGlue());
        rightPanel.add(fareLabel);
        rightPanel.add(Box.createVerticalStrut(5));
        rightPanel.add(statusLabel);
        rightPanel.add(Box.createVerticalGlue());
        
        card.add(leftPanel, BorderLayout.WEST);
        card.add(rightPanel, BorderLayout.EAST);
        
        return card;
    }
    
    private JPanel createIconText(String icon, String text) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panel.setBackground(Color.WHITE);
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textLabel.setForeground(Color.GRAY);
        
        panel.add(iconLabel);
        panel.add(textLabel);
        
        return panel;
    }
    
    private JPanel createEmptyBookingsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(80, 80, 80, 80));
        
        JLabel iconLabel = new JLabel("📅");
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 64));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel textLabel = new JLabel("No bookings found");
        textLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        textLabel.setForeground(Color.GRAY);
        textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        textLabel.setBorder(new EmptyBorder(20, 0, 10, 0));
        
        JLabel subTextLabel = new JLabel("Book your first trip now!");
        subTextLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subTextLabel.setForeground(Color.GRAY);
        subTextLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(iconLabel);
        panel.add(textLabel);
        panel.add(subTextLabel);
        
        return panel;
    }
    
    private JPanel createErrorPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(80, 80, 80, 80));
        
        JLabel errorLabel = new JLabel("⚠️ Unable to load bookings");
        errorLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        errorLabel.setForeground(Color.RED);
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(errorLabel);
        
        return panel;
    }
}
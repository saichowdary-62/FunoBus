package funobus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class SeatSelectionPage extends JPanel {
    private JFrame parentFrame;
    private String source, destination, date;
    private Color primaryColor = new Color(25, 118, 210);
    
    public SeatSelectionPage(JFrame frame, String source, String destination, String date) {
        this.parentFrame = frame;
        this.source = source;
        this.destination = destination;
        this.date = date;
        
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        add(createNavbar(), BorderLayout.NORTH);
        
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(Color.WHITE);
        mainContent.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        
        JPanel headerPanel = createHeaderPanel();
        mainContent.add(headerPanel, BorderLayout.NORTH);
        
        JPanel busesPanel = createBusesPanel();
        JScrollPane scrollPane = new JScrollPane(busesPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainContent.add(scrollPane, BorderLayout.CENTER);
        
        add(mainContent, BorderLayout.CENTER);
    }
    
    private JPanel createNavbar() {
        JPanel navbar = new JPanel(new BorderLayout());
        navbar.setBackground(Color.WHITE);
        navbar.setBorder(BorderFactory.createEmptyBorder(15, 50, 15, 50));
        navbar.setPreferredSize(new Dimension(1200, 70));
        
        JLabel backButton = new JLabel("← Back to Home");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        backButton.setForeground(primaryColor);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                parentFrame.getContentPane().removeAll();
                parentFrame.add(new HomePage(parentFrame));
                parentFrame.revalidate();
                parentFrame.repaint();
            }
        });
        
        JLabel title = new JLabel("Select Your Bus", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(primaryColor);
        
        navbar.add(backButton, BorderLayout.WEST);
        navbar.add(title, BorderLayout.CENTER);
        
        return navbar;
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 30, 20));
        
        JLabel routeLabel = new JLabel(source + " → " + destination);
        routeLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        routeLabel.setForeground(new Color(33, 33, 33));
        routeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel dateLabel = new JLabel("Journey Date: " + date);
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        dateLabel.setForeground(Color.GRAY);
        dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        dateLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        panel.add(routeLabel);
        panel.add(dateLabel);
        
        return panel;
    }
    
    private JPanel createBusesPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        List<BusSchedule> schedules = BusDAO.getAvailableBuses(source, destination, date);
        
        if (schedules.isEmpty()) {
            JLabel noBusLabel = new JLabel("No buses available for this route on " + date);
            noBusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            noBusLabel.setForeground(Color.RED);
            noBusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(noBusLabel);
        } else {
            for (BusSchedule schedule : schedules) {
                BusCard busCard = new BusCard(
                    schedule.getBusNumber(),
                    schedule.getOperatorName(),
                    schedule.getDepartureTime(),
                    schedule.getArrivalTime(),
                    "₹" + (int) schedule.getFare(),
                    40,
                    schedule.getAvailableSeats(),
                    schedule.getScheduleId(),
                    parentFrame
                );
                panel.add(busCard);
                panel.add(Box.createVerticalStrut(15));
            }
        }
        
        return panel;
    }
}
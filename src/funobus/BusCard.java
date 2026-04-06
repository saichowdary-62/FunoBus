package funobus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class BusCard extends JPanel {
    private String busNumber, operatorName, departureTime, arrivalTime, fare;
    private int totalSeats, availableSeats, scheduleId;
    private JFrame parentFrame;
    private Color primaryColor = new Color(25, 118, 210);
    private Color successColor = new Color(56, 142, 60);
    private ArrayList<Integer> selectedSeats = new ArrayList<>();
    
    public BusCard(String busNumber, String operatorName, String departureTime, 
                   String arrivalTime, String fare, int totalSeats, int availableSeats,
                   int scheduleId, JFrame parentFrame) {
        this.busNumber = busNumber;
        this.operatorName = operatorName;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.fare = fare;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
        this.scheduleId = scheduleId;
        this.parentFrame = parentFrame;
        
        initUI();
    }
    
    private void initUI() {
        setLayout(new BorderLayout(15, 0));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        setPreferredSize(new Dimension(1000, 130));
        
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setPreferredSize(new Dimension(250, 100));
        
        JLabel operatorLabel = new JLabel(operatorName);
        operatorLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        
        JLabel busLabel = new JLabel(busNumber + " • AC Sleeper");
        busLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        busLabel.setForeground(Color.GRAY);
        
        JLabel seatsLabel = new JLabel(availableSeats + " seats available");
        seatsLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        seatsLabel.setForeground(availableSeats > 10 ? successColor : new Color(245, 124, 0));
        
        leftPanel.add(operatorLabel);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(busLabel);
        leftPanel.add(Box.createVerticalStrut(8));
        leftPanel.add(seatsLabel);
        
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 10));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setPreferredSize(new Dimension(400, 100));
        
        JLabel depTime = new JLabel(departureTime);
        depTime.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        JLabel arrow = new JLabel("→");
        arrow.setFont(new Font("Segoe UI", Font.BOLD, 28));
        arrow.setForeground(primaryColor);
        
        JLabel arrTime = new JLabel(arrivalTime);
        arrTime.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        centerPanel.add(depTime);
        centerPanel.add(arrow);
        centerPanel.add(arrTime);
        
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setPreferredSize(new Dimension(200, 100));
        
        int fareValue = Integer.parseInt(fare.replace("₹", "").trim());
        
        JLabel fareLabel = new JLabel("₹" + fareValue);
        fareLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        fareLabel.setForeground(successColor);
        fareLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        JButton bookBtn = new JButton("SELECT SEATS");
        bookBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        bookBtn.setBackground(primaryColor);
        bookBtn.setForeground(Color.WHITE);
        bookBtn.setBorderPainted(false);
        bookBtn.setFocusPainted(false);
        bookBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bookBtn.setPreferredSize(new Dimension(150, 38));
        bookBtn.addActionListener(e -> {
            if (!HomePage.isLoggedIn()) {
                JOptionPane.showMessageDialog(parentFrame, "Please login to book tickets!");
                return;
            }
            showSeatSelectionDialog(fareValue);
        });
        
        rightPanel.add(Box.createVerticalGlue());
        rightPanel.add(fareLabel);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(bookBtn);
        rightPanel.add(Box.createVerticalGlue());
        
        add(leftPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
    }
    
    private void showSeatSelectionDialog(int baseFare) {
        JDialog dialog = new JDialog(parentFrame, "Select Seats", true);
        dialog.setSize(900, 600);
        dialog.setLocationRelativeTo(parentFrame);
        dialog.setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel seatPanel = new JPanel(new BorderLayout());
        seatPanel.setBackground(Color.WHITE);
        
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        legendPanel.setBackground(Color.WHITE);
        legendPanel.add(createLegend("🟩", "Available"));
        legendPanel.add(createLegend("🟨", "Selected"));
        legendPanel.add(createLegend("🟥", "Booked"));
        
        JPanel seatGrid = new JPanel(new GridLayout(8, 5, 10, 10));
        seatGrid.setBackground(Color.WHITE);
        seatGrid.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        selectedSeats.clear();
        
        for (int i = 1; i <= 40; i++) {
            JCheckBox seat = new JCheckBox(String.valueOf(i));
            seat.setFont(new Font("Segoe UI", Font.BOLD, 14));
            seat.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            if (i % 7 == 0 || i % 13 == 0) {
                seat.setEnabled(false);
                seat.setBackground(new Color(255, 200, 200));
            }
            
            final int seatNumber = i;
            seat.addActionListener(e -> {
                if (seat.isSelected()) selectedSeats.add(seatNumber);
                else selectedSeats.remove(Integer.valueOf(seatNumber));
            });
            
            seatGrid.add(seat);
        }
        
        seatPanel.add(legendPanel, BorderLayout.NORTH);
        seatPanel.add(seatGrid, BorderLayout.CENTER);
        
        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.Y_AXIS));
        summaryPanel.setBackground(Color.WHITE);
        summaryPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        summaryPanel.setPreferredSize(new Dimension(280, 400));
        
        int tax = baseFare * 5 / 100;
        int total = baseFare + tax;
        
        JLabel titleLabel = new JLabel("Booking Summary");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel busNameLabel = new JLabel(operatorName);
        busNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        busNameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        busNameLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 5, 0));
        
        JLabel selectedSeatsLabel = new JLabel("Selected Seats: None");
        selectedSeatsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        selectedSeatsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        selectedSeatsLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        JLabel baseLabel = new JLabel("Base Fare: ₹" + baseFare);
        baseLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        baseLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel taxLabel = new JLabel("GST (5%): ₹" + tax);
        taxLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        taxLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel totalLabel = new JLabel("Total: ₹" + total);
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        totalLabel.setForeground(successColor);
        totalLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        totalLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        
        JButton confirmBtn = new JButton("CONFIRM BOOKING");
        confirmBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        confirmBtn.setBackground(successColor);
        confirmBtn.setForeground(Color.WHITE);
        confirmBtn.setBorderPainted(false);
        confirmBtn.setFocusPainted(false);
        confirmBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        confirmBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        confirmBtn.setPreferredSize(new Dimension(240, 45));
        
        confirmBtn.addActionListener(e -> {
            if (selectedSeats.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please select at least one seat!");
                return;
            }
            
            StringBuilder seatsStr = new StringBuilder();
            for (int i = 0; i < selectedSeats.size(); i++) {
                if (i > 0) seatsStr.append(", ");
                seatsStr.append(selectedSeats.get(i));
            }
            
            int grandTotal = total * selectedSeats.size();
            
            int userId = HomePage.getCurrentUserId();
            boolean saved = BookingDAO.saveBooking(userId, scheduleId, seatsStr.toString(), grandTotal);
            
            if (saved) {
                String message = String.format(
                    "✅ BOOKING CONFIRMED!\n\n" +
                    "━━━━━━━━━━━━━━━━━━━━━━\n" +
                    "Booking Reference: FB%d\n" +
                    "━━━━━━━━━━━━━━━━━━━━━━\n\n" +
                    "Operator: %s\n" +
                    "Bus: %s\n" +
                    "Journey: %s → %s\n" +
                    "Seats: %s\n" +
                    "Total Fare: ₹%d\n" +
                    "━━━━━━━━━━━━━━━━━━━━━━\n\n" +
                    "💵 Please pay ₹%d in CASH to the bus conductor.\n\n" +
                    "Thank you for choosing FunoBus! 🚍",
                    System.currentTimeMillis() % 10000,
                    operatorName, 
                    busNumber, 
                    departureTime, 
                    arrivalTime,
                    seatsStr.toString(), 
                    grandTotal,
                    grandTotal
                );
                
                JOptionPane.showMessageDialog(dialog, message, "Booking Successful", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "❌ Booking failed! Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        summaryPanel.add(titleLabel);
        summaryPanel.add(busNameLabel);
        summaryPanel.add(selectedSeatsLabel);
        summaryPanel.add(Box.createVerticalStrut(20));
        summaryPanel.add(baseLabel);
        summaryPanel.add(taxLabel);
        summaryPanel.add(totalLabel);
        summaryPanel.add(Box.createVerticalStrut(20));
        summaryPanel.add(confirmBtn);
        
        mainPanel.add(seatPanel, BorderLayout.CENTER);
        mainPanel.add(summaryPanel, BorderLayout.EAST);
        
        dialog.add(mainPanel);
        dialog.setVisible(true);
    }
    
    private JPanel createLegend(String icon, String text) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panel.setBackground(Color.WHITE);
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        
        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        textLabel.setForeground(Color.GRAY);
        
        panel.add(iconLabel);
        panel.add(textLabel);
        return panel;
    }
}
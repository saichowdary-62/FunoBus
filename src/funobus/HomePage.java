package funobus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class HomePage extends JPanel {
    private JFrame parentFrame;
    private Color primaryColor = new Color(25, 118, 210);
    private Color secondaryColor = new Color(56, 142, 60);
    private static boolean isLoggedIn = false;
    private static String currentUser = null;
    private static int currentUserId = -1;
    
    public HomePage(JFrame frame) {
        this.parentFrame = frame;
        setLayout(new BorderLayout());
        setBackground(new Color(250, 250, 250));
        
        add(createNavbar(), BorderLayout.NORTH);
        
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(new Color(250, 250, 250));
        mainContent.setBorder(BorderFactory.createEmptyBorder(0, 50, 50, 50));
        
        mainContent.add(createHeroSection());
        mainContent.add(Box.createVerticalStrut(30));
        mainContent.add(createQuickBookingSection());
        mainContent.add(Box.createVerticalStrut(40));
        mainContent.add(createPopularRoutesSection());
        mainContent.add(Box.createVerticalStrut(40));
        mainContent.add(createWhyChooseUs());
        
        add(new JScrollPane(mainContent), BorderLayout.CENTER);
    }
    
    private JPanel createNavbar() {
        JPanel navbar = new JPanel(new BorderLayout());
        navbar.setBackground(Color.WHITE);
        navbar.setBorder(BorderFactory.createEmptyBorder(15, 50, 15, 50));
        
        JPanel brandPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        brandPanel.setBackground(Color.WHITE);
        
        JLabel busIcon = new JLabel("🚍");
        busIcon.setFont(new Font("Segoe UI", Font.PLAIN, 32));
        
        JLabel brandName = new JLabel("FunoBus");
        brandName.setFont(new Font("Segoe UI", Font.BOLD, 24));
        brandName.setForeground(primaryColor);
        
        brandPanel.add(busIcon);
        brandPanel.add(brandName);
        
        if (isLoggedIn) {
            JLabel welcomeLabel = new JLabel("Welcome, " + currentUser + "!");
            welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            welcomeLabel.setForeground(secondaryColor);
            welcomeLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
            brandPanel.add(welcomeLabel);
        }
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setBackground(Color.WHITE);
        
        if (!isLoggedIn) {
            JButton loginBtn = new JButton("Login");
            loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
            loginBtn.setForeground(primaryColor);
            loginBtn.setBackground(Color.WHITE);
            loginBtn.setBorder(BorderFactory.createLineBorder(primaryColor, 1));
            loginBtn.setFocusPainted(false);
            loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            loginBtn.setPreferredSize(new Dimension(100, 40));
            loginBtn.addActionListener(e -> showLoginDialog());
            
            JButton signupBtn = new JButton("Sign Up");
            signupBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
            signupBtn.setBackground(secondaryColor);
            signupBtn.setForeground(Color.WHITE);
            signupBtn.setBorderPainted(false);
            signupBtn.setFocusPainted(false);
            signupBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            signupBtn.setPreferredSize(new Dimension(100, 40));
            signupBtn.addActionListener(e -> showSignupDialog());
            
            rightPanel.add(loginBtn);
            rightPanel.add(signupBtn);
        } else {
            JButton bookingsBtn = new JButton("📋 My Bookings");
            bookingsBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
            bookingsBtn.setForeground(primaryColor);
            bookingsBtn.setBackground(Color.WHITE);
            bookingsBtn.setBorder(BorderFactory.createLineBorder(primaryColor, 1));
            bookingsBtn.setFocusPainted(false);
            bookingsBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            bookingsBtn.setPreferredSize(new Dimension(140, 40));
            bookingsBtn.addActionListener(e -> {
                parentFrame.getContentPane().removeAll();
                parentFrame.add(new MyBookingsPage(parentFrame));
                parentFrame.revalidate();
                parentFrame.repaint();
            });
            
            JButton logoutBtn = new JButton("Logout");
            logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
            logoutBtn.setBackground(Color.RED);
            logoutBtn.setForeground(Color.WHITE);
            logoutBtn.setBorderPainted(false);
            logoutBtn.setFocusPainted(false);
            logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            logoutBtn.setPreferredSize(new Dimension(100, 40));
            logoutBtn.addActionListener(e -> {
                isLoggedIn = false;
                currentUser = null;
                currentUserId = -1;
                refreshPage();
            });
            
            rightPanel.add(bookingsBtn);
            rightPanel.add(logoutBtn);
        }
        
        navbar.add(brandPanel, BorderLayout.WEST);
        navbar.add(rightPanel, BorderLayout.EAST);
        
        return navbar;
    }
    
    private JPanel createHeroSection() {
        JPanel hero = new JPanel(new GridBagLayout());
        hero.setBackground(primaryColor);
        hero.setPreferredSize(new Dimension(1200, 250));
        hero.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JLabel welcomeLabel = new JLabel("Travel Across AP & Telangana");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 42));
        welcomeLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel("Safe • Comfortable • Affordable");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        subtitleLabel.setForeground(new Color(255, 255, 255, 230));
        
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 0));
        statsPanel.setBackground(primaryColor);
        
        String[][] stats = {{"50+", "Routes"}, {"20+", "Cities"}, {"1M+", "Happy Riders"}, {"4.5★", "Rating"}};
        for (String[] stat : stats) statsPanel.add(createStatLabel(stat[0], stat[1]));
        
        hero.add(welcomeLabel, gbc);
        hero.add(Box.createVerticalStrut(15), gbc);
        hero.add(subtitleLabel, gbc);
        hero.add(Box.createVerticalStrut(25), gbc);
        hero.add(statsPanel, gbc);
        
        return hero;
    }
    
    private JPanel createStatLabel(String value, String label) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(primaryColor);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel descLabel = new JLabel(label);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descLabel.setForeground(new Color(255, 255, 255, 200));
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(valueLabel);
        panel.add(descLabel);
        return panel;
    }
    
    private JPanel createQuickBookingSection() {
        JPanel section = new JPanel(new BorderLayout());
        section.setBackground(Color.WHITE);
        section.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(Color.WHITE);
        
        JLabel title = new JLabel("Quick Bus Booking");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(33, 33, 33));
        
        JLabel subtitle = new JLabel("Book your journey in seconds");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(Color.GRAY);
        
        titlePanel.add(title);
        titlePanel.add(subtitle);
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        String[] cities = getAPTSCities();
        
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("FROM"), gbc);
        gbc.gridx = 0; gbc.gridy = 1;
        JComboBox<String> fromCombo = new JComboBox<>(cities);
        fromCombo.setPreferredSize(new Dimension(250, 45));
        formPanel.add(fromCombo, gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        formPanel.add(new JLabel("TO"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        JComboBox<String> toCombo = new JComboBox<>(cities);
        toCombo.setPreferredSize(new Dimension(250, 45));
        formPanel.add(toCombo, gbc);
        
        gbc.gridx = 2; gbc.gridy = 0;
        formPanel.add(new JLabel("DATE"), gbc);
        gbc.gridx = 2; gbc.gridy = 1;
        JComboBox<String> dateCombo = new JComboBox<>();
        dateCombo.addItem("Today");
        dateCombo.addItem("Tomorrow");
        for (int i = 2; i <= 7; i++) dateCombo.addItem(getDate(i));
        dateCombo.setPreferredSize(new Dimension(200, 45));
        formPanel.add(dateCombo, gbc);
        
        gbc.gridx = 3; gbc.gridy = 1;
        JButton searchBtn = new JButton("SEARCH BUSES");
        searchBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchBtn.setBackground(secondaryColor);
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setBorderPainted(false);
        searchBtn.setFocusPainted(false);
        searchBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchBtn.setPreferredSize(new Dimension(180, 45));
        
        searchBtn.addActionListener(e -> {
            if (!isLoggedIn) {
                int option = JOptionPane.showConfirmDialog(this,
                    "Please login to book tickets. Would you like to login now?",
                    "Login Required",
                    JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) showLoginDialog();
                return;
            }
            
            String from = (String) fromCombo.getSelectedItem();
            String to = (String) toCombo.getSelectedItem();
            String date = (String) dateCombo.getSelectedItem();
            
            if (!from.equals(to) && !from.equals("Select City") && !to.equals("Select City")) {
                parentFrame.getContentPane().removeAll();
                parentFrame.add(new SeatSelectionPage(parentFrame, from, to, date));
                parentFrame.revalidate();
                parentFrame.repaint();
            } else {
                JOptionPane.showMessageDialog(this, "Please select valid source and destination!");
            }
        });
        
        formPanel.add(searchBtn, gbc);
        
        section.add(titlePanel, BorderLayout.NORTH);
        section.add(formPanel, BorderLayout.CENTER);
        
        return section;
    }
    
    private JPanel createPopularRoutesSection() {
        JPanel section = new JPanel(new BorderLayout());
        section.setBackground(Color.WHITE);
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(Color.WHITE);
        
        JLabel title = new JLabel("Popular Routes");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(33, 33, 33));
        
        JLabel subtitle = new JLabel("Most booked routes in AP & Telangana");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(Color.GRAY);
        
        titlePanel.add(title);
        titlePanel.add(subtitle);
        
        JPanel routesGrid = new JPanel(new GridLayout(2, 4, 20, 20));
        routesGrid.setBackground(Color.WHITE);
        routesGrid.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        String[][] routes = {
            {"Hyderabad → Vijayawada", "450", "6:00 AM", "2h 30m", "32"},
            {"Visakhapatnam → Tirupati", "850", "8:00 PM", "12h", "25"},
            {"Warangal → Khammam", "180", "10:00 AM", "2h", "18"},
            {"Guntur → Nellore", "320", "7:30 AM", "4h", "22"},
            {"Tirupati → Kadapa", "280", "6:30 PM", "3h", "15"},
            {"Rajahmundry → Kakinada", "120", "11:00 AM", "1h", "28"},
            {"Karimnagar → Nizamabad", "160", "2:00 PM", "1h 45m", "20"},
            {"Kurnool → Anantapur", "220", "9:00 AM", "2h 45m", "24"}
        };
        
        Color[] colors = {
            new Color(33, 150, 243), new Color(76, 175, 80), new Color(156, 39, 176),
            new Color(255, 152, 0), new Color(233, 30, 99), new Color(0, 188, 212),
            new Color(255, 193, 7), new Color(96, 125, 139)
        };
        
        for (int i = 0; i < routes.length; i++) {
            final int index = i;
            JPanel card = createRouteCard(routes[i], colors[i]);
            final String from = routes[i][0].split(" → ")[0];
            final String to = routes[i][0].split(" → ")[1];
            
            card.setCursor(new Cursor(Cursor.HAND_CURSOR));
            card.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent evt) {
                    if (!isLoggedIn) {
                        int option = JOptionPane.showConfirmDialog(card,
                            "Please login to book tickets. Would you like to login now?",
                            "Login Required",
                            JOptionPane.YES_NO_OPTION);
                        if (option == JOptionPane.YES_OPTION) showLoginDialog();
                        return;
                    }
                    parentFrame.getContentPane().removeAll();
                    parentFrame.add(new SeatSelectionPage(parentFrame, from, to, "Today"));
                    parentFrame.revalidate();
                    parentFrame.repaint();
                }
                public void mouseEntered(MouseEvent evt) {
                    card.setBackground(colors[index].darker());
                }
                public void mouseExited(MouseEvent evt) {
                    card.setBackground(colors[index]);
                }
            });
            
            routesGrid.add(card);
        }
        
        section.add(titlePanel, BorderLayout.NORTH);
        section.add(routesGrid, BorderLayout.CENTER);
        
        return section;
    }
    
    private JPanel createRouteCard(String[] route, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(color);
        card.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
        
        JLabel routeLabel = new JLabel(route[0]);
        routeLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        routeLabel.setForeground(Color.WHITE);
        routeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel priceLabel = new JLabel("₹" + route[1]);
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        priceLabel.setForeground(Color.WHITE);
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPanel details = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        details.setBackground(color);
        
        JLabel timeLabel = new JLabel("🕐 " + route[2]);
        timeLabel.setForeground(new Color(255, 255, 255, 230));
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        
        JLabel durationLabel = new JLabel("⏱️ " + route[3]);
        durationLabel.setForeground(new Color(255, 255, 255, 230));
        durationLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        
        JLabel seatsLabel = new JLabel("💺 " + route[4] + " seats");
        seatsLabel.setForeground(new Color(255, 255, 255, 230));
        seatsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        
        details.add(timeLabel);
        details.add(durationLabel);
        details.add(seatsLabel);
        
        JLabel bookLabel = new JLabel("Book Now →");
        bookLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        bookLabel.setForeground(Color.WHITE);
        bookLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        bookLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        card.add(routeLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(priceLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(details);
        card.add(bookLabel);
        
        return card;
    }
    
    private JPanel createWhyChooseUs() {
        JPanel section = new JPanel(new BorderLayout());
        section.setBackground(Color.WHITE);
        section.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));
        
        JLabel title = new JLabel("Why Choose FunoBus?");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(33, 33, 33));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        
        JPanel grid = new JPanel(new GridLayout(1, 4, 30, 0));
        grid.setBackground(Color.WHITE);
        grid.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        String[][] features = {
            {"🎫", "Easy Booking", "Book in 3 clicks"},
            {"💺", "Choose Seat", "Select preferred seats"},
            {"💰", "Best Price", "Lowest fares guaranteed"},
            {"🛡️", "Safe Travel", "COVID safety protocols"}
        };
        
        for (String[] f : features) {
            JPanel card = new JPanel();
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
            card.setBackground(Color.WHITE);
            
            JLabel icon = new JLabel(f[0]);
            icon.setFont(new Font("Segoe UI", Font.PLAIN, 40));
            icon.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            JLabel titleLabel = new JLabel(f[1]);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            titleLabel.setForeground(new Color(33, 33, 33));
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
            
            JLabel descLabel = new JLabel(f[2]);
            descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            descLabel.setForeground(Color.GRAY);
            descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            card.add(icon);
            card.add(titleLabel);
            card.add(descLabel);
            
            grid.add(card);
        }
        
        section.add(title, BorderLayout.NORTH);
        section.add(grid, BorderLayout.CENTER);
        
        return section;
    }
    
    private String[] getAPTSCities() {
        return new String[]{"Select City", "Hyderabad", "Vijayawada", "Visakhapatnam", "Tirupati",
            "Warangal", "Guntur", "Nellore", "Kurnool", "Rajahmundry", "Kakinada",
            "Karimnagar", "Nizamabad", "Khammam", "Anantapur", "Kadapa", "Eluru",
            "Ongole", "Mahbubnagar", "Adilabad"};
    }
    
    private String getDate(int days) {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.add(java.util.Calendar.DAY_OF_MONTH, days);
        return new java.text.SimpleDateFormat("dd MMM, EEE").format(cal.getTime());
    }
    
    private void refreshPage() {
        parentFrame.getContentPane().removeAll();
        parentFrame.add(new HomePage(parentFrame));
        parentFrame.revalidate();
        parentFrame.repaint();
    }
    
    public static void setLoggedIn(boolean status, String username) {
        isLoggedIn = status;
        currentUser = username;
    }
    
    public static boolean isLoggedIn() {
        return isLoggedIn;
    }
    
    public static String getCurrentUser() {
        return currentUser;
    }
    
    public static int getCurrentUserId() {
        return currentUserId;
    }
    
    public static void setCurrentUserId(int userId) {
        currentUserId = userId;
    }
    
    private void showLoginDialog() {
        JDialog dialog = new JDialog(parentFrame, "Login", true);
        dialog.setSize(400, 480);
        dialog.setLocationRelativeTo(parentFrame);
        dialog.setLayout(new BorderLayout());
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 10, 8, 10);
        
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel title = new JLabel("Welcome Back!");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(primaryColor);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(title, gbc);
        
        gbc.gridy = 1;
        JLabel subtitle = new JLabel("Login to book your journey");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(Color.GRAY);
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(subtitle, gbc);
        
        gbc.gridy = 2;
        gbc.insets = new Insets(20, 10, 5, 10);
        panel.add(new JLabel("Email"), gbc);
        
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 10, 10, 10);
        JTextField emailField = new JTextField();
        emailField.setPreferredSize(new Dimension(300, 40));
        panel.add(emailField, gbc);
        
        gbc.gridy = 4;
        gbc.insets = new Insets(10, 10, 5, 10);
        panel.add(new JLabel("Password"), gbc);
        
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 10, 10, 10);
        JPasswordField passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(300, 40));
        panel.add(passwordField, gbc);
        
        gbc.gridy = 6;
        gbc.insets = new Insets(20, 10, 10, 10);
        JButton loginBtn = new JButton("LOGIN");
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        loginBtn.setBackground(primaryColor);
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setBorderPainted(false);
        loginBtn.setFocusPainted(false);
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginBtn.setPreferredSize(new Dimension(300, 45));
        
        loginBtn.addActionListener(e -> {
            String email = emailField.getText().trim();
            String pass = new String(passwordField.getPassword());
            
            if (email.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please enter email and password!");
                return;
            }
            
            User user = UserDAO.loginUser(email, pass);
            
            if (user != null) {
                setLoggedIn(true, user.getFullName().split(" ")[0]);
                setCurrentUserId(user.getUserId());
                JOptionPane.showMessageDialog(dialog, "✅ Login successful! Welcome " + user.getFullName() + "!");
                dialog.dispose();
                refreshPage();
            } else {
                JOptionPane.showMessageDialog(dialog, 
                    "❌ Invalid email or password!\n\nTry: john@email.com / john123", 
                    "Login Failed", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        panel.add(loginBtn, gbc);
        
        gbc.gridy = 7;
        gbc.insets = new Insets(10, 10, 10, 10);
        JLabel signupLabel = new JLabel("Don't have an account? Sign Up");
        signupLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        signupLabel.setForeground(primaryColor);
        signupLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signupLabel.setHorizontalAlignment(SwingConstants.CENTER);
        signupLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                dialog.dispose();
                showSignupDialog();
            }
        });
        panel.add(signupLabel, gbc);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private void showSignupDialog() {
        JDialog dialog = new JDialog(parentFrame, "Sign Up", true);
        dialog.setSize(450, 620);
        dialog.setLocationRelativeTo(parentFrame);
        dialog.setLayout(new BorderLayout());
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 10, 8, 10);
        
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel title = new JLabel("Create Account");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(secondaryColor);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(title, gbc);
        
        gbc.gridy = 1;
        panel.add(new JLabel("Full Name"), gbc);
        
        gbc.gridy = 2;
        JTextField nameField = new JTextField();
        nameField.setPreferredSize(new Dimension(350, 40));
        panel.add(nameField, gbc);
        
        gbc.gridy = 3;
        panel.add(new JLabel("Email"), gbc);
        
        gbc.gridy = 4;
        JTextField emailField = new JTextField();
        emailField.setPreferredSize(new Dimension(350, 40));
        panel.add(emailField, gbc);
        
        gbc.gridy = 5;
        panel.add(new JLabel("Phone"), gbc);
        
        gbc.gridy = 6;
        JTextField phoneField = new JTextField();
        phoneField.setPreferredSize(new Dimension(350, 40));
        panel.add(phoneField, gbc);
        
        gbc.gridy = 7;
        panel.add(new JLabel("Password"), gbc);
        
        gbc.gridy = 8;
        JPasswordField passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(350, 40));
        panel.add(passwordField, gbc);
        
        gbc.gridy = 9;
        gbc.insets = new Insets(20, 10, 10, 10);
        JButton signupBtn = new JButton("SIGN UP");
        signupBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        signupBtn.setBackground(secondaryColor);
        signupBtn.setForeground(Color.WHITE);
        signupBtn.setBorderPainted(false);
        signupBtn.setFocusPainted(false);
        signupBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signupBtn.setPreferredSize(new Dimension(350, 45));
        
        signupBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            String pass = new String(passwordField.getPassword());
            
            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill all fields!");
                return;
            }
            
            if (UserDAO.isEmailExists(email)) {
                JOptionPane.showMessageDialog(dialog, 
                    "Email already registered! Please use another email.",
                    "Email Exists",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            User newUser = new User(name, email, phone, pass);
            boolean registered = UserDAO.registerUser(newUser);
            
            if (registered) {
                User user = UserDAO.loginUser(email, pass);
                if (user != null) {
                    setLoggedIn(true, user.getFullName().split(" ")[0]);
                    setCurrentUserId(user.getUserId());
                }
                JOptionPane.showMessageDialog(dialog, 
                    "✅ Account created successfully! Welcome " + name.split(" ")[0] + "!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
                refreshPage();
            } else {
                JOptionPane.showMessageDialog(dialog, 
                    "❌ Registration failed! Please try again.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        panel.add(signupBtn, gbc);
        
        gbc.gridy = 10;
        gbc.insets = new Insets(10, 10, 10, 10);
        JLabel loginLabel = new JLabel("Already have an account? Login");
        loginLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        loginLabel.setForeground(primaryColor);
        loginLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loginLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                dialog.dispose();
                showLoginDialog();
            }
        });
        panel.add(loginLabel, gbc);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
}
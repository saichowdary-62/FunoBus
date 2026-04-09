package funobus;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;

public class AdminPage extends JPanel {
    private JFrame parentFrame;
    private Color primaryColor = new Color(25, 118, 210);
    private Color dangerColor = new Color(220, 53, 69);
    private Color successColor = new Color(40, 167, 69);
    private Color warningColor = new Color(255, 193, 7);
    private Color infoColor = new Color(23, 162, 184);
    private Color darkColor = new Color(52, 58, 64);
    
    private JTable busesTable;
    private JTable routesTable;
    private JTable bookingsTable;
    private JTable usersTable;
    private JTable schedulesTable;
    
    private DefaultTableModel busesModel;
    private DefaultTableModel routesModel;
    private DefaultTableModel bookingsModel;
    private DefaultTableModel usersModel;
    private DefaultTableModel schedulesModel;
    
    private JLabel statsBuses, statsRoutes, statsBookings, statsUsers, statsRevenue, statsTodayBookings;
    private Timer refreshTimer;
    
    public AdminPage(JFrame frame) {
        this.parentFrame = frame;
        setLayout(new BorderLayout());
        setBackground(new Color(240, 242, 245));
        
        add(createHeader(), BorderLayout.NORTH);
        
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setBackground(Color.WHITE);
        
        tabbedPane.addTab("📊 Dashboard", createDashboardPanel());
        tabbedPane.addTab("🚍 Buses", createBusesPanel());
        tabbedPane.addTab("🛣️ Routes", createRoutesPanel());
        tabbedPane.addTab("📅 Schedules", createSchedulesPanel());
        tabbedPane.addTab("👥 Users", createUsersPanel());
        tabbedPane.addTab("📋 Bookings", createBookingsPanel());
        tabbedPane.addTab("💰 Revenue", createRevenuePanel());
        
        add(tabbedPane, BorderLayout.CENTER);
        
        loadAllData();
        
        // Auto-refresh every 30 seconds
        refreshTimer = new Timer(30000, e -> refreshAllData());
        refreshTimer.start();
    }
    
    private void loadAllData() {
        loadStats();
        loadBusesData();
        loadRoutesData();
        loadSchedulesData();
        loadUsersData();
        loadBookingsData();
    }
    
    private void refreshAllData() {
        loadStats();
        loadBusesData();
        loadRoutesData();
        loadSchedulesData();
        loadUsersData();
        loadBookingsData();
    }
    
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JLabel title = new JLabel("Admin Dashboard - FunoBus");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(primaryColor);
        
        JButton refreshBtn = new JButton("🔄 Refresh");
        refreshBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        refreshBtn.setBackground(infoColor);
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setBorderPainted(false);
        refreshBtn.setFocusPainted(false);
        refreshBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshBtn.setPreferredSize(new Dimension(120, 40));
        refreshBtn.addActionListener(e -> refreshAllData());
        
        JButton logoutBtn = new JButton("🚪 Logout");
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoutBtn.setBackground(dangerColor);
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.setPreferredSize(new Dimension(120, 40));
        logoutBtn.addActionListener(e -> {
            refreshTimer.stop();
            HomePage.setLoggedIn(false, null);
            parentFrame.getContentPane().removeAll();
            parentFrame.add(new HomePage(parentFrame));
            parentFrame.revalidate();
            parentFrame.repaint();
        });
        
        JLabel adminLabel = new JLabel("Welcome, Admin Amar");
        adminLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        adminLabel.setForeground(Color.GRAY);
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.add(adminLabel);
        rightPanel.add(refreshBtn);
        rightPanel.add(logoutBtn);
        
        header.add(title, BorderLayout.WEST);
        header.add(rightPanel, BorderLayout.EAST);
        
        return header;
    }
    
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 242, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Stats Cards Panel
        JPanel statsPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        statsPanel.setBackground(new Color(240, 242, 245));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        
        statsBuses = createStatCard("🚍", "Total Buses", "0", primaryColor);
        statsRoutes = createStatCard("🛣️", "Total Routes", "0", successColor);
        statsBookings = createStatCard("📋", "Total Bookings", "0", warningColor);
        statsUsers = createStatCard("👥", "Total Users", "0", new Color(156, 39, 176));
        statsRevenue = createStatCard("💰", "Total Revenue", "₹0", successColor);
        statsTodayBookings = createStatCard("📅", "Today's Bookings", "0", infoColor);
        
        statsPanel.add(statsBuses);
        statsPanel.add(statsRoutes);
        statsPanel.add(statsUsers);
        statsPanel.add(statsBookings);
        statsPanel.add(statsRevenue);
        statsPanel.add(statsTodayBookings);
        
        // Recent Activity Panel
        JPanel activityPanel = new JPanel(new BorderLayout());
        activityPanel.setBackground(Color.WHITE);
        activityPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel activityTitle = new JLabel("📋 Recent Bookings");
        activityTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        activityTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        String[] columns = {"Booking Ref", "User", "Seats", "Total", "Date"};
        DefaultTableModel activityModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        JTable activityTable = new JTable(activityModel);
        activityTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        activityTable.setRowHeight(30);
        activityTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        loadRecentActivity(activityModel);
        
        JScrollPane scrollPane = new JScrollPane(activityTable);
        scrollPane.setPreferredSize(new Dimension(0, 250));
        
        activityPanel.add(activityTitle, BorderLayout.NORTH);
        activityPanel.add(scrollPane, BorderLayout.CENTER);
        
        panel.add(statsPanel, BorderLayout.NORTH);
        panel.add(activityPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JLabel createStatCard(String icon, String title, String value, Color color) {
        JLabel label = new JLabel("<html><div style='text-align: center; padding: 20px; background: white; border-radius: 10px;'>" +
            "<span style='font-size: 32px;'>" + icon + "</span><br>" +
            "<span style='font-size: 28px; font-weight: bold; color: " + getHexColor(color) + ";'>" + value + "</span><br>" +
            "<span style='font-size: 14px; color: gray;'>" + title + "</span></div></html>");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        label.setBackground(Color.WHITE);
        label.setOpaque(true);
        return label;
    }
    
    private String getHexColor(Color color) {
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }
    
    private void updateStatCard(JLabel label, String value) {
        String text = label.getText();
        String newText = text.replaceAll(">\\d+<", ">" + value + "<");
        label.setText(newText);
    }
    
    private void loadStats() {
        updateStatCard(statsBuses, String.valueOf(getTotalCount("buses")));
        updateStatCard(statsRoutes, String.valueOf(getTotalCount("routes")));
        updateStatCard(statsUsers, String.valueOf(getTotalCount("users")));
        updateStatCard(statsBookings, String.valueOf(getTotalCount("bookings")));
        
        double revenue = getTotalRevenue();
        updateStatCard(statsRevenue, "₹" + String.format("%.0f", revenue));
        
        int todayBookings = getTodayBookingsCount();
        updateStatCard(statsTodayBookings, String.valueOf(todayBookings));
    }
    
    private void loadRecentActivity(DefaultTableModel model) {
        model.setRowCount(0);
        String sql = "SELECT b.booking_reference, u.full_name, b.seat_numbers, b.total_fare, b.booking_date " +
                     "FROM bookings b JOIN users u ON b.user_id = u.user_id " +
                     "ORDER BY b.booking_date DESC LIMIT 10";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("booking_reference"));
                row.add(rs.getString("full_name"));
                row.add(rs.getString("seat_numbers"));
                row.add("₹" + rs.getDouble("total_fare"));
                row.add(rs.getTimestamp("booking_date").toString().substring(0, 16));
                model.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private JPanel createBusesPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(240, 242, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonPanel.setBackground(new Color(240, 242, 245));
        
        JButton addBtn = createButton("➕ Add Bus", successColor);
        JButton editBtn = createButton("✏️ Edit Bus", primaryColor);
        JButton deleteBtn = createButton("🗑️ Delete Bus", dangerColor);
        JButton refreshBtn = createButton("🔄 Refresh", darkColor);
        
        addBtn.addActionListener(e -> showAddBusDialog());
        editBtn.addActionListener(e -> showEditBusDialog());
        deleteBtn.addActionListener(e -> deleteItem("buses", "bus_id", busesTable, busesModel));
        refreshBtn.addActionListener(e -> loadBusesData());
        
        buttonPanel.add(addBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(refreshBtn);
        
        String[] columns = {"ID", "Bus Number", "Operator Name", "Bus Type", "Total Seats"};
        busesModel = new NonEditableTableModel(columns);
        busesTable = createStyledTable(busesModel);
        
        JScrollPane scrollPane = new JScrollPane(busesTable);
        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createRoutesPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(240, 242, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonPanel.setBackground(new Color(240, 242, 245));
        
        JButton addBtn = createButton("➕ Add Route", successColor);
        JButton editBtn = createButton("✏️ Edit Route", primaryColor);
        JButton deleteBtn = createButton("🗑️ Delete Route", dangerColor);
        JButton refreshBtn = createButton("🔄 Refresh", darkColor);
        
        addBtn.addActionListener(e -> showAddRouteDialog());
        editBtn.addActionListener(e -> showEditRouteDialog());
        deleteBtn.addActionListener(e -> deleteItem("routes", "route_id", routesTable, routesModel));
        refreshBtn.addActionListener(e -> loadRoutesData());
        
        buttonPanel.add(addBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(refreshBtn);
        
        String[] columns = {"ID", "Source", "Destination", "Distance (km)", "Base Fare (₹)"};
        routesModel = new NonEditableTableModel(columns);
        routesTable = createStyledTable(routesModel);
        
        JScrollPane scrollPane = new JScrollPane(routesTable);
        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createSchedulesPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(240, 242, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonPanel.setBackground(new Color(240, 242, 245));
        
        JButton addBtn = createButton("➕ Add Schedule", successColor);
        JButton deleteBtn = createButton("🗑️ Delete Schedule", dangerColor);
        JButton refreshBtn = createButton("🔄 Refresh", darkColor);
        
        addBtn.addActionListener(e -> showAddScheduleDialog());
        deleteBtn.addActionListener(e -> deleteItem("bus_schedules", "schedule_id", schedulesTable, schedulesModel));
        refreshBtn.addActionListener(e -> loadSchedulesData());
        
        buttonPanel.add(addBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(refreshBtn);
        
        String[] columns = {"ID", "Bus", "Route", "Departure", "Arrival", "Date", "Seats", "Fare"};
        schedulesModel = new NonEditableTableModel(columns);
        schedulesTable = createStyledTable(schedulesModel);
        
        JScrollPane scrollPane = new JScrollPane(schedulesTable);
        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createUsersPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(240, 242, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonPanel.setBackground(new Color(240, 242, 245));
        
        JButton deleteBtn = createButton("🗑️ Delete User", dangerColor);
        JButton refreshBtn = createButton("🔄 Refresh", darkColor);
        
        deleteBtn.addActionListener(e -> deleteItem("users", "user_id", usersTable, usersModel));
        refreshBtn.addActionListener(e -> loadUsersData());
        
        buttonPanel.add(deleteBtn);
        buttonPanel.add(refreshBtn);
        
        String[] columns = {"ID", "Full Name", "Email", "Phone", "Role", "Joined Date"};
        usersModel = new NonEditableTableModel(columns);
        usersTable = createStyledTable(usersModel);
        
        JScrollPane scrollPane = new JScrollPane(usersTable);
        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createBookingsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(240, 242, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonPanel.setBackground(new Color(240, 242, 245));
        
        JButton refreshBtn = createButton("🔄 Refresh", darkColor);
        refreshBtn.addActionListener(e -> loadBookingsData());
        
        buttonPanel.add(refreshBtn);
        
        String[] columns = {"ID", "Reference", "User ID", "Seats", "Total Fare", "Status", "Date"};
        bookingsModel = new NonEditableTableModel(columns);
        bookingsTable = createStyledTable(bookingsModel);
        
        JScrollPane scrollPane = new JScrollPane(bookingsTable);
        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createRevenuePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 242, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Today's Revenue
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(createRevenueCard("📅 Today's Revenue", getTodayRevenue(), successColor), gbc);
        
        // This Week Revenue
        gbc.gridx = 1;
        panel.add(createRevenueCard("📆 This Week", getWeekRevenue(), primaryColor), gbc);
        
        // This Month Revenue
        gbc.gridx = 2;
        panel.add(createRevenueCard("📊 This Month", getMonthRevenue(), warningColor), gbc);
        
        // Total Revenue
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 3;
        panel.add(createTotalRevenueCard(), gbc);
        
        return panel;
    }
    
    private JPanel createRevenueCard(String title, double amount, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(25, 40, 25, 40)
        ));
        card.setPreferredSize(new Dimension(250, 150));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        titleLabel.setForeground(Color.GRAY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel amountLabel = new JLabel("₹" + String.format("%,.0f", amount));
        amountLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        amountLabel.setForeground(color);
        amountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        amountLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        card.add(titleLabel);
        card.add(amountLabel);
        
        return card;
    }
    
    private JPanel createTotalRevenueCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(25, 118, 210));
        card.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        double totalRevenue = getTotalRevenue();
        
        JLabel titleLabel = new JLabel("💰 TOTAL REVENUE");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel amountLabel = new JLabel("₹" + String.format("%,.0f", totalRevenue));
        amountLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        amountLabel.setForeground(Color.WHITE);
        amountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        amountLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        card.add(titleLabel);
        card.add(amountLabel);
        
        return card;
    }
    
    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(140, 35));
        return button;
    }
    
    private JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(35);
        table.setIntercellSpacing(new Dimension(10, 5));
        table.setSelectionBackground(new Color(200, 220, 255));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(primaryColor);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));
        return table;
    }
    
    private void loadBusesData() {
        busesModel.setRowCount(0);
        String sql = "SELECT * FROM buses ORDER BY bus_id";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("bus_id"));
                row.add(rs.getString("bus_number"));
                row.add(rs.getString("operator_name"));
                row.add(rs.getString("bus_type"));
                row.add(rs.getInt("total_seats"));
                busesModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void loadRoutesData() {
        routesModel.setRowCount(0);
        String sql = "SELECT * FROM routes ORDER BY route_id";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("route_id"));
                row.add(rs.getString("source_city"));
                row.add(rs.getString("destination_city"));
                row.add(rs.getInt("distance_km"));
                row.add(rs.getDouble("base_fare"));
                routesModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void loadSchedulesData() {
        schedulesModel.setRowCount(0);
        String sql = "SELECT bs.schedule_id, b.operator_name, CONCAT(r.source_city, ' → ', r.destination_city) as route, " +
                     "bs.departure_time, bs.arrival_time, bs.journey_date, bs.available_seats, bs.fare " +
                     "FROM bus_schedules bs " +
                     "JOIN buses b ON bs.bus_id = b.bus_id " +
                     "JOIN routes r ON bs.route_id = r.route_id " +
                     "ORDER BY bs.journey_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("schedule_id"));
                row.add(rs.getString("operator_name"));
                row.add(rs.getString("route"));
                row.add(rs.getTime("departure_time").toString().substring(0, 5));
                row.add(rs.getTime("arrival_time").toString().substring(0, 5));
                row.add(rs.getDate("journey_date").toString());
                row.add(rs.getInt("available_seats"));
                row.add("₹" + rs.getDouble("fare"));
                schedulesModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void loadUsersData() {
        usersModel.setRowCount(0);
        String sql = "SELECT user_id, full_name, email, phone, role, created_at FROM users ORDER BY user_id";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("user_id"));
                row.add(rs.getString("full_name"));
                row.add(rs.getString("email"));
                row.add(rs.getString("phone"));
                row.add(rs.getString("role"));
                row.add(rs.getString("created_at"));
                usersModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void loadBookingsData() {
        bookingsModel.setRowCount(0);
        String sql = "SELECT * FROM bookings ORDER BY booking_id DESC";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("booking_id"));
                row.add(rs.getString("booking_reference"));
                row.add(rs.getInt("user_id"));
                row.add(rs.getString("seat_numbers"));
                row.add("₹" + rs.getDouble("total_fare"));
                row.add(rs.getString("booking_status"));
                row.add(rs.getTimestamp("booking_date").toString());
                bookingsModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private int getTotalCount(String tableName) {
        String sql = "SELECT COUNT(*) as count FROM " + tableName;
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    private double getTotalRevenue() {
        String sql = "SELECT SUM(total_fare) as total FROM bookings";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    private double getTodayRevenue() {
        String sql = "SELECT SUM(total_fare) as total FROM bookings WHERE DATE(booking_date) = CURDATE()";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    private double getWeekRevenue() {
        String sql = "SELECT SUM(total_fare) as total FROM bookings WHERE WEEK(booking_date) = WEEK(CURDATE())";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    private double getMonthRevenue() {
        String sql = "SELECT SUM(total_fare) as total FROM bookings WHERE MONTH(booking_date) = MONTH(CURDATE())";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    private int getTodayBookingsCount() {
        String sql = "SELECT COUNT(*) as count FROM bookings WHERE DATE(booking_date) = CURDATE()";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    private void showAddBusDialog() {
        JDialog dialog = new JDialog(parentFrame, "Add Bus", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(parentFrame);
        dialog.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(new JLabel("Bus Number:"), gbc);
        gbc.gridx = 1;
        JTextField busNumberField = new JTextField(15);
        dialog.add(busNumberField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(new JLabel("Operator Name:"), gbc);
        gbc.gridx = 1;
        JTextField operatorField = new JTextField(15);
        dialog.add(operatorField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        dialog.add(new JLabel("Bus Type:"), gbc);
        gbc.gridx = 1;
        String[] types = {"AC Sleeper", "AC Seater", "Non-AC Sleeper", "Non-AC Seater"};
        JComboBox<String> typeCombo = new JComboBox<>(types);
        dialog.add(typeCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        dialog.add(new JLabel("Total Seats:"), gbc);
        gbc.gridx = 1;
        JSpinner seatsSpinner = new JSpinner(new SpinnerNumberModel(40, 20, 60, 1));
        dialog.add(seatsSpinner, gbc);
        
        gbc.gridx = 1; gbc.gridy = 4;
        JButton saveBtn = new JButton("Save Bus");
        saveBtn.setBackground(successColor);
        saveBtn.setForeground(Color.WHITE);
        saveBtn.addActionListener(e -> {
            String sql = "INSERT INTO buses (bus_number, operator_name, bus_type, total_seats) VALUES (?, ?, ?, ?)";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, busNumberField.getText());
                pstmt.setString(2, operatorField.getText());
                pstmt.setString(3, (String) typeCombo.getSelectedItem());
                pstmt.setInt(4, (int) seatsSpinner.getValue());
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(dialog, "Bus added successfully!");
                dialog.dispose();
                loadBusesData();
                loadStats();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(dialog, "Error adding bus!");
            }
        });
        dialog.add(saveBtn, gbc);
        
        dialog.setVisible(true);
    }
    
    private void showEditBusDialog() {
        int selectedRow = busesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a bus to edit!");
            return;
        }
        
        int busId = (int) busesModel.getValueAt(selectedRow, 0);
        String currentBusNumber = (String) busesModel.getValueAt(selectedRow, 1);
        String currentOperator = (String) busesModel.getValueAt(selectedRow, 2);
        
        JDialog dialog = new JDialog(parentFrame, "Edit Bus", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(parentFrame);
        dialog.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(new JLabel("Bus Number:"), gbc);
        gbc.gridx = 1;
        JTextField busNumberField = new JTextField(currentBusNumber, 15);
        dialog.add(busNumberField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(new JLabel("Operator Name:"), gbc);
        gbc.gridx = 1;
        JTextField operatorField = new JTextField(currentOperator, 15);
        dialog.add(operatorField, gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        JButton updateBtn = new JButton("Update Bus");
        updateBtn.setBackground(primaryColor);
        updateBtn.setForeground(Color.WHITE);
        updateBtn.addActionListener(e -> {
            String sql = "UPDATE buses SET bus_number = ?, operator_name = ? WHERE bus_id = ?";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, busNumberField.getText());
                pstmt.setString(2, operatorField.getText());
                pstmt.setInt(3, busId);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(dialog, "Bus updated successfully!");
                dialog.dispose();
                loadBusesData();
                loadStats();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(dialog, "Error updating bus!");
            }
        });
        dialog.add(updateBtn, gbc);
        
        dialog.setVisible(true);
    }
    
    private void showAddRouteDialog() {
        JDialog dialog = new JDialog(parentFrame, "Add Route", true);
        dialog.setSize(450, 350);
        dialog.setLocationRelativeTo(parentFrame);
        dialog.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(new JLabel("Source City:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> sourceCombo = new JComboBox<>(getAPTSCities());
        dialog.add(sourceCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(new JLabel("Destination City:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> destCombo = new JComboBox<>(getAPTSCities());
        dialog.add(destCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        dialog.add(new JLabel("Distance (km):"), gbc);
        gbc.gridx = 1;
        JTextField distanceField = new JTextField(10);
        dialog.add(distanceField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        dialog.add(new JLabel("Base Fare (₹):"), gbc);
        gbc.gridx = 1;
        JTextField fareField = new JTextField(10);
        dialog.add(fareField, gbc);
        
        gbc.gridx = 1; gbc.gridy = 4;
        JButton saveBtn = new JButton("Save Route");
        saveBtn.setBackground(successColor);
        saveBtn.setForeground(Color.WHITE);
        saveBtn.addActionListener(e -> {
            String sql = "INSERT INTO routes (source_city, destination_city, distance_km, base_fare) VALUES (?, ?, ?, ?)";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, (String) sourceCombo.getSelectedItem());
                pstmt.setString(2, (String) destCombo.getSelectedItem());
                pstmt.setInt(3, Integer.parseInt(distanceField.getText()));
                pstmt.setDouble(4, Double.parseDouble(fareField.getText()));
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(dialog, "Route added successfully!");
                dialog.dispose();
                loadRoutesData();
                loadStats();
            } catch (SQLException | NumberFormatException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(dialog, "Error adding route!");
            }
        });
        dialog.add(saveBtn, gbc);
        
        dialog.setVisible(true);
    }
    
    private void showEditRouteDialog() {
        int selectedRow = routesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a route to edit!");
            return;
        }
        
        int routeId = (int) routesModel.getValueAt(selectedRow, 0);
        
        JDialog dialog = new JDialog(parentFrame, "Edit Route", true);
        dialog.setSize(450, 350);
        dialog.setLocationRelativeTo(parentFrame);
        dialog.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(new JLabel("Distance (km):"), gbc);
        gbc.gridx = 1;
        JTextField distanceField = new JTextField(10);
        dialog.add(distanceField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(new JLabel("Base Fare (₹):"), gbc);
        gbc.gridx = 1;
        JTextField fareField = new JTextField(10);
        dialog.add(fareField, gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        JButton updateBtn = new JButton("Update Route");
        updateBtn.setBackground(primaryColor);
        updateBtn.setForeground(Color.WHITE);
        updateBtn.addActionListener(e -> {
            String sql = "UPDATE routes SET distance_km = ?, base_fare = ? WHERE route_id = ?";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, Integer.parseInt(distanceField.getText()));
                pstmt.setDouble(2, Double.parseDouble(fareField.getText()));
                pstmt.setInt(3, routeId);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(dialog, "Route updated successfully!");
                dialog.dispose();
                loadRoutesData();
                loadStats();
            } catch (SQLException | NumberFormatException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(dialog, "Error updating route!");
            }
        });
        dialog.add(updateBtn, gbc);
        
        dialog.setVisible(true);
    }
    
    private void showAddScheduleDialog() {
        JDialog dialog = new JDialog(parentFrame, "Add Schedule", true);
        dialog.setSize(500, 450);
        dialog.setLocationRelativeTo(parentFrame);
        dialog.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Bus selection
        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(new JLabel("Select Bus:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> busCombo = new JComboBox<>(getBusList());
        dialog.add(busCombo, gbc);
        
        // Route selection
        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(new JLabel("Select Route:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> routeCombo = new JComboBox<>(getRouteList());
        dialog.add(routeCombo, gbc);
        
        // Departure time
        gbc.gridx = 0; gbc.gridy = 2;
        dialog.add(new JLabel("Departure Time:"), gbc);
        gbc.gridx = 1;
        JSpinner depHour = new JSpinner(new SpinnerNumberModel(6, 0, 23, 1));
        JSpinner depMin = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));
        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        timePanel.add(new JLabel("Hour:"));
        timePanel.add(depHour);
        timePanel.add(new JLabel("Min:"));
        timePanel.add(depMin);
        dialog.add(timePanel, gbc);
        
        // Arrival time
        gbc.gridx = 0; gbc.gridy = 3;
        dialog.add(new JLabel("Arrival Time:"), gbc);
        gbc.gridx = 1;
        JSpinner arrHour = new JSpinner(new SpinnerNumberModel(8, 0, 23, 1));
        JSpinner arrMin = new JSpinner(new SpinnerNumberModel(30, 0, 59, 1));
        JPanel timePanel2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        timePanel2.add(new JLabel("Hour:"));
        timePanel2.add(arrHour);
        timePanel2.add(new JLabel("Min:"));
        timePanel2.add(arrMin);
        dialog.add(timePanel2, gbc);
        
        // Journey date
        gbc.gridx = 0; gbc.gridy = 4;
        dialog.add(new JLabel("Journey Date:"), gbc);
        gbc.gridx = 1;
        JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);
        dialog.add(dateSpinner, gbc);
        
        // Available seats
        gbc.gridx = 0; gbc.gridy = 5;
        dialog.add(new JLabel("Available Seats:"), gbc);
        gbc.gridx = 1;
        JSpinner seatsSpinner = new JSpinner(new SpinnerNumberModel(40, 1, 60, 1));
        dialog.add(seatsSpinner, gbc);
        
        // Fare
        gbc.gridx = 0; gbc.gridy = 6;
        dialog.add(new JLabel("Fare (₹):"), gbc);
        gbc.gridx = 1;
        JTextField fareField = new JTextField(10);
        dialog.add(fareField, gbc);
        
        gbc.gridx = 1; gbc.gridy = 7;
        JButton saveBtn = new JButton("Add Schedule");
        saveBtn.setBackground(successColor);
        saveBtn.setForeground(Color.WHITE);
        saveBtn.addActionListener(e -> {
            try {
                String busInfo = (String) busCombo.getSelectedItem();
                int busId = Integer.parseInt(busInfo.split(" - ")[0]);
                
                String routeInfo = (String) routeCombo.getSelectedItem();
                int routeId = Integer.parseInt(routeInfo.split(" - ")[0]);
                
                String depTime = String.format("%02d:%02d:00", depHour.getValue(), depMin.getValue());
                String arrTime = String.format("%02d:%02d:00", arrHour.getValue(), arrMin.getValue());
                
                java.util.Date date = (java.util.Date) dateSpinner.getValue();
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                
                int seats = (int) seatsSpinner.getValue();
                double fare = Double.parseDouble(fareField.getText());
                
                String sql = "INSERT INTO bus_schedules (bus_id, route_id, departure_time, arrival_time, journey_date, available_seats, fare) VALUES (?, ?, ?, ?, ?, ?, ?)";
                try (Connection conn = DBConnection.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, busId);
                    pstmt.setInt(2, routeId);
                    pstmt.setString(3, depTime);
                    pstmt.setString(4, arrTime);
                    pstmt.setDate(5, sqlDate);
                    pstmt.setInt(6, seats);
                    pstmt.setDouble(7, fare);
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(dialog, "Schedule added successfully!");
                    dialog.dispose();
                    loadSchedulesData();
                    loadStats();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(dialog, "Error adding schedule!");
            }
        });
        dialog.add(saveBtn, gbc);
        
        dialog.setVisible(true);
    }
    
    private void deleteItem(String table, String idColumn, JTable tableComponent, DefaultTableModel model) {
        int selectedRow = tableComponent.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item to delete!");
            return;
        }
        
        int id = (int) model.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this item?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM " + table + " WHERE " + idColumn + " = ?";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Item deleted successfully!");
                
                // Refresh the specific table
                if (table.equals("buses")) loadBusesData();
                else if (table.equals("routes")) loadRoutesData();
                else if (table.equals("bus_schedules")) loadSchedulesData();
                else if (table.equals("users")) loadUsersData();
                
                loadStats();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting item!");
            }
        }
    }
    
    private String[] getAPTSCities() {
        return new String[]{"Hyderabad", "Vijayawada", "Visakhapatnam", "Tirupati", "Warangal",
            "Guntur", "Nellore", "Kurnool", "Rajahmundry", "Kakinada", "Karimnagar",
            "Nizamabad", "Khammam", "Anantapur", "Kadapa", "Eluru", "Ongole", "Mahbubnagar", "Adilabad"};
    }
    
    private String[] getBusList() {
        java.util.ArrayList<String> list = new java.util.ArrayList<>();
        String sql = "SELECT bus_id, bus_number, operator_name FROM buses";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(rs.getInt("bus_id") + " - " + rs.getString("bus_number") + " (" + rs.getString("operator_name") + ")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list.toArray(new String[0]);
    }
    
    private String[] getRouteList() {
        java.util.ArrayList<String> list = new java.util.ArrayList<>();
        String sql = "SELECT route_id, source_city, destination_city FROM routes";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(rs.getInt("route_id") + " - " + rs.getString("source_city") + " → " + rs.getString("destination_city"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list.toArray(new String[0]);
    }
    
    class NonEditableTableModel extends DefaultTableModel {
        public NonEditableTableModel(String[] columns) {
            super(columns, 0);
        }
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }
}
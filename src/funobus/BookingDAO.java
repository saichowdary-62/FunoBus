package funobus;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class BookingDAO {
    
    public static boolean saveBooking(int userId, int scheduleId, String seatNumbers, double totalFare) {
        String bookingRef = generateBookingReference();
        String sql = "INSERT INTO bookings (booking_reference, user_id, schedule_id, seat_numbers, total_fare, payment_status) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, bookingRef);
            pstmt.setInt(2, userId);
            pstmt.setInt(3, scheduleId);
            pstmt.setString(4, seatNumbers);
            pstmt.setDouble(5, totalFare);
            pstmt.setString(6, "Cash");
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static ResultSet getUserBookings(int userId) {
        String sql = "SELECT b.booking_reference, b.booking_date, b.seat_numbers, b.total_fare, b.payment_status, " +
                     "bs.departure_time, bs.arrival_time, bs.journey_date, " +
                     "r.source_city, r.destination_city, " +
                     "bus.operator_name, bus.bus_number " +
                     "FROM bookings b " +
                     "JOIN bus_schedules bs ON b.schedule_id = bs.schedule_id " +
                     "JOIN routes r ON bs.route_id = r.route_id " +
                     "JOIN buses bus ON bs.bus_id = bus.bus_id " +
                     "WHERE b.user_id = ? " +
                     "ORDER BY b.booking_date DESC";
        
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private static String generateBookingReference() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String date = sdf.format(new Date());
        Random rand = new Random();
        int random = 1000 + rand.nextInt(9000);
        return "FB" + date + random;
    }
}
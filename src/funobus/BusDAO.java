package funobus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BusDAO {
    
    public static List<BusSchedule> getAvailableBuses(String source, String destination, String date) {
        List<BusSchedule> schedules = new ArrayList<>();
        
        String sql = "SELECT bs.*, b.bus_number, b.operator_name, b.bus_type, " +
                     "r.source_city, r.destination_city " +
                     "FROM bus_schedules bs " +
                     "JOIN buses b ON bs.bus_id = b.bus_id " +
                     "JOIN routes r ON bs.route_id = r.route_id " +
                     "WHERE r.source_city = ? AND r.destination_city = ? " +
                     "AND DATE(bs.journey_date) = ? AND bs.available_seats > 0";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, source);
            pstmt.setString(2, destination);
            pstmt.setString(3, convertDate(date));
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                BusSchedule schedule = new BusSchedule();
                schedule.setScheduleId(rs.getInt("schedule_id"));
                schedule.setBusNumber(rs.getString("bus_number"));
                schedule.setOperatorName(rs.getString("operator_name"));
                schedule.setBusType(rs.getString("bus_type"));
                schedule.setSource(rs.getString("source_city"));
                schedule.setDestination(rs.getString("destination_city"));
                
                Time depTime = rs.getTime("departure_time");
                schedule.setDepartureTime(depTime.toString().substring(0, 5));
                
                Time arrTime = rs.getTime("arrival_time");
                schedule.setArrivalTime(arrTime.toString().substring(0, 5));
                
                schedule.setJourneyDate(rs.getDate("journey_date").toString());
                schedule.setAvailableSeats(rs.getInt("available_seats"));
                schedule.setFare(rs.getDouble("fare"));
                
                schedules.add(schedule);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return schedules;
    }
    
    private static String convertDate(String date) {
        if (date.equals("Today")) {
            return new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
        } else if (date.equals("Tomorrow")) {
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.add(java.util.Calendar.DAY_OF_MONTH, 1);
            return new java.text.SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        }
        return date;
    }
}
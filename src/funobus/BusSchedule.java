package funobus;

public class BusSchedule {
    private int scheduleId;
    private String busNumber;
    private String operatorName;
    private String busType;
    private String source;
    private String destination;
    private String departureTime;
    private String arrivalTime;
    private String journeyDate;
    private int availableSeats;
    private double fare;
    
    public int getScheduleId() { return scheduleId; }
    public void setScheduleId(int scheduleId) { this.scheduleId = scheduleId; }
    
    public String getBusNumber() { return busNumber; }
    public void setBusNumber(String busNumber) { this.busNumber = busNumber; }
    
    public String getOperatorName() { return operatorName; }
    public void setOperatorName(String operatorName) { this.operatorName = operatorName; }
    
    public String getBusType() { return busType; }
    public void setBusType(String busType) { this.busType = busType; }
    
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    
    public String getDepartureTime() { return departureTime; }
    public void setDepartureTime(String departureTime) { this.departureTime = departureTime; }
    
    public String getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(String arrivalTime) { this.arrivalTime = arrivalTime; }
    
    public String getJourneyDate() { return journeyDate; }
    public void setJourneyDate(String journeyDate) { this.journeyDate = journeyDate; }
    
    public int getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }
    
    public double getFare() { return fare; }
    public void setFare(double fare) { this.fare = fare; }
}
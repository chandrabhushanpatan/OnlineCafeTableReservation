// Reservation.java
package model;

import java.time.LocalDateTime;

public class Reservation {
    private int reservationId;
    private int customerId;
    private int tableId;
    private LocalDateTime reservationTime;
    private LocalDateTime endTime;
    private int numberOfGuests;
    private String status;
    private String specialRequests;

    public Reservation(int reservationId, int customerId, int tableId,
                      LocalDateTime reservationTime, LocalDateTime endTime,
                      int numberOfGuests, String status, String specialRequests) {
        this.reservationId = reservationId;
        this.customerId = customerId;
        this.tableId = tableId;
        this.reservationTime = reservationTime;
        this.endTime = endTime;
        this.numberOfGuests = numberOfGuests;
        this.status = status;
        this.specialRequests = specialRequests;
    }

    // Getters and setters
    public int getReservationId() { return reservationId; }
    public void setReservationId(int reservationId) { this.reservationId = reservationId; }
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public int getTableId() { return tableId; }
    public void setTableId(int tableId) { this.tableId = tableId; }
    public LocalDateTime getReservationTime() { return reservationTime; }
    public void setReservationTime(LocalDateTime reservationTime) { 
        this.reservationTime = reservationTime; 
    }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public int getNumberOfGuests() { return numberOfGuests; }
    public void setNumberOfGuests(int numberOfGuests) { this.numberOfGuests = numberOfGuests; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getSpecialRequests() { return specialRequests; }
    public void setSpecialRequests(String specialRequests) { 
        this.specialRequests = specialRequests; 
    }
}
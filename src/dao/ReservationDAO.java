// ReservationDAO.java
package dao;

import model.Reservation;
import Myjdbc.DatabaseConnection;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {
    // Create a new reservation
    public int createReservation(Reservation reservation) throws SQLException {
        String SQL = "INSERT INTO Reservations (customerId, tableId, reservationTime, endTime, " +
                     "numberOfGuests, status, specialRequests) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, reservation.getCustomerId());
            pstmt.setInt(2, reservation.getTableId());
            pstmt.setTimestamp(3, Timestamp.valueOf(reservation.getReservationTime()));
            pstmt.setTimestamp(4, Timestamp.valueOf(reservation.getEndTime()));
            pstmt.setInt(5, reservation.getNumberOfGuests());
            pstmt.setString(6, reservation.getStatus());
            pstmt.setString(7, reservation.getSpecialRequests());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating reservation failed, no rows affected.");
            }
            
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new SQLException("Creating reservation failed, no ID obtained.");
                }
            }
        }
    }

    // Get reservation by ID
    public Reservation getReservationById(int reservationId) throws SQLException {
        String SQL = "SELECT * FROM Reservations WHERE reservationId = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            
            pstmt.setInt(1, reservationId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Reservation(
                        rs.getInt("reservationId"),
                        rs.getInt("customerId"),
                        rs.getInt("tableId"),
                        rs.getTimestamp("reservationTime").toLocalDateTime(),
                        rs.getTimestamp("endTime").toLocalDateTime(),
                        rs.getInt("numberOfGuests"),
                        rs.getString("status"),
                        rs.getString("specialRequests")
                    );
                }
            }
        }
        return null;
    }

    // Get reservations by customer
    public List<Reservation> getReservationsByCustomer(int customerId) throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String SQL = "SELECT * FROM Reservations WHERE customerId = ? ORDER BY reservationTime DESC";
        
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            
            pstmt.setInt(1, customerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    reservations.add(new Reservation(
                        rs.getInt("reservationId"),
                        rs.getInt("customerId"),
                        rs.getInt("tableId"),
                        rs.getTimestamp("reservationTime").toLocalDateTime(),
                        rs.getTimestamp("endTime").toLocalDateTime(),
                        rs.getInt("numberOfGuests"),
                        rs.getString("status"),
                        rs.getString("specialRequests")
                    ));
                }
            }
        }
        return reservations;
    }

    // Update reservation status
    public boolean updateReservationStatus(int reservationId, String status) throws SQLException {
        String SQL = "UPDATE Reservations SET status = ? WHERE reservationId = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            
            pstmt.setString(1, status);
            pstmt.setInt(2, reservationId);
            return pstmt.executeUpdate() > 0;
        }
    }

    // Get all reservations
    public List<Reservation> getAllReservations() throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String SQL = "SELECT * FROM Reservations ORDER BY reservationTime DESC";
        
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {
            
            while (rs.next()) {
                reservations.add(new Reservation(
                    rs.getInt("reservationId"),
                    rs.getInt("customerId"),
                    rs.getInt("tableId"),
                    rs.getTimestamp("reservationTime").toLocalDateTime(),
                    rs.getTimestamp("endTime").toLocalDateTime(),
                    rs.getInt("numberOfGuests"),
                    rs.getString("status"),
                    rs.getString("specialRequests")
                ));
            }
        }
        return reservations;
    }
}
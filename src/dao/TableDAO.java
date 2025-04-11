// TableDAO.java
package dao;

import model.Table;
import Myjdbc.DatabaseConnection;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TableDAO {
    // Insert a new table
    public int insertTable(Table table) throws SQLException {
        String SQL = "INSERT INTO Tables (tableNumber, capacity, tableType, status) " +
                     "VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, table.getTableNumber());
            pstmt.setInt(2, table.getCapacity());
            pstmt.setString(3, table.getTableType());
            pstmt.setString(4, table.getStatus());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating table failed, no rows affected.");
            }
            
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new SQLException("Creating table failed, no ID obtained.");
                }
            }
        }
    }

    // Get all tables
    public List<Table> getAllTables() throws SQLException {
        List<Table> tables = new ArrayList<>();
        String SQL = "SELECT * FROM Tables";
        
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {
            
            while (rs.next()) {
                tables.add(new Table(
                    rs.getInt("tableId"),
                    rs.getString("tableNumber"),
                    rs.getInt("capacity"),
                    rs.getString("tableType"),
                    rs.getString("status")
                ));
            }
        }
        return tables;
    }

    // Get available tables for a given time and number of guests
    public List<Table> getAvailableTables(LocalDateTime startTime, LocalDateTime endTime, 
                                         int numberOfGuests) throws SQLException {
        List<Table> availableTables = new ArrayList<>();
        String SQL = "SELECT t.* FROM Tables t WHERE t.capacity >= ? AND t.status = 'Available' " +
                     "AND t.tableId NOT IN (SELECT r.tableId FROM Reservations r " +
                     "WHERE (r.reservationTime < ? AND r.endTime > ?) " +
                     "AND r.status IN ('Confirmed', 'Completed'))";
        
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            
            pstmt.setInt(1, numberOfGuests);
            pstmt.setTimestamp(2, Timestamp.valueOf(endTime));
            pstmt.setTimestamp(3, Timestamp.valueOf(startTime));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    availableTables.add(new Table(
                        rs.getInt("tableId"),
                        rs.getString("tableNumber"),
                        rs.getInt("capacity"),
                        rs.getString("tableType"),
                        rs.getString("status")
                    ));
                }
            }
        }
        return availableTables;
    }

    // Update table status
    public boolean updateTableStatus(int tableId, String status) throws SQLException {
        String SQL = "UPDATE Tables SET status = ? WHERE tableId = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            
            pstmt.setString(1, status);
            pstmt.setInt(2, tableId);
            return pstmt.executeUpdate() > 0;
        }
    }

    // Get table by ID
    public Table getTableById(int tableId) throws SQLException {
        String SQL = "SELECT * FROM Tables WHERE tableId = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            
            pstmt.setInt(1, tableId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Table(
                        rs.getInt("tableId"),
                        rs.getString("tableNumber"),
                        rs.getInt("capacity"),
                        rs.getString("tableType"),
                        rs.getString("status")
                    );
                }
            }
        }
        return null;
    }
}
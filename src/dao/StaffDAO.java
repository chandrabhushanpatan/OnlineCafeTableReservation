// StaffDAO.java
package dao;

import model.Staff;
import Myjdbc.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StaffDAO {
    // Insert a new staff member
    public int insertStaff(Staff staff) throws SQLException {
        String SQL = "INSERT INTO Staff (fullName, email, phone, position, status, username, password) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, staff.getFullName());
            pstmt.setString(2, staff.getEmail());
            pstmt.setString(3, staff.getPhone());
            pstmt.setString(4, staff.getPosition());
            pstmt.setString(5, staff.getStatus());
            pstmt.setString(6, staff.getUsername());
            pstmt.setString(7, staff.getPassword());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating staff failed, no rows affected.");
            }
            
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new SQLException("Creating staff failed, no ID obtained.");
                }
            }
        }
    }

    // Retrieve a staff member by ID
    public Staff getStaffById(int staffId) throws SQLException {
        String SQL = "SELECT * FROM Staff WHERE staffId = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            
            pstmt.setInt(1, staffId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Staff(
                        rs.getInt("staffId"),
                        rs.getString("fullName"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("position"),
                        rs.getString("status"),
                        rs.getString("username"),
                        rs.getString("password")
                    );
                }
            }
        }
        return null;
    }

    // Retrieve a staff member by username
    public Staff getStaffByUsername(String username) throws SQLException {
        String SQL = "SELECT * FROM Staff WHERE username = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Staff(
                        rs.getInt("staffId"),
                        rs.getString("fullName"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("position"),
                        rs.getString("status"),
                        rs.getString("username"),
                        rs.getString("password")
                    );
                }
            }
        }
        return null;
    }

    // Update a staff member
    public boolean updateStaff(Staff staff) throws SQLException {
        String SQL = "UPDATE Staff SET fullName = ?, email = ?, phone = ?, position = ?, " +
                     "status = ?, username = ?, password = ? WHERE staffId = ?";
        
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            
            pstmt.setString(1, staff.getFullName());
            pstmt.setString(2, staff.getEmail());
            pstmt.setString(3, staff.getPhone());
            pstmt.setString(4, staff.getPosition());
            pstmt.setString(5, staff.getStatus());
            pstmt.setString(6, staff.getUsername());
            pstmt.setString(7, staff.getPassword());
            pstmt.setInt(8, staff.getStaffId());
            
            return pstmt.executeUpdate() > 0;
        }
    }

    // Delete a staff member
    public boolean deleteStaff(int staffId) throws SQLException {
        String SQL = "DELETE FROM Staff WHERE staffId = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            
            pstmt.setInt(1, staffId);
            return pstmt.executeUpdate() > 0;
        }
    }

    // Get all staff members
    public List<Staff> getAllStaff() throws SQLException {
        List<Staff> staffList = new ArrayList<>();
        String SQL = "SELECT * FROM Staff";
        
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {
            
            while (rs.next()) {
                staffList.add(new Staff(
                    rs.getInt("staffId"),
                    rs.getString("fullName"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("position"),
                    rs.getString("status"),
                    rs.getString("username"),
                    rs.getString("password")
                ));
            }
        }
        return staffList;
    }
}
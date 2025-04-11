// ManagerDAO.java
package dao;

import model.Manager;
import Myjdbc.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ManagerDAO {
    // Insert a new manager
    public int insertManager(Manager manager) throws SQLException {
        String SQL = "INSERT INTO Managers (fullName, email, phone, department, username, password) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, manager.getFullName());
            pstmt.setString(2, manager.getEmail());
            pstmt.setString(3, manager.getPhone());
            pstmt.setString(4, manager.getDepartment());
            pstmt.setString(5, manager.getUsername());
            pstmt.setString(6, manager.getPassword());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating manager failed, no rows affected.");
            }
            
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new SQLException("Creating manager failed, no ID obtained.");
                }
            }
        }
    }

    // Retrieve a manager by ID
    public Manager getManagerById(int managerId) throws SQLException {
        String SQL = "SELECT * FROM Managers WHERE managerId = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            
            pstmt.setInt(1, managerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Manager(
                        rs.getInt("managerId"),
                        rs.getString("fullName"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("department"),
                        rs.getString("username"),
                        rs.getString("password")
                    );
                }
            }
        }
        return null;
    }

    // Retrieve a manager by username
    public Manager getManagerByUsername(String username) throws SQLException {
        String SQL = "SELECT * FROM Managers WHERE username = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Manager(
                        rs.getInt("managerId"),
                        rs.getString("fullName"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("department"),
                        rs.getString("username"),
                        rs.getString("password")
                    );
                }
            }
        }
        return null;
    }

    // Update a manager
    public boolean updateManager(Manager manager) throws SQLException {
        String SQL = "UPDATE Managers SET fullName = ?, email = ?, phone = ?, department = ?, " +
                     "username = ?, password = ? WHERE managerId = ?";
        
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            
            pstmt.setString(1, manager.getFullName());
            pstmt.setString(2, manager.getEmail());
            pstmt.setString(3, manager.getPhone());
            pstmt.setString(4, manager.getDepartment());
            pstmt.setString(5, manager.getUsername());
            pstmt.setString(6, manager.getPassword());
            pstmt.setInt(7, manager.getManagerId());
            
            return pstmt.executeUpdate() > 0;
        }
    }

    // Delete a manager
    public boolean deleteManager(int managerId) throws SQLException {
        String SQL = "DELETE FROM Managers WHERE managerId = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            
            pstmt.setInt(1, managerId);
            return pstmt.executeUpdate() > 0;
        }
    }

    // Get all managers
    public List<Manager> getAllManagers() throws SQLException {
        List<Manager> managerList = new ArrayList<>();
        String SQL = "SELECT * FROM Managers";
        
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {
            
            while (rs.next()) {
                managerList.add(new Manager(
                    rs.getInt("managerId"),
                    rs.getString("fullName"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("department"),
                    rs.getString("username"),
                    rs.getString("password")
                ));
            }
        }
        return managerList;
    }
}
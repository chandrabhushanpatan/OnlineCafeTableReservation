// MenuItemDAO.java
package dao;

import model.MenuItem;
import Myjdbc.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuItemDAO {
    // Create a new menu item
    public int createMenuItem(MenuItem menuItem) throws SQLException {
        String SQL = "INSERT INTO MenuItems (categoryId, name, description, price, isAvailable) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, menuItem.getCategoryId());
            pstmt.setString(2, menuItem.getName());
            pstmt.setString(3, menuItem.getDescription());
            pstmt.setDouble(4, menuItem.getPrice());
            pstmt.setBoolean(5, menuItem.isAvailable());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating menu item failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating menu item failed, no ID obtained.");
                }
            }
        }
    }

    // Get menu item by ID
    public MenuItem getMenuItemById(int itemId) throws SQLException {
        String SQL = "SELECT * FROM MenuItems WHERE itemId = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            
            pstmt.setInt(1, itemId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new MenuItem(
                        rs.getInt("itemId"),
                        rs.getInt("categoryId"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getBoolean("isAvailable")
                    );
                }
            }
        }
        return null;
    }

    // Get all menu items
    public List<MenuItem> getAllMenuItems() throws SQLException {
        List<MenuItem> menuItems = new ArrayList<>();
        String SQL = "SELECT * FROM MenuItems";
        
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {
            
            while (rs.next()) {
                menuItems.add(new MenuItem(
                    rs.getInt("itemId"),
                    rs.getInt("categoryId"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getDouble("price"),
                    rs.getBoolean("isAvailable")
                ));
            }
        }
        return menuItems;
    }

    // Get menu items by category
    public List<MenuItem> getMenuItemsByCategory(int categoryId) throws SQLException {
        List<MenuItem> menuItems = new ArrayList<>();
        String SQL = "SELECT * FROM MenuItems WHERE categoryId = ?";
        
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            
            pstmt.setInt(1, categoryId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    menuItems.add(new MenuItem(
                        rs.getInt("itemId"),
                        rs.getInt("categoryId"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getBoolean("isAvailable")
                    ));
                }
            }
        }
        return menuItems;
    }

    // Update a menu item
    public boolean updateMenuItem(MenuItem menuItem) throws SQLException {
        String SQL = "UPDATE MenuItems SET categoryId = ?, name = ?, description = ?, " +
                     "price = ?, isAvailable = ? WHERE itemId = ?";
        
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            
            pstmt.setInt(1, menuItem.getCategoryId());
            pstmt.setString(2, menuItem.getName());
            pstmt.setString(3, menuItem.getDescription());
            pstmt.setDouble(4, menuItem.getPrice());
            pstmt.setBoolean(5, menuItem.isAvailable());
            pstmt.setInt(6, menuItem.getItemId());
            
            return pstmt.executeUpdate() > 0;
        }
    }

    // Delete a menu item
    public boolean deleteMenuItem(int itemId) throws SQLException {
        String SQL = "DELETE FROM MenuItems WHERE itemId = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            
            pstmt.setInt(1, itemId);
            return pstmt.executeUpdate() > 0;
        }
    }

    // Toggle menu item availability
    public boolean toggleMenuItemAvailability(int itemId) throws SQLException {
        String SQL = "UPDATE MenuItems SET isAvailable = NOT isAvailable WHERE itemId = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            
            pstmt.setInt(1, itemId);
            return pstmt.executeUpdate() > 0;
        }
    }
}
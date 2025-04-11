// DatabaseInitializer.java
package main;

import Myjdbc.DatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    public static void initializeDatabase() {
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement()) {

            String[] createTables = {
            		"CREATE TABLE IF NOT EXISTS Customers (" +
            		"customerId INT AUTO_INCREMENT PRIMARY KEY, " +
            		"fullName VARCHAR(100) NOT NULL, " +
            		"email VARCHAR(100) UNIQUE NOT NULL, " +
            		"phone VARCHAR(15), " +
            		"address VARCHAR(255), " +
            		"username VARCHAR(50) UNIQUE NOT NULL, " +
            		"password VARCHAR(255) NOT NULL)",  

                "CREATE TABLE IF NOT EXISTS Staff (" +
                "staffId INT AUTO_INCREMENT PRIMARY KEY, " +
                "fullName VARCHAR(100) NOT NULL, " +
                "email VARCHAR(100) UNIQUE NOT NULL, " +
                "phone VARCHAR(15), " +
                "position VARCHAR(50) NOT NULL, " +
                "status ENUM('Active', 'Inactive', 'On Leave') DEFAULT 'Active', " +
                "username VARCHAR(50) UNIQUE, " +
                "password VARCHAR(255))",

                "CREATE TABLE IF NOT EXISTS Managers (" +
                "managerId INT AUTO_INCREMENT PRIMARY KEY, " +
                "fullName VARCHAR(100) NOT NULL, " +
                "email VARCHAR(100) UNIQUE NOT NULL, " +
                "phone VARCHAR(15), " +
                "department VARCHAR(50), " +
                "username VARCHAR(50) UNIQUE, " +
                "password VARCHAR(255))",

                "CREATE TABLE IF NOT EXISTS Tables (" +
                "tableId INT AUTO_INCREMENT PRIMARY KEY, " +
                "tableNumber VARCHAR(10) UNIQUE NOT NULL, " +
                "capacity INT NOT NULL, " +
                "tableType ENUM('Standard', 'Booth', 'Outdoor', 'VIP') DEFAULT 'Standard', " +
                "status ENUM('Available', 'Reserved', 'Occupied', 'Maintenance') DEFAULT 'Available')",

                "CREATE TABLE IF NOT EXISTS FoodCategories (" +
                "categoryId INT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(50) NOT NULL, " +
                "description TEXT)",

                "CREATE TABLE IF NOT EXISTS MenuItems (" +
                "itemId INT AUTO_INCREMENT PRIMARY KEY, " +
                "categoryId INT, " +
                "name VARCHAR(100) NOT NULL, " +
                "description TEXT, " +
                "price DECIMAL(10,2) NOT NULL, " +
                "isAvailable BOOLEAN DEFAULT TRUE, " +
                "FOREIGN KEY (categoryId) REFERENCES FoodCategories(categoryId))",

                "CREATE TABLE IF NOT EXISTS Reservations (" +
                "reservationId INT AUTO_INCREMENT PRIMARY KEY, " +
                "customerId INT NOT NULL, " +
                "tableId INT NOT NULL, " +
                "reservationTime DATETIME NOT NULL, " +
                "endTime DATETIME, " +
                "numberOfGuests INT NOT NULL, " +
                "status ENUM('Confirmed', 'Cancelled', 'Completed', 'No-show') DEFAULT 'Confirmed', " +
                "specialRequests TEXT, " +
                "FOREIGN KEY (customerId) REFERENCES Customers(customerId), " +
                "FOREIGN KEY (tableId) REFERENCES Tables(tableId), " +
                "INDEX (reservationTime))",

                "CREATE TABLE IF NOT EXISTS Orders (" +
                "orderId INT AUTO_INCREMENT PRIMARY KEY, " +
                "customerId INT, " +
                "tableId INT, " +
                "orderTime DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "status ENUM('Pending', 'Preparing', 'Ready', 'Served', 'Cancelled') DEFAULT 'Pending', " +
                "totalAmount DECIMAL(10,2), " +
                "notes TEXT, " +
                "FOREIGN KEY (customerId) REFERENCES Customers(customerId), " +
                "FOREIGN KEY (tableId) REFERENCES Tables(tableId))",

                "CREATE TABLE IF NOT EXISTS OrderItems (" +
                "orderItemId INT AUTO_INCREMENT PRIMARY KEY, " +
                "orderId INT NOT NULL, " +
                "itemId INT NOT NULL, " +
                "quantity INT NOT NULL DEFAULT 1, " +
                "specialInstructions TEXT, " +
                "itemPrice DECIMAL(10,2) NOT NULL, " +
                "FOREIGN KEY (orderId) REFERENCES Orders(orderId), " +
                "FOREIGN KEY (itemId) REFERENCES MenuItems(itemId))"
            };

            for (String sql : createTables) {
                stmt.executeUpdate(sql);
            }

            insertSampleData(conn);

        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void insertSampleData(Connection conn) throws SQLException {
        if (!isTableEmpty(conn, "Customers")) {
            return;
        }

        try (Statement stmt = conn.createStatement()) {
            // Insert sample data
        	// In DatabaseInitializer.java
        	stmt.executeUpdate("INSERT INTO Customers (fullName, email, phone, address, username, password) VALUES " +
        	    "('John Doe', 'john@example.com', '1234567890', '123 Main St', 'john', '1234'), " +
        	    "('Jane Smith', 'jane@example.com', '9876543210', '456 Oak Ave', 'jane', '1234')");

            stmt.executeUpdate("INSERT INTO Staff (fullName, email, phone, position, status, username, password) VALUES " +
                "('Staff One', 'staff1@example.com', '1111111111', 'Waiter', 'Active', 'staff1', '1234'), " +
                "('Staff Two', 'staff2@example.com', '2222222222', 'Chef', 'Active', 'staff2', '1234')");

            stmt.executeUpdate("INSERT INTO Managers (fullName, email, phone, department, username, password) VALUES " +
                "('Manager One', 'manager@example.com', '3333333333', 'Operations', 'manager', '1234')");

            stmt.executeUpdate("INSERT INTO Tables (tableNumber, capacity, tableType, status) VALUES " +
                "('T1', 2, 'Standard', 'Available'), " +
                "('T2', 4, 'Standard', 'Available'), " +
                "('T3', 6, 'Booth', 'Available'), " +
                "('T4', 8, 'VIP', 'Available'), " +
                "('T5', 4, 'Outdoor', 'Available')");

            stmt.executeUpdate("INSERT INTO FoodCategories (name, description) VALUES " +
                "('Appetizers', 'Starters and small plates'), " +
                "('Main Courses', 'Hearty main dishes'), " +
                "('Desserts', 'Sweet treats'), " +
                "('Beverages', 'Drinks and refreshments')");

            stmt.executeUpdate("INSERT INTO MenuItems (categoryId, name, description, price) VALUES " +
                "(1, 'Bruschetta', 'Toasted bread with tomatoes and garlic', 6.99), " +
                "(1, 'Mozzarella Sticks', 'Fried cheese sticks with marinara', 7.99), " +
                "(2, 'Spaghetti Carbonara', 'Pasta with creamy egg sauce', 12.99), " +
                "(2, 'Grilled Salmon', 'Fresh salmon with vegetables', 16.99), " +
                "(3, 'Tiramisu', 'Classic Italian dessert', 8.99), " +
                "(3, 'Chocolate Lava Cake', 'Warm chocolate cake with molten center', 9.99), " +
                "(4, 'Cappuccino', 'Espresso with steamed milk', 3.99), " +
                "(4, 'Iced Tea', 'Refreshing cold tea', 2.99)");
        }
    }

    private static boolean isTableEmpty(Connection conn, String tableName) throws SQLException {
        try (var rs = conn.createStatement().executeQuery("SELECT COUNT(*) FROM " + tableName)) {
            return rs.next() && rs.getInt(1) == 0;
        }
    }
}
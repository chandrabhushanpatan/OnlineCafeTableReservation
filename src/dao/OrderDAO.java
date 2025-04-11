package dao;

import model.Order;
import model.OrderItem;
import Myjdbc.DatabaseConnection;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    // Create a new order
    public int createOrder(Order order) throws SQLException {
        String SQL = "INSERT INTO Orders (customerId, tableId, orderTime, status, totalAmount, notes) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, order.getCustomerId());
            pstmt.setInt(2, order.getTableId());
            pstmt.setTimestamp(3, Timestamp.valueOf(order.getOrderTime()));
            pstmt.setString(4, order.getStatus());
            pstmt.setDouble(5, order.getTotalAmount());
            pstmt.setString(6, order.getNotes());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating order failed, no rows affected.");
            }
            
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new SQLException("Creating order failed, no ID obtained.");
                }
            }
        }
    }

    // Add order items
    public void addOrderItems(int orderId, List<OrderItem> orderItems) throws SQLException {
        String SQL = "INSERT INTO OrderItems (orderId, itemId, quantity, specialInstructions, itemPrice) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            for (OrderItem item : orderItems) {
                pstmt.setInt(1, orderId);
                pstmt.setInt(2, item.getItemId());
                pstmt.setInt(3, item.getQuantity());
                pstmt.setString(4, item.getSpecialInstructions());
                pstmt.setDouble(5, item.getItemPrice());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }
    }

    // Get order by ID with items
    public Order getOrderById(int orderId) throws SQLException {
        String orderSQL = "SELECT * FROM Orders WHERE orderId = ?";
        String itemsSQL = "SELECT oi.*, mi.name FROM OrderItems oi " +
                          "JOIN MenuItems mi ON oi.itemId = mi.itemId " +
                          "WHERE oi.orderId = ?";
        
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement orderStmt = conn.prepareStatement(orderSQL);
             PreparedStatement itemsStmt = conn.prepareStatement(itemsSQL)) {
            
            // Get order details
            orderStmt.setInt(1, orderId);
            try (ResultSet orderRs = orderStmt.executeQuery()) {
                if (orderRs.next()) {
                    Order order = new Order(
                        orderRs.getInt("orderId"),
                        orderRs.getInt("customerId"),
                        orderRs.getInt("tableId"),
                        orderRs.getTimestamp("orderTime").toLocalDateTime(),
                        orderRs.getString("status"),
                        orderRs.getDouble("totalAmount"),
                        orderRs.getString("notes"),
                        new ArrayList<>()
                    );
                    
                    // Get order items
                    itemsStmt.setInt(1, orderId);
                    try (ResultSet itemsRs = itemsStmt.executeQuery()) {
                        while (itemsRs.next()) {
                            order.getOrderItems().add(new OrderItem(
                                itemsRs.getInt("orderItemId"),
                                itemsRs.getInt("orderId"),
                                itemsRs.getInt("itemId"),
                                itemsRs.getInt("quantity"),
                                itemsRs.getString("specialInstructions"),
                                itemsRs.getDouble("itemPrice")
                            ));
                        }
                    }
                    return order;
                }
            }
        }
        return null;
    }

    // Update order status
    public boolean updateOrderStatus(int orderId, String status) throws SQLException {
        String SQL = "UPDATE Orders SET status = ? WHERE orderId = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, orderId);
            return pstmt.executeUpdate() > 0;
        }
    }

    // Get all orders
    public List<Order> getAllOrders() throws SQLException {
        List<Order> orders = new ArrayList<>();
        String SQL = "SELECT * FROM Orders";
        
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {
            
            while (rs.next()) {
                Order order = new Order(
                    rs.getInt("orderId"),
                    rs.getInt("customerId"),
                    rs.getInt("tableId"),
                    rs.getTimestamp("orderTime").toLocalDateTime(),
                    rs.getString("status"),
                    rs.getDouble("totalAmount"),
                    rs.getString("notes"),
                    new ArrayList<>()
                );
                
                // Get order items
                String itemsSQL = "SELECT oi.*, mi.name FROM OrderItems oi " +
                                  "JOIN MenuItems mi ON oi.itemId = mi.itemId " +
                                  "WHERE oi.orderId = ?";
                try (PreparedStatement itemsStmt = conn.prepareStatement(itemsSQL)) {
                    itemsStmt.setInt(1, order.getOrderId());
                    try (ResultSet itemsRs = itemsStmt.executeQuery()) {
                        while (itemsRs.next()) {
                            order.getOrderItems().add(new OrderItem(
                                itemsRs.getInt("orderItemId"),
                                itemsRs.getInt("orderId"),
                                itemsRs.getInt("itemId"),
                                itemsRs.getInt("quantity"),
                                itemsRs.getString("specialInstructions"),
                                itemsRs.getDouble("itemPrice")
                            ));
                        }
                    }
                }
                orders.add(order);
            }
        }
        return orders;
    }
    
    // Get orders by customer
    public List<Order> getOrdersByCustomer(int customerId) throws SQLException {
        List<Order> orders = new ArrayList<>();
        String SQL = "SELECT * FROM Orders WHERE customerId = ? ORDER BY orderTime DESC";
        
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, customerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order(
                        rs.getInt("orderId"),
                        rs.getInt("customerId"),
                        rs.getInt("tableId"),
                        rs.getTimestamp("orderTime").toLocalDateTime(),
                        rs.getString("status"),
                        rs.getDouble("totalAmount"),
                        rs.getString("notes"),
                        new ArrayList<>()
                    );
                    
                    // Get order items
                    String itemsSQL = "SELECT oi.*, mi.name FROM OrderItems oi " +
                                      "JOIN MenuItems mi ON oi.itemId = mi.itemId " +
                                      "WHERE oi.orderId = ?";
                    try (PreparedStatement itemsStmt = conn.prepareStatement(itemsSQL)) {
                        itemsStmt.setInt(1, order.getOrderId());
                        try (ResultSet itemsRs = itemsStmt.executeQuery()) {
                            while (itemsRs.next()) {
                                order.getOrderItems().add(new OrderItem(
                                    itemsRs.getInt("orderItemId"),
                                    itemsRs.getInt("orderId"),
                                    itemsRs.getInt("itemId"),
                                    itemsRs.getInt("quantity"),
                                    itemsRs.getString("specialInstructions"),
                                    itemsRs.getDouble("itemPrice")
                                ));
                            }
                        }
                    }
                    orders.add(order);
                }
            }
        }
        return orders;
    }
}
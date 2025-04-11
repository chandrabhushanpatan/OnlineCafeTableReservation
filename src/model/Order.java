// Order.java
package model;

import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private int orderId;
    private int customerId;
    private int tableId;
    private LocalDateTime orderTime;
    private String status;
    private double totalAmount;
    private String notes;
    private List<OrderItem> orderItems;

    public Order(int orderId, int customerId, int tableId, LocalDateTime orderTime,
                String status, double totalAmount, String notes, List<OrderItem> orderItems) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.tableId = tableId;
        this.orderTime = orderTime;
        this.status = status;
        this.totalAmount = totalAmount;
        this.notes = notes;
        this.orderItems = orderItems;
    }

    // Getters and setters
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public int getTableId() { return tableId; }
    public void setTableId(int tableId) { this.tableId = tableId; }
    public LocalDateTime getOrderTime() { return orderTime; }
    public void setOrderTime(LocalDateTime orderTime) { this.orderTime = orderTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public List<OrderItem> getOrderItems() { return orderItems; }
    public void setOrderItems(List<OrderItem> orderItems) { this.orderItems = orderItems; }
}
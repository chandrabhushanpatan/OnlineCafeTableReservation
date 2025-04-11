// Table.java
package model;

public class Table {
    private int tableId;
    private String tableNumber;
    private int capacity;
    private String tableType;
    private String status;

    public Table(int tableId, String tableNumber, int capacity, String tableType, String status) {
        this.tableId = tableId;
        this.tableNumber = tableNumber;
        this.capacity = capacity;
        this.tableType = tableType;
        this.status = status;
    }

    // Getters and setters
    public int getTableId() { return tableId; }
    public void setTableId(int tableId) { this.tableId = tableId; }
    public String getTableNumber() { return tableNumber; }
    public void setTableNumber(String tableNumber) { this.tableNumber = tableNumber; }
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public String getTableType() { return tableType; }
    public void setTableType(String tableType) { this.tableType = tableType; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
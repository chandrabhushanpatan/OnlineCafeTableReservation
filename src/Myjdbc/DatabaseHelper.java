package Myjdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseHelper {

    public static void insertCustomer(String fullName, String email, String phone, String address) {
        String sql = "INSERT INTO Customers (fullName, email, phone, address) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, fullName);
            pstmt.setString(2, email);
            pstmt.setString(3, phone);
            pstmt.setString(4, address);
            pstmt.executeUpdate();
            System.out.println("Customer inserted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getAllCustomers() {
        String sql = "SELECT * FROM Customers";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("customerId") + " | Name: " + rs.getString("fullName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

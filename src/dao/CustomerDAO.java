package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Myjdbc.DatabaseConnection;
import model.Customer;

public class CustomerDAO {

	public int insertCustomer(Customer customer) throws SQLException {
		String SQL = "INSERT INTO Customers (fullName, email, phone, address, username, password) "
				+ "VALUES (?, ?, ?, ?, ?, ?)";

		try (Connection conn = DatabaseConnection.connect();
				PreparedStatement pstmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {

			pstmt.setString(1, customer.getFullName());
			pstmt.setString(2, customer.getEmail());
			pstmt.setString(3, customer.getPhone());
			pstmt.setString(4, customer.getAddress());
			pstmt.setString(5, customer.getUsername());
			pstmt.setString(6, customer.getPassword()); // Storing plaintext

			int affectedRows = pstmt.executeUpdate();
			if (affectedRows > 0) {
				ResultSet rs = pstmt.getGeneratedKeys();
				if (rs.next())
					return rs.getInt(1);
			}
			return -1;
		}
	}

	public Customer getCustomerByUsername(String username) throws SQLException {
		String SQL = "SELECT * FROM Customers WHERE username = ?";
		try (Connection conn = DatabaseConnection.connect(); PreparedStatement pstmt = conn.prepareStatement(SQL)) {

			pstmt.setString(1, username);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return new Customer(rs.getInt("customerId"), rs.getString("fullName"), rs.getString("email"),
						rs.getString("phone"), rs.getString("address"), rs.getString("username"),
						rs.getString("password"));
			}
			return null;
		}
	}

	private Connection connection;

	public boolean updateCustomer(Customer customer) {
		String sql = "UPDATE customers SET full_name=?, email=?, phone=?, address=?, username=?, password=? WHERE customer_id=?";

		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setString(1, customer.getFullName());
			stmt.setString(2, customer.getEmail());
			stmt.setString(3, customer.getPhone());
			stmt.setString(4, customer.getAddress());
			stmt.setString(5, customer.getUsername());
			stmt.setString(6, customer.getPassword());
			stmt.setInt(7, customer.getCustomerId());

			int rowsUpdated = stmt.executeUpdate();
			return rowsUpdated > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// Close connection when done
	public void close() {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}
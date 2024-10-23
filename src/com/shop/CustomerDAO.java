package com.shop;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDAO {
    private Connection connection;

    public CustomerDAO() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
    }

    // Add a new customer
    public void addCustomer(String name, String contact) throws SQLException {
        String query = "INSERT INTO customers (name, contact) VALUES (?, ?)";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, name);
        stmt.setString(2, contact);
        stmt.executeUpdate();
        stmt.close();
    }

    // Retrieve all customers
    public ResultSet getAllCustomers() throws SQLException {
        String query = "SELECT * FROM customers";
        PreparedStatement stmt = connection.prepareStatement(query);
        return stmt.executeQuery();
    }

    // Close connection
    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}

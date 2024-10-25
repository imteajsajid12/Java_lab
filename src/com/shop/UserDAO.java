package com.shop;

import java.sql.*;

public class UserDAO {
    private Connection connection;

    public UserDAO() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
    }

    public void registerUser(String username, String password, String name, String contact) throws SQLException {
        String sql = "INSERT INTO users (username, password, name, contact) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, name);
            stmt.setString(4, contact);
            stmt.executeUpdate();
        }
    }

    public boolean validateUser(String username, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }

    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}

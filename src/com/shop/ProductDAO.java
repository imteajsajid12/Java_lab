package com.shop;

import java.sql.*;

public class ProductDAO {
    private Connection connection;

    public ProductDAO() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
    }

    public void addProduct(String name, double purchasePrice, double sellingPrice, int quantity, double discount) throws SQLException {
        String sql = "INSERT INTO products (product_name, purchase_price, selling_price, quantity, discount) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setDouble(2, purchasePrice);
            stmt.setDouble(3, sellingPrice);
            stmt.setInt(4, quantity);
            stmt.setDouble(5, discount);
            stmt.executeUpdate();
        }
    }

    public int searchProductByName(String productName) throws SQLException {
        String sql = "SELECT product_id FROM products WHERE product_name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, productName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("product_id");
            }
        }
        return -1; // Product not found
    }

    public double getSellingPriceById(int productId) throws SQLException {
        String sql = "SELECT selling_price FROM products WHERE product_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("selling_price");
            }
        }
        return 0;
    }

    public double getDiscountById(int productId) throws SQLException {
        String sql = "SELECT discount FROM products WHERE product_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("discount");
            }
        }
        return 0;
    }

    public int getStockById(int productId) throws SQLException {
        String sql = "SELECT quantity FROM products WHERE product_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("quantity");
            }
        }
        return 0;
    }

    public void updateStock(int productId, int quantitySold) throws SQLException {
        String sql = "UPDATE products SET quantity = quantity - ? WHERE product_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, quantitySold);
            stmt.setInt(2, productId);
            stmt.executeUpdate();
        }
    }

    public int getTotalStock() throws SQLException {
        String sql = "SELECT SUM(quantity) AS total_stock FROM products";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("total_stock");
            }
        }
        return 0;
    }

    public void displayAllProducts() throws SQLException {
        String sql = "SELECT * FROM products";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("product_id") +
                        ", Name: " + rs.getString("product_name") +
                        ", Purchase Price: $" + rs.getDouble("purchase_price") +
                        ", Selling Price: $" + rs.getDouble("selling_price") +
                        ", Quantity: " + rs.getInt("quantity") +
                        ", Discount: " + rs.getDouble("discount") + "%");
            }
        }
    }

    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}

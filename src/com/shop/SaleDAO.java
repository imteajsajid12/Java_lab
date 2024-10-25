package com.shop;

import java.sql.*;

public class SaleDAO {
    private Connection connection;

    public SaleDAO() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
    }


    public void sellProduct(int productId, int quantitySold, double salePrice, double discountedSellingPrice) throws SQLException {
        String sql = "INSERT INTO sales (product_id, quantity_sold, sale_price, discounted_selling_price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            stmt.setInt(2, quantitySold);
            stmt.setDouble(3, salePrice);
            stmt.setDouble(4, discountedSellingPrice);
            stmt.executeUpdate();
        }
    }

    public double getTotalSales() throws SQLException {
        String sql = "SELECT SUM(sale_price * quantity_sold) AS total_sales FROM sales";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble("total_sales");
            }
        }
        return 0;
    }


    public double getTotalProfit() throws SQLException {
        String sql = "SELECT SUM((sale_price - (SELECT purchase_price FROM products WHERE products.product_id = sales.product_id)) * quantity_sold) AS total_profit FROM sales";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble("total_profit");
            }
        }
        return 0;
    }



    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}

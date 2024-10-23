package com.shop;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            UserDAO userDAO = new UserDAO();
            ProductDAO productDAO = new ProductDAO();
            SaleDAO saleDAO = new SaleDAO();

            // Login or Register
            boolean loggedIn = false;
            while (!loggedIn) {
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Choose an option: ");
                int choice = sc.nextInt();
                sc.nextLine(); // Clear buffer

                if (choice == 1) {
                    // Register new user
                    System.out.print("Enter username: ");
                    String username = sc.nextLine();
                    System.out.print("Enter password: ");
                    String password = sc.nextLine();
                    System.out.print("Enter your name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter contact information: ");
                    String contact = sc.nextLine();

                    userDAO.registerUser(username, password, name, contact);
                    System.out.println("User registered successfully!");

                } else if (choice == 2) {
                    // Login existing user
                    System.out.print("Enter username: ");
                    String username = sc.nextLine();
                    System.out.print("Enter password: ");
                    String password = sc.nextLine();

                    if (userDAO.validateUser(username, password)) {
                        System.out.println("Login successful! Welcome " + username);
                        loggedIn = true;
                    } else {
                        System.out.println("Invalid username or password. Please try again.");
                    }
                } else if (choice == 3) {
                    System.out.println("Exiting...");
                    sc.close();
                    return;
                }
            }

            // After login - Main Menu
            while (true) {
                System.out.println("\nMain Menu:");
                System.out.println("1. Add Product");
                System.out.println("2. Sell Products");
                System.out.println("3. View Total Stock");
                System.out.println("4. View Total Sales");
                System.out.println("5. View Total Profit");
                System.out.println("6. View All Products");
                System.out.println("7. Search for a Product");
                System.out.println("8. Exit");

                System.out.print("Choose an option: ");
                int choice = sc.nextInt();
                sc.nextLine(); // Clear buffer

                if (choice == 1) {
                    // Add product to stock
                    System.out.print("Enter product name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter purchase price: ");
                    double purchasePrice = sc.nextDouble();
                    System.out.print("Enter selling price: ");
                    double sellingPrice = sc.nextDouble();
                    System.out.print("Enter quantity: ");
                    int quantity = sc.nextInt();
                    System.out.print("Enter discount percentage: ");
                    double discount = sc.nextDouble();
                    productDAO.addProduct(name, purchasePrice, sellingPrice, quantity, discount);
                    System.out.println("Product added successfully!");

                } else if (choice == 2) {
                    // Sell multiple products
                    List<SaleEntry> sales = new ArrayList<>();
                    double totalSaleAmount = 0;

                    while (true) {
                        System.out.print("Enter product name to sell (or 'done' to finish): ");
                        String productName = sc.nextLine();

                        if (productName.equalsIgnoreCase("done")) {
                            break;
                        }

                        int productId = productDAO.searchProductByName(productName);

                        if (productId == -1) {
                            System.out.println("Product not found!");
                        } else {
                            System.out.print("Enter quantity to sell: ");
                            int quantityToSell = sc.nextInt();
                            sc.nextLine(); // Clear buffer

                            int availableStock = productDAO.getStockById(productId);

                            if (quantityToSell > availableStock) {
                                System.out.println("Insufficient stock. Available quantity: " + availableStock);
                            } else {
                                double discountPercentage = productDAO.getDiscountById(productId);
                                double sellingPrice = productDAO.getSellingPriceById(productId);
                                double discountedPrice = sellingPrice - (sellingPrice * discountPercentage / 100);
                                double totalPrice = discountedPrice * quantityToSell;

                                // Add to sales list
                                sales.add(new SaleEntry(productName, quantityToSell, sellingPrice, discountPercentage, totalPrice));
                                totalSaleAmount += totalPrice;

                                // Record the sale
                                saleDAO.sellProduct(productId, quantityToSell, totalPrice);
                                productDAO.updateStock(productId, quantityToSell); // Update stock
                            }
                        }
                    }

                    // Display sales table
                    System.out.println("\nSales Summary:");
                    System.out.printf("%-20s %-10s %-15s %-10s %-10s\n", "Product Name", "Quantity", "Price (each)", "Discount", "Total Price");
                    System.out.println("----------------------------------------------------------------------");

                    for (SaleEntry sale : sales) {
                        System.out.printf("%-20s %-10d $%-14.2f %-10.2f%% $%-10.2f\n", sale.getProductName(), sale.getQuantity(), sale.getSellingPrice(), sale.getDiscountPercentage(), sale.getTotalPrice());
                    }

                    System.out.printf("\nTotal Sale Amount for all products: $%.2f\n", totalSaleAmount);

                } else if (choice == 3) {
                    // View total stock
                    int totalStock = productDAO.getTotalStock();
                    System.out.println("Total stock available: " + totalStock);

                } else if (choice == 4) {
                    // View total sales
                    double totalSales = saleDAO.getTotalSales();
                    System.out.printf("Total sales: $%.2f\n", totalSales);

                } else if (choice == 5) {
                    // View total profit
                    double totalProfit = saleDAO.getTotalProfit();
                    System.out.printf("Total profit: $%.2f\n", totalProfit);

                } else if (choice == 6) {
                    // View all products
                    productDAO.displayAllProducts();

                } else if (choice == 7) {
                    // Search for a product by name
                    System.out.print("Enter product name to search: ");
                    String productName = sc.nextLine();
                    productDAO.searchProductByName(productName);

                } else if (choice == 8) {
                    // Exit
                    System.out.println("Exiting...");
                    break;
                }
            }

            // Close all connections
            productDAO.closeConnection();
            saleDAO.closeConnection();
            userDAO.closeConnection();

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        sc.close();
    }
}

// SaleEntry class to hold product sale details
class SaleEntry {
    private String productName;
    private int quantity;
    private double sellingPrice;
    private double discountPercentage;
    private double totalPrice;

    public SaleEntry(String productName, int quantity, double sellingPrice, double discountPercentage, double totalPrice) {
        this.productName = productName;
        this.quantity = quantity;
        this.sellingPrice = sellingPrice;
        this.discountPercentage = discountPercentage;
        this.totalPrice = totalPrice;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}

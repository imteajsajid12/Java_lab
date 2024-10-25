package com.shop;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[]    args) {
        Scanner sc = new Scanner(System.in);

        try {
            UserDAO userDAO = new UserDAO();
            ProductDAO productDAO = new ProductDAO();
            SaleDAO saleDAO = new SaleDAO();

            boolean loggedIn = false;
            while (!loggedIn) {
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Choose an option: ");
                int choice = sc.nextInt();
                sc.nextLine();

                if (choice == 1) {
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
                sc.nextLine();

                if (choice == 1) {
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

                }
                else if (choice == 2) {
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
                            sc.nextLine();

                            int availableStock = productDAO.getStockById(productId);

                            if (quantityToSell > availableStock) {
                                System.out.println("Insufficient stock. Available quantity: " + availableStock);
                            } else {
                                double sellingPrice = productDAO.getSellingPriceById(productId);
                                double discountPercentage = productDAO.getDiscountById(productId);
                                double discountedPrice = sellingPrice - (sellingPrice * discountPercentage / 100);
                                System.out.print("Do you want to apply a different discount percentage (y/n)? ");
                                String applyDiscount = sc.nextLine();

                                if (applyDiscount.equalsIgnoreCase("y")) {
                                    System.out.print("Enter discount percentage: ");
                                    discountPercentage = sc.nextDouble();
                                    sc.nextLine();
                                    discountedPrice = sellingPrice - (sellingPrice * discountPercentage / 100);
                                }

                                double totalPrice = discountedPrice * quantityToSell;
                                sales.add(new SaleEntry(productName, quantityToSell, sellingPrice, discountPercentage, discountedPrice, totalPrice));
                                totalSaleAmount += totalPrice;
                                saleDAO.sellProduct(productId, quantityToSell, totalPrice, discountedPrice);
                                productDAO.updateStock(productId, quantityToSell); // Update stock
                            }
                        }
                    }
                    System.out.println("\nSales Summary:");
                    System.out.printf("%-20s %-10s %-15s %-10s %-15s %-10s\n", "Product Name", "Quantity", "Price (each)", "Discount", "Discounted Price", "Total Price");
                    System.out.println("----------------------------------------------------------------------");

                    for (SaleEntry sale : sales) {
                        System.out.printf("%-20s %-10d $%-14.2f %-10.2f%% $%-14.2f $%-10.2f\n",
                                sale.getProductName(),
                                sale.getQuantity(),
                                sale.getSellingPrice(),
                                sale.getDiscountPercentage(),
                                sale.getDiscountedSellingPrice(),
                                sale.getTotalPrice());
                    }

                    System.out.printf("\nTotal Sale Amount for all products: $%.2f\n", totalSaleAmount);


                } else if (choice == 3) {

                    productDAO.displayTotalStockWithProductNames();

                } else if (choice == 4) {

                    double totalSales = saleDAO.getTotalSales();
                    System.out.printf("Total sales: $%.2f\n", totalSales);

                } else if (choice == 5) {
                    double totalProfit = saleDAO.getTotalProfit();
                    System.out.printf("Total profit: $%.2f\n", totalProfit);

                } else if (choice == 6) {
                    productDAO.displayAllProducts();

                } else if (choice == 7) {
                    System.out.print("Enter product name to search: ");
                    String productName = sc.nextLine();
                    productDAO.searchProductByName1(productName);

                } else if (choice == 8) {
                    System.out.println("Exiting...");
                    break;
                }
            }

            productDAO.closeConnection();
            saleDAO.closeConnection();
            userDAO.closeConnection();

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }


        sc.close();
    }
}


package com.shop;

class SaleEntry {
    private String productName;
    private int quantity;
    private double sellingPrice;
    private double discountPercentage;
    private double discountedSellingPrice;
    private double totalPrice;

    public SaleEntry(String productName, int quantity, double sellingPrice, double discountPercentage, double discountedSellingPrice, double totalPrice) {
        this.productName = productName;
        this.quantity = quantity;
        this.sellingPrice = sellingPrice;
        this.discountPercentage = discountPercentage;
        this.discountedSellingPrice = discountedSellingPrice;
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

    public double getDiscountedSellingPrice() {
        return discountedSellingPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}

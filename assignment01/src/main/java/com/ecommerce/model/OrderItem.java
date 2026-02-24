package com.ecommerce.model;

public class OrderItem {
    private final String productId;
    private final String productName;
    private final double price;

    public OrderItem(String productId, String productName, double price) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public double getPrice() {
        return price;
    }
}

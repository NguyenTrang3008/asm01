package com.ecommerce.model;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private final String orderId;
    private final String customerId;
    private final String customerEmail;
    private final List<OrderItem> items;
    private final double total;
    private OrderStatus status;
    private final String shippingAddress;

    public Order(String orderId, String customerId, String customerEmail, 
                 List<OrderItem> items, double total, String shippingAddress) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.customerEmail = customerEmail;
        this.items = new ArrayList<>(items);
        this.total = total;
        this.status = OrderStatus.CONFIRMED;
        this.shippingAddress = shippingAddress;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public List<OrderItem> getItems() {
        return new ArrayList<>(items);
    }

    public double getTotal() {
        return total;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public boolean canBeCancelled() {
        return status == OrderStatus.CONFIRMED;
    }

    public boolean canBeShipped() {
        return status == OrderStatus.CONFIRMED;
    }
}

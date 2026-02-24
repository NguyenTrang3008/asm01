package com.ecommerce.repository;

import com.ecommerce.model.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderRepository {
    private final List<Order> orders = new ArrayList<>();

    public void save(Order order) {
        orders.add(order);
    }

    public Optional<Order> findById(String orderId) {
        return orders.stream()
                .filter(o -> o.getOrderId().equals(orderId))
                .findFirst();
    }

    public List<Order> findAll() {
        return new ArrayList<>(orders);
    }
}

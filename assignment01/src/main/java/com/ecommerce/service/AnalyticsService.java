package com.ecommerce.service;

public class AnalyticsService {
    public void trackOrder(double total, String paymentMethod) {
        System.out.println("[ANALYTICS] New order: $" + String.format("%.2f", total) + " via " + paymentMethod);
    }
}

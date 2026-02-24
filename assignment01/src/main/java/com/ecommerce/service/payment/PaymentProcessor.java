package com.ecommerce.service.payment;

public interface PaymentProcessor {
    boolean processPayment(double amount);
    double calculateFee(double amount);
}

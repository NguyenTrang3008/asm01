package com.ecommerce.service.payment;

public class BankTransferPaymentProcessor implements PaymentProcessor {
    @Override
    public boolean processPayment(double amount) {
        System.out.println("Waiting for bank transfer confirmation...");
        return true; // Always succeeds (manual verification later)
    }

    @Override
    public double calculateFee(double amount) {
        return 0; // No fee for bank transfer
    }
}

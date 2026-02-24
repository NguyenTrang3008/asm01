package com.ecommerce.service;

import com.ecommerce.service.payment.BankTransferPaymentProcessor;
import com.ecommerce.service.payment.CreditCardPaymentProcessor;
import com.ecommerce.service.payment.PayPalPaymentProcessor;
import com.ecommerce.service.payment.PaymentProcessor;

import java.util.HashMap;
import java.util.Map;

public class PaymentService {
    private final Map<String, PaymentProcessor> processors;

    public PaymentService() {
        this.processors = new HashMap<>();
        processors.put("CREDIT_CARD", new CreditCardPaymentProcessor());
        processors.put("PAYPAL", new PayPalPaymentProcessor());
        processors.put("BANK_TRANSFER", new BankTransferPaymentProcessor());
    }

    public boolean processPayment(String paymentMethod, double amount) {
        PaymentProcessor processor = processors.get(paymentMethod);
        if (processor == null) {
            throw new IllegalArgumentException("Unknown payment method: " + paymentMethod);
        }
        return processor.processPayment(amount);
    }

    public double calculateTotalWithFee(String paymentMethod, double amount) {
        PaymentProcessor processor = processors.get(paymentMethod);
        if (processor == null) {
            throw new IllegalArgumentException("Unknown payment method: " + paymentMethod);
        }
        return amount + processor.calculateFee(amount);
    }
}

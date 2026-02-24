package com.ecommerce;

import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.service.*;
import com.ecommerce.service.notification.EmailNotificationService;
import com.ecommerce.service.notification.NotificationService;
import com.ecommerce.service.notification.SmsNotificationService;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        // Initialize repositories
        ProductRepository productRepository = new ProductRepository();
        OrderRepository orderRepository = new OrderRepository();

        // Initialize services
        DiscountService discountService = new DiscountService();
        PaymentService paymentService = new PaymentService();
        StockManager stockManager = new StockManager(productRepository);
        NotificationService emailService = new EmailNotificationService();
        NotificationService smsService = new SmsNotificationService();
        LoggingService loggingService = new LoggingService();
        AnalyticsService analyticsService = new AnalyticsService();

        // Create OrderService with dependencies
        OrderService service = new OrderService(
                productRepository,
                orderRepository,
                discountService,
                paymentService,
                stockManager,
                emailService,
                smsService,
                loggingService,
                analyticsService
        );

        String orderId = service.createOrder(
                "CUST-001",
                "john@example.com",
                Arrays.asList("P001", "P002"),
                "CREDIT_CARD",
                "123 Main St, City"
        );

        if (orderId != null) {
            System.out.println("\n=== Order Created: " + orderId + " ===\n");
            service.shipOrder(orderId, "TRACK-12345");
        }

        System.out.println("\n=== All Orders ===");
        service.printAllOrders();
    }
}

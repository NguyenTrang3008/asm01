package com.ecommerce.service;

import com.ecommerce.model.Order;
import com.ecommerce.model.OrderItem;
import com.ecommerce.model.OrderStatus;
import com.ecommerce.model.Product;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.service.notification.NotificationService;

import java.util.ArrayList;
import java.util.List;

public class OrderService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final DiscountService discountService;
    private final PaymentService paymentService;
    private final StockManager stockManager;
    private final NotificationService emailService;
    private final NotificationService smsService;
    private final LoggingService loggingService;
    private final AnalyticsService analyticsService;

    public OrderService(ProductRepository productRepository,
                        OrderRepository orderRepository,
                        DiscountService discountService,
                        PaymentService paymentService,
                        StockManager stockManager,
                        NotificationService emailService,
                        NotificationService smsService,
                        LoggingService loggingService,
                        AnalyticsService analyticsService) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.discountService = discountService;
        this.paymentService = paymentService;
        this.stockManager = stockManager;
        this.emailService = emailService;
        this.smsService = smsService;
        this.loggingService = loggingService;
        this.analyticsService = analyticsService;
    }

    public String createOrder(String customerId, String customerEmail, List<String> productIds,
                              String paymentMethod, String shippingAddress) {
        // Validate products and calculate total
        List<OrderItem> orderItems = new ArrayList<>();
        double subtotal = 0;

        for (String productId : productIds) {
            Product product = productRepository.findById(productId).orElse(null);
            if (product == null) {
                System.out.println("ERROR: Product not found: " + productId);
                return null;
            }
            if (!product.isInStock()) {
                System.out.println("ERROR: Out of stock: " + product.getName());
                return null;
            }

            double discountedPrice = discountService.calculateDiscountedPrice(product);
            subtotal += discountedPrice;
            orderItems.add(new OrderItem(productId, product.getName(), discountedPrice));
        }

        // Calculate total with payment fee
        double total = paymentService.calculateTotalWithFee(paymentMethod, subtotal);

        // Reserve stock
        stockManager.reserveStock(productIds);

        // Process payment
        boolean paymentSuccess = paymentService.processPayment(paymentMethod, total);

        if (!paymentSuccess) {
            System.out.println("ERROR: Payment failed!");
            stockManager.releaseStock(productIds);
            return null;
        }

        // Create order
        String orderId = "ORD-" + System.currentTimeMillis();
        Order order = new Order(orderId, customerId, customerEmail, orderItems, total, shippingAddress);
        orderRepository.save(order);

        // Send notifications
        emailService.sendNotification(customerEmail,
                "Order Confirmed - " + orderId + ". Total: $" + String.format("%.2f", total));

        // Log and track
        loggingService.log("Order created: " + orderId + " for customer " + customerId);
        analyticsService.trackOrder(total, paymentMethod);

        return orderId;
    }

    public void cancelOrder(String orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            System.out.println("ERROR: Order not found: " + orderId);
            return;
        }

        if (!order.canBeCancelled()) {
            System.out.println("ERROR: Cannot cancel order in status: " + order.getStatus());
            return;
        }

        order.setStatus(OrderStatus.CANCELLED);

        // Restore stock
        List<String> productIds = order.getItems().stream()
                .map(OrderItem::getProductId)
                .toList();
        stockManager.releaseStock(productIds);

        emailService.sendNotification(order.getCustomerEmail(), "Your order " + orderId + " has been cancelled.");
        loggingService.log("Order cancelled: " + orderId);
    }

    public void shipOrder(String orderId, String trackingNumber) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            return;
        }

        if (!order.canBeShipped()) {
            return;
        }

        order.setStatus(OrderStatus.SHIPPED);

        emailService.sendNotification(order.getCustomerEmail(),
                "Your order " + orderId + " has been shipped! Tracking: " + trackingNumber);
        smsService.sendNotification(order.getCustomerEmail(), "Order shipped: " + trackingNumber);
        loggingService.log("Order shipped: " + orderId);
    }

    public void printAllOrders() {
        for (Order order : orderRepository.findAll()) {
            System.out.println("Order: " + order.getOrderId() + " - Status: " + order.getStatus() +
                    " - Total: $" + String.format("%.2f", order.getTotal()));
        }
    }
}

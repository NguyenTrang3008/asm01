package com.ecommerce.service.notification;

public class EmailNotificationService implements NotificationService {
    @Override
    public void sendNotification(String recipient, String message) {
        System.out.println("[EMAIL] Sending to " + recipient + ": " + message);
    }
}

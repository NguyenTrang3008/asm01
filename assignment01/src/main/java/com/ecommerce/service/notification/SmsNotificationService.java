package com.ecommerce.service.notification;

public class SmsNotificationService implements NotificationService {
    @Override
    public void sendNotification(String recipient, String message) {
        System.out.println("[SMS] Sending to " + recipient + ": " + message);
    }
}

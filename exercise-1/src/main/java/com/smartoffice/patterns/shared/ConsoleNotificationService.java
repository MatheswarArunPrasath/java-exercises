package com.smartoffice.patterns.shared;

public class ConsoleNotificationService implements NotificationService {
    @Override
    public void notifyAutoRelease(String u, int room, String when, String email, String phone) {
        if (email != null && !email.isBlank())
            System.out.println("[Email] To: " + email + " | Booking for Room " + room + " auto-released at " + when);
        if (phone != null && !phone.isBlank())
            System.out.println("[SMS] To: " + phone + " | Booking for Room " + room + " auto-released at " + when);
    }
}

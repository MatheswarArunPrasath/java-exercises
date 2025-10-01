package com.smartoffice.notify;

public class ConsoleNotificationService implements NotificationService {
    @Override
    public void notifyAutoRelease(String username, int roomNo, String when, String email, String phone) {
        if (email != null)
            System.out.println("[Email] To: " + email + " | Subject: Booking Released | Body: Hi " + username
                    + ", your booking for Room " + roomNo + " was auto-released at " + when + ".");
        if (phone != null)
            System.out.println("[SMS] To: " + phone + " | Hi " + username + ", your booking for Room " + roomNo
                    + " was auto-released at " + when + ".");
    }
}

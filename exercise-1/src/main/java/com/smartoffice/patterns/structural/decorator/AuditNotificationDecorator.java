package com.smartoffice.patterns.structural.decorator;

import com.smartoffice.patterns.shared.NotificationService;

public class AuditNotificationDecorator extends NotificationDecorator {
    public AuditNotificationDecorator(NotificationService delegate) {
        super(delegate);
    }

    @Override
    public void notifyAutoRelease(String username, int roomNo, String when, String email, String phone) {
        System.out.println("[AUDIT] notifyAutoRelease -> user=" + username +
                ", room=" + roomNo + ", when=" + when +
                ", email=" + email + ", phone=" + phone);
        delegate.notifyAutoRelease(username, roomNo, when, email, phone);
        System.out.println("[AUDIT] delivered");
    }
}

package com.smartoffice.patterns.structural.decorator;

import com.smartoffice.patterns.shared.ConsoleNotificationService;
import com.smartoffice.patterns.shared.NotificationService;

public class FlakyNotificationService implements NotificationService {
    private final int failFirstN;
    private int attempts = 0;
    private final NotificationService fallback = new ConsoleNotificationService();

    public FlakyNotificationService(int failFirstN) {
        this.failFirstN = Math.max(0, failFirstN);
    }

    @Override
    public void notifyAutoRelease(String username, int roomNo, String when, String email, String phone) {
        attempts++;
        if (attempts <= failFirstN) {
            throw new RuntimeException("Simulated transient failure (" + attempts + ")");
        }
        fallback.notifyAutoRelease(username, roomNo, when, email, phone);
    }
}

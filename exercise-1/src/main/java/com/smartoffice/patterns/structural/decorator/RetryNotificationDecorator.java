package com.smartoffice.patterns.structural.decorator;

import com.smartoffice.patterns.shared.NotificationService;

public class RetryNotificationDecorator extends NotificationDecorator {
    private final int maxAttempts;
    private final long sleepMillis;

    public RetryNotificationDecorator(NotificationService delegate, int maxAttempts) {
        this(delegate, maxAttempts, 0);
    }

    public RetryNotificationDecorator(NotificationService delegate, int maxAttempts, long sleepMillis) {
        super(delegate);
        this.maxAttempts = Math.max(1, maxAttempts);
        this.sleepMillis = Math.max(0, sleepMillis);
    }

    @Override
    public void notifyAutoRelease(String username, int roomNo, String when, String email, String phone) {
        int attempts = 0;
        while (true) {
            attempts++;
            try {
                delegate.notifyAutoRelease(username, roomNo, when, email, phone);
                if (attempts > 1) {
                    System.out.println("[Retry] Success on attempt " + attempts);
                }
                return;
            } catch (RuntimeException ex) {
                if (attempts >= maxAttempts) {
                    System.out.println("[Retry] Failed after " + attempts + " attempts: " + ex.getMessage());
                    throw ex;
                }
                System.out.println("[Retry] Attempt " + attempts + " failed: " + ex.getMessage());
                if (sleepMillis > 0) {
                    try {
                        Thread.sleep(sleepMillis);
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        }
    }
}

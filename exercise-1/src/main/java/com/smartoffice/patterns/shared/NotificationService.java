package com.smartoffice.patterns.shared;

public interface NotificationService {
    void notifyAutoRelease(String username, int roomNo, String when, String email, String phone);
}

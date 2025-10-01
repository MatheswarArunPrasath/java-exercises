package com.smartoffice.notify;

public interface NotificationService {
    void notifyAutoRelease(String username, int roomNo, String when, String email, String phone);
}

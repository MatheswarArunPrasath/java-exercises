package com.smartoffice.patterns.structural.decorator;

import com.smartoffice.patterns.shared.NotificationService;

public abstract class NotificationDecorator implements NotificationService {
    protected final NotificationService delegate;

    protected NotificationDecorator(NotificationService delegate) {
        this.delegate = delegate;
    }
}

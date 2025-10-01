package com.smartoffice.patterns.shared;

import java.time.LocalDateTime;

public class SystemTimeProvider implements TimeProvider {
    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();
    }
}

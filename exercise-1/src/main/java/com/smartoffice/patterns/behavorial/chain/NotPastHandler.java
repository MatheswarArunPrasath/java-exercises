package com.smartoffice.patterns.behavioral.chain;

import com.smartoffice.patterns.shared.TimeProvider;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class NotPastHandler extends ValidationHandler {
    private static final ZoneId ZONE = ZoneId.of("Asia/Kolkata");
    private final TimeProvider time;

    public NotPastHandler(TimeProvider time) {
        this.time = time;
    }

    @Override
    protected void doValidate(BookingInput input) {
        ZonedDateTime now = time.now().atZone(ZONE);
        ZonedDateTime start = ZonedDateTime.of(input.date(), input.start(), ZONE);
        if (start.isBefore(now))
            throw new IllegalArgumentException("Cannot book in the past.");
    }
}

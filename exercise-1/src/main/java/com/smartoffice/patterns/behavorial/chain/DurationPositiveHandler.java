package com.smartoffice.patterns.behavioral.chain;

public class DurationPositiveHandler extends ValidationHandler {
    @Override
    protected void doValidate(BookingInput input) {
        if (input.durationMinutes() <= 0)
            throw new IllegalArgumentException("Invalid duration. Must be positive minutes.");
    }
}

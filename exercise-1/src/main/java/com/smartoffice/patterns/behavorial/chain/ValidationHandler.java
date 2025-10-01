package com.smartoffice.patterns.behavioral.chain;

public abstract class ValidationHandler {
    private ValidationHandler next;

    public ValidationHandler linkWith(ValidationHandler next) {
        this.next = next;
        return next;
    }

    public final void validate(BookingInput input) {
        doValidate(input);
        if (next != null)
            next.validate(input);
    }

    protected abstract void doValidate(BookingInput input);
}

package com.smartoffice.util;

public final class Validation {
    private Validation() {
    }

    public static void positive(int n, String field) {
        if (n <= 0)
            throw new IllegalArgumentException("Invalid " + field + ". Please enter a valid positive number.");
    }
}

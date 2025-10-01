package com.smartoffice.patterns.shared;

public class LinearTemperaturePolicyLite {
    private final int minC, maxC;

    public LinearTemperaturePolicyLite(int minC, int maxC) {
        if (minC >= maxC)
            throw new IllegalArgumentException("minC < maxC required");
        this.minC = minC;
        this.maxC = maxC;
    }

    public int target(int headcount, int capacity) {
        if (capacity <= 0)
            return maxC;
        int hc = Math.max(0, Math.min(headcount, capacity));
        double f = ((double) hc) / capacity;
        return (int) Math.round(maxC - f * (maxC - minC));
    }
}

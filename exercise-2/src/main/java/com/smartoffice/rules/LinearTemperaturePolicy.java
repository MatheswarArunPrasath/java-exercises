package com.smartoffice.rules;

public class LinearTemperaturePolicy implements ACTemperaturePolicy {
    private final int minC;
    private final int maxC;

    public LinearTemperaturePolicy() {
        this(18, 26);
    }

    public LinearTemperaturePolicy(int minC, int maxC) {
        if (minC >= maxC)
            throw new IllegalArgumentException("minC must be < maxC");
        this.minC = minC;
        this.maxC = maxC;
    }

    @Override
    public int targetTemp(int headcount, int capacity) {
        if (capacity <= 0)
            return maxC;
        int hc = Math.max(0, Math.min(headcount, capacity));
        double fraction = ((double) hc) / capacity;
        double temp = maxC - fraction * (maxC - minC);
        return (int) Math.round(temp);
    }

    public int minC() {
        return minC;
    }

    public int maxC() {
        return maxC;
    }
}

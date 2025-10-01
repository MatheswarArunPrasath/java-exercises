package com.smartoffice.sensors;

public class TurnstileSensor implements Sensor {
    private int count = 0;

    @Override
    public synchronized void registerDelta(int delta) {
        int next = count + delta;
        if (next < 0)
            next = 0;
        count = next;
    }

    @Override
    public synchronized int currentCount() {
        return count;
    }
}

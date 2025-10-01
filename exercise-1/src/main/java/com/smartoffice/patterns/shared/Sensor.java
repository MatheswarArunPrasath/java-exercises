package com.smartoffice.patterns.shared;

public interface Sensor {
    void registerDelta(int delta);

    int currentCount();
}

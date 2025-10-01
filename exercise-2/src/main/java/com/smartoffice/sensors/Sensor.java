package com.smartoffice.sensors;

public interface Sensor {
    void registerDelta(int delta);

    int currentCount();
}

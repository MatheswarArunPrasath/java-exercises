package com.smartoffice.sensors;

import com.smartoffice.core.Office;
import com.smartoffice.core.Room;
import com.smartoffice.rules.OccupancyRule;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SensorRegistry {
    private final Office office;
    private final OccupancyRule rule;
    private final Map<Integer, Sensor> sensors = new ConcurrentHashMap<>();

    public SensorRegistry(Office office, OccupancyRule rule) {
        this.office = office;
        this.rule = rule;
    }

    private Sensor sensorFor(int roomNo) {
        return sensors.computeIfAbsent(roomNo, rn -> new TurnstileSensor());
    }

    public void registerEntry(int roomNo, int count) {
        if (count <= 0)
            throw new IllegalArgumentException("Invalid entry count.");
        Room r = office.getRoomOrThrow(roomNo);
        Sensor s = sensorFor(roomNo);
        s.registerDelta(count);
        int validated = Math.min(s.currentCount(), r.getCapacity());
        r.setHeadcount(validated);
    }

    public void registerExit(int roomNo, int count) {
        if (count <= 0)
            throw new IllegalArgumentException("Invalid exit count.");
        Room r = office.getRoomOrThrow(roomNo);
        Sensor s = sensorFor(roomNo);
        s.registerDelta(-count);
        int validated = Math.min(Math.max(0, s.currentCount()), r.getCapacity());
        r.setHeadcount(validated);
    }
}

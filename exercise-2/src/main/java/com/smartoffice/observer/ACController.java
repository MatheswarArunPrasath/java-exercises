package com.smartoffice.observer;

import com.smartoffice.core.Room;
import com.smartoffice.rules.ACTemperaturePolicy;
import com.smartoffice.rules.LinearTemperaturePolicy;

import java.util.logging.Logger;

public class ACController implements OccupancyObserver, HeadcountObserver {
    private static final Logger LOG = Logger.getLogger(ACController.class.getName());

    private final Room room;
    private final ACTemperaturePolicy policy;
    private volatile boolean on = false;
    private volatile int currentTemp;

    public ACController(Room room) {
        this(room, new LinearTemperaturePolicy());
    }

    public ACController(Room room, ACTemperaturePolicy policy) {
        this.room = room;
        this.policy = policy;
        this.currentTemp = (policy instanceof LinearTemperaturePolicy)
                ? ((LinearTemperaturePolicy) policy).maxC()
                : 26;
    }

    @Override
    public void onOccupancyChange(boolean occupied) {
        on = occupied;
        if (on) {
            adjustTemperature(room.getHeadcount(), room.getCapacity());
            LOG.info(() -> "AC ON in Room " + room.getRoomNo() + " (Temp " + currentTemp + "°C)");
        } else {
            LOG.info(() -> "AC OFF in Room " + room.getRoomNo());
        }
    }

    @Override
    public void onHeadcountChange(int headcount) {
        if (!on)
            return;
        adjustTemperature(headcount, room.getCapacity());
        LOG.info(() -> "AC temp adjusted in Room " + room.getRoomNo() + " to " + currentTemp + "°C (headcount="
                + headcount + ")");
    }

    private void adjustTemperature(int headcount, int capacity) {
        this.currentTemp = policy.targetTemp(headcount, capacity);
    }

    public boolean isOn() {
        return on;
    }

    public int getCurrentTemp() {
        return currentTemp;
    }
}

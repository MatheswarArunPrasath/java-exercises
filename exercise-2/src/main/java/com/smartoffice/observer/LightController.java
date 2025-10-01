package com.smartoffice.observer;

import com.smartoffice.core.Room;
import java.util.logging.Logger;

public class LightController implements OccupancyObserver {
    private static final Logger LOG = Logger.getLogger(LightController.class.getName());
    private final Room room;
    private volatile boolean on = false;

    public LightController(Room room) {
        this.room = room;
    }

    @Override
    public void onOccupancyChange(boolean occupied) {
        on = occupied;
        LOG.info(() -> (on ? "Lights ON" : "Lights OFF") + " in Room " + room.getRoomNo());
    }

    public boolean isOn() {
        return on;
    }
}

package com.smartoffice.observer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class OccupancySubject {
    private final List<OccupancyObserver> observers = new CopyOnWriteArrayList<>();
    private volatile boolean occupied = false;

    public void addObserver(OccupancyObserver o) {
        observers.add(o);
    }

    public void removeObserver(OccupancyObserver o) {
        observers.remove(o);
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean newState) {
        if (this.occupied != newState) {
            this.occupied = newState;
            for (OccupancyObserver o : observers)
                o.onOccupancyChange(newState);
        }
    }
}

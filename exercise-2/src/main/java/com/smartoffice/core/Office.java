package com.smartoffice.core;

import com.smartoffice.observer.ACController;
import com.smartoffice.observer.LightController;
import com.smartoffice.services.TimeProvider;
import com.smartoffice.usage.UsageTracker;
import com.smartoffice.util.Validation;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.smartoffice.AppContext.usage;

public final class Office {
    private static volatile Office INSTANCE;
    private final TimeProvider timeProvider;
    private final Map<Integer, Room> rooms = new ConcurrentHashMap<>();

    private Office(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }

    public static Office getInstance(TimeProvider timeProvider) {
        if (INSTANCE == null) {
            synchronized (Office.class) {
                if (INSTANCE == null)
                    INSTANCE = new Office(timeProvider);
            }
        }
        return INSTANCE;
    }

    public synchronized void configureRooms(int count) {
        Validation.positive(count, "room count");
        rooms.clear();
        for (int i = 1; i <= count; i++) {
            Room r = new Room(i, timeProvider);

            ACController ac = new ACController(r);
            LightController light = new LightController(r);

            r.getOccupancySubject().addObserver(ac);
            r.getOccupancySubject().addObserver(light);
            r.setControllers(ac, light);

            UsageTracker tracker = new UsageTracker(usage(), i, () -> timeProvider.now());
            r.getHeadcountSubject().addObserver(tracker);
            r.getOccupancySubject().addObserver(tracker);

            rooms.put(i, r);
        }
    }

    public Room getRoomOrThrow(int roomNo) {
        Room r = rooms.get(roomNo);
        if (r == null)
            throw new IllegalArgumentException("Room " + roomNo + " does not exist.");
        return r;
    }

    public Collection<Room> allRooms() {
        return Collections.unmodifiableCollection(rooms.values());
    }
}

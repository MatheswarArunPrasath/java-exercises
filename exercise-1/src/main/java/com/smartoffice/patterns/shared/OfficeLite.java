package com.smartoffice.patterns.shared;

import java.util.*;

public class OfficeLite {
    private final TimeProvider time;
    private final Map<Integer, RoomLite> rooms = new HashMap<>();

    public OfficeLite(TimeProvider time) {
        this.time = time;
    }

    public void configureRooms(int n) {
        rooms.clear();
        for (int i = 1; i <= n; i++)
            rooms.put(i, new RoomLite(i, time));
    }

    public RoomLite getRoomOrThrow(int rn) {
        var r = rooms.get(rn);
        if (r == null)
            throw new IllegalArgumentException("Room " + rn + " does not exist.");
        return r;
    }
}

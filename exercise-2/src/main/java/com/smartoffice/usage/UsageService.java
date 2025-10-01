package com.smartoffice.usage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UsageService {
    private final Map<Integer, RoomUsage> byRoom = new ConcurrentHashMap<>();

    public RoomUsage forRoom(int roomNo) {
        return byRoom.computeIfAbsent(roomNo, RoomUsage::new);
    }

    public Map<Integer, RoomUsage> snapshot() {
        return Map.copyOf(byRoom);
    }
}

package com.smartoffice.patterns.shared;

import java.time.*;

public class RoomLite {
    private static final ZoneId ZONE = ZoneId.of("Asia/Kolkata");
    private final int roomNo;
    private int capacity = 10;
    private int headcount = 0;
    private BookingLite current;
    private final TimeProvider time;

    public RoomLite(int roomNo, TimeProvider time) {
        this.roomNo = roomNo;
        this.time = time;
    }

    public int getRoomNo() {
        return roomNo;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int c) {
        if (c <= 0)
            throw new IllegalArgumentException("capacity>0");
        capacity = c;
    }

    public int getHeadcount() {
        return headcount;
    }

    public void setHeadcount(int n) {
        headcount = Math.max(0, Math.min(n, capacity));
    }

    public BookingLite getCurrent() {
        return current;
    }

    public String block(LocalDate date, LocalTime startTime, int durMin, String owner) {
        ZonedDateTime now = time.now().atZone(ZONE);
        ZonedDateTime start = ZonedDateTime.of(date, startTime, ZONE);
        if (start.isBefore(now))
            throw new IllegalArgumentException("Cannot book in the past.");
        if (durMin <= 0)
            throw new IllegalArgumentException("Invalid duration. Must be positive minutes.");
        if (current != null && current.overlaps(start, durMin))
            return "Room " + roomNo + " is already booked during this time. Cannot book.";
        current = new BookingLite(roomNo, start, durMin, owner);
        return "Room " + roomNo + " booked from " + startTime + " for " + durMin + " minutes.";
    }
}

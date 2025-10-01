package com.smartoffice.core;

import java.time.ZonedDateTime;

public class Booking {
    private final int roomNo;
    private final ZonedDateTime start;
    private final int durationMinutes;
    private final String owner;
    private volatile BookingState state = BookingState.PENDING_OCCUPANCY;

    public Booking(int roomNo, ZonedDateTime start, int durationMinutes, String owner) {
        this.roomNo = roomNo;
        this.start = start;
        this.durationMinutes = durationMinutes;
        this.owner = owner;
    }

    public int getRoomNo() {
        return roomNo;
    }

    public ZonedDateTime getStart() {
        return start;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public BookingState getState() {
        return state;
    }

    public void setState(BookingState s) {
        this.state = s;
    }

    public String getOwner() {
        return owner;
    }

    public ZonedDateTime getEnd() {
        return start.plusMinutes(durationMinutes);
    }

    public boolean overlaps(ZonedDateTime otherStart, int otherDurationMinutes) {
        ZonedDateTime thisEnd = getEnd();
        ZonedDateTime otherEnd = otherStart.plusMinutes(otherDurationMinutes);
        return !thisEnd.isBefore(otherStart) && !otherEnd.isBefore(start);
    }
}

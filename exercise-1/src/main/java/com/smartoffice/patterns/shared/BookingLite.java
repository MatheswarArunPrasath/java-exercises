package com.smartoffice.patterns.shared;

import java.time.ZonedDateTime;

public class BookingLite {
    private final int roomNo;
    private final ZonedDateTime start;
    private final int durationMin;
    private final String owner;

    public BookingLite(int roomNo, ZonedDateTime start, int durationMin, String owner) {
        this.roomNo = roomNo;
        this.start = start;
        this.durationMin = durationMin;
        this.owner = owner;
    }

    public ZonedDateTime getStart() {
        return start;
    }

    public ZonedDateTime getEnd() {
        return start.plusMinutes(durationMin);
    }

    public boolean overlaps(ZonedDateTime otherStart, int otherDur) {
        ZonedDateTime aStart = start, aEnd = getEnd();
        ZonedDateTime bStart = otherStart, bEnd = otherStart.plusMinutes(otherDur);
        // Overlap if intervals intersect with positive length
        return aStart.isBefore(bEnd) && bStart.isBefore(aEnd);
    }
}

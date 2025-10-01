package com.smartoffice.usage;

import java.time.Duration;
import java.time.LocalDateTime;

public class RoomUsage {
    private final int roomNo;
    private long totalOccupiedMinutes = 0;
    private int peakHeadcount = 0;
    private int bookings = 0;

    private boolean currentlyOccupied = false;
    private LocalDateTime occupiedSince = null;

    public RoomUsage(int roomNo) {
        this.roomNo = roomNo;
    }

    public synchronized void onHeadcount(int headcount) {
        if (headcount > peakHeadcount)
            peakHeadcount = headcount;
    }

    public synchronized void onOccupiedChange(boolean nowOccupied, LocalDateTime now) {
        if (nowOccupied && !currentlyOccupied) {
            currentlyOccupied = true;
            occupiedSince = now;
        } else if (!nowOccupied && currentlyOccupied) {
            if (occupiedSince != null) {
                totalOccupiedMinutes += Duration.between(occupiedSince, now).toMinutes();
            }
            currentlyOccupied = false;
            occupiedSince = null;
        }
    }

    public synchronized void onBooked() {
        bookings++;
    }

    public int getRoomNo() {
        return roomNo;
    }

    public synchronized long getTotalOccupiedMinutes() {
        return totalOccupiedMinutes;
    }

    public synchronized int getPeakHeadcount() {
        return peakHeadcount;
    }

    public synchronized int getBookings() {
        return bookings;
    }
}

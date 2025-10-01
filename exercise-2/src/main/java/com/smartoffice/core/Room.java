package com.smartoffice.core;

import com.smartoffice.observer.HeadcountSubject;
import com.smartoffice.observer.OccupancySubject;
import com.smartoffice.observer.ACController;
import com.smartoffice.observer.LightController;
import com.smartoffice.services.TimeProvider;
import com.smartoffice.util.Validation;

import java.time.*;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class Room {
    private static final ZoneId ZONE = ZoneId.of("Asia/Kolkata");
    private final int roomNo;
    private final TimeProvider timeProvider;

    private final OccupancySubject occupancySubject = new OccupancySubject();
    private final HeadcountSubject headcountSubject = new HeadcountSubject();

    private final AtomicInteger capacity = new AtomicInteger(10);
    private final AtomicInteger headcount = new AtomicInteger(0);

    private volatile Booking currentBooking;

    private ACController acController;
    private LightController lightController;

    public Room(int roomNo, TimeProvider timeProvider) {
        this.roomNo = roomNo;
        this.timeProvider = timeProvider;
    }

    public int getRoomNo() {
        return roomNo;
    }

    public int getCapacity() {
        return capacity.get();
    }

    public void setCapacity(int cap) {
        Validation.positive(cap, "capacity");
        capacity.set(cap);
    }

    public int getHeadcount() {
        return headcount.get();
    }

    public OccupancySubject getOccupancySubject() {
        return occupancySubject;
    }

    public HeadcountSubject getHeadcountSubject() {
        return headcountSubject;
    }

    public void setControllers(ACController ac, LightController light) {
        this.acController = ac;
        this.lightController = light;
        this.headcountSubject.addObserver(ac);
    }

    public synchronized void setHeadcount(int people) {
        if (people < 0)
            throw new IllegalArgumentException("Invalid occupancy.");
        if (people > getCapacity())
            throw new IllegalArgumentException("Exceeds room capacity.");
        headcount.set(people);
        headcountSubject.setHeadcount(people);

        boolean nowOccupied = people >= 2;
        boolean wasOccupied = occupancySubject.isOccupied();
        occupancySubject.setOccupied(nowOccupied);

        if (nowOccupied && !wasOccupied && currentBooking != null
                && currentBooking.getState() == BookingState.PENDING_OCCUPANCY) {
            currentBooking.setState(BookingState.ACTIVE);
        }
    }

    public synchronized String block(java.time.LocalDate dateOrNull, java.time.LocalTime startTime, int durationMinutes,
            String ownerUsername) {
        Validation.positive(durationMinutes, "duration");
        if (startTime == null)
            throw new IllegalArgumentException("Invalid time format.");
        ZonedDateTime now = timeProvider.now().atZone(ZONE);
        LocalDate date = (dateOrNull != null) ? dateOrNull : now.toLocalDate();
        ZonedDateTime startDT = ZonedDateTime.of(date, startTime, ZONE);
        if (startDT.isBefore(now))
            throw new IllegalArgumentException("Cannot book in the past.");
        if (currentBooking != null && currentBooking.overlaps(startDT, durationMinutes)) {
            return "Room " + roomNo + " is already booked during this time. Cannot book.";
        }
        currentBooking = new Booking(roomNo, startDT, durationMinutes, ownerUsername);
        return "Room " + roomNo + " booked from " + startTime + " for " + durationMinutes + " minutes.";
    }

    public synchronized String cancel() {
        if (currentBooking == null || currentBooking.getState() == BookingState.RELEASED
                || currentBooking.getState() == BookingState.FINISHED) {
            return "Room " + roomNo + " is not booked. Cannot cancel booking.";
        }
        currentBooking.setState(BookingState.RELEASED);
        currentBooking = null;
        return "Booking for Room " + roomNo + " cancelled successfully.";
    }

    public Optional<Booking> getCurrentBooking() {
        return Optional.ofNullable(currentBooking);
    }

    public synchronized void finishIfOver() {
        if (currentBooking == null)
            return;
        if (currentBooking.getState() == BookingState.RELEASED || currentBooking.getState() == BookingState.FINISHED)
            return;
        ZonedDateTime now = timeProvider.now().atZone(ZONE);
        if (!now.isBefore(currentBooking.getEnd())) {
            currentBooking.setState(BookingState.FINISHED);
            occupancySubject.setOccupied(false);
        }
    }

    public synchronized boolean autoReleaseIfNoShow(java.time.Duration threshold) {
        if (currentBooking == null)
            return false;
        if (currentBooking.getState() != BookingState.PENDING_OCCUPANCY)
            return false;
        java.time.ZonedDateTime now = timeProvider.now().atZone(ZONE);
        if (!now.isAfter(currentBooking.getStart()))
            return false;
        java.time.Duration sinceStart = java.time.Duration.between(currentBooking.getStart(), now);
        if (sinceStart.compareTo(threshold) >= 0) {
            currentBooking.setState(BookingState.RELEASED);
            currentBooking = null;
            occupancySubject.setOccupied(false);
            return true;
        }
        return false;
    }

    public synchronized String status() {
        if (autoReleaseIfNoShow(java.time.Duration.ofMinutes(5))) {
            return "Room " + roomNo + " is now unoccupied. Booking released. AC and lights off.";
        }
        finishIfOver();

        String bookingStr = (currentBooking == null)
                ? "No booking"
                : currentBooking.getState() + " (start: " +
                        currentBooking.getStart().toLocalTime() + ", for " + currentBooking.getDurationMinutes() + "m)";

        String acPart = (acController == null || !acController.isOn())
                ? "AC=OFF"
                : "AC=ON, Temp=" + acController.getCurrentTemp() + "°C";

        return String.format(
                "Room %d | Capacity=%d | Headcount=%d (%s) | %s | %s",
                roomNo, getCapacity(), getHeadcount(),
                occupancySubject.isOccupied() ? "OCCUPIED" : "UNOCCUPIED",
                bookingStr, acPart);
    }
}

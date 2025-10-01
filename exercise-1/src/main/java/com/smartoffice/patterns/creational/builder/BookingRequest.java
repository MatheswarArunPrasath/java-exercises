package com.smartoffice.patterns.creational.builder;

import java.time.LocalDate;
import java.time.LocalTime;

public final class BookingRequest {
    private final int roomNo;
    private final LocalDate date;
    private final LocalTime start;
    private final int durationMinutes;
    private final String owner;
    private final String purpose;
    private final Integer attendees;

    BookingRequest(int roomNo, LocalDate date, LocalTime start, int durationMinutes,
            String owner, String purpose, Integer attendees) {
        this.roomNo = roomNo;
        this.date = date;
        this.start = start;
        this.durationMinutes = durationMinutes;
        this.owner = owner;
        this.purpose = purpose;
        this.attendees = attendees;
    }

    public int roomNo() {
        return roomNo;
    }

    public LocalDate date() {
        return date;
    }

    public LocalTime start() {
        return start;
    }

    public int durationMinutes() {
        return durationMinutes;
    }

    public String owner() {
        return owner;
    }

    public String purpose() {
        return purpose;
    }

    public Integer attendees() {
        return attendees;
    }

    @Override
    public String toString() {
        return "BookingRequest{room=" + roomNo + ", " + date + " " + start +
                ", dur=" + durationMinutes + "m, owner=" + owner +
                (purpose != null ? ", purpose=" + purpose : "") +
                (attendees != null ? ", attendees=" + attendees : "") + "}";
    }
}

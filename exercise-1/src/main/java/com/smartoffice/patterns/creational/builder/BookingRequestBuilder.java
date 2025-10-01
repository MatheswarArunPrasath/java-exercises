package com.smartoffice.patterns.creational.builder;

import java.time.LocalDate;
import java.time.LocalTime;

public final class BookingRequestBuilder {
    private Integer roomNo;
    private LocalDate date;
    private LocalTime start;
    private Integer durationMinutes;
    private String owner;
    private String purpose;
    private Integer attendees;

    private BookingRequestBuilder() {
    }

    public static BookingRequestBuilder create() {
        return new BookingRequestBuilder();
    }

    public BookingRequestBuilder roomNo(int n) {
        this.roomNo = n;
        return this;
    }

    public BookingRequestBuilder date(LocalDate d) {
        this.date = d;
        return this;
    }

    public BookingRequestBuilder start(LocalTime t) {
        this.start = t;
        return this;
    }

    public BookingRequestBuilder durationMinutes(int m) {
        this.durationMinutes = m;
        return this;
    }

    public BookingRequestBuilder owner(String o) {
        this.owner = o;
        return this;
    }

    public BookingRequestBuilder purpose(String p) {
        this.purpose = p;
        return this;
    }

    public BookingRequestBuilder attendees(Integer a) {
        this.attendees = a;
        return this;
    }

    public BookingRequest build() {
        if (roomNo == null || date == null || start == null || durationMinutes == null || owner == null)
            throw new IllegalStateException("Missing required fields.");
        if (durationMinutes <= 0)
            throw new IllegalStateException("durationMinutes must be > 0");
        return new BookingRequest(roomNo, date, start, durationMinutes, owner, purpose, attendees);
    }
}

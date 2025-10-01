package com.smartoffice.patterns.shared;

import java.time.LocalDate;
import java.time.LocalTime;

public class BookingManagerLite {
    public String book(RoomLite r, LocalDate date, LocalTime start, int durMin, String owner) {
        return r.block(date, start, durMin, owner);
    }
}

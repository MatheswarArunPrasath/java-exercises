package com.smartoffice.core;

import java.time.LocalDate;
import java.time.LocalTime;

public class BookingManager {
    public String book(Room room, LocalDate dateOrNull, LocalTime start, int durationMin, String owner) {
        return room.block(dateOrNull, start, durationMin, owner);
    }

    public String cancel(Room room) {
        return room.cancel();
    }
}

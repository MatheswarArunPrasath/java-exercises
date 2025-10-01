package com.smartoffice.patterns.behavioral.chain;

import java.time.LocalDate;
import java.time.LocalTime;

public record BookingInput(int roomNo, LocalDate date, LocalTime start, int durationMinutes, String owner) {
}

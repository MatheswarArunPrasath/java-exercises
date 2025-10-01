package com.smartoffice.patterns.behavioral.chain;

import com.smartoffice.patterns.shared.OfficeLite;
import com.smartoffice.patterns.shared.RoomLite;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class NoOverlapHandler extends ValidationHandler {
    private static final ZoneId ZONE = ZoneId.of("Asia/Kolkata");
    private final OfficeLite office;

    public NoOverlapHandler(OfficeLite office) {
        this.office = office;
    }

    @Override
    protected void doValidate(BookingInput input) {
        RoomLite r = office.getRoomOrThrow(input.roomNo());
        var existing = r.getCurrent();
        if (existing != null) {
            ZonedDateTime start = ZonedDateTime.of(input.date(), input.start(), ZONE);
            if (existing.overlaps(start, input.durationMinutes()))
                throw new IllegalArgumentException(
                        "Room " + input.roomNo() + " is already booked during this time. Cannot book.");
        }
    }
}

package com.smartoffice.patterns.behavioral.chain;

import com.smartoffice.patterns.shared.OfficeLite;

public class RoomExistsHandler extends ValidationHandler {
    private final OfficeLite office;

    public RoomExistsHandler(OfficeLite office) {
        this.office = office;
    }

    @Override
    protected void doValidate(BookingInput input) {
        office.getRoomOrThrow(input.roomNo()); // throws if not present
    }
}

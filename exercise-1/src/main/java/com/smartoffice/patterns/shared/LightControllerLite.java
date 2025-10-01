package com.smartoffice.patterns.shared;

public class LightControllerLite {
    private final RoomLite room;
    private boolean on = false;

    public LightControllerLite(RoomLite room) {
        this.room = room;
    }

    public void on() {
        on = true;
        System.out.println("Lights ON in Room " + room.getRoomNo());
    }

    public void off() {
        on = false;
        System.out.println("Lights OFF in Room " + room.getRoomNo());
    }
}

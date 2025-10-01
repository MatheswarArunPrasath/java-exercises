package com.smartoffice.patterns.shared;

public class ACControllerLite {
    private final RoomLite room;
    private final LinearTemperaturePolicyLite policy;
    private boolean on = false;
    private int currentTemp = 26;

    public ACControllerLite(RoomLite room, LinearTemperaturePolicyLite policy) {
        this.room = room;
        this.policy = policy;
    }

    public void on() {
        on = true;
        adjust();
        System.out.println("AC ON in Room " + room.getRoomNo() + " @ " + currentTemp + "°C");
    }

    public void off() {
        on = false;
        System.out.println("AC OFF in Room " + room.getRoomNo());
    }

    public void adjust() {
        if (on) {
            currentTemp = policy.target(room.getHeadcount(), room.getCapacity());
        }
    }
}

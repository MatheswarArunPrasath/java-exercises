package com.smartoffice.patterns.behavioral.state;

import com.smartoffice.patterns.shared.RoomLite;

public class EnergyContext {
    private final RoomLite room;
    private EnergyState state = new UnoccupiedState();
    private int headcount = 0;

    public EnergyContext(RoomLite room) {
        this.room = room;
        print();
    }

    public void peopleEnter(int n) {
        if (n > 0) {
            headcount += n;
            state.peopleEnter(this, n);
            print();
        }
    }

    public void peopleExit(int n) {
        if (n > 0) {
            headcount = Math.max(0, headcount - n);
            state.peopleExit(this, n);
            print();
        }
    }

    public void idleTimeout() {
        state.idleTimeout(this);
        print();
    }

    void setState(EnergyState s) {
        this.state = s;
    }

    public int headcount() {
        return headcount;
    }

    public RoomLite room() {
        return room;
    }

    private void print() {
        System.out.println(
                "Energy mode for Room " + room.getRoomNo() + " -> " + state.name() + " (headcount=" + headcount + ")");
    }
}

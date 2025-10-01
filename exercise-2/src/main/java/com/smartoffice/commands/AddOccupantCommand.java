package com.smartoffice.commands;

import com.smartoffice.core.Office;
import com.smartoffice.core.Room;
import com.smartoffice.security.Role;
import com.smartoffice.util.ConsoleIO;

public class AddOccupantCommand implements Command {
    private final Office office;
    private final ConsoleIO io;
    private final int roomNo;
    private final int people;

    private AddOccupantCommand(Office o, ConsoleIO io, int rn, int p) {
        this.office = o;
        this.io = io;
        this.roomNo = rn;
        this.people = p;
    }

    public static AddOccupantCommand from(String input, Office office, ConsoleIO io) {
        String[] p = input.trim().split("\\s+");
        if (p.length != 4)
            throw new IllegalArgumentException("Usage: Add occupant <roomNo> <count>");
        int rn, cnt;
        try {
            rn = Integer.parseInt(p[2]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid room number. Please enter a valid room number.");
        }
        try {
            cnt = Integer.parseInt(p[3]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid headcount. Please enter a valid positive number.");
        }
        return new AddOccupantCommand(office, io, rn, cnt);
    }

    @Override
    public Role requiredRole() {
        return Role.USER;
    }

    @Override
    public void execute() {
        try {
            Room r = office.getRoomOrThrow(roomNo);
            r.setHeadcount(people);
            if (people >= 2) {
                io.println("Room " + roomNo + " is now occupied by at least 2 persons. AC and lights turned on.");
            } else {
                io.println("Room " + roomNo + " occupancy insufficient to mark as occupied.");
            }
        } catch (IllegalArgumentException ex) {
            String msg = ex.getMessage();
            if (msg.contains("does not exist"))
                io.println("Room " + roomNo + " does not exist.");
            else
                io.println("Error: " + msg);
        }
    }
}

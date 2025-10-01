package com.smartoffice.commands;

import com.smartoffice.core.Office;
import com.smartoffice.core.Room;
import com.smartoffice.security.Role;
import com.smartoffice.util.ConsoleIO;

public class RoomStatusCommand implements Command {
    private final Office office;
    private final ConsoleIO io;
    private final int roomNo;

    private RoomStatusCommand(Office o, ConsoleIO io, int rn) {
        this.office = o;
        this.io = io;
        this.roomNo = rn;
    }

    public static RoomStatusCommand from(String input, Office office, ConsoleIO io) {
        String[] p = input.trim().split("\\s+");
        if (p.length != 3)
            throw new IllegalArgumentException("Usage: Room status <roomNo>");
        try {
            return new RoomStatusCommand(office, io, Integer.parseInt(p[2]));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid room number. Please enter a valid room number.");
        }
    }

    @Override
    public Role requiredRole() {
        return Role.USER;
    }

    @Override
    public void execute() {
        try {
            Room r = office.getRoomOrThrow(roomNo);
            io.println(r.status());
        } catch (IllegalArgumentException ex) {
            io.println(ex.getMessage().contains("does not exist")
                    ? "Room " + roomNo + " does not exist."
                    : "Error: " + ex.getMessage());
        }
    }
}

package com.smartoffice.commands;

import com.smartoffice.core.Office;
import com.smartoffice.core.Room;
import com.smartoffice.security.Role;
import com.smartoffice.util.ConsoleIO;

public class SetRoomCapacityCommand implements Command {
    private final Office office;
    private final ConsoleIO io;
    private final int roomNo;
    private final int capacity;

    private SetRoomCapacityCommand(Office o, ConsoleIO io, int rn, int cap) {
        this.office = o;
        this.io = io;
        this.roomNo = rn;
        this.capacity = cap;
    }

    public static SetRoomCapacityCommand from(String input, Office office, ConsoleIO io) {
        String[] p = input.trim().split("\\s+");
        if (p.length != 6)
            throw new IllegalArgumentException("Usage: Config room max capacity <roomNo> <capacity>");
        int rn;
        int cap;
        try {
            rn = Integer.parseInt(p[4]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid room number. Please enter a valid room number.");
        }
        try {
            cap = Integer.parseInt(p[5]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid capacity. Please enter a valid positive number.");
        }
        return new SetRoomCapacityCommand(office, io, rn, cap);
    }

    @Override
    public Role requiredRole() {
        return Role.ADMIN;
    }

    @Override
    public void execute() {
        if (capacity <= 0) {
            io.println("Invalid capacity. Please enter a valid positive number.");
            return;
        }
        try {
            Room r = office.getRoomOrThrow(roomNo);
            r.setCapacity(capacity);
            io.println("Room " + roomNo + " maximum capacity set to " + capacity + ".");
        } catch (IllegalArgumentException ex) {
            io.println(ex.getMessage().contains("does not exist")
                    ? "Room " + roomNo + " does not exist."
                    : "Error: " + ex.getMessage());
        }
    }
}

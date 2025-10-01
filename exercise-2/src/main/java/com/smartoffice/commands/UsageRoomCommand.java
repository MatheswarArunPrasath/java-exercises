package com.smartoffice.commands;

import com.smartoffice.security.Role;
import com.smartoffice.usage.UsageService;
import com.smartoffice.util.ConsoleIO;

public class UsageRoomCommand implements Command {
    private final UsageService usage;
    private final ConsoleIO io;
    private final int roomNo;

    private UsageRoomCommand(UsageService u, ConsoleIO io, int rn) {
        this.usage = u;
        this.io = io;
        this.roomNo = rn;
    }

    public static UsageRoomCommand from(String input, UsageService usage, ConsoleIO io) {
        String[] p = input.trim().split("\\s+");
        if (p.length != 3)
            throw new IllegalArgumentException("Usage: usage room <roomNo>");
        try {
            return new UsageRoomCommand(usage, io, Integer.parseInt(p[2]));
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
        var ru = usage.forRoom(roomNo);
        io.println(String.format("Room %d -> bookings: %d, occupied: %d min, peak headcount: %d",
                roomNo, ru.getBookings(), ru.getTotalOccupiedMinutes(), ru.getPeakHeadcount()));
    }
}

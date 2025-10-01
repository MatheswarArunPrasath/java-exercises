package com.smartoffice.commands;

import com.smartoffice.core.Office;
import com.smartoffice.security.Role;
import com.smartoffice.sensors.SensorRegistry;
import com.smartoffice.util.ConsoleIO;

public class SensorExitCommand implements Command {
    private final Office office;
    private final SensorRegistry sensors;
    private final ConsoleIO io;
    private final int roomNo;
    private final int count;

    private SensorExitCommand(Office o, SensorRegistry s, ConsoleIO io, int rn, int c) {
        this.office = o;
        this.sensors = s;
        this.io = io;
        this.roomNo = rn;
        this.count = c;
    }

    public static SensorExitCommand from(String input, Office office, SensorRegistry sensors, ConsoleIO io) {
        String[] p = input.trim().split("\\s+");
        if (p.length != 4)
            throw new IllegalArgumentException("Usage: sensor exit <roomNo> <count>");
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
        return new SensorExitCommand(office, sensors, io, rn, cnt);
    }

    @Override
    public Role requiredRole() {
        return Role.USER;
    }

    @Override
    public void execute() {
        try {
            office.getRoomOrThrow(roomNo);
            sensors.registerExit(roomNo, count);
            io.println(count + " person(s) exited Room " + roomNo + ".");
        } catch (IllegalArgumentException ex) {
            io.println(ex.getMessage().contains("does not exist") ? "Room " + roomNo + " does not exist."
                    : "Error: " + ex.getMessage());
        }
    }
}

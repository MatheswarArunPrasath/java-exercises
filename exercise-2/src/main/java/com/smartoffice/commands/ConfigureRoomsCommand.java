package com.smartoffice.commands;

import com.smartoffice.core.Office;
import com.smartoffice.security.Role;
import com.smartoffice.util.ConsoleIO;

public class ConfigureRoomsCommand implements Command {
    private final Office office;
    private final ConsoleIO io;
    private final int count;

    private ConfigureRoomsCommand(Office office, ConsoleIO io, int count) {
        this.office = office;
        this.io = io;
        this.count = count;
    }

    public static ConfigureRoomsCommand from(String input, Office office, ConsoleIO io) {
        String[] p = input.trim().split("\\s+");
        if (p.length != 4)
            throw new IllegalArgumentException("Usage: config room count <N>");
        return new ConfigureRoomsCommand(office, io, Integer.parseInt(p[3]));
    }

    @Override
    public Role requiredRole() {
        return Role.ADMIN;
    }

    @Override
    public void execute() {
        office.configureRooms(count);
        StringBuilder sb = new StringBuilder("Office configured with " + count + " meeting rooms:\n");
        for (int i = 1; i <= count; i++) {
            sb.append("Room ").append(i);
            if (i < count)
                sb.append(", ");
        }
        sb.append(".");
        io.println(sb.toString());
    }
}

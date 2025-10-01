package com.smartoffice.commands;

import com.smartoffice.core.Office;
import com.smartoffice.core.Room;
import com.smartoffice.security.Role;
import com.smartoffice.util.ConsoleIO;

import java.util.stream.Collectors;

public class RoomsUnoccupiedCommand implements Command {
    private final Office office;
    private final ConsoleIO io;

    public RoomsUnoccupiedCommand(Office o, ConsoleIO io) {
        this.office = o;
        this.io = io;
    }

    public static RoomsUnoccupiedCommand from(Office office, ConsoleIO io) {
        return new RoomsUnoccupiedCommand(office, io);
    }

    @Override
    public Role requiredRole() {
        return Role.USER;
    }

    @Override
    public void execute() {
        var list = office.allRooms().stream()
                .filter(r -> !r.getOccupancySubject().isOccupied())
                .map(Room::getRoomNo)
                .sorted()
                .collect(Collectors.toList());
        if (list.isEmpty()) {
            io.println("No unoccupied rooms.");
        } else {
            io.println("Unoccupied rooms: " + list.stream().map(i -> "Room " + i).collect(Collectors.joining(", ")));
        }
    }
}

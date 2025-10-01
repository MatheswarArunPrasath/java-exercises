package com.smartoffice.commands;

import com.smartoffice.core.BookingManager;
import com.smartoffice.core.Office;
import com.smartoffice.core.Room;
import com.smartoffice.security.AuthService;
import com.smartoffice.security.Role;
import com.smartoffice.util.ConsoleIO;

public class CancelRoomCommand implements Command {
    private final Office office;
    private final BookingManager manager;
    private final ConsoleIO io;
    private final AuthService auth;
    private final int roomNo;

    private CancelRoomCommand(Office o, BookingManager m, ConsoleIO io, AuthService a, int rn) {
        this.office = o;
        this.manager = m;
        this.io = io;
        this.auth = a;
        this.roomNo = rn;
    }

    public static CancelRoomCommand from(String input, Office office, BookingManager manager, ConsoleIO io,
            AuthService auth) {
        String[] p = input.trim().split("\\s+");
        if (p.length != 3)
            throw new IllegalArgumentException("Usage: cancel room <roomNo>");
        try {
            return new CancelRoomCommand(office, manager, io, auth, Integer.parseInt(p[2]));
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
        var current = auth.currentUser();
        if (current == null) {
            io.println("Login required.");
            return;
        }
        try {
            Room r = office.getRoomOrThrow(roomNo);
            var b = r.getCurrentBooking();
            if (b.isEmpty()) {
                io.println("Room " + roomNo + " is not booked. Cannot cancel booking.");
                return;
            }
            var booking = b.get();
            if (!current.getRole().atLeast(Role.ADMIN) && !current.getUsername().equals(booking.getOwner())) {
                io.println("Not allowed. Only owner or admin can cancel this booking.");
                return;
            }
            io.println(manager.cancel(r));
        } catch (IllegalArgumentException ex) {
            io.println(ex.getMessage().contains("does not exist") ? "Room " + roomNo + " does not exist."
                    : "Error: " + ex.getMessage());
        }
    }
}

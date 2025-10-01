package com.smartoffice.commands;

import com.smartoffice.core.BookingManager;
import com.smartoffice.core.Office;
import com.smartoffice.core.Room;
import com.smartoffice.security.AuthService;
import com.smartoffice.security.Role;
import com.smartoffice.util.ConsoleIO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class BookRoomCommand implements Command {
    private final Office office;
    private final BookingManager manager;
    private final ConsoleIO io;
    private final AuthService auth;
    private final Integer roomNo;
    private final LocalDate dateOrNull;
    private final LocalTime start;
    private final int duration;

    private BookRoomCommand(Office o, BookingManager m, ConsoleIO io, AuthService a,
            Integer rn, LocalDate dateOrNull, LocalTime s, int d) {
        this.office = o;
        this.manager = m;
        this.io = io;
        this.auth = a;
        this.roomNo = rn;
        this.dateOrNull = dateOrNull;
        this.start = s;
        this.duration = d;
    }

    public static BookRoomCommand from(String input, Office office, BookingManager manager, ConsoleIO io,
            AuthService auth) {
        String[] p = input.trim().split("\\s+");
        try {
            if (p.length == 5) {
                int rn = Integer.parseInt(p[2]);
                LocalTime st = LocalTime.parse(p[3]);
                int dur = Integer.parseInt(p[4]);
                return new BookRoomCommand(office, manager, io, auth, rn, null, st, dur);
            } else if (p.length == 6) {
                int rn = Integer.parseInt(p[2]);
                LocalDate dt = LocalDate.parse(p[3]);
                LocalTime st = LocalTime.parse(p[4]);
                int dur = Integer.parseInt(p[5]);
                return new BookRoomCommand(office, manager, io, auth, rn, dt, st, dur);
            } else {
                throw new IllegalArgumentException(
                        "Usage: Block room <roomNo> <HH:mm> <durationMin> OR Block room <roomNo> <yyyy-MM-dd> <HH:mm> <durationMin>");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid room number. Please enter a valid room number.");
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid time format. Use HH:mm or date as yyyy-MM-dd.");
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
            io.println(manager.book(r, dateOrNull, start, duration, current.getUsername()));
        } catch (IllegalArgumentException ex) {
            String msg = ex.getMessage();
            if (msg.contains("does not exist"))
                io.println("Room " + roomNo + " does not exist.");
            else if (msg.startsWith("Room ") && msg.contains("already booked"))
                io.println(msg);
            else if (msg.equals("Cannot book in the past."))
                io.println(msg);
            else
                io.println("Error: " + msg);
        }
    }
}

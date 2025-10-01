package com.smartoffice.commands;

import com.smartoffice.security.Role;
import com.smartoffice.usage.RoomUsage;
import com.smartoffice.usage.UsageService;
import com.smartoffice.util.ConsoleIO;

public class UsageSummaryCommand implements Command {
    private final UsageService usage;
    private final ConsoleIO io;

    public UsageSummaryCommand(UsageService usage, ConsoleIO io) {
        this.usage = usage;
        this.io = io;
    }

    public static UsageSummaryCommand from(UsageService usage, ConsoleIO io) {
        return new UsageSummaryCommand(usage, io);
    }

    @Override
    public Role requiredRole() {
        return Role.USER;
    }

    @Override
    public void execute() {
        var snap = usage.snapshot();
        if (snap.isEmpty()) {
            io.println("No usage data yet.");
            return;
        }
        io.println("Room Usage Summary:");
        for (RoomUsage ru : snap.values()) {
            io.println(String.format("Room %d -> bookings: %d, occupied: %d min, peak headcount: %d",
                    ru.getRoomNo(), ru.getBookings(), ru.getTotalOccupiedMinutes(), ru.getPeakHeadcount()));
        }
    }
}

package com.smartoffice;

import com.smartoffice.commands.*;
import com.smartoffice.core.BookingManager;
import com.smartoffice.core.Office;
import com.smartoffice.rules.TwoOrMoreRule;
import com.smartoffice.sensors.SensorRegistry;
import com.smartoffice.security.AuthService;
import com.smartoffice.security.Role;
import com.smartoffice.services.AutoReleaseScheduler;
import com.smartoffice.services.SystemTimeProvider;
import com.smartoffice.notify.ConsoleNotificationService;
import com.smartoffice.util.ConsoleIO;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class App {
    private static final Logger LOG = Logger.getLogger(App.class.getName());

    public static void main(String[] args) {
        try {
            LogManager.getLogManager().readConfiguration();
        } catch (Exception ignored) {
        }
        Logger.getLogger("").setLevel(Level.INFO);

        ConsoleIO io = new ConsoleIO(System.in, System.out);
        var time = new SystemTimeProvider();
        var office = Office.getInstance(time); // Singleton
        var manager = new BookingManager();
        var sensors = new SensorRegistry(office, new TwoOrMoreRule());
        var auth = new AuthService(); // default admin/admin123
        var notifier = new ConsoleNotificationService();

        AutoReleaseScheduler scheduler = new AutoReleaseScheduler(office, time, notifier, auth);
        scheduler.start();

        printHelp(io);

        boolean running = true;
        while (running) {
            io.print("> ");
            String raw = io.readLine();
            if (raw == null)
                break;
            raw = raw.trim();
            if (raw.equalsIgnoreCase("exit")) {
                running = false;
                continue;
            }
            if (raw.equalsIgnoreCase("help")) {
                printHelp(io);
                continue;
            }

            try {
                Command c = parse(raw.toLowerCase(Locale.ROOT), raw, office, manager, sensors, io, auth);
                if (c == null) {
                    io.println("Unrecognized command. Type 'help' for list.");
                    continue;
                }

                var current = auth.currentUser();
                if (current == null && !(c instanceof LoginCommand)) {
                    io.println("Login required.");
                    continue;
                }
                Role have = current == null ? Role.USER : current.getRole();
                if (!have.atLeast(c.requiredRole())) {
                    io.println("Forbidden. Requires role: " + c.requiredRole());
                    continue;
                }

                c.execute();
            } catch (Exception ex) {
                LOG.log(Level.WARNING, "Command failed", ex);
                io.println("Error: " + ex.getMessage());
            }
        }

        scheduler.stop();
        io.println("Goodbye.");
    }

    private static void printHelp(ConsoleIO io) {
        io.println("Smart Office — Commands");
        io.println("Auth:");
        io.println("  login <username> <password>");
        io.println("  logout");
        io.println("  whoami");
        io.println("  change_password <username> <old> <new>");
        io.println("  set_contact <email|- > <phone|- >");
        io.println("Admin:");
        io.println("  create_user <username> <ADMIN|USER> <password>");
        io.println("  list_users");
        io.println("  config room count <N>");
        io.println("  config room max capacity <roomNo> <capacity>");
        io.println("User:");
        io.println("  block room <roomNo> <HH:mm> <durationMin>");
        io.println("  block room <roomNo> <yyyy-MM-dd> <HH:mm> <durationMin>");
        io.println("  cancel room <roomNo>");
        io.println("  add occupant <roomNo> <count>");
        io.println("  sensor enter <roomNo> <count>");
        io.println("  sensor exit <roomNo> <count>");
        io.println("  room status <roomNo>");
        io.println("  rooms unoccupied");
        io.println("Usage:");
        io.println("  usage summary");
        io.println("  usage room <roomNo>");
        io.println("Other:");
        io.println("  help");
        io.println("  exit");
    }

    private static Command parse(String lower, String raw,
            Office office, BookingManager manager,
            SensorRegistry sensors, ConsoleIO io, AuthService auth) {
        if (lower.startsWith("login "))
            return LoginCommand.from(raw, auth, io);
        if (lower.equals("logout"))
            return LogoutCommand.from(auth, io);
        if (lower.equals("whoami"))
            return WhoAmICommand.from(auth, io);
        if (lower.startsWith("create_user"))
            return CreateUserCommand.from(raw, auth, io);
        if (lower.equals("list_users"))
            return ListUsersCommand.from(auth, io);
        if (lower.startsWith("change_password"))
            return ChangePasswordCommand.from(raw, auth, io);
        if (lower.startsWith("set_contact"))
            return SetContactCommand.from(raw, auth, io);

        if (lower.startsWith("config room count"))
            return ConfigureRoomsCommand.from(raw, office, io);
        if (lower.startsWith("config room max capacity"))
            return SetRoomCapacityCommand.from(raw, office, io);

        if (lower.startsWith("block room"))
            return BookRoomCommand.from(raw, office, manager, io, auth);
        if (lower.startsWith("cancel room"))
            return CancelRoomCommand.from(raw, office, manager, io, auth);
        if (lower.startsWith("add occupant"))
            return AddOccupantCommand.from(raw, office, io);
        if (lower.startsWith("sensor enter"))
            return SensorEnterCommand.from(raw, office, sensors, io);
        if (lower.startsWith("sensor exit"))
            return SensorExitCommand.from(raw, office, sensors, io);
        if (lower.startsWith("room status"))
            return RoomStatusCommand.from(raw, office, io);
        if (lower.equals("rooms unoccupied"))
            return RoomsUnoccupiedCommand.from(office, io);

        if (lower.equals("usage summary"))
            return UsageSummaryCommand.from(AppContext.usage(), io);
        if (lower.startsWith("usage room"))
            return UsageRoomCommand.from(raw, AppContext.usage(), io);

        return null;
    }
}

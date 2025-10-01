package com.smartoffice.commands;

import com.smartoffice.security.AuthService;
import com.smartoffice.security.Role;
import com.smartoffice.util.ConsoleIO;

public class SetContactCommand implements Command {
    private final AuthService auth;
    private final ConsoleIO io;
    private final String email;
    private final String phone;

    private SetContactCommand(AuthService a, ConsoleIO io, String email, String phone) {
        this.auth = a;
        this.io = io;
        this.email = email;
        this.phone = phone;
    }

    public static SetContactCommand from(String input, AuthService auth, ConsoleIO io) {
        String[] p = input.trim().split("\\s+");
        if (p.length != 3)
            throw new IllegalArgumentException("Usage: set_contact <email|- > <phone|- >");
        return new SetContactCommand(auth, io, "-".equals(p[1]) ? null : p[1], "-".equals(p[2]) ? null : p[2]);
    }

    @Override
    public Role requiredRole() {
        return Role.USER;
    }

    @Override
    public void execute() {
        var u = auth.currentUser();
        if (u == null) {
            io.println("Login required.");
            return;
        }
        auth.updateUserContacts(u.getUsername(), email, phone);
        io.println("Contact info updated.");
    }
}

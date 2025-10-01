package com.smartoffice.commands;

import com.smartoffice.security.AuthService;
import com.smartoffice.security.Role;
import com.smartoffice.util.ConsoleIO;

public class WhoAmICommand implements Command {
    private final AuthService auth;
    private final ConsoleIO io;

    private WhoAmICommand(AuthService a, ConsoleIO io) {
        this.auth = a;
        this.io = io;
    }

    public static WhoAmICommand from(AuthService a, ConsoleIO io) {
        return new WhoAmICommand(a, io);
    }

    @Override
    public Role requiredRole() {
        return Role.USER;
    }

    @Override
    public void execute() {
        var u = auth.currentUser();
        io.println(u == null ? "Not logged in." : (u.getUsername() + " (" + u.getRole() + ")"));
    }
}

package com.smartoffice.commands;

import com.smartoffice.security.AuthService;
import com.smartoffice.security.Role;
import com.smartoffice.util.ConsoleIO;

public class LogoutCommand implements Command {
    private final AuthService auth;
    private final ConsoleIO io;

    private LogoutCommand(AuthService a, ConsoleIO io) {
        this.auth = a;
        this.io = io;
    }

    public static LogoutCommand from(AuthService a, ConsoleIO io) {
        return new LogoutCommand(a, io);
    }

    @Override
    public Role requiredRole() {
        return Role.USER;
    }

    @Override
    public void execute() {
        auth.logout();
        io.println("Logged out.");
    }
}

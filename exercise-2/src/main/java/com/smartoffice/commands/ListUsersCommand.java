package com.smartoffice.commands;

import com.smartoffice.security.AuthService;
import com.smartoffice.security.Role;
import com.smartoffice.util.ConsoleIO;

public class ListUsersCommand implements Command {
    private final AuthService auth;
    private final ConsoleIO io;

    private ListUsersCommand(AuthService a, ConsoleIO io) {
        this.auth = a;
        this.io = io;
    }

    public static ListUsersCommand from(AuthService a, ConsoleIO io) {
        return new ListUsersCommand(a, io);
    }

    @Override
    public Role requiredRole() {
        return Role.ADMIN;
    }

    @Override
    public void execute() {
        auth.listUsers().values().forEach(u -> io.println(u.getUsername() + " (" + u.getRole() + ")"));
    }
}

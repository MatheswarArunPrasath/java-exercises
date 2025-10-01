package com.smartoffice.commands;

import com.smartoffice.security.AuthService;
import com.smartoffice.security.Role;
import com.smartoffice.util.ConsoleIO;

public class LoginCommand implements Command {
    private final AuthService auth;
    private final ConsoleIO io;
    private final String username;
    private final String password;

    private LoginCommand(AuthService a, ConsoleIO io, String u, String p) {
        this.auth = a;
        this.io = io;
        this.username = u;
        this.password = p;
    }

    public static LoginCommand from(String input, AuthService auth, ConsoleIO io) {
        String[] p = input.trim().split("\\s+");
        if (p.length != 3)
            throw new IllegalArgumentException("Usage: login <username> <password>");
        return new LoginCommand(auth, io, p[1], p[2]);
    }

    @Override
    public Role requiredRole() {
        return Role.USER;
    }

    @Override
    public void execute() {
        if (auth.login(username, password))
            io.println("Logged in as " + username + ".");
        else
            io.println("Invalid credentials.");
    }
}

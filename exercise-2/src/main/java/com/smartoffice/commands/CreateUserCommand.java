package com.smartoffice.commands;

import com.smartoffice.security.AuthService;
import com.smartoffice.security.Role;
import com.smartoffice.util.ConsoleIO;

public class CreateUserCommand implements Command {
    private final AuthService auth;
    private final ConsoleIO io;
    private final String username;
    private final Role role;
    private final String password;

    private CreateUserCommand(AuthService a, ConsoleIO io, String u, Role r, String pw) {
        this.auth = a;
        this.io = io;
        this.username = u;
        this.role = r;
        this.password = pw;
    }

    public static CreateUserCommand from(String input, AuthService auth, ConsoleIO io) {
        String[] p = input.trim().split("\\s+");
        if (p.length != 4)
            throw new IllegalArgumentException("Usage: create_user <username> <ADMIN|USER> <password>");
        Role r = Role.valueOf(p[2].toUpperCase());
        return new CreateUserCommand(auth, io, p[1], r, p[3]);
    }

    @Override
    public Role requiredRole() {
        return Role.ADMIN;
    }

    @Override
    public void execute() {
        if (auth.createUser(username, role, password))
            io.println("User created: " + username + " (" + role + ")");
        else
            io.println("User already exists: " + username);
    }
}

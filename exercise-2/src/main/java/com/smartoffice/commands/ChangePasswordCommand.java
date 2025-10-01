package com.smartoffice.commands;

import com.smartoffice.security.AuthService;
import com.smartoffice.security.Role;
import com.smartoffice.security.User;
import com.smartoffice.util.ConsoleIO;

public class ChangePasswordCommand implements Command {
    private final AuthService auth;
    private final ConsoleIO io;
    private final String username;
    private final String oldPw;
    private final String newPw;

    private ChangePasswordCommand(AuthService a, ConsoleIO io, String u, String o, String n) {
        this.auth = a;
        this.io = io;
        this.username = u;
        this.oldPw = o;
        this.newPw = n;
    }

    public static ChangePasswordCommand from(String input, AuthService auth, ConsoleIO io) {
        String[] p = input.trim().split("\\s+");
        if (p.length != 4)
            throw new IllegalArgumentException("Usage: change_password <username> <old> <new>");
        return new ChangePasswordCommand(auth, io, p[1], p[2], p[3]);
    }

    @Override
    public Role requiredRole() {
        return Role.USER;
    }

    @Override
    public void execute() {
        User current = auth.currentUser();
        if (current == null) {
            io.println("Login required.");
            return;
        }
        if (!current.getUsername().equals(username) && current.getRole() != Role.ADMIN) {
            io.println("Not allowed. You can change only your own password.");
            return;
        }
        boolean ok = auth.changePassword(username, oldPw, newPw);
        io.println(ok ? "Password changed." : "Password change failed.");
    }
}

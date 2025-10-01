package com.smartoffice.commands;

import com.smartoffice.security.Role;

public interface Command {
    Role requiredRole();

    void execute();
}

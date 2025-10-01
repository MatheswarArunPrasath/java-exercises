package com.smartoffice.security;

public enum Role {
    USER, ADMIN;

    public boolean atLeast(Role required) {
        return this.ordinal() >= required.ordinal();
    }
}

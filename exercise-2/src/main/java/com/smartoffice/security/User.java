package com.smartoffice.security;

public class User {
    private final String username;
    private final Role role;
    private final String passwordHash;
    private final String email;
    private final String phone;

    public User(String username, Role role, String passwordHash) {
        this(username, role, passwordHash, null, null);
    }

    public User(String username, Role role, String passwordHash, String email, String phone) {
        this.username = username;
        this.role = role;
        this.passwordHash = passwordHash;
        this.email = email;
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public Role getRole() {
        return role;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}

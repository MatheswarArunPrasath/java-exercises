package com.smartoffice.security;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AuthService {
    private final Map<String, User> users = new ConcurrentHashMap<>();
    private volatile User current;

    public AuthService() {
        String adminHash = PasswordHasher.hash("admin123");
        users.put("admin", new User("admin", Role.ADMIN, adminHash, "admin@example.com", null));
    }

    public synchronized boolean login(String username, String password) {
        User u = users.get(username);
        if (u == null)
            return false;
        if (!PasswordHasher.verify(password, u.getPasswordHash()))
            return false;
        current = u;
        return true;
    }

    public synchronized void logout() {
        current = null;
    }

    public User currentUser() {
        return current;
    }

    public synchronized boolean createUser(String username, Role role, String password) {
        if (users.containsKey(username))
            return false;
        users.put(username, new User(username, role, PasswordHasher.hash(password)));
        return true;
    }

    public synchronized boolean changePassword(String username, String oldPw, String newPw) {
        User u = users.get(username);
        if (u == null)
            return false;
        if (!PasswordHasher.verify(oldPw, u.getPasswordHash()))
            return false;
        users.put(username,
                new User(u.getUsername(), u.getRole(), PasswordHasher.hash(newPw), u.getEmail(), u.getPhone()));
        return true;
    }

    public Map<String, User> listUsers() {
        return Map.copyOf(users);
    }

    public User getUser(String username) {
        return users.get(username);
    }

    public synchronized void updateUserContacts(String username, String email, String phone) {
        User u = users.get(username);
        if (u == null)
            return;
        users.put(username, new User(u.getUsername(), u.getRole(), u.getPasswordHash(), email, phone));
    }
}

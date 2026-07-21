package reservationsystem.service;

import reservationsystem.model.User;

import java.util.List;

public class AuthenticationService {
    private User currentUser;

    public AuthenticationResult login(String username, String password, List<User> savedUsers) {
        if (username == null || username.isBlank()) {
            return AuthenticationResult.failure("Username is required");
        }

        if (password == null || password.isBlank()) {
            return AuthenticationResult.failure("Password is required");
        }

        if (savedUsers == null) {
            return AuthenticationResult.failure("Saved users cannot be null");
        }

        for (User user : savedUsers) {
            if (user.getUsername().equals(username.trim())
                    && user.getPassword().equals(password)) {
                currentUser = user;
                return AuthenticationResult.success(user);
            }
        }

        return AuthenticationResult.failure("Invalid username or password");
    }

    public void logout() {
        currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }
}

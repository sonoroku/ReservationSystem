package reservationsystem.service;

import reservationsystem.model.User;

public class AuthenticationResult {
    private final boolean successful;
    private final String message;
    private final User user;

    private AuthenticationResult(boolean successful, String message, User user) {
        this.successful = successful;
        this.message = message;
        this.user = user;
    }

    public static AuthenticationResult success(User user) {
        return new AuthenticationResult(true, "Login successful", user);
    }

    public static AuthenticationResult failure(String message) {
        return new AuthenticationResult(false, message, null);
    }

    public boolean isSuccessful() {
        return successful;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }
}

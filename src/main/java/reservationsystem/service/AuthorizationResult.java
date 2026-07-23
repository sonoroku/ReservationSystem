package reservationsystem.service;

public class AuthorizationResult {

    private final boolean authorized;
    private final String message;

    private AuthorizationResult(boolean authorized, String message) {
        this.authorized = authorized;
        this.message = message;
    }

    public static AuthorizationResult authorized() {
        return new AuthorizationResult(true, "Access granted");
    }

    public static AuthorizationResult denied(String message) {
        return new AuthorizationResult(false, message);
    }

    public boolean isAuthorized() {
        return authorized;
    }

    public String getMessage() {
        return message;
    }
}

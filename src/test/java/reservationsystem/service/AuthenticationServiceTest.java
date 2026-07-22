package reservationsystem.service;

import org.junit.jupiter.api.Test;
import reservationsystem.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AuthenticationServiceTest {

    @Test
    void validCredentialsAuthenticateUser() {
        AuthenticationService service = new AuthenticationService();

        User user = new User("user001", "password123", false);

        AuthenticationResult result = service.login(
                "user001",
                "password123",
                List.of(user)
        );

        assertTrue(result.isSuccessful());
        assertEquals("Login successful", result.getMessage());
        assertEquals(user, result.getUser());
        assertEquals(user, service.getCurrentUser());
        assertTrue(service.isLoggedIn());
    }

    @Test
    void invalidPasswordReturnsErrorMessage() {
        AuthenticationService service = new AuthenticationService();

        User user = new User("user001", "password123", false);

        AuthenticationResult result = service.login(
                "user001",
                "wrongpassword",
                List.of(user)
        );

        assertFalse(result.isSuccessful());
        assertEquals("Invalid username or password", result.getMessage());
        assertNull(result.getUser());
        assertNull(service.getCurrentUser());
        assertFalse(service.isLoggedIn());
    }

    @Test
    void invalidUsernameReturnsErrorMessage() {
        AuthenticationService service = new AuthenticationService();

        User user = new User("user001", "password123", false);

        AuthenticationResult result = service.login(
                "user999",
                "password123",
                List.of(user)
        );

        assertFalse(result.isSuccessful());
        assertEquals("Invalid username or password", result.getMessage());
        assertNull(result.getUser());
        assertNull(service.getCurrentUser());
        assertFalse(service.isLoggedIn());
    }

    @Test
    void blankUsernameReturnsErrorMessage() {
        AuthenticationService service = new AuthenticationService();

        AuthenticationResult result = service.login(
                "",
                "password123",
                List.of()
        );

        assertFalse(result.isSuccessful());
        assertEquals("Username is required", result.getMessage());
        assertFalse(service.isLoggedIn());
    }

    @Test
    void blankPasswordReturnsErrorMessage() {
        AuthenticationService service = new AuthenticationService();

        AuthenticationResult result = service.login(
                "user001",
                "",
                List.of()
        );

        assertFalse(result.isSuccessful());
        assertEquals("Password is required", result.getMessage());
        assertFalse(service.isLoggedIn());
    }

    @Test
    void nullUsernameReturnsErrorMessage() {
        AuthenticationService service = new AuthenticationService();

        AuthenticationResult result = service.login(
                null,
                "password123",
                List.of()
        );

        assertFalse(result.isSuccessful());
        assertEquals("Username is required", result.getMessage());
        assertFalse(service.isLoggedIn());
    }

    @Test
    void nullPasswordReturnsErrorMessage() {
        AuthenticationService service = new AuthenticationService();

        AuthenticationResult result = service.login(
                "user001",
                null,
                List.of()
        );

        assertFalse(result.isSuccessful());
        assertEquals("Password is required", result.getMessage());
        assertFalse(service.isLoggedIn());
    }

    @Test
    void nullSavedUsersReturnsErrorMessage() {
        AuthenticationService service = new AuthenticationService();

        AuthenticationResult result = service.login(
                "user001",
                "password123",
                null
        );

        assertFalse(result.isSuccessful());
        assertEquals("Saved users cannot be null", result.getMessage());
        assertFalse(service.isLoggedIn());
    }

    @Test
    void usernameWithExtraSpacesStillAuthenticatesUser() {
        AuthenticationService service = new AuthenticationService();

        User user = new User("user001", "password123", false);

        AuthenticationResult result = service.login(
                "  user001  ",
                "password123",
                List.of(user)
        );

        assertTrue(result.isSuccessful());
        assertEquals("Login successful", result.getMessage());
        assertEquals(user, result.getUser());
        assertEquals(user, service.getCurrentUser());
        assertTrue(service.isLoggedIn());
    }

    @Test
    void logoutClearsCurrentUser() {
        AuthenticationService service = new AuthenticationService();

        User user = new User("user001", "password123", false);

        service.login(
                "user001",
                "password123",
                List.of(user)
        );

        assertTrue(service.isLoggedIn());

        service.logout();

        assertNull(service.getCurrentUser());
        assertFalse(service.isLoggedIn());
    }
}

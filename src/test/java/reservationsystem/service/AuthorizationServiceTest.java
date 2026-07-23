package reservationsystem.service;

import org.junit.jupiter.api.Test;
import reservationsystem.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AuthorizationServiceTest {

    @Test
    void grantsAdminAccessToAdministrator() {
        User admin = new User("admin", "password", true);
        AuthorizationService service =
                new AuthorizationService(() -> admin);

        assertTrue(service.isCurrentUserAdmin());
        assertTrue(service.checkAdminAccess().isAuthorized());
    }

    @Test
    void deniesAdminAccessToRegularUserWithClearMessage() {
        User regularUser = new User("student", "password", false);
        AuthorizationService service =
                new AuthorizationService(() -> regularUser);

        AuthorizationResult result =
                service.checkAdminAccess();

        assertFalse(service.isCurrentUserAdmin());
        assertFalse(result.isAuthorized());
        assertEquals(
                "Administrator access is required",
                result.getMessage()
        );
    }

    @Test
    void deniesAdminAccessWhenNoUserIsLoggedIn() {
        AuthorizationService service =
                new AuthorizationService(() -> null);
        AuthorizationResult result = service.checkAdminAccess();

        assertFalse(result.isAuthorized());
        assertEquals(
                "You must be logged in to access this feature",
                result.getMessage()
        );
    }

    @Test
    void followsChangesInAuthenticatedSession() {
        User[] currentUser = {
                new User("student", "password", false)
        };
        AuthorizationService service =
                new AuthorizationService(() -> currentUser[0]);

        assertFalse(service.isCurrentUserAdmin());

        currentUser[0] = new User("admin", "password", true);

        assertTrue(service.isCurrentUserAdmin());
    }
}

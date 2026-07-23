package reservationsystem.controller;

import org.junit.jupiter.api.Test;
import reservationsystem.model.User;
import reservationsystem.persistence.UserJsonRepository;
import reservationsystem.service.AuthenticationService;
import reservationsystem.service.AuthorizationService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AuthenticationControllerTest {

    @Test
    void authorizationFollowsAuthenticatedControllerSession() {
        AuthenticationService authenticationService =
                new AuthenticationService();
        AuthenticationController controller =
                new AuthenticationController(
                        authenticationService,
                        new UserJsonRepository()
                );
        AuthorizationService authorizationService =
                new AuthorizationService(controller);

        assertFalse(authorizationService.isCurrentUserAdmin());

        authenticationService.login(
                "student",
                "password",
                List.of(new User("student", "password", false))
        );
        assertFalse(authorizationService.isCurrentUserAdmin());

        authenticationService.login(
                "admin",
                "password",
                List.of(new User("admin", "password", true))
        );
        assertTrue(authorizationService.isCurrentUserAdmin());

        controller.logout();
        assertFalse(authorizationService.isCurrentUserAdmin());
    }

    @Test
    void providesAuthenticatedUsernameAsCurrentUserId() {
        AuthenticationService authenticationService =
                new AuthenticationService();
        AuthenticationController controller =
                new AuthenticationController(
                        authenticationService,
                        new UserJsonRepository()
                );

        authenticationService.login(
                "admin",
                "password",
                List.of(new User("admin", "password", true))
        );

        assertEquals("admin", controller.getCurrentUserId());
    }

    @Test
    void rejectsCurrentUserIdRequestAfterLogout() {
        AuthenticationService authenticationService =
                new AuthenticationService();
        AuthenticationController controller =
                new AuthenticationController(
                        authenticationService,
                        new UserJsonRepository()
                );

        authenticationService.login(
                "student",
                "password",
                List.of(new User("student", "password", false))
        );
        controller.logout();

        assertThrows(
                IllegalStateException.class,
                controller::getCurrentUserId
        );
    }
}

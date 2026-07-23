package reservationsystem.controller;

import org.junit.jupiter.api.Test;
import reservationsystem.model.User;
import reservationsystem.persistence.UserJsonRepository;
import reservationsystem.service.AuthenticationService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AuthenticationControllerTest {

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

package reservationsystem.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AuthenticationNavigationTest {

    @Test
    void startsOnLoginScreen() {
        AuthenticationNavigation navigation = new AuthenticationNavigation();

        assertEquals(
                AuthenticationNavigation.Screen.LOGIN,
                navigation.getCurrentScreen()
        );
        assertEquals("", navigation.getLoginMessage());
    }

    @Test
    void registrationCanBeOpenedWithoutLoggingIn() {
        AuthenticationNavigation navigation = new AuthenticationNavigation();

        navigation.openRegistration();

        assertEquals(
                AuthenticationNavigation.Screen.REGISTRATION,
                navigation.getCurrentScreen()
        );
    }

    @Test
    void registrationCanBeCancelledBackToLogin() {
        AuthenticationNavigation navigation = new AuthenticationNavigation();
        navigation.openRegistration();

        navigation.returnToLogin();

        assertEquals(
                AuthenticationNavigation.Screen.LOGIN,
                navigation.getCurrentScreen()
        );
        assertEquals("", navigation.getLoginMessage());
    }

    @Test
    void successfulRegistrationReturnsToLoginWithConfirmation() {
        AuthenticationNavigation navigation = new AuthenticationNavigation();
        navigation.openRegistration();

        navigation.completeRegistration();

        assertEquals(
                AuthenticationNavigation.Screen.LOGIN,
                navigation.getCurrentScreen()
        );
        assertEquals(
                "Registration successful. Please log in.",
                navigation.getLoginMessage()
        );
    }
}

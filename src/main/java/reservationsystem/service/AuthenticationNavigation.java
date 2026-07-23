package reservationsystem.service;

public class AuthenticationNavigation {

    public enum Screen {
        LOGIN,
        REGISTRATION
    }

    private Screen currentScreen = Screen.LOGIN;
    private String loginMessage = "";

    public Screen getCurrentScreen() {
        return currentScreen;
    }

    public String getLoginMessage() {
        return loginMessage;
    }

    public void openRegistration() {
        currentScreen = Screen.REGISTRATION;
        loginMessage = "";
    }

    public void returnToLogin() {
        currentScreen = Screen.LOGIN;
        loginMessage = "";
    }

    public void completeRegistration() {
        currentScreen = Screen.LOGIN;
        loginMessage = "Registration successful. Please log in.";
    }
}

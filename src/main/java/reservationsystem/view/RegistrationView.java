package reservationsystem.view;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import reservationsystem.service.RegistrationResult;
import reservationsystem.service.UserService;

public class RegistrationView {

    private final UserService userService;
    private final Runnable registrationSuccessAction;
    private final Runnable cancelAction;
    private final TextField usernameField;
    private final PasswordField passwordField;
    private final PasswordField confirmPasswordField;
    private final Label messageLabel;

    public RegistrationView(
            UserService userService,
            Runnable registrationSuccessAction,
            Runnable cancelAction
    ) {
        if (userService == null) {
            throw new IllegalArgumentException("User service cannot be null");
        }

        if (registrationSuccessAction == null) {
            throw new IllegalArgumentException(
                    "Registration success action cannot be null"
            );
        }

        if (cancelAction == null) {
            throw new IllegalArgumentException(
                    "Cancel action cannot be null"
            );
        }

        this.userService = userService;
        this.registrationSuccessAction = registrationSuccessAction;
        this.cancelAction = cancelAction;
        this.usernameField = new TextField();
        this.passwordField = new PasswordField();
        this.confirmPasswordField = new PasswordField();
        this.messageLabel = new Label();
    }

    public VBox createView() {
        Label titleLabel = new Label("Register New User");

        usernameField.setPromptText("Example: user001");
        usernameField.setMaxWidth(300);
        usernameField.setId("registrationUsernameField");
        passwordField.setPromptText("Enter password");
        passwordField.setMaxWidth(300);
        passwordField.setId("registrationPasswordField");
        confirmPasswordField.setPromptText("Confirm password");
        confirmPasswordField.setMaxWidth(300);
        confirmPasswordField.setId("registrationConfirmPasswordField");

        Button registerButton = new Button("Register");
        registerButton.setDefaultButton(true);
        registerButton.setId("registerButton");
        registerButton.setOnAction(event -> registerUser());

        Button backButton = new Button("Back to Login");
        backButton.setId("backToLoginButton");
        backButton.setOnAction(event -> cancelAction.run());

        messageLabel.setId("registrationMessageLabel");

        VBox layout = new VBox(10);
        layout.getChildren().addAll(
                titleLabel,
                new Label("Username:"),
                usernameField,
                new Label("Password:"),
                passwordField,
                new Label("Confirm Password:"),
                confirmPasswordField,
                registerButton,
                backButton,
                messageLabel
        );

        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));

        return layout;
    }

    private void registerUser() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (confirmPassword == null || confirmPassword.isBlank()) {
            messageLabel.setText("Please confirm the password.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            messageLabel.setText("Passwords do not match.");
            return;
        }

        RegistrationResult result = userService.registerUser(username, password);
        messageLabel.setText(result.getMessage());

        if (result.isSuccessful()) {
            registrationSuccessAction.run();
        }
    }
}

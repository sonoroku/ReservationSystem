package reservationsystem.view;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import reservationsystem.service.RegistrationResult;
import reservationsystem.service.UserService;

public class RegistrationView {

    private final UserService userService;
    private final TextField usernameField;
    private final PasswordField passwordField;
    private final PasswordField confirmPasswordField;
    private final Label messageLabel;

    public RegistrationView() {
        this(new UserService());
    }

    public RegistrationView(UserService userService) {
        if (userService == null) {
            throw new IllegalArgumentException("User service cannot be null");
        }

        this.userService = userService;
        this.usernameField = new TextField();
        this.passwordField = new PasswordField();
        this.confirmPasswordField = new PasswordField();
        this.messageLabel = new Label();
    }

    public VBox createView() {
        Label titleLabel = new Label("Register New User");

        usernameField.setPromptText("Example: user001");
        passwordField.setPromptText("Enter password");
        confirmPasswordField.setPromptText("Confirm password");

        Button registerButton = new Button("Register");
        registerButton.setOnAction(event -> registerUser());

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
                messageLabel
        );

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
    }
}

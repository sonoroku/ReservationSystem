package reservationsystem.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import reservationsystem.controller.AuthenticationController;
import reservationsystem.service.AuthenticationResult;

public class LoginView {
	
	private final AuthenticationController authenticationController;
    private final Runnable loginSuccessAction;

    private final TextField usernameField;
    private final PasswordField passwordField;
    private final Label messageLabel;

    public LoginView(
            AuthenticationController authenticationController,
            Runnable loginSuccessAction
    ) {
        if (authenticationController == null) {
            throw new IllegalArgumentException(
                    "Authentication controller cannot be null"
            );
        }

        if (loginSuccessAction == null) {
            throw new IllegalArgumentException(
                    "Login success action cannot be null"
            );
        }

        this.authenticationController = authenticationController;
        this.loginSuccessAction = loginSuccessAction;

        usernameField = new TextField();
        passwordField = new PasswordField();
        messageLabel = new Label();
    }

    public VBox createView() {
        Label titleLabel = new Label("Reservation System Login");
        titleLabel.setStyle(
                "-fx-font-size: 22px; -fx-font-weight: bold;"
        );

        usernameField.setPromptText("Username");
        usernameField.setMaxWidth(300);
        usernameField.setId("loginUsernameField");

        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(300);
        passwordField.setId("loginPasswordField");

        Button loginButton = new Button("Login");
        loginButton.setDefaultButton(true);
        loginButton.setId("loginButton");
        loginButton.setOnAction(event -> login());

        passwordField.setOnAction(event -> login());

        messageLabel.setId("loginMessageLabel");

        VBox layout = new VBox(
                12,
                titleLabel,
                new Label("Username:"),
                usernameField,
                new Label("Password:"),
                passwordField,
                loginButton,
                messageLabel
        );

        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));

        return layout;
    }

    private void login() {
        AuthenticationResult result =
                authenticationController.login(
                        usernameField.getText(),
                        passwordField.getText()
                );

        messageLabel.setText(result.getMessage());

        if (result.isSuccessful()) {
            passwordField.clear();
            loginSuccessAction.run();
        }
    }

}

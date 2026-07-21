package reservationsystem.view;

import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class RegistrationView {

    private final TextField userIdField;
    private final TextField nameField;
    private final PasswordField passwordField;
    private final PasswordField confirmPasswordField;
    private final ComboBox<String> roleComboBox;
    private final Label messageLabel;

    public RegistrationView() {
        this.userIdField = new TextField();
        this.nameField = new TextField();
        this.passwordField = new PasswordField();
        this.confirmPasswordField = new PasswordField();
        this.roleComboBox = new ComboBox<>();
        this.messageLabel = new Label();
    }

    public VBox createView() {
        Label titleLabel = new Label("Register New User");

        userIdField.setPromptText("Example: user001");
        nameField.setPromptText("Enter full name");
        passwordField.setPromptText("Enter password");
        confirmPasswordField.setPromptText("Confirm password");

        roleComboBox.setItems(FXCollections.observableArrayList(
                "Student",
                "Admin"
        ));
        roleComboBox.setPromptText("Select role");

        Button registerButton = new Button("Register");
        registerButton.setOnAction(event -> registerUser());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(
                titleLabel,
                new Label("User ID:"),
                userIdField,
                new Label("Name:"),
                nameField,
                new Label("Password:"),
                passwordField,
                new Label("Confirm Password:"),
                confirmPasswordField,
                new Label("Role:"),
                roleComboBox,
                registerButton,
                messageLabel
        );

        return layout;
    }

    private void registerUser() {
        String userId = userIdField.getText();
        String name = nameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String role = roleComboBox.getValue();

        if (userId == null || userId.isBlank()) {
            messageLabel.setText("User ID is required.");
            return;
        }

        if (name == null || name.isBlank()) {
            messageLabel.setText("Name is required.");
            return;
        }

        if (password == null || password.isBlank()) {
            messageLabel.setText("Password is required.");
            return;
        }

        if (confirmPassword == null || confirmPassword.isBlank()) {
            messageLabel.setText("Please confirm the password.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            messageLabel.setText("Passwords do not match.");
            return;
        }

        if (role == null || role.isBlank()) {
            messageLabel.setText("Role is required.");
            return;
        }

        messageLabel.setText("Registration form is valid.");
    }
}

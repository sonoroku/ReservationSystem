package reservationsystem.view;

import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import reservationsystem.controller.ReservationController;
import reservationsystem.controller.SpaceController;
import reservationsystem.model.Space;
import reservationsystem.service.ReservationValidationResult;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

public class CreateReservationView {

    private final SpaceController spaceController;
    private final ReservationController reservationController;

    private final ComboBox<Space> spaceComboBox;
    private final DatePicker datePicker;
    private final TextField userIdField;
    private final TextField startTimeField;
    private final TextField endTimeField;
    private final Label messageLabel;

    public CreateReservationView() {
        this(new SpaceController(), new ReservationController());
    }

    public CreateReservationView(
            SpaceController spaceController,
            ReservationController reservationController
    ) {
        this.spaceController = spaceController;
        this.reservationController = reservationController;
        this.spaceComboBox = new ComboBox<>();
        this.datePicker = new DatePicker();
        this.userIdField = new TextField();
        this.startTimeField = new TextField();
        this.endTimeField = new TextField();
        this.messageLabel = new Label();
    }

    public VBox createView() {
        Label titleLabel = new Label("Create Reservation");

        loadSpacesIntoComboBox();

        datePicker.setValue(LocalDate.now());

        userIdField.setPromptText("Example: user001");
        startTimeField.setPromptText("Example: 09:00");
        endTimeField.setPromptText("Example: 10:00");

        Button submitButton = new Button("Create Reservation");
        submitButton.setOnAction(event -> createReservation());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(
                titleLabel,
                new Label("Select a space:"),
                spaceComboBox,
                new Label("Select a date:"),
                datePicker,
                new Label("User ID:"),
                userIdField,
                new Label("Start time:"),
                startTimeField,
                new Label("End time:"),
                endTimeField,
                submitButton,
                messageLabel
        );

        return layout;
    }

    private void loadSpacesIntoComboBox() {
        List<Space> spaces = spaceController.getAllSpaces();

        spaceComboBox.setItems(FXCollections.observableArrayList(spaces));

        if (!spaces.isEmpty()) {
            spaceComboBox.setValue(spaces.get(0));
        }

        spaceComboBox.setCellFactory(listView -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(Space space, boolean empty) {
                super.updateItem(space, empty);

                if (empty || space == null) {
                    setText(null);
                } else {
                    setText(space.getName());
                }
            }
        });

        spaceComboBox.setButtonCell(new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(Space space, boolean empty) {
                super.updateItem(space, empty);

                if (empty || space == null) {
                    setText(null);
                } else {
                    setText(space.getName());
                }
            }
        });
    }

    private void createReservation() {
        Space selectedSpace = spaceComboBox.getValue();
        LocalDate selectedDate = datePicker.getValue();
        String userId = userIdField.getText().trim();

        if (selectedSpace == null) {
            messageLabel.setText("Please select a space.");
            return;
        }

        if (selectedDate == null) {
            messageLabel.setText("Please select a date.");
            return;
        }

        if (userId.isEmpty()) {
            messageLabel.setText("Please enter a user ID.");
            return;
        }

        try {
            LocalTime startTime = LocalTime.parse(startTimeField.getText().trim());
            LocalTime endTime = LocalTime.parse(endTimeField.getText().trim());

            ReservationValidationResult result = reservationController.createReservation(
                    selectedSpace.getId(),
                    userId,
                    selectedDate,
                    startTime,
                    endTime
            );

            if (result.isValid()) {
                messageLabel.setText("Reservation created successfully.");
                clearTimeFields();
            } else {
                messageLabel.setText(result.getMessage());
            }
        } catch (DateTimeParseException e) {
            messageLabel.setText("Please enter times in HH:mm format, such as 09:00 or 14:30.");
        }
    }

    private void clearTimeFields() {
        startTimeField.clear();
        endTimeField.clear();
    }
}
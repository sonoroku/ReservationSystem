package reservationsystem.view;

import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import reservationsystem.controller.AdminReservationController;
import reservationsystem.controller.SpaceController;
import reservationsystem.model.Space;
import reservationsystem.service.AdminReservationCreationResult;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

public class AdminCreateReservationView {
	
	 private final SpaceController spaceController;
	    private final AdminReservationController adminReservationController;
	    private final Runnable reservationCreatedAction;

	    private final ComboBox<String> userComboBox;
	    private final ComboBox<Space> spaceComboBox;
	    private final DatePicker datePicker;
	    private final TextField startTimeField;
	    private final TextField endTimeField;
	    private final Label messageLabel;

	    public AdminCreateReservationView(
	            SpaceController spaceController,
	            AdminReservationController adminReservationController,
	            Runnable reservationCreatedAction
	    ) {
	        if (spaceController == null) {
	            throw new IllegalArgumentException(
	                    "Space controller cannot be null"
	            );
	        }

	        if (adminReservationController == null) {
	            throw new IllegalArgumentException(
	                    "Admin reservation controller cannot be null"
	            );
	        }

	        if (reservationCreatedAction == null) {
	            throw new IllegalArgumentException(
	                    "Reservation-created action cannot be null"
	            );
	        }

	        this.spaceController = spaceController;
	        this.adminReservationController =
	                adminReservationController;
	        this.reservationCreatedAction =
	                reservationCreatedAction;

	        userComboBox = new ComboBox<>();
	        spaceComboBox = new ComboBox<>();
	        datePicker = new DatePicker();
	        startTimeField = new TextField();
	        endTimeField = new TextField();
	        messageLabel = new Label();
	    }

	    public VBox createView() {
	        Label titleLabel =
	                new Label("Create Reservation for User");

	        userComboBox.setId("adminTargetUserComboBox");
	        spaceComboBox.setId("adminReservationSpaceComboBox");
	        datePicker.setId("adminReservationDatePicker");
	        startTimeField.setId("adminReservationStartTimeField");
	        endTimeField.setId("adminReservationEndTimeField");
	        messageLabel.setId("adminReservationMessageLabel");
	        messageLabel.setWrapText(true);
	        messageLabel.setMinHeight(40);

	        loadUsers();
	        loadSpaces();

	        datePicker.setValue(LocalDate.now());
	        startTimeField.setPromptText("Example: 09:00");
	        endTimeField.setPromptText("Example: 10:00");

	        Button createButton =
	                new Button("Create Reservation for User");
	        createButton.setId("adminCreateReservationButton");
	        createButton.setOnAction(
	                event -> createReservation()
	        );

	        return new VBox(
	                10,
	                titleLabel,
	                new Label("Select a user:"),
	                userComboBox,
	                new Label("Select a space:"),
	                spaceComboBox,
	                new Label("Select a date:"),
	                datePicker,
	                new Label("Start time:"),
	                startTimeField,
	                new Label("End time:"),
	                endTimeField,
	                messageLabel,
	                createButton
	        );
	    }

	    private void loadUsers() {
	        try {
	            List<String> userIds =
	                    adminReservationController
	                            .getAvailableUserIds();

	            userComboBox.setItems(
	                    FXCollections.observableArrayList(userIds)
	            );

	            if (!userIds.isEmpty()) {
	                userComboBox.setValue(userIds.get(0));
	            }
	        } catch (IllegalStateException exception) {
	            userComboBox.getItems().clear();
	            messageLabel.setText(exception.getMessage());
	        }
	    }

	    private void loadSpaces() {
	        List<Space> spaces = spaceController.getAllSpaces();

	        spaceComboBox.setItems(
	                FXCollections.observableArrayList(spaces)
	        );

	        if (!spaces.isEmpty()) {
	            spaceComboBox.setValue(spaces.get(0));
	        }

	        spaceComboBox.setCellFactory(
	                listView -> createSpaceListCell()
	        );
	        spaceComboBox.setButtonCell(createSpaceListCell());
	    }

	    private ListCell<Space> createSpaceListCell() {
	        return new ListCell<>() {
	            @Override
	            protected void updateItem(
	                    Space space,
	                    boolean empty
	            ) {
	                super.updateItem(space, empty);
	                setText(
	                        empty || space == null
	                                ? null
	                                : space.getName()
	                );
	            }
	        };
	    }

	    private void createReservation() {
	        String selectedUserId = userComboBox.getValue();
	        Space selectedSpace = spaceComboBox.getValue();
	        LocalDate selectedDate = datePicker.getValue();

	        if (selectedUserId == null) {
	            messageLabel.setText("Please select a user.");
	            return;
	        }

	        if (selectedSpace == null) {
	            messageLabel.setText("Please select a space.");
	            return;
	        }

	        if (selectedDate == null) {
	            messageLabel.setText("Please select a date.");
	            return;
	        }

	        try {
	            LocalTime startTime = LocalTime.parse(
	                    startTimeField.getText().trim()
	            );
	            LocalTime endTime = LocalTime.parse(
	                    endTimeField.getText().trim()
	            );

	            AdminReservationCreationResult result =
	                    adminReservationController
	                            .createReservationForUser(
	                                    selectedUserId,
	                                    selectedSpace.getId(),
	                                    selectedDate,
	                                    startTime,
	                                    endTime
	                            );

	            messageLabel.setText(result.getMessage());

	            if (result.isSuccessful()) {
	                startTimeField.clear();
	                endTimeField.clear();
	                reservationCreatedAction.run();
	            }
	        } catch (DateTimeParseException exception) {
	            messageLabel.setText(
	                    "Please enter times in HH:mm format, "
	                            + "such as 09:00 or 14:30."
	            );
	        }
	    }

}

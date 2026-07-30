package reservationsystem.view;

import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import reservationsystem.controller.AdminReservationController;
import reservationsystem.controller.SpaceController;
import reservationsystem.model.Reservation;
import reservationsystem.model.Space;
import reservationsystem.service.AdminReservationModificationResult;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

public class AdminReservationModificationView {

    private final AdminReservationController adminReservationController;
    private final SpaceController spaceController;
    private final Runnable reservationModifiedAction;

    private final ListView<Reservation> reservationListView;
    private final ComboBox<Space> spaceComboBox;
    private final DatePicker datePicker;
    private final TextField startTimeField;
    private final TextField endTimeField;
    private final Label messageLabel;

    public AdminReservationModificationView(
            AdminReservationController adminReservationController,
            Runnable reservationModifiedAction
    ) {
        this(
                adminReservationController,
                new SpaceController(),
                reservationModifiedAction
        );
    }

    public AdminReservationModificationView(
            AdminReservationController adminReservationController,
            SpaceController spaceController,
            Runnable reservationModifiedAction
    ) {
        if (adminReservationController == null) {
            throw new IllegalArgumentException(
                    "Admin reservation controller cannot be null"
            );
        }

        if (spaceController == null) {
            throw new IllegalArgumentException(
                    "Space controller cannot be null"
            );
        }

        if (reservationModifiedAction == null) {
            throw new IllegalArgumentException(
                    "Reservation-modified action cannot be null"
            );
        }

        this.adminReservationController = adminReservationController;
        this.spaceController = spaceController;
        this.reservationModifiedAction = reservationModifiedAction;

        this.reservationListView = new ListView<>();
        this.spaceComboBox = new ComboBox<>();
        this.datePicker = new DatePicker();
        this.startTimeField = new TextField();
        this.endTimeField = new TextField();
        this.messageLabel = new Label();
    }

    public VBox createView() {
        Label titleLabel = new Label("Admin Modify Reservation");

        loadReservations();
        loadSpaces();

        reservationListView.setPrefHeight(180);
        reservationListView.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Reservation reservation, boolean empty) {
                super.updateItem(reservation, empty);

                if (empty || reservation == null) {
                    setText(null);
                } else {
                    setText(formatReservation(reservation));
                }
            }
        });

        reservationListView.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, selectedReservation) -> {
                    if (selectedReservation != null) {
                        populateFormFromReservation(selectedReservation);
                    }
                });

        spaceComboBox.setCellFactory(listView -> new ListCell<>() {
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

        spaceComboBox.setButtonCell(new ListCell<>() {
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

        startTimeField.setPromptText("Example: 09:00");
        endTimeField.setPromptText("Example: 10:00");

        Button modifyButton = new Button("Modify Reservation");
        modifyButton.setOnAction(event -> modifySelectedReservation());

        Button refreshButton = new Button("Refresh Reservations");
        refreshButton.setOnAction(event -> {
            loadReservations();
            messageLabel.setText("Reservation list refreshed.");
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(
                titleLabel,
                new Label("Select a reservation:"),
                reservationListView,
                new Label("Select a new space:"),
                spaceComboBox,
                new Label("Select a new date:"),
                datePicker,
                new Label("New start time:"),
                startTimeField,
                new Label("New end time:"),
                endTimeField,
                modifyButton,
                refreshButton,
                messageLabel
        );

        return layout;
    }

    private void loadReservations() {
        try {
            List<Reservation> reservations = adminReservationController.getAllReservations();
            reservationListView.setItems(FXCollections.observableArrayList(reservations));

            if (reservations.isEmpty()) {
                messageLabel.setText("No reservations found.");
            }
        } catch (IllegalStateException exception) {
            reservationListView.getItems().clear();
            messageLabel.setText(exception.getMessage());
        }
    }

    private void loadSpaces() {
        List<Space> spaces = spaceController.getAllSpaces();
        spaceComboBox.setItems(FXCollections.observableArrayList(spaces));
    }

    private void populateFormFromReservation(Reservation reservation) {
        Space matchingSpace = spaceComboBox.getItems()
                .stream()
                .filter(space -> space.getId() == reservation.getSpaceId())
                .findFirst()
                .orElse(null);

        spaceComboBox.setValue(matchingSpace);
        datePicker.setValue(reservation.getDate());
        startTimeField.setText(reservation.getStartTime().toString());
        endTimeField.setText(reservation.getEndTime().toString());

        messageLabel.setText("Reservation selected for modification.");
    }

    private void modifySelectedReservation() {
        Reservation selectedReservation =
                reservationListView.getSelectionModel().getSelectedItem();

        if (selectedReservation == null) {
            messageLabel.setText("Please select a reservation.");
            return;
        }

        Space selectedSpace = spaceComboBox.getValue();

        if (selectedSpace == null) {
            messageLabel.setText("Please select a space.");
            return;
        }

        LocalDate selectedDate = datePicker.getValue();

        if (selectedDate == null) {
            messageLabel.setText("Please select a date.");
            return;
        }

        try {
            LocalTime startTime = LocalTime.parse(startTimeField.getText().trim());
            LocalTime endTime = LocalTime.parse(endTimeField.getText().trim());

            AdminReservationModificationResult result =
                    adminReservationController.modifyReservation(
                            selectedReservation.getId(),
                            selectedSpace.getId(),
                            selectedDate,
                            startTime,
                            endTime
                    );

            messageLabel.setText(result.getMessage());

            if (result.isSuccessful()) {
                loadReservations();
                reservationModifiedAction.run();
            }
        } catch (DateTimeParseException exception) {
            messageLabel.setText(
                    "Please enter times in HH:mm format, such as 09:00 or 14:30."
            );
        }
    }

    private String formatReservation(Reservation reservation) {
        return "Reservation ID: " + reservation.getId()
                + " | User ID: " + reservation.getUserId()
                + " | Space ID: " + reservation.getSpaceId()
                + " | Date: " + reservation.getDate()
                + " | Start: " + reservation.getStartTime()
                + " | End: " + reservation.getEndTime();
    }
}

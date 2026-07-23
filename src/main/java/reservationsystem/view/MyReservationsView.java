package reservationsystem.view;

import javafx.geometry.Insets;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import reservationsystem.controller.ReservationController;
import reservationsystem.controller.SpaceController;
import reservationsystem.model.Reservation;
import reservationsystem.model.Space;
import reservationsystem.service.ReservationCancellationResult;
import reservationsystem.service.ReservationModificationResult;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

public class MyReservationsView extends VBox {

    private final ReservationController reservationController;
    private final SpaceController spaceController;
    private final ListView<Reservation> reservationsListView;
    private final Label statusLabel;
    private final VBox modificationForm;
    private final ComboBox<Space> modificationSpaceComboBox;
    private final DatePicker modificationDatePicker;
    private final TextField modificationStartTimeField;
    private final TextField modificationEndTimeField;
    private int reservationBeingModifiedId;

    public MyReservationsView() {
        this(new ReservationController(), new SpaceController());
    }

    public MyReservationsView(ReservationController reservationController) {
        this(reservationController, new SpaceController());
    }

    public MyReservationsView(
            ReservationController reservationController,
            SpaceController spaceController
    ) {
        this.reservationController = reservationController;
        this.spaceController = spaceController;

        setSpacing(10);
        setPadding(new Insets(15));

        Label titleLabel = new Label("My Reservations");

        Button loadButton = new Button("View My Reservations");
        loadButton.setOnAction(event -> loadReservations());

        Button refreshButton = new Button("Refresh");
        refreshButton.setOnAction(event -> loadReservations());
        
        Button cancelButton = new Button("Cancel Selected Reservation");
        cancelButton.setOnAction(event -> cancelSelectedReservation());

        Button modifyButton = new Button("Modify Selected Reservation");
        modifyButton.setOnAction(event -> showModificationForm());

        reservationsListView = new ListView<>();
        reservationsListView.setPrefHeight(250);
        reservationsListView.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Reservation reservation, boolean empty) {
                super.updateItem(reservation, empty);
                setText(empty || reservation == null ? null : formatReservation(reservation));
            }
        });

        statusLabel = new Label("Click View My Reservations to load your reservations.");

        modificationSpaceComboBox = new ComboBox<>();
        configureModificationSpaceComboBox();

        modificationDatePicker = new DatePicker();
        modificationStartTimeField = new TextField();
        modificationStartTimeField.setPromptText("Example: 09:00");
        modificationEndTimeField = new TextField();
        modificationEndTimeField.setPromptText("Example: 10:00");

        Button saveModificationButton = new Button("Save Changes");
        saveModificationButton.setOnAction(event -> saveModification());

        Button dismissModificationButton = new Button("Cancel Edit");
        dismissModificationButton.setOnAction(event -> hideModificationForm());

        modificationForm = new VBox(
                8,
                new Label("Modify Reservation"),
                new Label("Space:"),
                modificationSpaceComboBox,
                new Label("Date:"),
                modificationDatePicker,
                new Label("Start time:"),
                modificationStartTimeField,
                new Label("End time:"),
                modificationEndTimeField,
                saveModificationButton,
                dismissModificationButton
        );
        hideModificationForm();

        getChildren().addAll(
                titleLabel,
                loadButton,
                refreshButton,
                cancelButton,
                modifyButton,
                reservationsListView,
                modificationForm,
                statusLabel
        );
    }

    private void loadReservations() {
        reservationsListView.getItems().clear();
        hideModificationForm();

        try {
            List<Reservation> reservations = reservationController.getMyReservations();

            if (reservations.isEmpty()) {
                statusLabel.setText("No reservations found for this user.");
                return;
            }

            reservationsListView.getItems().setAll(reservations);

            statusLabel.setText("Showing your reservations.");
        } catch (IllegalArgumentException exception) {
            statusLabel.setText(exception.getMessage());
        }
    }
    
    private void cancelSelectedReservation() {
        Reservation selectedReservation =
                reservationsListView.getSelectionModel().getSelectedItem();

        if (selectedReservation == null) {
            statusLabel.setText("Select a reservation before cancelling.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Cancel Reservation");
        confirmation.setHeaderText(
                "Cancel reservation " + selectedReservation.getId() + "?"
        );
        confirmation.setContentText("This action cannot be undone.");

        Optional<ButtonType> response = confirmation.showAndWait();

        if (response.isEmpty() || response.get() != ButtonType.OK) {
            statusLabel.setText("Cancellation was not confirmed.");
            return;
        }

        ReservationCancellationResult result =
                reservationController.cancelReservation(
                        selectedReservation.getId()
                );

        if (result.isSuccessful()) {
            loadReservations();
        }

        statusLabel.setText(result.getMessage());
    }

    private void configureModificationSpaceComboBox() {
        List<Space> spaces = spaceController.getAllSpaces();
        modificationSpaceComboBox.setItems(FXCollections.observableArrayList(spaces));

        modificationSpaceComboBox.setCellFactory(listView -> createSpaceListCell());
        modificationSpaceComboBox.setButtonCell(createSpaceListCell());
    }

    private ListCell<Space> createSpaceListCell() {
        return new ListCell<>() {
            @Override
            protected void updateItem(Space space, boolean empty) {
                super.updateItem(space, empty);
                setText(empty || space == null ? null : space.getName());
            }
        };
    }

    private void showModificationForm() {
        Reservation selectedReservation =
                reservationsListView.getSelectionModel().getSelectedItem();

        if (selectedReservation == null) {
            statusLabel.setText("Select a reservation before modifying.");
            return;
        }

        reservationBeingModifiedId = selectedReservation.getId();
        modificationSpaceComboBox.getItems().stream()
                .filter(space -> space.getId() == selectedReservation.getSpaceId())
                .findFirst()
                .ifPresentOrElse(
                        modificationSpaceComboBox::setValue,
                        () -> modificationSpaceComboBox.setValue(null)
                );
        modificationDatePicker.setValue(selectedReservation.getDate());
        modificationStartTimeField.setText(selectedReservation.getStartTime().toString());
        modificationEndTimeField.setText(selectedReservation.getEndTime().toString());

        modificationForm.setManaged(true);
        modificationForm.setVisible(true);
        statusLabel.setText("Editing reservation " + selectedReservation.getId() + ".");
    }

    private void saveModification() {
        Space selectedSpace = modificationSpaceComboBox.getValue();

        if (selectedSpace == null) {
            statusLabel.setText("Please select a space.");
            return;
        }

        if (modificationDatePicker.getValue() == null) {
            statusLabel.setText("Please select a date.");
            return;
        }

        try {
            LocalTime startTime = LocalTime.parse(modificationStartTimeField.getText().trim());
            LocalTime endTime = LocalTime.parse(modificationEndTimeField.getText().trim());

            ReservationModificationResult result = reservationController.modifyReservation(
                    reservationBeingModifiedId,
                    selectedSpace.getId(),
                    modificationDatePicker.getValue(),
                    startTime,
                    endTime
            );

            if (result.isSuccessful()) {
                loadReservations();
            }

            statusLabel.setText(result.getMessage());
        } catch (DateTimeParseException exception) {
            statusLabel.setText(
                    "Please enter times in HH:mm format, such as 09:00 or 14:30."
            );
        }
    }

    private void hideModificationForm() {
        modificationForm.setManaged(false);
        modificationForm.setVisible(false);
    }

    public void resetForSessionChange() {
        reservationsListView.getItems().clear();
        reservationsListView.getSelectionModel().clearSelection();
        modificationSpaceComboBox.setValue(null);
        modificationDatePicker.setValue(null);
        modificationStartTimeField.clear();
        modificationEndTimeField.clear();
        reservationBeingModifiedId = 0;
        hideModificationForm();
        statusLabel.setText(
                "Click View My Reservations to load your reservations."
        );
    }

    private String formatReservation(Reservation reservation) {
        Optional<Space> space = reservationController.getSpaceForReservation(reservation);
        String spaceDescription = space
                .map(value -> value.getName() + " (ID: " + value.getId() + ")")
                .orElse("ID: " + reservation.getSpaceId());

        return "Reservation ID: " + reservation.getId()
                + " | Space: " + spaceDescription
                + " | Date: " + reservation.getDate()
                + " | Start: " + reservation.getStartTime()
                + " | End: " + reservation.getEndTime();
    }
}

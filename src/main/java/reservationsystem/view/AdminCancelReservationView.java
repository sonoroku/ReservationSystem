package reservationsystem.view;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import reservationsystem.controller.AdminReservationController;
import reservationsystem.controller.SpaceController;
import reservationsystem.model.Reservation;
import reservationsystem.model.Space;
import reservationsystem.service.AdminReservationCancellationResult;

import java.util.List;
import java.util.Optional;

public class AdminCancelReservationView {

    private final AdminReservationController adminReservationController;
    private final SpaceController spaceController;
    private final Runnable reservationCancelledAction;

    private final ListView<Reservation> reservationsListView;
    private final Label messageLabel;

    public AdminCancelReservationView(
            AdminReservationController adminReservationController,
            SpaceController spaceController,
            Runnable reservationCancelledAction
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

        if (reservationCancelledAction == null) {
            throw new IllegalArgumentException(
                    "Reservation-cancelled action cannot be null"
            );
        }

        this.adminReservationController = adminReservationController;
        this.spaceController = spaceController;
        this.reservationCancelledAction = reservationCancelledAction;

        reservationsListView = new ListView<>();
        messageLabel = new Label();
    }

    public VBox createView() {
        Label titleLabel = new Label("Cancel Any Reservation");

        reservationsListView.setId("adminCancellationReservationList");
        reservationsListView.setPrefHeight(350);
        reservationsListView.setCellFactory(
                listView -> new ListCell<>() {
                    @Override
                    protected void updateItem(
                            Reservation reservation,
                            boolean empty
                    ) {
                        super.updateItem(reservation, empty);
                        setText(
                                empty || reservation == null
                                        ? null
                                        : formatReservation(reservation)
                        );
                    }
                }
        );

        messageLabel.setId("adminCancellationMessageLabel");
        messageLabel.setWrapText(true);

        Button refreshButton = new Button("Refresh Reservations");
        refreshButton.setId("adminCancellationRefreshButton");
        refreshButton.setOnAction(event -> loadReservations());

        Button cancelButton = new Button("Cancel Selected Reservation");
        cancelButton.setId("adminCancellationConfirmButton");
        cancelButton.setOnAction(event -> cancelSelectedReservation());

        loadReservations();

        return new VBox(
                10,
                titleLabel,
                new Label("Select a reservation:"),
                reservationsListView,
                refreshButton,
                cancelButton,
                messageLabel
        );
    }

    private void loadReservations() {
        reservationsListView.getItems().clear();

        try {
            List<Reservation> reservations =
                    adminReservationController.getAllReservations();

            if (reservations.isEmpty()) {
                messageLabel.setText("No reservations are available.");
                return;
            }

            reservationsListView.getItems().setAll(reservations);
            messageLabel.setText("Showing all reservations.");
        } catch (IllegalStateException exception) {
            messageLabel.setText(exception.getMessage());
        }
    }

    private void cancelSelectedReservation() {
        Reservation selectedReservation =
                reservationsListView
                        .getSelectionModel()
                        .getSelectedItem();

        if (selectedReservation == null) {
            messageLabel.setText(
                    "Select a reservation before cancelling."
            );
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Cancel Reservation");
        confirmation.setHeaderText(
                "Cancel reservation "
                        + selectedReservation.getId()
                        + " for "
                        + selectedReservation.getUserId()
                        + "?"
        );
        confirmation.setContentText("This action cannot be undone.");

        Optional<ButtonType> response = confirmation.showAndWait();

        if (response.isEmpty() || response.get() != ButtonType.OK) {
            messageLabel.setText("Cancellation was not confirmed.");
            return;
        }

        AdminReservationCancellationResult result =
                adminReservationController.cancelReservation(
                        selectedReservation.getId()
                );

        if (result.isSuccessful()) {
            loadReservations();
            reservationCancelledAction.run();
        } else if (result.getStatus()
                == AdminReservationCancellationResult.Status.NOT_FOUND) {
            loadReservations();
        }

        messageLabel.setText(result.getMessage());
    }

    private String formatReservation(Reservation reservation) {
        Optional<Space> space = spaceController.getSpaceById(
                reservation.getSpaceId()
        );
        String spaceDescription = space
                .map(value ->
                        value.getName() + " (ID: " + value.getId() + ")")
                .orElse("ID: " + reservation.getSpaceId());

        return "Reservation ID: " + reservation.getId()
                + " | Owner: " + reservation.getUserId()
                + " | Space: " + spaceDescription
                + " | Date: " + reservation.getDate()
                + " | Start: " + reservation.getStartTime()
                + " | End: " + reservation.getEndTime();
    }
}

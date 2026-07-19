package reservationsystem.view;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import reservationsystem.controller.ReservationController;
import reservationsystem.model.Reservation;
import reservationsystem.model.Space;

import java.util.List;
import java.util.Optional;

public class MyReservationsView extends VBox {

    private final ReservationController reservationController;
    private final ListView<Reservation> reservationsListView;
    private final Label statusLabel;

    public MyReservationsView() {
        this(new ReservationController());
    }

    public MyReservationsView(ReservationController reservationController) {
        this.reservationController = reservationController;

        setSpacing(10);
        setPadding(new Insets(15));

        Label titleLabel = new Label("My Reservations");

        Button loadButton = new Button("View My Reservations");
        loadButton.setOnAction(event -> loadReservations());

        Button refreshButton = new Button("Refresh");
        refreshButton.setOnAction(event -> loadReservations());

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

        getChildren().addAll(
                titleLabel,
                loadButton,
                refreshButton,
                reservationsListView,
                statusLabel
        );
    }

    private void loadReservations() {
        reservationsListView.getItems().clear();

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

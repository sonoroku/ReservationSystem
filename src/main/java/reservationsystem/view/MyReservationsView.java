package reservationsystem.view;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import reservationsystem.controller.MyReservationsController;
import reservationsystem.model.Reservation;

import java.util.List;

public class MyReservationsView extends VBox {

    private final MyReservationsController myReservationsController;
    private final TextField userIdField;
    private final ListView<String> reservationsListView;
    private final Label statusLabel;

    public MyReservationsView() {
        this(new MyReservationsController());
    }

    public MyReservationsView(MyReservationsController myReservationsController) {
        this.myReservationsController = myReservationsController;

        setSpacing(10);
        setPadding(new Insets(15));

        Label titleLabel = new Label("My Reservations");

        Label instructionLabel = new Label("Enter a user ID to view reservations:");

        userIdField = new TextField();
        userIdField.setPromptText("Example: user001");

        Button loadButton = new Button("View My Reservations");
        loadButton.setOnAction(event -> loadReservations());

        Button refreshButton = new Button("Refresh");
        refreshButton.setOnAction(event -> loadReservations());

        reservationsListView = new ListView<>();
        reservationsListView.setPrefHeight(250);

        statusLabel = new Label("Enter a user ID and click View My Reservations.");

        getChildren().addAll(
                titleLabel,
                instructionLabel,
                userIdField,
                loadButton,
                refreshButton,
                reservationsListView,
                statusLabel
        );
    }

    private void loadReservations() {
        String userId = userIdField.getText();

        reservationsListView.getItems().clear();

        if (userId == null || userId.isBlank()) {
            statusLabel.setText("User ID is required.");
            return;
        }

        try {
            List<Reservation> reservations = myReservationsController.getReservationsForUser(userId.trim());

            if (reservations.isEmpty()) {
                statusLabel.setText("No reservations found for this user.");
                return;
            }

            for (Reservation reservation : reservations) {
                reservationsListView.getItems().add(formatReservation(reservation));
            }

            statusLabel.setText("Showing reservations for " + userId.trim() + ".");
        } catch (IllegalArgumentException exception) {
            statusLabel.setText(exception.getMessage());
        }
    }

    private String formatReservation(Reservation reservation) {
        return "Reservation ID: " + reservation.getId()
                + " | Space ID: " + reservation.getSpaceId()
                + " | Date: " + reservation.getDate()
                + " | Start: " + reservation.getStartTime()
                + " | End: " + reservation.getEndTime();
    }
}

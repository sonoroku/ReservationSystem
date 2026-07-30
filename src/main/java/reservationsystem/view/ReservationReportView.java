package reservationsystem.view;

import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import reservationsystem.controller.ReservationReportController;
import reservationsystem.service.ReservationReportEntry;
import reservationsystem.service.ReservationReportResult;

import java.util.List;

public class ReservationReportView {

    private final ReservationReportController reservationReportController;
    private final ListView<ReservationReportEntry> reportListView;
    private final Label messageLabel;

    public ReservationReportView(
            ReservationReportController reservationReportController
    ) {
        if (reservationReportController == null) {
            throw new IllegalArgumentException(
                    "Reservation report controller cannot be null"
            );
        }

        this.reservationReportController = reservationReportController;
        reportListView = new ListView<>();
        messageLabel = new Label();
    }

    public VBox createView() {
        Label titleLabel = new Label("All Reservations Report");

        reportListView.setId("reservationReportList");
        reportListView.setPrefHeight(400);
        reportListView.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(
                    ReservationReportEntry entry,
                    boolean empty
            ) {
                super.updateItem(entry, empty);
                setText(empty || entry == null ? null : formatEntry(entry));
            }
        });

        messageLabel.setId("reservationReportMessageLabel");
        messageLabel.setWrapText(true);

        Button refreshButton = new Button("Refresh Reservations Report");
        refreshButton.setId("reservationReportRefreshButton");
        refreshButton.setOnAction(event -> refreshReport());

        refreshReport();

        return new VBox(
                10,
                titleLabel,
                refreshButton,
                messageLabel,
                reportListView
        );
    }

    public void refreshReport() {
        reportListView.getItems().clear();

        try {
            ReservationReportResult result =
                    reservationReportController.getAllReservationsReport();

            if (!result.isSuccessful()) {
                messageLabel.setText(result.getMessage());
                return;
            }

            reportListView.setItems(
                    FXCollections.observableArrayList(result.getEntries())
            );
            messageLabel.setText(result.getMessage());
        } catch (IllegalStateException exception) {
            messageLabel.setText(exception.getMessage());
        }
    }

    public List<ReservationReportEntry> getDisplayedEntries() {
        return List.copyOf(reportListView.getItems());
    }

    private String formatEntry(ReservationReportEntry entry) {
        return "Reservation ID: " + entry.getReservationId()
                + " | User ID: " + entry.getUserId()
                + " | Space: " + entry.getSpaceName()
                + " (ID: " + entry.getSpaceId() + ")"
                + " | Building: " + entry.getBuilding()
                + " | Date: " + entry.getDate()
                + " | Start: " + entry.getStartTime()
                + " | End: " + entry.getEndTime();
    }
}

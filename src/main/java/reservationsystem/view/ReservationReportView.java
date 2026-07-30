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

import java.time.LocalDate;
import java.util.List;

public class ReservationReportView {

    private final ReservationReportController reservationReportController;
    private final ListView<ReservationReportEntry> reportListView;
    private final Label messageLabel;
    private final ReportDateFilterControls dateFilterControls;
    private LocalDate appliedStartDate;
    private LocalDate appliedEndDate;

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
        dateFilterControls = new ReportDateFilterControls(
                "reservationReport",
                this::applyDateFilter,
                this::clearDateFilter
        );
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
                dateFilterControls.getView(),
                refreshButton,
                messageLabel,
                reportListView
        );
    }

    public void refreshReport() {
        try {
            ReservationReportResult result = hasAppliedDateFilter()
                    ? reservationReportController.getAllReservationsReport(
                            appliedStartDate,
                            appliedEndDate
                    )
                    : reservationReportController
                            .getAllReservationsReport();

            displayResult(result);
        } catch (IllegalStateException exception) {
            messageLabel.setText(exception.getMessage());
        }
    }

    private void applyDateFilter() {
        try {
            LocalDate startDate = dateFilterControls.getStartDate();
            LocalDate endDate = dateFilterControls.getEndDate();
            ReservationReportResult result = reservationReportController
                    .getAllReservationsReport(startDate, endDate);

            if (!result.isSuccessful()) {
                messageLabel.setText(result.getMessage());
                return;
            }

            appliedStartDate = startDate;
            appliedEndDate = endDate;
            displayResult(result);
        } catch (IllegalStateException exception) {
            messageLabel.setText(exception.getMessage());
        }
    }

    private void clearDateFilter() {
        appliedStartDate = null;
        appliedEndDate = null;
        refreshReport();
    }

    private boolean hasAppliedDateFilter() {
        return appliedStartDate != null && appliedEndDate != null;
    }

    private void displayResult(ReservationReportResult result) {
        if (!result.isSuccessful()) {
            messageLabel.setText(result.getMessage());
            return;
        }

        reportListView.setItems(
                FXCollections.observableArrayList(result.getEntries())
        );
        messageLabel.setText(result.getMessage());
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

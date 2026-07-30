package reservationsystem.view;

import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Window;
import reservationsystem.controller.ReservationReportController;
import reservationsystem.service.CsvExportResult;
import reservationsystem.service.ReportCsvExporter;
import reservationsystem.service.ReservationReportEntry;
import reservationsystem.service.ReservationReportResult;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

public class ReservationReportView {

    private final ReservationReportController reservationReportController;
    private final ListView<ReservationReportEntry> reportListView;
    private final Label messageLabel;
    private final ReportDateFilterControls dateFilterControls;
    private final ReportCsvExporter reportCsvExporter;
    private final ReportCsvExportDialog reportCsvExportDialog;
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
        reportCsvExporter = new ReportCsvExporter();
        reportCsvExportDialog = new ReportCsvExportDialog();
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

        Button exportButton = new Button("Export CSV");
        exportButton.setId("reservationReportExportButton");
        exportButton.setOnAction(event -> exportReport());

        refreshReport();

        return new VBox(
                10,
                titleLabel,
                dateFilterControls.getView(),
                refreshButton,
                exportButton,
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

    private void exportReport() {
        List<ReservationReportEntry> displayedEntries =
                getDisplayedEntries();
        if (displayedEntries.isEmpty()) {
            messageLabel.setText(
                    CsvExportResult.emptyData(null).getMessage()
            );
            return;
        }

        Window owner = reportListView.getScene() == null
                ? null
                : reportListView.getScene().getWindow();
        Path destination = reportCsvExportDialog.chooseDestination(
                owner,
                "reservations-report.csv"
        );
        if (destination == null) {
            messageLabel.setText("Export cancelled");
            return;
        }

        CsvExportResult result = reportCsvExporter.exportReservations(
                displayedEntries,
                destination
        );
        if (result.getStatus() == CsvExportResult.Status.FILE_EXISTS) {
            if (!reportCsvExportDialog.confirmOverwrite(owner, destination)) {
                messageLabel.setText("Export cancelled");
                return;
            }
            result = reportCsvExporter.exportReservations(
                    displayedEntries,
                    destination,
                    true
            );
        }
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

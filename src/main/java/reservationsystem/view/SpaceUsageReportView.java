package reservationsystem.view;

import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Window;
import reservationsystem.controller.SpaceUsageReportController;
import reservationsystem.model.Space;
import reservationsystem.model.SpaceUsageReportRow;
import reservationsystem.service.CsvExportResult;
import reservationsystem.service.ReportCsvExporter;
import reservationsystem.service.SpaceUsageReportResult;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

public class SpaceUsageReportView {

    private final SpaceUsageReportController spaceUsageReportController;
    private final ListView<SpaceUsageReportRow> reportListView;
    private final Label messageLabel;
    private final ReportDateFilterControls dateFilterControls;
    private final ReportCsvExporter reportCsvExporter;
    private final ReportCsvExportDialog reportCsvExportDialog;
    private LocalDate appliedStartDate;
    private LocalDate appliedEndDate;

    public SpaceUsageReportView(
            SpaceUsageReportController spaceUsageReportController
    ) {
        if (spaceUsageReportController == null) {
            throw new IllegalArgumentException(
                    "Space usage report controller cannot be null"
            );
        }

        this.spaceUsageReportController = spaceUsageReportController;
        reportListView = new ListView<>();
        messageLabel = new Label();
        dateFilterControls = new ReportDateFilterControls(
                "spaceUsageReport",
                this::applyDateFilter,
                this::clearDateFilter
        );
        reportCsvExporter = new ReportCsvExporter();
        reportCsvExportDialog = new ReportCsvExportDialog();
    }

    public VBox createView() {
        Label titleLabel = new Label("Space Usage Report");

        reportListView.setId("spaceUsageReportList");
        reportListView.setPrefHeight(400);
        reportListView.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(
                    SpaceUsageReportRow row,
                    boolean empty
            ) {
                super.updateItem(row, empty);
                setText(empty || row == null ? null : formatRow(row));
            }
        });

        messageLabel.setId("spaceUsageReportMessageLabel");
        messageLabel.setWrapText(true);

        Button refreshButton = new Button("Refresh Usage Report");
        refreshButton.setId("spaceUsageReportRefreshButton");
        refreshButton.setOnAction(event -> refreshReport());

        Button exportButton = new Button("Export CSV");
        exportButton.setId("spaceUsageReportExportButton");
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
            SpaceUsageReportResult result = hasAppliedDateFilter()
                    ? spaceUsageReportController.getSpaceUsageReport(
                            appliedStartDate,
                            appliedEndDate
                    )
                    : spaceUsageReportController.getSpaceUsageReport();

            displayResult(result);
        } catch (IllegalStateException exception) {
            messageLabel.setText(exception.getMessage());
        }
    }

    private void applyDateFilter() {
        try {
            LocalDate startDate = dateFilterControls.getStartDate();
            LocalDate endDate = dateFilterControls.getEndDate();
            SpaceUsageReportResult result = spaceUsageReportController
                    .getSpaceUsageReport(startDate, endDate);

            if (!isDisplayable(result)) {
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

    private void displayResult(SpaceUsageReportResult result) {
        if (!isDisplayable(result)) {
            messageLabel.setText(result.getMessage());
            return;
        }

        reportListView.setItems(
                FXCollections.observableArrayList(result.getRows())
        );
        if (result.getStatus() == SpaceUsageReportResult.Status.EMPTY) {
            messageLabel.setText(result.getMessage());
        } else {
            messageLabel.setText(
                    "Showing usage for "
                            + result.getRows().size()
                            + " spaces."
            );
        }
    }

    private boolean isDisplayable(SpaceUsageReportResult result) {
        return result.getStatus() == SpaceUsageReportResult.Status.SUCCESS
                || result.getStatus() == SpaceUsageReportResult.Status.EMPTY;
    }

    private void exportReport() {
        List<SpaceUsageReportRow> displayedRows = getDisplayedRows();
        if (displayedRows.isEmpty()) {
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
                "space-usage-report.csv"
        );
        if (destination == null) {
            messageLabel.setText("Export cancelled");
            return;
        }

        CsvExportResult result = reportCsvExporter.exportSpaceUsage(
                displayedRows,
                destination
        );
        if (result.getStatus() == CsvExportResult.Status.FILE_EXISTS) {
            if (!reportCsvExportDialog.confirmOverwrite(owner, destination)) {
                messageLabel.setText("Export cancelled");
                return;
            }
            result = reportCsvExporter.exportSpaceUsage(
                    displayedRows,
                    destination,
                    true
            );
        }
        messageLabel.setText(result.getMessage());
    }

    public List<SpaceUsageReportRow> getDisplayedRows() {
        return List.copyOf(reportListView.getItems());
    }

    private String formatRow(SpaceUsageReportRow row) {
        Space space = row.getSpace();
        int count = row.getReservationCount();
        String reservationWord = count == 1 ? "reservation" : "reservations";

        return space.getName()
                + " | Building: " + space.getBuilding()
                + " | Capacity: " + space.getCapacity()
                + " | " + count + " " + reservationWord;
    }
}

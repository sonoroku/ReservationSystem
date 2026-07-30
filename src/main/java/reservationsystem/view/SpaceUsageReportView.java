package reservationsystem.view;

import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import reservationsystem.controller.SpaceUsageReportController;
import reservationsystem.model.Space;
import reservationsystem.model.SpaceUsageReportRow;
import reservationsystem.service.SpaceUsageReportResult;

import java.util.List;

public class SpaceUsageReportView {

    private final SpaceUsageReportController spaceUsageReportController;
    private final ListView<SpaceUsageReportRow> reportListView;
    private final Label messageLabel;

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
            SpaceUsageReportResult result =
                    spaceUsageReportController.getSpaceUsageReport();

            if (result.getStatus() != SpaceUsageReportResult.Status.SUCCESS) {
                messageLabel.setText(result.getMessage());
                return;
            }

            reportListView.setItems(
                    FXCollections.observableArrayList(result.getRows())
            );
            messageLabel.setText(
                    "Showing usage for "
                            + result.getRows().size()
                            + " spaces."
            );
        } catch (IllegalStateException exception) {
            messageLabel.setText(exception.getMessage());
        }
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

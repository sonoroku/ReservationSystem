package reservationsystem.view;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import reservationsystem.controller.ReservationController;
import reservationsystem.model.DailyReservationSummary;
import reservationsystem.model.Reservation;
import reservationsystem.model.Space;
import reservationsystem.service.DailyReservationSummaryResult;

import java.util.Optional;

public class DailySummaryView {

    private final ReservationController reservationController;
    private final VBox summaryContent;
    private final Label messageLabel;

    public DailySummaryView(
            ReservationController reservationController
    ) {
        if (reservationController == null) {
            throw new IllegalArgumentException(
                    "Reservation controller cannot be null"
            );
        }

        this.reservationController = reservationController;
        summaryContent = new VBox(12);
        messageLabel = new Label();
    }

    public VBox createView() {
        Label titleLabel = new Label("My Daily Reservation Summary");

        summaryContent.setId("dailySummaryContent");
        messageLabel.setId("dailySummaryMessageLabel");
        messageLabel.setWrapText(true);

        Button refreshButton = new Button("Refresh Daily Summary");
        refreshButton.setId("dailySummaryRefreshButton");
        refreshButton.setOnAction(event -> refreshSummary());

        refreshSummary();

        return new VBox(
                10,
                titleLabel,
                refreshButton,
                messageLabel,
                summaryContent
        );
    }

    public void refreshSummary() {
        summaryContent.getChildren().clear();

        try {
            DailyReservationSummaryResult result =
                    reservationController.getDailyReservationSummary();

            if (result.isEmpty()) {
                messageLabel.setText(result.getMessage());
                return;
            }

            for (DailyReservationSummary summary
                    : result.getSummaries()) {
                summaryContent.getChildren().add(
                        createDateSection(summary)
                );
            }

            messageLabel.setText(
                    "Showing reservations grouped by date."
            );
        } catch (IllegalArgumentException exception) {
            messageLabel.setText(exception.getMessage());
        }
    }

    private VBox createDateSection(DailyReservationSummary summary) {
        int reservationCount = summary.getReservationCount();
        String reservationWord =
                reservationCount == 1 ? "reservation" : "reservations";

        Label dateHeading = new Label(
                summary.getDate()
                        + " — "
                        + reservationCount
                        + " "
                        + reservationWord
        );
        dateHeading.getStyleClass().add("availability-date-header");

        VBox dateSection = new VBox(5, dateHeading);

        for (Reservation reservation : summary.getReservations()) {
            dateSection.getChildren().add(
                    new Label(formatReservation(reservation))
            );
        }

        return dateSection;
    }

    private String formatReservation(Reservation reservation) {
        Optional<Space> space =
                reservationController.getSpaceForReservation(reservation);
        String spaceDescription = space
                .map(value ->
                        value.getName() + " (ID: " + value.getId() + ")")
                .orElse("ID: " + reservation.getSpaceId());

        return "Reservation ID: " + reservation.getId()
                + " | Space: " + spaceDescription
                + " | Start: " + reservation.getStartTime()
                + " | End: " + reservation.getEndTime();
    }
}

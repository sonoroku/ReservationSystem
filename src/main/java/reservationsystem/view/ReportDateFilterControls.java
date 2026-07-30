package reservationsystem.view;

import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.time.LocalDate;

public class ReportDateFilterControls {

    private final DatePicker startDatePicker;
    private final DatePicker endDatePicker;
    private final HBox view;

    public ReportDateFilterControls(
            String idPrefix,
            Runnable applyAction,
            Runnable clearAction
    ) {
        if (idPrefix == null || idPrefix.isBlank()) {
            throw new IllegalArgumentException("ID prefix cannot be blank");
        }
        if (applyAction == null || clearAction == null) {
            throw new IllegalArgumentException(
                    "Date filter actions cannot be null"
            );
        }

        startDatePicker = new DatePicker();
        startDatePicker.setId(idPrefix + "StartDatePicker");
        endDatePicker = new DatePicker();
        endDatePicker.setId(idPrefix + "EndDatePicker");

        Button applyButton = new Button("Apply");
        applyButton.setId(idPrefix + "ApplyDateFilterButton");
        applyButton.setOnAction(event -> applyAction.run());

        Button clearButton = new Button("Clear");
        clearButton.setId(idPrefix + "ClearDateFilterButton");
        clearButton.setOnAction(event -> {
            clearDates();
            clearAction.run();
        });

        view = new HBox(
                10,
                new Label("Start date:"),
                startDatePicker,
                new Label("End date:"),
                endDatePicker,
                applyButton,
                clearButton
        );
    }

    public HBox getView() {
        return view;
    }

    public LocalDate getStartDate() {
        return startDatePicker.getValue();
    }

    public LocalDate getEndDate() {
        return endDatePicker.getValue();
    }

    private void clearDates() {
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
    }
}

package reservationsystem.view;

import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import reservationsystem.controller.ReservationController;
import reservationsystem.controller.SpaceController;
import reservationsystem.controller.TimeSuggestionController;
import reservationsystem.model.Space;
import reservationsystem.model.TimeSlot;
import reservationsystem.service.ReservationValidationResult;
import reservationsystem.service.TimeSuggestionResult;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

public class CreateReservationView {

    private final SpaceController spaceController;
    private final ReservationController reservationController;
    private final TimeSuggestionController timeSuggestionController;

    private final ComboBox<Space> spaceComboBox;
    private final DatePicker datePicker;
    private final TextField startTimeField;
    private final TextField endTimeField;
    private final TextField durationMinutesField;
    private final ListView<TimeSlot> suggestionsListView;
    private final Label messageLabel;

    public CreateReservationView() {
        this(
                new SpaceController(),
                new ReservationController(),
                new TimeSuggestionController()
        );
    }

    public CreateReservationView(
            SpaceController spaceController,
            ReservationController reservationController
    ) {
        this(
                spaceController,
                reservationController,
                new TimeSuggestionController()
        );
    }

    public CreateReservationView(
            SpaceController spaceController,
            ReservationController reservationController,
            TimeSuggestionController timeSuggestionController
    ) {
        this.spaceController = spaceController;
        this.reservationController = reservationController;
        this.timeSuggestionController = timeSuggestionController;
        this.spaceComboBox = new ComboBox<>();
        this.datePicker = new DatePicker();
        this.startTimeField = new TextField();
        this.endTimeField = new TextField();
        this.durationMinutesField = new TextField();
        this.suggestionsListView = new ListView<>();
        this.messageLabel = new Label();
    }

    public VBox createView() {
        Label titleLabel = new Label("Create Reservation");

        loadSpacesIntoComboBox();

        datePicker.setValue(LocalDate.now());

        startTimeField.setPromptText("Example: 09:00");
        endTimeField.setPromptText("Example: 10:00");
        durationMinutesField.setPromptText("Example: 60");

        Button suggestTimesButton = new Button("Suggest Times");
        suggestTimesButton.setOnAction(event -> suggestAvailableTimes());

        suggestionsListView.setPrefHeight(150);
        suggestionsListView.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(TimeSlot timeSlot, boolean empty) {
                super.updateItem(timeSlot, empty);

                if (empty || timeSlot == null) {
                    setText(null);
                } else {
                    setText(timeSlot.getStartTime() + " - " + timeSlot.getEndTime());
                }
            }
        });

        suggestionsListView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, selectedSuggestion) -> {
                    if (selectedSuggestion != null) {
                        populateReservationFields(
                                spaceComboBox.getValue(),
                                datePicker.getValue(),
                                selectedSuggestion.getStartTime(),
                                selectedSuggestion.getEndTime()
                        );
                    }
                }
        );

        spaceComboBox.valueProperty().addListener(
                (observable, oldValue, newValue) -> clearSuggestions()
        );
        datePicker.valueProperty().addListener(
                (observable, oldValue, newValue) -> clearSuggestions()
        );
        durationMinutesField.textProperty().addListener(
                (observable, oldValue, newValue) -> clearSuggestions()
        );

        Button submitButton = new Button("Create Reservation");
        submitButton.setOnAction(event -> createReservation());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(
                titleLabel,
                new Label("Select a space:"),
                spaceComboBox,
                new Label("Select a date:"),
                datePicker,
                new Label("Duration in minutes:"),
                durationMinutesField,
                suggestTimesButton,
                new Label("Suggested available times:"),
                suggestionsListView,
                new Label("Start time:"),
                startTimeField,
                new Label("End time:"),
                endTimeField,
                submitButton,
                messageLabel
        );

        return layout;
    }

    private void loadSpacesIntoComboBox() {
        List<Space> spaces = spaceController.getAllSpaces();

        spaceComboBox.setItems(FXCollections.observableArrayList(spaces));

        if (!spaces.isEmpty()) {
            spaceComboBox.setValue(spaces.get(0));
        }

        spaceComboBox.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Space space, boolean empty) {
                super.updateItem(space, empty);

                if (empty || space == null) {
                    setText(null);
                } else {
                    setText(space.getName());
                }
            }
        });

        spaceComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Space space, boolean empty) {
                super.updateItem(space, empty);

                if (empty || space == null) {
                    setText(null);
                } else {
                    setText(space.getName());
                }
            }
        });
    }

    private void suggestAvailableTimes() {
        Space selectedSpace = spaceComboBox.getValue();
        LocalDate selectedDate = datePicker.getValue();

        suggestionsListView.getItems().clear();

        if (selectedSpace == null) {
            messageLabel.setText("Please select a space.");
            return;
        }

        if (selectedDate == null) {
            messageLabel.setText("Please select a date.");
            return;
        }

        try {
            int durationMinutes = Integer.parseInt(durationMinutesField.getText().trim());

            TimeSuggestionResult result = timeSuggestionController.suggestAvailableTimes(
                    selectedSpace.getId(),
                    selectedDate,
                    durationMinutes
            );

            if (!result.isSuccessful()) {
                messageLabel.setText(result.getMessage());
                return;
            }

            List<TimeSlot> suggestions = result.getSuggestions();

            if (suggestions.isEmpty()) {
                messageLabel.setText("No available times found.");
                return;
            }

            suggestionsListView.setItems(FXCollections.observableArrayList(suggestions));
            messageLabel.setText(result.getMessage());
        } catch (NumberFormatException exception) {
            messageLabel.setText("Please enter a valid duration in minutes.");
        }
    }

    public void populateReservationFields(
            Space space,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime
    ) {
        spaceComboBox.setValue(space);
        datePicker.setValue(date);
        startTimeField.setText(startTime.toString());
        endTimeField.setText(endTime.toString());
        messageLabel.setText("Suggested time selected. Review the times, then create the reservation.");
    }

    private void clearSuggestions() {
        suggestionsListView.getItems().clear();
        suggestionsListView.getSelectionModel().clearSelection();
    }

    private void createReservation() {
        Space selectedSpace = spaceComboBox.getValue();
        LocalDate selectedDate = datePicker.getValue();

        if (selectedSpace == null) {
            messageLabel.setText("Please select a space.");
            return;
        }

        if (selectedDate == null) {
            messageLabel.setText("Please select a date.");
            return;
        }

        try {
            LocalTime startTime = LocalTime.parse(startTimeField.getText().trim());
            LocalTime endTime = LocalTime.parse(endTimeField.getText().trim());

            ReservationValidationResult result = reservationController.createReservation(
                    selectedSpace.getId(),
                    selectedDate,
                    startTime,
                    endTime
            );

            if (result.isValid()) {
                messageLabel.setText("Reservation created successfully.");
                clearTimeFields();
            } else {
                messageLabel.setText(result.getMessage());
            }
        } catch (DateTimeParseException exception) {
            messageLabel.setText("Please enter times in HH:mm format, such as 09:00 or 14:30.");
        }
    }

    private void clearTimeFields() {
        startTimeField.clear();
        endTimeField.clear();
    }
}

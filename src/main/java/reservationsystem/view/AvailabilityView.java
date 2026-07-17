package reservationsystem.view;

import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import reservationsystem.controller.AvailabilityController;
import reservationsystem.controller.SpaceController;
import reservationsystem.model.DatedAvailability;
import reservationsystem.model.Space;
import reservationsystem.model.TimeSlot;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;

public class AvailabilityView {

	private final SpaceController spaceController;
    private final AvailabilityController availabilityController;
    private final AvailabilityDisplayMapper availabilityDisplayMapper;

    private final ComboBox<Space> spaceComboBox;
    private final DatePicker startDatePicker;
    private final DatePicker endDatePicker;
    private final ListView<AvailabilityListItem> availabilityListView;
    private final Label messageLabel;

    public AvailabilityView() {
        this.spaceController = new SpaceController();
        this.availabilityController = new AvailabilityController();
        this.availabilityDisplayMapper = new AvailabilityDisplayMapper();

        this.spaceComboBox = new ComboBox<>();
        this.startDatePicker = new DatePicker();
        this.endDatePicker = new DatePicker();
        this.availabilityListView = new ListView<>();
        this.messageLabel = new Label();

        configureSpaceComboBox();
        configureAvailabilityListView();
    }

    public VBox createView() {
        Label titleLabel = new Label("View Space Availability");
        Label spaceLabel = new Label("Select a space");
        Label startDateLabel = new Label("Select a date");
        Label endDateLabel = new Label("Select end date for range");

        Button loadDayButton = new Button("Load Day Availability");
        loadDayButton.setOnAction(event -> loadDayAvailability());

        Button loadRangeButton = new Button("View Range Availability");
        loadRangeButton.setOnAction(event -> loadRangeAvailability());

        VBox view = new VBox(
                10,
                titleLabel,
                spaceLabel,
                spaceComboBox,
                startDateLabel,
                startDatePicker,
                endDateLabel,
                endDatePicker,
                loadDayButton,
                loadRangeButton,
                messageLabel,
                availabilityListView
        );

        view.setSpacing(10);
        return view;
    }

    private void configureSpaceComboBox() {
        List<Space> spaces = spaceController.getAllSpaces();
        spaceComboBox.setItems(FXCollections.observableArrayList(spaces));

        spaceComboBox.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Space space, boolean empty) {
                super.updateItem(space, empty);
                setText(empty || space == null ? null : space.getName());
            }
        });

        spaceComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Space space, boolean empty) {
                super.updateItem(space, empty);
                setText(empty || space == null ? null : space.getName());
            }
        });
    }

    private void configureAvailabilityListView() {
        availabilityListView.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(AvailabilityListItem item, boolean empty) {
                super.updateItem(item, empty);

                getStyleClass().removeAll(
                        "availability-slot-available",
                        "availability-slot-reserved",
                        "availability-date-header"
                );

                if (empty || item == null) {
                    setText(null);
                    return;
                }

                if (item.isDateHeader()) {
                    setText(formatDateHeader(item.getDate()));
                    getStyleClass().add("availability-date-header");
                    return;
                }

                AvailabilityDisplayState displayState =
                        availabilityDisplayMapper.map(item.getTimeSlot());

                setText(formatTimeSlot(item.getTimeSlot(), displayState));
                getStyleClass().add(displayState.getStyleClass());
            }
        });
    }

    private void loadDayAvailability() {
        Space selectedSpace = spaceComboBox.getValue();
        LocalDate selectedDate = startDatePicker.getValue();

        if (selectedSpace == null) {
            showMessage("Please select a space.");
            availabilityListView.getItems().clear();
            return;
        }

        if (selectedDate == null) {
            showMessage("Please select a date.");
            availabilityListView.getItems().clear();
            return;
        }

        List<TimeSlot> timeSlots = availabilityController.getAvailabilityForDay(
                selectedSpace.getId(),
                selectedDate
        );

        List<AvailabilityListItem> displayItems = new ArrayList<>();
        for (TimeSlot timeSlot : timeSlots) {
            displayItems.add(AvailabilityListItem.forTimeSlot(timeSlot));
        }

        availabilityListView.setItems(FXCollections.observableArrayList(displayItems));
        showMessage("Showing availability for " + selectedSpace.getName() + ".");
    }

    private void loadRangeAvailability() {
        Space selectedSpace = spaceComboBox.getValue();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (selectedSpace == null) {
            showMessage("Please select a space.");
            availabilityListView.getItems().clear();
            return;
        }

        try {
            List<DatedAvailability> dateRangeAvailability =
                    availabilityController.getAvailabilityForDateRange(
                            selectedSpace.getId(),
                            startDate,
                            endDate
                    );

            List<AvailabilityListItem> displayItems = new ArrayList<>();

            for (DatedAvailability datedAvailability : dateRangeAvailability) {
                displayItems.add(AvailabilityListItem.forDateHeader(datedAvailability.getDate()));

                for (TimeSlot timeSlot : datedAvailability.getTimeSlots()) {
                    displayItems.add(AvailabilityListItem.forTimeSlot(timeSlot));
                }
            }

            availabilityListView.setItems(FXCollections.observableArrayList(displayItems));
            showMessage("Showing date range availability for " + selectedSpace.getName() + ".");
        } catch (IllegalArgumentException exception) {
            availabilityListView.getItems().clear();
            showMessage(exception.getMessage());
        }
    }

    private String formatTimeSlot(TimeSlot timeSlot, AvailabilityDisplayState displayState) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);

        return timeSlot.getStartTime().format(timeFormatter)
                + " - "
                + timeSlot.getEndTime().format(timeFormatter)
                + " : "
                + displayState.getStatusText();
    }

    private String formatDateHeader(LocalDate date) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy");
        return date.format(dateFormatter);
    }

    private void showMessage(String message) {
        messageLabel.setText(message);
    }

    private static class AvailabilityListItem {
        private final LocalDate date;
        private final TimeSlot timeSlot;

        private AvailabilityListItem(LocalDate date, TimeSlot timeSlot) {
            this.date = date;
            this.timeSlot = timeSlot;
        }

        static AvailabilityListItem forDateHeader(LocalDate date) {
            return new AvailabilityListItem(date, null);
        }

        static AvailabilityListItem forTimeSlot(TimeSlot timeSlot) {
            return new AvailabilityListItem(null, timeSlot);
        }

        boolean isDateHeader() {
            return date != null;
        }

        LocalDate getDate() {
            return date;
        }

        TimeSlot getTimeSlot() {
            return timeSlot;
        }
    }
}
package reservationsystem.view;

import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import reservationsystem.controller.AvailabilityController;
import reservationsystem.controller.SpaceController;
import reservationsystem.model.Space;
import reservationsystem.model.TimeSlot;
import javafx.scene.control.ListCell;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AvailabilityView {

    private final SpaceController spaceController;
    private final AvailabilityController availabilityController;

    private final ComboBox<Space> spaceComboBox;
    private final DatePicker datePicker;
    private final ListView<TimeSlot> availabilityListView;
    private final Label messageLabel;
    private final AvailabilityDisplayMapper availabilityDisplayMapper;

    public AvailabilityView() {
        this(new SpaceController(), new AvailabilityController());
    }

    public AvailabilityView(SpaceController spaceController, AvailabilityController availabilityController) {
        this.spaceController = spaceController;
        this.availabilityController = availabilityController;
        this.spaceComboBox = new ComboBox<>();
        this.datePicker = new DatePicker();
        this.availabilityListView = new ListView<>();
        configureAvailabilityListView();
        this.messageLabel = new Label();
        this.availabilityDisplayMapper = new AvailabilityDisplayMapper();
    }

    public VBox createView() {
        Label titleLabel = new Label("View Day Availability");

        loadSpacesIntoComboBox();

        datePicker.setValue(LocalDate.now());

        Button loadAvailabilityButton = new Button("Load Availability");
        loadAvailabilityButton.setOnAction(event -> loadAvailability());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(
                titleLabel,
                new Label("Select a space:"),
                spaceComboBox,
                new Label("Select a date:"),
                datePicker,
                loadAvailabilityButton,
                messageLabel,
                availabilityListView
        );

        return layout;
    }

    private void loadSpacesIntoComboBox() {
        List<Space> spaces = spaceController.getAllSpaces();

        spaceComboBox.setItems(FXCollections.observableArrayList(spaces));

        if (!spaces.isEmpty()) {
            spaceComboBox.setValue(spaces.get(0));
        }

        spaceComboBox.setCellFactory(listView -> new javafx.scene.control.ListCell<>() {
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

        spaceComboBox.setButtonCell(new javafx.scene.control.ListCell<>() {
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

    private void loadAvailability() {
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

        List<TimeSlot> timeSlots = availabilityController.getAvailabilityForDay(
                selectedSpace.getId(),
                selectedDate
        );

        availabilityListView.setItems(FXCollections.observableArrayList(timeSlots));
        messageLabel.setText("Availability loaded for " + selectedSpace.getName() + ".");
    }
    
    private void configureAvailabilityListView() {
        availabilityListView.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(TimeSlot timeSlot, boolean empty) {
                super.updateItem(timeSlot, empty);

                getStyleClass().removeAll(
                        "availability-slot-reserved",
                        "availability-slot-available"
                );

                if (empty || timeSlot == null) {
                    setText(null);
                    return;
                }

                AvailabilityDisplayState displayState = availabilityDisplayMapper.map(timeSlot);

                setText(formatTimeSlot(timeSlot, displayState));
                getStyleClass().add(displayState.getStyleClass());
            }
        });
    }

    private String formatTimeSlot(TimeSlot timeSlot, AvailabilityDisplayState displayState) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");

        String start = timeSlot.getStartTime().format(formatter);
        String end = timeSlot.getEndTime().format(formatter);

        return start + " - " + end + " : " + displayState.getStatusText();
    }
}
package reservationsystem.view;

import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import reservationsystem.controller.SpaceController;
import reservationsystem.controller.SpaceFilterResult;
import reservationsystem.model.Space;

import java.util.List;

public class SpaceListView {

    private final SpaceController spaceController;
    private final Label detailsMessageLabel;
    private final Label nameDetailsLabel;
    private final Label buildingDetailsLabel;
    private final Label capacityDetailsLabel;
    private final Label featuresDetailsLabel;

    public SpaceListView() {
        this(new SpaceController());
    }

    public SpaceListView(SpaceController spaceController) {
        this.spaceController = spaceController;
        this.detailsMessageLabel = new Label("Select a space to view its details.");
        this.nameDetailsLabel = new Label();
        this.buildingDetailsLabel = new Label();
        this.capacityDetailsLabel = new Label();
        this.featuresDetailsLabel = new Label();
    }

    public VBox createView() {
        Label titleLabel = new Label("Reservable Spaces");

        TableView<Space> tableView = new TableView<>();
        tableView.setId("spaces-table");

        TableColumn<Space, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Space, String> buildingColumn = new TableColumn<>("Building");
        buildingColumn.setCellValueFactory(new PropertyValueFactory<>("building"));

        TableColumn<Space, Integer> capacityColumn = new TableColumn<>("Capacity");
        capacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));

        tableView.getColumns().add(nameColumn);
        tableView.getColumns().add(buildingColumn);
        tableView.getColumns().add(capacityColumn);

        restoreAllSpaces(tableView);

        Label minimumCapacityLabel = new Label("Minimum capacity:");
        TextField minimumCapacityField = new TextField();
        minimumCapacityField.setId("minimum-capacity-field");
        minimumCapacityField.setPromptText("1-500");

        Label filterMessageLabel = new Label();
        filterMessageLabel.setId("capacity-filter-message");

        Button applyFilterButton = new Button("Apply");
        applyFilterButton.setId("apply-capacity-filter");
        applyFilterButton.setOnAction(event -> applyCapacityFilter(
                minimumCapacityField.getText(),
                tableView,
                filterMessageLabel
        ));

        Button clearFilterButton = new Button("Clear");
        clearFilterButton.setId("clear-capacity-filter");
        clearFilterButton.setOnAction(event -> {
            minimumCapacityField.clear();
            filterMessageLabel.setText("");
            restoreAllSpaces(tableView);
        });

        HBox capacityFilterControls = new HBox(
                5,
                minimumCapacityLabel,
                minimumCapacityField,
                applyFilterButton,
                clearFilterButton
        );

        Button viewDetailsButton = new Button("View Details");
        viewDetailsButton.setOnAction(event -> showSelectedSpaceDetails(
                tableView.getSelectionModel().getSelectedItem()));

        VBox detailsPanel = new VBox(
                5,
                new Label("Space Details"),
                detailsMessageLabel,
                nameDetailsLabel,
                buildingDetailsLabel,
                capacityDetailsLabel,
                featuresDetailsLabel
        );

        VBox layout = new VBox(10);
        layout.getChildren().addAll(
                titleLabel,
                capacityFilterControls,
                filterMessageLabel,
                tableView,
                viewDetailsButton,
                detailsPanel
        );

        return layout;
    }

    private void applyCapacityFilter(
            String minimumCapacityInput,
            TableView<Space> tableView,
            Label filterMessageLabel
    ) {
        SpaceFilterResult result = spaceController.filterByMinimumCapacity(minimumCapacityInput);
        filterMessageLabel.setText(result.message());

        if (result.isValid()) {
            tableView.setItems(FXCollections.observableArrayList(result.spaces()));
            tableView.setPlaceholder(new Label("No spaces match the filter"));
        }
    }

    private void restoreAllSpaces(TableView<Space> tableView) {
        List<Space> spaces = spaceController.getAllSpaces();
        tableView.setItems(FXCollections.observableArrayList(spaces));
        tableView.setPlaceholder(new Label("No spaces available"));
    }

    private void showSelectedSpaceDetails(Space selectedSpace) {
        spaceController.getSelectedSpaceDetails(selectedSpace)
                .ifPresentOrElse(this::displaySpaceDetails, this::displayNoSelectionPrompt);
    }

    private void displaySpaceDetails(Space space) {
        detailsMessageLabel.setText("");
        nameDetailsLabel.setText("Name: " + space.getName());
        buildingDetailsLabel.setText("Building: " + space.getBuilding());
        capacityDetailsLabel.setText("Capacity: " + space.getCapacity());

        String features = space.getFeatures().isEmpty()
                ? "No features available"
                : String.join(", ", space.getFeatures());
        featuresDetailsLabel.setText("Features: " + features);
    }

    private void displayNoSelectionPrompt() {
        detailsMessageLabel.setText("Please select a space.");
        nameDetailsLabel.setText("");
        buildingDetailsLabel.setText("");
        capacityDetailsLabel.setText("");
        featuresDetailsLabel.setText("");
    }
}

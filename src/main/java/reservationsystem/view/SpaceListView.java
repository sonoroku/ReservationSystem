package reservationsystem.view;

import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import reservationsystem.controller.SpaceController;
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

        TableColumn<Space, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Space, String> buildingColumn = new TableColumn<>("Building");
        buildingColumn.setCellValueFactory(new PropertyValueFactory<>("building"));

        TableColumn<Space, Integer> capacityColumn = new TableColumn<>("Capacity");
        capacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));

        tableView.getColumns().add(nameColumn);
        tableView.getColumns().add(buildingColumn);
        tableView.getColumns().add(capacityColumn);

        List<Space> spaces = spaceController.getAllSpaces();

        if (spaces.isEmpty()) {
            tableView.setPlaceholder(new Label("No spaces available"));
        } else {
            tableView.setItems(FXCollections.observableArrayList(spaces));
        }

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
        layout.getChildren().addAll(titleLabel, tableView, viewDetailsButton, detailsPanel);

        return layout;
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

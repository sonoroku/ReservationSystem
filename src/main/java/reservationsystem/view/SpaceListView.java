package reservationsystem.view;

import javafx.collections.FXCollections;
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

    public SpaceListView() {
        this(new SpaceController());
    }

    public SpaceListView(SpaceController spaceController) {
        this.spaceController = spaceController;
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

        VBox layout = new VBox(10);
        layout.getChildren().addAll(titleLabel, tableView);

        return layout;
    }
}
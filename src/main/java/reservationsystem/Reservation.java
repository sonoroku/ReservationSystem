package reservationsystem;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import reservationsystem.view.AvailabilityView;
import reservationsystem.view.SpaceListView;

public class Reservation extends Application {

    @Override
    public void start(Stage stage) {
        SpaceListView spaceListView = new SpaceListView();
        AvailabilityView availabilityView = new AvailabilityView();

        TabPane tabPane = new TabPane();

        Tab spacesTab = new Tab("Spaces");
        spacesTab.setContent(spaceListView.createView());
        spacesTab.setClosable(false);

        Tab availabilityTab = new Tab("Availability");
        availabilityTab.setContent(availabilityView.createView());
        availabilityTab.setClosable(false);

        tabPane.getTabs().addAll(spacesTab, availabilityTab);

        Scene scene = new Scene(tabPane, 800, 600);

        stage.setTitle("Reservation System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
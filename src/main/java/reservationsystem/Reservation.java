package reservationsystem;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import reservationsystem.view.AvailabilityView;
import reservationsystem.view.CreateReservationView;
import reservationsystem.view.MyReservationsView;
import reservationsystem.view.SpaceListView;

public class Reservation extends Application {

    @Override
    public void start(Stage stage) {
        SpaceListView spaceListView = new SpaceListView();
        AvailabilityView availabilityView = new AvailabilityView();
        CreateReservationView createReservationView = new CreateReservationView();
        MyReservationsView myReservationsView = new MyReservationsView();

        TabPane tabPane = new TabPane();

        Tab spacesTab = new Tab("Spaces");
        spacesTab.setContent(spaceListView.createView());
        spacesTab.setClosable(false);

        Tab availabilityTab = new Tab("Availability");
        availabilityTab.setContent(availabilityView.createView());
        availabilityTab.setClosable(false);

        Tab createReservationTab = new Tab("Create Reservation");
        createReservationTab.setContent(createReservationView.createView());
        createReservationTab.setClosable(false);

        Tab myReservationsTab = new Tab("My Reservations");
        myReservationsTab.setContent(myReservationsView);
        myReservationsTab.setClosable(false);

        tabPane.getTabs().addAll(
                spacesTab,
                availabilityTab,
                createReservationTab,
                myReservationsTab
        );

        Scene scene = new Scene(tabPane, 800, 650);
        scene.getStylesheets().add(
                getClass().getResource("/availability-styles.css").toExternalForm()
        );

        stage.setTitle("Reservation System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

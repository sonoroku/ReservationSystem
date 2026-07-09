package reservationsystem;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import reservationsystem.view.SpaceListView;

public class Reservation extends Application {

    @Override
    public void start(Stage stage) {
        SpaceListView spaceListView = new SpaceListView();

        Scene scene = new Scene(spaceListView.createView(), 700, 400);

        stage.setTitle("Reservation System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
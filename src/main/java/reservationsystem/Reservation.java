package reservationsystem;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import reservationsystem.view.AvailabilityView;
import reservationsystem.view.CreateReservationView;
import reservationsystem.view.MyReservationsView;
import reservationsystem.view.SpaceListView;
import reservationsystem.view.RegistrationView;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import reservationsystem.controller.AuthenticationController;
import reservationsystem.controller.ReservationController;
import reservationsystem.controller.SpaceController;
import reservationsystem.view.LoginView;

public class Reservation extends Application {
	
	private Stage primaryStage;
	private AuthenticationController authenticationController;

    @Override
    public void start(Stage stage) {
    	primaryStage = stage;
    	authenticationController = new AuthenticationController();

        stage.setTitle("Reservation System");
        showLoginScreen();
        stage.show();
    }

    private Scene createMainScene() {
        ReservationController reservationController =
                new ReservationController(authenticationController);
        SpaceListView spaceListView = new SpaceListView();
        AvailabilityView availabilityView = new AvailabilityView();
        CreateReservationView createReservationView =
                new CreateReservationView(
                        new SpaceController(),
                        reservationController
                );
        MyReservationsView myReservationsView =
                new MyReservationsView(reservationController);
        RegistrationView registrationView = new RegistrationView();

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
        ScrollPane myReservationsScrollPane = new ScrollPane(myReservationsView);
        myReservationsScrollPane.setFitToWidth(true);
        myReservationsTab.setContent(myReservationsScrollPane);
        myReservationsTab.setClosable(false);
        
        Tab registrationTab = new Tab("Register");
        registrationTab.setContent(registrationView.createView());
        registrationTab.setClosable(false);

        tabPane.getTabs().addAll(
        spacesTab,
        availabilityTab,
        createReservationTab,
        myReservationsTab,
        registrationTab
        );

        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(createSessionBar());
        mainLayout.setCenter(tabPane);

        Scene mainScene = new Scene(mainLayout, 800, 650);
        mainScene.getStylesheets().add(
                getClass()
                        .getResource("/availability-styles.css")
                        .toExternalForm()
        );

        return mainScene;
    }
    
    private HBox createSessionBar() {
        Label currentUserLabel = new Label(
                "Logged in as: "
                        + authenticationController
                                .getCurrentUser()
                                .getUsername()
        );

        Button logoutButton = new Button("Logout");
        logoutButton.setId("logoutButton");
        logoutButton.setOnAction(event -> logout());

        HBox sessionBar = new HBox(
                12,
                currentUserLabel,
                logoutButton
        );

        sessionBar.setAlignment(Pos.CENTER_RIGHT);
        sessionBar.setPadding(new Insets(10));

        return sessionBar;
    }

    private void showLoginScreen() {
        LoginView loginView = new LoginView(
                authenticationController,
                this::showMainApplication
        );

        Scene loginScene = new Scene(
                loginView.createView(),
                500,
                400
        );

        java.net.URL stylesheet =
                getClass().getResource(
                        "/availability-styles.css"
                );

        if (stylesheet != null) {
            loginScene.getStylesheets().add(
                    stylesheet.toExternalForm()
            );
        }

        primaryStage.setScene(loginScene);
        primaryStage.centerOnScreen();
    }

    private void showMainApplication() {
        if (!authenticationController.isLoggedIn()) {
            showLoginScreen();
            return;
        }

        primaryStage.setScene(createMainScene());
        primaryStage.centerOnScreen();
    }

    private void logout() {
        authenticationController.logout();
        showLoginScreen();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

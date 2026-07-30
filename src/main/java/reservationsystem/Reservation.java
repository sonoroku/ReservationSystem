package reservationsystem;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import reservationsystem.view.AvailabilityView;
import reservationsystem.view.CreateReservationView;
import reservationsystem.view.DailySummaryView;
import reservationsystem.view.MyReservationsView;
import reservationsystem.view.SpaceListView;
import reservationsystem.view.RegistrationView;
import reservationsystem.service.AuthenticationNavigation;
import reservationsystem.service.UserService;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import reservationsystem.controller.AuthenticationController;
import reservationsystem.controller.ReservationController;
import reservationsystem.controller.SpaceController;
import reservationsystem.service.AuthorizationService;
import reservationsystem.view.LoginView;

import reservationsystem.controller.AdminReservationController;
import reservationsystem.view.AdminCancelReservationView;
import reservationsystem.view.AdminCreateReservationView;
import reservationsystem.view.AdminReservationModificationView;

public class Reservation extends Application {

	private Stage primaryStage;
	private AuthenticationController authenticationController;
	private AuthenticationNavigation authenticationNavigation;
	private AuthorizationService authorizationService;

    @Override
    public void start(Stage stage) {
    	primaryStage = stage;
    	authenticationController = new AuthenticationController();
		authenticationNavigation = new AuthenticationNavigation();
        authorizationService =
                new AuthorizationService(authenticationController);

        stage.setTitle("Reservation System");
        showLoginScreen();
        stage.show();
    }

    private Scene createMainScene() {
        SpaceController spaceController = new SpaceController();

        ReservationController reservationController =
                new ReservationController(authenticationController);
        SpaceListView spaceListView = new SpaceListView();
        AvailabilityView availabilityView = new AvailabilityView();
        CreateReservationView createReservationView =
                new CreateReservationView(
                        spaceController,
                        reservationController
                );
        MyReservationsView myReservationsView =
                new MyReservationsView(reservationController);
        DailySummaryView dailySummaryView =
                new DailySummaryView(reservationController);
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

        Tab dailySummaryTab = new Tab("Daily Summary");
        ScrollPane dailySummaryScrollPane = new ScrollPane(
                dailySummaryView.createView()
        );
        dailySummaryScrollPane.setFitToWidth(true);
        dailySummaryTab.setContent(dailySummaryScrollPane);
        dailySummaryTab.setClosable(false);

        tabPane.getTabs().addAll(
        spacesTab,
        availabilityTab,
        createReservationTab,
        myReservationsTab,
        dailySummaryTab
        );

        if (authorizationService.isCurrentUserAdmin()) {
            AdminReservationController adminReservationController =
                    new AdminReservationController(
                            authenticationController
                    );

            Runnable refreshReservationViews = () -> {
                myReservationsView.refreshReservations();
                availabilityView.refreshAvailability();
                dailySummaryView.refreshSummary();
            };

            AdminReservationModificationView adminReservationModificationView =
                    new AdminReservationModificationView(
                            adminReservationController,
                            spaceController,
                            refreshReservationViews
                    );

            AdminCreateReservationView adminCreateReservationView =
                    new AdminCreateReservationView(
                            spaceController,
                            adminReservationController,
                            refreshReservationViews
                    );

            Tab adminCreateReservationTab =
                    new Tab("Admin Create Reservation");

            ScrollPane adminCreateScrollPane =
                    new ScrollPane(
                            adminCreateReservationView.createView()
                    );

            adminCreateScrollPane.setFitToWidth(true);
            adminCreateReservationTab.setContent(
                    adminCreateScrollPane
            );
            adminCreateReservationTab.setClosable(false);

            tabPane.getTabs().add(adminCreateReservationTab);

            AdminCancelReservationView adminCancelReservationView =
                    new AdminCancelReservationView(
                            adminReservationController,
                            spaceController,
                            refreshReservationViews
                    );

            Tab adminCancelReservationTab =
                    new Tab("Admin Cancel Reservation");

            ScrollPane adminCancelScrollPane =
                    new ScrollPane(
                            adminCancelReservationView.createView()
                    );

            adminCancelScrollPane.setFitToWidth(true);
            adminCancelReservationTab.setContent(adminCancelScrollPane);
            adminCancelReservationTab.setClosable(false);

            tabPane.getTabs().add(adminCancelReservationTab);

            Tab adminModifyReservationTab =
                    new Tab("Admin Modify Reservation");
            ScrollPane adminModifyScrollPane = new ScrollPane(
                    adminReservationModificationView.createView()
            );
            adminModifyScrollPane.setFitToWidth(true);
            adminModifyReservationTab.setContent(adminModifyScrollPane);
            adminModifyReservationTab.setClosable(false);

            tabPane.getTabs().add(adminModifyReservationTab);
        }

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
                        + " ("
                        + (authorizationService.isCurrentUserAdmin()
                                ? "Administrator"
                                : "Regular User")
                        + ")"
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
                this::showMainApplication,
                this::showRegistrationScreen,
                authenticationNavigation.getLoginMessage()
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

    private void showRegistrationScreen() {
        authenticationNavigation.openRegistration();

        RegistrationView registrationView = new RegistrationView(
                new UserService(),
                this::completeRegistration,
                this::cancelRegistration
        );

        Scene registrationScene = new Scene(
                registrationView.createView(),
                500,
                500
        );

        java.net.URL stylesheet =
                getClass().getResource(
                        "/availability-styles.css"
                );

        if (stylesheet != null) {
            registrationScene.getStylesheets().add(
                    stylesheet.toExternalForm()
            );
        }

        primaryStage.setScene(registrationScene);
        primaryStage.centerOnScreen();
    }

    private void completeRegistration() {
        authenticationNavigation.completeRegistration();
        showLoginScreen();
    }

    private void cancelRegistration() {
        authenticationNavigation.returnToLogin();
        showLoginScreen();
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
		authenticationNavigation.returnToLogin();
        showLoginScreen();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

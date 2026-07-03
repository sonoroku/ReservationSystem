module reservationsystem {
    requires javafx.controls;
    requires javafx.fxml;


    opens reservationsystem to javafx.fxml;
    exports reservationsystem;
}
module reservationsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;


    opens reservationsystem to javafx.fxml;
    exports reservationsystem;
}
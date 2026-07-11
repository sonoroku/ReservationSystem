module reservationsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    exports reservationsystem;
    exports reservationsystem.model;
    exports reservationsystem.controller;
    exports reservationsystem.persistence;
    exports reservationsystem.view;
    exports reservationsystem.service;

    opens reservationsystem to javafx.fxml;
    opens reservationsystem.model to javafx.base;
}
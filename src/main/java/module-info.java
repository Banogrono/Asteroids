

module Asteroids {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.asteroids to javafx.fxml;
    exports com.asteroids;

    opens com.asteroids.controllers to javafx.fxml;
    exports com.asteroids.controllers;

}
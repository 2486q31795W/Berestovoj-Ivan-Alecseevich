module com.train.trainroutemanagement {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.train.trainroutemanagement to javafx.fxml;
    exports com.train.trainroutemanagement;
}
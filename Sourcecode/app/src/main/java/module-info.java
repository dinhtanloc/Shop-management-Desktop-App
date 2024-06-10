module com.example.app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.prefs;
    requires java.desktop;
    requires com.opencsv;

//    requires com.gluonhq.charm.glisten;




    opens com.example.app to javafx.fxml;
    exports com.example.app;
    exports com.example.app.controllers;
    opens com.example.app.controllers to javafx.fxml;
    exports com.example.app.controllers.tabs to javafx.fxml;
    opens com.example.app.controllers.tabs to javafx.fxml;


}
module my.app.ims {
    requires javafx.controls;
    requires javafx.fxml;


    opens my.app.ims_v2 to javafx.fxml;
    exports my.app.ims_v2;
    exports controller;
    opens controller to javafx.fxml;
}
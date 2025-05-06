module org.example.mobileprovider {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;

    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;

    opens mobileprovider to javafx.fxml;
    exports mobileprovider;
    exports controller;
    opens controller to javafx.fxml;
}
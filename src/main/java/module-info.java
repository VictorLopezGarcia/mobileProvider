module org.example.mobileprovider {
    requires javafx.fxml;
    requires javafx.graphics;

    requires org.kordamp.bootstrapfx.core;
    requires org.controlsfx.controls;
    requires java.desktop;
    requires jdk.compiler;

    opens mobileprovider to javafx.fxml;
    exports mobileprovider;
    exports controller;
    opens controller to javafx.fxml;
}
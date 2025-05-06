module org.example.model {
    requires javafx.fxml;
    requires javafx.graphics;

    requires org.kordamp.bootstrapfx.core;
    requires org.controlsfx.controls;
    requires java.desktop;
    requires jdk.compiler;

    opens model to javafx.fxml;
    exports model;
    exports controller;
    opens controller to javafx.fxml;
}
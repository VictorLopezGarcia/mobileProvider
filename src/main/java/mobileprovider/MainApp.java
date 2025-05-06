package mobileprovider;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.util.Objects;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Catalogo.instanciarBd();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/views/dashboard-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load()); // Ajusta las dimensiones
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm()); // Añade tu archivo CSS
        stage.setTitle("Catálogo de Móviles");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
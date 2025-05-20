package model;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.util.Objects;

public class MainApp extends Application {

    // Método principal de arranque de JavaFX. Se ejecuta al iniciar la aplicación.
    @Override
    public void start(Stage stage) throws IOException {
        // Carga los datos del catálogo de móviles desde el CSV
        Catalogo.instanciarBd();

        // Carga el archivo FXML del dashboard principal (interfaz de usuario)
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/views/dashboard-view.fxml"));

        // Crea la escena a partir del contenido cargado del FXML
        Scene scene = new Scene(fxmlLoader.load());

        // Aplica el archivo CSS de estilos a la escena
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm());

        // Define el título de la ventana principal
        stage.setTitle("Catálogo de Móviles");

        // Establece la escena en el escenario principal (ventana)
        stage.setScene(scene);

        // Muestra la ventana
        stage.show();
    }

    // Método main. Lanza la aplicación JavaFX.
    public static void main(String[] args) {
        launch(); // Llama internamente al método start()
    }
}


package controller;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContentDisplay;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import model.Movil;
import org.controlsfx.control.GridCell;

import java.io.IOException;

public class MovilGridCell extends GridCell<Movil> {

    // Controlador de la tarjeta de móvil
    private MovilCardController controller;

    // Constructor de la celda
    public MovilGridCell() {
        try {
            // Carga el archivo FXML que define la vista de una tarjeta de móvil
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/movil-card.fxml"));
            VBox view = loader.load();
            controller = loader.getController(); // Obtiene el controlador asociado al FXML
            setGraphic(view); // Asigna la vista gráfica a la celda
        } catch (IOException e) {
            e.printStackTrace(); // Muestra el error si falla la carga
        }
    }

    // Este método se ejecuta cada vez que se necesita actualizar el contenido de una celda
    @Override
    protected void updateItem(Movil movil, boolean empty) {
        super.updateItem(movil, empty);

        if (empty || movil == null) {
            // Si no hay datos, no se muestra nada
            setGraphic(null);
        } else {
            // Se pasan los datos del móvil al controlador
            controller.setMovil(movil);

            // Se establece la vista ya configurada como contenido de la celda
            setGraphic(controller.getView());
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY); // Solo se muestra la parte gráfica (sin texto)

            // Si el móvil no ha sido animado aún, se le aplica una animación
            if (!DashboardController.movilesAnimados.contains(movil.getModelo())) {
                // Animación de aparición (fade in)
                FadeTransition fade = new FadeTransition(Duration.millis(300), controller.getView());
                fade.setFromValue(0);
                fade.setToValue(1);

                // Animación de desplazamiento desde abajo (slide)
                TranslateTransition slide = new TranslateTransition(Duration.millis(300), controller.getView());
                slide.setFromY(20);
                slide.setToY(0);

                // Se combinan ambas animaciones para que ocurran a la vez
                ParallelTransition animacion = new ParallelTransition(fade, slide);
                animacion.play(); // Se ejecuta la animación

                // Se guarda el modelo para no volver a animarlo
                DashboardController.movilesAnimados.add(movil.getModelo());
            }
        }
    }
}

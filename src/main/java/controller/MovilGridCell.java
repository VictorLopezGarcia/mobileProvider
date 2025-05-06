
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

    private MovilCardController controller;

    public MovilGridCell() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/movil-card.fxml"));
            VBox view = loader.load();
            controller = loader.getController();
            setGraphic(view); // Set it once
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void updateItem(Movil movil, boolean empty) {
        super.updateItem(movil, empty);
        if (empty || movil == null) {
            setGraphic(null);
        } else {
            controller.setMovil(movil); // Actualiza los datos
            setGraphic(controller.getView()); // Reusa la vista
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

            if (!DashboardController.movilesAnimados.contains(movil.getModelo())) {
                FadeTransition fade = new FadeTransition(Duration.millis(300), controller.getView());
                fade.setFromValue(0);
                fade.setToValue(1);

                TranslateTransition slide = new TranslateTransition(Duration.millis(300), controller.getView());
                slide.setFromY(20);
                slide.setToY(0);

                ParallelTransition animacion = new ParallelTransition(fade, slide);
                animacion.play();

                DashboardController.movilesAnimados.add(movil.getModelo());
            }
        }
    }
}

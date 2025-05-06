
package controller;

import javafx.fxml.FXMLLoader;
import mobileprovider.Movil;
import org.controlsfx.control.GridCell;

public class MovilGridCell extends GridCell<Movil> {
    @Override
    protected void updateItem(Movil movil, boolean empty) {
        super.updateItem(movil, empty);
        if (empty || movil == null) {
            setGraphic(null);
        } else {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/movil-card.fxml"));
                loader.load();
                MovilCardController controller = loader.getController();
                controller.setMovil(movil);
                setGraphic(controller.getView());
            } catch (Exception e) {
                e.printStackTrace();
                setGraphic(null);
            }
        }
    }
}

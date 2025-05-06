package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import mobileprovider.Catalogo;
import mobileprovider.Movil;

import java.io.IOException;

public class DashboardController {

    @FXML
    private FlowPane catalogoContainer;

    @FXML
    public void initialize() {
        // Aquí cargarías los datos de los móviles y crearías las "cards"
        cargarCatalogo();
    }



    private void cargarCatalogo() {
        // Simulación de datos de móviles
        catalogoContainer.getChildren().clear(); // Limpia el catálogo anterior si es necesario
        for (Movil movil : Catalogo.ListaMovilesModelo()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/movil-card.fxml"));
                VBox movilCard = loader.load();

                // Obtiene el controlador de la card cargada
                MovilCardController cardController = loader.getController();

                // Pasa los datos del móvil al controlador de la card
                cardController.setMovil(movil);

                catalogoContainer.getChildren().add(movilCard);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void mostrarTodos(ActionEvent event) {
        System.out.println("Mostrar todos los móviles.");
        // Implementa la lógica para mostrar todos los móviles
        cargarCatalogo(); // Vuelve a cargar el catálogo (simplificado)
    }

    @FXML
    public void filtrarPorMarca(ActionEvent event) {
        System.out.println("Filtrar por marca.");
        // Implementa la lógica para filtrar por marca
    }

    @FXML
    public void filtrarPorPrecio(ActionEvent event) {
        System.out.println("Filtrar por precio.");
        // Implementa la lógica para filtrar por precio
    }
}
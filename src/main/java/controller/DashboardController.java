package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.controlsfx.control.GridView;
import mobileprovider.Catalogo;
import mobileprovider.Movil;

public class DashboardController {

    @FXML
    private GridView<Movil> catalogoGrid;

    @FXML
    private TextField campoBusqueda;

    private ObservableList<Movil> listaMoviles;
    private FilteredList<Movil> listaFiltrada;

    @FXML
    public void initialize() {
        listaMoviles = FXCollections.observableArrayList(Catalogo.ListaMovilesModelo());
        listaFiltrada = new FilteredList<>(listaMoviles, m -> true);
        SortedList<Movil> listaOrdenada = new SortedList<>(listaFiltrada);

        catalogoGrid.setItems(listaOrdenada);
        catalogoGrid.setCellFactory(grid -> new MovilGridCell());
        catalogoGrid.setCellWidth(200);
        catalogoGrid.setCellHeight(250);

        campoBusqueda.setPromptText("Buscar por marca o modelo");
        Text icon = new Text("\ue322"); // Código Unicode del icono
        icon.getStyleClass().add("material-icons");




        // Búsqueda dinámica
        campoBusqueda.textProperty().addListener((obs, oldVal, newVal) -> {
            String filtro = newVal == null ? "" : newVal.toLowerCase();
            listaFiltrada.setPredicate(movil ->
                    movil.getMarca().toLowerCase().contains(filtro) ||
                            movil.getModelo().toLowerCase().contains(filtro)
            );
        });
    }

    @FXML
    public void mostrarTodos() {
        listaFiltrada.setPredicate(m -> true);
    }

    @FXML
    public void filtrarPorMarcaSamsung() {
        listaFiltrada.setPredicate(m -> m.getMarca().equalsIgnoreCase("Samsung"));
    }

    @FXML
    public void filtrarPorPrecioMenor500() {
        listaFiltrada.setPredicate(m -> m.getPrecio() < 500);
    }
}
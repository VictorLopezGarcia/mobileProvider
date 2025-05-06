package controller;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.controlsfx.control.GridView;
import model.Catalogo;
import model.Movil;

import java.util.HashSet;
import java.util.Set;

public class DashboardController {

    @FXML
    private GridView<Movil> catalogoGrid;

    @FXML
    private TextField campoBusqueda;

    @FXML
    private VBox sidebar;

    @FXML
    private Button btnInicio, btnSamsung, btnBarato;

    private boolean sidebarExpandido = false;

    private final double WIDTH_EXPANDIDO = 200;
    private final double WIDTH_CONTRAIDO = 60;

    private ObservableList<Movil> listaMoviles;
    private FilteredList<Movil> listaFiltrada;

    public static Set<String> movilesAnimados = new HashSet<>();


    @FXML
    public void initialize() {
        listaMoviles = FXCollections.observableArrayList(Catalogo.ListaMovilesModelo());
        listaFiltrada = new FilteredList<>(listaMoviles, m -> true);
        SortedList<Movil> listaOrdenada = new SortedList<>(listaFiltrada);

        catalogoGrid.setItems(listaOrdenada);
        catalogoGrid.setCellFactory(grid -> new MovilGridCell());
        catalogoGrid.setCellWidth(200);
        catalogoGrid.setCellHeight(250);

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
        DashboardController.movilesAnimados.clear();
        listaFiltrada.setPredicate(m -> true);
    }

    @FXML
    public void filtrarPorMarcaSamsung() {
        DashboardController.movilesAnimados.clear();
        listaFiltrada.setPredicate(m -> m.getMarca().equalsIgnoreCase("Samsung"));
    }

    @FXML
    public void filtrarPorPrecioMenor500() {
        DashboardController.movilesAnimados.clear();
        listaFiltrada.setPredicate(m -> m.getPrecio() < 500);
    }

    @FXML
    private void toggleSidebar() {
        double targetWidth = sidebarExpandido ? WIDTH_CONTRAIDO : WIDTH_EXPANDIDO;

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(200),
                        new KeyValue(sidebar.prefWidthProperty(), targetWidth, Interpolator.EASE_BOTH)
                )
        );
        timeline.play();

        sidebarExpandido = !sidebarExpandido;

        actualizarTextoBotones(sidebarExpandido);
    }

    private void actualizarTextoBotones(boolean mostrarTexto) {
        btnInicio.setText(mostrarTexto ? "Inicio" : "");
        btnSamsung.setText(mostrarTexto ? "Samsung" : "");
        btnBarato.setText(mostrarTexto ? "Barato < 500€" : "");
    }
}
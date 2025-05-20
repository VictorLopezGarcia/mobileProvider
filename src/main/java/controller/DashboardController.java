package controller;

import javafx.animation.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;


import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.controlsfx.control.GridView;
import model.Catalogo;
import model.Movil;
import org.controlsfx.control.RangeSlider;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class DashboardController {

    @FXML
    private GridView<Movil> catalogoGrid;

    @FXML
    private TextField campoBusqueda;

    @FXML
    private VBox sidebar, sidebarOpciones;

    @FXML
    private Button btnInicio, btnOpciones, btnOrdenPrecio;

    @FXML
    private RangeSlider precioRangeSlider;

    @FXML
    private ComboBox<String> comboMarcas;

    @FXML
    private Text btnFiltro;

    @FXML private Label valueLabel;


    private boolean ordenAscendente = true;
    private boolean sidebarExpandido = false;

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

        campoBusqueda.setOnKeyPressed(e -> {
            if (e.getCode().toString().equals("ENTER")) {
                String filtro = campoBusqueda.getText();
                listaFiltrada.setPredicate(movil ->
                        movil.getMarca().toLowerCase().contains(filtro.toLowerCase()) ||
                                movil.getModelo().toLowerCase().contains(filtro.toLowerCase())
                );
            }
        });
        TreeSet<String> marcas = new TreeSet<>();
        for (Movil m : listaMoviles) {
            marcas.add(m.getMarca());
        }
        comboMarcas.setItems(FXCollections.observableArrayList(marcas));
        comboMarcas.setOnAction(e -> {
            String marca = comboMarcas.getValue();
            if (marca != null) {
                movilesAnimados.clear();
                listaFiltrada.setPredicate(m -> m.getMarca().equalsIgnoreCase(marca));
            }
        });

        btnOrdenPrecio.setOnAction(e -> {
            RotateTransition rt = new RotateTransition(Duration.millis(300), btnFiltro);
            rt.setByAngle(180);
            rt.play();
            ordenAscendente = !ordenAscendente;
            SortedList<Movil> listaOrdenadaBtn = new SortedList<>(listaFiltrada,
                    (m1, m2) -> ordenAscendente ?
                            Double.compare(m1.getPrecio(), m2.getPrecio()) :
                            Double.compare(m2.getPrecio(), m1.getPrecio())
            );
            catalogoGrid.setItems(listaOrdenadaBtn);


        });

        precioRangeSlider.setLowValue(0);
        precioRangeSlider.setMin(0);
        precioRangeSlider.setHighValue(Catalogo.masCaro().getPrecio());
        precioRangeSlider.setMax(Catalogo.masCaro().getPrecio());




        precioRangeSlider.lowValueProperty().addListener((_, _, _) -> updateLabels());
        precioRangeSlider.highValueProperty().addListener((_, _, _) -> updateLabels());
        precioRangeSlider.widthProperty().addListener((_, _, _) -> updateLabels());

    }

    @FXML
    public void mostrarTodos() {
        DashboardController.movilesAnimados.clear();
        listaFiltrada.setPredicate(m -> true);
    }

    @FXML
    private void toggleSidebar() {
        if (sidebarOpciones.isVisible() && sidebarExpandido) {
            mostrarMenuOpciones();
        }
        double WIDTH_EXPANDIDO = 200;
        double WIDTH_CONTRAIDO = 50;
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

    @FXML
    public void filtrarPorRangoPrecio() {
        double min = precioRangeSlider.getLowValue();
        double max = precioRangeSlider.getHighValue();
        DashboardController.movilesAnimados.clear();
        listaFiltrada.setPredicate(m -> m.getPrecio() >= min && m.getPrecio() <= max);
    }


    @FXML
    public void mostrarMenuOpciones() {
        if (!sidebarExpandido) {
            toggleSidebar();

        }
        double alturaMax = sidebarOpciones.getHeight() > 0 ? sidebarOpciones.getHeight() : 180; // Ajusta si es necesario
        boolean mostrar = !sidebarOpciones.isVisible();

        // Clip rectangular para el efecto cortina
        javafx.scene.shape.Rectangle clip = (javafx.scene.shape.Rectangle) sidebarOpciones.getClip();
        if (clip == null) {
            clip = new javafx.scene.shape.Rectangle(180, 0);
            sidebarOpciones.setClip(clip);
        }
        clip.setWidth(180);

        double startHeight = mostrar ? 0 : alturaMax;
        double endHeight = mostrar ? alturaMax : 0;
        double startOpacity = mostrar ? 0 : 1;
        double endOpacity = mostrar ? 1 : 0;

        sidebarOpciones.setVisible(true);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(clip.heightProperty(), startHeight, Interpolator.LINEAR),
                        new KeyValue(sidebarOpciones.opacityProperty(), startOpacity, Interpolator.LINEAR)
                ),
                new KeyFrame(Duration.millis(250),
                        new KeyValue(clip.heightProperty(), endHeight, Interpolator.LINEAR),
                        new KeyValue(sidebarOpciones.opacityProperty(), endOpacity, Interpolator.LINEAR)
                )
        );

        timeline.setOnFinished(e -> {
            if (!mostrar) {
                sidebarOpciones.setVisible(false);
            }
        });

        timeline.play();
    }

    private void updateLabels() {
        valueLabel.setText(String.format("%.2f - %.2f €", precioRangeSlider.getLowValue(), precioRangeSlider.getHighValue()));
        System.out.println(precioRangeSlider.getWidth());
    }


    private void actualizarTextoBotones(boolean mostrarTexto) {
        btnInicio.setText(mostrarTexto ? "Mostrar Todos" : "");
        btnOpciones.setText(mostrarTexto ? "Filtrar" : "");
    }
}
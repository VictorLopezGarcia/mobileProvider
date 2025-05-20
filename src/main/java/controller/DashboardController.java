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

    // Elementos de la interfaz gráfica
    @FXML private GridView<Movil> catalogoGrid; // Vista en cuadrícula para mostrar los móviles
    @FXML private TextField campoBusqueda; // Campo para escribir texto y buscar móviles
    @FXML private VBox sidebar, sidebarOpciones; // Menús laterales (principal y opciones)
    @FXML private Button btnInicio, btnOpciones, btnOrdenPrecio; // Botones de la interfaz
    @FXML private RangeSlider precioRangeSlider; // Control para seleccionar un rango de precio
    @FXML private ComboBox<String> comboMarcas; // Menú desplegable para seleccionar marcas
    @FXML private Text btnFiltro; // Texto que se gira al ordenar por precio
    @FXML private Label valueLabel; // Etiqueta que muestra el rango actual de precios

    private boolean ordenAscendente = true; // Controla el orden ascendente o descendente
    private boolean sidebarExpandido = false; // Controla si el menú lateral está expandido

    private ObservableList<Movil> listaMoviles; // Lista observable con todos los móviles
    private FilteredList<Movil> listaFiltrada; // Lista filtrada según búsqueda o filtros

    // Conjunto para guardar móviles animados y evitar repetir animaciones
    public static Set<String> movilesAnimados = new HashSet<>();

    @FXML
    public void initialize() {
        // Cargamos la lista de móviles desde el catálogo
        listaMoviles = FXCollections.observableArrayList(Catalogo.ListaMovilesModelo());
        listaFiltrada = new FilteredList<>(listaMoviles, m -> true);
        SortedList<Movil> listaOrdenada = new SortedList<>(listaFiltrada);

        // Mostramos los móviles en la vista en cuadrícula
        catalogoGrid.setItems(listaOrdenada);
        catalogoGrid.setCellFactory(grid -> new MovilGridCell());
        catalogoGrid.setCellWidth(200);
        catalogoGrid.setCellHeight(250);

        // Filtro de búsqueda al escribir
        campoBusqueda.textProperty().addListener((obs, oldVal, newVal) -> {
            String filtro = newVal == null ? "" : newVal.toLowerCase();
            listaFiltrada.setPredicate(movil ->
                    movil.getMarca().toLowerCase().contains(filtro) ||
                            movil.getModelo().toLowerCase().contains(filtro)
            );
        });

        // Al pulsar ENTER también se aplica el filtro
        campoBusqueda.setOnKeyPressed(e -> {
            if (e.getCode().toString().equals("ENTER")) {
                String filtro = campoBusqueda.getText();
                listaFiltrada.setPredicate(movil ->
                        movil.getMarca().toLowerCase().contains(filtro.toLowerCase()) ||
                                movil.getModelo().toLowerCase().contains(filtro.toLowerCase())
                );
            }
        });

        // Rellenamos el comboBox con todas las marcas disponibles
        TreeSet<String> marcas = new TreeSet<>();
        for (Movil m : listaMoviles) {
            marcas.add(m.getMarca());
        }
        comboMarcas.setItems(FXCollections.observableArrayList(marcas));

        // Al seleccionar una marca, filtramos la lista
        comboMarcas.setOnAction(e -> {
            String marca = comboMarcas.getValue();
            if (marca != null) {
                movilesAnimados.clear();
                listaFiltrada.setPredicate(m -> m.getMarca().equalsIgnoreCase(marca));
            }
        });

        // Botón para ordenar por precio (ascendente/descendente)
        btnOrdenPrecio.setOnAction(e -> {
            // Rotamos el icono del filtro para dar feedback visual
            RotateTransition rt = new RotateTransition(Duration.millis(300), btnFiltro);
            rt.setByAngle(180);
            rt.play();

            // Cambiamos el orden del comparador
            ordenAscendente = !ordenAscendente;
            SortedList<Movil> listaOrdenadaBtn = new SortedList<>(listaFiltrada,
                    (m1, m2) -> ordenAscendente ?
                            Double.compare(m1.getPrecio(), m2.getPrecio()) :
                            Double.compare(m2.getPrecio(), m1.getPrecio())
            );
            catalogoGrid.setItems(listaOrdenadaBtn);
        });

        // Configuramos los valores iniciales del slider de precio
        precioRangeSlider.setLowValue(0);
        precioRangeSlider.setMin(0);
        precioRangeSlider.setHighValue(Catalogo.masCaro().getPrecio());
        precioRangeSlider.setMax(Catalogo.masCaro().getPrecio());

        // Actualizamos las etiquetas cuando el usuario mueve el slider
        precioRangeSlider.lowValueProperty().addListener((_, _, _) -> updateLabels());
        precioRangeSlider.highValueProperty().addListener((_, _, _) -> updateLabels());
        precioRangeSlider.widthProperty().addListener((_, _, _) -> updateLabels());
    }

    @FXML
    public void mostrarTodos() {
        // Eliminamos todos los filtros aplicados
        DashboardController.movilesAnimados.clear();
        listaFiltrada.setPredicate(m -> true);
    }

    @FXML
    private void toggleSidebar() {
        // Muestra u oculta el menú lateral

        if (sidebarOpciones.isVisible() && sidebarExpandido) {
            mostrarMenuOpciones();
        }

        double WIDTH_EXPANDIDO = 200;
        double WIDTH_CONTRAIDO = 50;
        double targetWidth = sidebarExpandido ? WIDTH_CONTRAIDO : WIDTH_EXPANDIDO;

        // Animación para cambiar el ancho del menú lateral
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
        // Filtra la lista de móviles por el rango seleccionado en el slider
        double min = precioRangeSlider.getLowValue();
        double max = precioRangeSlider.getHighValue();
        DashboardController.movilesAnimados.clear();
        listaFiltrada.setPredicate(m -> m.getPrecio() >= min && m.getPrecio() <= max);
    }

    @FXML
    public void mostrarMenuOpciones() {
        // Muestra u oculta el panel de opciones con animación

        if (!sidebarExpandido) {
            toggleSidebar(); // Asegura que el sidebar esté expandido
        }

        double alturaMax = sidebarOpciones.getHeight() > 0 ? sidebarOpciones.getHeight() : 180;
        boolean mostrar = !sidebarOpciones.isVisible();

        // Creamos el efecto cortina con un "clip" rectangular
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

        // Animación de apertura/cierre
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

    // Actualiza la etiqueta que muestra el rango de precios actual
    private void updateLabels() {
        valueLabel.setText(String.format("%.2f - %.2f €", precioRangeSlider.getLowValue(), precioRangeSlider.getHighValue()));
        System.out.println(precioRangeSlider.getWidth());
    }

    // Cambia el texto de los botones según si el menú lateral está abierto o no
    private void actualizarTextoBotones(boolean mostrarTexto) {
        btnInicio.setText(mostrarTexto ? "Mostrar Todos" : "");
        btnOpciones.setText(mostrarTexto ? "Filtrar" : "");
    }
}

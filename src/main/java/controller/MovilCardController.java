package controller;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import model.Movil;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static javafx.scene.paint.Color.BLACK;

public class MovilCardController {

    @FXML
    private VBox tarjeta;

    @FXML
    private ImageView imageView;

    @FXML
    private Label modeloLabel;

    @FXML
    private Label spec1Label;

    @FXML
    private Label spec2Label;

    @FXML
    private Label spec3Label;

    @FXML
    private Label spec4Label;

    @FXML
    private Label precioLabel;

    @FXML
    private Text color;

    private Movil movil;

    // Mapa que guarda las imágenes cargadas para no tener que volver a cargarlas
    private static final Map<String, Image> imageCache = new HashMap<>();

    // Devuelve la vista (la tarjeta completa)
    public VBox getView() {
        return tarjeta;
    }

    // Este método recibe un objeto Movil y actualiza todos los datos de la tarjeta
    public void setMovil(Movil movil) {
        this.movil = movil;
        if (movil != null) {
            // Muestra la marca y el modelo en el primer label
            modeloLabel.setText(movil.getMarca() + " " + movil.getModelo());
            if (!movil.getCaracteristicas().isEmpty()) {
                spec1Label.setText(movil.getCaracteristicas().getFirst());
            } else {
                spec1Label.setText("");
            }
            if (movil.getCaracteristicas().size() > 1) {
                spec2Label.setText(movil.getCaracteristicas().get(1));
            } else {
                spec2Label.setText("");
            }
            if (movil.getCaracteristicas().size() > 2) {
                spec3Label.setText(movil.getCaracteristicas().get(2));
                try {
                    // Intenta aplicar el color especificado como fondo al texto
                    color.setFill(Color.web(movil.getCaracteristicas().get(2).substring(movil.getCaracteristicas().get(2).indexOf(":") + 1).trim().toLowerCase()));
                } catch (IllegalArgumentException e) {
                    // Si el color no es válido, se usa color negro
                    color.setFill(BLACK);
                }

            } else {
                spec3Label.setText("");
            }
            if (movil.getCaracteristicas().size() > 3) {
                spec4Label.setText(movil.getCaracteristicas().get(3));
            } else {
                spec4Label.setText("");
            }

            // Muestra el precio con dos decimales
            precioLabel.setText("Precio: " + String.format("%.2f€", movil.getPrecio()));

            // Carga la imagen del móvil
            String imagePath = movil.getRutaImagen();
            if (imagePath != null && !imagePath.isEmpty()) {
                // Si la imagen ya está guardada en caché, se reutiliza
                if (imageCache.containsKey(imagePath)) {
                    imageView.setImage(imageCache.get(imagePath));
                } else {

                    Task<Image> loadImageTask = getImageTask(imagePath);
                    new Thread(loadImageTask).start();
                }
            } else {
                // Si no hay imagen, se muestra una imagen por defecto
                imageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/database/imagenes/no_available.png"))));
            }
        }
    }

    // Crea una tarea para cargar la imagen desde el recurso
    private Task<Image> getImageTask(String imagePath) {
        Task<Image> loadImageTask = new Task<>() {
            @Override
            protected Image call() throws Exception {
                Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
                imageCache.put(imagePath, image); // Guarda la imagen en caché
                return image;
            }
        };

        // Si se carga correctamente, se muestra en el imageView
        loadImageTask.setOnSucceeded(event -> imageView.setImage(loadImageTask.getValue()));

        // Si falla la carga, se muestra la imagen por defecto y se guarda en caché
        loadImageTask.setOnFailed(event -> {
            System.err.println("Error al cargar la imagen: " + imagePath);
            loadImageTask.getException().printStackTrace();
            imageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/database/imagenes/no_available.png"))));
            imageCache.put(imagePath, new Image(Objects.requireNonNull(getClass().getResourceAsStream("/database/imagenes/no_available.png"))));
        });

        return loadImageTask;
    }
}

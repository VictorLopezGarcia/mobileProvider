package controller;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.Movil;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
    private Label precioLabel;

    private Movil movil;

    private static final Map<String, Image> imageCache = new HashMap<>();


    public VBox getView() {
        return tarjeta;
    }

    public void setMovil(Movil movil) {
        this.movil = movil;
        if (movil != null) {
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
            precioLabel.setText("Precio: " + String.format("%.2f€", movil.getPrecio()));


            String imagePath = movil.getRutaImagen();
            if (imagePath != null && !imagePath.isEmpty()) {
                if (imageCache.containsKey(imagePath)) {
                    imageView.setImage(imageCache.get(imagePath));
                } else {

                    Task<Image> loadImageTask = getImageTask(imagePath);

                    new Thread(loadImageTask).start();
                }
            } else {
                imageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/database/imagenes/no_available.png"))));
            }
        }
    }

    private Task<Image> getImageTask(String imagePath) {
        Task<Image> loadImageTask = new Task<>() {
            @Override
            protected Image call() throws Exception {
                Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
                imageCache.put(imagePath, image);
                return image;
            }
        };

        loadImageTask.setOnSucceeded(event -> imageView.setImage(loadImageTask.getValue()));
        loadImageTask.setOnFailed(event -> {
            System.err.println("Error al cargar la imagen: " + imagePath);
            loadImageTask.getException().printStackTrace();
            imageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/database/imagenes/no_available.png"))));
            imageCache.put(imagePath, new Image(Objects.requireNonNull(getClass().getResourceAsStream("/database/imagenes/no_available.png")))); // Cachear también la imagen de error
        });
        return loadImageTask;
    }
}
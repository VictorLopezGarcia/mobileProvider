package controller;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import mobileprovider.Movil;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MovilCardController {

    @FXML
    private ImageView imageView;

    @FXML
    private Label nombreMovilLabel;

    @FXML
    private Label spec1Label;

    @FXML
    private Label spec2Label;

    @FXML
    private Label precioLabel;

    @FXML
    private Button comprarButton;

    @FXML
    private ImageView iconoEspecificacion1;

    @FXML
    private ImageView iconoEspecificacion2;

    @FXML
    private ImageView iconoPrecio;


    private Movil movil;
    private static final Map<String, Image> imageCache = new HashMap<>();

    public void setMovil(Movil movil) {
        this.movil = movil;
        actualizarCard();

        VBox vbox = (VBox) imageView.getParent();
        vbox.setOnMouseClicked(event -> handleMouseClick());
    }

    private void actualizarCard() {


        if (movil != null) {
            nombreMovilLabel.setText(movil.getMarca() + " " + movil.getModelo());
            if (!movil.getCaracteristicas().isEmpty()) {
                spec1Label.setText(movil.getCaracteristicas().getFirst());
                Text icon = new Text("\ue412"); // Código Unicode del icono
                icon.getStyleClass().add("material-icons");
                spec1Label.setGraphic(icon);
                spec1Label.setContentDisplay(ContentDisplay.LEFT);
            } else {
                spec1Label.setText("");
            }
            if (movil.getCaracteristicas().size() > 1) {
                spec2Label.setText(movil.getCaracteristicas().get(1));
                Text icon = new Text("\ue322"); // Código Unicode del icono
                icon.getStyleClass().add("material-icons");
                spec2Label.setGraphic(icon);
                spec2Label.setContentDisplay(ContentDisplay.LEFT);
            } else {
                spec2Label.setText("");
            }
            precioLabel.setText("Precio: " + String.format("%.2f€", movil.getPrecio()));

            String imagePath = movil.getRutaImagen();
            if (imagePath != null && !imagePath.isEmpty()) {
                if (imageCache.containsKey(imagePath)) {
                    imageView.setImage(imageCache.get(imagePath));
                } else {
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

                    new Thread(loadImageTask).start();
                }
            } else {
                imageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/database/imagenes/no_available.png"))));
            }
        }
    }

    @FXML
    public void handleMouseClick() {
        // Aquí puedes manejar el evento de clic en la tarjeta del móvil
        System.out.println("Clic en el móvil: " + movil.getMarca() + " " + movil.getModelo());
        // Desplegar un boton flotante que me permita comprar el movil:
        // Haz la apertura del boton flotante
        // Puedes usar un Popup o un Dialog para mostrar opciones adicionales


        // Ejemplo de cómo podrías abrir un diálogo simple
        // Alert alert = new Alert(Alert.AlertType.INFORMATION);
        // alert.setTitle("Comprar Móvil");
        // alert.setHeaderText("¿Deseas comprar este móvil?");
        // alert.setContentText("Marca: " + movil.getMarca() + "\nModelo: " + movil.getModelo());
        // alert.showAndWait();
        // O puedes usar un Popup para mostrar opciones adicionales
         Popup popup = new Popup();
         popup.getContent().add(new Label("Opciones de compra"));
         popup.setAutoHide(true);
         popup.setHideOnEscape(true);
         popup.show(imageView.getParent().getParent().getScene().getWindow(), imageView.getLayoutX(), imageView.getLayoutY());
        // Aquí puedes agregar la lógica para mostrar un botón flotante
        // o cualquier otro componente que desees para la compra del móvil





    }


}
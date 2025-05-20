package model;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;
/**
 * Clase fachada que gestiona el catálogo de móviles.
 */
public class Catalogo {

    // Conjunto ordenado que almacena los móviles en stock
    private static final TreeSet<Movil> stock = new TreeSet<>();

    // Método para leer los datos desde el CSV e instanciar los móviles
    public static void instanciarBd() {
        stock.clear(); // Limpia el stock actual
        try {
            // Lee todas las líneas del archivo CSV
            List<String> lineas = Files.readAllLines(Paths.get("src/main/resources/database/Mobiles_Dataset_2025.csv"), StandardCharsets.UTF_8);

            for (String linea : lineas.subList(1, lineas.size())) { // Omite la cabecera
                String[] partes = linea.split(";");

                if (partes.length < 12) {
                    System.err.println("Error: línea con formato incorrecto: " + linea);
                    break;
                } else if (partes.length > 12) {
                    System.err.println("Error: línea con más de 12 campos: " + linea);
                    continue;
                }

                // Se extraen y limpian todos los campos
                String marca = partes[0].trim();
                String modelo = partes[1].trim();
                String peso = partes[2].trim();
                String RAM = partes[3].trim();
                String FrontCamera = partes[4].trim();
                String BackCamera = partes[5].trim();
                String procesador = partes[6].trim();
                String bateria = partes[7].trim();
                String pantalla = partes[8].trim();
                double precio = Double.parseDouble(partes[9].trim().replace("USD", "").trim().replace(',', '.'));
                String fechaLanzamiento = partes[10].trim();
                String Almacenamiento = partes[11].trim();

                // Agrupamos características
                String[] caracteristicasArray = {peso, RAM, FrontCamera, BackCamera, procesador, bateria, pantalla, fechaLanzamiento, Almacenamiento};
                ArrayList<String> caracteristicas = new ArrayList<>(Arrays.asList(caracteristicasArray));

                // Generamos una cantidad de stock aleatoria
                int stockCantidad = new Random().nextInt(10) + 1;

                // Creamos el móvil
                Movil movil = new Movil(marca, modelo, caracteristicas, precio, stockCantidad);

                // Se comprueba si existe una imagen asociada al móvil
                if(Catalogo.class.getResource("/database/imagenes/" + marca + "_" + modelo.replace(" ", "_") + ".png") != null) {
                    movil.setRutaImagen("/database/imagenes/" + marca + "_" + modelo.replace(" ", "_") + ".png");
                } else if(Catalogo.class.getResource("/database/imagenes/" + marca + "_" + modelo.replace(" ", "_") + ".jpg") != null) {
                    movil.setRutaImagen("/database/imagenes/" + marca + "_" + modelo.replace(" ", "_") + ".jpg");
                } else {
                    movil.setRutaImagen("/database/imagenes/no_available.png");
                }

                // Añade el móvil al stock
                stock.add(movil);
            }

        } catch (IOException e) {
            System.err.println("Error leyendo Mobiles_Dataset_2025.csv: " + e.getMessage());
        }
    }

    /**
     * Añade un nuevo móvil al catálogo y lo guarda en el CSV.
     */
    public static void agregarMovil(Movil m) {
        stock.add(m);
        guardarEnCsv(m); // Escribe en el archivo
    }

    /**
     * Guarda un móvil nuevo en el archivo CSV.
     */
    private static void guardarEnCsv(Movil m) {
        ArrayList<String> lineas = new ArrayList<>();
        String caracs = String.join(";", m.getCaracteristicas()); // Une las características
        String movil = String.format("%s;%s;%.2f;%d;%s", m.getMarca(), m.getModelo(), m.getPrecio(), m.getStock(), caracs);
        lineas.add(movil);
        try {
            // Añade la línea al final del archivo
            Files.write(Paths.get("src\\Company\\moviles.csv"), lineas, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("Error escribiendo Mobiles_Dataset_2025.csv: " + e.getMessage());
        }
    }

    /**
     * Intenta comprar un móvil. Si está disponible, reduce su stock y guarda el cambio.
     */
    public static boolean comprarTelefono(Movil m) {
        if(stock.contains(m) && m.getStock() > 0) {
            m.setStock(m.getStock() - 1);
            guardarEnCsv(m); // Guarda los cambios
            return true;
        } else {
            return false;
        }
    }

    // Devuelve todos los móviles ordenados por modelo (por defecto del TreeSet)
    public static ArrayList<Movil> ListaMovilesModelo() {
        return new ArrayList<>(stock);
    }

    // Devuelve todos los móviles ordenados por marca
    public static ArrayList<Movil> ListaMovilesMarca() {
        return stock.stream()
                .sorted(Comparator.comparing(Movil::getMarca))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    // Busca todos los móviles de una marca determinada
    public static ArrayList<Movil> buscarPorMarca(String marca) {
        return stock.stream()
                .filter(m -> m.getMarca().equalsIgnoreCase(marca))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    // Devuelve el móvil con modelo exacto, si existe
    public static Movil buscarPorModelo(String modelo) {
        return stock.stream()
                .filter(m -> m.getModelo().equalsIgnoreCase(modelo))
                .findFirst().orElse(null);
    }

    // Devuelve el móvil más barato
    public static Movil masBarato() {
        return stock.stream()
                .min(Comparator.comparingDouble(Movil::getPrecio))
                .orElse(null);
    }

    // Devuelve el móvil más caro
    public static Movil masCaro() {
        return stock.stream()
                .max(Comparator.comparingDouble(Movil::getPrecio))
                .orElse(null);
    }
}

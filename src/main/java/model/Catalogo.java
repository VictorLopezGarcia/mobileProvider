package model;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;
/**
 * Clase fachada
 */
public class Catalogo {

    
    private static final TreeSet<Movil> stock = new TreeSet<>();

    public static void instanciarBd() {
        stock.clear();
        try {
            List<String> lineas = Files.readAllLines(Paths.get("src/main/resources/database/Mobiles_Dataset_2025.csv"), StandardCharsets.UTF_8);
            for (String linea : lineas.subList(1, lineas.size())) { // Omitir cabecera
                String[] partes = linea.split(";");

                if (partes.length < 12) {
                    System.err.println("Error: línea con formato incorrecto: " + linea);
                    break;
                } else if (partes.length > 12) {
                    System.err.println("Error: línea con más de 12 campos: " + linea);
                    continue;
                }

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
                String[] caracteristicasArray = {peso, RAM, FrontCamera, BackCamera, procesador, bateria, pantalla, fechaLanzamiento, Almacenamiento};
                ArrayList<String> caracteristicas = new ArrayList<>(Arrays.asList(caracteristicasArray));
                int stockCantidad = new Random().nextInt(10) + 1; // Generar un stock aleatorio entre 1 y 10
                Movil movil = new Movil(marca, modelo, caracteristicas, precio, stockCantidad);
                if(Catalogo.class.getResource("/database/imagenes/" +marca+ "_" + modelo.replace(" ", "_") + ".png") != null) {
                    movil.setRutaImagen("/database/imagenes/" + marca+ "_" + modelo.replace(" ", "_")   + ".png");
                }else if(Catalogo.class.getResource("/database/imagenes/" + marca+ "_" + modelo.replace(" ", "_")  + ".jpg") != null) {
                    movil.setRutaImagen("/database/imagenes/" + marca+ "_" + modelo.replace(" ", "_")   + ".jpg");
                }
                else movil.setRutaImagen("/database/imagenes/no_available.png");
                stock.add(movil);
            }
        } catch (IOException e) {
            System.err.println("Error leyendo moviles.csv: " + e.getMessage());
        }
    }

    /** 
    *
    * 
    */
    public static void agregarMovil(Movil m) {
        stock.add(m);
        guardarEnCsv(m); // Escribe de nuevo en el archivo al agregar
    }
    
    /** 
    *
    * 
    */
    private static void guardarEnCsv(Movil m) {
        ArrayList<String> lineas = new ArrayList<>();
        String caracs = String.join(";", m.getCaracteristicas());   
        String movil = (String.format("%s;%s;%.2f;%d;%s", m.getMarca(), m.getModelo(), m.getPrecio(), m.getStock(), caracs));
        lineas.add(movil);
        
        try {
            // Abrir el archivo en modo append (añadir al final)
            Files.write(Paths.get("src\\Company\\moviles.csv"),  lineas , StandardCharsets.UTF_8, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("Error escribiendo moviles.csv: " + e.getMessage());
        }
    }
    
    /** 
    *
    * 
    */
    public static boolean comprarTelefono(Movil m) {
        if(stock.contains(m) && m.getStock() > 0) {
            m.setStock(m.getStock() - 1);
                guardarEnCsv(m); // Guardar cambios
                return true;
        }else return false;
    }

    /*
    public static ArrayList<Movil> ListaMovilesPvp() {
        return stock.stream()
                .sorted(Comparator.comparingDouble(Movil::getPrecio))
                .collect(Collectors.toCollection(ArrayList::new));
    }
     */
    
    /** 
    *
    * 
    */
    public static ArrayList<Movil> ListaMovilesModelo() {
        return new ArrayList<>(stock);
    }

    public static ArrayList<Movil> ListaMovilesMarca() {
        return stock.stream()
                .sorted(Comparator.comparing(Movil::getMarca))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static ArrayList<Movil> buscarPorMarca(String marca) {
        return stock.stream()
                .filter(m -> m.getMarca().equalsIgnoreCase(marca))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static Movil buscarPorModelo(String modelo) {
        return stock.stream()
                .filter(m -> m.getModelo().equalsIgnoreCase(modelo))
                .findFirst().orElse(null);
    }
}

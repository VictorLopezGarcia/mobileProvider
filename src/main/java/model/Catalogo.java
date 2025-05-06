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
    
    private static final int numCaracteristicas = 4;

    
    private static final TreeSet<Movil> stock = new TreeSet<>();

    public static void instanciarBd() {
        stock.clear();
        try {
            List<String> lineas = Files.readAllLines(Paths.get("src/main/resources/database/smartphones_transformado.csv"), StandardCharsets.UTF_8);
            for (String linea : lineas.subList(1, lineas.size())) { // Omitir cabecera
                String[] partes = linea.split(";");
                if ((partes.length != numCaracteristicas+4)) {
                   break;
                }

                String marca = partes[0].trim();
                String modelo = partes[1].trim();
                double precio = Double.parseDouble(partes[2].trim().replace(',', '.'));
                int stockCantidad = Integer.parseInt(partes[3].trim());
                ArrayList<String> caracteristicas = new ArrayList<>(Arrays.asList(partes).subList(4, partes.length));
                Movil movil = new Movil(marca, modelo, caracteristicas, precio, stockCantidad);
                if(Catalogo.class.getResource("/database/imagenes/" + modelo.toLowerCase().replace(" ", "") + ".png") != null) {
                    movil.setRutaImagen("/database/imagenes/" + modelo.toLowerCase().replace(" ", "")  + ".png");
                }else if(Catalogo.class.getResource("/database/imagenes/" + modelo.toLowerCase().replace(" ", "") + ".jpg") != null) {
                    movil.setRutaImagen("/database/imagenes/" + modelo.toLowerCase().replace(" ", "")  + ".jpg");
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

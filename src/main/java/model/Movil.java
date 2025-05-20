
package model;
import java.util.ArrayList;

/**
 * Representa un móvil con sus características principales.
 */
public class Movil implements Comparable<Movil> {

    // Atributos del móvil
    private String marca;
    private String modelo;
    private ArrayList<String> caracteristicas; // Lista de características como RAM, cámara, etc.
    private double precio; // Precio del móvil
    private int stock;     // Cantidad disponible
    private String rutaImagen; // Ruta de la imagen asociada al móvil

    /**
     * Constructor de la clase Movil
     * @param marca Marca del móvil
     * @param modelo Modelo del móvil
     * @param caracteristicas Lista de características (RAM, cámara, batería, etc.)
     * @param precio Precio del móvil
     * @param stock Cantidad disponible en stock
     */
    public Movil(String marca, String modelo, ArrayList<String> caracteristicas, double precio, int stock) {
        this.marca = marca;
        this.modelo = modelo;
        this.caracteristicas = caracteristicas;
        this.precio = precio;
        this.stock = stock;
    }

    // Métodos getters y setters para cada atributo

    public String getMarca() { return marca; }

    public void setMarca(String marca) { this.marca = marca; }

    public String getModelo() { return modelo; }

    public void setModelo(String modelo) { this.modelo = modelo; }

    public ArrayList<String> getCaracteristicas() { return caracteristicas; }

    public void setCaracteristicas(ArrayList<String> caracteristicas) { this.caracteristicas = caracteristicas; }

    public double getPrecio() { return precio; }

    public void setPrecio(double precio) { this.precio = precio; }

    public int getStock() { return stock; }

    public void setStock(int stock) { this.stock = stock; }

    /**
     * Define la igualdad entre móviles por su modelo (ignorando mayúsculas/minúsculas).
     */
    @Override
    public boolean equals(Object obj) {
        if(obj.getClass() != Movil.class) return false;
        else return this.modelo.equalsIgnoreCase(((Movil) obj).getModelo());
    }

    /**
     * Reutiliza el hashCode por defecto. Se podría mejorar para basarlo en el modelo.
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * Define el orden natural entre móviles (por modelo alfabéticamente).
     */
    @Override
    public int compareTo(Movil o) {
        return this.modelo.compareTo(o.getModelo());
    }

    /**
     * Representación en texto del móvil.
     */
    @Override
    public String toString() {
        return marca + " " + modelo + " - $" + precio + " (" + stock + " disponibles)";
    }

    /**
     * Getter y setter para la ruta de imagen asociada al móvil.
     */
    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }
}

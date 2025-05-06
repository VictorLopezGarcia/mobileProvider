
package mobileprovider;
import java.util.ArrayList;

/** Clase padre del proyecto
 * */
public class Movil implements Comparable<Movil> {
    private String marca;
    private String modelo;
    private ArrayList<String> caracteristicas;
    private double precio;
    private int stock;
    private String rutaImagen;

    /**
     * 
     */
    public Movil(String marca, String modelo, ArrayList<String> caracteristicas, double precio, int stock) {
        this.marca = marca;
        this.modelo = modelo;
        this.caracteristicas = caracteristicas;
        this.precio = precio;
        this.stock = stock;
    }

    // Getters y Setters
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

    
    @Override
    public boolean equals(Object obj) {
        if(obj.getClass()!= Movil.class) return false;
        else return this.modelo.equalsIgnoreCase(((Movil) obj).getModelo() );
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    
    @Override
    public int compareTo(Movil o) {
        return this.modelo.compareTo(o.getModelo());
    }

    @Override
    public String toString() {
        return marca + " " + modelo + " - $" + precio + " (" + stock + " disponibles)";
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

}

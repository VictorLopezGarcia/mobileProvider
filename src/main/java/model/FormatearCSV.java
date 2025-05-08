package model;

import java.io.*;
import java.nio.file.*;
import java.util.regex.*;

public class FormatearCSV {
    public static void main(String[] args) {
        String rutaArchivo = "src/main/resources/database/Mobiles_Dataset_2025.csv";
        String rutaArchivoSalida = "src/main/resources/database/Mobiles_Dataset_2025_CLEAN_FORMATTED.csv";

        try {
            Path path = Paths.get(rutaArchivo);
            Path pathSalida = Paths.get(rutaArchivoSalida);
            BufferedReader reader = Files.newBufferedReader(path);
            BufferedWriter writer = Files.newBufferedWriter(pathSalida);

            String linea;
            while ((linea = reader.readLine()) != null) {
                // Reemplazar comas por punto y coma, excepto en números con comas
                String lineaFormateada = linea.replaceAll(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", ";")
                        .replace("\"", "");
                writer.write(lineaFormateada);
                writer.newLine();
            }

            reader.close();
            writer.close();
            System.out.println("Archivo procesado correctamente. Nuevo archivo: " + rutaArchivoSalida);
        } catch (IOException e) {
            System.err.println("Error procesando el archivo: " + e.getMessage());
        }
    }
}



class CorregirSeparadores {
    public static void main(String[] args) {
        String rutaArchivo = "src/main/resources/database/Mobiles_Dataset_2025.csv";
        String rutaArchivoSalida = "src/main/resources/database/Mobiles_Dataset_2025_CLEAN_CORRECTED.csv";

        try {
            Path path = Paths.get(rutaArchivo);
            Path pathSalida = Paths.get(rutaArchivoSalida);
            BufferedReader reader = Files.newBufferedReader(path);
            BufferedWriter writer = Files.newBufferedWriter(pathSalida);

            String linea;
            while ((linea = reader.readLine()) != null) {
                // Dividir la línea en partes usando el delimitador ";"
                String[] partes = linea.split(";");

                // Corregir el separador en la posición de la batería (índice 7 y 8)
                if (partes.length > 8) {
                    partes[7] = partes[7] + "," + partes[8];

                    // Eliminar la parte duplicada
                    partes[8] = null;
                }

                // Reconstruir la línea con el delimitador ";"
                StringBuilder lineaCorregida = new StringBuilder();
                for (String parte : partes) {
                    if (parte != null) {
                        if (lineaCorregida.length() > 0) {
                            lineaCorregida.append(";");
                        }
                        lineaCorregida.append(parte);
                    }
                }

                writer.write(lineaCorregida.toString());
                writer.newLine();
            }

            reader.close();
            writer.close();
            System.out.println("Archivo corregido correctamente. Nuevo archivo: " + rutaArchivoSalida);
        } catch (IOException e) {
            System.err.println("Error procesando el archivo: " + e.getMessage());
        }
    }
}



class CorregirCSV {
    public static void main(String[] args) {
        String rutaArchivo = "src/main/resources/database/Mobiles_Dataset_2025.csv";
        String rutaArchivoSalida = "src/main/resources/database/Mobiles_Dataset_2025_CLEAN_CORRECTED.csv";

        try {
            Path path = Paths.get(rutaArchivo);
            Path pathSalida = Paths.get(rutaArchivoSalida);
            BufferedReader reader = Files.newBufferedReader(path);
            BufferedWriter writer = Files.newBufferedWriter(pathSalida);

            String linea;
            while ((linea = reader.readLine()) != null) {
                // Verifica si "mAh" no está seguido de un punto y coma
                if (linea.contains("mAh") && !linea.matches(".*mAh;.*")) {
                    linea = linea.replaceFirst("mAh", "mAh;");
                }
                writer.write(linea);
                writer.newLine();
            }

            reader.close();
            writer.close();
            System.out.println("Archivo corregido correctamente. Nuevo archivo: " + rutaArchivoSalida);
        } catch (IOException e) {
            System.err.println("Error procesando el archivo: " + e.getMessage());
        }
    }
}

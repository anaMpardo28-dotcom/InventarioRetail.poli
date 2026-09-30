import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * Genera los archivos de ventas, uno por cada vendedor que exista
 * en Vendedores.csv.
 *
 * @author G3 - Los Java
 * @version 2.0
 */
public class GenerateVentasFile {

    private static final String CARPETA_ENTRADA = "datos/entrada";
    private static final int CANTIDAD_PRODUCTOS_DISPONIBLES = 10;

    /**
     * Inicia la generación de las ventas de todos los vendedores.
     *
     * @param args argumentos no utilizados
     */
    public static void main(String[] args) {
        try {
            generarVentasDeTodos(4);
            System.out.println("Los archivos de ventas se generaron correctamente.");
        } catch (IOException excepcion) {
            System.err.println("Ocurrio un error: " + excepcion.getMessage());
        }
    }

    /**
     * Lee Vendedores.csv y crea un archivo de ventas para cada vendedor.
     *
     * @param cantidadMaximaVentas cantidad máxima de ventas por vendedor
     * @throws IOException si Vendedores.csv no existe o no se puede leer
     */
    public static void generarVentasDeTodos(int cantidadMaximaVentas)
            throws IOException {

        File archivoVendedores = new File(CARPETA_ENTRADA + "/Vendedores.csv");
        if (!archivoVendedores.exists()) {
            throw new IOException("Debe generar primero el archivo Vendedores.csv.");
        }

        Random aleatorio = new Random();

        try (BufferedReader lector = new BufferedReader(new FileReader(archivoVendedores))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                if (linea.isBlank()) {
                    continue;
                }
                String[] datos = linea.split(";");
                if (datos.length < 4) {
                    throw new IOException("Formato incorrecto en Vendedores.csv: " + linea);
                }
                long id = Long.parseLong(datos[1].trim());
                String nombre = datos[2].trim() + " " + datos[3].trim();
                int cantidadVentas = aleatorio.nextInt(cantidadMaximaVentas) + 1;

                createSalesMenFile(cantidadVentas, nombre, id);
            }
        }
    }

    /**
     * Crea el archivo Ventas_[id].csv de un vendedor con ventas
     * pseudoaleatorias. Cada línea es IdProducto;Cantidad.
     *
     * @param randomSalesCount cantidad de ventas a generar
     * @param name             nombre del vendedor
     * @param id               número de documento del vendedor
     * @throws IOException si ocurre un error al crear el archivo
     */
    public static void createSalesMenFile(int randomSalesCount, String name, long id)
            throws IOException {

        if (randomSalesCount <= 0) {
            System.out.println("La cantidad de ventas no es valida.");
            return;
        }

        Random aleatorio = new Random();
        File archivo = new File(CARPETA_ENTRADA + "/Ventas_" + id + ".csv");

        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(archivo))) {
            for (int i = 0; i < randomSalesCount; i++) {
                String idProducto = String.format(
                        "P%03d", aleatorio.nextInt(CANTIDAD_PRODUCTOS_DISPONIBLES) + 1);
                int cantidad = aleatorio.nextInt(10) + 1;

                escritor.write(idProducto + ";" + cantidad);
                escritor.newLine();
            }
        }
    }
}

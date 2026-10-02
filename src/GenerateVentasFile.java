import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Genera un archivo de ventas por cada vendedor registrado.
 *
 * @author G3 - Los Java
 * @version 1.1
 */
public class GenerateVentasFile {

    private static final String CARPETA_ENTRADA = "datos/entrada";

    /**
     * Lee los vendedores y genera un archivo de ventas para cada uno.
     *
     * @param cantidadMaximaVentas máximo de ventas por vendedor
     * @throws IOException si ocurre un error al leer o escribir archivos
     */
    public static void createSalesMenFile(
            int cantidadMaximaVentas
    ) throws IOException {

        if (cantidadMaximaVentas <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad máxima de ventas debe ser mayor que cero."
            );
        }

        File archivoVendedores =
                new File(CARPETA_ENTRADA, "Vendedores.csv");

        try (BufferedReader lector =
                     new BufferedReader(new FileReader(archivoVendedores))) {

            String linea;

            while ((linea = lector.readLine()) != null) {

                if (linea.isBlank()) {
                    continue;
                }

                String[] datos = linea.split(";");

                if (datos.length != 4) {
                    throw new IOException(
                            "Registro de vendedor inválido: " + linea
                    );
                }

                long documento = Long.parseLong(datos[1]);
                String nombre = datos[2] + " " + datos[3];

                int cantidadVentas =
                        new Random().nextInt(cantidadMaximaVentas) + 1;

                createSalesMenFile(
                        cantidadVentas,
                        nombre,
                        documento
                );
            }
        }
    }

    /**
     * Genera las ventas de un vendedor.
     *
     * La primera línea contiene TipoDocumento;NumeroDocumento.
     * Las siguientes contienen IdProducto;CantidadVendida.
     *
     * @param randomSalesCount cantidad de ventas aleatorias que se generarán
     * @param name nombre completo del vendedor
     * @param id número de documento del vendedor
     * @throws IOException si ocurre un error al leer o escribir archivos
     */
    public static void createSalesMenFile(
            int randomSalesCount,
            String name,
            long id
    ) throws IOException {

        if (randomSalesCount <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad de ventas debe ser mayor que cero."
            );
        }

        String tipoDocumento = buscarTipoDocumento(name, id);
        List<String> productos = leerProductos();

        File archivoVentas =
                new File(CARPETA_ENTRADA, "ventas_" + id + ".csv");

        Random aleatorio = new Random();

        try (BufferedWriter escritor =
                     new BufferedWriter(new FileWriter(archivoVentas))) {

            escritor.write(tipoDocumento + ";" + id);
            escritor.newLine();

            for (int posicion = 0;
                 posicion < randomSalesCount;
                 posicion++) {

                String idProducto =
                        productos.get(aleatorio.nextInt(productos.size()));

                int cantidadVendida = aleatorio.nextInt(10) + 1;

                escritor.write(idProducto + ";" + cantidadVendida);
                escritor.newLine();
            }
        }
    }

    /**
     * Busca el tipo de documento del vendedor en Vendedores.csv.
     */
    private static String buscarTipoDocumento(
            String nombre,
            long documento
    ) throws IOException {

        File archivoVendedores =
                new File(CARPETA_ENTRADA, "Vendedores.csv");

        try (BufferedReader lector =
                     new BufferedReader(new FileReader(archivoVendedores))) {

            String linea;

            while ((linea = lector.readLine()) != null) {

                if (linea.isBlank()) {
                    continue;
                }

                String[] datos = linea.split(";");

                if (datos.length != 4) {
                    throw new IOException(
                            "Registro de vendedor inválido: " + linea
                    );
                }

                long documentoRegistrado = Long.parseLong(datos[1]);
                String nombreRegistrado = datos[2] + " " + datos[3];

                if (documentoRegistrado == documento
                        && nombreRegistrado.equals(nombre)) {

                    return datos[0];
                }
            }
        }

        throw new IOException(
                "No se encontró al vendedor: " + nombre + " (" + documento + ")."
        );
    }

    /**
     * Lee los identificadores de los productos disponibles.
     */
    private static List<String> leerProductos() throws IOException {

        List<String> productos = new ArrayList<>();

        File archivoProductos =
                new File(CARPETA_ENTRADA, "productos.csv");

        try (BufferedReader lector =
                     new BufferedReader(new FileReader(archivoProductos))) {

            String linea;

            while ((linea = lector.readLine()) != null) {

                if (linea.isBlank()) {
                    continue;
                }

                String[] datos = linea.split(";");

                if (datos.length != 3) {
                    throw new IOException(
                            "Registro de producto inválido: " + linea
                    );
                }

                productos.add(datos[0]);
            }
        }

        if (productos.isEmpty()) {
            throw new IOException(
                    "El archivo productos.csv no contiene productos."
            );
        }

        return productos;
    }
}
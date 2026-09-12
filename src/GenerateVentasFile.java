import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * Debe generar el archivo Ventas.csv para el proyecto, reutilizando
 * los vendedores que ya existen en Vendedores.csv.
 *
 * @author G3 - Los Java
 * @version 1.0
 */
public class GenerateVentasFile {

    private static final String CARPETA_ENTRADA =
            "datos/entrada";

    private static final int CANTIDAD_PRODUCTOS_DISPONIBLES = 10;

    /**
     * Iniciar la ejecución del programa.
     *
     * @param args argumentos no utilizados
     */
    public static void main(String[] args) {
        try {
            createSalesMenFile(4);

            System.out.println(
                    "El archivo de ventas se genero correctamente."
            );

        } catch (IOException excepcion) {
            System.err.println(
                    "Ocurrio un error: "
                            + excepcion.getMessage()
            );
        }
    }

    /**
     * Debe crear el archivo Ventas.csv con una línea por cada
     * vendedor que exista en Vendedores.csv. Cada línea inicia con
     * el tipo y número de documento del vendedor (leídos desde ese
     * archivo) y luego una cantidad variable de pares
     * IdProducto;CantidadVendida. Formato de cada línea:
     * TipoDocumento;NumeroDocumento;IdProducto;Cantidad;IdProducto;Cantidad;...
     *
     * @param cantidadMaximaProductosPorVenta cantidad máxima de pares
     *                                        producto/cantidad que
     *                                        puede tener cada vendedor
     * @throws IOException si ocurre un error al leer Vendedores.csv,
     *                      si ese archivo no existe todavia, o si
     *                      ocurre un error al crear Ventas.csv
     */
    public static void createSalesMenFile(
            int cantidadMaximaProductosPorVenta
    ) throws IOException {

        if (cantidadMaximaProductosPorVenta <= 0) {

            System.out.println(
                    "La cantidad de productos por venta no es valida."
            );

            return;
        }

        File archivoVendedores =
                new File(
                        CARPETA_ENTRADA + "/Vendedores.csv"
                );

        if (!archivoVendedores.exists()) {

            System.out.println(
                    "Debe generar primero el archivo Vendedores.csv."
            );

            return;
        }

        BufferedReader lector =
                new BufferedReader(
                        new FileReader(archivoVendedores)
                );

        File archivoVentas =
                new File(
                        CARPETA_ENTRADA + "/Ventas.csv"
                );

        BufferedWriter escritor =
                new BufferedWriter(
                        new FileWriter(archivoVentas)
                );

        Random aleatorio =
                new Random();

        String lineaVendedor;

        while ((lineaVendedor = lector.readLine()) != null) {

            if (lineaVendedor.isBlank()) {
                continue;
            }

            String[] datosVendedor =
                    lineaVendedor.split(";");

            String tipoDocumento = datosVendedor[0];
            String numeroDocumento = datosVendedor[1];

            StringBuilder lineaVenta =
                    new StringBuilder();

            lineaVenta.append(tipoDocumento);
            lineaVenta.append(";");
            lineaVenta.append(numeroDocumento);

            int cantidadProductosVendidos =
                    aleatorio.nextInt(
                            cantidadMaximaProductosPorVenta
                    ) + 1;

            for (int posicionVenta = 0;
                 posicionVenta < cantidadProductosVendidos;
                 posicionVenta++) {

                String idProducto =
                        String.format(
                                "P%03d",
                                aleatorio.nextInt(
                                        CANTIDAD_PRODUCTOS_DISPONIBLES
                                ) + 1
                        );

                int cantidadVendida =
                        aleatorio.nextInt(10) + 1;

                lineaVenta.append(";");
                lineaVenta.append(idProducto);
                lineaVenta.append(";");
                lineaVenta.append(cantidadVendida);
            }

            escritor.write(lineaVenta.toString());
            escritor.newLine();
        }

        lector.close();
        escritor.close();
    }
}

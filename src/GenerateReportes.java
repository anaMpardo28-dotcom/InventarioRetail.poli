import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Lee los archivos de productos, vendedores y ventas, y genera los
 * reportes de vendedores (dinero recaudado) y de productos
 * (cantidad vendida), ordenados de mayor a menor.
 *
 * @author G3 - Los Java
 * @version 1.0
 */
public class GenerateReportes {

    private static final String CARPETA_ENTRADA = "datos/entrada";
    private static final String CARPETA_SALIDA = "datos/salida";

    /**
     * Inicia la generación de los reportes.
     *
     */
    public static void generarReportes() {
        try {
            Map<String, String> nombresProductos = new LinkedHashMap<>();
            Map<String, Long> preciosProductos = new LinkedHashMap<>();
            leerProductos(nombresProductos, preciosProductos);

            Map<String, String> vendedores = new LinkedHashMap<>();
            leerVendedores(vendedores);

            Map<String, Long> dineroPorVendedor = new LinkedHashMap<>();
            Map<String, Long> cantidadPorProducto = new LinkedHashMap<>();
            for (String idProducto : nombresProductos.keySet()) {
                cantidadPorProducto.put(idProducto, 0L);
            }

            for (String documento : vendedores.keySet()) {
                long total = procesarVentas(documento, preciosProductos, cantidadPorProducto);
                dineroPorVendedor.put(documento, total);
            }

            crearReporteVendedores(vendedores, dineroPorVendedor);
            crearReporteProductos(nombresProductos, preciosProductos, cantidadPorProducto);

            System.out.println("Los reportes se generaron correctamente.");

        } catch (IOException excepcion) {
            System.err.println("Ocurrio un error: " + excepcion.getMessage());
        }
    }

    /**
     * Lee productos.csv (IdProducto;Nombre;Precio).
     *
     * @param nombres mapa donde se guardan los nombres por id
     * @param precios mapa donde se guardan los precios por id
     * @throws IOException si el archivo no existe o tiene formato incorrecto
     */
    private static void leerProductos(Map<String, String> nombres,
                                      Map<String, Long> precios) throws IOException {
        File archivo = new File(CARPETA_ENTRADA + "/productos.csv");
        if (!archivo.exists()) {
            throw new IOException("No existe el archivo productos.csv.");
        }

        try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
            String linea;
            int numeroLinea = 0;
            while ((linea = lector.readLine()) != null) {
                numeroLinea++;
                if (linea.isBlank()) {
                    continue;
                }
                String[] datos = linea.split(";");
                if (datos.length != 3) {
                    throw new IOException("Formato incorrecto en productos.csv, linea "
                            + numeroLinea);
                }
                long precio;
                try {
                    precio = Long.parseLong(datos[2].trim());
                } catch (NumberFormatException e) {
                    throw new IOException("Precio invalido en productos.csv, linea "
                            + numeroLinea);
                }
                if (precio < 0) {
                    throw new IOException("Precio negativo en productos.csv, linea "
                            + numeroLinea);
                }
                nombres.put(datos[0].trim(), datos[1].trim());
                precios.put(datos[0].trim(), precio);
            }
        }
    }

    /**
     * Lee Vendedores.csv (Tipo;Documento;Nombres;Apellidos).
     *
     * @param vendedores mapa documento -> nombre completo
     * @throws IOException si el archivo no existe o tiene formato incorrecto
     */
    private static void leerVendedores(Map<String, String> vendedores)
            throws IOException {
        File archivo = new File(CARPETA_ENTRADA + "/Vendedores.csv");
        if (!archivo.exists()) {
            throw new IOException("No existe el archivo Vendedores.csv.");
        }

        try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
            String linea;
            int numeroLinea = 0;
            while ((linea = lector.readLine()) != null) {
                numeroLinea++;
                if (linea.isBlank()) {
                    continue;
                }
                String[] datos = linea.split(";");
                if (datos.length != 4) {
                    throw new IOException("Formato incorrecto en Vendedores.csv, linea "
                            + numeroLinea);
                }
                vendedores.put(datos[1].trim(), datos[2].trim() + " " + datos[3].trim());
            }
        }
    }
    /**
     * Lee la identificación del vendedor y sus ventas.
     * Acumula las unidades por producto y calcula el recaudo.
     *
     * @param documento documento del vendedor
     * @param precios precios por producto
     * @param cantidades unidades acumuladas por producto
     * @return dinero recaudado por el vendedor
     * @throws IOException si el archivo falta o contiene datos inválidos
     */
    private static long procesarVentas(
            String documento,
            Map<String, Long> precios,
            Map<String, Long> cantidades
    ) throws IOException {

        String nombreArchivo = "ventas_" + documento + ".csv";
        File archivo = new File(CARPETA_ENTRADA, nombreArchivo);
        long total = 0;

        try (BufferedReader lector =
                     new BufferedReader(new FileReader(archivo))) {

            String encabezado = lector.readLine();

            if (encabezado == null) {
                throw new IOException("Archivo vacío: " + nombreArchivo);
            }

            String[] identificacion = encabezado.split(";", -1);

            if (identificacion.length != 2
                    || identificacion[0].trim().isEmpty()
                    || !identificacion[1].trim().equals(documento)) {

                throw new IOException(
                        "Identificación del vendedor incorrecta en "
                                + nombreArchivo
                );
            }

            String linea;
            int numeroLinea = 1;

            while ((linea = lector.readLine()) != null) {
                numeroLinea++;

                if (linea.isBlank()) {
                    continue;
                }

                String[] datos = linea.split(";", -1);

                if (datos.length != 2) {
                    throw new IOException(
                            "Formato incorrecto en " + nombreArchivo
                                    + ", línea " + numeroLinea
                    );
                }

                String idProducto = datos[0].trim();
                long cantidad;

                try {
                    cantidad = Long.parseLong(datos[1].trim());
                } catch (NumberFormatException excepcion) {
                    throw new IOException(
                            "Cantidad inválida en " + nombreArchivo
                                    + ", línea " + numeroLinea
                    );
                }

                if (!precios.containsKey(idProducto)) {
                    throw new IOException(
                            "Producto inexistente: " + idProducto
                                    + " en " + nombreArchivo
                                    + ", línea " + numeroLinea
                    );
                }

                if (cantidad <= 0) {
                    throw new IOException(
                            "La cantidad debe ser positiva en "
                                    + nombreArchivo
                                    + ", línea " + numeroLinea
                    );
                }

                total += precios.get(idProducto) * cantidad;

                cantidades.put(
                        idProducto,
                        cantidades.get(idProducto) + cantidad
                );
            }
        }

        return total;
    }
    /**
     * Crea ReporteVendedores.csv ordenado de mayor a menor dinero recaudado.
     *
     * @param vendedores mapa documento -> nombre completo
     * @param dinero dinero recaudado por documento
     * @throws IOException si ocurre un error al crear el archivo
     */
    private static void crearReporteVendedores(Map<String, String> vendedores,
                                               Map<String, Long> dinero)
            throws IOException {
        List<Map.Entry<String, Long>> lista = new ArrayList<>(dinero.entrySet());
        lista.sort((a, b) -> Long.compare(b.getValue(), a.getValue()));

        try (BufferedWriter escritor = abrirSalida("ReporteVendedores.csv")) {
            for (Map.Entry<String, Long> entrada : lista) {
                escritor.write(vendedores.get(entrada.getKey()) + ";" + entrada.getValue());
                escritor.newLine();
            }
        }
    }

    /**
     * Crea ReporteProductos.csv ordenado de mayor a menor cantidad vendida.
     *
     * @param nombres nombres por id de producto
     * @param precios precios por id de producto
     * @param cantidades cantidad total vendida por producto
     * @throws IOException si ocurre un error al crear el archivo
     */
    private static void crearReporteProductos(Map<String, String> nombres,
                                              Map<String, Long> precios,
                                              Map<String, Long> cantidades)
            throws IOException {
        List<Map.Entry<String, Long>> lista = new ArrayList<>(cantidades.entrySet());
        lista.sort((a, b) -> Long.compare(b.getValue(), a.getValue()));

        try (BufferedWriter escritor = abrirSalida("ReporteProductos.csv")) {
            for (Map.Entry<String, Long> entrada : lista) {
                String id = entrada.getKey();
                escritor.write(nombres.get(id) + ";" + precios.get(id) + ";"
                        + entrada.getValue());
                escritor.newLine();
            }
        }
    }

    /**
     * Crea la carpeta de salida si hace falta y abre el archivo indicado.
     *
     * @param nombre nombre del archivo a crear
     * @return escritor listo para usar
     * @throws IOException si no se puede crear la carpeta o el archivo
     */
    private static BufferedWriter abrirSalida(String nombre) throws IOException {
        File carpeta = new File(CARPETA_SALIDA);
        if (!carpeta.exists() && !carpeta.mkdirs()) {
            throw new IOException("No fue posible crear la carpeta de salida.");
        }
        return new BufferedWriter(new FileWriter(new File(carpeta, nombre)));
    }
}

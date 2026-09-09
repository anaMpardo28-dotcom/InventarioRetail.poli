import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * Debe generar el archivo de productos para el proyecto.
 *
 * @author G3 - Los Java
 * @version 1.0
 */
public class GenerateInfoFiles {

    private static final String CARPETA_ENTRADA =
            "datos/entrada";

    private static final String[] NOMBRES_PRODUCTOS = {
            "Camiseta estampada",
            "Pantalón de traje",
            "Jean relax fit",
            "Chaqueta denim",
            "Hoodie",
            "Trench coat marrón",
            "Camiseta estilo polo",
            "Camisa oxford",
            "Cárdigan tejido",
            "Short deportivo",
            "Blazer cruzado",
            "Camiseta en algodón"
    };

    /**
     * Iniciar la ejecución del programa.
     *
     * @param args argumentos no utilizados
     */
    public static void main(String[] args) {
        try {
            createProductsFile(10);

            System.out.println(
                    "El archivo de productos se genero correctamente."
            );

        } catch (IOException excepcion) {
            System.err.println(
                    "Ocurrio un error: "
                            + excepcion.getMessage()
            );
        }
    }

    /**
     * Debe crear un archivo con productos pseudoaleatorios.
     *
     * @param cantidadProductos cantidad de productos que se generarán
     * @throws IOException si ocurre un error al crear el archivo
     */
    public static void createProductsFile(
            int cantidadProductos
    ) throws IOException {

        if (cantidadProductos <= 0
                || cantidadProductos > NOMBRES_PRODUCTOS.length) {

            System.out.println(
                    "La cantidad de productos no es valida."
            );

            return;
        }

        File carpetaEntrada =
                new File(CARPETA_ENTRADA);

        if (!carpetaEntrada.exists()) {
            boolean carpetaCreada =
                    carpetaEntrada.mkdirs();

            if (!carpetaCreada) {
                throw new IOException(
                        "No fue posible crear la carpeta de entrada."
                );
            }
        }

        File archivoProductos =
                new File(
                        CARPETA_ENTRADA + "/productos.csv"
                );

        BufferedWriter escritor =
                new BufferedWriter(
                        new FileWriter(archivoProductos)
                );

        Random aleatorio =
                new Random();

        for (int posicion = 0;
             posicion < cantidadProductos;
             posicion++) {

            String idProducto =
                    String.format(
                            "P%03d",
                            posicion + 1
                    );

            long precioUnitario =
                    (aleatorio.nextInt(36) + 5) * 5000L;

            escritor.write(
                    idProducto
                            + ";"
                            + NOMBRES_PRODUCTOS[posicion]
                            + ";"
                            + precioUnitario
            );

            escritor.newLine();
        }

        escritor.close();
    }
}
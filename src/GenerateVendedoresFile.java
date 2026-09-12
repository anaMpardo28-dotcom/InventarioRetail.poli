import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * Debe generar el archivo Vendedores.csv para el proyecto.
 *
 * @author G3 - Los Java
 * @version 1.0
 */
public class GenerateVendedoresFile {

    private static final String CARPETA_ENTRADA =
            "datos/entrada";

    private static final String[] TIPOS_DOCUMENTO = {
            "CC",
            "CE",
            "TI"
    };

    private static final String[] NOMBRES_VENDEDORES = {
            "Camila",
            "Andrés",
            "Valentina",
            "Santiago",
            "Mariana",
            "Juan",
            "Laura",
            "David",
            "Sofía",
            "Carlos"
    };

    private static final String[] APELLIDOS_VENDEDORES = {
            "Gómez",
            "Rodríguez",
            "Martínez",
            "Pardo",
            "Suárez",
            "Torres",
            "Ramírez",
            "Vargas",
            "Castro",
            "Molina"
    };

    /**
     * Iniciar la ejecución del programa.
     *
     * @param args argumentos no utilizados
     */
    public static void main(String[] args) {
        try {
            createSalesManInfoFile(5);

            System.out.println(
                    "El archivo de vendedores se genero correctamente."
            );

        } catch (IOException excepcion) {
            System.err.println(
                    "Ocurrio un error: "
                            + excepcion.getMessage()
            );
        }
    }

    /**
     * Debe crear el archivo Vendedores.csv con la información de los
     * vendedores, sin línea de encabezado. Cada línea tiene el
     * formato:
     * TipoDocumento;NumeroDocumento;Nombres;Apellidos
     *
     * @param cantidadVendedores cantidad de vendedores que se generarán
     * @throws IOException si ocurre un error al crear el archivo
     */
    public static void createSalesManInfoFile(
            int cantidadVendedores
    ) throws IOException {

        if (cantidadVendedores <= 0
                || cantidadVendedores > NOMBRES_VENDEDORES.length) {

            System.out.println(
                    "La cantidad de vendedores no es valida."
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

        File archivoVendedores =
                new File(
                        CARPETA_ENTRADA + "/Vendedores.csv"
                );

        BufferedWriter escritor =
                new BufferedWriter(
                        new FileWriter(archivoVendedores)
                );

        Random aleatorio =
                new Random();

        for (int posicion = 0;
             posicion < cantidadVendedores;
             posicion++) {

            String tipoDocumento =
                    TIPOS_DOCUMENTO[
                            aleatorio.nextInt(
                                    TIPOS_DOCUMENTO.length
                            )
                            ];

            long numeroDocumento =
                    10_000_000L
                            + aleatorio.nextInt(90_000_000);

            escritor.write(
                    tipoDocumento
                            + ";"
                            + numeroDocumento
                            + ";"
                            + NOMBRES_VENDEDORES[posicion]
                            + ";"
                            + APELLIDOS_VENDEDORES[posicion]
            );

            escritor.newLine();
        }

        escritor.close();
    }
}

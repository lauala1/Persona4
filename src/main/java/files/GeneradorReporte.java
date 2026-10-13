package files;

import services.ClasificadorAP;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;

/** Genera el reporte final de dispositivos por punto de acceso. */
public class GeneradorReporte {

    private final ClasificadorAP clasificador;

    public GeneradorReporte() {
        this(new ClasificadorAP());
    }

    public GeneradorReporte(ClasificadorAP clasificador) {
        this.clasificador = Objects.requireNonNull(clasificador,
                "El clasificador no puede ser null");
    }

    /**
     * Escribe líneas AP;ubicación;cantidad ordenadas de mayor a menor.
     */
    public void generar(
            Path archivo,
            Map<String, Integer> dispositivosPorAP,
            Map<String, String> ubicacionesPorAP) throws IOException {

        Objects.requireNonNull(archivo,
                "La ruta del reporte no puede ser null");
        Objects.requireNonNull(ubicacionesPorAP,
                "Las ubicaciones de los AP no pueden ser null");

        Map<String, Integer> ordenados =
                clasificador.ordenarDescendente(dispositivosPorAP);
        validarUbicaciones(ordenados, ubicacionesPorAP);

        Path carpetaPadre = archivo.toAbsolutePath().getParent();
        if (carpetaPadre != null) {
            Files.createDirectories(carpetaPadre);
        }

        try (BufferedWriter writer = Files.newBufferedWriter(
                archivo, StandardCharsets.UTF_8)) {
            for (Map.Entry<String, Integer> resultado : ordenados.entrySet()) {
                String identificador = resultado.getKey();
                writer.write(identificador + ";"
                        + ubicacionesPorAP.get(identificador) + ";"
                        + resultado.getValue());
                writer.newLine();
            }
        }
    }

    private void validarUbicaciones(
            Map<String, Integer> resultados,
            Map<String, String> ubicacionesPorAP) {
        for (String identificador : resultados.keySet()) {
            String ubicacion = ubicacionesPorAP.get(identificador);
            if (ubicacion == null || ubicacion.isBlank()) {
                throw new IllegalArgumentException(
                        "No se encontró la ubicación del AP " + identificador);
            }
            if (contieneSeparadorOLineaNueva(identificador)
                    || contieneSeparadorOLineaNueva(ubicacion)) {
                throw new IllegalArgumentException(
                        "El AP y su ubicación no pueden contener ';' ni saltos de línea");
            }
        }
    }

    private boolean contieneSeparadorOLineaNueva(String texto) {
        return texto.contains(";") || texto.contains("\n")
                || texto.contains("\r");
    }
}

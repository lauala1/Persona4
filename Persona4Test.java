import files.GeneradorReporte;
import services.ClasificadorAP;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Persona4Test {

    public static void main(String[] args) throws Exception {
        probarOrdenamiento();
        probarReporte();
        probarUbicacionFaltante();
        System.out.println("Pruebas de Persona 4 superadas");
    }

    private static void probarOrdenamiento() {
        Map<String, Integer> ordenados = new ClasificadorAP()
                .ordenarDescendente(Map.of(
                        "AP02", 89,
                        "AP03", 120,
                        "AP01", 145));

        assert new ArrayList<>(ordenados.keySet()).equals(
                List.of("AP01", "AP03", "AP02"));
    }

    private static void probarReporte() throws Exception {
        Path archivo = Files.createTempFile("saturation-", ".csv");
        new GeneradorReporte().generar(
                archivo,
                Map.of("AP02", 89, "AP03", 120, "AP01", 145),
                Map.of(
                        "AP01", "Biblioteca",
                        "AP02", "Cafetería",
                        "AP03", "Laboratorio"));

        assert Files.readAllLines(archivo).equals(List.of(
                "AP01;Biblioteca;145",
                "AP03;Laboratorio;120",
                "AP02;Cafetería;89"));
        Files.deleteIfExists(archivo);
    }

    private static void probarUbicacionFaltante() throws Exception {
        Path archivo = Files.createTempFile("saturation-error-", ".csv");
        try {
            new GeneradorReporte().generar(
                    archivo, Map.of("AP01", 10), Map.of());
            throw new AssertionError("Se esperaba una ubicación faltante");
        } catch (IllegalArgumentException esperado) {
            assert esperado.getMessage().contains("AP01");
        } finally {
            Files.deleteIfExists(archivo);
        }
    }
}

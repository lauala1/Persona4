package services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/** Organiza los resultados del análisis de la red por cantidad de dispositivos. */
public class ClasificadorAP {

    /**
     * Ordena los AP de mayor a menor. En caso de empate usa el identificador.
     * El mapa recibido no se modifica.
     */
    public Map<String, Integer> ordenarDescendente(
            Map<String, Integer> dispositivosPorAP) {

        Objects.requireNonNull(dispositivosPorAP,
                "Los resultados del análisis no pueden ser null");

        List<Map.Entry<String, Integer>> resultados =
                new ArrayList<>(dispositivosPorAP.entrySet());
        for (Map.Entry<String, Integer> resultado : resultados) {
            validarResultado(resultado.getKey(), resultado.getValue());
        }

        resultados.sort(Map.Entry.<String, Integer>comparingByValue(
                Comparator.reverseOrder())
                .thenComparing(Map.Entry.comparingByKey()));

        Map<String, Integer> ordenados = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> resultado : resultados) {
            ordenados.put(resultado.getKey(), resultado.getValue());
        }
        return ordenados;
    }

    private void validarResultado(String identificador, Integer cantidad) {
        if (identificador == null || identificador.isBlank()) {
            throw new IllegalArgumentException(
                    "El identificador del AP no puede estar vacío");
        }
        if (cantidad == null || cantidad < 0) {
            throw new IllegalArgumentException(
                    "La cantidad de dispositivos del AP " + identificador
                            + " debe ser mayor o igual que cero");
        }
    }
}

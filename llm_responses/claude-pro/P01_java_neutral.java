import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordFrequency {

    /**
     * Retorna un mapa que asocia cada palabra única con su número de apariciones.
     * Si la lista está vacía, retorna un mapa vacío.
     */
    public static Map<String, Integer> wordFrequency(List<String> words) {
        Map<String, Integer> frequencies = new HashMap<>();
        for (String word : words) {
            frequencies.merge(word, 1, Integer::sum);
        }
        return frequencies;
    }

    // Ejecutar con assertions habilitadas: java -ea WordFrequency.java
    public static void main(String[] args) {
        boolean assertionsEnabled = false;
        assert assertionsEnabled = true; // solo se asigna si -ea está activo
        if (!assertionsEnabled) {
            System.err.println("Advertencia: ejecuta con 'java -ea' para que los asserts se evalúen.");
        }

        // Caso 1: ejemplo del enunciado
        assert wordFrequency(List.of("the", "fox", "the")).equals(Map.of("the", 2, "fox", 1));

        // Caso 2: lista vacía
        assert wordFrequency(List.of()).isEmpty();

        // Caso 3: distingue mayúsculas/minúsculas y cuenta repeticiones múltiples
        assert wordFrequency(List.of("a", "A", "a", "b", "a")).equals(Map.of("a", 3, "A", 1, "b", 1));

        System.out.println("Todas las pruebas pasaron.");
    }
}
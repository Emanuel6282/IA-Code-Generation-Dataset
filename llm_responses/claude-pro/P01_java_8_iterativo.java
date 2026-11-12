import java.util.ArrayList;
import java.util.Arrays;
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
            // Map.getOrDefault (nuevo en Java 8): evita el if/containsKey y el null.
            frequencies.put(word, frequencies.getOrDefault(word, 0) + 1);
        }
        return frequencies;
    }

    // Compilar y ejecutar con assertions habilitadas:
    //   javac WordFrequency.java && java -ea WordFrequency
    public static void main(String[] args) {
        boolean assertionsEnabled = false;
        assert assertionsEnabled = true; // solo se asigna si -ea está activo
        if (!assertionsEnabled) {
            System.err.println("Advertencia: ejecuta con 'java -ea' para que los asserts se evalúen.");
        }

        // Caso 1: ejemplo del enunciado
        Map<String, Integer> expected1 = new HashMap<>();
        expected1.put("the", 2);
        expected1.put("fox", 1);
        assert wordFrequency(Arrays.asList("the", "fox", "the")).equals(expected1);

        // Caso 2: lista vacía
        assert wordFrequency(new ArrayList<String>()).isEmpty();

        // Caso 3: distingue mayúsculas/minúsculas y cuenta repeticiones múltiples
        Map<String, Integer> expected3 = new HashMap<>();
        expected3.put("a", 3);
        expected3.put("A", 1);
        expected3.put("b", 1);
        assert wordFrequency(Arrays.asList("a", "A", "a", "b", "a")).equals(expected3);

        System.out.println("Todas las pruebas pasaron.");
    }
}
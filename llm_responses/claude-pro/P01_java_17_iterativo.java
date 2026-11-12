import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordFrequency {

    /**
     * Retorna un mapa que asocia cada palabra única con su número de apariciones.
     * Si la lista está vacía, retorna un mapa vacío.
     */
    public static Map<String, Integer> wordFrequency(List<String> words) {
        var frequencies = new HashMap<String, Integer>();   // var: inferencia de tipo local (Java 10)
        for (var word : words) {
            frequencies.put(word, frequencies.getOrDefault(word, 0) + 1);
        }
        return frequencies;
    }

    /** Caso de prueba como record (Java 16, estándar en Java 17 LTS): inmutable y sin boilerplate. */
    record TestCase(String name, List<String> words, Map<String, Integer> expected) {}

    // Ejecutar con assertions habilitadas: java -ea WordFrequency.java
    public static void main(String[] args) {
        var assertionsEnabled = false;
        assert assertionsEnabled = true; // solo se asigna si -ea está activo
        if (!assertionsEnabled) {
            System.err.println("Advertencia: ejecuta con 'java -ea' para que los asserts se evalúen.");
        }

        var testCases = new ArrayList<TestCase>();
        // Caso 1: ejemplo del enunciado
        testCases.add(new TestCase("ejemplo del enunciado",
                List.of("the", "fox", "the"), Map.of("the", 2, "fox", 1)));
        // Caso 2: lista vacía
        testCases.add(new TestCase("lista vacía", List.of(), Map.of()));
        // Caso 3: distingue mayúsculas/minúsculas y cuenta repeticiones múltiples
        testCases.add(new TestCase("mayúsculas y repeticiones",
                List.of("a", "A", "a", "b", "a"), Map.of("a", 3, "A", 1, "b", 1)));

        for (var testCase : testCases) {
            var result = wordFrequency(testCase.words());
            assert result.equals(testCase.expected())
                    : "Caso '%s': entrada %s, esperado %s, obtenido %s".formatted(
                            testCase.name(), testCase.words(), testCase.expected(), result);
        }

        System.out.println("Todas las pruebas pasaron.");
    }
}
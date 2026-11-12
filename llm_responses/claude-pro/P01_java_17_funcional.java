import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WordFrequency {

    /**
     * Retorna un mapa inmutable que asocia cada palabra única con su número de apariciones.
     * Si la lista está vacía (o es null), retorna un mapa vacío.
     */
    public static Map<String, Integer> wordFrequency(List<String> words) {
        return Optional.ofNullable(words)
                .stream()                                      // Optional.stream() (Java 9): null -> stream vacío
                .flatMap(List::stream)
                .collect(Collectors.collectingAndThen(
                        Collectors.groupingBy(
                                Function.identity(),           // agrupa por la propia palabra
                                Collectors.summingInt(word -> 1)),
                        Map::copyOf));                         // Map.copyOf (Java 10): copia inmutable
    }

    /** Caso de prueba como record (estándar desde Java 16): inmutable y sin boilerplate. */
    record TestCase(String name, List<String> words, Map<String, Integer> expected) {

        /** Devuelve el mensaje de fallo, o Optional.empty() si el caso pasa. */
        Optional<String> failure() {
            var result = wordFrequency(words);
            return Optional.of(result)
                    .filter(actual -> !actual.equals(expected))
                    .map(actual -> "Caso '%s': entrada %s, esperado %s, obtenido %s"
                            .formatted(name, words, expected, actual));
        }
    }

    // Ejecutar con assertions habilitadas: java -ea WordFrequency.java
    public static void main(String[] args) {
        var assertionsEnabled = false;
        assert assertionsEnabled = true; // solo se asigna si -ea está activo
        if (!assertionsEnabled) {
            System.err.println("Advertencia: ejecuta con 'java -ea' para que los asserts se evalúen.");
        }

        var failures = Stream.of(
                        // Caso 1: ejemplo del enunciado
                        new TestCase("ejemplo del enunciado",
                                List.of("the", "fox", "the"), Map.of("the", 2, "fox", 1)),
                        // Caso 2: lista vacía
                        new TestCase("lista vacía", List.of(), Map.of()),
                        // Caso 3: distingue mayúsculas/minúsculas y cuenta repeticiones múltiples
                        new TestCase("mayúsculas y repeticiones",
                                List.of("a", "A", "a", "b", "a"), Map.of("a", 3, "A", 1, "b", 1)))
                .map(TestCase::failure)
                .flatMap(Optional::stream)                     // descarta los casos que pasan
                .toList();                                     // Stream.toList() (Java 16): lista inmutable

        assert failures.isEmpty() : String.join("\n", failures);
        System.out.println("Todas las pruebas pasaron.");
    }
}
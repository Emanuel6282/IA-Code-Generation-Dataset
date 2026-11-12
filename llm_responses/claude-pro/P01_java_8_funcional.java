import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.Collections;
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
        return Optional.ofNullable(words)                      // Optional: entrada nula -> vacía
                .map(List::stream)
                .orElseGet(Stream::empty)
                .collect(Collectors.collectingAndThen(
                        Collectors.groupingBy(
                                Function.identity(),           // agrupa por la propia palabra
                                Collectors.summingInt(word -> 1)),
                        Collections::unmodifiableMap));        // resultado inmutable
    }

    /** Construye un mapa esperado a partir de pares clave-valor, sin bucles. */
    @SafeVarargs
    private static Map<String, Integer> mapOf(SimpleEntry<String, Integer>... entries) {
        return Arrays.stream(entries).collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue));
    }

    private static SimpleEntry<String, Integer> entry(String key, int value) {
        return new SimpleEntry<>(key, value);
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
        assert wordFrequency(Stream.of("the", "fox", "the").collect(Collectors.toList()))
                .equals(mapOf(entry("the", 2), entry("fox", 1)));

        // Caso 2: lista vacía
        assert wordFrequency(Collections.<String>emptyList()).isEmpty();

        // Caso 3: distingue mayúsculas/minúsculas y cuenta repeticiones múltiples
        assert wordFrequency(Stream.of("a", "A", "a", "b", "a").collect(Collectors.toList()))
                .equals(mapOf(entry("a", 3), entry("A", 1), entry("b", 1)));

        System.out.println("Todas las pruebas pasaron.");
    }
}
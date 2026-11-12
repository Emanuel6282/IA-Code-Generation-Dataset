import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Main {

    public static Map<String, Integer> wordFrequency(List<String> words) {
        return Optional.ofNullable(words)
            .orElseGet(List::of)
            .stream()
            .collect(Collectors.groupingBy(
                word -> word,
                Collectors.summingInt(word -> 1)
            ));
    }

    public static void main(String[] args) {

        // Caso de prueba 1: Palabras repetidas
        assert wordFrequency(List.of("the", "fox", "the"))
            .equals(Map.of("the", 2, "fox", 1));

        // Caso de prueba 2: Lista vacía
        assert wordFrequency(List.of()).isEmpty();

        // Caso de prueba 3: Múltiples repeticiones
        assert wordFrequency(List.of("apple", "banana", "apple", "apple"))
            .equals(Map.of("apple", 3, "banana", 1));

        System.out.println("Todos los tests pasaron correctamente.");
    }
}
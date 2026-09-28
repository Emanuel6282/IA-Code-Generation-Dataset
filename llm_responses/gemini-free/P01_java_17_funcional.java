import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Solution {

    public static Map<String, Integer> wordFrequency(List<String> words) {
        // Paradigma funcional: procesamiento declarativo con Optional y Stream API
        return Optional.ofNullable(words)
            .map(List::stream)
            .orElseGet(() -> List.<String>of().stream())
            .collect(Collectors.groupingBy(
                Function.identity(),
                Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
            ));
    }

    public static void main(String[] args) {
        // --- Casos de prueba usando List.of y Map.of (Java 17) ---

        // Caso 1: Ejemplo provisto con palabras repetidas
        var input1 = List.of("the", "fox", "the");
        var expected1 = Map.of("the", 2, "fox", 1);
        assert wordFrequency(input1).equals(expected1) : "Falló el Caso 1";

        // Caso 2: Lista de entrada vacía
        var input2 = List.<String>of();
        assert wordFrequency(input2).isEmpty() : "Falló el Caso 2";

        // Caso 3: Palabras únicas sin repeticiones
        var input3 = List.of("java17", "stream", "functional");
        var expected3 = Map.of("java17", 1, "stream", 1, "functional", 1);
        assert wordFrequency(input3).equals(expected3) : "Falló el Caso 3";

        System.out.println("¡Todos los casos de prueba pasaron correctamente!");
    }
}
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Solution {

    public static Map<String, Integer> wordFrequency(List<String> words) {
        // Paradigma funcional utilizando Java 8 Stream API y Optional
        return Optional.ofNullable(words)
            .map(List::stream)
            .orElseGet(Collections::emptySet)
            .collect(Collectors.groupingBy(
                Function.identity(),
                Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
            ));
    }

    public static void main(String[] args) {
        // --- Casos de prueba compatibles con Java 8 ---

        // Caso 1: Ejemplo provisto con palabras repetidas
        List<String> input1 = Arrays.asList("the", "fox", "the");
        Map<String, Integer> expected1 = new HashMap<>();
        expected1.put("the", 2);
        expected1.put("fox", 1);
        assert wordFrequency(input1).equals(expected1) : "Falló el Caso 1";

        // Caso 2: Lista de entrada vacía
        List<String> input2 = Collections.emptyList();
        assert wordFrequency(input2).isEmpty() : "Falló el Caso 2";

        // Caso 3: Palabras únicas sin repeticiones
        List<String> input3 = Arrays.asList("java8", "funcional", "stream");
        Map<String, Integer> expected3 = new HashMap<>();
        expected3.put("java8", 1);
        expected3.put("funcional", 1);
        expected3.put("stream", 1);
        assert wordFrequency(input3).equals(expected3) : "Falló el Caso 3";

        System.out.println("¡Todos los casos de prueba pasaron correctamente!");
    }
}
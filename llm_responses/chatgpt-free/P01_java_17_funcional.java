import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class WordFrequency {

    public static Map<String, Integer> wordFrequency(List<String> words) {
        return Optional.ofNullable(words)
                .orElseGet(ArrayList::new)
                .stream()
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.summingInt(word -> 1)
                ));
    }

    public static void main(String[] args) {
        // Caso de prueba 1
        List<String> test1 = List.of("the", "fox", "the");

        assert wordFrequency(test1).equals(
                Map.of("the", 2, "fox", 1)
        );

        // Caso de prueba 2: lista vacía
        List<String> test2 = List.of();

        assert wordFrequency(test2).equals(
                new HashMap<String, Integer>()
        );

        // Caso de prueba 3
        List<String> test3 = List.of("hello");

        assert wordFrequency(test3).equals(
                Map.of("hello", 1)
        );

        System.out.println("Todos los tests pasaron correctamente.");
    }
}
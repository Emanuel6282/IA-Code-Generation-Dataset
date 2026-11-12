import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Main {

    public static Map<String, Integer> wordFrequency(List<String> words) {
        return Optional.ofNullable(words)
            .orElseGet(Collections::emptyList)
            .stream()
            .collect(Collectors.groupingBy(
                word -> word,
                Collectors.collectingAndThen(
                    Collectors.counting(),
                    Long::intValue
                )
            ));
    }

    public static void main(String[] args) {

        // Caso de prueba 1: Palabras repetidas
        Map<String, Integer> result1 = wordFrequency(
            Arrays.asList("the", "fox", "the")
        );

        assert result1.get("the") == 2;
        assert result1.get("fox") == 1;
        assert result1.size() == 2;

        // Caso de prueba 2: Lista vacía
        Map<String, Integer> result2 = wordFrequency(
            Collections.emptyList()
        );

        assert result2.isEmpty();

        // Caso de prueba 3: Una sola palabra
        Map<String, Integer> result3 = wordFrequency(
            Arrays.asList("hello")
        );

        assert result3.get("hello") == 1;
        assert result3.size() == 1;

        System.out.println("Todos los tests pasaron correctamente.");
    }
}
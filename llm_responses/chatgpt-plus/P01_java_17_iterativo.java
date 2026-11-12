import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static Map<String, Integer> wordFrequency(List<String> words) {
        Map<String, Integer> frequency = new HashMap<>();

        for (String word : words) {
            if (frequency.containsKey(word)) {
                frequency.put(word, frequency.get(word) + 1);
            } else {
                frequency.put(word, 1);
            }
        }

        return frequency;
    }

    public static void main(String[] args) {

        // Caso de prueba 1: Palabras repetidas
        List<String> words1 = new ArrayList<>(
            List.of("the", "fox", "the")
        );

        assert wordFrequency(words1).equals(
            Map.of("the", 2, "fox", 1)
        );

        // Caso de prueba 2: Lista vacía
        List<String> words2 = new ArrayList<>();

        assert wordFrequency(words2).isEmpty();

        // Caso de prueba 3: Una sola palabra
        List<String> words3 = new ArrayList<>(
            List.of("hello")
        );

        assert wordFrequency(words3).equals(
            Map.of("hello", 1)
        );

        System.out.println("Todos los tests pasaron correctamente.");
    }
}
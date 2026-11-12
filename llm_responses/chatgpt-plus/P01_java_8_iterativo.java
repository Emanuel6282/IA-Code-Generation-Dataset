import java.util.ArrayList;
import java.util.Arrays;
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
            Arrays.asList("the", "fox", "the")
        );

        Map<String, Integer> expected1 = new HashMap<>();
        expected1.put("the", 2);
        expected1.put("fox", 1);

        assert wordFrequency(words1).equals(expected1);

        // Caso de prueba 2: Lista vacía
        List<String> words2 = new ArrayList<>();

        assert wordFrequency(words2).isEmpty();

        // Caso de prueba 3: Una sola palabra
        List<String> words3 = new ArrayList<>(
            Arrays.asList("hello")
        );

        Map<String, Integer> expected3 = new HashMap<>();
        expected3.put("hello", 1);

        assert wordFrequency(words3).equals(expected3);

        System.out.println("Todos los tests pasaron correctamente.");
    }
}
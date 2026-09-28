import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordFrequency {

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
        // Caso de prueba 1
        assert wordFrequency(List.of("the", "fox", "the"))
                .equals(Map.of("the", 2, "fox", 1));

        // Caso de prueba 2
        assert wordFrequency(List.of())
                .equals(Map.of());

        // Caso de prueba 3
        assert wordFrequency(List.of("hello"))
                .equals(Map.of("hello", 1));

        System.out.println("Todos los tests pasaron correctamente.");
    }
}
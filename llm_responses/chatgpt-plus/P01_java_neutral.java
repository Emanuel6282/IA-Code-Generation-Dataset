import java.util.*;

public class Main {

    public static Map<String, Integer> wordFrequency(List<String> words) {
        Map<String, Integer> frequency = new HashMap<>();

        for (String word : words) {
            frequency.put(word, frequency.getOrDefault(word, 0) + 1);
        }

        return frequency;
    }

    public static void main(String[] args) {

        // Caso de prueba 1: Palabras repetidas
        assert wordFrequency(Arrays.asList("the", "fox", "the"))
                .equals(Map.of("the", 2, "fox", 1));

        // Caso de prueba 2: Lista vacía
        assert wordFrequency(Collections.emptyList()).isEmpty();

        // Caso de prueba 3: Múltiples repeticiones
        assert wordFrequency(Arrays.asList("apple", "banana", "apple"))
                .equals(Map.of("apple", 2, "banana", 1));

        System.out.println("Todos los tests pasaron correctamente.");
    }
}
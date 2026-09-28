import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Solution {

    public static Map<String, Integer> wordFrequency(List<String> words) {
        Map<String, Integer> frequencyMap = new HashMap<>();

        for (String word : words) {
            frequencyMap.merge(word, 1, Integer::sum);
        }

        return frequencyMap;
    }

    public static void main(String[] args) {
        // --- Casos de prueba ---

        // Caso 1: Ejemplo provisto con palabras repetidas
        Map<String, Integer> expected1 = Map.of("the", 2, "fox", 1);
        assert wordFrequency(List.of("the", "fox", "the")).equals(expected1) 
            : "Error en Caso 1";

        // Caso 2: Lista de entrada vacía
        assert wordFrequency(List.of()).isEmpty() 
            : "Error en Caso 2";

        // Caso 3: Palabras únicas sin repeticiones
        Map<String, Integer> expected3 = Map.of("java", 1, "code", 1, "test", 1);
        assert wordFrequency(List.of("java", "code", "test")).equals(expected3) 
            : "Error en Caso 3";

        System.out.println("¡Todos los casos de prueba pasaron correctamente!");
    }
}
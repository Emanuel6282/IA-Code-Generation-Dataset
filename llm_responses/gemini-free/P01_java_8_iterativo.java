import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Solution {

    public static Map<String, Integer> wordFrequency(List<String> words) {
        Map<String, Integer> frequencyMap = new HashMap<>();

        // Paradigma iterativo con bucle for-each tradicional
        for (String word : words) {
            // Método getOrDefault introducido en Java 8 (sin expresiones lambda)
            frequencyMap.put(word, frequencyMap.getOrDefault(word, 0) + 1);
        }

        return frequencyMap;
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
        List<String> input3 = Arrays.asList("java8", "iterativo", "código");
        Map<String, Integer> expected3 = new HashMap<>();
        expected3.put("java8", 1);
        expected3.put("iterativo", 1);
        expected3.put("código", 1);
        assert wordFrequency(input3).equals(expected3) : "Falló el Caso 3";

        System.out.println("¡Todos los casos de prueba pasaron correctamente!");
    }
}
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Solution {

    public static Map<String, Integer> wordFrequency(List<String> words) {
        // Inicialización de variable local usando 'var' (característica estándar en Java 17)
        var frequencyMap = new HashMap<String, Integer>();

        // Paradigma iterativo: bucle for-each imperativo con estado mutable
        for (String word : words) {
            frequencyMap.put(word, frequencyMap.getOrDefault(word, 0) + 1);
        }

        return frequencyMap;
    }

    public static void main(String[] args) {
        // --- Casos de prueba usando List.of() y Map.of() (características de colecciones inmutables) ---

        // Caso 1: Ejemplo provisto con palabras repetidas
        var input1 = List.of("the", "fox", "the");
        var expected1 = Map.of("the", 2, "fox", 1);
        assert wordFrequency(input1).equals(expected1) : "Falló el Caso 1";

        // Caso 2: Lista de entrada vacía
        var input2 = List.<String>of();
        assert wordFrequency(input2).isEmpty() : "Falló el Caso 2";

        // Caso 3: Palabras únicas sin repeticiones
        var input3 = List.of("java17", "iterativo", "código");
        var expected3 = Map.of("java17", 1, "iterativo", 1, "código", 1);
        assert wordFrequency(input3).equals(expected3) : "Falló el Caso 3";

        System.out.println("¡Todos los casos de prueba pasaron correctamente!");
    }
}
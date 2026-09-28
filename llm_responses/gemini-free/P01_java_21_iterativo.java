import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Java 21 permite el método main sin la declaración de la clase pública ceremonial ni argumentos en las pruebas.
public class Solution {

    public static Map<String, Integer> wordFrequency(List<String> words) {
        // Uso de 'var' e inicialización imperativa
        var frequencyMap = new HashMap<String, Integer>();

        // Paradigma iterativo: bucle for-each tradicional acumulando sobre estado mutable
        for (String word : words) {
            frequencyMap.put(word, frequencyMap.getOrDefault(word, 0) + 1);
        }

        return frequencyMap;
    }

    public static void main(String[] args) {
        // --- Casos de prueba usando colecciones inmutables (List.of / Map.of) ---

        // Caso 1: Ejemplo provisto con palabras repetidas
        var input1 = List.of("the", "fox", "the");
        var expected1 = Map.of("the", 2, "fox", 1);
        assert wordFrequency(input1).equals(expected1) : "Falló el Caso 1";

        // Caso 2: Lista de entrada vacía
        var input2 = List.<String>of();
        assert wordFrequency(input2).isEmpty() : "Falló el Caso 2";

        // Caso 3: Palabras únicas sin repeticiones
        var input3 = List.of("java21", "iterativo", "código");
        var expected3 = Map.of("java21", 1, "iterativo", 1, "código", 1);
        assert wordFrequency(input3).equals(expected3) : "Falló el Caso 3";

        System.out.println("¡Todos los casos de prueba pasaron correctamente!");
    }
}
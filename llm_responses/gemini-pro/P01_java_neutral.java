import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.ArrayList;

public class WordFrequencyTest {

    /**
     * Dada una lista de palabras, retorna un mapa con la frecuencia de cada palabra.
     */
    public static Map<String, Integer> wordFrequency(List<String> words) {
        Map<String, Integer> frequencyMap = new HashMap<>();
        
        // Si la lista es nula o está vacía, retorna la estructura vacía
        if (words == null || words.isEmpty()) {
            return frequencyMap;
        }

        // Cuenta las frecuencias utilizando getOrDefault para simplificar la lógica
        for (String word : words) {
            frequencyMap.put(word, frequencyMap.getOrDefault(word, 0) + 1);
        }

        return frequencyMap;
    }

    public static void main(String[] args) {
        // --- Casos de prueba ---
        
        // 1. Caso base del ejemplo
        Map<String, Integer> result1 = wordFrequency(Arrays.asList("the", "fox", "the"));
        assert result1.size() == 2 : "Error en el tamaño del caso 1";
        assert result1.get("the") == 2 : "Error en el conteo de 'the'";
        assert result1.get("fox") == 1 : "Error en el conteo de 'fox'";

        // 2. Caso con una lista vacía
        Map<String, Integer> result2 = wordFrequency(new ArrayList<>());
        assert result2.isEmpty() : "Error en el caso 2: el mapa debería estar vacío";

        // 3. Caso con repeticiones múltiples
        Map<String, Integer> result3 = wordFrequency(Arrays.asList("java", "code", "java", "code", "java"));
        assert result3.size() == 2 : "Error en el tamaño del caso 3";
        assert result3.get("java") == 3 : "Error en el conteo de 'java'";
        assert result3.get("code") == 2 : "Error en el conteo de 'code'";

        System.out.println("Todos los casos de prueba se ejecutaron correctamente.");
        System.out.println("Nota: Para que los 'assert' de Java arrojen excepción si fallan, recuerda ejecutar la JVM con la bandera -ea (enableassertions).");
    }
}
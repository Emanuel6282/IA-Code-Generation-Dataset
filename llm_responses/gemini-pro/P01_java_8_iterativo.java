import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordFrequencyIterative {

    /**
     * Dada una lista de palabras, retorna un mapa con la frecuencia 
     * de cada palabra de forma estrictamente iterativa.
     */
    public static Map<String, Integer> wordFrequency(List<String> words) {
        Map<String, Integer> frequencyMap = new HashMap<>();
        
        // Verificación de seguridad para listas nulas
        if (words == null) {
            return frequencyMap;
        }

        // Bucle for tradicional (paradigma iterativo)
        // Se evita el uso de Streams o lambdas
        for (String word : words) {
            // getOrDefault es una característica introducida en Java 8 
            // que simplifica el clásico if/else iterativo.
            frequencyMap.put(word, frequencyMap.getOrDefault(word, 0) + 1);
        }

        return frequencyMap;
    }

    public static void main(String[] args) {
        // ==========================================
        // CASOS DE PRUEBA
        // ==========================================
        // Nota: Ejecutar la JVM con el argumento '-ea' (enableassertions) 
        // para que las aserciones funcionen.

        // 1. Caso base del ejemplo proporcionado
        Map<String, Integer> result1 = wordFrequency(Arrays.asList("the", "fox", "the"));
        assert result1.size() == 2 : "Error en el tamaño del resultado 1";
        assert result1.get("the") != null && result1.get("the") == 2 : "Error en el conteo de 'the'";
        assert result1.get("fox") != null && result1.get("fox") == 1 : "Error en el conteo de 'fox'";

        // 2. Caso con una lista vacía (ArrayList explícito como se solicitó)
        Map<String, Integer> result2 = wordFrequency(new ArrayList<String>());
        assert result2 != null && result2.isEmpty() : "Error: El resultado 2 debe ser un mapa vacío";

        // 3. Caso con repeticiones múltiples
        Map<String, Integer> result3 = wordFrequency(Arrays.asList("iterativo", "java", "iterativo", "c", "java", "iterativo"));
        assert result3.size() == 3 : "Error en el tamaño del resultado 3";
        assert result3.get("iterativo") != null && result3.get("iterativo") == 3 : "Error en el conteo de 'iterativo'";
        assert result3.get("java") != null && result3.get("java") == 2 : "Error en el conteo de 'java'";
        assert result3.get("c") != null && result3.get("c") == 1 : "Error en el conteo de 'c'";

        System.out.println("Todos los tests iterativos en Java 8 pasaron correctamente.");
    }
}
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordFrequencyIterative21 {

    /**
     * Dada una lista de palabras, retorna un mapa con la frecuencia 
     * de cada palabra utilizando un enfoque estrictamente iterativo.
     */
    public static Map<String, Integer> wordFrequency(List<String> words) {
        // Uso de 'var' para inferencia local de tipos, manteniendo el código limpio.
        var frequencyMap = new HashMap<String, Integer>();
        
        // Verificación de seguridad
        if (words == null || words.isEmpty()) {
            return frequencyMap;
        }

        // Bucle iterativo clásico. Se evita cualquier uso de la Stream API, 
        // lambdas (como Map.computeIfAbsent) o recursión.
        for (var word : words) {
            frequencyMap.put(word, frequencyMap.getOrDefault(word, 0) + 1);
        }

        return frequencyMap;
    }

    public static void main(String[] args) {
        // ==========================================
        // CASOS DE PRUEBA
        // ==========================================
        // Nota: Para que los assert validen correctamente, la JVM debe 
        // ejecutarse con la bandera '-ea' (enableassertions).

        // 1. Caso base del ejemplo proporcionado
        // Utilizamos List.of() (factory inmutable moderno) y 'var'.
        var list1 = List.of("the", "fox", "the");
        var result1 = wordFrequency(list1);
        assert result1.size() == 2 : "Error en el tamaño del resultado 1";
        assert result1.get("the") != null && result1.get("the") == 2 : "Error en el conteo de 'the'";
        assert result1.get("fox") != null && result1.get("fox") == 1 : "Error en el conteo de 'fox'";

        // 2. Caso con una lista vacía
        // Se instancia un ArrayList vacío explícitamente, tal como pide el enfoque imperativo.
        var list2 = new ArrayList<String>();
        var result2 = wordFrequency(list2);
        assert result2 != null && result2.isEmpty() : "Error: El resultado 2 debe ser un mapa vacío";

        // 3. Caso con repeticiones múltiples
        var list3 = List.of("java", "veintiuno", "java", "iterativo", "veintiuno", "java");
        var result3 = wordFrequency(list3);
        assert result3.size() == 3 : "Error en el tamaño del resultado 3";
        assert result3.get("java") != null && result3.get("java") == 3 : "Error en el conteo de 'java'";
        assert result3.get("veintiuno") != null && result3.get("veintiuno") == 2 : "Error en el conteo de 'veintiuno'";
        assert result3.get("iterativo") != null && result3.get("iterativo") == 1 : "Error en el conteo de 'iterativo'";

        System.out.println("Todos los tests iterativos en Java 21 pasaron correctamente.");
    }
}
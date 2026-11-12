import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class WordFrequencyFunctional {

    /**
     * Dada una lista de palabras, retorna un mapa con la frecuencia 
     * de cada palabra de forma estrictamente funcional (sin bucles).
     */
    public static Map<String, Integer> wordFrequency(List<String> words) {
        // 1. Optional evita condicionales imperativos (if words == null)
        // 2. orElseGet provee una lista vacía inmutable si la entrada es nula
        // 3. stream() inicia el pipeline funcional
        // 4. collect y groupingBy aplican la reducción funcional declarativa
        return Optional.ofNullable(words)
                .orElseGet(Collections::emptyList)
                .stream()
                .collect(Collectors.groupingBy(
                        Function.identity(),             // La clave es la palabra misma
                        Collectors.summingInt(w -> 1)    // El valor es la suma de sus apariciones
                ));
    }

    public static void main(String[] args) {
        // ==========================================
        // CASOS DE PRUEBA
        // ==========================================
        // Nota: Ejecutar la JVM con el argumento '-ea' (enableassertions) 
        // para que las aserciones validen correctamente.

        // 1. Caso base del ejemplo proporcionado
        Map<String, Integer> result1 = wordFrequency(Arrays.asList("the", "fox", "the"));
        assert result1.size() == 2 : "Error en el tamaño del resultado 1";
        assert result1.get("the").equals(2) : "Error en el conteo de 'the'";
        assert result1.get("fox").equals(1) : "Error en el conteo de 'fox'";

        // 2. Caso con una lista vacía
        Map<String, Integer> result2 = wordFrequency(Collections.emptyList());
        assert result2.isEmpty() : "Error: El resultado 2 debe ser un mapa vacío";

        // 3. Caso con repeticiones múltiples comprobando el paradigma funcional
        Map<String, Integer> result3 = wordFrequency(Arrays.asList("stream", "lambda", "stream", "optional", "lambda", "stream"));
        assert result3.size() == 3 : "Error en el tamaño del resultado 3";
        assert result3.get("stream").equals(3) : "Error en el conteo de 'stream'";
        assert result3.get("lambda").equals(2) : "Error en el conteo de 'lambda'";
        assert result3.get("optional").equals(1) : "Error en el conteo de 'optional'";

        System.out.println("Todos los tests funcionales en Java 8 pasaron correctamente.");
    }
}
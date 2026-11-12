import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class WordFrequencyFunctional21 {

    /**
     * Dada una lista de palabras, retorna un mapa con la frecuencia 
     * de cada palabra de forma estrictamente funcional (sin bucles mutables).
     */
    public static Map<String, Integer> wordFrequency(List<String> words) {
        // 1. Optional.ofNullable evita declaraciones condicionales if/else imperativas.
        // 2. orElseGet(List::of) provee una colección vacía inmutable de forma perezosa en caso de nulos.
        // 3. stream() inicia la evaluación funcional.
        // 4. groupingBy agrupa por identidad (la palabra en sí).
        // 5. summingInt actúa como función de reducción pura, sumando 1 por cada ocurrencia.
        return Optional.ofNullable(words)
                .orElseGet(List::of)
                .stream()
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.summingInt(w -> 1)
                ));
    }

    public static void main(String[] args) {
        // ==========================================
        // CASOS DE PRUEBA
        // ==========================================
        // Nota: Ejecutar la JVM con el argumento '-ea' (enableassertions) 
        // para que las aserciones validen correctamente.

        // 1. Caso base del ejemplo proporcionado
        // Uso de 'var' para inferencia de tipos y 'List.of()' para inmutabilidad
        var list1 = List.of("the", "fox", "the");
        var result1 = wordFrequency(list1);
        assert result1.size() == 2 : "Error en el tamaño del resultado 1";
        assert result1.get("the").equals(2) : "Error en el conteo de 'the'";
        assert result1.get("fox").equals(1) : "Error en el conteo de 'fox'";

        // 2. Caso con una lista vacía
        var list2 = List.<String>of();
        var result2 = wordFrequency(list2);
        assert result2.isEmpty() : "Error: El resultado 2 debe ser un mapa vacío";

        // 3. Caso con repeticiones múltiples evaluando la pureza funcional
        var list3 = List.of("java", "funcional", "java", "flujo", "funcional", "funcional");
        var result3 = wordFrequency(list3);
        assert result3.size() == 3 : "Error en el tamaño del resultado 3";
        assert result3.get("funcional").equals(3) : "Error en el conteo de 'funcional'";
        assert result3.get("java").equals(2) : "Error en el conteo de 'java'";
        assert result3.get("flujo").equals(1) : "Error en el conteo de 'flujo'";

        System.out.println("Todos los tests funcionales en Java 21 pasaron correctamente.");
    }
}
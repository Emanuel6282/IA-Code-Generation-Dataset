import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class P03FlattenNestedListFunctional_v8 {

    /**
     * Toma una lista arbitrariamente anidada (representada como List<Object>)
     * y retorna una lista plana unidimensional de enteros.
     * 
     * Implementación estrictamente funcional en Java 8: 
     * Basada en el API de Streams (flatMap), recursión pura y Optional.
     */
    @SuppressWarnings("unchecked")
    public static List<Integer> flatten(List<Object> nestedList) {
        // Envolvemos la entrada en un Optional para prevenir NullPointerExceptions 
        // de forma declarativa sin usar condicionales if (nestedList == null).
        return Optional.ofNullable(nestedList)
            .orElseGet(Collections::emptyList) // Retorna lista vacía si es null
            .stream()
            // flatMap "aplana" los múltiples Streams resultantes en uno solo continuo
            .flatMap(item -> {
                if (item instanceof Integer) {
                    // Si es un escalar, lo envolvemos en un Stream unitario
                    return Stream.of((Integer) item);
                } else if (item instanceof List) {
                    // Si es una lista, llamada recursiva pura y conversión a Stream
                    return flatten((List<Object>) item).stream();
                }
                // Si encontramos un tipo no deseado, retornamos un Stream vacío para ignorarlo
                return Stream.empty();
            })
            // Recolectamos el flujo resultante en una nueva lista, sin mutar estructuras previas
            .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        // NOTA: Para ejecutar estos tests, asegúrate de habilitar las aserciones 
        // en la JVM usando el flag "-ea" (ej. java -ea P03FlattenNestedListFunctional_v8)
        
        // ==========================================
        // CASOS DE PRUEBA
        // ==========================================

        // 1. Caso base del ejemplo: múltiples niveles de anidamiento
        List<Object> nested1 = Arrays.asList(1, Arrays.asList(2, 3), Arrays.asList(4, Arrays.asList(5, 6)));
        List<Integer> expected1 = Arrays.asList(1, 2, 3, 4, 5, 6);
        assert flatten(nested1).equals(expected1) : "Error: Falló el caso base del ejemplo";

        // 2. Caso límite: lista completamente vacía
        List<Object> nested2 = Collections.emptyList();
        List<Integer> expected2 = Collections.emptyList();
        assert flatten(nested2).equals(expected2) : "Error: Una lista vacía debería retornar una lista vacía";

        // 3. Caso extremo: anidamiento profundo con listas vacías en el medio
        List<Object> nested3 = Arrays.asList(1, Collections.emptyList(), Arrays.asList(Arrays.asList(Arrays.asList(3))));
        List<Integer> expected3 = Arrays.asList(1, 3);
        assert flatten(nested3).equals(expected3) : "Error: Falló ignorando listas vacías internas";

        System.out.println("Todos los tests funcionales en Java 8 pasaron correctamente.");
    }
}
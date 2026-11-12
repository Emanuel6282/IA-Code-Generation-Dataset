// CARACTERISTICAS DE JAVA 17 UTILIZADAS Y POR QUE:
// 1. Pattern Matching para instanceof (JEP 394): Permite evaluar el tipo de dato y extraer la variable casteada directamente (`item instanceof Integer number`). En el paradigma funcional, esto limpia el código lambda eliminando los casteos manuales ruidosos.
// 2. Stream.toList() (JEP 395 / Java 16+): Reemplaza la antigua verbosidad de `.collect(Collectors.toList())`. Además, devuelve una lista no modificable (unmodifiable list), lo que encaja perfectamente con las garantías de inmutabilidad del paradigma funcional puro.
// 3. Colecciones de Fábrica Inmutables (List.of): Se utiliza tanto para proveer el valor por defecto en el Optional como para construir los casos de prueba de manera declarativa e inmutable.

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class P03FlattenNestedListFunctional_v17 {

    /**
     * Toma una lista arbitrariamente anidada y retorna una lista plana unidimensional.
     * 
     * Implementación estrictamente funcional en Java 17:
     * Sin bucles imperativos, utilizando el API de Streams (flatMap), recursión pura,
     * Optional para seguridad de nulos e inmutabilidad garantizada.
     */
    @SuppressWarnings("unchecked")
    public static List<Integer> flatten(List<Object> nestedList) {
        // Envolvemos la entrada en un Optional para prevenir nulos funcionalmente
        return Optional.ofNullable(nestedList)
            .orElseGet(List::of) // Si es null, retorna una lista inmutable vacía
            .stream()
            // flatMap mapea cada elemento a un Stream y "aplana" todos en uno solo
            .flatMap(item -> {
                // Pattern Matching de Java 17 limpia la evaluación de tipos
                if (item instanceof Integer number) {
                    return Stream.of(number);
                } else if (item instanceof List<?> list) {
                    // Recursión funcional: aplanamos la sublista y pedimos su stream
                    return flatten((List<Object>) list).stream();
                }
                // Si hay elementos nulos o tipos incorrectos, los ignoramos con un Stream vacío
                return Stream.empty();
            })
            // Terminal del Stream optimizada en Java 16/17 que retorna una List inmutable
            .toList();
    }

    public static void main(String[] args) {
        // NOTA: Para ejecutar estos tests, asegúrate de habilitar las aserciones 
        // en la JVM usando el flag "-ea" (ej. java -ea P03FlattenNestedListFunctional_v17)
        
        // ==========================================
        // CASOS DE PRUEBA
        // ==========================================

        // 1. Caso base del ejemplo: múltiples niveles de anidamiento
        List<Object> nested1 = List.of(1, List.of(2, 3), List.of(4, List.of(5, 6)));
        List<Integer> expected1 = List.of(1, 2, 3, 4, 5, 6);
        assert flatten(nested1).equals(expected1) : "Error: Falló el caso base del ejemplo";

        // 2. Caso límite: lista completamente vacía
        List<Object> nested2 = List.of();
        List<Integer> expected2 = List.of();
        assert flatten(nested2).equals(expected2) : "Error: Una lista vacía debería retornar una lista vacía";

        // 3. Caso extremo: anidamiento profundo asimétrico con listas vacías en el medio
        List<Object> nested3 = List.of(1, List.of(), List.of(List.of(List.of(3))));
        List<Integer> expected3 = List.of(1, 3);
        assert flatten(nested3).equals(expected3) : "Error: Falló al procesar listas vacías internas";

        System.out.println("Todos los tests funcionales en Java 17 pasaron correctamente.");
    }
}
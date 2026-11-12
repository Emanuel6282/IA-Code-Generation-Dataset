// CARACTERISTICAS DE JAVA 21 UTILIZADAS Y POR QUE:
// 1. Pattern Matching para switch como expresión (JEP 441): En Java 21, `switch` puede usarse como una expresión funcional que retorna valores (usando `->` en lugar de `:`) y que hace coincidencia de tipos directamente (`case Integer i`). Esto permite reemplazar la clásica cadena imperativa de `if-else if` con `instanceof` por un bloque declarativo limpio directamente dentro del pipeline del Stream.
// 2. Casos Null en Switch: Java 21 permite integrar explícitamente `case null` dentro del `switch`. Esto encaja perfectamente en el paradigma funcional, permitiendo manejar valores ausentes dentro del mismo flujo sin requerir filtros o condicionales previos.
// 3. Stream.toList(): Proporciona una recolección directa hacia una lista inmutable, garantizando la pureza de la función al evitar modificaciones posteriores.

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class P03FlattenNestedListFunctional_v21 {

    /**
     * Toma una lista arbitrariamente anidada y retorna una lista plana unidimensional.
     * 
     * Implementación estrictamente funcional en Java 21:
     * Basada en Streams, flatMap, recursión pura y expresiones Switch con Pattern Matching.
     */
    @SuppressWarnings("unchecked")
    public static List<Integer> flatten(List<Object> nestedList) {
        // Envolvemos la lista en Optional para un manejo seguro y declarativo de nulos
        return Optional.ofNullable(nestedList)
            .orElseGet(List::of)
            .stream()
            // flatMap transforma cada elemento en un Stream y los aplana en uno solo
            .flatMap(item -> 
                // Expresión Switch funcional de Java 21 con Pattern Matching
                switch (item) {
                    case Integer i -> 
                        Stream.of(i); // Si es entero, lo envuelve en un Stream unitario
                        
                    case List<?> l -> 
                        // Recursión pura: aplanamos la sublista y pedimos su Stream
                        flatten((List<Object>) l).stream(); 
                        
                    case null, default -> 
                        // Si es nulo o de un tipo no reconocido, retornamos un Stream vacío
                        Stream.empty(); 
                }
            )
            // Recolectamos el resultado en una lista inmutable (Java 16+)
            .toList();
    }

    public static void main(String[] args) {
        // NOTA: Para ejecutar estos tests, asegúrate de habilitar las aserciones 
        // en la JVM usando el flag "-ea" (ej. java -ea P03FlattenNestedListFunctional_v21)
        
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

        System.out.println("Todos los tests funcionales en Java 21 pasaron correctamente.");
    }
}
// CARACTERISTICAS DE JAVA 21 UTILIZADAS Y POR QUE:
// 1. Sequenced Collections (JEP 431): Java 21 introdujo interfaces unificadas para colecciones secuenciadas. Ahora `List` posee métodos nativos como `.reversed()` (devuelve una vista invertida de la lista en O(1)) y `.removeLast()`. Esto elimina la necesidad de escribir bucles `for` inversos manuales, haciendo que el manejo de la pila (stack) iterativa sea extremadamente elegante y seguro.
// 2. Pattern Matching para switch (JEP 441): Completado en Java 21, permite evaluar tipos dinámicos (`instanceof`) y extraer variables directamente dentro de las ramas de un bloque `switch`. Reemplaza las engorrosas cadenas `if-else if` y mejora la legibilidad arquitectónica del algoritmo.
// 3. Casos Null en Switch: Java 21 permite capturar explícitamente `case null` dentro del `switch`, previniendo los clásicos `NullPointerException` sin requerir condicionales previos.

import java.util.ArrayList;
import java.util.List;

public class P03FlattenNestedListIterative_v21 {

    /**
     * Toma una lista arbitrariamente anidada y retorna una lista plana unidimensional.
     * 
     * Implementación estrictamente iterativa en Java 21: sin recursión ni Streams,
     * utilizando una pila (stack) basada en ArrayList y las nuevas Sequenced Collections.
     */
    public static List<Integer> flatten(List<Object> nestedList) {
        var result = new ArrayList<Integer>();
        
        // Inicializamos la pila inyectando la lista original invertida.
        // El método .reversed() de Java 21 provee una vista inversa inmediata.
        var stack = new ArrayList<Object>(nestedList.reversed());
        
        while (!stack.isEmpty()) {
            // Usamos .removeLast() introducido en Sequenced Collections (Java 21)
            // para extraer el elemento en el tope de la pila eficientemente
            Object item = stack.removeLast();
            
            // Pattern Matching para switch (Java 21)
            switch (item) {
                case Integer number -> 
                    result.add(number);
                    
                case List<?> list -> 
                    // Si es una sublista, obtenemos su vista invertida y la agregamos 
                    // a la pila para que sus elementos se procesen en el orden correcto
                    stack.addAll(list.reversed());
                    
                case null, default -> {
                    // Ignora elementos nulos o de tipos no soportados de manera segura
                }
            }
        }
        
        return result;
    }

    public static void main(String[] args) {
        // NOTA: Para ejecutar estos tests, asegúrate de habilitar las aserciones 
        // en la JVM usando el flag "-ea" (ej. java -ea P03FlattenNestedListIterative_v21)
        
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

        // 3. Caso extremo: anidamiento profundo con listas vacías en el medio
        List<Object> nested3 = List.of(1, List.of(), List.of(List.of(List.of(3))));
        List<Integer> expected3 = List.of(1, 3);
        assert flatten(nested3).equals(expected3) : "Error: Falló ignorando listas vacías internas";

        System.out.println("Todos los tests iterativos en Java 21 pasaron correctamente.");
    }
}
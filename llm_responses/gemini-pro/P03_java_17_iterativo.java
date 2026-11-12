// CARACTERISTICAS DE JAVA 17 UTILIZADAS Y POR QUE:
// 1. Pattern Matching para instanceof (JEP 394): Permite evaluar el tipo y declarar la variable casteada en la misma línea (ej. `item instanceof Integer number`). Esto elimina los peligrosos y ruidosos casteos manuales explícitos `(Integer) item` de las versiones antiguas de Java.
// 2. Inferencia de tipos locales con 'var' (JEP 286): Reduce la verbosidad en la declaración de variables locales iterativas (como la pila o la lista de resultados) dejando que el compilador deduzca el tipo.
// 3. Colecciones de Fábrica Inmutables (List.of): Introducidas a partir de Java 9, reemplazan el verboso `Arrays.asList()`. Permiten construir estructuras de datos complejas para los tests de forma concisa y declarativa.

import java.util.ArrayList;
import java.util.List;

public class P03FlattenNestedListIterative_v17 {

    /**
     * Toma una lista arbitrariamente anidada y retorna una lista plana unidimensional.
     * 
     * Implementación estrictamente iterativa: sin recursión ni Streams,
     * administrando el estado de profundidad con un bucle while y una pila explícita.
     */
    public static List<Integer> flatten(List<Object> nestedList) {
        var result = new ArrayList<Integer>();
        
        // Pila (stack) manual iterativa para rastrear las sublistas
        var stack = new ArrayList<Object>();
        
        // Se inyectan los elementos en orden inverso a la pila para asegurar
        // que, al hacer LIFO (Last In, First Out), salgan de izquierda a derecha.
        for (int i = nestedList.size() - 1; i >= 0; i--) {
            stack.add(nestedList.get(i));
        }
        
        while (!stack.isEmpty()) {
            var item = stack.remove(stack.size() - 1);
            
            // Pattern Matching: Java 17 extrae e instancia automáticamente la variable 'number' o 'list'
            if (item instanceof Integer number) {
                result.add(number);
            } else if (item instanceof List<?> list) {
                // Al detectar una sublista, iteramos en orden inverso y apilamos
                for (int i = list.size() - 1; i >= 0; i--) {
                    stack.add(list.get(i));
                }
            }
        }
        
        return result;
    }

    public static void main(String[] args) {
        // NOTA: Para ejecutar estos tests, asegúrate de habilitar las aserciones 
        // en la JVM usando el flag "-ea" (ej. java -ea P03FlattenNestedListIterative_v17)
        
        // ==========================================
        // CASOS DE PRUEBA
        // ==========================================

        // 1. Caso base del ejemplo: múltiples niveles de anidamiento
        List<Object> nested1 = List.of(1, List.of(2, 3), List.of(4, List.of(5, 6)));
        var expected1 = List.of(1, 2, 3, 4, 5, 6);
        assert flatten(nested1).equals(expected1) : "Error: Falló el caso base del ejemplo";

        // 2. Caso límite: lista completamente vacía
        List<Object> nested2 = List.of();
        var expected2 = List.of();
        assert flatten(nested2).equals(expected2) : "Error: Una lista vacía debería retornar una lista vacía";

        // 3. Caso extremo: anidamiento profundo con listas vacías en el medio
        List<Object> nested3 = List.of(1, List.of(), List.of(List.of(List.of(3))));
        var expected3 = List.of(1, 3);
        assert flatten(nested3).equals(expected3) : "Error: Falló al ignorar listas vacías internas";

        System.out.println("Todos los tests iterativos en Java 17 pasaron correctamente.");
    }
}
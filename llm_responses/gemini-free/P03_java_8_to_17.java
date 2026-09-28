// Características de Java 17 utilizadas en este refactor:
// 1. Pattern Matching para instanceof (JEP 394 - Java 16/17):
//    Elimina los casteo explícitos redundantes (`(List<?>) item` o `(Integer) item`) vinculando
//    el tipo directamente a la variable de patrón en la comprobación.
// 2. Inferencia de tipos con `var` (JEP 286 - Java 10):
//    Reduce la verbosidad en declaraciones de variables locales sin perder la seguridad de tipado estático.
// 3. Métodos de fábrica para colecciones inmutables (`List.of`) (JEP 269 - Java 9):
//    Sustituye `Arrays.asList` y `Collections.emptyList()` por `List.of()`, creando colecciones inmutables
//    más seguras, concisas y de mejor rendimiento.

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class P03FlattenList_v17 {

    /**
     * Aplana una lista arbitrariamente anidada de enteros usando Java 17.
     */
    public static List<Integer> flatten(Object nested) {
        var result = new ArrayList<Integer>();
        Deque<Object> stack = new ArrayDeque<>();
        stack.push(nested);

        while (!stack.isEmpty()) {
            Object item = stack.pop();

            // Pattern Matching para instanceof evita casteo explícito
            if (item instanceof List<?> list) {
                // Agregar en orden inverso para preservar el orden original
                for (int i = list.size() - 1; i >= 0; i--) {
                    stack.push(list.get(i));
                }
            } else if (item instanceof Integer val) {
                result.add(val);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        // TC01: anidamiento basico
        List<Object> input1 = List.of(1, List.of(2, 3), List.of(4, List.of(5, 6)));
        assert flatten(input1).equals(List.of(1, 2, 3, 4, 5, 6)) : "TC01 fallo";

        // TC02: lista vacia
        assert flatten(List.of()).equals(List.of()) : "TC02 fallo";

        // TC03: ya plana
        List<Object> input3 = List.of(1, 2, 3);
        assert flatten(input3).equals(List.of(1, 2, 3)) : "TC03 fallo";

        System.out.println("All tests passed");
    }
}

// --- Explicación de los cambios ---
// 1. Reemplazo de `isinstance` y casteo manual por Pattern Matching:
//    `if (item instanceof List<?> list)` y `else if (item instanceof Integer val)` reducen el código repetitivo,
//    evitando la doble declaración del tipo y garantizando un código más limpio y seguro contra errores de casteo.
// 2. Uso de `var` para variables locales:
//    Se utiliza `var result = new ArrayList<Integer>()` para evitar redundancia en el lado izquierdo de la declaración.
// 3. Modernización de las estructuras de prueba con `List.of()`:
//    Se reemplazó `Arrays.asList()` y `Collections.emptyList()` por `List.of()`, la forma estándar e idiomática
//    en Java moderno para crear listas inmutables de manera concisa.
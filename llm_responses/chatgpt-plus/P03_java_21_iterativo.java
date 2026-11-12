// P03: Flatten Nested List — Java 21
// Paradigma: Estrictamente iterativo
//
// CARACTERÍSTICAS UTILIZADAS:
// 1. List.getLast() (Java 21):
//    Permite acceder al último elemento de una lista.
// 2. Pattern matching con instanceof (Java 16+):
//    Comprueba el tipo y declara la variable en una operación.
// 3. List.of() (Java 9+):
//    Simplifica la creación de listas para las pruebas.
// 4. ArrayList y ArrayDeque:
//    Almacenan el resultado y los elementos pendientes.
// 5. Bucles for y while:
//    Mantienen el algoritmo estrictamente iterativo.
//
// Sin Streams, lambdas, recursión ni librerías externas.

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class P03Flatten_v21 {

    public static List<Integer> flatten(List<?> nested) {
        List<Integer> result = new ArrayList<>();
        Deque<Object> stack = new ArrayDeque<>();

        // Introducir los elementos iniciales en orden inverso.
        for (int i = nested.size() - 1; i >= 0; i--) {
            stack.push(nested.get(i));
        }

        // Procesar iterativamente todos los elementos.
        while (!stack.isEmpty()) {
            Object item = stack.pop();

            if (item instanceof List<?> sublist) {

                // Introducir los elementos en orden inverso.
                for (int i = sublist.size() - 1; i >= 0; i--) {
                    stack.push(sublist.get(i));
                }

            } else {
                result.add((Integer) item);
            }
        }

        return result;
    }

    public static void main(String[] args) {

        // Test 1: Lista con varios niveles de anidamiento
        assert flatten(List.of(
                1,
                List.of(2, 3),
                List.of(4, List.of(5, 6))
        )).equals(List.of(1, 2, 3, 4, 5, 6));

        // Test 2: Lista vacía
        assert flatten(List.of()).equals(List.of());

        // Test 3: Lista plana
        assert flatten(List.of(1, 2, 3))
                .equals(List.of(1, 2, 3));

        System.out.println("All tests passed");
    }
}
// P03: Flatten Nested List — Java 8
// Paradigma: Estrictamente iterativo
//
// Características:
// - Uso de bucles for y while.
// - Uso de ArrayList para almacenar resultados.
// - Uso de ArrayDeque como pila explícita.
// - Sin Streams, lambdas ni recursión.
// - Biblioteca estándar únicamente.

import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

public class P03Flatten_v8 {

    public static List<Integer> flatten(List<?> nested) {
        List<Integer> result = new ArrayList<>();
        Deque<Object> stack = new ArrayDeque<>();

        // Insertar los elementos en orden inverso.
        for (int i = nested.size() - 1; i >= 0; i--) {
            stack.push(nested.get(i));
        }

        // Procesar todos los elementos iterativamente.
        while (!stack.isEmpty()) {
            Object item = stack.pop();

            if (item instanceof List<?>) {
                List<?> sublist = (List<?>) item;

                // Mantener el orden original de aparición.
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
        assert flatten(Arrays.asList(
                1,
                Arrays.asList(2, 3),
                Arrays.asList(4, Arrays.asList(5, 6))
        )).equals(Arrays.asList(1, 2, 3, 4, 5, 6));

        // Test 2: Lista vacía
        assert flatten(Collections.emptyList())
                .equals(Collections.emptyList());

        // Test 3: Lista plana
        assert flatten(Arrays.asList(1, 2, 3))
                .equals(Arrays.asList(1, 2, 3));

        System.out.println("All tests passed");
    }
}
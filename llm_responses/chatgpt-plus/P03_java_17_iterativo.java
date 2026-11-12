// P03: Flatten Nested List — Java 17
// Paradigma: Estrictamente iterativo
//
// Características utilizadas:
// 1. Pattern matching para instanceof:
//    Permite comprobar y convertir tipos sin un cast explícito.
// 2. List.of():
//    Facilita la creación de listas para los casos de prueba.
// 3. Inferencia de tipos con <>:
//    Evita repetir parámetros genéricos en constructores.
// 4. ArrayList y ArrayDeque:
//    Permiten almacenar resultados y procesar una pila.
// 5. Bucles for y while:
//    Mantienen el algoritmo completamente iterativo.

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class P03Flatten_v17 {

    public static List<Integer> flatten(List<?> nested) {
        List<Integer> result = new ArrayList<>();
        Deque<Object> stack = new ArrayDeque<>();

        // Insertar los elementos iniciales en orden inverso.
        for (int i = nested.size() - 1; i >= 0; i--) {
            stack.push(nested.get(i));
        }

        // Procesar los elementos sin utilizar recursión.
        while (!stack.isEmpty()) {
            Object item = stack.pop();

            // Pattern matching para instanceof.
            if (item instanceof List<?> sublist) {

                // Insertar los elementos en orden inverso
                // para conservar su orden de aparición.
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
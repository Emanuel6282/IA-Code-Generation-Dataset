import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

public class Solution {

    /**
     * Java 8 - Paradigma Iterativo Estricto
     * Aplica un algoritmo iterativo basado en una pila (Deque) para desanidar
     * cualquier lista sin hacer uso de recursión, lambdas o la API de Streams.
     */
    public static List<Integer> flatten(List<?> nestedList) {
        List<Integer> result = new ArrayList<>();
        if (nestedList == null || nestedList.isEmpty()) {
            return result;
        }

        Deque<Object> stack = new ArrayDeque<>();
        stack.push(nestedList);

        while (!stack.isEmpty()) {
            Object current = stack.pop();

            if (current instanceof List) {
                List<?> list = (List<?>) current;
                // Se insertan los elementos en orden inverso para que al desapilar (pop)
                // se procesen en el orden original de izquierda a derecha.
                for (int i = list.size() - 1; i >= 0; i--) {
                    stack.push(list.get(i));
                }
            } else if (current instanceof Integer) {
                result.add((Integer) current);
            }
        }

        return result;
    }

    public static void main(String[] args) {
        // --- Casos de prueba usando assert (Java 8) ---

        // Caso 1: Ejemplo del problema con múltiples niveles de anidamiento
        List<?> test1 = Arrays.asList(1, Arrays.asList(2, 3), Arrays.asList(4, Arrays.asList(5, 6)));
        assert flatten(test1).equals(Arrays.asList(1, 2, 3, 4, 5, 6)) : "Falló el Caso 1";

        // Caso 2: Lista vacía y estructuras con sublistas vacías
        List<?> test2a = new ArrayList<>();
        List<?> test2b = Arrays.asList(Arrays.asList(), Arrays.asList(Arrays.asList()), Arrays.asList(1, Arrays.asList()), Arrays.asList(Arrays.asList(2)), Arrays.asList());
        assert flatten(test2a).equals(new ArrayList<Integer>()) : "Falló el Caso 2a (Lista vacía)";
        assert flatten(test2b).equals(Arrays.asList(1, 2)) : "Falló el Caso 2b (Sublistas vacías)";

        // Caso 3: Lista totalmente plana y lista profundamente anidada
        List<?> test3a = Arrays.asList(10, 20, 30);
        List<?> test3b = Arrays.asList(Arrays.asList(Arrays.asList(Arrays.asList(Arrays.asList(99)))));
        assert flatten(test3a).equals(Arrays.asList(10, 20, 30)) : "Falló el Caso 3a";
        assert flatten(test3b).equals(Arrays.asList(99)) : "Falló el Caso 3b";

        System.out.println("¡Todos los casos de prueba pasaron correctamente!");
    }
}
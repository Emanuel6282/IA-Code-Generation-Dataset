import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Solution {

    /**
     * Java 17 - Paradigma Iterativo Estricto
     * Utiliza Pattern Matching para instanceof (LTS Java 16+) y procesamiento
     * basado en una pila (Deque) sin usar recursión, lambdas ni la API de Streams.
     */
    public static List<Integer> flatten(List<?> nestedList) {
        var result = new ArrayList<Integer>();
        if (nestedList == null || nestedList.isEmpty()) {
            return result;
        }

        Deque<Object> stack = new ArrayDeque<>();
        stack.push(nestedList);

        while (!stack.isEmpty()) {
            Object current = stack.pop();

            // Pattern Matching para instanceof (característica de Java 16+)
            if (current instanceof List<?> list) {
                // Se apilan los elementos en orden inverso para procesar
                // la lista de izquierda a derecha al hacer pop()
                for (int i = list.size() - 1; i >= 0; i--) {
                    stack.push(list.get(i));
                }
            } else if (current instanceof Integer val) {
                result.add(val);
            }
        }

        return result;
    }

    public static void main(String[] args) {
        // --- Casos de prueba usando assert (Java 17) ---

        // Caso 1: Ejemplo provisto con múltiples niveles de anidamiento
        List<?> test1 = List.of(1, List.of(2, 3), List.of(4, List.of(5, 6)));
        assert flatten(test1).equals(List.of(1, 2, 3, 4, 5, 6)) : "Falló el Caso 1";

        // Caso 2: Lista vacía y estructuras con sublistas vacías intercaladas
        List<?> test2a = List.of();
        List<?> test2b = List.of(List.of(), List.of(List.of()), List.of(1, List.of()), List.of(List.of(2)), List.of());
        assert flatten(test2a).equals(List.of()) : "Falló el Caso 2a (Lista vacía)";
        assert flatten(test2b).equals(List.of(1, 2)) : "Falló el Caso 2b (Sublistas vacías)";

        // Caso 3: Lista totalmente plana y lista profundamente anidada
        List<?> test3a = List.of(10, 20, 30);
        List<?> test3b = List.of(List.of(List.of(List.of(List.of(99)))));
        assert flatten(test3a).equals(List.of(10, 20, 30)) : "Falló el Caso 3a (Lista ya plana)";
        assert flatten(test3b).equals(List.of(99)) : "Falló el Caso 3b (Profundamente anidada)";

        System.out.println("¡Todos los casos de prueba pasaron correctamente!");
    }
}
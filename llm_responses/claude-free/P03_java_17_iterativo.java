import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

public class Flatten {

    /**
     * Dada una lista arbitrariamente anidada de enteros, retorna una lista
     * plana con todos los enteros en el mismo orden de aparicion.
     *
     * Implementacion estrictamente iterativa: usa una pila explicita
     * (ArrayDeque) para simular manualmente el descenso por los niveles de
     * anidamiento, en lugar de recursion. No se usan Streams ni lambdas.
     * Compatible con Java 17.
     *
     * Se modela la entrada como List<Object>, ya que Java no tiene una
     * forma nativa de expresar "lista de enteros o listas anidadas" con
     * tipado estatico simple; cada elemento se inspecciona con
     * pattern matching for instanceof (JEP 394, Java 16+).
     *
     * @param nested Lista que puede contener Integer y/o otras List<Object>
     *               anidadas a cualquier profundidad.
     * @return Una List<Integer> plana con todos los enteros en el mismo
     *         orden de aparicion. Si la lista de entrada esta vacia,
     *         retorna una lista vacia.
     */
    public static List<Integer> flatten(List<Object> nested) {
        List<Integer> result = new ArrayList<>();
        Deque<Object> stack = new ArrayDeque<>();

        // Se insertan los elementos en orden inverso para que, al usar la
        // pila (push/pop, LIFO), se procesen en el orden original.
        for (var i = nested.size() - 1; i >= 0; i--) {
            stack.push(nested.get(i));
        }

        while (!stack.isEmpty()) {
            var item = stack.pop();

            if (item instanceof List<?> subLista) {
                // Pattern matching for instanceof (JEP 394): el cast y la
                // asignacion a "subLista" ocurren en la misma condicion,
                // sin necesidad de un cast explicito por separado.
                for (var i = subLista.size() - 1; i >= 0; i--) {
                    stack.push(subLista.get(i));
                }
            } else if (item instanceof Integer numero) {
                result.add(numero);
            }
        }

        return result;
    }

    public static void main(String[] args) {
        // Caso 1: ejemplo del enunciado, anidamiento de varios niveles
        var caso1 = Arrays.asList(
                1,
                Arrays.asList(2, 3),
                Arrays.asList(4, Arrays.asList(5, 6))
        );
        assert flatten(caso1).equals(Arrays.asList(1, 2, 3, 4, 5, 6)) : "Caso 1 fallido";

        // Caso 2: lista vacia -> lista vacia
        var caso2 = Collections.emptyList();
        assert flatten((List<Object>) caso2).isEmpty() : "Caso 2 fallido";

        // Caso 3: lista plana sin anidamiento
        var caso3 = Arrays.asList(1, 2, 3);
        assert flatten(caso3).equals(Arrays.asList(1, 2, 3)) : "Caso 3 fallido";

        System.out.println("Todos los casos de prueba pasaron correctamente.");
    }
}
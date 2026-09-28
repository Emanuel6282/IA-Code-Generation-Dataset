import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.ArrayList;
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
     * Compatible con Java 21.
     *
     * Se modela la entrada como List<Object>; cada elemento se inspecciona
     * con pattern matching for switch (JEP 441, estandar desde Java 21),
     * que permite expresar la bifurcacion "es lista / es entero / otro"
     * de forma mas declarativa que una cadena if/else if.
     *
     * @param nested Lista que puede contener Integer y/o otras List<Object>
     *               anidadas a cualquier profundidad.
     * @return Una List<Integer> plana con todos los enteros en el mismo
     *         orden de aparicion. Si la lista de entrada esta vacia,
     *         retorna una lista vacia.
     */
    public static List<Integer> flatten(List<Object> nested) {
        var result = new ArrayList<Integer>();
        Deque<Object> stack = new ArrayDeque<>();

        // Se insertan los elementos en orden inverso para que, al usar la
        // pila (push/pop, LIFO), se procesen en el orden original.
        for (var i = nested.size() - 1; i >= 0; i--) {
            stack.push(nested.get(i));
        }

        while (!stack.isEmpty()) {
            var item = stack.pop();

            // Pattern matching for switch (JEP 441): cada "case" hace el
            // chequeo de tipo y el binding de la variable en un solo paso,
            // de forma mas declarativa que instanceof + if/else if.
            switch (item) {
                case List<?> subLista -> {
                    for (var i = subLista.size() - 1; i >= 0; i--) {
                        stack.push(subLista.get(i));
                    }
                }
                case Integer numero -> result.add(numero);
                default -> {
                    // Cualquier otro tipo se ignora silenciosamente, ya
                    // que el contrato de la funcion asume una lista de
                    // enteros anidados.
                }
            }
        }

        return result;
    }

    public static void main(String[] args) {
        // Caso 1: ejemplo del enunciado, anidamiento de varios niveles
        var caso1 = Arrays.<Object>asList(
                1,
                Arrays.asList(2, 3),
                Arrays.asList(4, Arrays.asList(5, 6))
        );
        assert flatten(caso1).equals(Arrays.asList(1, 2, 3, 4, 5, 6)) : "Caso 1 fallido";

        // Caso 2: lista vacia -> lista vacia
        List<Object> caso2 = Collections.emptyList();
        assert flatten(caso2).isEmpty() : "Caso 2 fallido";

        // Caso 3: lista plana sin anidamiento
        var caso3 = Arrays.<Object>asList(1, 2, 3);
        assert flatten(caso3).equals(Arrays.asList(1, 2, 3)) : "Caso 3 fallido";

        System.out.println("Todos los casos de prueba pasaron correctamente.");
    }
}
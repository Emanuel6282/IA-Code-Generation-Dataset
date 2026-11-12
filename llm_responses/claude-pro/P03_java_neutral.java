import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

public class Flatten {

    /**
     * Retorna una lista plana con todos los enteros de {@code nested}, en el mismo orden.
     * Cada elemento debe ser un {@link Integer} o, a su vez, una {@link List} anidada.
     *
     * <p>Usa una pila explícita de iteradores en lugar de recursión, así que funciona con
     * cualquier nivel de anidamiento sin provocar {@code StackOverflowError}.
     *
     * @throws IllegalArgumentException si algún elemento no es Integer ni List
     */
    public static List<Integer> flatten(List<?> nested) {
        List<Integer> result = new ArrayList<>();
        Deque<Iterator<?>> stack = new ArrayDeque<>();
        stack.push(nested.iterator());
        while (!stack.isEmpty()) {
            Iterator<?> current = stack.peek();
            if (!current.hasNext()) {
                stack.pop();                       // el nivel actual se agotó: vuelve al anterior
                continue;
            }
            Object item = current.next();
            if (item instanceof List) {
                stack.push(((List<?>) item).iterator());  // baja un nivel; este se retoma después
            } else if (item instanceof Integer) {
                result.add((Integer) item);
            } else {
                throw new IllegalArgumentException("Elemento no válido: " + item);
            }
        }
        return result;
    }

    // Ejecutar con assertions habilitadas: java -ea Flatten.java
    public static void main(String[] args) {
        boolean assertionsEnabled = false;
        assert assertionsEnabled = true; // solo se asigna si -ea está activo
        if (!assertionsEnabled) {
            System.err.println("Advertencia: ejecuta con 'java -ea' para que los asserts se evalúen.");
        }

        // Caso 1: ejemplo del enunciado
        assert flatten(List.of(1, List.of(2, 3), List.of(4, List.of(5, 6))))
                .equals(List.of(1, 2, 3, 4, 5, 6));

        // Caso 2: lista vacía y sublistas vacías anidadas
        assert flatten(List.of()).isEmpty();
        assert flatten(List.of(List.of(), List.of(List.of()))).isEmpty();

        // Caso 3: anidamiento muy profundo (una versión recursiva desbordaría la pila)
        List<?> deep = List.of(7);
        for (int i = 0; i < 100_000; i++) {
            deep = List.of(deep);
        }
        assert flatten(List.of(0, deep, 8)).equals(List.of(0, 7, 8));

        System.out.println("Todas las pruebas pasaron.");
    }
}
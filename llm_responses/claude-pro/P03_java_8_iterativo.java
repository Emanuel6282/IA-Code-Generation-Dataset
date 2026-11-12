import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

    /** Caso de prueba (Java 8 no tiene records: clase inmutable con campos final). */
    private static final class TestCase {
        final String name;
        final List<?> nested;
        final List<Integer> expected;

        TestCase(String name, List<?> nested, List<Integer> expected) {
            this.name = name;
            this.nested = nested;
            this.expected = expected;
        }
    }

    // Compilar y ejecutar con assertions habilitadas:
    //   javac Flatten.java && java -ea Flatten
    public static void main(String[] args) {
        boolean assertionsEnabled = false;
        assert assertionsEnabled = true; // solo se asigna si -ea está activo
        if (!assertionsEnabled) {
            System.err.println("Advertencia: ejecuta con 'java -ea' para que los asserts se evalúen.");
        }

        // Anidamiento muy profundo (una versión recursiva desbordaría la pila).
        List<?> deep = Collections.singletonList(7);
        for (int i = 0; i < 100_000; i++) {
            deep = Collections.singletonList(deep);
        }

        List<TestCase> testCases = new ArrayList<>();
        // Caso 1: ejemplo del enunciado
        testCases.add(new TestCase("ejemplo del enunciado",
                Arrays.asList(1, Arrays.asList(2, 3), Arrays.asList(4, Arrays.asList(5, 6))),
                Arrays.asList(1, 2, 3, 4, 5, 6)));
        // Caso 2: lista vacía y sublistas vacías anidadas
        testCases.add(new TestCase("listas vacías",
                Arrays.asList(Collections.emptyList(), Arrays.asList(Collections.emptyList())),
                Collections.<Integer>emptyList()));
        // Caso 3: anidamiento muy profundo
        testCases.add(new TestCase("anidamiento profundo",
                Arrays.asList(0, deep, 8), Arrays.asList(0, 7, 8)));

        assert flatten(Collections.emptyList()).isEmpty() : "lista vacía -> lista vacía";

        // Se ejecutan todos los casos y se acumulan los fallos para reportarlos juntos.
        List<String> failures = new ArrayList<>();
        for (TestCase testCase : testCases) {
            List<Integer> result = flatten(testCase.nested);
            if (!result.equals(testCase.expected)) {
                failures.add("Caso '" + testCase.name + "': esperado " + testCase.expected
                        + ", obtenido " + result);
            }
        }

        // String.join (nuevo en Java 8): une los mensajes de fallo sin bucle manual.
        assert failures.isEmpty() : String.join("\n", failures);
        System.out.println("Todas las pruebas pasaron.");
    }
}
import java.util.ArrayDeque;
import java.util.ArrayList;
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
        var result = new ArrayList<Integer>();
        var stack = new ArrayDeque<Iterator<?>>();
        stack.push(nested.iterator());
        while (!stack.isEmpty()) {
            var current = stack.peek();
            if (!current.hasNext()) {
                stack.pop();                     // el nivel actual se agotó: vuelve al anterior
                continue;
            }
            var item = current.next();
            // Pattern matching para instanceof (JEP 394, estándar desde Java 16): comprueba
            // el tipo y declara la variable ya convertida, sin casts explícitos.
            if (item instanceof List<?> sublist) {
                stack.push(sublist.iterator());  // baja un nivel; este se retoma después
            } else if (item instanceof Integer number) {
                result.add(number);
            } else {
                throw new IllegalArgumentException("Elemento no válido: " + item);
            }
        }
        return result;
    }

    /** Caso de prueba como record (estándar desde Java 16): inmutable y sin boilerplate. */
    record TestCase(String name, List<?> nested, List<Integer> expected) {}

    // Ejecutar con assertions habilitadas: java -ea Flatten.java
    public static void main(String[] args) {
        var assertionsEnabled = false;
        assert assertionsEnabled = true; // solo se asigna si -ea está activo
        if (!assertionsEnabled) {
            System.err.println("Advertencia: ejecuta con 'java -ea' para que los asserts se evalúen.");
        }

        // Anidamiento muy profundo (una versión recursiva desbordaría la pila).
        List<?> deep = List.of(7);
        for (var i = 0; i < 100_000; i++) {
            deep = List.of(deep);
        }

        var testCases = new ArrayList<TestCase>();
        // Caso 1: ejemplo del enunciado
        testCases.add(new TestCase("ejemplo del enunciado",
                List.of(1, List.of(2, 3), List.of(4, List.of(5, 6))), List.of(1, 2, 3, 4, 5, 6)));
        // Caso 2: lista vacía y sublistas vacías anidadas
        testCases.add(new TestCase("listas vacías", List.of(List.of(), List.of(List.of())), List.of()));
        // Caso 3: anidamiento muy profundo
        testCases.add(new TestCase("anidamiento profundo", List.of(0, deep, 8), List.of(0, 7, 8)));

        assert flatten(List.of()).isEmpty() : "lista vacía -> lista vacía";

        // Se ejecutan todos los casos y se acumulan los fallos para reportarlos juntos.
        var failures = new ArrayList<String>();
        for (var testCase : testCases) {
            var result = flatten(testCase.nested());
            if (!result.equals(testCase.expected())) {
                // String.formatted (Java 15): formato legible sin String.format estático.
                failures.add("Caso '%s': esperado %s, obtenido %s"
                        .formatted(testCase.name(), testCase.expected(), result));
            }
        }

        assert failures.isEmpty() : String.join("\n", failures);
        System.out.println("Todas las pruebas pasaron.");
    }
}
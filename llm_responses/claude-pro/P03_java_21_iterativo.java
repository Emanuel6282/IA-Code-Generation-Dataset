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
     * @throws IllegalArgumentException si algún elemento no es Integer ni List (o es null)
     */
    public static List<Integer> flatten(List<?> nested) {
        var result = new ArrayList<Integer>();
        // Sequenced collections (JEP 431, nuevo en Java 21): ArrayList gana addLast,
        // getLast y removeLast, así que funciona como pila sin cálculos de índices.
        var stack = new ArrayList<Iterator<?>>();
        stack.addLast(nested.iterator());
        while (!stack.isEmpty()) {
            var current = stack.getLast();
            if (!current.hasNext()) {
                stack.removeLast();              // el nivel actual se agotó: vuelve al anterior
                continue;
            }
            // Pattern matching en switch (JEP 441, estándar en Java 21): elige la rama por
            // tipo y declara la variable ya convertida. `case null` (también nuevo en 21)
            // trata el null explícitamente en lugar de lanzar NullPointerException.
            switch (current.next()) {
                case List<?> sublist -> stack.addLast(sublist.iterator());  // baja un nivel
                case Integer number -> result.add(number);
                case null, default -> throw new IllegalArgumentException("Elemento no válido");
            }
        }
        return result;
    }

    /** Caso de prueba como record: inmutable y sin boilerplate. */
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
            // Record pattern en switch (JEP 440, estándar en Java 21): desestructura el
            // record en sus componentes sin llamar a los getters.
            switch (testCase) {
                case TestCase(var name, var nested, var expected) -> {
                    var result = flatten(nested);
                    if (!result.equals(expected)) {
                        failures.add("Caso '%s': esperado %s, obtenido %s".formatted(name, expected, result));
                    }
                }
            }
        }

        // getFirst() (sequenced collections) en lugar de failures.get(0); solo se
        // evalúa si el assert falla.
        assert failures.isEmpty()
                : "%d caso(s) fallido(s); primero: %s".formatted(failures.size(), failures.getFirst());
        System.out.println("Todas las pruebas pasaron.");
    }
}
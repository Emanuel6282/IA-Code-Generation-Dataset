import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class Flatten {

    /**
     * Lista enlazada inmutable como jerarquía sellada de records: vacía ({@code Nil}) o un
     * nodo con cabeza y resto ({@code Node}). Añadir al principio es O(1) y no copia nada.
     * Al ser {@code sealed}, el compilador conoce todos los casos y verifica que cada
     * switch los cubra.
     */
    sealed interface ConsList<T> permits Nil, Node {}

    record Nil<T>() implements ConsList<T> {}

    record Node<T>(T head, ConsList<T> tail) implements ConsList<T> {}

    /** Recorre una lista enlazada de forma perezosa, del primer nodo al último. */
    private static <T> Stream<T> values(ConsList<T> list) {
        // Patrones de record en instanceof (JEP 440, Java 21): desestructuran el nodo
        // y extraen sus componentes sin casts.
        return Stream.iterate(list, node -> node instanceof Node<T>, node -> node instanceof Node<T>(var head, var tail) ? tail : node)
                .flatMap(node -> node instanceof Node<T>(var head, var tail) ? Stream.of(head) : Stream.empty());
    }

    /** Devuelve una pila nueva con los elementos de {@code items} delante de {@code stack}. */
    private static ConsList<Object> pushAll(List<?> items, ConsList<Object> stack) {
        // List.reversed() (sequenced collections, JEP 431, Java 21): vista en orden
        // inverso, sin copiar ni modificar la lista original. Se recorre de atrás hacia
        // delante para que el primer elemento quede en la cima de la pila.
        return items.reversed().stream()
                .reduce(stack, (acc, item) -> new Node<>(item, acc),
                        (a, b) -> { throw new UnsupportedOperationException("solo secuencial"); });
    }

    /** Estado inmutable del recorrido como record: pendientes y salida en orden inverso. */
    private record State(ConsList<Object> pending, ConsList<Integer> out) {

        boolean done() {
            return pending instanceof Nil<Object>;
        }

        /**
         * Procesa el primer pendiente y devuelve un estado nuevo, sin modificar el actual.
         * Pattern matching en switch con patrones de record anidados (JEP 440 + JEP 441,
         * estándar en Java 21): cada rama desestructura el nodo y comprueba a la vez el
         * tipo de su cabeza. El compilador verifica que el switch sea exhaustivo.
         */
        State step() {
            return switch (pending) {
                case Nil<Object>() -> this;                                       // ya terminó
                case Node<Object>(List<?> sublist, var rest) -> new State(pushAll(sublist, rest), out);
                case Node<Object>(Integer number, var rest) -> new State(rest, new Node<>(number, out));
                case Node<Object>(var other, var rest) ->
                        throw new IllegalArgumentException("Elemento no válido: " + other);
            };
        }

        /** Convierte la salida en una lista inmutable en el orden original. */
        List<Integer> toList() {
            // La salida se acumuló al revés; List.reversed() (Java 21) la devuelve en
            // orden, como vista inmutable de la lista recién creada.
            return values(out).toList().reversed();
        }
    }

    /**
     * Retorna una lista plana e inmutable con todos los enteros de {@code nested}, en el
     * mismo orden. Cada elemento debe ser un {@link Integer} o una {@link List} anidada.
     * Si {@code nested} es null, retorna una lista vacía.
     *
     * <p>No usa recursión: {@code Stream.iterate} genera la secuencia perezosa de estados,
     * {@code dropWhile} descarta los intermedios y {@code findFirst} toma el final. Así
     * funciona con cualquier nivel de anidamiento sin provocar {@code StackOverflowError}.
     *
     * @throws IllegalArgumentException si algún elemento no es Integer ni List (o es null)
     */
    public static List<Integer> flatten(List<?> nested) {
        return Optional.ofNullable(nested)                      // Optional: entrada nula -> vacía
                .flatMap(list -> Stream.iterate(new State(pushAll(list, new Nil<>()), new Nil<>()), State::step)
                        .dropWhile(state -> !state.done())
                        .findFirst())                           // Optional<State> con el estado final
                .map(State::toList)
                .orElseGet(List::of);
    }

    /** Caso de prueba como record. */
    record TestCase(String name, List<?> nested, List<Integer> expected) {}

    /** Devuelve el mensaje de fallo de un caso, o Optional.empty() si pasa. */
    static Optional<String> failure(TestCase testCase) {
        return switch (testCase) {
            case TestCase(var name, var nested, var expected) -> Optional.of(flatten(nested))
                    .filter(actual -> !actual.equals(expected))
                    .map(actual -> "Caso '%s': esperado %s, obtenido %s".formatted(name, expected, actual));
        };
    }

    // Ejecutar con assertions habilitadas: java -ea Flatten.java
    public static void main(String[] args) {
        var assertionsEnabled = false;
        assert assertionsEnabled = true; // solo se asigna si -ea está activo
        if (!assertionsEnabled) {
            System.err.println("Advertencia: ejecuta con 'java -ea' para que los asserts se evalúen.");
        }

        // Anidamiento muy profundo (una versión recursiva desbordaría la pila),
        // construido con Stream.iterate en lugar de un bucle.
        Object deep = Stream.iterate((Object) List.of(7), acc -> List.of(acc)).skip(100_000).findFirst().orElseThrow();

        assert flatten(List.of()).isEmpty() : "lista vacía -> lista vacía";

        var failures = Stream.of(
                        // Caso 1: ejemplo del enunciado
                        new TestCase("ejemplo del enunciado",
                                List.of(1, List.of(2, 3), List.of(4, List.of(5, 6))), List.of(1, 2, 3, 4, 5, 6)),
                        // Caso 2: lista vacía y sublistas vacías anidadas
                        new TestCase("listas vacías", List.of(List.of(), List.of(List.of())), List.of()),
                        // Caso 3: anidamiento muy profundo
                        new TestCase("anidamiento profundo", List.of(0, deep, 8), List.of(0, 7, 8)))
                .map(Flatten::failure)
                .flatMap(Optional::stream)   // descarta los casos que pasan
                .toList();                   // lista inmutable

        // getFirst() (sequenced collections, Java 21) sobre los fallos; solo se evalúa
        // si el assert falla.
        assert failures.isEmpty()
                : "%d caso(s) fallido(s); primero: %s".formatted(failures.size(), failures.getFirst());
        System.out.println("Todas las pruebas pasaron.");
    }
}
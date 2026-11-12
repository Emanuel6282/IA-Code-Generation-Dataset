import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.RandomAccess;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Flatten {

    /** Lista enlazada inmutable como record: añadir al principio es O(1) y no copia nada. */
    private record Cons<T>(T head, Cons<T> tail) {}   // tail == null marca el fin

    /** Recorre una lista enlazada de forma perezosa, del primer nodo al último. */
    private static <T> Stream<T> values(Cons<T> list) {
        // Stream.iterate con condición de parada (Java 9): termina al llegar a null.
        return Stream.iterate(list, Objects::nonNull, Cons::tail).map(Cons::head);
    }

    /** Devuelve una pila nueva con los elementos de {@code items} delante de {@code stack}. */
    private static Cons<Object> pushAll(List<?> items, Cons<Object> stack) {
        // Acceso por índice en O(1); si la lista no lo admite (p. ej. LinkedList),
        // se trabaja sobre una copia local para no pagar O(n) por cada get().
        List<?> list = items instanceof RandomAccess ? items : new ArrayList<>(items);
        var n = list.size();
        // Se recorre de atrás hacia delante para que el primer elemento quede en la cima.
        return IntStream.range(0, n)
                .mapToObj(i -> (Object) list.get(n - 1 - i))
                .reduce(stack, (acc, item) -> new Cons<>(item, acc),
                        (a, b) -> { throw new UnsupportedOperationException("solo secuencial"); });
    }

    /** Estado inmutable del recorrido como record: pendientes y salida en orden inverso. */
    private record State(Cons<Object> pending, Cons<Integer> out) {

        boolean done() {
            return pending == null;
        }

        /** Procesa el primer pendiente y devuelve un estado nuevo, sin modificar el actual. */
        State step() {
            // Pattern matching para instanceof (Java 16): comprueba el tipo y declara
            // la variable ya convertida, sin casts explícitos.
            if (pending.head() instanceof List<?> sublist) {      // sublista -> su contenido
                return new State(pushAll(sublist, pending.tail()), out);
            }
            if (pending.head() instanceof Integer number) {       // entero -> a la salida
                return new State(pending.tail(), new Cons<>(number, out));
            }
            throw new IllegalArgumentException("Elemento no válido: " + pending.head());
        }

        /** Convierte la salida en una lista inmutable en el orden original. */
        List<Integer> toList() {
            var reversed = values(out).toList();           // Stream.toList() (Java 16)
            var n = reversed.size();
            return IntStream.range(0, n).mapToObj(i -> reversed.get(n - 1 - i)).toList();
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
     * @throws IllegalArgumentException si algún elemento no es Integer ni List
     */
    public static List<Integer> flatten(List<?> nested) {
        return Optional.ofNullable(nested)                      // Optional: entrada nula -> vacía
                .flatMap(list -> Stream.iterate(new State(pushAll(list, null), null), State::step)
                        .dropWhile(state -> !state.done())     // dropWhile (Java 9)
                        .findFirst())                           // Optional<State> con el estado final
                .map(State::toList)
                .orElseGet(List::of);
    }

    /** Caso de prueba como record. */
    record TestCase(String name, List<?> nested, List<Integer> expected) {

        /** Devuelve el mensaje de fallo, o Optional.empty() si el caso pasa. */
        Optional<String> failure() {
            return Optional.of(flatten(nested))
                    .filter(actual -> !actual.equals(expected))
                    .map(actual -> "Caso '%s': esperado %s, obtenido %s".formatted(name, expected, actual));
        }
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
                .map(TestCase::failure)
                .flatMap(Optional::stream)   // Optional.stream() (Java 9): descarta los que pasan
                .toList();                   // lista inmutable

        assert failures.isEmpty() : String.join("\n", failures);
        System.out.println("Todas las pruebas pasaron.");
    }
}
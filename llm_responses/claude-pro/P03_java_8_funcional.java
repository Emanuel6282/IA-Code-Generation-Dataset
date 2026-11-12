import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.RandomAccess;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Flatten {

    /** Lista enlazada inmutable: añadir al principio es O(1) y no copia nada. */
    private static final class Cons<T> {
        final T head;
        final Cons<T> tail;   // null = fin de la lista

        Cons(T head, Cons<T> tail) {
            this.head = head;
            this.tail = tail;
        }
    }

    /** Devuelve una pila nueva con los elementos de {@code items} delante de {@code stack}. */
    private static Cons<Object> pushAll(List<?> items, Cons<Object> stack) {
        // Acceso por índice en O(1); si la lista no lo admite (p. ej. LinkedList),
        // se trabaja sobre una copia local para no pagar O(n) por cada get().
        List<?> list = items instanceof RandomAccess ? items : new ArrayList<>(items);
        int n = list.size();
        // Se recorre de atrás hacia delante para que el primer elemento quede en la cima.
        return IntStream.range(0, n)
                .mapToObj(i -> (Object) list.get(n - 1 - i))
                .reduce(stack, (acc, item) -> new Cons<>(item, acc),
                        (a, b) -> { throw new UnsupportedOperationException("solo secuencial"); });
    }

    /** Estado inmutable del recorrido: pendientes, salida (en orden inverso) y su tamaño. */
    private static final class State {
        final Cons<Object> pending;
        final Cons<Integer> out;
        final int size;

        State(Cons<Object> pending, Cons<Integer> out, int size) {
            this.pending = pending;
            this.out = out;
            this.size = size;
        }

        boolean done() {
            return pending == null;
        }

        /** Procesa el primer pendiente y devuelve un estado nuevo, sin modificar el actual. */
        State step() {
            Object head = pending.head;
            if (head instanceof List) {                       // sublista -> su contenido
                return new State(pushAll((List<?>) head, pending.tail), out, size);
            }
            if (head instanceof Integer) {                    // entero -> a la salida
                return new State(pending.tail, new Cons<>((Integer) head, out), size + 1);
            }
            throw new IllegalArgumentException("Elemento no válido: " + head);
        }

        /** Convierte la salida en una lista inmutable en el orden original. */
        List<Integer> toList() {
            // Java 8 no tiene Stream.takeWhile: se usa limit con el tamaño conocido.
            List<Integer> reversed = Stream.iterate(out, node -> node.tail)
                    .limit(size)
                    .map(node -> node.head)
                    .collect(Collectors.toList());
            return IntStream.range(0, size)
                    .mapToObj(i -> reversed.get(size - 1 - i))
                    .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
        }
    }

    /**
     * Retorna una lista plana e inmutable con todos los enteros de {@code nested}, en el
     * mismo orden. Cada elemento debe ser un {@link Integer} o una {@link List} anidada.
     * Si {@code nested} es null, retorna una lista vacía.
     *
     * <p>No usa recursión: {@code Stream.iterate} genera la secuencia perezosa de estados
     * y {@code findFirst} se detiene en el primero sin pendientes. Así funciona con
     * cualquier nivel de anidamiento sin provocar {@code StackOverflowError}.
     *
     * @throws IllegalArgumentException si algún elemento no es Integer ni List
     */
    public static List<Integer> flatten(List<?> nested) {
        return Optional.ofNullable(nested)                     // Optional: entrada nula -> vacía
                .flatMap(list -> Stream.iterate(new State(pushAll(list, null), null, 0), State::step)
                        .filter(State::done)
                        .findFirst())                          // Optional<State> con el estado final
                .map(State::toList)
                .orElseGet(Collections::emptyList);
    }

    /** Devuelve el mensaje de fallo de un caso, o Optional.empty() si pasa. */
    private static Optional<String> failure(String name, List<?> nested, List<Integer> expected) {
        List<Integer> result = flatten(nested);
        return Optional.of(result)
                .filter(actual -> !actual.equals(expected))
                .map(actual -> "Caso '" + name + "': esperado " + expected + ", obtenido " + actual);
    }

    // Compilar y ejecutar con assertions habilitadas:
    //   javac Flatten.java && java -ea Flatten
    public static void main(String[] args) {
        boolean assertionsEnabled = false;
        assert assertionsEnabled = true; // solo se asigna si -ea está activo
        if (!assertionsEnabled) {
            System.err.println("Advertencia: ejecuta con 'java -ea' para que los asserts se evalúen.");
        }

        // Anidamiento muy profundo (una versión recursiva desbordaría la pila),
        // construido con reduce en lugar de un bucle.
        Object deep = IntStream.range(0, 100_000).boxed()
                .reduce((Object) Collections.singletonList(7), (acc, i) -> Collections.singletonList(acc),
                        (a, b) -> { throw new UnsupportedOperationException("solo secuencial"); });

        assert flatten(Collections.emptyList()).isEmpty() : "lista vacía -> lista vacía";

        List<String> failures = Stream.of(
                        // Caso 1: ejemplo del enunciado
                        failure("ejemplo del enunciado",
                                Arrays.asList(1, Arrays.asList(2, 3), Arrays.asList(4, Arrays.asList(5, 6))),
                                Arrays.asList(1, 2, 3, 4, 5, 6)),
                        // Caso 2: lista vacía y sublistas vacías anidadas
                        failure("listas vacías",
                                Arrays.asList(Collections.emptyList(), Arrays.asList(Collections.emptyList())),
                                Collections.<Integer>emptyList()),
                        // Caso 3: anidamiento muy profundo
                        failure("anidamiento profundo", Arrays.asList(0, deep, 8), Arrays.asList(0, 7, 8)))
                // Java 8 no tiene Optional.stream(): se convierte cada Optional en un
                // stream de 0 o 1 elementos para descartar los casos que pasan.
                .flatMap(opt -> opt.map(Stream::of).orElseGet(Stream::empty))
                .collect(Collectors.toList());

        assert failures.isEmpty() : String.join("\n", failures);
        System.out.println("Todas las pruebas pasaron.");
    }
}
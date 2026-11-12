import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

public class BinarySearch {

    /** Estado inmutable de la búsqueda como record (Java 16): rango [low, high] e índice encontrado. */
    record SearchState(int low, int high, int found) {

        boolean done() {
            return found != -1 || low > high;
        }

        /** Devuelve el siguiente estado sin modificar el actual. */
        SearchState narrow(int[] arr, int target) {
            if (done()) {
                return this;
            }
            int mid = low + (high - low) / 2;   // evita desbordamiento de int
            // switch como expresión (Java 14): cada rama produce el nuevo estado.
            return switch (Integer.signum(Integer.compare(arr[mid], target))) {
                case 0 -> new SearchState(low, high, mid);       // encontrado
                case -1 -> new SearchState(mid + 1, high, -1);   // está a la derecha
                default -> new SearchState(low, mid - 1, -1);    // está a la izquierda
            };
        }
    }

    /**
     * Busca target en arr (ordenado ascendentemente) y retorna su índice, o -1 si no está.
     */
    public static int binarySearch(int[] arr, int target) {
        // Stream.iterate genera la secuencia perezosa de estados; dropWhile (Java 9)
        // descarta los estados intermedios y findFirst toma el primero terminado. La
        // búsqueda termina siempre en ⌊log2 n⌋ + 1 pasos como mucho, así que el
        // stream infinito nunca se recorre más allá de eso.
        return Stream.iterate(new SearchState(0, arr.length - 1, -1), state -> state.narrow(arr, target))
                .dropWhile(state -> !state.done())
                .findFirst()
                .map(SearchState::found)
                .orElse(-1);
    }

    /** Caso de prueba como record. */
    record TestCase(String name, int[] arr, int target, int expected) {

        /** Devuelve el mensaje de fallo, o Optional.empty() si el caso pasa. */
        Optional<String> failure() {
            return Optional.of(binarySearch(arr, target))
                    .filter(actual -> actual != expected)
                    .map(actual -> "Caso '%s': arr=%s, target=%d, esperado %d, obtenido %d"
                            .formatted(name, Arrays.toString(arr), target, expected, actual));
        }
    }

    // Ejecutar con assertions habilitadas: java -ea BinarySearch.java
    public static void main(String[] args) {
        var assertionsEnabled = false;
        assert assertionsEnabled = true; // solo se asigna si -ea está activo
        if (!assertionsEnabled) {
            System.err.println("Advertencia: ejecuta con 'java -ea' para que los asserts se evalúen.");
        }

        var failures = Stream.of(
                        // Caso 1: el objetivo está en el arreglo (ejemplo del enunciado)
                        new TestCase("objetivo presente", new int[] {1, 3, 5, 7, 9}, 5, 2),
                        // Caso 2: el objetivo no está en el arreglo (ejemplo del enunciado)
                        new TestCase("objetivo ausente", new int[] {1, 3, 5, 7, 9}, 4, -1),
                        // Caso 3: arreglo vacío
                        new TestCase("arreglo vacío", new int[] {}, 1, -1))
                .map(TestCase::failure)
                .flatMap(Optional::stream)   // Optional.stream() (Java 9): descarta los que pasan
                .toList();                   // Stream.toList() (Java 16): lista inmutable

        assert failures.isEmpty() : String.join("\n", failures);
        System.out.println("Todas las pruebas pasaron.");
    }
}
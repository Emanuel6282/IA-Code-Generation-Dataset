import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BinarySearch {

    /** Estado inmutable de la búsqueda: rango [low, high] e índice encontrado (-1 si aún no). */
    private static final class SearchState {
        final int low;
        final int high;
        final int found;

        SearchState(int low, int high, int found) {
            this.low = low;
            this.high = high;
            this.found = found;
        }

        boolean done() {
            return found != -1 || low > high;
        }

        /** Devuelve el siguiente estado sin modificar el actual. */
        SearchState narrow(int[] arr, int target) {
            if (done()) {
                return this;
            }
            int mid = low + (high - low) / 2;             // evita desbordamiento de int
            return arr[mid] == target ? new SearchState(low, high, mid)
                 : arr[mid] < target  ? new SearchState(mid + 1, high, -1)   // mitad derecha
                 :                      new SearchState(low, mid - 1, -1);   // mitad izquierda
        }
    }

    /**
     * Busca target en arr (ordenado ascendentemente) y retorna su índice, o -1 si no está.
     */
    public static int binarySearch(int[] arr, int target) {
        // Una búsqueda binaria sobre n elementos termina en como mucho
        // ⌊log2 n⌋ + 1 pasos, que es el número de bits de n.
        int maxSteps = Integer.SIZE - Integer.numberOfLeadingZeros(arr.length);

        // Stream.iterate genera la secuencia perezosa de estados: inicial, tras 1 paso,
        // tras 2... findFirst se detiene en el primer estado terminado, así que solo se
        // calculan los pasos necesarios. Optional modela "puede que no haya resultado".
        return Stream.iterate(new SearchState(0, arr.length - 1, -1), state -> state.narrow(arr, target))
                .limit(maxSteps + 1L)
                .filter(SearchState::done)
                .findFirst()
                .map(state -> state.found)
                .orElse(-1);
    }

    /** Devuelve el mensaje de fallo de un caso, o Optional.empty() si pasa. */
    private static Optional<String> failure(String name, int[] arr, int target, int expected) {
        int result = binarySearch(arr, target);
        return Optional.of(result)
                .filter(actual -> actual != expected)
                .map(actual -> "Caso '" + name + "': arr=" + Arrays.toString(arr) + ", target=" + target
                        + ", esperado " + expected + ", obtenido " + actual);
    }

    // Compilar y ejecutar con assertions habilitadas:
    //   javac BinarySearch.java && java -ea BinarySearch
    public static void main(String[] args) {
        boolean assertionsEnabled = false;
        assert assertionsEnabled = true; // solo se asigna si -ea está activo
        if (!assertionsEnabled) {
            System.err.println("Advertencia: ejecuta con 'java -ea' para que los asserts se evalúen.");
        }

        List<String> failures = Stream.of(
                        // Caso 1: el objetivo está en el arreglo (ejemplo del enunciado)
                        failure("objetivo presente", new int[] {1, 3, 5, 7, 9}, 5, 2),
                        // Caso 2: el objetivo no está en el arreglo (ejemplo del enunciado)
                        failure("objetivo ausente", new int[] {1, 3, 5, 7, 9}, 4, -1),
                        // Caso 3: arreglo vacío
                        failure("arreglo vacío", new int[] {}, 1, -1))
                // Java 8 no tiene Optional.stream(): se convierte cada Optional en un
                // stream de 0 o 1 elementos para descartar los casos que pasan.
                .flatMap(opt -> opt.map(Stream::of).orElseGet(Stream::empty))
                .collect(Collectors.toList());

        assert failures.isEmpty() : String.join("\n", failures);
        System.out.println("Todas las pruebas pasaron.");
    }
}
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

public class BinarySearch {

    /**
     * Estado de la búsqueda como jerarquía sellada de records: o se sigue buscando en un
     * rango, o se encontró el objetivo, o se sabe que no está. Al ser `sealed`, el
     * compilador conoce todos los casos posibles y verifica que cada switch los cubra.
     */
    sealed interface SearchState permits Searching, Found, NotFound {}

    record Searching(int low, int high) implements SearchState {}

    record Found(int index) implements SearchState {}

    record NotFound() implements SearchState {}

    /**
     * Un paso de la búsqueda: devuelve el siguiente estado sin modificar el actual.
     * Usa pattern matching en switch con patrones de record y guardas `when`
     * (JEP 440 + JEP 441, estándar en Java 21).
     */
    static SearchState narrow(SearchState state, int[] arr, int target) {
        return switch (state) {
            case Found found -> found;                                  // ya terminó
            case NotFound notFound -> notFound;                         // ya terminó
            case Searching(var low, var high) when low > high -> new NotFound();
            case Searching(var low, var high) -> {
                int mid = low + (high - low) / 2;                      // evita desbordamiento
                yield arr[mid] == target ? new Found(mid)
                    : arr[mid] < target  ? new Searching(mid + 1, high)   // mitad derecha
                    :                      new Searching(low, mid - 1);   // mitad izquierda
            }
        };
    }

    /**
     * Busca target en arr (ordenado ascendentemente) y retorna su índice, o -1 si no está.
     */
    public static int binarySearch(int[] arr, int target) {
        // Stream.iterate genera la secuencia perezosa de estados; dropWhile descarta los
        // que siguen buscando y findFirst toma el primer estado final. La búsqueda termina
        // siempre en ⌊log2 n⌋ + 2 pasos como mucho, así que el stream infinito no se
        // recorre más allá de eso.
        return Stream.<SearchState>iterate(new Searching(0, arr.length - 1), state -> narrow(state, arr, target))
                .dropWhile(state -> state instanceof Searching)
                .findFirst()
                .map(state -> switch (state) {
                    case Found(var index) -> index;
                    case NotFound() -> -1;
                    case Searching searching ->   // imposible tras dropWhile
                            throw new IllegalStateException("búsqueda sin terminar: " + searching);
                })
                .orElse(-1);
    }

    /** Caso de prueba como record. */
    record TestCase(String name, int[] arr, int target, int expected) {}

    /** Devuelve el mensaje de fallo de un caso, o Optional.empty() si pasa. */
    static Optional<String> failure(TestCase testCase) {
        return switch (testCase) {
            case TestCase(var name, var arr, var target, var expected) -> Optional.of(binarySearch(arr, target))
                    .filter(actual -> actual != expected)
                    .map(actual -> "Caso '%s': arr=%s, target=%d, esperado %d, obtenido %d"
                            .formatted(name, Arrays.toString(arr), target, expected, actual));
        };
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
                .map(BinarySearch::failure)
                .flatMap(Optional::stream)   // descarta los casos que pasan
                .toList();                   // lista inmutable

        // Sequenced collections (JEP 431, nuevo en Java 21): getFirst() sobre los fallos;
        // solo se evalúa si el assert falla.
        assert failures.isEmpty()
                : "%d caso(s) fallido(s); primero: %s".formatted(failures.size(), failures.getFirst());
        System.out.println("Todas las pruebas pasaron.");
    }
}
import java.util.Optional;
import java.util.function.BiFunction;

public class BinarySearchJava21Functional {

    // Envoltorio inmutable de datos para representar el estado de la búsqueda (Java Records)
    private record SearchState(int left, int right) {}

    // Tipos sellados (Sealed Interfaces) y Records para Pattern Matching de Java 21
    private sealed interface SearchResult permits SearchResult.Found, SearchResult.GoLeft, SearchResult.GoRight {}
    private record Found(int index) implements SearchResult {}
    private record GoLeft() implements SearchResult {}
    private record GoRight() implements SearchResult {}

    public static int binarySearch(int[] arr, int target) {
        // Evaluación del elemento medio mediante Pattern Matching exhaustivo (Java 21)
        BiFunction<Integer, SearchState, SearchResult> evaluateMid = (mid, state) -> {
            var diff = Integer.compare(arr[mid], target);
            return switch (diff) {
                case 0 -> new Found(mid);
                case 1 -> new GoLeft();   // arr[mid] > target
                case -1 -> new GoRight(); // arr[mid] < target
                default -> throw new IllegalStateException("Resultado de comparación no válido: " + diff);
            };
        };

        // Función recursiva pura en estilo funcional usando Pattern Matching de Records/Tipos
        BiFunction<SearchState, BiFunction, Optional<Integer>> search = (state, self) -> {
            if (state.left() > state.right()) {
                return Optional.empty();
            }

            var mid = state.left() + (state.right() - state.left()) / 2;

            // Pattern Matching sobre Sealed Interfaces (Java 21)
            return switch (evaluateMid.apply(mid, state)) {
                case Found(var index) -> Optional.of(index);
                case GoLeft() -> (Optional<Integer>) self.apply(new SearchState(state.left(), mid - 1), self);
                case GoRight() -> (Optional<Integer>) self.apply(new SearchState(mid + 1, state.right()), self);
            };
        };

        // Flujo de control funcional usando Optional y composición declarativa
        return Optional.ofNullable(arr)
                .filter(a -> a.length > 0)
                .flatMap(a -> search.apply(new SearchState(0, a.length - 1), search))
                .orElse(-1);
    }

    public static void main(String[] args) {
        // --- Casos de prueba (Java 21) ---

        // Caso 1: Elemento presente en el arreglo (ejemplo provisto)
        assert binarySearch(new int[]{1, 3, 5, 7, 9}, 5) == 2 : "Falló el Caso 1";

        // Caso 2: Elemento no presente en el arreglo (ejemplo provisto)
        assert binarySearch(new int[]{1, 3, 5, 7, 9}, 4) == -1 : "Falló el Caso 2";

        // Caso 3: Arreglo vacío y búsqueda en límites extremos
        assert binarySearch(new int[]{}, 10) == -1 : "Falló el Caso 3a (Arreglo vacío)";
        assert binarySearch(new int[]{10, 20, 30, 40}, 10) == 0 : "Falló el Caso 3b (Primer elemento)";
        assert binarySearch(new int[]{10, 20, 30, 40}, 40) == 3 : "Falló el Caso 3c (Último elemento)";

        System.out.println("¡Todos los casos de prueba pasaron correctamente!");
    }
}
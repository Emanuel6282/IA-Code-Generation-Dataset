import java.util.Optional;
import java.util.function.BiFunction;

public class BinarySearchJava17Functional {

    public static int binarySearch(int[] arr, int target) {
        // Encapsulamiento funcional de un Rango de búsqueda inmutable
        record SearchRange(int left, int right) {}

        // Función recursiva utilizando Expresiones Switch (Java 17) para el flujo de control
        BiFunction<SearchRange, BiFunction, Optional<Integer>> search = (range, self) -> {
            if (range.left() > range.right()) {
                return Optional.empty();
            }

            var mid = range.left() + (range.right() - range.left()) / 2;

            return switch (Integer.compare(arr[mid], target)) {
                case 0 -> Optional.of(mid);
                case -1 -> (Optional<Integer>) self.apply(new SearchRange(mid + 1, range.right()), self);
                case 1 -> (Optional<Integer>) self.apply(new SearchRange(range.left(), mid - 1), self);
                default -> Optional.empty();
            };
        };

        // Procesamiento declarativo con Optional e inferencia de tipos (var)
        return Optional.ofNullable(arr)
                .filter(a -> a.length > 0)
                .flatMap(a -> search.apply(new SearchRange(0, a.length - 1), search))
                .orElse(-1);
    }

    public static void main(String[] args) {
        // --- Casos de prueba (Java 17) ---

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
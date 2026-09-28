import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public class BinarySearchJava8Functional {

    // Paradigma Funcional en Java 8: Implementación usando recursión pura mediante expresiones Lambda
    // y envoltorios Optional para evitar variables mutables o bucles for/while imperativos.
    public static int binarySearch(int[] arr, int target) {
        // Definición de una función de orden superior utilizando la interfaz funcional BiFunction
        BiFunction<Integer, Integer, Optional<Integer>> search = new BiFunction<Integer, Integer, Optional<Integer>>() {
            @Override
            public Optional<Integer> apply(Integer left, Integer right) {
                if (left > right) {
                    return Optional.empty();
                }

                int mid = left + (right - left) / 2;

                if (arr[mid] == target) {
                    return Optional.of(mid);
                } else if (arr[mid] < target) {
                    return this.apply(mid + 1, right);
                } else {
                    return this.apply(left, mid - 1);
                }
            }
        };

        // Evaluación de la búsqueda y extracción funcional utilizando Optional.orElse
        return Optional.ofNullable(arr)
                .filter(a -> a.length > 0)
                .flatMap(a -> search.apply(0, a.length - 1))
                .orElse(-1);
    }

    public static void main(String[] args) {
        // --- Casos de prueba (Java 8) ---

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
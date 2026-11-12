// P02: Binary Search — Java 8
// Paradigma: Funcional
//
// Características:
// - Stream API de Java 8.
// - Expresiones lambda.
// - Optional<Integer>.
// - Recursión sin estado mutable.
// - Sin bucles for/while.
// - Sin Arrays.binarySearch().

import java.util.Optional;
import java.util.stream.IntStream;

public class BinarySearch {

    public static int binarySearch(int[] arr, int target) {

        // Vista funcional de los índices del arreglo.
        // No se modifica el arreglo original.
        Optional<int[]> input = Optional.of(arr);

        return input
            .map(values -> search(values, target, 0, values.length - 1))
            .orElse(-1);
    }

    private static int search(
            int[] arr,
            int target,
            int left,
            int right) {

        // Caso base: intervalo vacío.
        if (left > right) {
            return -1;
        }

        // Índice central.
        final int mid = left + (right - left) / 2;

        // Stream API y lambda para comparar el elemento central.
        Optional<Integer> comparison = IntStream.of(mid)
            .map(index -> Integer.compare(arr[index], target))
            .boxed()
            .findFirst();

        return comparison
            .map(result -> {
                if (result == 0) {
                    return mid;
                }

                if (result < 0) {
                    return search(arr, target, mid + 1, right);
                }

                return search(arr, target, left, mid - 1);
            })
            .orElse(-1);
    }

    public static void main(String[] args) {

        // Test 1: Objetivo encontrado.
        assert binarySearch(new int[]{1, 3, 5, 7, 9}, 5) == 2;

        // Test 2: Objetivo no encontrado.
        assert binarySearch(new int[]{1, 3, 5, 7, 9}, 4) == -1;

        // Test 3: Arreglo vacío.
        assert binarySearch(new int[]{}, 5) == -1;

        System.out.println("All tests passed");
    }
}
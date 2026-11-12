// P02: Binary Search — Java 17
// Paradigma: Funcional
//
// Características:
// - Stream API y expresiones lambda.
// - Optional<Integer>.
// - Recursión sin estado mutable.
// - Expresiones switch modernas.
// - Sin bucles for/while.
// - Sin Arrays.binarySearch().
// - Biblioteca estándar únicamente.

import java.util.Optional;
import java.util.stream.IntStream;

public class BinarySearch {

    public static int binarySearch(int[] arr, int target) {
        return Optional.of(arr)
                .map(values -> search(
                        values,
                        target,
                        0,
                        values.length - 1
                ))
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

        // Índice central inmutable.
        final int mid = left + (right - left) / 2;

        // Comparación mediante Stream API y lambda.
        Optional<Integer> comparison = IntStream.of(mid)
                .map(index -> Integer.compare(
                        arr[index],
                        target
                ))
                .boxed()
                .findFirst();

        // Evaluación funcional del resultado.
        return comparison
                .map(result -> switch (result) {
                    case 0 -> mid;

                    case -1 -> search(
                            arr,
                            target,
                            mid + 1,
                            right
                    );

                    case 1 -> search(
                            arr,
                            target,
                            left,
                            mid - 1
                    );

                    default -> -1;
                })
                .orElse(-1);
    }

    public static void main(String[] args) {

        // Test 1: Objetivo encontrado.
        assert binarySearch(
                new int[]{1, 3, 5, 7, 9}, 5
        ) == 2;

        // Test 2: Objetivo no encontrado.
        assert binarySearch(
                new int[]{1, 3, 5, 7, 9}, 4
        ) == -1;

        // Test 3: Arreglo vacío.
        assert binarySearch(
                new int[]{}, 5
        ) == -1;

        System.out.println("All tests passed");
    }
}
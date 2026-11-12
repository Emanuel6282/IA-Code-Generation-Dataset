// P06: Max Subarray Sum — Java 17
// Paradigma: ESTRICTAMENTE FUNCIONAL
//
// CARACTERÍSTICAS UTILIZADAS:
//
// 1. Records (estables desde Java 16):
//    Permiten representar estados inmutables
//    de forma concisa y segura.
//
// 2. Stream API (Java 8):
//    Procesa los elementos mediante operaciones
//    funcionales, sin bucles imperativos.
//
// 3. Expresiones lambda (Java 8):
//    Definen transformaciones y reducciones.
//
// 4. Optional (Java 8):
//    Representa la presencia o ausencia
//    de un resultado.
//
// 5. IntStream (Java 8):
//    Genera los índices del arreglo.
//
// 6. reduce() (Java 8):
//    Permite acumular estados sin modificar
//    los objetos anteriores.
//
// 7. Inferencia de tipos con var (Java 10):
//    Mejora la legibilidad de variables locales.
//
// NOTA:
// Los records son la característica moderna
// más relevante para esta solución en Java 17.
//
// Biblioteca estándar únicamente.
// Sin for, while, recursión ni estado mutable.

import java.util.Optional;
import java.util.stream.IntStream;

public class P06MaxSubarraySum_v17 {

    // Estado inmutable del algoritmo de Kadane.
    private record State(int currentSum, int maxSum) {}

    // Función pura: recibe un estado y un número,
    // y devuelve un nuevo estado inmutable.
    private static State nextState(State state, int num) {

        int newCurrentSum = Math.max(
            num,
            state.currentSum() + num
        );

        int newMaxSum = Math.max(
            state.maxSum(),
            newCurrentSum
        );

        return new State(newCurrentSum, newMaxSum);
    }

    // Función principal.
    public static int maxSubarraySum(int[] nums) {

        // No se permite un subarreglo vacío.
        if (nums == null || nums.length == 0) {
            throw new IllegalArgumentException(
                "El arreglo no puede estar vacío"
            );
        }

        // Inicialización con el primer elemento.
        var initialState = new State(nums[0], nums[0]);

        // Procesamiento funcional y secuencial.
        var finalState = IntStream.range(1, nums.length)
            .mapToObj(i -> nums[i])
            .reduce(
                initialState,
                (state, num) -> nextState(state, num),
                (left, right) -> {
                    throw new UnsupportedOperationException(
                        "La reducción debe ser secuencial"
                    );
                }
            );

        // Optional permite representar el resultado.
        Optional<Integer> result = Optional.of(
            finalState.maxSum()
        );

        return result.orElseThrow(
            () -> new IllegalStateException(
                "No se pudo calcular la suma máxima"
            )
        );
    }

    public static void main(String[] args) {

        // TEST 1: Ejemplo del enunciado.
        assert maxSubarraySum(
            new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4}
        ) == 6 : "TC01 falló";

        // TEST 2: Todos los elementos negativos.
        assert maxSubarraySum(
            new int[]{-8, -3, -6, -2, -5, -4}
        ) == -2 : "TC02 falló";

        // TEST 3: Todos los elementos positivos.
        assert maxSubarraySum(
            new int[]{1, 2, 3, 4, 5}
        ) == 15 : "TC03 falló";

        System.out.println("All tests passed");
    }
}
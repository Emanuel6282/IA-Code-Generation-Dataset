// P06: Max Subarray Sum — Java 8
// Paradigma: ESTRICTAMENTE FUNCIONAL
//
// CARACTERÍSTICAS UTILIZADAS:
//
// 1. Stream API (Java 8):
//    Procesa los elementos mediante operaciones funcionales.
//
// 2. Expresiones lambda (Java 8):
//    Definen transformaciones sin bucles imperativos.
//
// 3. Optional (Java 8):
//    Representa la presencia o ausencia de un resultado.
//
// 4. IntStream (Java 8):
//    Permite recorrer índices mediante un flujo de enteros.
//
// 5. reduce() (Java 8):
//    Acumula resultados mediante una función de reducción.
//
// 6. Estados inmutables:
//    Cada transición genera un nuevo objeto State.
//
// 7. Referencias a métodos (Java 8):
//    Permiten utilizar operaciones como Optional::get.
//
// NOTA:
// La solución no utiliza variables mutables,
// bucles for/while ni recursión.
//
// Biblioteca estándar únicamente.

import java.util.Optional;
import java.util.stream.IntStream;

public class P06MaxSubarraySum_v8 {

    // Estado inmutable del algoritmo de Kadane.
    private static final class State {

        final int currentSum;
        final int maxSum;

        State(int currentSum, int maxSum) {
            this.currentSum = currentSum;
            this.maxSum = maxSum;
        }
    }

    // Función pura que transforma un estado en otro.
    private static State nextState(State state, int num) {

        int newCurrentSum = Math.max(
            num,
            state.currentSum + num
        );

        int newMaxSum = Math.max(
            state.maxSum,
            newCurrentSum
        );

        return new State(newCurrentSum, newMaxSum);
    }

    // Función principal.
    public static int maxSubarraySum(int[] nums) {

        if (nums == null || nums.length == 0) {
            throw new IllegalArgumentException(
                "El arreglo no puede estar vacío"
            );
        }

        // Estado inicial con el primer elemento.
        final State initialState = new State(
            nums[0],
            nums[0]
        );

        // Procesar los elementos restantes.
        Optional<State> result = Optional.of(
            IntStream.range(1, nums.length)
                .mapToObj(i -> nums[i])
                .reduce(
                    initialState,
                    (state, num) -> nextState(state, num),
                    (left, right) -> {
                        throw new UnsupportedOperationException(
                            "La reducción debe ser secuencial"
                        );
                    }
                )
        );

        // Extraer el resultado de Optional.
        return result.map(state -> state.maxSum)
                     .orElseThrow(
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
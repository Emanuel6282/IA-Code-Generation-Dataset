// P06: Max Subarray Sum — Java 21
// Paradigma: ESTRICTAMENTE FUNCIONAL
//
// CARACTERÍSTICAS UTILIZADAS:
//
// 1. Stream API y lambdas (Java 8):
//    Permiten procesar elementos sin bucles imperativos.
//
// 2. Optional (Java 8):
//    Representa un resultado que puede estar ausente.
//
// 3. Records (Java 16):
//    Representan estados inmutables de forma concisa.
//
// 4. var (Java 10):
//    Permite inferir tipos de variables locales.
//
// 5. Pattern matching para switch (Java 21, JEP 441):
//    Permite validar la entrada mediante patrones.
//    Es una característica estable en Java 21.
//
// 6. Reducción funcional:
//    Cada paso produce un nuevo estado sin modificar
//    el estado anterior.
//
// NOTA:
// Se utiliza un Stream secuencial porque Kadane
// depende del orden de los elementos.
//
// Biblioteca estándar únicamente.
// Sin bucles for/while, recursión ni estado mutable.

import java.util.Optional;
import java.util.stream.IntStream;

public class P06MaxSubarraySum_v21 {

    // Estado inmutable del algoritmo de Kadane.
    private record State(int currentSum, int maxSum) {}

    // Transición funcional: devuelve un nuevo estado.
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

        // Java 21: pattern matching para switch.
        int[] values = switch (nums) {
            case null -> throw new IllegalArgumentException(
                "El arreglo no puede ser null"
            );

            case int[] arr when arr.length == 0 ->
                throw new IllegalArgumentException(
                    "El arreglo no puede estar vacío"
                );

            case int[] arr -> arr;
        };

        // El primer elemento inicializa Kadane.
        var initialState = new State(values[0], values[0]);

        // Procesamiento mediante Stream API y lambdas.
        State finalState = IntStream.range(1, values.length)
            .mapToObj(i -> values[i])
            .sequential()
            .reduce(
                initialState,
                (state, num) -> nextState(state, num),
                (left, right) -> {
                    throw new IllegalStateException(
                        "La reducción no admite combinación paralela"
                    );
                }
            );

        // Optional representa el resultado.
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
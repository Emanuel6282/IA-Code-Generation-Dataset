// Java 8 - Paradigma Funcional Estricto
// Se omite todo bucle imperativo (for/while) en favor de Stream API, lambdas,
// reducción inmutable con IntStream.range() / Stream.reduce() y manejo con Optional.

import java.util.Arrays;
import java.util.Optional;

public class MaxSubarraySumFunctional {

    /**
     * Representa un estado inmutable para acumular la suma local y global (Kadane).
     */
    private static final class State {
        private final int currentMax;
        private final int globalMax;

        public State(int currentMax, int globalMax) {
            this.currentMax = currentMax;
            this.globalMax = globalMax;
        }

        public State accumulate(int num) {
            int newCurrent = Math.max(num, this.currentMax + num);
            int newGlobal = Math.max(this.globalMax, newCurrent);
            return new State(newCurrent, newGlobal);
        }

        public int getGlobalMax() {
            return globalMax;
        }
    }

    /**
     * Calcula la suma máxima de un subarreglo contiguo no vacío (Algoritmo de Kadane)
     * utilizando estrictamente el paradigma funcional con Java 8 Streams y Optional.
     *
     * @param nums Arreglo de enteros (puede contener negativos).
     * @return La suma máxima encontrada.
     * @throws IllegalArgumentException Si el arreglo es nulo o está vacío.
     */
    public static int maxSubarraySum(int[] nums) {
        return Optional.ofNullable(nums)
                .filter(arr -> arr.length > 0)
                .map(arr -> {
                    State initialState = new State(arr[0], arr[0]);
                    
                    // Stream funcional sobre el resto del arreglo (índices 1 a len-1)
                    return Arrays.stream(arr, 1, arr.length)
                            .boxed()
                            .reduce(initialState,
                                    State::accumulate,
                                    (s1, s2) -> s2) // Combinador para streams (mantiene el último estado acumulado)
                            .getGlobalMax();
                })
                .orElseThrow(() -> new IllegalArgumentException("El arreglo debe tener al menos 1 elemento."));
    }

    // ==========================================
    // CASOS DE PRUEBA (ASSERTS)
    // ==========================================
    public static void main(String[] args) {
        // NOTA: Para ejecutar los asserts en Java 8, activar la opción '-ea':
        // java -ea MaxSubarraySumFunctional

        // CASO 1: Ejemplo del enunciado con números mixtos
        // Subarreglo óptimo: [4, -1, 2, 1] -> suma = 6
        int[] tc1 = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        assert maxSubarraySum(tc1) == 6 : "Falló Caso 1: esperado 6, obtenido " + maxSubarraySum(tc1);

        // CASO 2: Todos los elementos son negativos
        // Subarreglo óptimo: [-1] -> retorna el menos negativo
        int[] tc2 = {-5, -3, -8, -1, -4};
        assert maxSubarraySum(tc2) == -1 : "Falló Caso 2: esperado -1, obtenido " + maxSubarraySum(tc2);

        // CASO 3: Arreglo de un solo elemento positivo
        // Subarreglo óptimo: [42] -> suma = 42
        int[] tc3 = {42};
        assert maxSubarraySum(tc3) == 42 : "Falló Caso 3: esperado 42, obtenido " + maxSubarraySum(tc3);

        System.out.println("¡Todos los casos de prueba funcional en Java 8 pasaron correctamente!");
    }
}
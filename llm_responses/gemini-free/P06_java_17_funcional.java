// Java 17 - Paradigma Funcional Estricto
//
// Características de Java 17 / 16 utilizadas:
// 1. Record de Java 16/17 (JEP 395): Utilizado para definir la estructura de estado inmutable de Kadane (`State`).
// 2. Stream API con `Arrays.stream()` y `.reduce()` para procesar la lista en una sola pasada inmutable.
// 3. Manejo declarativo de validación de entrada mediante `Optional.ofNullable()` y `.orElseThrow()`.
// 4. Inferencia de tipos de variables locales `var` en el flujo de ejecución principal.

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public class MaxSubarraySumFunctional17 {

    /**
     * Record inmutable de Java 17 que mantiene el estado actual y global del Algoritmo de Kadane.
     */
    private record State(int currentMax, int globalMax) {
        /**
         * Función pura de acumulación que devuelve un nuevo registro `State` inmutable.
         */
        public State accumulate(int num) {
            int newCurrent = Math.max(num, this.currentMax + num);
            int newGlobal = Math.max(this.globalMax, newCurrent);
            return new State(newCurrent, newGlobal);
        }
    }

    /**
     * Calcula la suma máxima de un subarreglo contiguo no vacío (Algoritmo de Kadane)
     * utilizando estrictamente el paradigma funcional en Java 17.
     *
     * @param nums Arreglo de enteros (puede contener negativos).
     * @return Suma máxima del subarreglo contiguo.
     * @throws IllegalArgumentException Si el arreglo es nulo o está vacío.
     */
    public static int maxSubarraySum(int[] nums) {
        return Optional.ofNullable(nums)
                .filter(arr -> arr.length > 0)
                .map(arr -> {
                    var initialState = new State(arr[0], arr[0]);

                    // Pipeline funcional con Stream API reduciendo sobre el resto del arreglo (índices 1 a len-1)
                    return Arrays.stream(arr, 1, arr.length)
                            .boxed()
                            .reduce(initialState,
                                    State::accumulate,
                                    (s1, s2) -> s2) // Combinador inmutable de estados
                            .globalMax();
                })
                .orElseThrow(() -> new IllegalArgumentException("El arreglo no puede estar vacío (debe tener al menos 1 elemento)."));
    }

    // ==========================================
    // CASOS DE PRUEBA (ASSERTS)
    // ==========================================
    public static void main(String[] args) {
        // NOTA: Para ejecutar asserts en Java, recuerda activar la bandera '-ea':
        // java -ea MaxSubarraySumFunctional17

        // CASO 1: Ejemplo del enunciado con números positivos y negativos
        // Subarreglo óptimo: [4, -1, 2, 1] -> suma = 6
        int[] tc1 = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        assert maxSubarraySum(tc1) == 6 : "Falló Caso 1: esperado 6, obtenido " + maxSubarraySum(tc1);

        // CASO 2: Todos los elementos son negativos
        // Subarreglo óptimo: [-1] -> retorna el menos negativo
        int[] tc2 = {-5, -3, -8, -1, -4};
        assert maxSubarraySum(tc2) == -1 : "Falló Caso 2: esperado -1, obtenido " + maxSubarraySum(tc2);

        // CASO 3: Arreglo de un solo elemento positivo
        // Subarreglo óptimo: [100] -> suma = 100
        int[] tc3 = {100};
        assert maxSubarraySum(tc3) == 100 : "Falló Caso 3: esperado 100, obtenido " + maxSubarraySum(tc3);

        System.out.println("¡Todos los casos de prueba funcionales en Java 17 pasaron correctamente!");
    }
}
// Java 21 - Paradigma Funcional Estricto
//
// Características de Java 21 utilizadas:
// 1. Records inmutables (JEP 395/440): Para definir el estado acumulador de Kadane (`State`) de manera limpia y concisa.
// 2. Inferencia de tipos con `var` (JEP 286/443) para variables locales en el flujo funcional.
// 3. Stream API con `Arrays.stream()` y `.reduce()`, combinado con `Optional` para validación declarativa.
// 4. Paradigma funcional puro: Cero bucles imperativos (`for`/`while`), sin variables mutables externas y sin recursión.

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public class MaxSubarraySumFunctional21 {

    /**
     * Record inmutable de Java 21 que modela el estado de acumulación del Algoritmo de Kadane.
     */
    private record State(int currentMax, int globalMax) {
        /**
         * Función pura de transición que retorna un nuevo objeto State inmutable.
         */
        public State accumulate(int num) {
            var newCurrent = Math.max(num, this.currentMax + num);
            var newGlobal = Math.max(this.globalMax, newCurrent);
            return new State(newCurrent, newGlobal);
        }
    }

    /**
     * Calcula la suma máxima de cualquier subarreglo contiguo no vacío (Algoritmo de Kadane)
     * empleando estrictamente el paradigma funcional con Java 21 Streams y Optional.
     *
     * @param nums Arreglo de enteros (puede contener negativos).
     * @return La suma máxima encontrada.
     * @throws IllegalArgumentException Si el arreglo es nulo o está vacío.
     */
    public static int maxSubarraySum(int[] nums) {
        return Optional.ofNullable(nums)
                .filter(arr -> arr.length > 0)
                .map(arr -> {
                    var initialState = new State(arr[0], arr[0]);

                    // Pipeline funcional sobre el resto del arreglo (índices 1 a len-1)
                    return Arrays.stream(arr, 1, arr.length)
                            .boxed()
                            .reduce(initialState,
                                    State::accumulate,
                                    (s1, s2) -> s2) // Combinador funcional para streams paralelos/secuenciales
                            .globalMax();
                })
                .orElseThrow(() -> new IllegalArgumentException("El arreglo no puede estar vacío (debe tener al menos 1 elemento)."));
    }

    // ==========================================
    // CASOS DE PRUEBA (ASSERTS)
    // ==========================================
    public static void main(String[] args) {
        // NOTA: Para ejecutar asserts en Java 21, recuerda activar el flag '-ea':
        // java -ea MaxSubarraySumFunctional21

        // CASO 1: Ejemplo del enunciado con números mixtos
        // Subarreglo óptimo: [4, -1, 2, 1] -> suma = 6
        int[] tc1 = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        assert maxSubarraySum(tc1) == 6 : "Falló Caso 1: esperado 6, obtenido " + maxSubarraySum(tc1);

        // CASO 2: Todos los elementos son negativos
        // Subarreglo óptimo: [-1] -> retorna el menos negativo
        int[] tc2 = {-5, -3, -8, -1, -4};
        assert maxSubarraySum(tc2) == -1 : "Falló Caso 2: esperado -1, obtenido " + maxSubarraySum(tc2);

        // CASO 3: Arreglo de un solo elemento positivo
        // Subarreglo óptimo: [99] -> suma = 99
        int[] tc3 = {99};
        assert maxSubarraySum(tc3) == 99 : "Falló Caso 3: esperado 99, obtenido " + maxSubarraySum(tc3);

        System.out.println("¡Todos los casos de prueba funcionales en Java 21 pasaron correctamente!");
    }
}
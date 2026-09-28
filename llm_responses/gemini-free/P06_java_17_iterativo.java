// Java 17 - Paradigma Iterativo Estricto
//
// Características de Java 17 / 11+ utilizadas:
// 1. Inferencia de tipos de variables locales 'var' (JEP 286): Para una sintaxis limpia y legible en variables locales.
// 2. Exception messages enriquecidos de Java 17: Validaciones con excepciones detalladas y comprobación nula rápida.
// 3. Inmutabilidad en estructuras de estado mediante Java Records (JEP 395).
// 4. Paradigma iterativo estricto: Bucle 'for' tradicional sin Streams, lambdas ni recursión.

import java.util.Objects;

public class MaxSubarraySumIterative17 {

    /**
     * Record de Java 17 utilizado como contenedor inmutable de resultados.
     */
    public record SubarrayResult(int maxSum, int startIndex, int endIndex) {}

    /**
     * Encuentra la suma máxima de cualquier subarreglo contiguo no vacío (Algoritmo de Kadane).
     *
     * Complejidad Temporal: O(n) - Recorrido iterativo de una sola pasada.
     * Complejidad Espacial: O(1) - Acumulación mediante variables mutables.
     *
     * @param nums Arreglo de enteros (puede contener negativos).
     * @return La suma máxima encontrada.
     * @throws IllegalArgumentException Si el arreglo es nulo o está vacío.
     */
    public static int maxSubarraySum(int[] nums) {
        if (Objects.isNull(nums) || nums.length == 0) {
            throw new IllegalArgumentException("El arreglo no puede ser nulo o estar vacío.");
        }

        // Inferencia de tipos con 'var'
        var currentMax = nums[0];
        var globalMax = nums[0];

        // Bucle iterativo for tradicional sin Streams ni lambdas
        for (var i = 1; i < nums.length; i++) {
            var num = nums[i];

            // Condicional imperativo para extender la secuencia o reiniciar desde num
            if (currentMax + num > num) {
                currentMax = currentMax + num;
            } else {
                currentMax = num;
            }

            // Actualización del máximo global
            if (currentMax > globalMax) {
                globalMax = currentMax;
            }
        }

        return globalMax;
    }

    /**
     * Sobrecarga orientada a objetos que retorna el estado completo utilizando Java 17 Records.
     */
    public static SubarrayResult maxSubarraySumDetailed(int[] nums) {
        if (Objects.isNull(nums) || nums.length == 0) {
            throw new IllegalArgumentException("El arreglo no puede ser nulo o estar vacío.");
        }

        var currentMax = nums[0];
        var globalMax = nums[0];
        var tempStart = 0;
        var bestStart = 0;
        var bestEnd = 0;

        for (var i = 1; i < nums.length; i++) {
            var num = nums[i];

            if (currentMax + num > num) {
                currentMax = currentMax + num;
            } else {
                currentMax = num;
                tempStart = i;
            }

            if (currentMax > globalMax) {
                globalMax = currentMax;
                bestStart = tempStart;
                bestEnd = i;
            }
        }

        return new SubarrayResult(globalMax, bestStart, bestEnd);
    }

    // ==========================================
    // CASOS DE PRUEBA (ASSERTS)
    // ==========================================
    public static void main(String[] args) {
        // NOTA: Para ejecutar asserts en Java, asegúrese de usar la opción '-ea':
        // java -ea MaxSubarraySumIterative17

        // CASO 1: Ejemplo del enunciado con números mixtos
        // Subarreglo óptimo: [4, -1, 2, 1] -> suma = 6
        int[] tc1 = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        assert maxSubarraySum(tc1) == 6 : "Falló Caso 1: esperado 6, obtenido " + maxSubarraySum(tc1);

        // CASO 2: Todos los elementos son negativos
        // Subarreglo óptimo: [-1] -> retorna el menos negativo
        int[] tc2 = {-5, -3, -8, -1, -4};
        assert maxSubarraySum(tc2) == -1 : "Falló Caso 2: esperado -1, obtenido " + maxSubarraySum(tc2);

        // CASO 3: Arreglo de un solo elemento positivo
        // Subarreglo óptimo: [15] -> suma = 15
        int[] tc3 = {15};
        assert maxSubarraySum(tc3) == 15 : "Falló Caso 3: esperado 15, obtenido " + maxSubarraySum(tc3);

        System.out.println("¡Todos los casos de prueba iterativos en Java 17 pasaron correctamente!");
    }
}
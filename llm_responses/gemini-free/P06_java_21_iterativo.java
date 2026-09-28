// Java 21 - Paradigma Iterativo Estricto
//
// Características de Java 21 utilizadas:
// 1. Inferencia avanzada de tipos con 'var' para variables locales (JEP 286 / JEP 443).
// 2. Registros (Records - JEP 395/440) para encapsular resultados de estado inmutable de manera limpia.
// 3. Paradigma iterativo estricto: Bucle 'for' tradicional basado en índices, evitando por completo Streams, lambdas y recursión.

import java.util.Objects;

public class MaxSubarraySumIterative21 {

    /**
     * Record inmutable de Java 21 para encapsular metadatos del subarreglo (opcional para análisis extendido).
     */
    public record SubarrayMetrics(int maxSum, int length) {}

    /**
     * Calcula la suma máxima de cualquier subarreglo contiguo no vacío (Algoritmo de Kadane).
     *
     * Complejidad Temporal: O(n) - Un único recorrido iterativo lineal.
     * Complejidad Espacial: O(1) - Uso exclusivo de variables locales primitivas mutables.
     *
     * @param nums Arreglo de enteros (puede contener negativos).
     * @return La suma máxima encontrada.
     * @throws IllegalArgumentException Si el arreglo es nulo o está vacío.
     */
    public static int maxSubarraySum(int[] nums) {
        if (Objects.isNull(nums) || nums.length == 0) {
            throw new IllegalArgumentException("El arreglo no puede ser nulo o estar vacío (debe tener al menos 1 elemento).");
        }

        // Variables mutables inicializadas con el primer elemento
        var currentMax = nums[0];
        var globalMax = nums[0];

        // Bucle iterativo tradicional imperativo (Java 21)
        for (var i = 1; i < nums.length; i++) {
            var num = nums[i];

            // Selección imperativa entre extender la secuencia previa o iniciar una nueva
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

    // ==========================================
    // CASOS DE PRUEBA (ASSERTS)
    // ==========================================
    public static void main(String[] args) {
        // NOTA: Para ejecutar asserts en Java, recuerda activar la bandera '-ea':
        // java -ea MaxSubarraySumIterative21

        // CASO 1: Ejemplo del enunciado con números mixtos
        // Subarreglo óptimo: [4, -1, 2, 1] -> suma = 6
        int[] tc1 = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        assert maxSubarraySum(tc1) == 6 : "Falló Caso 1: esperado 6, obtenido " + maxSubarraySum(tc1);

        // CASO 2: Todos los elementos son negativos
        // Subarreglo óptimo: [-1] -> retorna el menos negativo
        int[] tc2 = {-5, -3, -8, -1, -4};
        assert maxSubarraySum(tc2) == -1 : "Falló Caso 2: esperado -1, obtenido " + maxSubarraySum(tc2);

        // CASO 3: Arreglo de un solo elemento positivo
        // Subarreglo óptimo: [77] -> suma = 77
        int[] tc3 = {77};
        assert maxSubarraySum(tc3) == 77 : "Falló Caso 3: esperado 77, obtenido " + maxSubarraySum(tc3);

        System.out.println("¡Todos los casos de prueba iterativos en Java 21 pasaron correctamente!");
    }
}
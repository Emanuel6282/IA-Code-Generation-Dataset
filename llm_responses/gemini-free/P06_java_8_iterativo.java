// Java 8 - Paradigma Iterativo Estricto
// Se evita explícitamente el uso de Java Streams, lambdas y recursión.
// La solución emplea bucles 'for' imperativos tradicionales y variables mutables.

public class MaxSubarraySumIterative {

    /**
     * Calcula la suma máxima de cualquier subarreglo contiguo no vacío
     * utilizando el Algoritmo de Kadane bajo un enfoque iterativo puro.
     *
     * Complejidad Temporal: O(n) - Un solo recorrido imperativo del arreglo.
     * Complejidad Espacial: O(1) - Punteros y acumulación local en variables primitivas.
     *
     * @param nums Arreglo de enteros (puede contener negativos).
     * @return Suma máxima del subarreglo contiguo.
     */
    public static int maxSubarraySum(int[] nums) {
        if (nums == null || nums.length == 0) {
            throw new IllegalArgumentException("El arreglo no puede estar vacío (debe tener al menos 1 elemento).");
        }

        // Variables acumuladoras mutables para mantener el estado
        int currentMax = nums[0];
        int globalMax = nums[0];

        // Bucle iterativo for tradicional
        for (int i = 1; i < nums.length; i++) {
            int num = nums[i];

            // Toma de decisión imperativa: extender el subarreglo actual o comenzar uno nuevo
            if (currentMax + num > num) {
                currentMax = currentMax + num;
            } else {
                currentMax = num;
            }

            // Actualización del máximo global hallado
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
        // NOTA: Para ejecutar asserts en Java 8, recuerde activar el flag '-ea':
        // java -ea MaxSubarraySumIterative

        // CASO 1: Ejemplo del enunciado con números positivos y negativos
        // Subarreglo óptimo: [4, -1, 2, 1] -> suma = 6
        int[] tc1 = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        assert maxSubarraySum(tc1) == 6 : "Falló Caso 1: esperado 6, obtenido " + maxSubarraySum(tc1);

        // CASO 2: Todos los elementos son negativos
        // Subarreglo óptimo: [-1] -> retorna el menos negativo
        int[] tc2 = {-5, -3, -8, -1, -4};
        assert maxSubarraySum(tc2) == -1 : "Falló Caso 2: esperado -1, obtenido " + maxSubarraySum(tc2);

        // CASO 3: Arreglo de un solo elemento positivo
        // Subarreglo óptimo: [10] -> suma = 10
        int[] tc3 = {10};
        assert maxSubarraySum(tc3) == 10 : "Falló Caso 3: esperado 10, obtenido " + maxSubarraySum(tc3);

        System.out.println("¡Todos los casos de prueba en Java 8 (Iterativo) pasaron correctamente!");
    }
}
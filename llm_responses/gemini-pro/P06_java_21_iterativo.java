// CARACTERISTICAS DE JAVA 21 Y PARADIGMA ITERATIVO UTILIZADAS:
// 1. Estabilidad Sintáctica del Paradigma Imperativo: Al igual que en Java 17, se utiliza `var` para la inferencia de tipos locales. Aunque Java 21 (LTS) introduce características revolucionarias como Virtual Threads y Record Patterns, el núcleo procedimental para iteraciones matemáticas sobre arreglos primitivos se mantiene intencionalmente idéntico a versiones anteriores para preservar la retrocompatibilidad absoluta.
// 2. Optimizaciones Subyacentes de la JVM: Ejecutar este código imperativo clásico en la máquina virtual de Java 21 se beneficia de un motor JIT (Just-In-Time) mucho más maduro, con mejoras en la predicción de saltos (branch prediction) y vectorización automática, acelerando los bucles `for` sin requerir cambios en el código fuente.

public class MaxSubarray_Java21_Iterative {

    /**
     * Calcula la suma máxima de un subarreglo contiguo utilizando una aproximación iterativa estricta.
     */
    public static int maxSubarraySum(int[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException("El arreglo debe tener al menos 1 elemento.");
        }

        // Inferencia de tipos con 'var'
        var currentSum = arr[0];
        var maxSum = arr[0];

        // Bucle for imperativo tradicional
        for (var i = 1; i < arr.length; i++) {
            
            // Decisión iterativa explícita (Algoritmo de Kadane)
            if (currentSum + arr[i] > arr[i]) {
                currentSum = currentSum + arr[i];
            } else {
                currentSum = arr[i];
            }
            
            if (currentSum > maxSum) {
                maxSum = currentSum;
            }
        }

        return maxSum;
    }

    public static void main(String[] args) {
        // ==========================================
        // CASOS DE PRUEBA
        // Ejecutar compilando y corriendo con el flag -ea:
        // java -ea MaxSubarray_Java21_Iterative
        // ==========================================

        // 1. Caso base del ejemplo: mezcla de positivos y negativos
        int[] test1 = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        assert maxSubarraySum(test1) == 6 : "Error: Caso base iterativo esperado 6";

        // 2. Caso con todos los elementos negativos (retorna el menos negativo)
        int[] test2 = {-5, -2, -9, -1, -4};
        assert maxSubarraySum(test2) == -1 : "Error: Arreglo de negativos iterativo esperado -1";

        // 3. Caso con todos los elementos positivos (retorna la suma total)
        int[] test3 = {1, 2, 3, 4, 5};
        assert maxSubarraySum(test3) == 15 : "Error: Arreglo de positivos iterativo esperado 15";

        System.out.println("Todos los tests iterativos en Java 21 pasaron correctamente.");
    }
}
// CARACTERISTICAS DE JAVA Y PARADIGMA ITERATIVO UTILIZADAS:
// 1. Paradigma Imperativo/Iterativo Puro: Aunque Java 8 es famoso por introducir el API de Streams y Lambdas, para cumplir estrictamente con el requerimiento iterativo, evitamos por completo `IntStream` o `reduce`. Todo el control recae en bucles `for` tradicionales y el manejo de estado local.
// 2. Variables Mutables Primitivas: El estado algorítmico se gestiona mutando variables primitivas (`currentSum`, `maxSum`). Al ser un problema de rendimiento $O(n)$ y espacial $O(1)$, la iteración a bajo nivel en Java (JVM) es extremadamente rápida.
// 3. Control de Flujo Explícito: En lugar de usar `Math.max()`, desarrollamos explícitamente las bifurcaciones `if/else` para ilustrar la mecánica imperativa pura del Algoritmo de Kadane, proveyendo un código base procedimental clásico.

public class MaxSubarray_Java8_Iterative {

    /**
     * Calcula la suma máxima de un subarreglo contiguo utilizando una aproximación iterativa.
     */
    public static int maxSubarraySum(int[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException("El arreglo debe tener al menos 1 elemento.");
        }

        // Estado inicial mutable
        int currentSum = arr[0];
        int maxSum = arr[0];

        // Iteración clásica desde el segundo elemento
        for (int i = 1; i < arr.length; i++) {
            
            // Lógica imperativa explícita: ¿extendemos la cadena o empezamos una nueva?
            if (currentSum + arr[i] > arr[i]) {
                currentSum = currentSum + arr[i];
            } else {
                currentSum = arr[i];
            }
            
            // Actualización imperativa del máximo global
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
        // java -ea MaxSubarray_Java8_Iterative
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

        System.out.println("Todos los tests iterativos en Java 8 pasaron correctamente.");
    }
}
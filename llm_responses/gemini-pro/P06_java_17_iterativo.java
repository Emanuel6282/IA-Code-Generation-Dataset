// CARACTERISTICAS DE JAVA 17 Y PARADIGMA ITERATIVO UTILIZADAS:
// 1. Inferencia de Tipos Locales (var): Introducida en Java 10 y consolidada como estándar en Java 17, el uso de `var` reduce la verbosidad en la declaración de las variables de estado locales e índices de iteración.
// 2. Paradigma Imperativo Puro: Se mantiene el uso de variables mutables nativas y bucles `for` tradicionales. En algoritmos de recorrido lineal $O(n)$ como Kadane, esta aproximación iterativa permite a la JVM (con sus modernas optimizaciones JIT en Java 17) ejecutar el código a nivel de hardware sin el "overhead" de creación de objetos.
// 3. Excepciones con Mensajes Claros: A diferencia del paradigma funcional puro, el manejo iterativo permite validaciones tempranas simples y directas (Guard Clauses) antes de iniciar el ciclo.

public class MaxSubarray_Java17_Iterative {

    /**
     * Calcula la suma máxima de un subarreglo contiguo utilizando una aproximación iterativa.
     */
    public static int maxSubarraySum(int[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException("El arreglo debe tener al menos 1 elemento.");
        }

        // Uso de 'var' para inferencia de tipos locales (característica moderna de Java)
        var currentSum = arr[0];
        var maxSum = arr[0];

        // Iteración imperativa utilizando 'var' en el índice
        for (var i = 1; i < arr.length; i++) {
            
            // Lógica explícita de bifurcación de Kadane
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
        // java -ea MaxSubarray_Java17_Iterative
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

        System.out.println("Todos los tests iterativos en Java 17 pasaron correctamente.");
    }
}
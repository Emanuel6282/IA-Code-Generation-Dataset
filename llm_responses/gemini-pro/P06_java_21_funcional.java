import java.util.Arrays;

// CARACTERISTICAS DE JAVA 21 Y PARADIGMA FUNCIONAL UTILIZADAS:
// 1. Records Locales Consolidados: El uso de 'record' introducido en versiones anteriores alcanza su máxima madurez en Java 21. Se utiliza para crear tuplas de estado inmutables y transparentes sin el ruido sintáctico de las clases tradicionales.
// 2. Ejecución Directa de Código Fuente (JEP 330 / JEP 458): Java 21 mejora la capacidad de ejecutar programas directamente desde el código fuente sin un paso previo de compilación, lo cual agiliza la evaluación de scripts funcionales.
// 3. Estabilidad del Stream API con var: Se mantiene el uso de inferencia de tipos (`var`) junto con el encadenamiento declarativo de `reduce` y `Optional`, demostrando que el paradigma de plegado funcional en Java ha alcanzado su forma final para operaciones matemáticas secuenciales.

public class MaxSubarray_Java21_Functional {

    /**
     * Calcula la suma máxima de un subarreglo contiguo utilizando Streams y un estado estrictamente inmutable.
     */
    public static int maxSubarraySum(int[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException("El arreglo debe tener al menos 1 elemento.");
        }

        // Estructura inmutable local para transportar el acumulador del algoritmo de Kadane
        record KadaneState(int currentMax, int globalMax) {}

        // Pipeline funcional puro: sin variables mutables, iteradores, ni efectos secundarios
        var finalState = Arrays.stream(arr)
                // 1. Map: Transformación del flujo de primitivos a un flujo de estados iniciales
                .mapToObj(x -> new KadaneState(x, x))
                
                // 2. Reduce: Función pura de plegado que propaga el estado inmutable
                .reduce((acc, val) -> {
                    var newCurrent = Math.max(val.currentMax(), acc.currentMax() + val.currentMax());
                    var newGlobal = Math.max(acc.globalMax(), newCurrent);
                    return new KadaneState(newCurrent, newGlobal);
                });

        // 3. Extracción segura del estado final empleando la mónada Optional
        return finalState
                .map(KadaneState::globalMax)
                .orElseThrow(() -> new IllegalStateException("Flujo vacío inesperado durante la reducción."));
    }

    public static void main(String[] args) {
        // ==========================================
        // CASOS DE PRUEBA
        // En Java 21, puedes ejecutar este archivo directamente sin compilar:
        // java -ea MaxSubarray_Java21_Functional.java
        // ==========================================

        // 1. Caso base del ejemplo: mezcla de positivos y negativos
        int[] test1 = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        assert maxSubarraySum(test1) == 6 : "Error: Caso base funcional esperado 6";

        // 2. Caso con todos los elementos negativos (retorna el menos negativo)
        int[] test2 = {-5, -2, -9, -1, -4};
        assert maxSubarraySum(test2) == -1 : "Error: Arreglo de negativos funcional esperado -1";

        // 3. Caso con todos los elementos positivos (retorna la suma total)
        int[] test3 = {1, 2, 3, 4, 5};
        assert maxSubarraySum(test3) == 15 : "Error: Arreglo de positivos funcional esperado 15";

        System.out.println("Todos los tests funcionales en Java 21 pasaron correctamente.");
    }
}
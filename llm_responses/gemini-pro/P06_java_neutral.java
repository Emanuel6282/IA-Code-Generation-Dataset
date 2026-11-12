public class MaxSubarray {

    /**
     * Calcula la suma máxima de un subarreglo contiguo utilizando el algoritmo de Kadane.
     */
    public static int maxSubarraySum(int[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException("El arreglo debe tener al menos 1 elemento.");
        }

        int currentMax = arr[0];
        int globalMax = arr[0];

        for (int i = 1; i < arr.length; i++) {
            // Decide si es mejor extender el subarreglo actual o iniciar uno nuevo
            currentMax = Math.max(arr[i], currentMax + arr[i]);
            
            // Actualiza el máximo global si el actual lo supera
            globalMax = Math.max(globalMax, currentMax);
        }

        return globalMax;
    }

    public static void main(String[] args) {
        // ==========================================
        // CASOS DE PRUEBA
        // Ejecutar compilando y corriendo con el flag -ea:
        // java -ea MaxSubarray
        // ==========================================

        // 1. Caso base del ejemplo: mezcla de positivos y negativos
        int[] test1 = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        assert maxSubarraySum(test1) == 6 : "Error: Caso base esperado 6";

        // 2. Caso con todos los elementos negativos (retorna el menos negativo)
        int[] test2 = {-5, -2, -9, -1, -4};
        assert maxSubarraySum(test2) == -1 : "Error: Arreglo de negativos esperado -1";

        // 3. Caso con todos los elementos positivos (retorna la suma total)
        int[] test3 = {1, 2, 3, 4, 5};
        assert maxSubarraySum(test3) == 15 : "Error: Arreglo de positivos esperado 15";

        System.out.println("Todos los casos de prueba pasaron correctamente.");
    }
}
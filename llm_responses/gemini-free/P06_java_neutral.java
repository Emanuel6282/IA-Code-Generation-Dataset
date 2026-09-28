public class Solution {

    /**
     * Calcula la suma máxima de cualquier subarreglo contiguo no vacío (Algoritmo de Kadane).
     *
     * @param nums Arreglo de enteros (puede contener negativos).
     * @return La suma máxima encontrada.
     * @throws IllegalArgumentException Si el arreglo es nulo o está vacío.
     */
    public static int maxSubarraySum(int[] nums) {
        if (nums == null || nums.length == 0) {
            throw new IllegalArgumentException("El arreglo debe tener al menos 1 elemento.");
        }

        int currentMax = nums[0];
        int globalMax = nums[0];

        // Recorrido lineal iterativo (O(n) en tiempo, O(1) en espacio)
        for (int i = 1; i < nums.length; i++) {
            // Se decide si extender el subarreglo actual o iniciar uno nuevo en nums[i]
            currentMax = Math.max(nums[i], currentMax + nums[i]);
            
            // Se actualiza la suma máxima global alcanzada hasta el momento
            globalMax = Math.max(globalMax, currentMax);
        }

        return globalMax;
    }

    // ==========================================
    // CASOS DE PRUEBA (ASSERTS)
    // ==========================================
    public static void main(String[] args) {
        // NOTA: Para ejecutar asserts en Java, recuerda usar el flag '-ea' al ejecutar (java -ea Solution)

        // CASO 1: Ejemplo del enunciado con números mixtos
        // Subarreglo óptimo: [4, -1, 2, 1] -> suma = 6
        int[] tc1 = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        assert maxSubarraySum(tc1) == 6 : "Falló Caso 1: esperado 6, obtenido " + maxSubarraySum(tc1);

        // CASO 2: Todos los elementos son negativos
        // Subarreglo óptimo: [-1] -> retorna el menos negativo
        int[] tc2 = {-5, -3, -8, -1, -4};
        assert maxSubarraySum(tc2) == -1 : "Falló Caso 2: esperado -1, obtenido " + maxSubarraySum(tc2);

        // CASO 3: Arreglo de un solo elemento
        // Subarreglo óptimo: [100] -> suma = 100
        int[] tc3 = {100};
        assert maxSubarraySum(tc3) == 100 : "Falló Caso 3: esperado 100, obtenido " + maxSubarraySum(tc3);

        System.out.println("¡Todos los casos de prueba pasaron correctamente!");
    }
}
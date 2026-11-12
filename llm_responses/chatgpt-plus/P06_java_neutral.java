// P06: Max Subarray Sum — Java
// Algoritmo de Kadane
//
// Complejidad temporal: O(n)
// Complejidad espacial: O(1)

public class P06MaxSubarraySum {

    public static int maxSubarraySum(int[] nums) {

        // No se permite un subarreglo vacío.
        if (nums == null || nums.length == 0) {
            throw new IllegalArgumentException(
                "El arreglo no puede estar vacío"
            );
        }

        // Inicializar con el primer elemento.
        int currentSum = nums[0];
        int maxSum = nums[0];

        // Recorrer los elementos restantes.
        for (int i = 1; i < nums.length; i++) {

            // Elegir entre iniciar un nuevo subarreglo
            // o continuar el subarreglo anterior.
            currentSum = Math.max(
                nums[i],
                currentSum + nums[i]
            );

            // Actualizar la máxima suma encontrada.
            maxSum = Math.max(maxSum, currentSum);
        }

        return maxSum;
    }

    public static void main(String[] args) {

        // TEST 1: Ejemplo del enunciado.
        assert maxSubarraySum(
            new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4}
        ) == 6;

        // TEST 2: Todos los elementos negativos.
        assert maxSubarraySum(
            new int[]{-8, -3, -6, -2, -5, -4}
        ) == -2;

        // TEST 3: Todos los elementos positivos.
        assert maxSubarraySum(
            new int[]{1, 2, 3, 4, 5}
        ) == 15;

        System.out.println("All tests passed");
    }
}
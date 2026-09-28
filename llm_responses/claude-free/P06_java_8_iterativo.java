public class MaxSubarraySum {

    /**
     * Dado un arreglo de enteros (puede contener negativos), retorna la
     * suma maxima de cualquier subarreglo contiguo.
     *
     * Implementacion estrictamente iterativa (algoritmo de Kadane): usa
     * un bucle for explicito con variables mutables (currentMax,
     * globalMax) actualizadas manualmente en cada iteracion mediante
     * comparaciones if/else, sin usar Math.max() para maximizar la
     * explicitud iterativa del codigo. No se usan Streams ni lambdas.
     * Compatible con Java 8.
     *
     * @param arr Arreglo de enteros (no vacio), puede contener negativos.
     * @return La suma maxima de cualquier subarreglo contiguo no vacio.
     *         Si todos los elementos son negativos, retorna el menos
     *         negativo.
     */
    public static int maxSubarraySum(int[] arr) {
        int currentMax = arr[0];
        int globalMax = arr[0];

        for (int i = 1; i < arr.length; i++) {
            int extendido = currentMax + arr[i];
            if (arr[i] > extendido) {
                currentMax = arr[i];
            } else {
                currentMax = extendido;
            }

            if (currentMax > globalMax) {
                globalMax = currentMax;
            }
        }

        return globalMax;
    }

    public static void main(String[] args) {
        // Caso 1: ejemplo del enunciado, mezcla de positivos y negativos
        assert maxSubarraySum(new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4}) == 6 : "Caso 1 fallido";

        // Caso 2: todos los elementos son negativos -> retorna el menos negativo
        assert maxSubarraySum(new int[]{-8, -3, -6, -2, -5, -4}) == -2 : "Caso 2 fallido";

        // Caso 3: todos los elementos son positivos -> la suma total es la maxima
        assert maxSubarraySum(new int[]{1, 2, 3, 4, 5}) == 15 : "Caso 3 fallido";

        System.out.println("Todos los casos de prueba pasaron correctamente.");
    }
}
public class MaxSubarraySum {

    /**
     * Dado un arreglo de enteros (puede contener negativos), retorna la
     * suma maxima de cualquier subarreglo contiguo.
     *
     * Implementacion mediante el algoritmo de Kadane: se recorre el
     * arreglo manteniendo la suma maxima que termina en la posicion
     * actual (currentMax) y la suma maxima global encontrada hasta el
     * momento (globalMax). En cada paso, currentMax se actualiza como el
     * maximo entre extender el subarreglo anterior (currentMax + arr[i])
     * o empezar un nuevo subarreglo desde el elemento actual (arr[i]).
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
            currentMax = Math.max(arr[i], currentMax + arr[i]);
            globalMax = Math.max(globalMax, currentMax);
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
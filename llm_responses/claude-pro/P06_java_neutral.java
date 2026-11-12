public class MaxSubarray {

    /**
     * Retorna la suma máxima de un subarreglo contiguo no vacío de {@code nums}.
     *
     * <p>Usa el algoritmo de Kadane: O(n) en tiempo y O(1) en memoria. Las sumas se
     * acumulan en {@code long}: la suma de varios int puede superar el rango de int
     * (por ejemplo, Integer.MAX_VALUE + Integer.MAX_VALUE), y con int se desbordaría en
     * silencio dando un resultado negativo.
     *
     * @throws IllegalArgumentException si {@code nums} está vacío
     */
    public static long maxSubarraySum(int[] nums) {
        if (nums.length == 0) {
            throw new IllegalArgumentException("el arreglo debe tener al menos un elemento");
        }
        // Se parte del primer elemento (no de 0) porque el subarreglo no puede ser vacío;
        // así, si todos son negativos, el resultado es el menos negativo.
        long best = nums[0];
        long current = nums[0];
        for (int i = 1; i < nums.length; i++) {
            // La mejor suma que termina en nums[i]: o extiende el subarreglo anterior,
            // o empieza de nuevo en nums[i] si lo acumulado hasta ahora resta.
            current = Math.max(nums[i], current + nums[i]);
            best = Math.max(best, current);
        }
        return best;
    }

    // Ejecutar con assertions habilitadas: java -ea MaxSubarray.java
    public static void main(String[] args) {
        boolean assertionsEnabled = false;
        assert assertionsEnabled = true; // solo se asigna si -ea está activo
        if (!assertionsEnabled) {
            System.err.println("Advertencia: ejecuta con 'java -ea' para que los asserts se evalúen.");
        }

        // Caso 1: ejemplo del enunciado ([4, -1, 2, 1])
        assert maxSubarraySum(new int[] {-2, 1, -3, 4, -1, 2, 1, -5, 4}) == 6;

        // Caso 2: todos negativos -> el menos negativo
        assert maxSubarraySum(new int[] {-3, -1, -2}) == -1;
        assert maxSubarraySum(new int[] {-5}) == -5;

        // Caso 3: suma que supera el rango de int (con int se desbordaría a -2)
        assert maxSubarraySum(new int[] {Integer.MAX_VALUE, Integer.MAX_VALUE}) == 2L * Integer.MAX_VALUE;

        // Arreglo vacío -> IllegalArgumentException
        boolean threw = false;
        try {
            maxSubarraySum(new int[] {});
        } catch (IllegalArgumentException e) {
            threw = true;
        }
        assert threw : "un arreglo vacío debería lanzar IllegalArgumentException";

        System.out.println("Todas las pruebas pasaron.");
    }
}
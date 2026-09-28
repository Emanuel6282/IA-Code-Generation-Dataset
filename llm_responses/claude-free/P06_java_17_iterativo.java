public class MaxSubarraySum {

    public static int maxSubarraySum(int[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException("El arreglo no puede ser null ni vacio");
        }

        int maxEndingHere = arr[0];
        int maxSoFar = arr[0];

        for (int i = 1; i < arr.length; i++) {
            int x = arr[i];
            if (maxEndingHere + x > x) {
                maxEndingHere = maxEndingHere + x;
            } else {
                maxEndingHere = x;
            }

            if (maxEndingHere > maxSoFar) {
                maxSoFar = maxEndingHere;
            }
        }

        return maxSoFar;
    }

    public static void main(String[] args) {
        assert maxSubarraySum(new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4}) == 6
                : "Fallo caso 1";

        assert maxSubarraySum(new int[]{-5, -2, -8, -1, -9}) == -1
                : "Fallo caso 2";

        assert maxSubarraySum(new int[]{5}) == 5
                : "Fallo caso 3";

        System.out.println("Todos los tests pasaron correctamente.");
    }
}
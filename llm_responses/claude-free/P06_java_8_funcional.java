import java.util.Arrays;
import java.util.Optional;

public class MaxSubarraySum {

    private static final class Acc {
        final int maxEndingHere;
        final int maxSoFar;

        Acc(int maxEndingHere, int maxSoFar) {
            this.maxEndingHere = maxEndingHere;
            this.maxSoFar = maxSoFar;
        }
    }

    public static int maxSubarraySum(int[] arr) {
        return Optional.ofNullable(arr)
                .filter(a -> a.length > 0)
                .map(a -> Arrays.stream(a)
                        .boxed()
                        .reduce(
                                new Acc(0, Integer.MIN_VALUE),
                                (acc, x) -> {
                                    int newMaxEndingHere = Math.max(x, acc.maxEndingHere + x);
                                    int newMaxSoFar = Math.max(acc.maxSoFar, newMaxEndingHere);
                                    return new Acc(newMaxEndingHere, newMaxSoFar);
                                },
                                (acc1, acc2) -> acc2
                        ).maxSoFar
                )
                .orElseThrow(() -> new IllegalArgumentException("El arreglo no puede ser null ni vacio"));
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
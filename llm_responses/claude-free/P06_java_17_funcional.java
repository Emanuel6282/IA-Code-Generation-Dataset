import java.util.Arrays;
import java.util.Optional;

public class MaxSubarraySum {

    private record Acc(int maxEndingHere, int maxSoFar) {
    }

    public static int maxSubarraySum(int[] arr) {
        return Optional.ofNullable(arr)
                .filter(a -> a.length > 0)
                .map(a -> Arrays.stream(a)
                        .boxed()
                        .reduce(
                                new Acc(0, Integer.MIN_VALUE),
                                (acc, x) -> {
                                    var newMaxEndingHere = Math.max(x, acc.maxEndingHere() + x);
                                    var newMaxSoFar = Math.max(acc.maxSoFar(), newMaxEndingHere);
                                    return new Acc(newMaxEndingHere, newMaxSoFar);
                                },
                                (acc1, acc2) -> acc2
                        ).maxSoFar()
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
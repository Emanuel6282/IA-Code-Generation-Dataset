import java.util.Arrays;
import java.util.Optional;

public class MaxSubarraySum {

private record State(int currentSum, int maxSum) {

    State add(int number) {
        int newCurrentSum = Math.max(
            number,
            currentSum + number
        );

        int newMaxSum = Math.max(
            maxSum,
            newCurrentSum
        );

        return new State(newCurrentSum, newMaxSum);
    }
}

public static int maxSubarraySum(int[] numbers) {
    Optional<Integer> first = Arrays.stream(numbers)
        .boxed()
        .findFirst();

    return first
        .map(firstNumber ->
            Arrays.stream(numbers)
                .skip(1)
                .boxed()
                .reduce(
                    new State(firstNumber, firstNumber),
                    State::add,
                    (left, right) -> new State(
                        Math.max(
                            left.currentSum(),
                            right.currentSum()
                        ),
                        Math.max(
                            left.maxSum(),
                            right.maxSum()
                        )
                    )
                )
                .maxSum()
        )
        .orElseThrow(
            () -> new IllegalArgumentException(
                "El arreglo no puede estar vacío"
            )
        );
}

public static void main(String[] args) {
    assert maxSubarraySum(
        new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4}
    ) == 6;

    assert maxSubarraySum(
        new int[]{-5, -2, -8, -1}
    ) == -1;

    assert maxSubarraySum(
        new int[]{1, 2, 3, 4}
    ) == 10;

    System.out.println("All tests passed");
}

}

import java.util.Arrays;
import java.util.Optional;

public class MaxSubarraySum {

private static class State {
    private final int currentSum;
    private final int maxSum;

    State(int currentSum, int maxSum) {
        this.currentSum = currentSum;
        this.maxSum = maxSum;
    }

    State next(int number) {
        int newCurrentSum = Math.max(number, currentSum + number);
        int newMaxSum = Math.max(maxSum, newCurrentSum);

        return new State(newCurrentSum, newMaxSum);
    }

    int getMaxSum() {
        return maxSum;
    }
}

public static int maxSubarraySum(int[] numbers) {
    Optional<State> result = Arrays.stream(numbers)
        .boxed()
        .reduce(
            (first, second) -> {
                State state = new State(first, first);
                return state.next(second);
            }
        );

    return result
        .map(State::getMaxSum)
        .orElseThrow(
            () -> new IllegalArgumentException("El arreglo no puede estar vacío")
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

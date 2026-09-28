// Max Subarray Sum — Java 8
// Paradigma: estrictamente iterativo
// Algoritmo: Kadane
// Complejidad: O(n) tiempo y O(1) espacio adicional

public class MaxSubarraySum {

public static int maxSubarraySum(int[] numbers) {
    int currentSum = numbers[0];
    int maxSum = numbers[0];

    for (int i = 1; i < numbers.length; i++) {
        if (currentSum + numbers[i] > numbers[i]) {
            currentSum = currentSum + numbers[i];
        } else {
            currentSum = numbers[i];
        }

        if (currentSum > maxSum) {
            maxSum = currentSum;
        }
    }

    return maxSum;
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

public class MaxSubarraySum {

public static int maxSubarraySum(int[] numbers) {
    int currentSum = numbers[0];
    int maxSum = numbers[0];

    for (int i = 1; i < numbers.length; i++) {
        currentSum = Math.max(numbers[i], currentSum + numbers[i]);
        maxSum = Math.max(maxSum, currentSum);
    }

    return maxSum;
}

public static void main(String[] args) {
    assert maxSubarraySum(new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4}) == 6;
    assert maxSubarraySum(new int[]{-5, -2, -8, -1}) == -1;
    assert maxSubarraySum(new int[]{1, 2, 3, 4}) == 10;

    System.out.println("All tests passed");
}

}

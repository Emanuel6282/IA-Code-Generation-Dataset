// Max Subarray Sum — Java 17
// Paradigma: estrictamente iterativo
// Algoritmo: Kadane
//
// Características utilizadas:
// - for-each: recorrido iterativo del arreglo.
// - Math.max: selección del mejor valor en cada paso.
//
// Nota: Java 17 no incorpora una característica nueva que mejore
// materialmente este algoritmo sin abandonar el paradigma iterativo.

public class MaxSubarraySum {

public static int maxSubarraySum(int[] numbers) {
    int currentSum = numbers[0];
    int maxSum = numbers[0];

    for (int number : numbers) {
        if (number == numbers[0]) {
            continue;
        }

        currentSum = Math.max(number, currentSum + number);
        maxSum = Math.max(maxSum, currentSum);
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

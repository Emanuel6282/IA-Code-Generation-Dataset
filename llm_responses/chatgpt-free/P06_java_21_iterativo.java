// Max Subarray Sum — Java 21
// Paradigma: estrictamente iterativo
// Algoritmo: Kadane
//
// Características utilizadas:
// - for: recorrido iterativo del arreglo.
// - Math.max: selección del mejor valor en cada paso.
//
// Java 21 incorpora características como record patterns,
// pattern matching para switch y Sequenced Collections, pero
// ninguna mejora materialmente este algoritmo iterativo.
// Por ello no se fuerzan características que no aportan al problema.
//
// Complejidad: O(n) tiempo y O(1) espacio adicional.

public class MaxSubarraySum {

public static int maxSubarraySum(int[] numbers) {
    int currentSum = numbers[0];
    int maxSum = numbers[0];

    for (int i = 1; i < numbers.length; i++) {
        currentSum = Math.max(
            numbers[i],
            currentSum + numbers[i]
        );

        maxSum = Math.max(
            maxSum,
            currentSum
        );
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

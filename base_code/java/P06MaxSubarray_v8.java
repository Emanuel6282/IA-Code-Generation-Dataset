// P06: Max Subarray Sum (Kadane) — Java 8 (codigo base para refactoring)
public class P06MaxSubarray_v8 {

    public static int maxSubarraySum(int[] arr) {
        int currentMax = arr[0];
        int globalMax = arr[0];
        for (int i = 1; i < arr.length; i++) {
            currentMax = Math.max(arr[i], currentMax + arr[i]);
            globalMax = Math.max(globalMax, currentMax);
        }
        return globalMax;
    }

    public static void main(String[] args) {
        assert maxSubarraySum(new int[]{-2,1,-3,4,-1,2,1,-5,4}) == 6;
        assert maxSubarraySum(new int[]{1}) == 1;
        assert maxSubarraySum(new int[]{-2,-1}) == -1;
        System.out.println("All tests passed");
    }
}

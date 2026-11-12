public class BinarySearch {

    public static int binarySearch(int[] arr, int target) {
        int left = 0;
        int right = arr.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (arr[mid] == target) {
                return mid;
            } else if (arr[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return -1;
    }

    public static void main(String[] args) {

        // Test 1: Objetivo encontrado
        assert binarySearch(new int[]{1, 3, 5, 7, 9}, 5) == 2;

        // Test 2: Objetivo no encontrado
        assert binarySearch(new int[]{1, 3, 5, 7, 9}, 4) == -1;

        // Test 3: Arreglo vacío
        assert binarySearch(new int[]{}, 5) == -1;

        System.out.println("All tests passed");
    }
}
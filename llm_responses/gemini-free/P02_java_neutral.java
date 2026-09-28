public class Solution {

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
        // --- Casos de prueba ---

        // Caso 1: Elemento presente en la matriz (ejemplo provisto)
        assert binarySearch(new int[]{1, 3, 5, 7, 9}, 5) == 2 : "Falló el Caso 1";

        // Caso 2: Elemento no presente en la matriz (ejemplo provisto)
        assert binarySearch(new int[]{1, 3, 5, 7, 9}, 4) == -1 : "Falló el Caso 2";

        // Caso 3: Matriz vacía y límites extremo
        assert binarySearch(new int[]{}, 10) == -1 : "Falló el Caso 3a (Matriz vacía)";
        assert binarySearch(new int[]{10, 20, 30, 40}, 10) == 0 : "Falló el Caso 3b (Primer elemento)";
        assert binarySearch(new int[]{10, 20, 30, 40}, 40) == 3 : "Falló el Caso 3c (Último elemento)";

        System.out.println("¡Todos los casos de prueba pasaron correctamente!");
    }
}
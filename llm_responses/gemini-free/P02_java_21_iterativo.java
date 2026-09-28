public class BinarySearchJava21Iterative {

    public static int binarySearch(int[] arr, int target) {
        // Inferencia de tipos con var e inicio de los punteros
        var left = 0;
        var right = arr.length - 1;

        // Control del ciclo iterativo con variables mutables
        while (left <= right) {
            var mid = left + (right - left) / 2;

            // Pattern Matching en Switch mejorado (estándar en Java 21)
            // Evaluamos la relación de comparación mediante un Integer de forma clara y exhaustiva
            switch (Integer.compare(arr[mid], target)) {
                case 0 -> {
                    return mid;
                }
                case -1 -> left = mid + 1; // arr[mid] < target
                case 1 -> right = mid - 1; // arr[mid] > target
                default -> throw new IllegalStateException("Resultado de comparación inesperado");
            }
        }

        return -1;
    }

    public static void main(String[] args) {
        // --- Casos de prueba (Java 21) ---

        // Caso 1: Elemento presente en el arreglo (ejemplo provisto)
        assert binarySearch(new int[]{1, 3, 5, 7, 9}, 5) == 2 : "Falló el Caso 1";

        // Caso 2: Elemento no presente en el arreglo (ejemplo provisto)
        assert binarySearch(new int[]{1, 3, 5, 7, 9}, 4) == -1 : "Falló el Caso 2";

        // Caso 3: Arreglo vacío y búsqueda en los límites extremos
        assert binarySearch(new int[]{}, 10) == -1 : "Falló el Caso 3a (Arreglo vacío)";
        assert binarySearch(new int[]{10, 20, 30, 40}, 10) == 0 : "Falló el Caso 3b (Primer elemento)";
        assert binarySearch(new int[]{10, 20, 30, 40}, 40) == 3 : "Falló el Caso 3c (Último elemento)";

        System.out.println("¡Todos los casos de prueba pasaron correctamente!");
    }
}
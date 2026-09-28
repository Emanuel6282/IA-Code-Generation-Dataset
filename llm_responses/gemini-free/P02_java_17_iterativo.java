public class BinarySearchJava17Iterative {

    public static int binarySearch(int[] arr, int target) {
        // Inferencia de tipos de variables locales (var) introducida en Java 10+
        var left = 0;
        var right = arr.length - 1;

        // Bucle while con variables mutables (paradigma iterativo)
        while (left <= right) {
            var mid = left + (right - left) / 2;

            // Uso de Expresiones Switch (Java 14/17) para estructurar el flujo de control
            // evaluando la comparación entera de forma clara y moderna
            switch (Integer.compare(arr[mid], target)) {
                case 0 -> {
                    return mid;
                }
                case -1 -> left = mid + 1; // arr[mid] < target
                case 1 -> right = mid - 1; // arr[mid] > target
            }
        }

        return -1;
    }

    public static void main(String[] args) {
        // --- Casos de prueba (Java 17) ---

        // Caso 1: Elemento presente en el arreglo (ejemplo provisto)
        assert binarySearch(new int[]{1, 3, 5, 7, 9}, 5) == 2 : "Falló el Caso 1";

        // Caso 2: Elemento no presente en el arreglo (ejemplo provisto)
        assert binarySearch(new int[]{1, 3, 5, 7, 9}, 4) == -1 : "Falló el Caso 2";

        // Caso 3: Arreglo vacío y búsqueda en límites extremos
        assert binarySearch(new int[]{}, 10) == -1 : "Falló el Caso 3a (Arreglo vacío)";
        assert binarySearch(new int[]{10, 20, 30, 40}, 10) == 0 : "Falló el Caso 3b (Primer elemento)";
        assert binarySearch(new int[]{10, 20, 30, 40}, 40) == 3 : "Falló el Caso 3c (Último elemento)";

        System.out.println("¡Todos los casos de prueba pasaron correctamente!");
    }
}
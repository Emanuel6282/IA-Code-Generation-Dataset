public class BinarySearchIterative8 {

    /**
     * Busca un valor objetivo en un arreglo de enteros ordenado ascendentemente.
     * Retorna el índice del objetivo si se encuentra, o -1 en caso contrario.
     * 
     * Implementación estrictamente iterativa, utilizando un bucle while
     * y mutando las variables de estado (left, right).
     */
    public static int binarySearch(int[] arr, int target) {
        // Validación de seguridad para evitar NullPointerException
        if (arr == null) {
            return -1;
        }

        int left = 0;
        int right = arr.length - 1;

        // Bucle iterativo clásico
        while (left <= right) {
            // El cálculo left + (right - left) / 2 es la forma matemáticamente 
            // segura de calcular la mitad evitando desbordamiento (Integer Overflow)
            int mid = left + (right - left) / 2;

            if (arr[mid] == target) {
                return mid; // Elemento encontrado
            } else if (arr[mid] < target) {
                left = mid + 1; // El objetivo está en la mitad derecha
            } else {
                right = mid - 1; // El objetivo está en la mitad izquierda
            }
        }

        return -1; // Elemento no encontrado
    }

    public static void main(String[] args) {
        // ==========================================
        // CASOS DE PRUEBA
        // ==========================================
        // Nota: Para que las aserciones validen correctamente, la JVM debe 
        // ejecutarse con la bandera '-ea' (enableassertions).

        // 1. Caso base del ejemplo: el elemento existe y debe retornar su índice
        int[] arr1 = {1, 3, 5, 7, 9};
        assert binarySearch(arr1, 5) == 2 : "Error: El objetivo 5 debería estar en el índice 2";

        // 2. Caso del ejemplo: el elemento no existe y debe retornar -1
        int[] arr2 = {1, 3, 5, 7, 9};
        assert binarySearch(arr2, 4) == -1 : "Error: El objetivo 4 no está, debería retornar -1";

        // 3. Caso límite: el arreglo está vacío
        int[] arr3 = {};
        assert binarySearch(arr3, 5) == -1 : "Error: Arreglo vacío debería retornar -1";

        System.out.println("Todos los tests iterativos en Java 8 pasaron correctamente.");
    }
}
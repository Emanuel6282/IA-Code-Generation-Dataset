public class BinarySearchTest {

    /**
     * Busca un valor objetivo en un arreglo de enteros ordenado ascendentemente.
     * Retorna el índice del objetivo si se encuentra, o -1 en caso contrario.
     */
    public static int binarySearch(int[] arr, int target) {
        int left = 0;
        int right = arr.length - 1;

        while (left <= right) {
            // Se calcula el punto medio de esta forma para prevenir el desbordamiento 
            // (overflow) de enteros que ocurriría con (left + right) / 2 en arreglos masivos
            int mid = left + (right - left) / 2;

            if (arr[mid] == target) {
                return mid; // Elemento encontrado
            } else if (arr[mid] < target) {
                left = mid + 1; // Descartar la mitad izquierda
            } else {
                right = mid - 1; // Descartar la mitad derecha
            }
        }

        return -1; // Elemento no encontrado
    }

    public static void main(String[] args) {
        // ==========================================
        // CASOS DE PRUEBA
        // ==========================================
        // Nota: Para que las aserciones se evalúen, la JVM debe ejecutarse 
        // con la bandera '-ea' o '-enableassertions'.

        // 1. Caso del ejemplo: el elemento existe en el arreglo
        assert binarySearch(new int[]{1, 3, 5, 7, 9}, 5) == 2 : "Fallo: el objetivo 5 debería estar en el índice 2";

        // 2. Caso del ejemplo: el elemento no existe en el arreglo
        assert binarySearch(new int[]{1, 3, 5, 7, 9}, 4) == -1 : "Fallo: el objetivo 4 no está, debería retornar -1";

        // 3. Caso límite: el arreglo está vacío
        assert binarySearch(new int[]{}, 5) == -1 : "Fallo: en un arreglo vacío debería retornar -1";

        System.out.println("Todos los casos de prueba pasaron correctamente.");
    }
}
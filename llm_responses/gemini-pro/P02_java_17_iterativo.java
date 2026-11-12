public class BinarySearchIterative17 {

    /**
     * Busca un valor objetivo en un arreglo de enteros ordenado ascendentemente.
     * Retorna el índice del objetivo si se encuentra, o -1 en caso contrario.
     * 
     * Implementación iterativa utilizando bucles while y las características 
     * de inferencia de tipos de las versiones modernas de Java.
     */
    public static int binarySearch(int[] arr, int target) {
        if (arr == null) {
            return -1;
        }

        // Uso de 'var' (Inferencia de tipos para variables locales) 
        // consolidado en las versiones recientes de Java LTS.
        var left = 0;
        var right = arr.length - 1;

        // Bucle iterativo con mutación de estado
        while (left <= right) {
            var mid = left + (right - left) / 2;
            var midVal = arr[mid];

            if (midVal == target) {
                return mid; // Elemento encontrado
            } else if (midVal < target) {
                left = mid + 1; // Descartar mitad izquierda
            } else {
                right = mid - 1; // Descartar mitad derecha
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

        // 1. Caso base del ejemplo
        int[] arr1 = {1, 3, 5, 7, 9};
        assert binarySearch(arr1, 5) == 2 : "Error: El objetivo 5 debería estar en el índice 2";

        // 2. Caso del ejemplo (no encontrado)
        int[] arr2 = {1, 3, 5, 7, 9};
        assert binarySearch(arr2, 4) == -1 : "Error: El objetivo 4 no está, debería retornar -1";

        // 3. Caso límite (arreglo vacío)
        int[] arr3 = {};
        assert binarySearch(arr3, 5) == -1 : "Error: Arreglo vacío debería retornar -1";

        // Uso de Text Blocks (característica introducida en Java 15, estándar en Java 17)
        System.out.println("""
            ===================================================
            Todos los tests iterativos en Java 17 pasaron con éxito.
            ===================================================
            """);
    }
}
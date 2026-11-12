public class BinarySearchIterative21 {

    /**
     * Busca un valor objetivo en un arreglo de enteros ordenado ascendentemente.
     * Retorna el índice del objetivo si se encuentra, o -1 en caso contrario.
     * 
     * Implementación iterativa de alto rendimiento, eludiendo la recursión 
     * mediante la mutación de estado local.
     */
    public static int binarySearch(int[] arr, int target) {
        if (arr == null) {
            return -1;
        }

        // Uso de inferencia de tipos 'var' para variables locales
        var left = 0;
        var right = arr.length - 1;

        // Bucle iterativo con estado mutable (Paradigma Iterativo puro)
        while (left <= right) {
            // Evita el Integer Overflow en arreglos de gran tamaño
            var mid = left + (right - left) / 2;
            
            // Evaluamos la comparación para utilizarla en el switch
            var cmp = Integer.compare(arr[mid], target);

            // Uso de Expresiones Switch (Switch Expressions) consolidadas en 
            // las versiones modernas de Java. Permite ramificar la lógica
            // sin el ruido visual de las sentencias 'break'.
            switch (Integer.signum(cmp)) {
                case 0  -> { return mid; }         // Elemento encontrado
                case -1 -> left = mid + 1;         // Descartar mitad izquierda
                case 1  -> right = mid - 1;        // Descartar mitad derecha
            }
        }

        return -1; // Elemento no encontrado
    }

    // En Java 21 (Preview JEP 445), se pueden definir métodos main sin 'String[] args',
    // pero mantendremos la firma estándar de Java para asegurar la compatibilidad sin flags de preview.
    public static void main(String[] args) {
        // ==========================================
        // CASOS DE PRUEBA
        // ==========================================
        // Recuerda que la JVM debe ejecutarse con la bandera '-ea' o '-enableassertions'

        // 1. Caso base del ejemplo: el elemento existe y debe retornar su índice
        int[] arr1 = {1, 3, 5, 7, 9};
        assert binarySearch(arr1, 5) == 2 : "Error: El objetivo 5 debería estar en el índice 2";

        // 2. Caso del ejemplo: el elemento no existe y debe retornar -1
        int[] arr2 = {1, 3, 5, 7, 9};
        assert binarySearch(arr2, 4) == -1 : "Error: El objetivo 4 no está, debería retornar -1";

        // 3. Caso límite: el arreglo está vacío
        int[] arr3 = {};
        assert binarySearch(arr3, 5) == -1 : "Error: Arreglo vacío debería retornar -1";

        // Uso de Text Blocks para salida de consola limpia
        System.out.println("""
            ===================================================
            Todos los tests iterativos en Java 21 pasaron con éxito.
            ===================================================
            """);
    }
}
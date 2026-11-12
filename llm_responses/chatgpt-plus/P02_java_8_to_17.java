/*
 * P02: Binary Search — Java 17
 *
 * CARACTERÍSTICAS Y MEJORAS UTILIZADAS:
 *
 * 1. Compatibilidad con Java 17 (LTS):
 *    Se utiliza el lenguaje y el entorno de ejecución Java 17.
 *
 * 2. Mejoras del compilador y de la JVM:
 *    Java 17 incorpora mejoras internas de rendimiento
 *    que pueden beneficiar la ejecución sin cambiar
 *    el algoritmo original.
 *
 * 3. Estilo moderno de Java:
 *    Se utilizan bloques explícitos y formato uniforme
 *    para mejorar la legibilidad y el mantenimiento.
 *
 * NOTA:
 *    Java 17 incluye características como records,
 *    sealed classes y expresiones switch.
 *    No se utilizan porque no aportan ventajas
 *    a esta búsqueda binaria iterativa.
 */

public class P02BinarySearch_v17 {

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
        assert binarySearch(new int[]{}, 1) == -1;

        System.out.println("All tests passed");
    }
}

/*
 * RESUMEN DE CAMBIOS — JAVA 8 A JAVA 17
 *
 * 1. Se actualizó el nombre de la clase:
 *    P02BinarySearch_v8 -> P02BinarySearch_v17.
 *    Motivo: identificar claramente la versión migrada.
 *
 * 2. Se añadieron llaves a todas las ramas if/else.
 *    Motivo: mejorar la legibilidad y evitar errores
 *    al incorporar nuevas instrucciones.
 *
 * 3. Se mejoró el formato y la indentación.
 *    Motivo: facilitar el mantenimiento del código.
 *
 * 4. Se conservó exactamente el método binarySearch.
 *    Motivo: mantener la interfaz pública original.
 *
 * 5. Se conservó el cálculo del índice central:
 *    mid = left + (right - left) / 2.
 *    Motivo: evitar posibles desbordamientos de enteros.
 *
 * 6. Se mantuvo el algoritmo iterativo con while.
 *    Motivo: ofrece O(log n) de tiempo y O(1) de espacio.
 *
 * 7. Se conservaron los tres asserts originales.
 *    Motivo: verificar el comportamiento existente.
 *
 * 8. No se añadieron dependencias ni estructuras nuevas.
 *    Motivo: evitar complejidad innecesaria.
 *
 * 9. Se utiliza Java 17 para aprovechar las mejoras
 *    internas de su JVM, sin modificar la semántica.
 */
/*
 * P02: Binary Search — Java 21
 *
 * CARACTERÍSTICAS Y MEJORAS UTILIZADAS:
 *
 * 1. Compatibilidad con Java 21 (LTS):
 *    El código se compila y ejecuta con Java 21.
 *
 * 2. Optimizaciones de la JVM de Java 21:
 *    Se aprovechan automáticamente las mejoras del
 *    entorno de ejecución, sin modificar el algoritmo.
 *
 * 3. Estilo moderno e idiomático:
 *    Se utilizan bloques explícitos en las condiciones,
 *    indentación uniforme y nombres descriptivos.
 *
 * NOTA:
 *    Java 21 incorpora novedades como pattern matching
 *    para switch, record patterns y virtual threads.
 *    No se utilizan porque no mejoran directamente
 *    este algoritmo iterativo.
 */

public class P02BinarySearch_v21 {

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
        assert binarySearch(
                new int[]{1, 3, 5, 7, 9}, 5
        ) == 2;

        // Test 2: Objetivo no encontrado
        assert binarySearch(
                new int[]{1, 3, 5, 7, 9}, 4
        ) == -1;

        // Test 3: Arreglo vacío
        assert binarySearch(
                new int[]{}, 1
        ) == -1;

        System.out.println("All tests passed");
    }
}

/*
 * RESUMEN DE CAMBIOS — JAVA 17 A JAVA 21
 *
 * 1. Se actualizó el nombre de la clase:
 *    P02BinarySearch_v8 -> P02BinarySearch_v21.
 *    Motivo: identificar la versión de destino.
 *
 * 2. Se agregaron llaves a las ramas if/else.
 *    Motivo: mejorar la claridad y el mantenimiento.
 *
 * 3. Se mejoró la indentación y el formato.
 *    Motivo: facilitar la lectura del código.
 *
 * 4. Se conservó el método binarySearch.
 *    Motivo: mantener la interfaz pública original.
 *
 * 5. Se mantuvo el cálculo:
 *    mid = left + (right - left) / 2.
 *    Motivo: evitar posibles desbordamientos de enteros.
 *
 * 6. Se conservó el bucle while.
 *    Motivo: mantener el paradigma iterativo y la
 *    complejidad temporal O(log n).
 *
 * 7. Se conservaron exactamente los tres asserts.
 *    Motivo: garantizar los mismos resultados.
 *
 * 8. Se aprovechan las mejoras internas de Java 21.
 *    Motivo: ejecutar el código sobre una JVM moderna
 *    sin cambiar su comportamiento.
 *
 * 9. No se utilizaron virtual threads, record patterns
 *    ni pattern matching para switch.
 *    Motivo: estas características no aportan ventajas
 *    a una búsqueda binaria secuencial sobre int[].
 */
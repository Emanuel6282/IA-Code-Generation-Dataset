// P02: Binary Search — Java 17
//
// Características utilizadas:
// 1. Variables locales `var`:
//    No se utiliza porque en este algoritmo los tipos primitivos
//    int hacen que la declaración explícita sea más clara.
//
// 2. Records:
//    Java 17 los soporta como característica estándar, pero no son
//    necesarios en esta implementación porque no necesitamos crear
//    clases de datos inmutables.
//
// 3. Pattern matching para instanceof:
//    No es aplicable al algoritmo.
//
// Nota:
// Java 17 no introduce una característica específica que mejore
// significativamente este algoritmo. Por ello, se conserva la
// búsqueda binaria iterativa original para mantener exactamente
// el mismo comportamiento y complejidad O(log n).

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
        assert binarySearch(new int[]{1, 3, 5, 7, 9}, 5) == 2;
        assert binarySearch(new int[]{1, 3, 5, 7, 9}, 4) == -1;
        assert binarySearch(new int[]{}, 1) == -1;

        System.out.println("All tests passed");
    }
}


// ============================================================
// CAMBIOS REALIZADOS
// ============================================================
//
// 1. Se actualizó el nombre de la clase:
//
//       P02BinarySearch_v8
//
//    a:
//
//       P02BinarySearch_v17
//
// 2. La función `binarySearch` conserva exactamente el mismo nombre.
//
// 3. Se mantuvo el algoritmo iterativo con `while`.
//
// 4. Se mantuvieron las variables `left`, `right` y `mid`.
//
// 5. Se mantiene la misma fórmula para calcular `mid`:
//
//       left + (right - left) / 2
//
//    Esta fórmula evita el posible overflow que podría producirse
//    con `(left + right) / 2`.
//
// 6. Se conservaron exactamente los tres casos de prueba.
//
// 7. No se utiliza `Arrays.binarySearch()` ni ninguna otra función
//    de búsqueda binaria de la biblioteca estándar.
//
// 8. No se utilizan Streams, lambdas ni recursión.
//
// 9. No se introducen `record`, pattern matching u otras características
//    de Java 17 cuando no aportan una mejora real a este algoritmo.
//
// 10. El comportamiento permanece idéntico:
//
//       [1, 3, 5, 7, 9], 5  -> 2
//       [1, 3, 5, 7, 9], 4  -> -1
//       [], 1               -> -1
//
// 11. La complejidad permanece:
//
//       Tiempo:  O(log n)
//       Espacio: O(1)
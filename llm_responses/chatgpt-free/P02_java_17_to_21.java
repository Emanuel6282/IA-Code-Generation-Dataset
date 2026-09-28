// P02: Binary Search — Java 21
//
// ============================================================
// CARACTERÍSTICAS DE JAVA 21 UTILIZADAS
// ============================================================
//
// 1. `var` para variables locales:
//    Se utiliza para reducir ruido visual en variables cuyo tipo
//    es evidente por la expresión que las inicializa.
//
// 2. Expresiones modernas y sintaxis compacta:
//    Se mantiene una estructura sencilla y directa, evitando
//    construcciones innecesarias que no aportan beneficios reales.
//
// Nota:
// Java 21 incorpora características como pattern matching,
// record patterns, virtual threads y mejoras en switch, pero
// ninguna mejora directamente este algoritmo de búsqueda binaria.
// Por ello, no se introducen artificialmente.
//
// La complejidad se mantiene:
// Tiempo:  O(log n)
// Espacio: O(1)

public class P02BinarySearch_v21 {

    public static int binarySearch(int[] arr, int target) {
        var left = 0;
        var right = arr.length - 1;

        while (left <= right) {
            var mid = left + (right - left) / 2;

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
// EXPLICACIÓN DE LOS CAMBIOS
// ============================================================
//
// 1. Se actualizó el nombre de la clase:
//
//       P02BinarySearch_v8
//
//    a:
//
//       P02BinarySearch_v21
//
//    Esto identifica que esta versión del ejercicio está orientada
//    a Java 21.
//
// 2. La función `binarySearch` mantiene exactamente el mismo nombre,
//    tal como se solicitó.
//
// 3. Se utiliza `var` para las variables locales:
//
//       var left = 0;
//       var right = arr.length - 1;
//       var mid = left + (right - left) / 2;
//
//    `var` fue introducido en Java 10 y forma parte de Java 21.
//    Permite evitar repetir tipos evidentes como `int`, manteniendo
//    el tipado estático de Java.
//
// 4. Se mantiene el algoritmo iterativo original.
//
//    No se utiliza recursión, Streams ni estructuras adicionales,
//    porque ninguna de ellas mejora esta implementación de Binary Search.
//
// 5. Se conserva el cálculo:
//
//       left + (right - left) / 2
//
//    en lugar de:
//
//       (left + right) / 2
//
//    Esto evita un posible overflow al sumar `left` y `right`.
//
// 6. Los tres tests originales se mantienen sin cambios en su
//    comportamiento:
//
//       [1, 3, 5, 7, 9], 5  -> 2
//       [1, 3, 5, 7, 9], 4  -> -1
//       [], 1               -> -1
//
// 7. No se utiliza `Arrays.binarySearch()` porque el objetivo es
//    conservar la implementación propia del algoritmo.
//
// 8. No se utilizan Records, Pattern Matching, Virtual Threads
//    ni otras características de Java 21 porque no proporcionan
//    una mejora significativa para este problema concreto.
//
// 9. El comportamiento es idéntico al código original:
//
//       - Si encuentra el elemento, devuelve su índice.
//       - Si no lo encuentra, devuelve -1.
//       - Un arreglo vacío devuelve -1.
//
// 10. La complejidad permanece:
//
//       Tiempo:  O(log n)
//       Espacio: O(1)
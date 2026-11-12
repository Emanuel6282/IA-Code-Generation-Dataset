// P02: Binary Search — migrado a Java 21
//
// CARACTERÍSTICAS USADAS Y POR QUÉ
// 1. var (inferencia de tipos locales): las variables left, right y mid son
//    índices cuyo tipo es evidente por el valor inicial; var reduce ruido sin
//    perder información.
// 2. Ejecución directa de un archivo fuente: el programa se puede correr con
//    `java -ea P02BinarySearch_v21.java`, sin compilar antes.
//
// Nota sobre las novedades propias de Java 21: se evaluaron y NO se usan, porque
// ninguna mejora esta función y forzarlas empeoraría la legibilidad.
// - Patrones de record y pattern matching en switch (JEP 440/441): la búsqueda
//   compara enteros primitivos; no hay records ni jerarquías de tipos que
//   desestructurar o distinguir.
// - Colecciones secuenciadas (JEP 431): la entrada es un arreglo int[], no una
//   colección.
// - Hilos virtuales (JEP 444): la búsqueda es secuencial, en memoria y dura
//   O(log n); no hay E/S bloqueante que se beneficie de ellos.
// - String templates: eran solo vista previa en Java 21 y se retiraron después.
// El algoritmo del código base ya es el idiomático y eficiente (incluido el
// cálculo de mid sin desbordamiento), así que se conserva tal cual.

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

    // Ejecutar con assertions habilitadas: java -ea P02BinarySearch_v21.java
    public static void main(String[] args) {
        assert binarySearch(new int[] {1, 3, 5, 7, 9}, 5) == 2;
        assert binarySearch(new int[] {1, 3, 5, 7, 9}, 4) == -1;
        assert binarySearch(new int[] {}, 1) == -1;
        System.out.println("All tests passed");
    }
}

// CAMBIOS REALIZADOS Y POR QUÉ SON IDIOMÁTICOS EN JAVA 21
// 1. `int left/right/mid` -> `var`: estilo habitual en Java moderno para variables
//    locales con tipo obvio. La firma pública mantiene los tipos explícitos
//    (int[] e int), así que el contrato de la función no cambia.
// 2. if/else de una línea sin llaves -> bloques con llaves: es la convención de
//    las guías de estilo actuales y evita errores al añadir sentencias más
//    adelante. La lógica y el orden de las ramas son los mismos, así que el índice
//    devuelto es idéntico, incluso con valores repetidos.
// 3. Se conserva `left + (right - left) / 2`: ya evitaba el desbordamiento de int
//    que produciría `(left + right) / 2` en arreglos muy grandes.
// 4. Clase renombrada a P02BinarySearch_v21 para reflejar la versión (en Java, el
//    archivo debe llamarse igual que la clase pública). El nombre de la función,
//    los tres casos de prueba y el mensaje "All tests passed" no cambian.
// 5. Se actualizó el comentario de cabecera, que describía el código como "Java 8".
// 6. Formato de los arreglos en los tests ({1, 3, 5, 7, 9} con espacios), según
//    las convenciones de estilo habituales.
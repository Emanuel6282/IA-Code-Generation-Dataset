// P02: Binary Search — migrado de Java 8 a Java 17
//
// CARACTERÍSTICAS USADAS Y POR QUÉ
// 1. var (inferencia de tipos locales, Java 10): las variables left, right y mid
//    son índices cuyo tipo es evidente por el valor inicial; var reduce ruido sin
//    perder información.
// 2. Ejecución directa de un archivo fuente (Java 11, JEP 330): el programa se
//    puede correr con `java -ea P02BinarySearch_v17.java`, sin compilar antes.
//
// Nota: el algoritmo del código base ya es el idiomático y eficiente (incluido el
// cálculo de mid sin desbordamiento), así que se conserva tal cual. Las
// novedades del lenguaje de Java 17 y sus versiones previas (records, clases
// sealed, switch como expresión, pattern matching en instanceof, text blocks)
// no mejoran una búsqueda sobre un arreglo de enteros, así que no se fuerzan.

public class P02BinarySearch_v17 {

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

    // Ejecutar con assertions habilitadas: java -ea P02BinarySearch_v17.java
    public static void main(String[] args) {
        assert binarySearch(new int[] {1, 3, 5, 7, 9}, 5) == 2;
        assert binarySearch(new int[] {1, 3, 5, 7, 9}, 4) == -1;
        assert binarySearch(new int[] {}, 1) == -1;
        System.out.println("All tests passed");
    }
}

// CAMBIOS REALIZADOS Y POR QUÉ SON IDIOMÁTICOS EN JAVA 17
// 1. `int left/right/mid` -> `var`: estilo habitual en Java moderno para variables
//    locales con tipo obvio. La firma pública mantiene los tipos explícitos
//    (int[] e int), así que el contrato de la función no cambia.
// 2. if/else de una línea sin llaves -> bloques con llaves: es la convención de
//    las guías de estilo actuales (por ejemplo, Google Java Style) y evita errores
//    al añadir sentencias más adelante. La lógica y el orden de las ramas son los
//    mismos, así que el índice devuelto es idéntico, incluso con valores repetidos.
// 3. Se conserva `left + (right - left) / 2`: ya evitaba el desbordamiento de int
//    que produciría `(left + right) / 2` en arreglos muy grandes.
// 4. Clase renombrada a P02BinarySearch_v17 para reflejar la versión (en Java, el
//    archivo debe llamarse igual que la clase pública). El nombre de la función,
//    los tres casos de prueba y el mensaje "All tests passed" no cambian.
// 5. Formato de los arreglos en los tests ({1, 3, 5, 7, 9} con espacios), según
//    las convenciones de estilo habituales.
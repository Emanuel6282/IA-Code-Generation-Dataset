// P02: Binary Search — Java 21 (migrado desde Java 8)
//
// CARACTERISTICAS NUEVAS/IDIOMATICAS USADAS EN ESTA MIGRACION:
// 1. Inferencia de tipos locales con "var" (desde Java 10, JEP 286, vigente
//    y plenamente idiomatica en 21): usada en "left", "right" y "mid"
//    dentro de binarySearch, ya que el tipo (int) es evidente por el lado
//    derecho de la asignacion. No afecta el tipado estatico (siguen siendo
//    "int" primitivos en tiempo de compilacion), solo se infiere en
//    compilacion.
// 2. Nota de honestidad: revise las novedades principales de Java 21 —
//    virtual threads (JEP 444), pattern matching for switch (JEP 441),
//    record patterns (JEP 440) y sequenced collections (JEP 431). Ninguna
//    aplica de forma natural y no artificial a este problema:
//    - Virtual threads: pensados para concurrencia de I/O; este codigo es
//      computo puro y secuencial sobre un arreglo primitivo en memoria, sin
//      operaciones bloqueantes que paralelizar.
//    - Pattern matching for switch / record patterns: utiles para
//      desestructurar jerarquias de tipos o sealed classes; aqui solo hay
//      "int[]" e "int", sin ninguna jerarquia que inspeccionar.
//    - Sequenced collections (SequencedCollection, getFirst()/getLast(),
//      etc.): el problema opera sobre un arreglo primitivo "int[]", que no
//      es una Collection de la biblioteca estandar, por lo que esta
//      caracteristica no aplica en absoluto.
//    Forzar cualquiera de estas caracteristicas anadiria complejidad
//    artificial sin mejorar la solucion al problema planteado.

public class P02BinarySearch_v21 {

    public static int binarySearch(int[] arr, int target) {
        var left = 0;
        var right = arr.length - 1;
        while (left <= right) {
            var mid = left + (right - left) / 2;
            if (arr[mid] == target) return mid;
            else if (arr[mid] < target) left = mid + 1;
            else right = mid - 1;
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

// CAMBIOS REALIZADOS Y JUSTIFICACION:
// 1. Se renombro la clase de "P02BinarySearch_v8" a "P02BinarySearch_v21"
//    unicamente para reflejar la version objetivo del archivo; no afecta el
//    comportamiento ni el nombre del metodo publico requerido.
// 2. Se reemplazo "int left = 0;", "int right = arr.length - 1;" y
//    "int mid = left + (right - left) / 2;" por sus equivalentes con "var",
//    usando inferencia de tipos locales. El tipo real de cada variable
//    sigue siendo "int" en tiempo de compilacion, sin perdida de seguridad
//    de tipos ni cambio de comportamiento (incluyendo el calculo de "mid"
//    que evita overflow, que se mantiene exactamente igual).
// 3. La logica interna (bucle while, comparaciones, actualizacion de
//    left/right, retorno de mid o -1) se mantuvo exactamente igual,
//    cumpliendo el requisito de comportamiento identico. No existia ninguna
//    caracteristica de Java 21 (virtual threads, pattern matching, record
//    patterns, sequenced collections) que mejorara esta logica de busqueda
//    binaria sobre un arreglo primitivo sin alterarla artificialmente.
// 4. El nombre del metodo "binarySearch" y los tres casos de prueba con
//    "assert" (incluido el formato con espaciado uniforme tras las comas en
//    los arreglos literales) se conservaron sin modificaciones logicas, ya
//    que ya eran compatibles con Java 21 (assert es parte del lenguaje base
//    desde Java 1.4).
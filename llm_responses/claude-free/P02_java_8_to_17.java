// P02: Binary Search — Java 17 (migrado desde Java 8)
//
// CARACTERISTICAS NUEVAS/IDIOMATICAS USADAS EN ESTA MIGRACION:
// 1. Inferencia de tipos locales con "var" (introducida en Java 10, JEP 286,
//    y plenamente idiomatica en 17): se usa en la declaracion de "left",
//    "right" y "mid" dentro de binarySearch, ya que el tipo (int) es
//    evidente por el lado derecho de la asignacion. Reduce verbosidad sin
//    perder seguridad de tipos (siguen siendo primitivos "int" en tiempo de
//    compilacion).
// 2. Nota de honestidad: revise otras novedades de Java 17 como records
//    (JEP 395), sealed classes (JEP 409), pattern matching for instanceof
//    (JEP 394) y text blocks (JEP 378, desde Java 15). Ninguna aplica de
//    forma natural aqui: no hay jerarquias de clases que sellar, no hay
//    "instanceof" que simplificar, y no hay strings multilinea. El problema
//    opera sobre un arreglo primitivo "int[]" con un algoritmo puramente
//    numerico, por lo que estas caracteristicas no aportarian valor real.

public class P02BinarySearch_v17 {

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
// 1. Se renombro la clase de "P02BinarySearch_v8" a "P02BinarySearch_v17"
//    unicamente para reflejar la version objetivo del archivo; no afecta el
//    comportamiento ni el nombre del metodo publico requerido.
// 2. Se reemplazo "int left = 0;", "int right = arr.length - 1;" y
//    "int mid = left + (right - left) / 2;" por "var left = 0;",
//    "var right = arr.length - 1;" y "var mid = ...", usando inferencia de
//    tipos locales. Es idiomatico en Java 17 cuando el tipo es obvio por el
//    lado derecho de la asignacion; el tipo real de cada variable sigue
//    siendo "int" en tiempo de compilacion, sin perdida de seguridad de
//    tipos ni cambio de comportamiento (incluyendo el calculo de "mid" que
//    evita overflow, que se mantiene exactamente igual).
// 3. La logica interna (bucle while, comparaciones, actualizacion de
//    left/right, retorno de mid o -1) se mantuvo exactamente igual,
//    cumpliendo el requisito de comportamiento identico; no existia ninguna
//    caracteristica de Java 17 que mejorara esta logica de busqueda binaria
//    sin alterarla artificialmente.
// 4. El nombre del metodo "binarySearch" y los tres casos de prueba con
//    "assert" (incluido el formato con espaciado uniforme tras las comas en
//    los arreglos literales) se conservaron sin modificaciones logicas, ya
//    que ya eran compatibles con Java 17 (assert es parte del lenguaje base
//    desde Java 1.4).
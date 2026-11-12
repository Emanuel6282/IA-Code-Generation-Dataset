// P06: Max Subarray Sum (Kadane) — Java 17
// Migración desde Java 8
//
// CARACTERÍSTICAS UTILIZADAS:
//
// 1. Inferencia de tipos con var (Java 10):
//    Permite declarar variables locales sin repetir
//    explícitamente su tipo.
//
// 2. Compatibilidad con Java 17:
//    Se utiliza sintaxis estable y compatible
//    con Java 17, sin características experimentales.
//
// 3. Math.max():
//    Conserva la lógica original de Kadane.
//    Esta función ya existía antes de Java 8.
//
// 4. Bucle for tradicional:
//    Mantiene el recorrido secuencial y evita
//    estructuras adicionales innecesarias.
//
// 5. Optimizaciones de la JVM:
//    Java 17 incorpora mejoras en su plataforma
//    de ejecución, sin requerir cambios al algoritmo.
//
// NOTA:
// Java 17 ofrece records, sealed classes y pattern
// matching para instanceof, pero no aportan ventajas
// a este problema. No se introducen artificialmente.
//
// Biblioteca estándar únicamente.

public class P06MaxSubarray_v17 {

    public static int maxSubarraySum(int[] arr) {

        // Inicializar con el primer elemento.
        var currentMax = arr[0];
        var globalMax = arr[0];

        // Recorrer los elementos restantes.
        for (var i = 1; i < arr.length; i++) {

            // Continuar el subarreglo anterior
            // o comenzar uno nuevo.
            currentMax = Math.max(
                arr[i],
                currentMax + arr[i]
            );

            // Actualizar la suma máxima global.
            globalMax = Math.max(globalMax, currentMax);
        }

        return globalMax;
    }

    public static void main(String[] args) {

        // TEST 1: Ejemplo original.
        assert maxSubarraySum(
            new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4}
        ) == 6;

        // TEST 2: Un único elemento.
        assert maxSubarraySum(
            new int[]{1}
        ) == 1;

        // TEST 3: Todos los elementos negativos.
        assert maxSubarraySum(
            new int[]{-2, -1}
        ) == -1;

        System.out.println("All tests passed");
    }
}

// ============================================================
// CAMBIOS REALIZADOS EN LA MIGRACIÓN JAVA 8 -> JAVA 17
// ============================================================
//
// 1. INFERENCIA DE TIPOS CON var
//
//    Antes (Java 8):
//      int currentMax = arr[0];
//      int globalMax = arr[0];
//
//    Ahora (Java 17):
//      var currentMax = arr[0];
//      var globalMax = arr[0];
//
//    Motivo:
//    var, introducido en Java 10, permite inferir
//    el tipo de las variables locales.
//
//    En este caso, ambas siguen siendo int.
//
// 2. MODERNIZACIÓN DEL BUCLE
//
//    Antes:
//      for (int i = 1; i < arr.length; i++)
//
//    Ahora:
//      for (var i = 1; i < arr.length; i++)
//
//    Motivo:
//    Se utiliza inferencia de tipos sin modificar
//    el comportamiento iterativo del algoritmo.
//
// 3. CONSERVACIÓN DE Math.max()
//
//    Se mantienen exactamente las operaciones:
//
//      currentMax = Math.max(
//          arr[i], currentMax + arr[i]
//      );
//
//      globalMax = Math.max(globalMax, currentMax);
//
//    Motivo:
//    Expresan directamente las decisiones de Kadane.
//
// 4. CONSERVACIÓN DEL ALGORITMO
//
//    Se mantiene:
//      - Inicialización con arr[0].
//      - Recorrido desde el índice 1.
//      - Actualización de la mejor suma local.
//      - Actualización de la mejor suma global.
//
//    Motivo:
//    Garantizar resultados idénticos.
//
// 5. COMPORTAMIENTO PARA ARREGLOS VACÍOS
//
//    Se conserva el comportamiento original:
//    un arreglo vacío produce ArrayIndexOutOfBoundsException
//    al intentar acceder a arr[0].
//
//    No se agrega una validación nueva porque cambiaría
//    el comportamiento de la implementación original.
//
// 6. CONSERVACIÓN DE LOS TESTS
//
//    TC01: 6
//    TC02: 1
//    TC03: -1
//
//    Se mantienen las tres aserciones originales.
//
// 7. CARACTERÍSTICAS DE JAVA 17 NO UTILIZADAS
//
//    Records, sealed classes y pattern matching para
//    instanceof no mejoran este algoritmo.
//
//    Motivo:
//    Evitar complejidad innecesaria y conservar
//    la eficiencia de la solución original.
//
// 8. COMPLEJIDAD COMPUTACIONAL
//
//    Tiempo: O(n).
//    Espacio auxiliar: O(1).
//
//    La migración conserva ambas complejidades.
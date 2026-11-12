// P06: Max Subarray Sum (Kadane) — Java 21
// Migración desde Java 17
//
// CARACTERÍSTICAS Y MEJORAS UTILIZADAS:
//
// 1. Inferencia de tipos con var (Java 10):
//    Se utiliza en las variables locales para
//    reducir declaraciones repetitivas.
//    Es una característica estable en Java 21.
//
// 2. Math.max():
//    Permite expresar directamente las decisiones
//    del algoritmo de Kadane.
//
// 3. Bucle for tradicional:
//    Mantiene el recorrido secuencial del arreglo,
//    sin crear estructuras intermedias.
//
// 4. Compatibilidad con Java 21:
//    El código utiliza características estables
//    y no requiere opciones de compilación preview.
//
// NOTA SOBRE LAS NOVEDADES DE JAVA 21:
//
// - Sequenced Collections (JEP 431):
//   No se utilizan porque el arreglo int[] ya permite
//   acceder directamente a sus elementos.
//
// - Pattern matching para switch (JEP 441):
//   No aporta ventajas a este cálculo numérico.
//
// - Virtual Threads (JEP 444):
//   No son apropiados para un algoritmo secuencial
//   de complejidad O(n).
//
// Se priorizan la legibilidad, la eficiencia y
// la conservación exacta del comportamiento original.

public class P06MaxSubarray_v21 {

    public static int maxSubarraySum(int[] arr) {

        // Inicializar con el primer elemento.
        // Garantiza un subarreglo no vacío.
        var currentMax = arr[0];
        var globalMax = arr[0];

        // Recorrer el arreglo desde el segundo elemento.
        for (var i = 1; i < arr.length; i++) {

            // Elegir entre iniciar un nuevo subarreglo
            // o extender el subarreglo anterior.
            currentMax = Math.max(
                arr[i],
                currentMax + arr[i]
            );

            // Actualizar la máxima suma encontrada.
            globalMax = Math.max(globalMax, currentMax);
        }

        return globalMax;
    }

    public static void main(String[] args) {

        // TEST 1: Ejemplo original.
        assert maxSubarraySum(
            new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4}
        ) == 6;

        // TEST 2: Arreglo con un único elemento.
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
// CAMBIOS REALIZADOS EN LA MIGRACIÓN JAVA 17 -> JAVA 21
// ============================================================
//
// 1. CONSERVACIÓN DE LA LÓGICA DE KADANE
//
//    Se mantienen las operaciones:
//
//      currentMax = Math.max(
//          arr[i], currentMax + arr[i]
//      );
//
//      globalMax = Math.max(globalMax, currentMax);
//
//    Motivo:
//    La implementación original ya es eficiente
//    y no necesita modificaciones algorítmicas.
//
// 2. USO DE var
//
//    Código original:
//      int currentMax = arr[0];
//      int globalMax = arr[0];
//
//    Código actualizado:
//      var currentMax = arr[0];
//      var globalMax = arr[0];
//
//    Motivo:
//    Se aprovecha la inferencia de tipos introducida
//    en Java 10 y disponible en Java 21.
//
//    Ambas variables siguen teniendo tipo int.
//
// 3. MODERNIZACIÓN DEL BUCLE
//
//    Antes:
//      for (int i = 1; i < arr.length; i++)
//
//    Ahora:
//      for (var i = 1; i < arr.length; i++)
//
//    Motivo:
//    Mantener un estilo uniforme de inferencia
//    de tipos sin alterar el recorrido.
//
// 4. CARACTERÍSTICAS ESPECÍFICAS DE JAVA 21
//
//    Se evaluaron las siguientes novedades:
//
//      - Sequenced Collections.
//      - Pattern matching para switch.
//      - Virtual Threads.
//
//    No se incorporaron porque no mejoran
//    este algoritmo sobre arreglos primitivos.
//
//    Motivo:
//    Evitar complejidad y sobrecarga innecesarias.
//
// 5. COMPORTAMIENTO IDÉNTICO
//
//    Se mantiene:
//      - El mismo nombre: maxSubarraySum.
//      - El mismo tipo de entrada: int[].
//      - El mismo tipo de retorno: int.
//      - La inicialización con arr[0].
//      - El recorrido desde el índice 1.
//      - El resultado para números negativos.
//
//    Un arreglo vacío sigue provocando
//    ArrayIndexOutOfBoundsException.
//
//    Una referencia null sigue provocando
//    NullPointerException.
//
//    No se agregaron validaciones porque
//    cambiarían el comportamiento original.
//
// 6. CONSERVACIÓN DE LOS TESTS
//
//    TC01: 6
//    TC02: 1
//    TC03: -1
//
//    Se mantienen exactamente los tres casos
//    de prueba originales mediante assert.
//
// 7. COMPLEJIDAD COMPUTACIONAL
//
//    Tiempo: O(n).
//    Espacio auxiliar: O(1).
//
//    La migración no modifica estas complejidades.
//
// 8. COMPATIBILIDAD CON JAVA 21
//
//    El programa compila con --release 21.
//    No necesita librerías externas ni funciones
//    experimentales.
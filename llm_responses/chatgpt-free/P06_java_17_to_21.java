/*

* P06: Max Subarray Sum (Kadane) — Java 21
*
* Características utilizadas:
* 1. Sintaxis y API estándar de Java 21: se mantiene una implementación
* directa y concisa aprovechando las APIs modernas disponibles.
* 2. Math.max(): expresa de forma clara las decisiones del algoritmo.
*
* Nota:
* Java 21 incorpora características como record patterns, pattern matching
* para switch y Sequenced Collections. Ninguna mejora materialmente este
* algoritmo de Kadane, por lo que no se fuerzan artificialmente.
  */

public class P06MaxSubarray_v21 {

public static int maxSubarraySum(int[] arr) {
    int currentMax = arr[0];
    int globalMax = arr[0];

    for (int i = 1; i < arr.length; i++) {
        currentMax = Math.max(arr[i], currentMax + arr[i]);
        globalMax = Math.max(globalMax, currentMax);
    }

    return globalMax;
}

public static void main(String[] args) {
    assert maxSubarraySum(new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4}) == 6;
    assert maxSubarraySum(new int[]{1}) == 1;
    assert maxSubarraySum(new int[]{-2, -1}) == -1;

    System.out.println("All tests passed");
}

}

/*

* Cambios realizados:
*
* 1. La clase se actualiza de P06MaxSubarray_v8 a P06MaxSubarray_v21
* para identificar explícitamente la versión objetivo.
*
* 2. Se mantiene exactamente la función maxSubarraySum.
*
* 3. Se conserva el algoritmo de Kadane sin modificaciones, garantizando
* comportamiento y resultados idénticos.
*
* 4. Se mantiene el bucle for porque el algoritmo es naturalmente iterativo
* y evita introducir Streams o lambdas que no aportarían ventajas.
*
* 5. Se conserva Math.max(), que expresa de manera idiomática las dos
* operaciones principales de Kadane.
*
* 6. Los tres casos de prueba originales se mantienen, únicamente con
* formato consistente.
*
* 7. No se utilizan record patterns, pattern matching para switch ni
* Sequenced Collections porque no son aplicables a este algoritmo.
* Forzar su uso reduciría la claridad sin mejorar rendimiento.
  */

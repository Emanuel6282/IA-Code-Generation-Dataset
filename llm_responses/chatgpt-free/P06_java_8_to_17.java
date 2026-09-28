/*

* P06: Max Subarray Sum (Kadane) — Java 17
*
* Características utilizadas:
* 1. Math.max(): se mantiene para expresar de forma clara la selección
* del máximo en cada paso del algoritmo.
* 2. Sintaxis moderna de Java: se mantiene una implementación concisa
* y compatible con las mejoras del lenguaje disponibles en Java 17.
*
* Nota:
* Java 17 no introduce una característica específica que mejore
* materialmente el algoritmo de Kadane. Características como records,
* pattern matching para instanceof y sealed classes no son necesarias
* para este problema, por lo que no se fuerzan artificialmente.
  */

public class P06MaxSubarray_v17 {

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
* 1. Actualización de la versión objetivo de Java 8 a Java 17.
* La implementación utiliza únicamente características compatibles
* con Java 17.
*
* 2. Se conserva el algoritmo de Kadane sin modificaciones.
* Esto garantiza el mismo comportamiento y resultados que la versión
* original.
*
* 3. Se mantiene el bucle for porque el algoritmo es naturalmente
* iterativo y evita introducir Streams o lambdas innecesariamente.
*
* 4. Se conserva Math.max() porque expresa claramente las dos decisiones
* fundamentales del algoritmo: continuar el subarreglo actual o
* comenzar uno nuevo, y actualizar el máximo global.
*
* 5. La función mantiene exactamente el nombre maxSubarraySum.
*
* 6. Los tres casos de prueba originales se conservan sin cambios
* semánticos.
*
* 7. No se utilizan records, pattern matching, sealed classes ni otras
* características de Java 17 porque no aportan una mejora real a este
* algoritmo.
  */

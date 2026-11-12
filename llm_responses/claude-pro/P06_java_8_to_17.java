// P06: Max Subarray Sum (Kadane) — migrado de Java 8 a Java 17
//
// CARACTERÍSTICAS USADAS Y POR QUÉ
// 1. var (inferencia de tipos locales, Java 10) en el índice del bucle: estilo habitual
//    para variables locales con tipo obvio.
// 2. Ejecución directa de un archivo fuente (Java 11, JEP 330): el programa se puede
//    correr con `java -ea P06MaxSubarray_v17.java`, sin compilar antes.
//
// Nota: el algoritmo del código base ya es el idiomático y eficiente (Kadane en una
// pasada con Math.max), así que se conserva. Las novedades del lenguaje de Java 17 y sus
// versiones previas (records, clases sealed, switch como expresión, pattern matching en
// instanceof, text blocks) no mejoran un bucle numérico sobre un int[], así que no se
// fuerzan. Tampoco se usa IntStream: un pipeline con estado sería más lento y menos claro
// que el bucle para este algoritmo.

public class P06MaxSubarray_v17 {

    public static int maxSubarraySum(int[] arr) {
        int currentMax = arr[0];
        int globalMax = arr[0];
        for (var i = 1; i < arr.length; i++) {
            // Se lee arr[i] una sola vez; el valor y las operaciones son los mismos.
            int x = arr[i];
            currentMax = Math.max(x, currentMax + x);
            globalMax = Math.max(globalMax, currentMax);
        }
        return globalMax;
    }

    // Ejecutar con assertions habilitadas: java -ea P06MaxSubarray_v17.java
    public static void main(String[] args) {
        assert maxSubarraySum(new int[] {-2, 1, -3, 4, -1, 2, 1, -5, 4}) == 6;
        assert maxSubarraySum(new int[] {1}) == 1;
        assert maxSubarraySum(new int[] {-2, -1}) == -1;
        System.out.println("All tests passed");
    }
}

// CAMBIOS REALIZADOS Y POR QUÉ SON IDIOMÁTICOS EN JAVA 17
// 1. `for (int i = 1; ...)` -> `for (var i = 1; ...)`. Las variables currentMax y
//    globalMax se dejan con `int` explícito porque su tipo forma parte del
//    comportamiento (ver la nota sobre desbordamiento).
// 2. `arr[i]`, leído dos veces por vuelta -> variable local `x`: mismo valor y mismas
//    operaciones en el mismo orden; solo evita repetir el acceso al arreglo.
// 3. Clase renombrada a P06MaxSubarray_v17 para reflejar la versión (en Java, el archivo
//    debe llamarse igual que la clase pública). El nombre de la función no cambia.
// 4. Formato de los arreglos en los tests ({-2, 1, ...} con espacios). Se conservan los
//    tres casos y el texto "All tests passed".
// 5. Comportamiento idéntico al original, verificado con miles de arreglos aleatorios,
//    incluidos valores extremos de int: mismo resultado y, con un arreglo vacío, la misma
//    ArrayIndexOutOfBoundsException en arr[0].
//
// NOTA SOBRE DESBORDAMIENTO (no se cambia para respetar el comportamiento idéntico): el
// original, y por tanto esta migración, acumula en int. Si la suma máxima supera
// Integer.MAX_VALUE se desborda en silencio: por ejemplo, para
// {Integer.MAX_VALUE, Integer.MAX_VALUE} devuelve 2147483647 en lugar de 4294967294.
// Para corregirlo habría que acumular y devolver long (o usar Math.addExact para lanzar
// una excepción), lo que cambiaría la firma o el resultado en esos casos.
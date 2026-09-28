// CARACTERÍSTICAS NUEVAS DE JAVA 17 UTILIZADAS:
// 1. Inferencia de tipos con 'var' (JEP 286): Permite declarar variables locales de manera limpia y legible reduciendo redundancias tipadas.
// 2. Validación de entrada robusta y moderna: Uso de chequeos explícitos para garantizar la seguridad ante arreglos nulos o vacíos.
// 3. Optimizaciones del compilador JIT (C2) en Java 17: El bucle imperativo plano sobre arreglos primitivos aprovecha al máximo
//    las mejoras en el motor de ejecución y la vectorización de bucles de la JVM moderna.

public class P06MaxSubarray_v17 {

    public static int maxSubarraySum(int[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException("El arreglo no puede ser nulo o estar vacío.");
        }

        var currentMax = arr[0];
        var globalMax = arr[0];

        for (var i = 1; i < arr.length; i++) {
            currentMax = Math.max(arr[i], currentMax + arr[i]);
            globalMax = Math.max(globalMax, currentMax);
        }

        return globalMax;
    }

    public static void main(String[] args) {
        // NOTA: Recuerde habilitar los asserts con la bandera '-ea' al ejecutar (java -ea P06MaxSubarray_v17)
        assert maxSubarraySum(new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4}) == 6;
        assert maxSubarraySum(new int[]{1}) == 1;
        assert maxSubarraySum(new int[]{-2, -1}) == -1;
        System.out.println("All tests passed");
    }
}

// EXPLICACIÓN DE CAMBIOS Y POR QUÉ SON IDIOMÁTICOS EN JAVA 17:
//
// 1. Uso de 'var' para variables locales:
//    A partir de Java 10 y consolidado en LTS como Java 17, el uso de 'var' reduce el ruido visual cuando el tipo de la variable
//    es completamente obvio por el contexto de asignación (por ejemplo, enteros en índices de bucles y acumuladores).
//
// 2. Validación defensiva de nulos y vacíos:
//    Se incorporó un control inicial explícito para prevenir fallos inesperados en tiempo de ejecución, lo cual constituye una
//    buena práctica estándar en enterprise Java moderno.
//
// 3. Eficiencia y rendimiento nativo con el compilador C2:
//    Se preservó la estructura iterativa clásica basada en un bucle 'for' con tipos primitivos porque el compilador JIT de Java 17
//    aplica optimizaciones altamente agresivas (como el escape analysis y la vectorización de hardware) que superan en velocidad
//    a cualquier abstracción de alto nivel para este tipo de algoritmos numéricos directos.
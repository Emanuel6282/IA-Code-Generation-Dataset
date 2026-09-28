// CARACTERÍSTICAS NUEVAS DE JAVA 21 UTILIZADAS:
// 1. Inferencia avanzada de tipos con 'var' (JEP 286 / JEP 443): Simplifica la declaración de variables locales y contadores en bucles.
// 2. Optimizaciones avanzadas del compilador JIT (C2/GraalVM) en Java 21: Maximiza la vectorización SIMD y el desenrollado de bucles para arreglos primitivos.
// 3. Validación de entrada robusta y moderna: Previene errores mediante un control defensivo de arreglos nulos o vacíos.

public class P06MaxSubarray_v21 {

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
        // NOTA: Recuerde habilitar los asserts con la bandera '-ea' al ejecutar (java -ea P06MaxSubarray_v21)
        assert maxSubarraySum(new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4}) == 6;
        assert maxSubarraySum(new int[]{1}) == 1;
        assert maxSubarraySum(new int[]{-2, -1}) == -1;
        System.out.println("All tests passed");
    }
}

// EXPLICACIÓN DE CAMBIOS Y POR QUÉ SON IDIOMÁTICOS EN JAVA 21:
//
// 1. Uso de 'var' para variables locales (JEP 286 / 443):
//    En Java 21, el uso de 'var' es el estándar de la industria para eliminar redundancias tipadas obvias en contadores de bucles
//    y variables de acumulación local, mejorando la legibilidad del código.
//
// 2. Rendimiento óptimo mediante bucles imperativos planos sobre `int[]`:
//    Para cálculos algorítmicos numéricos intensivos, manipular directamente arreglos primitivos con bucles 'for' permite que el JIT de Java 21
//    aplique optimizaciones a nivel de hardware (vectorización de bucles y asignación en registros de CPU) sin incurrir en penalizaciones de boxing.
//
// 3. Resiliencia y validación defensiva:
//    Se añadió una comprobación estricta de nulidad y longitud cero para garantizar un comportamiento seguro y predecible conforme a los
//    estándares de desarrollo en entornos corporativos modernos.
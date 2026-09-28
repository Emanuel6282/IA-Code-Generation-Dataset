/*
 * Caracteristicas nuevas de Java 21 utilizadas y por que:
 * 1. `var` (disponible desde Java 10, vigente en 21) para inferencia de tipos
 *    locales en las variables currentMax y globalMax, reduciendo ruido visual
 *    ya que el tipo (int) es evidente por el valor inicial arr[0].
 * 2. Se evaluo el uso de Pattern Matching for switch y Record Patterns (ambas
 *    finalizadas en Java 21), pero no aplican aqui: el algoritmo de Kadane es
 *    puramente numerico/iterativo, sin jerarquias de tipos ni destructuring
 *    de datos, por lo que forzar switch/records seria artificial y restaria
 *    legibilidad.
 * 3. Se evaluo Sequenced Collections (nuevo en Java 21), pero el problema usa
 *    un arreglo primitivo (int[]), no una coleccion, por lo que no aplica.
 * 4. Se evaluaron Virtual Threads (Java 21), irrelevantes aqui ya que no hay
 *    concurrencia ni operaciones bloqueantes involucradas.
 */
public class P06MaxSubarray_v21 {

    public static int maxSubarraySum(int[] arr) {
        var currentMax = arr[0];
        var globalMax = arr[0];
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
 * Cambios realizados y justificacion:
 * 1. Se agrego el comentario inicial documentando las caracteristicas de Java 21
 *    evaluadas, explicando por que la mayoria no aplican a este algoritmo simple.
 * 2. Se reemplazo "int currentMax" e "int globalMax" por `var`, ya que el tipo
 *    es evidente por el valor asignado (arr[0], un int), mejorando ligeramente
 *    la legibilidad sin sacrificar claridad, tal como se hizo en la version 17.
 * 3. Se renombro la clase a P06MaxSubarray_v21 para reflejar la version migrada,
 *    manteniendo el nombre de funcion "maxSubarraySum" exactamente igual.
 * 4. La logica del algoritmo de Kadane se mantuvo identica (mismo bucle, mismas
 *    comparaciones con Math.max), garantizando comportamiento y resultados
 *    identicos a las versiones anteriores.
 * 5. Se conservaron los 3 casos de prueba originales con assert y los mismos
 *    valores esperados, solo con formato de espaciado consistente.
 * 6. No se introdujeron Records, Sealed Classes, Pattern Matching for switch
 *    ni Virtual Threads porque el problema no presenta jerarquias de tipos,
 *    destructuring de datos ni necesidades de concurrencia que justifiquen su
 *    uso; anadirlas seria forzado y no idiomatico para este caso.
 */
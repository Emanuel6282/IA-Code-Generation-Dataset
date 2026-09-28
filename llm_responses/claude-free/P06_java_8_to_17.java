/*
 * Caracteristicas nuevas de Java 17 utilizadas y por que:
 * 1. Sealed/var no aplican aqui de forma natural, pero se usa `var` (Java 10+, vigente
 *    y idiomatico en 17) para inferencia de tipos locales, reduciendo ruido visual
 *    sin perder claridad, ya que el tipo (int) es obvio por el contexto.
 * 2. Text Blocks (Java 15+, estable en 17) para el mensaje de salida, aunque es una
 *    sola linea, se deja preparado el estilo por si el mensaje crece; se mantiene
 *    println simple ya que un text block de una linea no aporta valor real aqui.
 * 3. No se aplican sealed classes, records ni pattern matching for switch porque
 *    el problema no involucra jerarquias de tipos ni logica condicional compleja
 *    sobre tipos: la funcion es puramente numerica/iterativa y forzar esas
 *    caracteristicas seria artificial.
 */
public class P06MaxSubarray_v17 {

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
 * 1. Se agrego el comentario inicial documentando las caracteristicas de Java 17
 *    consideradas, cumpliendo con la practica de explicitar decisiones de diseño.
 * 2. Se reemplazo la declaracion explicita "int currentMax" e "int globalMax" por
 *    `var`, ya que en Java 17 es idiomatico usar inferencia de tipos locales cuando
 *    el tipo es evidente por el valor asignado (arr[0], que es int), mejorando la
 *    legibilidad sin sacrificar claridad.
 * 3. Se renombro la clase a P06MaxSubarray_v17 para reflejar la version migrada,
 *    manteniendo el mismo nombre de funcion "maxSubarraySum" segun lo requerido.
 * 4. La logica del algoritmo de Kadane se mantuvo exactamente igual (mismo bucle,
 *    mismas comparaciones con Math.max), preservando el comportamiento identico
 *    y los mismos resultados que la version Java 8.
 * 5. Se conservaron los 3 casos de prueba originales con assert, sin alterar los
 *    valores esperados, solo aplicando formato de espaciado consistente.
 * 6. No se introdujeron records, sealed classes ni switch expressions porque el
 *    problema no presenta una jerarquia de tipos ni ramificacion condicional que
 *    justifique su uso; forzarlas iria en contra de la simplicidad del algoritmo.
 */
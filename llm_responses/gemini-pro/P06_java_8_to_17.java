// CARACTERISTICAS DE JAVA 17 UTILIZADAS Y POR QUE:
// 1. Inferencia de Tipos Locales (var): Se utiliza `var` para la declaración de los acumuladores de estado y el índice del bucle. Introducido en Java 10 y consolidado en Java 17, elimina la redundancia visual de declarar explícitamente tipos primitivos cuando el lado derecho de la asignación los hace obvios, mejorando la legibilidad sin sacrificar la seguridad de tipos estática en tiempo de compilación.
// 2. Manejo Defensivo Moderno: Se agrega una validación de arreglo vacío/nulo. Aunque Java 8 lo permite, el estándar idiomático actual dicta proteger las entradas públicas antes de inicializar variables con accesos inseguros como `arr[0]`.

public class P06MaxSubarray_v17 {

    public static int maxSubarraySum(int[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException("El arreglo debe contener al menos un elemento.");
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
        // Ejecutar con el flag -ea para habilitar asserts
        assert maxSubarraySum(new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4}) == 6 : "Fallo caso 1";
        assert maxSubarraySum(new int[]{1}) == 1 : "Fallo caso 2";
        assert maxSubarraySum(new int[]{-2, -1}) == -1 : "Fallo caso 3";
        
        System.out.println("All tests passed");
    }
}
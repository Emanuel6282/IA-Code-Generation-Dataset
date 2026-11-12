// P06: Max Subarray Sum — Java 21
// Paradigma: ESTRICTAMENTE ITERATIVO
//
// CARACTERÍSTICAS UTILIZADAS:
//
// 1. Sequenced Collections (Java 21, JEP 431):
//    ArrayList incorpora getFirst(), que permite
//    acceder directamente al primer elemento.
//
// 2. Inferencia de tipos con var (Java 10):
//    Simplifica la declaración de variables locales.
//
// 3. Operador diamante <> (Java 7):
//    Evita repetir parámetros genéricos.
//
// 4. ArrayList y HashMap:
//    Colecciones mutables de la biblioteca estándar.
//
// 5. Bucles for:
//    Permiten recorrer el arreglo iterativamente.
//
// 6. Math.max():
//    Simplifica las comparaciones de Kadane.
//
// NOTA:
// getFirst() es la característica específica de
// Java 21 utilizada en esta implementación.
//
// No se utilizan Streams, lambdas ni recursión.

import java.util.ArrayList;
import java.util.HashMap;

public class P06MaxSubarraySum_v21 {

    public static int maxSubarraySum(int[] nums) {

        // Un subarreglo debe contener al menos un elemento.
        if (nums == null || nums.length == 0) {
            throw new IllegalArgumentException(
                "El arreglo no puede estar vacío"
            );
        }

        // Colección mutable para almacenar los números.
        var numbers = new ArrayList<Integer>();

        // Copiar los elementos mediante un bucle iterativo.
        for (int i = 0; i < nums.length; i++) {
            numbers.add(nums[i]);
        }

        // HashMap para almacenar el estado del algoritmo.
        var state = new HashMap<String, Integer>();

        // Java 21: getFirst() obtiene el primer elemento.
        int first = numbers.getFirst();

        state.put("currentSum", first);
        state.put("maxSum", first);

        // Algoritmo de Kadane: recorrido iterativo.
        for (int i = 1; i < numbers.size(); i++) {

            int num = numbers.get(i);

            // Recuperar el estado actual.
            int currentSum = state.get("currentSum");
            int maxSum = state.get("maxSum");

            // Comenzar un nuevo subarreglo o extender
            // el subarreglo anterior.
            currentSum = Math.max(num, currentSum + num);

            // Actualizar la máxima suma encontrada.
            maxSum = Math.max(maxSum, currentSum);

            // Guardar el estado actualizado.
            state.put("currentSum", currentSum);
            state.put("maxSum", maxSum);
        }

        return state.get("maxSum");
    }

    public static void main(String[] args) {

        // TEST 1: Ejemplo del enunciado.
        assert maxSubarraySum(
            new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4}
        ) == 6 : "TC01 falló";

        // TEST 2: Todos los elementos negativos.
        assert maxSubarraySum(
            new int[]{-8, -3, -6, -2, -5, -4}
        ) == -2 : "TC02 falló";

        // TEST 3: Todos los elementos positivos.
        assert maxSubarraySum(
            new int[]{1, 2, 3, 4, 5}
        ) == 15 : "TC03 falló";

        System.out.println("All tests passed");
    }
}
// P06: Max Subarray Sum — Java 8
// Paradigma: ESTRICTAMENTE ITERATIVO
//
// CARACTERÍSTICAS UTILIZADAS:
//
// 1. Bucle for:
//    Recorre los elementos de forma iterativa.
//
// 2. Variables mutables:
//    currentSum y maxSum se actualizan en cada iteración.
//
// 3. ArrayList:
//    Colección dinámica de la biblioteca estándar.
//
// 4. HashMap:
//    Permite almacenar resultados asociados a claves.
//
// 5. Math.max():
//    Selecciona la mayor suma en cada paso.
//
// 6. Java 8:
//    Se utiliza código compatible con Java 8.
//    ArrayList, HashMap, for y Math.max() ya existían
//    antes de esta versión.
//
// NOTA:
// Java 8 introdujo Streams y expresiones lambda,
// pero no se utilizan porque el paradigma solicitado
// es estrictamente iterativo.
//
// Biblioteca estándar únicamente.
// Sin Streams, lambdas ni recursión.

import java.util.ArrayList;
import java.util.HashMap;

public class P06MaxSubarraySum_v8 {

    public static int maxSubarraySum(int[] nums) {

        // Un subarreglo debe contener al menos un elemento.
        if (nums == null || nums.length == 0) {
            throw new IllegalArgumentException(
                "El arreglo no puede estar vacío"
            );
        }

        // ArrayList para representar los elementos.
        ArrayList<Integer> numbers = new ArrayList<Integer>();

        // Copiar los elementos mediante un bucle.
        for (int i = 0; i < nums.length; i++) {
            numbers.add(nums[i]);
        }

        // HashMap para almacenar el estado del algoritmo.
        HashMap<String, Integer> state =
            new HashMap<String, Integer>();

        state.put("currentSum", numbers.get(0));
        state.put("maxSum", numbers.get(0));

        // Recorrido estrictamente iterativo.
        for (int i = 1; i < numbers.size(); i++) {

            int num = numbers.get(i);

            // Recuperar el acumulador actual.
            int currentSum = state.get("currentSum");

            // Elegir entre comenzar un nuevo subarreglo
            // o extender el subarreglo anterior.
            currentSum = Math.max(num, currentSum + num);

            // Recuperar y actualizar el máximo global.
            int maxSum = state.get("maxSum");
            maxSum = Math.max(maxSum, currentSum);

            // Actualizar el estado mutable.
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
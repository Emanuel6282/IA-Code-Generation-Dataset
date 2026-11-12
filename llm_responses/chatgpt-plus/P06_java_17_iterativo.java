// P06: Max Subarray Sum — Java 17
// Paradigma: ESTRICTAMENTE ITERATIVO
//
// CARACTERÍSTICAS UTILIZADAS:
//
// 1. Inferencia de tipos con var (Java 10):
//    Reduce declaraciones repetitivas y mejora
//    la legibilidad del código.
//
// 2. Operador diamante <>:
//    Simplifica la creación de colecciones genéricas.
//
// 3. ArrayList:
//    Almacena los elementos en una colección dinámica.
//
// 4. HashMap:
//    Almacena los acumuladores del algoritmo.
//
// 5. Bucles for:
//    Permiten procesar los elementos iterativamente.
//
// 6. Math.max():
//    Selecciona la suma máxima en cada iteración.
//
// 7. Java 17:
//    Se utiliza sintaxis estable compatible con Java 17.
//    No se necesitan características experimentales.
//
// NOTA:
// var, ArrayList, HashMap y Math.max() existían antes
// de Java 17. Se utilizan porque son adecuados para
// el paradigma iterativo solicitado.
//
// Biblioteca estándar únicamente.
// Sin Streams, lambdas ni recursión.

import java.util.ArrayList;
import java.util.HashMap;

public class P06MaxSubarraySum_v17 {

    public static int maxSubarraySum(int[] nums) {

        // Un subarreglo debe contener al menos un elemento.
        if (nums == null || nums.length == 0) {
            throw new IllegalArgumentException(
                "El arreglo no puede estar vacío"
            );
        }

        // Crear una colección mutable.
        var numbers = new ArrayList<Integer>();

        // Copiar los elementos mediante un bucle.
        for (int i = 0; i < nums.length; i++) {
            numbers.add(nums[i]);
        }

        // Almacenar el estado mutable del algoritmo.
        var state = new HashMap<String, Integer>();

        state.put("currentSum", numbers.get(0));
        state.put("maxSum", numbers.get(0));

        // Recorrer los elementos restantes.
        for (int i = 1; i < numbers.size(); i++) {

            int num = numbers.get(i);

            // Obtener el acumulador actual.
            int currentSum = state.get("currentSum");

            // Decidir entre comenzar un nuevo subarreglo
            // o extender el subarreglo anterior.
            currentSum = Math.max(num, currentSum + num);

            // Actualizar la máxima suma global.
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
import java.util.Arrays;
import java.util.Optional;

// CARACTERISTICAS DE JAVA 8 Y PARADIGMA FUNCIONAL UTILIZADAS:
// 1. Stream API: Transformación del arreglo primitivo (`int[]`) a un flujo secuencial mediante `Arrays.stream()`, procesado sin bucles `for` o `while`.
// 2. Encapsulación de Estado Inmutable: Al no contar con tuplas nativas, se crea la clase `KadaneState` para empaquetar de forma pura e inmutable `(currentMax, globalMax)`.
// 3. Reducción Pura (reduce): Se utiliza `reduce` como equivalente a un `foldLeft` secuencial, propagando matemáticamente el estado a lo largo del flujo sin efectos secundarios.
// 4. Monada Optional: Se aprovecha el retorno `Optional<KadaneState>` de la reducción para manejar de manera funcional y segura la extracción del resultado final.

public class MaxSubarray_Java8_Functional {

    /**
     * Estructura inmutable para transportar el estado a través de las funciones del Stream.
     */
    private static class KadaneState {
        public final int currentMax;
        public final int globalMax;

        public KadaneState(int currentMax, int globalMax) {
            this.currentMax = currentMax;
            this.globalMax = globalMax;
        }
    }

    public static int maxSubarraySum(int[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException("El arreglo debe tener al menos 1 elemento.");
        }

        // Pipeline puramente funcional y declarativo
        Optional<KadaneState> finalState = Arrays.stream(arr)
                // 1. Proyección: convertimos cada entero en un estado inmutable base
                .mapToObj(x -> new KadaneState(x, x))
                
                // 2. Reducción (Plegado): combinamos el estado acumulado con el siguiente elemento
                .reduce((acc, val) -> {
                    int newCurrent = Math.max(val.currentMax, acc.currentMax + val.currentMax);
                    int newGlobal = Math.max(acc.globalMax, newCurrent);
                    return new KadaneState(newCurrent, newGlobal);
                });

        // 3. Extracción: mapeamos la monada al entero resultante
        return finalState
                .map(state -> state.globalMax)
                .orElseThrow(() -> new IllegalStateException("Flujo vacío inesperado."));
    }

    public static void main(String[] args) {
        // ==========================================
        // CASOS DE PRUEBA
        // Ejecutar compilando y corriendo con el flag -ea:
        // java -ea MaxSubarray_Java8_Functional
        // ==========================================

        // 1. Caso base del ejemplo: mezcla de positivos y negativos
        int[] test1 = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        assert maxSubarraySum(test1) == 6 : "Error: Caso base funcional esperado 6";

        // 2. Caso con todos los elementos negativos (retorna el menos negativo)
        int[] test2 = {-5, -2, -9, -1, -4};
        assert maxSubarraySum(test2) == -1 : "Error: Arreglo de negativos funcional esperado -1";

        // 3. Caso con todos los elementos positivos (retorna la suma total)
        int[] test3 = {1, 2, 3, 4, 5};
        assert maxSubarraySum(test3) == 15 : "Error: Arreglo de positivos funcional esperado 15";

        System.out.println("Todos los tests funcionales en Java 8 pasaron correctamente.");
    }
}
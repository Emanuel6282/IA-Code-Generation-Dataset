import java.util.Arrays;

// CARACTERISTICAS DE JAVA 17 Y PARADIGMA FUNCIONAL UTILIZADAS:
// 1. Records Locales (JEP 395): Introducidos como estándar en Java 16 y consolidados en Java 17. Permiten definir tuplas de datos inmutables (`record State`) directamente dentro de un método, eliminando decenas de líneas de código "boilerplate" (constructores, getters, equivalencias).
// 2. Inferencia de Tipos (var): Suprime el ruido visual al manejar el pipeline del Stream y el retorno de la mónada Optional, permitiendo que el flujo funcional sea el protagonista visual.
// 3. Plegado Funcional Puro (reduce): En lugar de un iterador mutando variables externas, el algoritmo de Kadane se modela como una secuencia de transformaciones de estado inmutable a través de `reduce`, operando exclusivamente con objetos referencialmente transparentes.

public class MaxSubarray_Java17_Functional {

    /**
     * Calcula la suma máxima de un subarreglo contiguo utilizando Streams y estado inmutable.
     */
    public static int maxSubarraySum(int[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException("El arreglo debe tener al menos 1 elemento.");
        }

        // Java 17: Record local inmutable para transportar el estado de Kadane.
        // Los métodos de acceso en un record prescinden del prefijo 'get' (e.g., state.globalMax())
        record State(int currentMax, int globalMax) {}

        // Pipeline puramente funcional
        var finalState = Arrays.stream(arr)
                // 1. Proyección: Convierte el stream primitivo en un stream de objetos inmutables
                .mapToObj(x -> new State(x, x))
                
                // 2. Reducción: Propaga el estado evaluando la regla de decisión matemática
                .reduce((acc, val) -> {
                    var newCurrent = Math.max(val.currentMax(), acc.currentMax() + val.currentMax());
                    var newGlobal = Math.max(acc.globalMax(), newCurrent);
                    return new State(newCurrent, newGlobal);
                });

        // 3. Extracción: Obtención funcional del valor usando referencias a métodos
        return finalState
                .map(State::globalMax)
                .orElseThrow(() -> new IllegalStateException("Flujo vacío inesperado."));
    }

    public static void main(String[] args) {
        // ==========================================
        // CASOS DE PRUEBA
        // Ejecutar compilando y corriendo con el flag -ea:
        // java -ea MaxSubarray_Java17_Functional
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

        System.out.println("Todos los tests funcionales en Java 17 pasaron correctamente.");
    }
}
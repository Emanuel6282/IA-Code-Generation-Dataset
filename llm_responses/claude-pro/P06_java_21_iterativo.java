import java.util.ArrayList;
import java.util.Arrays;

public class MaxSubarray {

    /**
     * Retorna la suma máxima de un subarreglo contiguo no vacío de {@code nums}.
     *
     * <p>Usa el algoritmo de Kadane: O(n) en tiempo y O(1) en memoria. Las sumas se
     * acumulan en {@code long}: la suma de varios int puede superar el rango de int
     * (por ejemplo, Integer.MAX_VALUE + Integer.MAX_VALUE), y con int se desbordaría en
     * silencio dando un resultado negativo.
     *
     * @throws IllegalArgumentException si {@code nums} está vacío
     */
    public static long maxSubarraySum(int[] nums) {
        if (nums.length == 0) {
            throw new IllegalArgumentException("el arreglo debe tener al menos un elemento");
        }
        // Se parte del primer elemento (no de 0) porque el subarreglo no puede ser vacío;
        // así, si todos son negativos, el resultado es el menos negativo.
        long best = nums[0];
        long current = nums[0];
        for (var i = 1; i < nums.length; i++) {
            long x = nums[i];
            // La mejor suma que termina en x: o extiende el subarreglo anterior,
            // o empieza de nuevo en x si lo acumulado hasta ahora resta.
            if (current + x > x) {
                current += x;
            } else {
                current = x;
            }
            if (current > best) {
                best = current;
            }
        }
        return best;
    }

    /** Caso de prueba como record: inmutable y sin boilerplate. */
    record TestCase(String name, int[] nums, long expected) {}

    // Ejecutar con assertions habilitadas: java -ea MaxSubarray.java
    public static void main(String[] args) {
        var assertionsEnabled = false;
        assert assertionsEnabled = true; // solo se asigna si -ea está activo
        if (!assertionsEnabled) {
            System.err.println("Advertencia: ejecuta con 'java -ea' para que los asserts se evalúen.");
        }

        var testCases = new ArrayList<TestCase>();
        // Caso 1: ejemplo del enunciado ([4, -1, 2, 1])
        testCases.add(new TestCase("ejemplo del enunciado", new int[] {-2, 1, -3, 4, -1, 2, 1, -5, 4}, 6));
        // Caso 2: todos negativos -> el menos negativo
        testCases.add(new TestCase("todos negativos", new int[] {-3, -1, -2}, -1));
        testCases.add(new TestCase("un solo elemento negativo", new int[] {-5}, -5));
        // Caso 3: suma que supera el rango de int (con int se desbordaría a -2)
        testCases.add(new TestCase("suma mayor que int",
                new int[] {Integer.MAX_VALUE, Integer.MAX_VALUE}, 2L * Integer.MAX_VALUE));

        // Se ejecutan todos los casos y se acumulan los fallos para reportarlos juntos.
        var failures = new ArrayList<String>();
        for (var testCase : testCases) {
            // Record pattern en switch (JEP 440 + JEP 441, estándar en Java 21):
            // desestructura el record en sus componentes sin llamar a los getters.
            switch (testCase) {
                case TestCase(var name, var nums, var expected) -> {
                    var result = maxSubarraySum(nums);
                    if (result != expected) {
                        failures.add("Caso '%s' %s: esperado %d, obtenido %d"
                                .formatted(name, Arrays.toString(nums), expected, result));
                    }
                }
            }
        }

        // Arreglo vacío -> IllegalArgumentException
        var threw = false;
        try {
            maxSubarraySum(new int[] {});
        } catch (IllegalArgumentException e) {
            threw = true;
        }
        if (!threw) {
            failures.addLast("un arreglo vacío debería lanzar IllegalArgumentException");
        }

        // Sequenced collections (JEP 431, nuevo en Java 21): getFirst()/addLast() en lugar
        // de get(0)/add(); el mensaje solo se evalúa si el assert falla.
        assert failures.isEmpty()
                : "%d fallo(s); primero: %s".formatted(failures.size(), failures.getFirst());
        System.out.println("Todas las pruebas pasaron.");
    }
}
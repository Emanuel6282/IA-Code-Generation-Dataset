import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        for (int i = 1; i < nums.length; i++) {
            long x = nums[i];
            // La mejor suma que termina en x: o extiende el subarreglo anterior,
            // o empieza de nuevo en x si lo acumulado hasta ahora resta.
            if (current + x > x) {
                current = current + x;
            } else {
                current = x;
            }
            if (current > best) {
                best = current;
            }
        }
        return best;
    }

    /** Caso de prueba (Java 8 no tiene records: clase inmutable con campos final). */
    private static final class TestCase {
        final String name;
        final int[] nums;
        final long expected;

        TestCase(String name, int[] nums, long expected) {
            this.name = name;
            this.nums = nums;
            this.expected = expected;
        }
    }

    // Compilar y ejecutar con assertions habilitadas:
    //   javac MaxSubarray.java && java -ea MaxSubarray
    public static void main(String[] args) {
        boolean assertionsEnabled = false;
        assert assertionsEnabled = true; // solo se asigna si -ea está activo
        if (!assertionsEnabled) {
            System.err.println("Advertencia: ejecuta con 'java -ea' para que los asserts se evalúen.");
        }

        List<TestCase> testCases = new ArrayList<>();
        // Caso 1: ejemplo del enunciado ([4, -1, 2, 1])
        testCases.add(new TestCase("ejemplo del enunciado", new int[] {-2, 1, -3, 4, -1, 2, 1, -5, 4}, 6));
        // Caso 2: todos negativos -> el menos negativo
        testCases.add(new TestCase("todos negativos", new int[] {-3, -1, -2}, -1));
        testCases.add(new TestCase("un solo elemento negativo", new int[] {-5}, -5));
        // Caso 3: suma que supera el rango de int (con int se desbordaría a -2)
        testCases.add(new TestCase("suma mayor que int",
                new int[] {Integer.MAX_VALUE, Integer.MAX_VALUE}, 2L * Integer.MAX_VALUE));

        // Se ejecutan todos los casos y se acumulan los fallos para reportarlos juntos.
        List<String> failures = new ArrayList<>();
        for (TestCase testCase : testCases) {
            long result = maxSubarraySum(testCase.nums);
            if (result != testCase.expected) {
                failures.add("Caso '" + testCase.name + "' " + Arrays.toString(testCase.nums)
                        + ": esperado " + testCase.expected + ", obtenido " + result);
            }
        }

        // Arreglo vacío -> IllegalArgumentException
        boolean threw = false;
        try {
            maxSubarraySum(new int[] {});
        } catch (IllegalArgumentException e) {
            threw = true;
        }
        if (!threw) {
            failures.add("un arreglo vacío debería lanzar IllegalArgumentException");
        }

        // String.join (nuevo en Java 8): une los mensajes de fallo sin bucle manual.
        assert failures.isEmpty() : String.join("\n", failures);
        System.out.println("Todas las pruebas pasaron.");
    }
}
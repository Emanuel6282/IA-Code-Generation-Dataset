import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BinarySearch {

    /**
     * Busca target en arr (ordenado ascendentemente) y retorna su índice, o -1 si no está.
     */
    public static int binarySearch(int[] arr, int target) {
        int low = 0;
        int high = arr.length - 1;
        while (low <= high) {
            // low + (high - low) / 2 evita el desbordamiento de int que causaría
            // (low + high) / 2 con arreglos muy grandes.
            int mid = low + (high - low) / 2;
            if (arr[mid] == target) {
                return mid;
            }
            if (arr[mid] < target) {
                low = mid + 1;   // el objetivo solo puede estar en la mitad derecha
            } else {
                high = mid - 1;  // el objetivo solo puede estar en la mitad izquierda
            }
        }
        return -1;
    }

    /** Caso de prueba (Java 8 no tiene records: clase inmutable con campos final). */
    private static final class TestCase {
        final String name;
        final int[] arr;
        final int target;
        final int expected;

        TestCase(String name, int[] arr, int target, int expected) {
            this.name = name;
            this.arr = arr;
            this.target = target;
            this.expected = expected;
        }
    }

    // Compilar y ejecutar con assertions habilitadas:
    //   javac BinarySearch.java && java -ea BinarySearch
    public static void main(String[] args) {
        boolean assertionsEnabled = false;
        assert assertionsEnabled = true; // solo se asigna si -ea está activo
        if (!assertionsEnabled) {
            System.err.println("Advertencia: ejecuta con 'java -ea' para que los asserts se evalúen.");
        }

        List<TestCase> testCases = new ArrayList<>();
        // Caso 1: el objetivo está en el arreglo (ejemplo del enunciado)
        testCases.add(new TestCase("objetivo presente", new int[] {1, 3, 5, 7, 9}, 5, 2));
        // Caso 2: el objetivo no está en el arreglo (ejemplo del enunciado)
        testCases.add(new TestCase("objetivo ausente", new int[] {1, 3, 5, 7, 9}, 4, -1));
        // Caso 3: arreglo vacío
        testCases.add(new TestCase("arreglo vacío", new int[] {}, 1, -1));

        // Se ejecutan todos los casos y se acumulan los fallos para reportarlos juntos.
        List<String> failures = new ArrayList<>();
        for (TestCase testCase : testCases) {
            int result = binarySearch(testCase.arr, testCase.target);
            if (result != testCase.expected) {
                failures.add("Caso '" + testCase.name + "': arr=" + Arrays.toString(testCase.arr)
                        + ", target=" + testCase.target + ", esperado " + testCase.expected
                        + ", obtenido " + result);
            }
        }

        // String.join (nuevo en Java 8): une los mensajes de fallo sin bucle manual.
        assert failures.isEmpty() : String.join("\n", failures);
        System.out.println("Todas las pruebas pasaron.");
    }
}
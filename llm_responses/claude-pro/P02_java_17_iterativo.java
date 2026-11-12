import java.util.ArrayList;
import java.util.Arrays;

public class BinarySearch {

    /**
     * Busca target en arr (ordenado ascendentemente) y retorna su índice, o -1 si no está.
     */
    public static int binarySearch(int[] arr, int target) {
        var low = 0;                 // var: inferencia de tipo local (Java 10)
        var high = arr.length - 1;
        while (low <= high) {
            // low + (high - low) / 2 evita el desbordamiento de int que causaría
            // (low + high) / 2 con arreglos muy grandes.
            var mid = low + (high - low) / 2;
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

    /** Caso de prueba como record (estándar desde Java 16): inmutable y sin boilerplate. */
    record TestCase(String name, int[] arr, int target, int expected) {}

    // Ejecutar con assertions habilitadas: java -ea BinarySearch.java
    public static void main(String[] args) {
        var assertionsEnabled = false;
        assert assertionsEnabled = true; // solo se asigna si -ea está activo
        if (!assertionsEnabled) {
            System.err.println("Advertencia: ejecuta con 'java -ea' para que los asserts se evalúen.");
        }

        var testCases = new ArrayList<TestCase>();
        // Caso 1: el objetivo está en el arreglo (ejemplo del enunciado)
        testCases.add(new TestCase("objetivo presente", new int[] {1, 3, 5, 7, 9}, 5, 2));
        // Caso 2: el objetivo no está en el arreglo (ejemplo del enunciado)
        testCases.add(new TestCase("objetivo ausente", new int[] {1, 3, 5, 7, 9}, 4, -1));
        // Caso 3: arreglo vacío
        testCases.add(new TestCase("arreglo vacío", new int[] {}, 1, -1));

        // Se ejecutan todos los casos y se acumulan los fallos para reportarlos juntos.
        var failures = new ArrayList<String>();
        for (var testCase : testCases) {
            var result = binarySearch(testCase.arr(), testCase.target());
            if (result != testCase.expected()) {
                // String.formatted (Java 15): formato legible sin String.format estático.
                failures.add("Caso '%s': arr=%s, target=%d, esperado %d, obtenido %d".formatted(
                        testCase.name(), Arrays.toString(testCase.arr()),
                        testCase.target(), testCase.expected(), result));
            }
        }

        assert failures.isEmpty() : String.join("\n", failures);
        System.out.println("Todas las pruebas pasaron.");
    }
}
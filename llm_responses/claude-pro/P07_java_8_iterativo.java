import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Java 8 — paradigma iterativo (for/while, ArrayList; sin Streams, lambdas ni recursión).
//
// Las novedades centrales de Java 8 (lambdas, Streams, Optional, referencias a métodos)
// quedan fuera por el paradigma iterativo. De Java 8 se usa String.join, que une los
// mensajes de fallo de las pruebas sin un bucle manual de concatenación. También se usa
// el operador diamante `new ArrayList<>()` (Java 7), habitual en código Java 8.
public class FloodFill {

    /** Desplazamientos arriba, abajo, izquierda, derecha (conectividad 4, sin diagonales). */
    private static final int[][] OFFSETS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    /**
     * Rellena con {@code newColor} la región conectada (4 direcciones) que contiene
     * (row, col): todas las celdas alcanzables desde ahí moviéndose arriba, abajo,
     * izquierda o derecha por celdas del mismo color original.
     *
     * <p>La matriz se modifica en el lugar y se retorna la misma referencia. Si la celda
     * ya tiene {@code newColor}, se retorna sin cambios.
     *
     * <p>Se recorre con una pila explícita (un ArrayList al que se añade y del que se
     * quita por el final, ambas operaciones O(1)) y un bucle while, sin recursión: un
     * DFS recursivo sobre una región grande desbordaría la pila de la JVM. Cada celda se
     * pinta al apilarla, así que no se visita dos veces. O(filas * columnas) en tiempo.
     * Admite matrices irregulares: los límites se comprueban por fila.
     *
     * @throws IndexOutOfBoundsException si (row, col) está fuera de la matriz
     */
    public static int[][] floodFill(int[][] grid, int row, int col, int newColor) {
        if (row < 0 || row >= grid.length || col < 0 || col >= grid[row].length) {
            throw new IndexOutOfBoundsException(
                    "posición (" + row + ", " + col + ") fuera de la matriz");
        }

        int original = grid[row][col];
        // Si ya tiene el nuevo color no hay nada que hacer; además, sin esta comprobación
        // el bucle no terminaría, porque las celdas pintadas seguirían teniendo el color
        // original.
        if (original == newColor) {
            return grid;
        }

        List<int[]> stack = new ArrayList<>();
        grid[row][col] = newColor;
        stack.add(new int[] {row, col});

        while (!stack.isEmpty()) {
            int[] cell = stack.remove(stack.size() - 1);   // pop: quitar el último
            for (int i = 0; i < OFFSETS.length; i++) {
                int r = cell[0] + OFFSETS[i][0];
                int c = cell[1] + OFFSETS[i][1];
                if (r >= 0 && r < grid.length && c >= 0 && c < grid[r].length
                        && grid[r][c] == original) {
                    grid[r][c] = newColor;
                    stack.add(new int[] {r, c});
                }
            }
        }
        return grid;
    }

    /** Caso de prueba (Java 8 no tiene records: clase inmutable con campos final). */
    private static final class TestCase {
        final String name;
        final int[][] grid;
        final int row;
        final int col;
        final int newColor;
        final int[][] expected;

        TestCase(String name, int[][] grid, int row, int col, int newColor, int[][] expected) {
            this.name = name;
            this.grid = grid;
            this.row = row;
            this.col = col;
            this.newColor = newColor;
            this.expected = expected;
        }
    }

    // Compilar y ejecutar con assertions habilitadas:
    //   javac FloodFill.java && java -ea FloodFill
    public static void main(String[] args) {
        boolean assertionsEnabled = false;
        assert assertionsEnabled = true; // solo se asigna si -ea está activo
        if (!assertionsEnabled) {
            System.err.println("Advertencia: ejecuta con 'java -ea' para que los asserts se evalúen.");
        }

        List<TestCase> testCases = new ArrayList<>();
        // Caso 1: ejemplo del enunciado (la esquina inferior derecha no está conectada)
        testCases.add(new TestCase("ejemplo del enunciado",
                new int[][] {{1, 1, 1}, {1, 1, 0}, {1, 0, 1}}, 1, 1, 2,
                new int[][] {{2, 2, 2}, {2, 2, 0}, {2, 0, 1}}));
        // Caso 2: el color ya es el nuevo -> la matriz no cambia
        testCases.add(new TestCase("color ya es el nuevo",
                new int[][] {{0, 0, 0}, {0, 1, 1}}, 1, 1, 1,
                new int[][] {{0, 0, 0}, {0, 1, 1}}));
        // Caso 3: no se rellena en diagonal
        testCases.add(new TestCase("sin diagonales",
                new int[][] {{1, 0}, {0, 1}}, 0, 0, 3,
                new int[][] {{3, 0}, {0, 1}}));

        // Se ejecutan todos los casos y se acumulan los fallos para reportarlos juntos.
        List<String> failures = new ArrayList<>();
        for (TestCase testCase : testCases) {
            int[][] result = floodFill(testCase.grid, testCase.row, testCase.col, testCase.newColor);
            if (!Arrays.deepEquals(result, testCase.expected)) {
                failures.add("Caso '" + testCase.name + "': esperado "
                        + Arrays.deepToString(testCase.expected) + ", obtenido "
                        + Arrays.deepToString(result));
            }
            if (result != testCase.grid) {
                failures.add("Caso '" + testCase.name + "': debe modificar y retornar la misma matriz");
            }
        }

        // Posición fuera de la matriz (incluido un índice negativo) -> IndexOutOfBoundsException
        int[][] badPositions = {{2, 0}, {-1, 0}, {0, 3}};
        for (int[] position : badPositions) {
            boolean threw = false;
            try {
                floodFill(new int[][] {{1, 1, 1}, {1, 1, 1}}, position[0], position[1], 5);
            } catch (IndexOutOfBoundsException e) {
                threw = true;
            }
            if (!threw) {
                failures.add("(" + position[0] + ", " + position[1]
                        + ") debería lanzar IndexOutOfBoundsException");
            }
        }

        // String.join (nuevo en Java 8): une los mensajes de fallo sin bucle manual.
        assert failures.isEmpty() : String.join("\n", failures);
        System.out.println("Todas las pruebas pasaron.");
    }
}
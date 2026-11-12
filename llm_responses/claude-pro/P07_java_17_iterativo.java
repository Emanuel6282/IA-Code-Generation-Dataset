import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Java 17 — paradigma iterativo (for/while, ArrayList; sin Streams, lambdas ni recursión).
//
// Características de Java 17 (incluidas las estabilizadas entre Java 9 y 17) que se usan:
// 1. Records (Java 16): Cell, Direction y TestCase son tipos de datos inmutables en una
//    línea, con constructor, equals, hashCode y toString generados. Sustituyen al
//    `int[]` de dos posiciones de la versión Java 8, que no tenía nombres ni tipo.
// 2. `var` (Java 10): inferencia de tipos en variables locales cuando el tipo es obvio
//    por el lado derecho.
// 3. List.of (Java 9): listas inmutables literales para las direcciones y los casos.
// 4. String.formatted (Java 15): mensajes con formato sin String.format(...).
// 5. Pattern matching de instanceof (Java 16) no aplica aquí: no hay comprobaciones de tipo.
public class FloodFill {

    /** Celda de la matriz (fila, columna). */
    private record Cell(int row, int col) {}

    /** Desplazamiento a una celda vecina. */
    private record Direction(int dRow, int dCol) {}

    /** Arriba, abajo, izquierda, derecha (conectividad 4, sin diagonales). */
    private static final List<Direction> DIRECTIONS = List.of(
            new Direction(-1, 0), new Direction(1, 0), new Direction(0, -1), new Direction(0, 1));

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
            throw new IndexOutOfBoundsException("posición (%d, %d) fuera de la matriz".formatted(row, col));
        }

        var original = grid[row][col];
        // Si ya tiene el nuevo color no hay nada que hacer; además, sin esta comprobación
        // el bucle no terminaría, porque las celdas pintadas seguirían teniendo el color
        // original.
        if (original == newColor) {
            return grid;
        }

        var stack = new ArrayList<Cell>();
        grid[row][col] = newColor;
        stack.add(new Cell(row, col));

        while (!stack.isEmpty()) {
            var cell = stack.remove(stack.size() - 1);   // pop: quitar el último
            for (var direction : DIRECTIONS) {
                var r = cell.row() + direction.dRow();
                var c = cell.col() + direction.dCol();
                if (r >= 0 && r < grid.length && c >= 0 && c < grid[r].length
                        && grid[r][c] == original) {
                    grid[r][c] = newColor;
                    stack.add(new Cell(r, c));
                }
            }
        }
        return grid;
    }

    /** Caso de prueba como record. */
    private record TestCase(String name, int[][] grid, int row, int col, int newColor, int[][] expected) {}

    // Compilar y ejecutar con assertions habilitadas:
    //   javac FloodFill.java && java -ea FloodFill
    public static void main(String[] args) {
        var assertionsEnabled = false;
        assert assertionsEnabled = true; // solo se asigna si -ea está activo
        if (!assertionsEnabled) {
            System.err.println("Advertencia: ejecuta con 'java -ea' para que los asserts se evalúen.");
        }

        var testCases = List.of(
                // Caso 1: ejemplo del enunciado (la esquina inferior derecha no está conectada)
                new TestCase("ejemplo del enunciado",
                        new int[][] {{1, 1, 1}, {1, 1, 0}, {1, 0, 1}}, 1, 1, 2,
                        new int[][] {{2, 2, 2}, {2, 2, 0}, {2, 0, 1}}),
                // Caso 2: el color ya es el nuevo -> la matriz no cambia
                new TestCase("color ya es el nuevo",
                        new int[][] {{0, 0, 0}, {0, 1, 1}}, 1, 1, 1,
                        new int[][] {{0, 0, 0}, {0, 1, 1}}),
                // Caso 3: no se rellena en diagonal
                new TestCase("sin diagonales",
                        new int[][] {{1, 0}, {0, 1}}, 0, 0, 3,
                        new int[][] {{3, 0}, {0, 1}}));

        // Se ejecutan todos los casos y se acumulan los fallos para reportarlos juntos.
        var failures = new ArrayList<String>();
        for (var testCase : testCases) {
            var result = floodFill(testCase.grid(), testCase.row(), testCase.col(), testCase.newColor());
            if (!Arrays.deepEquals(result, testCase.expected())) {
                failures.add("Caso '%s': esperado %s, obtenido %s".formatted(testCase.name(),
                        Arrays.deepToString(testCase.expected()), Arrays.deepToString(result)));
            }
            if (result != testCase.grid()) {
                failures.add("Caso '%s': debe modificar y retornar la misma matriz".formatted(testCase.name()));
            }
        }

        // Posición fuera de la matriz (incluido un índice negativo) -> IndexOutOfBoundsException
        var badPositions = List.of(new Cell(2, 0), new Cell(-1, 0), new Cell(0, 3));
        for (var position : badPositions) {
            var threw = false;
            try {
                floodFill(new int[][] {{1, 1, 1}, {1, 1, 1}}, position.row(), position.col(), 5);
            } catch (IndexOutOfBoundsException e) {
                threw = true;
            }
            if (!threw) {
                failures.add("%s debería lanzar IndexOutOfBoundsException".formatted(position));
            }
        }

        assert failures.isEmpty() : String.join("\n", failures);
        System.out.println("Todas las pruebas pasaron.");
    }
}
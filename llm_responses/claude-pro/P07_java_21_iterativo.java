import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Java 21 — paradigma iterativo (for/while, ArrayList; sin Streams, lambdas ni recursión).
//
// Características de Java 21 que se usan:
// 1. Record patterns (JEP 440): `case Cell(int r, int c)` y
//    `case Direction(int dRow, int dCol)` descomponen el record en sus componentes
//    directamente en el patrón, sin llamar a cell.row(), cell.col(), etc.
// 2. Pattern matching para switch (JEP 441): el switch sobre la celda extraída de la
//    pila y sobre el resultado de cada prueba (interfaz sellada Outcome) es exhaustivo
//    sin `default`: el compilador comprueba que se cubren todos los casos.
// 3. Sequenced collections (JEP 431): la pila usa addLast/removeLast de ArrayList, que
//    expresan la operación (apilar/desapilar por el final) en lugar de
//    add(...) / remove(size() - 1).
// También se usan records (16), `var` (10), List.of (9) y String.formatted (15).
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
     * <p>Se recorre con una pila explícita (ArrayList con addLast/removeLast, ambas O(1))
     * y un bucle while, sin recursión: un DFS recursivo sobre una región grande
     * desbordaría la pila de la JVM. Cada celda se pinta al apilarla, así que no se visita
     * dos veces. O(filas * columnas) en tiempo. Admite matrices irregulares: los límites
     * se comprueban por fila.
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
        stack.addLast(new Cell(row, col));

        while (!stack.isEmpty()) {
            // Record pattern: la celda desapilada se descompone en (r, c). Un único caso
            // basta: el compilador sabe que cubre todo Cell.
            switch (stack.removeLast()) {
                case Cell(int r, int c) -> {
                    for (var direction : DIRECTIONS) {
                        switch (direction) {
                            case Direction(int dRow, int dCol) -> {
                                var nr = r + dRow;
                                var nc = c + dCol;
                                if (nr >= 0 && nr < grid.length && nc >= 0 && nc < grid[nr].length
                                        && grid[nr][nc] == original) {
                                    grid[nr][nc] = newColor;
                                    stack.addLast(new Cell(nr, nc));
                                }
                            }
                        }
                    }
                }
            }
        }
        return grid;
    }

    /** Caso de prueba como record. */
    private record TestCase(String name, int[][] grid, int row, int col, int newColor, int[][] expected) {}

    /** Resultado de una prueba: jerarquía sellada, así el switch sobre ella es exhaustivo. */
    private sealed interface Outcome permits Passed, Failed {}

    private record Passed() implements Outcome {}

    private record Failed(String message) implements Outcome {}

    private static Outcome run(TestCase testCase) {
        var result = floodFill(testCase.grid(), testCase.row(), testCase.col(), testCase.newColor());
        if (!Arrays.deepEquals(result, testCase.expected())) {
            return new Failed("Caso '%s': esperado %s, obtenido %s".formatted(testCase.name(),
                    Arrays.deepToString(testCase.expected()), Arrays.deepToString(result)));
        }
        if (result != testCase.grid()) {
            return new Failed("Caso '%s': debe modificar y retornar la misma matriz".formatted(testCase.name()));
        }
        return new Passed();
    }

    private static Outcome runOutOfBounds(Cell position) {
        try {
            floodFill(new int[][] {{1, 1, 1}, {1, 1, 1}}, position.row(), position.col(), 5);
        } catch (IndexOutOfBoundsException e) {
            return new Passed();
        }
        return new Failed("%s debería lanzar IndexOutOfBoundsException".formatted(position));
    }

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

        var outcomes = new ArrayList<Outcome>();
        for (var testCase : testCases) {
            outcomes.addLast(run(testCase));
        }
        // Posición fuera de la matriz (incluido un índice negativo) -> IndexOutOfBoundsException
        for (var position : List.of(new Cell(2, 0), new Cell(-1, 0), new Cell(0, 3))) {
            outcomes.addLast(runOutOfBounds(position));
        }

        // Switch exhaustivo sobre la interfaz sellada, con record pattern en Failed.
        var failures = new ArrayList<String>();
        for (var outcome : outcomes) {
            switch (outcome) {
                case Passed() -> { }
                case Failed(String message) -> failures.addLast(message);
            }
        }

        assert failures.isEmpty() : String.join("\n", failures);
        System.out.println("Todas las pruebas pasaron.");
    }
}
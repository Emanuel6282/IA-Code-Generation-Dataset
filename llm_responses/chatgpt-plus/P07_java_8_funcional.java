// P07: 2D Flood Fill — Java 8
// Paradigma: ESTRICTAMENTE FUNCIONAL
//
// CARACTERÍSTICAS UTILIZADAS:
//
// 1. Stream API (Java 8):
//    Permite procesar colecciones de forma declarativa.
//
// 2. Expresiones lambda (Java 8):
//    Se utilizan con map(), filter() y reduce().
//
// 3. Optional (Java 8):
//    Representa el resultado opcional de una expansión.
//
// 4. Recursión:
//    Sustituye los bucles imperativos for y while.
//
// 5. Inmutabilidad:
//    Cada expansión genera un nuevo conjunto.
//    La matriz original permanece sin modificaciones.
//
// 6. IntStream (Java 8):
//    Permite construir la matriz resultante sin bucles.
//
// Biblioteca estándar únicamente.

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class FloodFillFunctionalJava8 {

    // Representa una posición inmutable.
    private static final class Position {

        final int row;
        final int col;

        Position(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            if (!(obj instanceof Position)) {
                return false;
            }

            Position other = (Position) obj;

            return row == other.row && col == other.col;
        }

        @Override
        public int hashCode() {
            return 31 * row + col;
        }
    }

    // Generar vecinos usando Stream y filter.
    private static Set<Position> neighbors(
            int[][] grid,
            Position position,
            int originalColor) {

        int rows = grid.length;
        int cols = grid[0].length;

        return Stream.of(
                new Position(position.row - 1, position.col),
                new Position(position.row + 1, position.col),
                new Position(position.row, position.col - 1),
                new Position(position.row, position.col + 1)
            )
            .filter(p ->
                p.row >= 0 && p.row < rows
                && p.col >= 0 && p.col < cols
            )
            .filter(p -> grid[p.row][p.col] == originalColor)
            .collect(Collectors.collectingAndThen(
                Collectors.toSet(),
                Collections::unmodifiableSet
            ));
    }

    // Unión funcional de conjuntos.
    private static Set<Position> union(
            Set<Position> first,
            Set<Position> second) {

        return Stream.concat(first.stream(), second.stream())
            .collect(Collectors.collectingAndThen(
                Collectors.toSet(),
                Collections::unmodifiableSet
            ));
    }

    // Expandir recursivamente la región conectada.
    private static Set<Position> expand(
            int[][] grid,
            int originalColor,
            Set<Position> frontier,
            Set<Position> visited) {

        if (frontier.isEmpty()) {
            return visited;
        }

        // reduce(): reunir los vecinos de la frontera.
        Set<Position> adjacent = frontier.stream()
            .map(p -> neighbors(grid, p, originalColor))
            .reduce(
                Collections.<Position>emptySet(),
                FloodFillFunctionalJava8::union
            );

        // filter(): excluir las posiciones visitadas.
        Set<Position> nextFrontier = adjacent.stream()
            .filter(p -> !visited.contains(p))
            .collect(Collectors.collectingAndThen(
                Collectors.toSet(),
                Collections::unmodifiableSet
            ));

        // Optional: determinar si existe otra expansión.
        Optional<Set<Position>> next =
            Optional.of(nextFrontier)
                .filter(set -> !set.isEmpty());

        return next
            .map(set -> expand(
                grid,
                originalColor,
                set,
                union(visited, set)
            ))
            .orElse(visited);
    }

    // Función principal.
    public static int[][] floodFill(
            int[][] grid,
            int row,
            int col,
            int newColor) {

        int originalColor = grid[row][col];

        // Mantener la matriz sin cambios si el color coincide.
        if (originalColor == newColor) {
            return grid;
        }

        // Posición inicial inmutable.
        Position start = new Position(row, col);

        Set<Position> initial = Collections.singleton(start);

        // Encontrar toda la región conectada.
        Set<Position> connected = expand(
            grid,
            originalColor,
            initial,
            initial
        );

        // Crear una nueva matriz mediante IntStream.
        return IntStream.range(0, grid.length)
            .mapToObj(r ->
                IntStream.range(0, grid[r].length)
                    .map(c ->
                        connected.contains(new Position(r, c))
                            ? newColor
                            : grid[r][c]
                    )
                    .toArray()
            )
            .toArray(int[][]::new);
    }

    public static void main(String[] args) {

        // TEST 1: Ejemplo del enunciado.
        assert Arrays.deepEquals(
            floodFill(
                new int[][]{
                    {1, 1, 1},
                    {1, 1, 0},
                    {1, 0, 1}
                },
                1, 1, 2
            ),
            new int[][]{
                {2, 2, 2},
                {2, 2, 0},
                {2, 0, 1}
            }
        );

        // TEST 2: El color original ya es el nuevo.
        assert Arrays.deepEquals(
            floodFill(
                new int[][]{
                    {0, 0, 0},
                    {0, 1, 1}
                },
                1, 1, 1
            ),
            new int[][]{
                {0, 0, 0},
                {0, 1, 1}
            }
        );

        // TEST 3: No colorear conexiones diagonales.
        assert Arrays.deepEquals(
            floodFill(
                new int[][]{
                    {1, 0, 1},
                    {0, 1, 0},
                    {1, 0, 1}
                },
                1, 1, 2
            ),
            new int[][]{
                {1, 0, 1},
                {0, 2, 0},
                {1, 0, 1}
            }
        );

        System.out.println("All tests passed");
    }
}
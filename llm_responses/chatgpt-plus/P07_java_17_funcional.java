// P07: 2D Flood Fill — Java 17
// Paradigma: ESTRICTAMENTE FUNCIONAL
//
// CARACTERÍSTICAS UTILIZADAS:
//
// 1. record (Java 16, disponible en Java 17):
//    Representa coordenadas inmutables.
//    Genera equals(), hashCode() y accesores.
//
// 2. Stream API y expresiones lambda:
//    Permiten generar, filtrar y transformar
//    posiciones sin bucles imperativos.
//
// 3. Optional:
//    Representa la existencia de una nueva frontera
//    y controla la continuación de la recursión.
//
// 4. Set.copyOf() (Java 10):
//    Construye conjuntos inmutables.
//
// 5. Recursión:
//    Sustituye los bucles for y while.
//
// 6. IntStream:
//    Construye la matriz resultante de forma declarativa.
//
// Biblioteca estándar únicamente.
// Sin bucles for/while ni modificación de la matriz original.

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class FloodFillFunctionalJava17 {

    // Coordenadas inmutables.
    private record Position(int row, int col) {}

    // Obtener vecinos válidos en cuatro direcciones.
    private static Set<Position> neighbors(
            int[][] grid,
            Position position,
            int originalColor) {

        int rows = grid.length;

        return Stream.of(
                new Position(position.row() - 1, position.col()),
                new Position(position.row() + 1, position.col()),
                new Position(position.row(), position.col() - 1),
                new Position(position.row(), position.col() + 1)
            )
            .filter(p ->
                p.row() >= 0
                && p.row() < rows
                && p.col() >= 0
                && p.col() < grid[p.row()].length
            )
            .filter(p ->
                grid[p.row()][p.col()] == originalColor
            )
            .collect(Collectors.toUnmodifiableSet());
    }

    // Unión funcional: crea un conjunto nuevo e inmutable.
    private static Set<Position> union(
            Set<Position> first,
            Set<Position> second) {

        return Stream.concat(
                first.stream(),
                second.stream()
            )
            .collect(Collectors.toUnmodifiableSet());
    }

    // Expandir recursivamente la región conectada.
    private static Set<Position> expand(
            int[][] grid,
            int originalColor,
            Set<Position> frontier,
            Set<Position> visited) {

        // Caso base.
        if (frontier.isEmpty()) {
            return visited;
        }

        // Reunir todos los vecinos mediante reduce().
        Set<Position> adjacent = frontier.stream()
            .map(p -> neighbors(grid, p, originalColor))
            .reduce(
                Set.<Position>of(),
                FloodFillFunctionalJava17::union
            );

        // Eliminar posiciones visitadas.
        Set<Position> nextFrontier = adjacent.stream()
            .filter(p -> !visited.contains(p))
            .collect(Collectors.toUnmodifiableSet());

        // Optional controla la expansión siguiente.
        return Optional.of(nextFrontier)
            .filter(next -> !next.isEmpty())
            .map(next -> expand(
                grid,
                originalColor,
                next,
                union(visited, next)
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

        // Si el color coincide, devolver sin cambios.
        if (originalColor == newColor) {
            return grid;
        }

        // Posición inicial.
        Position start = new Position(row, col);

        Set<Position> initial = Set.of(start);

        // Encontrar todas las posiciones conectadas.
        Set<Position> connected = expand(
            grid,
            originalColor,
            initial,
            initial
        );

        // Construir una nueva matriz mediante IntStream.
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
                new int[][] {
                    {1, 1, 1},
                    {1, 1, 0},
                    {1, 0, 1}
                },
                1, 1, 2
            ),
            new int[][] {
                {2, 2, 2},
                {2, 2, 0},
                {2, 0, 1}
            }
        );

        // TEST 2: El color original ya es el nuevo.
        assert Arrays.deepEquals(
            floodFill(
                new int[][] {
                    {0, 0, 0},
                    {0, 1, 1}
                },
                1, 1, 1
            ),
            new int[][] {
                {0, 0, 0},
                {0, 1, 1}
            }
        );

        // TEST 3: No colorear conexiones diagonales.
        assert Arrays.deepEquals(
            floodFill(
                new int[][] {
                    {1, 0, 1},
                    {0, 1, 0},
                    {1, 0, 1}
                },
                1, 1, 2
            ),
            new int[][] {
                {1, 0, 1},
                {0, 2, 0},
                {1, 0, 1}
            }
        );

        System.out.println("All tests passed");
    }
}
// P07: 2D Flood Fill — Java 21
// Paradigma: ESTRICTAMENTE FUNCIONAL
//
// CARACTERÍSTICAS UTILIZADAS:
//
// 1. record (Java 16, disponible en Java 21):
//    Representa coordenadas inmutables.
//
// 2. Stream API y lambdas:
//    Implementan map(), filter(), flatMap() y reduce()
//    sin bucles imperativos.
//
// 3. Optional:
//    Controla la continuación de la expansión recursiva.
//
// 4. Set.of() y Collectors.toUnmodifiableSet():
//    Permiten trabajar con conjuntos inmutables.
//
// 5. Stream.toList() (Java 16):
//    Genera listas no modificables.
//
// 6. Recursión:
//    Sustituye los bucles for y while.
//
// 7. IntStream:
//    Construye una nueva matriz sin modificar la original.
//
// Biblioteca estándar únicamente.

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class FloodFillFunctionalJava21 {

    // Coordenada inmutable.
    private record Position(int row, int col) {}

    // Obtener los vecinos válidos en cuatro direcciones.
    private static Set<Position> neighbors(
            int[][] grid,
            Position position,
            int originalColor) {

        return Stream.of(
                new Position(position.row() - 1, position.col()),
                new Position(position.row() + 1, position.col()),
                new Position(position.row(), position.col() - 1),
                new Position(position.row(), position.col() + 1)
            )
            .filter(p ->
                p.row() >= 0
                && p.row() < grid.length
                && p.col() >= 0
                && p.col() < grid[p.row()].length
            )
            .filter(p ->
                grid[p.row()][p.col()] == originalColor
            )
            .collect(Collectors.toUnmodifiableSet());
    }

    // Unión funcional de conjuntos.
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

        // Obtener vecinos de todas las posiciones actuales.
        Set<Position> adjacent = frontier.stream()
            .map(p -> neighbors(grid, p, originalColor))
            .reduce(
                Set.<Position>of(),
                FloodFillFunctionalJava21::union
            );

        // Excluir posiciones ya visitadas.
        Set<Position> nextFrontier = adjacent.stream()
            .filter(p -> !visited.contains(p))
            .collect(Collectors.toUnmodifiableSet());

        // Continuar solamente si existen vecinos nuevos.
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

        // Conjunto inicial inmutable.
        Set<Position> initial = Set.of(start);

        // Encontrar la región conectada.
        Set<Position> connected = expand(
            grid,
            originalColor,
            initial,
            initial
        );

        // Construir una nueva matriz funcionalmente.
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
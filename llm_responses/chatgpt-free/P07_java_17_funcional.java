/*

* 2D Flood Fill — Java 17
* Paradigma: estrictamente funcional
*
* Características de Java 17 utilizadas:
* 1. record: permite representar Position y State de forma concisa
* e inmutable.
* 2. Stream API: permite generar y transformar las posiciones sin
* utilizar bucles imperativos.
* 3. Lambdas: se utilizan para mapear, filtrar y transformar datos.
* 4. Optional: se utiliza para obtener de forma segura el estado
* final del recorrido.
*
* No se utilizan for, while ni recursion.
  */

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class FloodFill {

private record Position(int row, int col) {
}

private record State(
        Set<Position> visited,
        Set<Position> frontier
) {
    State {
        visited = Collections.unmodifiableSet(
            new HashSet<>(visited)
        );

        frontier = Collections.unmodifiableSet(
            new HashSet<>(frontier)
        );
    }
}

public static int[][] floodFill(
        int[][] grid,
        int row,
        int col,
        int newColor
) {
    int originalColor = grid[row][col];

    if (originalColor == newColor) {
        return grid;
    }

    int rows = grid.length;
    int cols = grid[0].length;

    Position start = new Position(row, col);

    State initialState = new State(
        Collections.singleton(start),
        Collections.singleton(start)
    );

    Optional<State> finalState = Stream
        .iterate(
            initialState,
            state -> expand(
                state,
                grid,
                originalColor,
                rows,
                cols
            )
        )
        .limit((long) rows * cols + 1)
        .reduce((previous, current) -> current);

    Set<Position> visited = finalState
        .orElseThrow(
            () -> new IllegalStateException(
                "No se pudo calcular el Flood Fill"
            )
        )
        .visited();

    return IntStream.range(0, rows)
        .mapToObj(r ->
            IntStream.range(0, cols)
                .map(c ->
                    visited.contains(new Position(r, c))
                        ? newColor
                        : grid[r][c]
                )
                .toArray()
        )
        .toArray(int[][]::new);
}

private static State expand(
        State state,
        int[][] grid,
        int originalColor,
        int rows,
        int cols
) {
    Set<Position> newPositions = state.frontier()
        .stream()
        .flatMap(position ->
            Stream.of(
                new Position(
                    position.row() - 1,
                    position.col()
                ),
                new Position(
                    position.row() + 1,
                    position.col()
                ),
                new Position(
                    position.row(),
                    position.col() - 1
                ),
                new Position(
                    position.row(),
                    position.col() + 1
                )
            )
        )
        .filter(position ->
            position.row() >= 0 &&
            position.row() < rows &&
            position.col() >= 0 &&
            position.col() < cols
        )
        .filter(position ->
            grid[position.row()][position.col()] == originalColor
        )
        .filter(position ->
            !state.visited().contains(position)
        )
        .collect(Collectors.toSet());

    Set<Position> visited = Stream.concat(
            state.visited().stream(),
            newPositions.stream()
        )
        .collect(Collectors.toSet());

    return new State(visited, newPositions);
}

public static void main(String[] args) {

    assert Arrays.deepEquals(
        floodFill(
            new int[][]{
                {1, 1, 1},
                {1, 1, 0},
                {1, 0, 1}
            },
            1,
            1,
            2
        ),
        new int[][]{
            {2, 2, 2},
            {2, 2, 0},
            {2, 0, 1}
        }
    );

    assert Arrays.deepEquals(
        floodFill(
            new int[][]{{0}},
            0,
            0,
            5
        ),
        new int[][]{{5}}
    );

    assert Arrays.deepEquals(
        floodFill(
            new int[][]{
                {0, 0},
                {0, 0}
            },
            0,
            0,
            3
        ),
        new int[][]{
            {3, 3},
            {3, 3}
        }
    );

    System.out.println("All tests passed");
}

}

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class FloodFill {

private static final class Position {
    private final int row;
    private final int col;

    Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Position)) {
            return false;
        }

        Position other = (Position) object;
        return row == other.row && col == other.col;
    }

    @Override
    public int hashCode() {
        return 31 * row + col;
    }
}

private static final class State {
    private final Set<Position> visited;
    private final Set<Position> frontier;

    State(Set<Position> visited, Set<Position> frontier) {
        this.visited = Collections.unmodifiableSet(
            new HashSet<>(visited)
        );
        this.frontier = Collections.unmodifiableSet(
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

    State finalState = Stream
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
        .reduce(
            (previous, current) -> current
        )
        .orElseThrow(
            () -> new IllegalStateException(
                "No se pudo calcular el Flood Fill"
            )
        );

    return IntStream.range(0, rows)
        .mapToObj(r ->
            IntStream.range(0, cols)
                .map(c ->
                    finalState.visited.contains(
                        new Position(r, c)
                    )
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
    Set<Position> newPositions = state.frontier
        .stream()
        .flatMap(position ->
            Stream.of(
                new Position(position.row - 1, position.col),
                new Position(position.row + 1, position.col),
                new Position(position.row, position.col - 1),
                new Position(position.row, position.col + 1)
            )
        )
        .filter(position ->
            position.row >= 0 &&
            position.row < rows &&
            position.col >= 0 &&
            position.col < cols
        )
        .filter(position ->
            grid[position.row][position.col] == originalColor
        )
        .filter(position ->
            !state.visited.contains(position)
        )
        .collect(Collectors.toSet());

    Set<Position> visited = Stream.concat(
            state.visited.stream(),
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

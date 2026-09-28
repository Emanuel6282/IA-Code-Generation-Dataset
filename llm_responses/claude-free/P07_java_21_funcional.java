import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class FloodFill {

    private record Point(int r, int c) {
    }

    public static int[][] floodFill(int[][] grid, int row, int col, int newColor) {
        int originalColor = grid[row][col];

        return Optional.of(originalColor)
                .filter(oc -> oc != newColor)
                .map(oc -> buildFilledGrid(grid, oc, newColor, row, col))
                .orElse(grid);
    }

    private static int[][] buildFilledGrid(int[][] grid, int originalColor, int newColor, int row, int col) {
        int rows = grid.length;
        int cols = grid[0].length;

        Set<Point> filledPositions = expand(
                grid, originalColor, rows, cols,
                Collections.emptySet(),
                Collections.singleton(new Point(row, col))
        );

        return IntStream.range(0, rows)
                .mapToObj(r -> IntStream.range(0, cols)
                        .map(c -> filledPositions.contains(new Point(r, c)) ? newColor : grid[r][c])
                        .toArray())
                .toArray(int[][]::new);
    }

    private static Set<Point> expand(int[][] grid, int originalColor, int rows, int cols,
                                      Set<Point> visited, Set<Point> frontier) {
        if (frontier.isEmpty()) {
            return visited;
        }

        Set<Point> newVisited = Stream.concat(visited.stream(), frontier.stream())
                .collect(Collectors.toSet());

        Set<Point> nextFrontier = frontier.stream()
                .flatMap(p -> neighbors(p).stream())
                .filter(p -> inBoundsAndSameColor(grid, p, rows, cols, originalColor))
                .filter(p -> !newVisited.contains(p))
                .collect(Collectors.toSet());

        return expand(grid, originalColor, rows, cols, newVisited, nextFrontier);
    }

    private static List<Point> neighbors(Point p) {
        return Arrays.asList(
                new Point(p.r() - 1, p.c()),
                new Point(p.r() + 1, p.c()),
                new Point(p.r(), p.c() - 1),
                new Point(p.r(), p.c() + 1)
        );
    }

    private static boolean inBoundsAndSameColor(int[][] grid, Point p, int rows, int cols, int originalColor) {
        return p.r() >= 0 && p.r() < rows && p.c() >= 0 && p.c() < cols && grid[p.r()][p.c()] == originalColor;
    }

    public static void main(String[] args) {
        // Caso 1: ejemplo del enunciado
        int[][] grid1 = {{1, 1, 1}, {1, 1, 0}, {1, 0, 1}};
        int[][] expected1 = {{2, 2, 2}, {2, 2, 0}, {2, 0, 1}};
        assert Arrays.deepEquals(floodFill(grid1, 1, 1, 2), expected1) : "Fallo caso 1";

        // Caso 2: el color ya es el nuevo color -> sin cambios
        int[][] grid2 = {{0, 0, 0}, {0, 1, 1}};
        int[][] expected2 = {{0, 0, 0}, {0, 1, 1}};
        assert Arrays.deepEquals(floodFill(grid2, 1, 1, 1), expected2) : "Fallo caso 2";

        // Caso 3: matriz de un solo color, se rellena por completo
        int[][] grid3 = {{3, 3}, {3, 3}};
        int[][] expected3 = {{7, 7}, {7, 7}};
        assert Arrays.deepEquals(floodFill(grid3, 0, 0, 7), expected3) : "Fallo caso 3";

        System.out.println("Todos los tests pasaron correctamente.");
    }
}
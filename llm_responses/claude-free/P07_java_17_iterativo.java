import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Arrays;

public class FloodFill {

    private record Point(int r, int c) {
    }

    public static int[][] floodFill(int[][] grid, int row, int col, int newColor) {
        int originalColor = grid[row][col];

        if (originalColor == newColor) {
            return grid;
        }

        int rows = grid.length;
        int cols = grid[0].length;

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        Deque<Point> queue = new ArrayDeque<>();
        queue.add(new Point(row, col));
        grid[row][col] = newColor;

        while (!queue.isEmpty()) {
            Point current = queue.poll();
            int r = current.r();
            int c = current.c();

            for (int[] dir : directions) {
                int nr = r + dir[0];
                int nc = c + dir[1];

                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && grid[nr][nc] == originalColor) {
                    grid[nr][nc] = newColor;
                    queue.add(new Point(nr, nc));
                }
            }
        }

        return grid;
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
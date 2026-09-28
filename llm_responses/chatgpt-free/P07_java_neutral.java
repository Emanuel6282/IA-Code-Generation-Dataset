import java.util.ArrayDeque;
import java.util.Queue;

public class FloodFill {

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

    Queue<int[]> queue = new ArrayDeque<>();
    queue.offer(new int[]{row, col});

    grid[row][col] = newColor;

    int[][] directions = {
        {-1, 0}, // arriba
        {1, 0},  // abajo
        {0, -1}, // izquierda
        {0, 1}   // derecha
    };

    while (!queue.isEmpty()) {
        int[] current = queue.poll();

        int currentRow = current[0];
        int currentCol = current[1];

        for (int[] direction : directions) {
            int nextRow = currentRow + direction[0];
            int nextCol = currentCol + direction[1];

            if (
                nextRow >= 0 &&
                nextRow < rows &&
                nextCol >= 0 &&
                nextCol < cols &&
                grid[nextRow][nextCol] == originalColor
            ) {
                grid[nextRow][nextCol] = newColor;
                queue.offer(new int[]{nextRow, nextCol});
            }
        }
    }

    return grid;
}

public static void main(String[] args) {

    assert java.util.Arrays.deepEquals(
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

    assert java.util.Arrays.deepEquals(
        floodFill(
            new int[][]{{0}},
            0,
            0,
            5
        ),
        new int[][]{{5}}
    );

    assert java.util.Arrays.deepEquals(
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

// 2D Flood Fill — Java 8
// Paradigma: estrictamente iterativo
// Algoritmo: BFS mediante ArrayList.
//
// Java 8 no introduce una característica específica que mejore
// materialmente el algoritmo Flood Fill. Se utilizan colecciones
// estándar compatibles con Java 8 y bucles iterativos.

import java.util.ArrayList;

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

    ArrayList<int[]> queue = new ArrayList<int[]>();
    queue.add(new int[]{row, col});

    int queueIndex = 0;

    grid[row][col] = newColor;

    int[][] directions = {
        {-1, 0}, // arriba
        {1, 0},  // abajo
        {0, -1}, // izquierda
        {0, 1}   // derecha
    };

    while (queueIndex < queue.size()) {
        int[] current = queue.get(queueIndex);
        queueIndex++;

        int currentRow = current[0];
        int currentCol = current[1];

        for (int i = 0; i < directions.length; i++) {
            int nextRow = currentRow + directions[i][0];
            int nextCol = currentCol + directions[i][1];

            if (
                nextRow >= 0 &&
                nextRow < rows &&
                nextCol >= 0 &&
                nextCol < cols &&
                grid[nextRow][nextCol] == originalColor
            ) {
                grid[nextRow][nextCol] = newColor;
                queue.add(new int[]{nextRow, nextCol});
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

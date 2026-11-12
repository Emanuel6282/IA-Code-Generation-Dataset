import java.util.ArrayDeque;
import java.util.Queue;

public class FloodFill {

    public static int[][] floodFill(
            int[][] grid,
            int row,
            int col,
            int newColor) {

        // Obtener el color original.
        int originalColor = grid[row][col];

        // Si el color ya coincide, no modificar nada.
        if (originalColor == newColor) {
            return grid;
        }

        int rows = grid.length;
        int cols = grid[0].length;

        // Direcciones: arriba, abajo, izquierda, derecha.
        int[][] directions = {
            {-1, 0},
            {1, 0},
            {0, -1},
            {0, 1}
        };

        // Cola para realizar BFS.
        Queue<int[]> queue = new ArrayDeque<>();

        queue.offer(new int[]{row, col});

        // Colorear el píxel inicial.
        grid[row][col] = newColor;

        // Recorrido iterativo.
        while (!queue.isEmpty()) {

            int[] current = queue.poll();

            int currentRow = current[0];
            int currentCol = current[1];

            // Examinar los cuatro vecinos.
            for (int[] direction : directions) {

                int nextRow = currentRow + direction[0];
                int nextCol = currentCol + direction[1];

                // Verificar límites y color original.
                if (nextRow >= 0 && nextRow < rows
                        && nextCol >= 0 && nextCol < cols
                        && grid[nextRow][nextCol] == originalColor) {

                    // Cambiar el color.
                    grid[nextRow][nextCol] = newColor;

                    // Agregar el vecino a la cola.
                    queue.offer(new int[]{nextRow, nextCol});
                }
            }
        }

        return grid;
    }

    public static void main(String[] args) {

        // TEST 1: Ejemplo del enunciado.
        assert java.util.Arrays.deepEquals(
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
        assert java.util.Arrays.deepEquals(
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
        assert java.util.Arrays.deepEquals(
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
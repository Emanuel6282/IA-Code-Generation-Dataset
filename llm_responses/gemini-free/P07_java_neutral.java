// Java - Algoritmo Flood Fill (Relleno por inundación)
// Paradigma: Iterativo BFS (Breadth-First Search) con Cola estándar

import java.util.LinkedList;
import java.util.Queue;

public class FloodFillSolution {

    /**
     * Implementa el algoritmo Flood Fill sobre una matriz 2D de enteros de forma iterativa (BFS).
     *
     * @param grid      Matriz 2D de enteros a modificar.
     * @param row       Fila de la posición inicial.
     * @param col       Columna de la posición inicial.
     * @param newColor  Nuevo color a propagar.
     * @return La matriz modificada.
     */
    public static int[][] floodFill(int[][] grid, int row, int col, int newColor) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return grid;
        }

        int originalColor = grid[row][col];
        if (originalColor == newColor) {
            return grid;
        }

        int rows = grid.length;
        int cols = grid[0].length;

        // Cola para almacenar las coordenadas (fila, columna)
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{row, col});
        grid[row][col] = newColor;

        // Direcciones en 4 sentidos: arriba, abajo, izquierda, derecha
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int r = current[0];
            int c = current[1];

            for (int[] dir : directions) {
                int nr = r + dir[0];
                int nc = c + dir[1];

                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && grid[nr][nc] == originalColor) {
                    grid[nr][nc] = newColor;
                    queue.offer(new int[]{nr, nc});
                }
            }
        }

        return grid;
    }

    // ==========================================
    // CASOS DE PRUEBA (ASSERTS)
    // ==========================================
    public static void main(String[] args) {
        // Habilitar aserciones con java -ea FloodFillSolution

        // CASO 1: Ejemplo clásico del enunciado
        int[][] grid1 = {
            {1, 1, 1},
            {1, 1, 0},
            {1, 0, 1}
        };
        int[][] expected1 = {
            {2, 2, 2},
            {2, 2, 0},
            {2, 0, 1}
        };
        int[][] result1 = floodFill(grid1, 1, 1, 2);
        for (int i = 0; i < result1.length; i++) {
            for (int j = 0; j < result1[0].length; j++) {
                assert result1[i][j] == expected1[i][j] : "Falló Caso 1 en (" + i + ", " + j + ")";
            }
        }

        // CASO 2: El color inicial ya es el nuevo color
        int[][] grid2 = {
            {0, 0, 0},
            {0, 1, 1}
        };
        int[][] expected2 = {
            {0, 0, 0},
            {0, 1, 1}
        };
        int[][] result2 = floodFill(grid2, 1, 1, 1);
        for (int i = 0; i < result2.length; i++) {
            for (int j = 0; j < result2[0].length; j++) {
                assert result2[i][j] == expected2[i][j] : "Falló Caso 2 en (" + i + ", " + j + ")";
            }
        }

        // CASO 3: Matriz de un solo elemento
        int[][] grid3 = {{5}};
        int[][] expected3 = {{9}};
        int[][] result3 = floodFill(grid3, 0, 0, 9);
        assert result3[0][0] == expected3[0][0] : "Falló Caso 3";

        System.out.println("¡Todos los casos de prueba en Java pasaron correctamente!");
    }
}
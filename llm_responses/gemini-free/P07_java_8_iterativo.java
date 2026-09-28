// Java 8 - Paradigma Iterativo Estricto
//
// Características utilizadas:
// 1. Compatibilidad total con Java 8 mediante estructuras estándar de colecciones (`ArrayList`).
// 2. Paradigma iterativo estricto: Empleo exclusivo de bucles `while` y `for`, evitando por completo Streams, expresiones lambda y recursión.
// 3. Simulación eficiente de cola mediante índice de cabeza (`head`) sobre `ArrayList` para un rendimiento óptimo en O(N).

import java.util.ArrayList;

public class FloodFillJava8 {

    /**
     * Implementa el algoritmo Flood Fill sobre una matriz 2D de enteros de forma iterativa (BFS)
     * utilizando estructuras de Java 8 sin lambdas ni streams.
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

        // Uso explícito de ArrayList como cola iterativa sin lambdas ni streams
        ArrayList<int[]> queue = new ArrayList<>();
        queue.add(new int[] { row, col });
        grid[row][col] = newColor;

        int head = 0;
        int[][] directions = new int[][] {
            { -1, 0 },
            { 1, 0 },
            { 0, -1 },
            { 0, 1 }
        };

        while (head < queue.size()) {
            int[] current = queue.get(head);
            head++;
            int r = current[0];
            int c = current[1];

            for (int i = 0; i < directions.length; i++) {
                int nr = r + directions[i][0];
                int nc = c + directions[i][1];

                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && grid[nr][nc] == originalColor) {
                    grid[nr][nc] = newColor;
                    queue.add(new int[] { nr, nc });
                }
            }
        }

        return grid;
    }

    // ==========================================
    // CASOS DE PRUEBA (ASSERTS)
    // ==========================================
    public static void main(String[] args) {
        // Habilitar aserciones ejecutando con: java -ea FloodFillJava8

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
        int[][] grid3 = {{3}};
        int[][] expected3 = {{7}};
        int[][] result3 = floodFill(grid3, 0, 0, 7);
        assert result3[0][0] == expected3[0][0] : "Falló Caso 3";

        System.out.println("¡Todos los casos de prueba iterativos en Java 8 pasaron correctamente!");
    }
}
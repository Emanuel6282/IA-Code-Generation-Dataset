// Java 17 - Paradigma Iterativo Estricto
//
// Características de Java 17 utilizadas:
// 1. Uso de `record` (`record Point(int r, int c)`): Introducido formalmente en Java 16/17 para modelar las coordenadas de forma inmutable, limpia y libre de código repetitivo (boilerplate).
// 2. Paradigma iterativo estricto: Empleo exclusivo de bucles `while` y `for` con `ArrayList` como cola optimizada, evitando por completo Streams, lambdas y recursión.
// 3. Optimizaciones de rendimiento y tipado estático nativo de la plataforma Java 17.

import java.util.ArrayList;

public class FloodFillJava17 {

    // Record inmutable de Java 17 para representar coordenadas (fila, columna)
    public record Point(int r, int c) {}

    /**
     * Implementa el algoritmo Flood Fill sobre una matriz 2D de enteros de forma iterativa (BFS)
     * utilizando características de Java 17 como records.
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

        // Cola iterativa utilizando ArrayList y objetos Point (record de Java 17)
        ArrayList<Point> queue = new ArrayList<>();
        queue.add(new Point(row, col));
        grid[row][col] = newColor;

        int head = 0;
        int[][] directions = new int[][] {
            { -1, 0 },
            { 1, 0 },
            { 0, -1 },
            { 0, 1 }
        };

        while (head < queue.size()) {
            Point current = queue.get(head);
            head++;

            for (int i = 0; i < directions.length; i++) {
                int nr = current.r() + directions[i][0];
                int nc = current.c() + directions[i][1];

                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && grid[nr][nc] == originalColor) {
                    grid[nr][nc] = newColor;
                    queue.add(new Point(nr, nc));
                }
            }
        }

        return grid;
    }

    // ==========================================
    // CASOS DE PRUEBA (ASSERTS)
    // ==========================================
    public static void main(String[] args) {
        // Habilitar aserciones ejecutando con: java -ea FloodFillJava17

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
        int[][] grid3 = {{6}};
        int[][] expected3 = {{3}};
        int[][] result3 = floodFill(grid3, 0, 0, 3);
        assert result3[0][0] == expected3[0][0] : "Falló Caso 3";

        System.out.println("¡Todos los casos de prueba iterativos en Java 17 pasaron correctamente!");
    }
}
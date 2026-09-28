// Java 17 - Paradigma Funcional Estricto
//
// Características de Java 17 utilizadas:
// 1. Uso de `record` (`record Point(int r, int c)`): Define las coordenadas direccionales de manera inmutable y limpia, eliminando código repetitivo.
// 2. Stream API y expresiones lambda (`Arrays.stream`, `map`, `forEach`) para procesar las 4 direcciones de forma declarativa.
// 3. Uso de `Optional` para gestionar la validación inicial y los límites de la matriz de forma funcional.
// 4. Ausencia total de bucles imperativos `for`/`while`, reemplazados por flujo funcional y recursión pura.

import java.util.Arrays;
import java.util.Optional;

public class FloodFillJava17Functional {

    // Record inmutable de Java 17 para representar coordenadas (fila, columna)
    public record Point(int r, int c) {}

    /**
     * Implementa el algoritmo Flood Fill utilizando estrictamente el paradigma funcional
     * en Java 17 con Streams, lambdas, Optional y records, sin bucles for/while.
     *
     * @param grid      Matriz 2D de enteros a modificar.
     * @param row       Fila de la posición inicial.
     * @param col       Columna de la posición inicial.
     * @param newColor  Nuevo color a propagar.
     * @return La matriz modificada.
     */
    public static int[][] floodFill(int[][] grid, int row, int col, int newColor) {
        return Optional.ofNullable(grid)
            .filter(g -> g.length > 0 && g[0].length > 0)
            .map(g -> {
                int originalColor = g[row][col];
                if (originalColor != newColor) {
                    fillRecursive(g, row, col, originalColor, newColor);
                }
                return g;
            })
            .orElse(grid);
    }

    private static void fillRecursive(int[][] grid, int r, int c, int originalColor, int newColor) {
        Optional.of(grid)
            .filter(g -> r >= 0 && r < g.length && c >= 0 && c < g[0].length && g[r][c] == originalColor)
            .ifPresent(g -> {
                g[r][c] = newColor;

                Point[] directions = {
                    new Point(-1, 0),
                    new Point(1, 0),
                    new Point(0, -1),
                    new Point(0, 1)
                };

                // Uso puramente funcional de Stream API sobre el record Point para las 4 direcciones
                Arrays.stream(directions)
                    .map(dir -> new Point(r + dir.r(), c + dir.c()))
                    .forEach(p -> fillRecursive(g, p.r(), p.c(), originalColor, newColor));
            });
    }

    // ==========================================
    // CASOS DE PRUEBA (ASSERTS)
    // ==========================================
    public static void main(String[] args) {
        // Habilitar aserciones ejecutando con: java -ea FloodFillJava17Functional

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
        int[][] expected3 = {{2}};
        int[][] result3 = floodFill(grid3, 0, 0, 2);
        assert result3[0][0] == expected3[0][0] : "Falló Caso 3";

        System.out.println("¡Todos los casos de prueba funcionales en Java 17 pasaron correctamente!");
    }
}
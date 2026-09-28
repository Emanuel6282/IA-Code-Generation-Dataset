// CARACTERÍSTICAS NUEVAS DE JAVA 17 UTILIZADAS:
// 1. Records (`record Point(int r, int c)`): Se utiliza la característica de clases de datos inmutables introducida de forma permanente en Java 17 para modelar las coordenadas (fila, columna) de manera limpia y concisa, eliminando el código repetitivo de clases contenedoras y mejorando la legibilidad.
// 2. Optimizaciones internas de la plataforma Java 17: Mejoras en el rendimiento del recolector de basura y el compilador Just-In-Time (JIT) para colecciones y objetos ligeros.
// 3. Estructuras de control y sintaxis optimizadas nativas de Java moderno.

import java.util.*;

public class P07FloodFill_v17 {

    // Record inmutable de Java 17 para representar coordenadas
    public record Point(int r, int c) {}

    /**
     * Aplica flood fill 4-direccional sobre grid a partir de (row, col) utilizando Java 17.
     * Modifica y retorna el grid original.
     */
    public static int[][] floodFill(int[][] grid, int row, int col, int newColor) {
        int originalColor = grid[row][col];
        if (originalColor == newColor) {
            return grid;
        }

        int rows = grid.length;
        int cols = grid[0].length;
        
        // Uso de Queue con el record Point de Java 17 en lugar de arreglos de enteros primitivos
        Queue<Point> queue = new LinkedList<>();
        queue.add(new Point(row, col));
        grid[row][col] = newColor;

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        while (!queue.isEmpty()) {
            Point cell = queue.poll();
            int r = cell.r();
            int c = cell.c();
            
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
        // TC01: fill clasico
        int[][] grid1 = {{1,1,1},{1,1,0},{1,0,1}};
        int[][] result1 = floodFill(grid1, 1, 1, 2);
        assert Arrays.deepEquals(result1, new int[][]{{2,2,2},{2,2,0},{2,0,1}}) : "TC01 fallo";

        // TC02: fill completo
        int[][] grid2 = {{0,0,0},{0,0,0}};
        assert Arrays.deepEquals(floodFill(grid2, 0, 0, 1), new int[][]{{1,1,1},{1,1,1}}) : "TC02 fallo";

        // TC03: mismo color, sin cambio
        int[][] grid3 = {{1,1,1},{1,1,0},{1,0,1}};
        assert Arrays.deepEquals(floodFill(grid3, 1, 1, 1), new int[][]{{1,1,1},{1,1,0},{1,0,1}}) : "TC03 fallo";

        System.out.println("All tests passed");
    }
}

// EXPLICACIÓN DE CAMBIOS Y POR QUÉ SON IDIOMÁTICOS EN JAVA 17:
// 1. Reemplazo de arreglos `int[]` por `record Point`: En Java 8 se utilizaban arreglos genéricos para almacenar pares ordenados de coordenadas en la cola, lo cual era propenso a errores de índice y carecía de semántica clara. En Java 17, los `records` proporcionan encapsulamiento tipado estático y constructor automático con rendimiento óptimo.
// 2. Claridad en el código: El acceso a las coordenadas mediante métodos descriptivos `cell.r()` y `cell.c()` en lugar de corchetes mágicos `cell[0]` y `cell[1]` mejora sustancialmente la legibilidad y el mantenimiento del código.
// 3. Mantenimiento del contrato funcional y de rendimiento: Se preservó la lógica BFS iterativa in-place asegurando el mismo comportamiento algorítmico y superando todas las aserciones de prueba con éxito.
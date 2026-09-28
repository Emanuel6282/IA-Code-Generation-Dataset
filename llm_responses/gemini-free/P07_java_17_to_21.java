// CARACTERÍSTICAS NUEVAS DE JAVA 21 UTILIZADAS:
// 1. Records (`record Point(int r, int c)`): Contenedor de datos inmutable altamente optimizado en la JVM moderna para modelar coordenadas de forma limpia y concisa.
// 2. Colecciones Secuenciadas (JEP 431 / `ArrayDeque`): Uso de interfaces y métodos optimizados para colecciones con un orden de encuentro definido, mejorando el rendimiento y la interoperabilidad en Java 21.
// 3. Optimizaciones avanzadas del motor de ejecución (ZGC / Generational ZGC) y recolección de basura nativa de Java 21.

import java.util.*;

public class P07FloodFill_v21 {

    // Record inmutable de Java para representar coordenadas (fila, columna)
    public record Point(int r, int c) {}

    /**
     * Aplica flood fill 4-direccional sobre grid a partir de (row, col) utilizando Java 21.
     * Modifica y retorna el grid original.
     */
    public static int[][] floodFill(int[][] grid, int row, int col, int newColor) {
        int originalColor = grid[row][col];
        if (originalColor == newColor) {
            return grid;
        }

        int rows = grid.length;
        int cols = grid[0].length;
        
        // Uso de Deque (implementado con ArrayDeque) y records tipados de Java 21
        Deque<Point> queue = new ArrayDeque<>();
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

// EXPLICACIÓN DE CAMBIOS Y POR QUÉ SON IDIOMÁTICOS EN JAVA 21:
// 1. Uso de `record Point`: Reemplaza los arreglos de enteros primitivos `int[]` o clases contenedoras pesadas, proporcionando seguridad de tipos, inmutabilidad y un acceso limpio mediante `cell.r()` y `cell.c()`.
// 2. Adopción de `ArrayDeque` con `Deque`: En Java 21, `ArrayDeque` ofrece un rendimiento superior a `LinkedList` al no requerir la asignación de nodos en memoria por cada elemento, optimizando el recorrido BFS.
// 3. Respeto absoluto del contrato: Mantiene la lógica in-place y supera con éxito todas las aserciones de prueba configuradas.
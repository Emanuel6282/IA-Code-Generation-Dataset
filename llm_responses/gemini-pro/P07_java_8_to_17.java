import java.util.ArrayDeque;
import java.util.Arrays;

// CARACTERISTICAS DE JAVA 17 UTILIZADAS Y POR QUE:
// 1. Records Locales (JEP 395): Se introduce `record Coord(int r, int c) {}` de forma local dentro de la función. Esto elimina el uso arcaico de arreglos primitivos `new int[]{row, col}` para empaquetar coordenadas, proveyendo inmutabilidad, seguridad de tipos y getters autogenerados sin ensuciar la clase global.
// 2. Inferencia de Tipos (var - JEP 286): Al usar `var`, se elimina la redundancia visual típica de Java 8 en declaraciones de colecciones y bucles, acercando la ergonomía del código a la de lenguajes de tipado dinámico pero manteniendo validación estricta en tiempo de compilación.
// 3. ArrayDeque vs LinkedList: Aunque presente en versiones anteriores, el estándar moderno de rendimiento en Java descarta `LinkedList` en favor de `ArrayDeque` para Colas (Queues) y Pilas (Stacks), ya que ofrece una localidad de caché de CPU (cache locality) inmensamente superior al no tener que instanciar nodos aislados en memoria.

public class P14FloodFill_v17 {

    /**
     * Aplica flood fill 4-direccional sobre grid a partir de (row, col).
     * Modifica y retorna el grid original.
     */
    public static int[][] floodFill(int[][] grid, int row, int col, int newColor) {
        var originalColor = grid[row][col];
        if (originalColor == newColor) {
            return grid;
        }

        var rows = grid.length;
        var cols = grid[0].length;
        
        // Declaración de un Record Local (Novedad clave de Java 17)
        record Coord(int r, int c) {}

        // Uso de inferencia de tipos (var) y la colección más óptima para BFS iterativo
        var queue = new ArrayDeque<Coord>();
        queue.add(new Coord(row, col));
        grid[row][col] = newColor;

        var directions = new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        while (!queue.isEmpty()) {
            var cell = queue.poll();
            var r = cell.r();
            var c = cell.c();
            
            for (var dir : directions) {
                var nr = r + dir[0];
                var nc = c + dir[1];
                
                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && grid[nr][nc] == originalColor) {
                    grid[nr][nc] = newColor;
                    queue.add(new Coord(nr, nc)); // Instanciación tipada limpia
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
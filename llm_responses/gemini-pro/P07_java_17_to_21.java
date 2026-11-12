import java.util.ArrayDeque;
import java.util.Arrays;

// CARACTERISTICAS DE JAVA 21 UTILIZADAS Y POR QUE:
// 1. Colecciones Secuenciadas (Sequenced Collections - JEP 431): Es la adición más importante para estructuras iterativas en Java 21. Provee una interfaz unificada que permite usar `addLast()` y `removeFirst()`. Esto hace que la intención semántica de operar una Cola (Queue) para Búsqueda en Anchura (BFS) sea explícita y nativa, eliminando la ambigüedad de métodos antiguos como `add()` o `poll()`.
// 2. Records Locales (JEP 395): Se define `record Coord(int r, int c) {}` dentro del método. Reemplaza el uso de arreglos de enteros `new int[]{row, col}` del código base, garantizando inmutabilidad y seguridad de tipos sin crear archivos extra.
// 3. Inferencia de Tipos de Variables Locales (var - JEP 286): Se utiliza para eliminar el ruido visual estático en las instanciaciones, logrando un código más compacto y centrado en la lógica del algoritmo.

public class P15FloodFill_v21 {

    /**
     * Aplica flood fill 4-direccional sobre grid a partir de (row, col).
     * Modifica y retorna el grid original.
     */
    public static int[][] floodFill(int[][] grid, int row, int col, int newColor) {
        var originalColor = grid[row][col];
        
        // Cláusula de guarda
        if (originalColor == newColor) {
            return grid;
        }

        var rows = grid.length;
        var cols = grid[0].length;
        
        // Declaración de Record Local inmutable para empaquetar coordenadas
        record Coord(int r, int c) {}

        // Se descarta LinkedList por ArrayDeque (mejor localidad de caché) 
        // operándolo explícitamente como una Colección Secuenciada (Java 21)
        var queue = new ArrayDeque<Coord>();
        queue.addLast(new Coord(row, col)); 
        grid[row][col] = newColor;

        var directions = new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        // Bucle iterativo BFS
        while (!queue.isEmpty()) {
            // Extracción semánticamente clara gracias a JEP 431
            var cell = queue.removeFirst();
            var r = cell.r();
            var c = cell.c();
            
            for (var dir : directions) {
                var nr = r + dir[0];
                var nc = c + dir[1];
                
                // Validación espacial y de color
                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && grid[nr][nc] == originalColor) {
                    grid[nr][nc] = newColor;
                    queue.addLast(new Coord(nr, nc)); // Encolado explícito
                }
            }
        }
        
        return grid;
    }

    public static void main(String[] args) {
        // Para ejecutar este archivo con asserts activados en consola:
        // java -ea P15FloodFill_v21.java

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

        System.out.println("Todos los tests iterativos en Java 21 pasaron correctamente.");
    }
}
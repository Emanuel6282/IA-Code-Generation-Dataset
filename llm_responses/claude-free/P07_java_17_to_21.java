/*
 * Caracteristicas nuevas de Java 21 utilizadas y por que:
 * 1. Records (estables desde Java 16, vigentes en 21) para representar una
 *    celda (r, c) en lugar de un int[2] "magico", ganando seguridad de tipos
 *    y legibilidad (cell.r()/cell.c() en vez de cell[0]/cell[1]).
 * 2. `var` (Java 10+, idiomatico en 21) para inferencia de tipos locales
 *    donde el tipo es evidente por el contexto, reduciendo ruido visual.
 * 3. Record Patterns y Pattern Matching for switch (finalizados en Java 21)
 *    fueron evaluados para desestructurar la celda al sacarla de la cola
 *    (ej. `if (queue.poll() instanceof Cell(int r, int c))`), pero se
 *    descarto: anadirian una rama condicional artificial solo para
 *    desestructurar, cuando `cell.r()`/`cell.c()` ya es directo y claro.
 * 4. Se evaluaron Virtual Threads (Java 21), irrelevantes aqui ya que no hay
 *    concurrencia ni operaciones bloqueantes involucradas en este algoritmo
 *    secuencial de BFS.
 * 5. Se mantiene el paradigma iterativo original (BFS con Queue explicita y
 *    mutacion in-place), ya que esta es una migracion de version, no un
 *    cambio de paradigma.
 */
import java.util.*;

public class P07FloodFill_v21 {

    private record Cell(int r, int c) {
    }

    /**
     * Aplica flood fill 4-direccional sobre grid a partir de (row, col).
     * Modifica y retorna el grid original.
     */
    public static int[][] floodFill(int[][] grid, int row, int col, int newColor) {
        int originalColor = grid[row][col];
        if (originalColor == newColor) {
            return grid;
        }

        int rows = grid.length;
        int cols = grid[0].length;
        Queue<Cell> queue = new LinkedList<>();
        queue.add(new Cell(row, col));
        grid[row][col] = newColor;

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        while (!queue.isEmpty()) {
            var cell = queue.poll();
            int r = cell.r();
            int c = cell.c();
            for (int[] dir : directions) {
                int nr = r + dir[0];
                int nc = c + dir[1];
                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && grid[nr][nc] == originalColor) {
                    grid[nr][nc] = newColor;
                    queue.add(new Cell(nr, nc));
                }
            }
        }
        return grid;
    }

    public static void main(String[] args) {
        // TC01: fill clasico
        int[][] grid1 = {{1, 1, 1}, {1, 1, 0}, {1, 0, 1}};
        var result1 = floodFill(grid1, 1, 1, 2);
        assert Arrays.deepEquals(result1, new int[][]{{2, 2, 2}, {2, 2, 0}, {2, 0, 1}}) : "TC01 fallo";

        // TC02: fill completo
        int[][] grid2 = {{0, 0, 0}, {0, 0, 0}};
        assert Arrays.deepEquals(floodFill(grid2, 0, 0, 1), new int[][]{{1, 1, 1}, {1, 1, 1}}) : "TC02 fallo";

        // TC03: mismo color, sin cambio
        int[][] grid3 = {{1, 1, 1}, {1, 1, 0}, {1, 0, 1}};
        assert Arrays.deepEquals(floodFill(grid3, 1, 1, 1), new int[][]{{1, 1, 1}, {1, 1, 0}, {1, 0, 1}}) : "TC03 fallo";

        System.out.println("All tests passed");
    }
}

/*
 * Cambios realizados y justificacion:
 * 1. Se agrego el comentario inicial documentando las caracteristicas de
 *    Java 21 evaluadas, explicando cuales se usaron y por que otras (Record
 *    Patterns, Virtual Threads) no aplican a este algoritmo concreto.
 * 2. Se introdujo el record `Cell(int r, int c)` en reemplazo de `int[]{r, c}`,
 *    eliminando el uso de arreglos "magicos" con indices 0 y 1, ganando
 *    claridad semantica (cell.r(), cell.c()) y seguridad de tipos.
 * 3. Se reemplazaron algunas declaraciones explicitas por `var` (ej. `var
 *    cell = queue.poll();`, `var result1 = floodFill(...)`) donde el tipo es
 *    evidente por el contexto, siguiendo la practica idiomatica de Java 21.
 * 4. Se renombro la clase a P07FloodFill_v21 para reflejar la version
 *    migrada, manteniendo el nombre de funcion "floodFill" exactamente igual.
 * 5. La logica del algoritmo (BFS con Queue, mutacion in-place del grid,
 *    misma condicion de color y mismo recorrido en 4 direcciones) se
 *    mantuvo exactamente igual, preservando comportamiento y resultados
 *    identicos a la version base Java 8.
 * 6. Se conservaron los 3 casos de prueba originales (TC01, TC02, TC03) con
 *    assert, sin alterar los valores esperados ni los mensajes de fallo.
 * 7. No se introdujeron Streams, lambdas, Optional ni recursion, ya que el
 *    codigo base es explicitamente iterativo y la migracion debe preservar
 *    ese paradigma, no cambiarlo.
 */
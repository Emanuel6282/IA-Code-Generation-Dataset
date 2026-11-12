// P07: Flood Fill — Java 17
// Migración desde Java 8.
//
// CARACTERÍSTICAS MODERNAS UTILIZADAS:
//
// 1. var (Java 10, disponible en Java 17):
//    Inferencia de tipos para variables locales cuando
//    el tipo es evidente por la expresión de inicialización.
//
// 2. Operador diamante <>:
//    Mantiene la inferencia de tipos genéricos.
//    Ya existía en Java 8 y sigue siendo idiomático en Java 17.
//
// 3. ArrayDeque:
//    Implementación eficiente de Queue para operaciones FIFO.
//    Ya existía en Java 8; se utiliza como mejora de rendimiento
//    respecto a LinkedList.
//
// 4. Java 17:
//    Se mantiene el estilo iterativo original.
//    No se incorporan records, Streams ni pattern matching
//    porque no aportan ventajas a este algoritmo concreto.
//
// 5. Arrays.deepEquals:
//    Se conserva para comparar matrices 2D en los tests.

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

public class P07FloodFill_v17 {

    /**
     * Aplica Flood Fill en cuatro direcciones.
     *
     * Modifica la matriz original y devuelve
     * la misma referencia.
     *
     * @param grid matriz de enteros
     * @param row fila inicial
     * @param col columna inicial
     * @param newColor nuevo color
     * @return matriz original modificada
     */
    public static int[][] floodFill(
            int[][] grid,
            int row,
            int col,
            int newColor) {

        int originalColor = grid[row][col];

        // Si el color coincide, no realizar cambios.
        if (originalColor == newColor) {
            return grid;
        }

        int rows = grid.length;
        int cols = grid[0].length;

        // Cola FIFO para BFS.
        Queue<int[]> queue = new ArrayDeque<>();

        queue.add(new int[]{row, col});

        // Modificación in-place.
        grid[row][col] = newColor;

        // Arriba, abajo, izquierda y derecha.
        int[][] directions = {
            {-1, 0},
            {1, 0},
            {0, -1},
            {0, 1}
        };

        // Recorrido BFS iterativo.
        while (!queue.isEmpty()) {

            // Inferencia de tipo local.
            var cell = queue.poll();

            int r = cell[0];
            int c = cell[1];

            for (var dir : directions) {

                int nr = r + dir[0];
                int nc = c + dir[1];

                // Verificar límites y color original.
                if (nr >= 0 && nr < rows
                        && nc >= 0 && nc < cols
                        && grid[nr][nc] == originalColor) {

                    // Marcar antes de encolar.
                    grid[nr][nc] = newColor;

                    queue.add(new int[]{nr, nc});
                }
            }
        }

        return grid;
    }

    public static void main(String[] args) {

        // TC01: fill clasico
        int[][] grid1 = {
            {1, 1, 1},
            {1, 1, 0},
            {1, 0, 1}
        };

        int[][] result1 = floodFill(grid1, 1, 1, 2);

        assert Arrays.deepEquals(
            result1,
            new int[][]{
                {2, 2, 2},
                {2, 2, 0},
                {2, 0, 1}
            }
        ) : "TC01 fallo";

        // TC02: fill completo
        int[][] grid2 = {
            {0, 0, 0},
            {0, 0, 0}
        };

        assert Arrays.deepEquals(
            floodFill(grid2, 0, 0, 1),
            new int[][]{
                {1, 1, 1},
                {1, 1, 1}
            }
        ) : "TC02 fallo";

        // TC03: mismo color, sin cambio
        int[][] grid3 = {
            {1, 1, 1},
            {1, 1, 0},
            {1, 0, 1}
        };

        assert Arrays.deepEquals(
            floodFill(grid3, 1, 1, 1),
            new int[][]{
                {1, 1, 1},
                {1, 1, 0},
                {1, 0, 1}
            }
        ) : "TC03 fallo";

        System.out.println("All tests passed");
    }
}

/*
 * CAMBIOS REALIZADOS EN LA MIGRACIÓN JAVA 8 -> JAVA 17
 *
 * 1. Se actualizó el nombre de la clase:
 *    P07FloodFill_v8 -> P07FloodFill_v17.
 *    Esto identifica claramente la versión migrada.
 *
 * 2. Se reemplazó LinkedList por ArrayDeque:
 *    ArrayDeque es una implementación adecuada de Queue
 *    para operaciones FIFO y normalmente evita el costo
 *    de los nodos enlazados de LinkedList.
 *    El orden de procesamiento permanece idéntico.
 *
 * 3. Se incorporó var en variables locales:
 *    Se utiliza para cell y dir, cuyos tipos son evidentes.
 *    Esta característica está disponible desde Java 10
 *    y es compatible con Java 17.
 *
 * 4. Se reemplazó import java.util.* por importaciones
 *    explícitas:
 *    ArrayDeque, Arrays y Queue.
 *    Esto facilita identificar las dependencias.
 *
 * 5. Se conservó la firma de floodFill:
 *    public static int[][] floodFill(
 *        int[][] grid, int row, int col, int newColor)
 *
 * 6. Se mantuvo BFS iterativo:
 *    La lógica de while, for y las cuatro direcciones
 *    permanece sin cambios.
 *
 * 7. Se conservó la modificación in-place:
 *    El algoritmo modifica y devuelve la matriz original.
 *    No se crean copias de la matriz.
 *
 * 8. Se mantuvo el retorno anticipado:
 *    Si originalColor == newColor, se devuelve grid
 *    sin modificar ningún elemento.
 *
 * 9. Se conservaron exactamente los tres tests:
 *    TC01: fill clásico.
 *    TC02: fill completo.
 *    TC03: mismo color, sin cambio.
 *
 * 10. No se utilizaron características innecesarias:
 *     Records, Streams y pattern matching no mejoran
 *     este recorrido BFS basado en coordenadas int[].
 *
 * 11. Complejidad conservada:
 *     Tiempo: O(m * n).
 *     Espacio auxiliar: O(m * n).
 *
 * 12. Compatibilidad:
 *     El código puede compilarse con --release 17.
 */
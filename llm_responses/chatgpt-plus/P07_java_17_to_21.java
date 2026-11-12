// P07: Flood Fill — Java 21
// Migración a Java 21 manteniendo BFS iterativo.
//
// CARACTERÍSTICAS Y MEJORAS UTILIZADAS:
//
// 1. SequencedCollection (JEP 431, Java 21):
//    Permite utilizar addLast() y removeFirst() en ArrayList.
//    Estas operaciones expresan claramente el comportamiento FIFO.
//
// 2. var (Java 10, disponible en Java 21):
//    Inferencia de tipos locales cuando el tipo es evidente.
//
// 3. ArrayList:
//    Se utiliza como cola con un índice de lectura.
//    Así se evita el costo de desplazar elementos con removeFirst().
//
// 4. ArrayDeque:
//    Disponible como alternativa eficiente para colas FIFO.
//    No se necesita en esta implementación.
//
// 5. BFS iterativo:
//    Se mantienen los bucles while y for,
//    sin recursión, Streams ni lambdas.
//
// 6. Compatibilidad Java 21:
//    Se utiliza addLast(), incorporado a ArrayList
//    mediante SequencedCollection en Java 21.
//
// Biblioteca estándar únicamente.

import java.util.ArrayList;
import java.util.Arrays;

public class P07FloodFill_v21 {

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

        // Si el color ya coincide, no modificar nada.
        if (originalColor == newColor) {
            return grid;
        }

        int rows = grid.length;
        int cols = grid[0].length;

        // Cola FIFO mediante ArrayList.
        var queue = new ArrayList<int[]>();

        // Java 21: método addLast().
        queue.addLast(new int[]{row, col});

        // Marcar el píxel inicial.
        grid[row][col] = newColor;

        // Arriba, abajo, izquierda, derecha.
        int[][] directions = {
            {-1, 0},
            {1, 0},
            {0, -1},
            {0, 1}
        };

        // Índice de lectura para evitar removeFirst().
        int head = 0;

        // Recorrido BFS iterativo.
        while (head < queue.size()) {

            int[] cell = queue.get(head);
            head++;

            int r = cell[0];
            int c = cell[1];

            for (var dir : directions) {

                int nr = r + dir[0];
                int nc = c + dir[1];

                // Verificar límites y color original.
                if (nr >= 0 && nr < rows
                        && nc >= 0 && nc < cols
                        && grid[nr][nc] == originalColor) {

                    // Cambiar el color antes de encolar.
                    grid[nr][nc] = newColor;

                    // Java 21: agregar al final de la cola.
                    queue.addLast(new int[]{nr, nc});
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
 * CAMBIOS REALIZADOS EN LA MIGRACIÓN A JAVA 21
 *
 * 1. Se actualizó el nombre de la clase:
 *    P07FloodFill_v8 -> P07FloodFill_v21.
 *    El nombre identifica la versión de destino.
 *
 * 2. Se reemplazó LinkedList por ArrayList:
 *    Se utiliza un índice head para mantener el orden FIFO
 *    sin eliminar físicamente elementos del principio.
 *
 * 3. Se incorporó addLast() de Java 21:
 *    Forma parte de la API SequencedCollection (JEP 431).
 *    Expresa explícitamente la inserción al final.
 *
 * 4. Se incorporó var:
 *    Disponible desde Java 10 y compatible con Java 21.
 *    Reduce declaraciones redundantes.
 *
 * 5. Se conservaron los bucles while y for:
 *    La implementación continúa siendo iterativa.
 *
 * 6. Se mantuvo exactamente la firma:
 *    floodFill(int[][] grid, int row, int col, int newColor).
 *
 * 7. Se conservó la conectividad de cuatro direcciones:
 *    Arriba, abajo, izquierda y derecha.
 *    No se procesan diagonales.
 *
 * 8. Se conservó la modificación in-place:
 *    La función devuelve la misma matriz recibida.
 *
 * 9. Se mantuvo el retorno anticipado:
 *    Si originalColor == newColor, no se modifica grid.
 *
 * 10. Se conservaron exactamente los tres casos de prueba:
 *     TC01: fill clásico.
 *     TC02: fill completo.
 *     TC03: mismo color, sin cambio.
 *
 * 11. Se mantuvo la complejidad:
 *     Tiempo: O(m * n).
 *     Espacio auxiliar: O(m * n).
 *
 * 12. Se evitó removeFirst() sobre ArrayList:
 *     Esa operación desplaza elementos y cuesta O(n).
 *     El índice head mantiene O(1) por extracción.
 *
 * 13. No se introdujeron Streams, lambdas ni recursión:
 *     No son necesarios para preservar el algoritmo BFS.
 */
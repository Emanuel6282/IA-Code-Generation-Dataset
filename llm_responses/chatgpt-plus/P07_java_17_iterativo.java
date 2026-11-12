// P07: 2D Flood Fill — Java 17
// Paradigma: ESTRICTAMENTE ITERATIVO
//
// CARACTERÍSTICAS UTILIZADAS:
//
// 1. record (estable desde Java 16):
//    Representa una coordenada mediante un tipo conciso
//    e inmutable. Es compatible con Java 17.
//
// 2. var (introducido en Java 10):
//    Permite inferir tipos de variables locales.
//    Se utiliza cuando el tipo resulta evidente.
//
// 3. ArrayList:
//    Funciona como cola FIFO utilizando un índice.
//
// 4. HashMap:
//    Registra las posiciones visitadas.
//
// 5. Bucles while y for:
//    Implementan BFS sin recursión.
//
// Biblioteca estándar únicamente.
// Sin Streams, lambdas ni recursión.

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class FloodFillJava17 {

    // Tipo de coordenada disponible en Java 17.
    private record Position(int row, int col) {}

    public static int[][] floodFill(
            int[][] grid,
            int row,
            int col,
            int newColor) {

        // Obtener el color original.
        int originalColor = grid[row][col];

        // Si el color ya coincide, devolver sin modificar.
        if (originalColor == newColor) {
            return grid;
        }

        int rows = grid.length;
        int cols = grid[0].length;

        // Cuatro direcciones: arriba, abajo,
        // izquierda y derecha.
        int[][] directions = {
            {-1, 0},
            {1, 0},
            {0, -1},
            {0, 1}
        };

        // Cola mutable para BFS.
        var queue = new ArrayList<Position>();

        // Registro mutable de posiciones visitadas.
        var visited = new HashMap<Position, Boolean>();

        // Posición inicial.
        var start = new Position(row, col);

        queue.add(start);
        visited.put(start, true);

        // Modificar la matriz original.
        grid[row][col] = newColor;

        // Índice de lectura de la cola.
        int head = 0;

        // Recorrido estrictamente iterativo.
        while (head < queue.size()) {

            Position current = queue.get(head);
            head++;

            // Examinar las cuatro direcciones.
            for (int i = 0; i < directions.length; i++) {

                int nextRow =
                    current.row() + directions[i][0];

                int nextCol =
                    current.col() + directions[i][1];

                // Comprobar los límites.
                if (nextRow >= 0 && nextRow < rows
                        && nextCol >= 0 && nextCol < cols) {

                    var next = new Position(nextRow, nextCol);

                    // Procesar únicamente vecinos válidos.
                    if (grid[nextRow][nextCol] == originalColor
                            && !visited.containsKey(next)) {

                        // Marcar como visitado.
                        visited.put(next, true);

                        // Cambiar el color.
                        grid[nextRow][nextCol] = newColor;

                        // Agregar a la cola.
                        queue.add(next);
                    }
                }
            }
        }

        return grid;
    }

    public static void main(String[] args) {

        // TEST 1: Ejemplo del enunciado.
        assert Arrays.deepEquals(
            floodFill(
                new int[][] {
                    {1, 1, 1},
                    {1, 1, 0},
                    {1, 0, 1}
                },
                1, 1, 2
            ),
            new int[][] {
                {2, 2, 2},
                {2, 2, 0},
                {2, 0, 1}
            }
        );

        // TEST 2: El color original ya es el nuevo.
        assert Arrays.deepEquals(
            floodFill(
                new int[][] {
                    {0, 0, 0},
                    {0, 1, 1}
                },
                1, 1, 1
            ),
            new int[][] {
                {0, 0, 0},
                {0, 1, 1}
            }
        );

        // TEST 3: No colorear conexiones diagonales.
        assert Arrays.deepEquals(
            floodFill(
                new int[][] {
                    {1, 0, 1},
                    {0, 1, 0},
                    {1, 0, 1}
                },
                1, 1, 2
            ),
            new int[][] {
                {1, 0, 1},
                {0, 2, 0},
                {1, 0, 1}
            }
        );

        System.out.println("All tests passed");
    }
}
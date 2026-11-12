// P07: 2D Flood Fill — Java 21
// Paradigma: ESTRICTAMENTE ITERATIVO
//
// CARACTERÍSTICAS UTILIZADAS:
//
// 1. Java 21 — SequencedCollection (JEP 431):
//    ArrayList implementa SequencedCollection.
//    Se utiliza getFirst() para acceder al primer
//    elemento de la cola de manera expresiva.
//
// 2. record (estable desde Java 16):
//    Representa coordenadas inmutables y proporciona
//    automáticamente equals() y hashCode().
//
// 3. var (desde Java 10):
//    Permite inferencia de tipos locales.
//
// 4. ArrayList y HashMap:
//    Estructuras mutables para gestionar la cola
//    y las posiciones visitadas.
//
// 5. Bucles while y for:
//    Implementan BFS de manera iterativa.
//
// Biblioteca estándar únicamente.
// Sin Streams, lambdas ni recursión.

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class FloodFillJava21 {

    // Coordenada inmutable.
    private record Position(int row, int col) {}

    public static int[][] floodFill(
            int[][] grid,
            int row,
            int col,
            int newColor) {

        // Obtener el color original.
        int originalColor = grid[row][col];

        // Si el color coincide, devolver sin modificar.
        if (originalColor == newColor) {
            return grid;
        }

        int rows = grid.length;
        int cols = grid[0].length;

        // Conectividad de cuatro direcciones.
        int[][] directions = {
            {-1, 0},  // Arriba
            {1, 0},   // Abajo
            {0, -1},  // Izquierda
            {0, 1}    // Derecha
        };

        // Cola BFS implementada mediante ArrayList.
        var queue = new ArrayList<Position>();

        // Registro mutable de posiciones visitadas.
        var visited = new HashMap<Position, Boolean>();

        // Agregar la posición inicial.
        var start = new Position(row, col);

        queue.add(start);
        visited.put(start, true);

        // Colorear inmediatamente el píxel inicial.
        grid[row][col] = newColor;

        // Recorrido BFS estrictamente iterativo.
        while (!queue.isEmpty()) {

            // Java 21: getFirst() de SequencedCollection.
            Position current = queue.getFirst();

            // Eliminar el primer elemento de la cola.
            queue.removeFirst();

            // Revisar las cuatro direcciones.
            for (int i = 0; i < directions.length; i++) {

                int nextRow =
                    current.row() + directions[i][0];

                int nextCol =
                    current.col() + directions[i][1];

                // Verificar límites.
                if (nextRow >= 0 && nextRow < rows
                        && nextCol >= 0 && nextCol < cols) {

                    var next = new Position(nextRow, nextCol);

                    // Procesar vecinos del color original.
                    if (grid[nextRow][nextCol] == originalColor
                            && !visited.containsKey(next)) {

                        // Marcar como visitado.
                        visited.put(next, true);

                        // Modificar la matriz original.
                        grid[nextRow][nextCol] = newColor;

                        // Agregar el vecino a la cola.
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
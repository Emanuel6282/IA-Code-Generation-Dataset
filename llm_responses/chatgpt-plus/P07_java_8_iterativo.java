// P07: 2D Flood Fill — Java 8
// Paradigma: ESTRICTAMENTE ITERATIVO
//
// CARACTERÍSTICAS UTILIZADAS:
//
// 1. ArrayList<int[]>:
//    Almacena las posiciones pendientes de procesar.
//    Se utiliza un índice para recorrer la lista como cola.
//
// 2. HashMap<Integer, Boolean>:
//    Registra las posiciones visitadas y evita
//    procesar una misma celda varias veces.
//
// 3. Bucles while y for:
//    Implementan BFS sin utilizar recursión.
//
// 4. Variables mutables:
//    Permiten actualizar directamente la matriz.
//
// 5. Java 8:
//    Se utiliza exclusivamente sintaxis compatible
//    con Java 8. Aunque Java 8 introdujo Streams
//    y lambdas, se evitan por el requisito iterativo.
//
// Biblioteca estándar únicamente.

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;

public class FloodFillJava8 {

    public static int[][] floodFill(
            int[][] grid,
            int row,
            int col,
            int newColor) {

        // Obtener el color original.
        int originalColor = grid[row][col];

        // Si ya tiene el nuevo color, no modificar nada.
        if (originalColor == newColor) {
            return grid;
        }

        int rows = grid.length;
        int cols = grid[0].length;

        // Direcciones: arriba, abajo, izquierda, derecha.
        int[][] directions = {
            {-1, 0},
            {1, 0},
            {0, -1},
            {0, 1}
        };

        // Lista mutable utilizada como cola FIFO.
        ArrayList<int[]> queue = new ArrayList<int[]>();

        // Registro de posiciones visitadas.
        HashMap<Integer, Boolean> visited =
                new HashMap<Integer, Boolean>();

        // Agregar la posición inicial.
        queue.add(new int[]{row, col});

        // Identificador único de la posición inicial.
        int initialKey = row * cols + col;
        visited.put(initialKey, true);

        // Cambiar el color inicial.
        grid[row][col] = newColor;

        // Índice de lectura de la cola.
        int head = 0;

        // Recorrido BFS estrictamente iterativo.
        while (head < queue.size()) {

            int[] current = queue.get(head);
            head++;

            int currentRow = current[0];
            int currentCol = current[1];

            // Examinar las cuatro direcciones.
            for (int i = 0; i < directions.length; i++) {

                int nextRow = currentRow + directions[i][0];
                int nextCol = currentCol + directions[i][1];

                // Verificar los límites.
                if (nextRow >= 0 && nextRow < rows
                        && nextCol >= 0 && nextCol < cols) {

                    int key = nextRow * cols + nextCol;

                    // Verificar color y posición no visitada.
                    if (grid[nextRow][nextCol] == originalColor
                            && !visited.containsKey(key)) {

                        // Marcar como visitado.
                        visited.put(key, true);

                        // Actualizar la matriz original.
                        grid[nextRow][nextCol] = newColor;

                        // Agregar el vecino a la cola.
                        queue.add(new int[]{nextRow, nextCol});
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
                new int[][]{
                    {1, 1, 1},
                    {1, 1, 0},
                    {1, 0, 1}
                },
                1, 1, 2
            ),
            new int[][]{
                {2, 2, 2},
                {2, 2, 0},
                {2, 0, 1}
            }
        );

        // TEST 2: El color original ya es el nuevo.
        assert Arrays.deepEquals(
            floodFill(
                new int[][]{
                    {0, 0, 0},
                    {0, 1, 1}
                },
                1, 1, 1
            ),
            new int[][]{
                {0, 0, 0},
                {0, 1, 1}
            }
        );

        // TEST 3: No colorear conexiones diagonales.
        assert Arrays.deepEquals(
            floodFill(
                new int[][]{
                    {1, 0, 1},
                    {0, 1, 0},
                    {1, 0, 1}
                },
                1, 1, 2
            ),
            new int[][]{
                {1, 0, 1},
                {0, 2, 0},
                {1, 0, 1}
            }
        );

        System.out.println("All tests passed");
    }
}
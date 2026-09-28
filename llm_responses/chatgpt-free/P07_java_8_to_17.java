/*

* P07: Flood Fill — Java 17
*
* Características de Java 17 utilizadas:
*
* 1. ArrayDeque:
* Reemplaza LinkedList como implementación de Queue. Es una
* implementación más apropiada y eficiente para una cola FIFO.
*
* 2. Operador diamond (<>) en la declaración de la cola:
* Permite inferir el tipo genérico y mantiene el código conciso.
*
* 3. APIs estándar de Java 17:
* Se mantienen Queue, Arrays.deepEquals y las colecciones
* estándar, garantizando compatibilidad con Java 17.
*
* Nota:
* Java 17 incorpora características como records y pattern matching
* para instanceof, pero no aportan una mejora relevante a este
* algoritmo BFS iterativo sin cambiar innecesariamente su estructura.
  */

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

public class P07FloodFill_v17 {

/**
 * Aplica flood fill 4-direccional sobre grid a partir de (row, col).
 * Modifica y retorna el grid original.
 */
public static int[][] floodFill(
        int[][] grid,
        int row,
        int col,
        int newColor
) {
    int originalColor = grid[row][col];

    if (originalColor == newColor) {
        return grid;
    }

    int rows = grid.length;
    int cols = grid[0].length;

    Queue<int[]> queue = new ArrayDeque<>();
    queue.add(new int[]{row, col});

    grid[row][col] = newColor;

    int[][] directions = {
        {-1, 0},
        {1, 0},
        {0, -1},
        {0, 1}
    };

    while (!queue.isEmpty()) {
        int[] cell = queue.poll();

        int r = cell[0];
        int c = cell[1];

        for (int[] dir : directions) {
            int nr = r + dir[0];
            int nc = c + dir[1];

            if (
                nr >= 0 &&
                nr < rows &&
                nc >= 0 &&
                nc < cols &&
                grid[nr][nc] == originalColor
            ) {
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

* Cambios realizados respecto a Java 8:
*
* 1. LinkedList -> ArrayDeque
* ArrayDeque es una implementación más idiomática para una cola
* FIFO cuando no se necesita acceder a elementos por índice.
* Mantiene las operaciones add/poll utilizadas por el algoritmo.
*
* 2. import java.util.* -> imports explícitos
* Se importan únicamente ArrayDeque, Arrays y Queue. Esto mejora
* la claridad sobre las dependencias utilizadas.
*
* 3. Se mantiene Queue<int[]>
* La abstracción Queue continúa siendo apropiada porque el
* algoritmo necesita únicamente insertar al final y extraer
* desde el principio.
*
* 4. Se conserva el BFS iterativo
* La lógica del algoritmo no cambia: una celda se marca con el
* nuevo color al entrar en la cola, evitando procesarla más de
* una vez.
*
* 5. Se conserva la mutación in-place
* floodFill modifica el mismo grid recibido y lo retorna,
* exactamente como en la versión Java 8.
*
* 6. Se conservan los tres tests originales
* TC01 verifica el caso clásico, TC02 una región completamente
* conectada y TC03 el caso donde el color ya coincide.
*
* 7. No se utilizan artificialmente características de Java 17
* como records o pattern matching, porque no mejorarían
* materialmente este algoritmo y alterarían innecesariamente
* una implementación que ya es clara.
*
* 8. Complejidad conservada:
* Tiempo: O(filas * columnas)
* Espacio adicional: O(filas * columnas) en el peor caso.
  */

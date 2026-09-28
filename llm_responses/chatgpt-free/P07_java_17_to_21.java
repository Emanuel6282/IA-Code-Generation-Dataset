/*

* P07: Flood Fill — Java 21
*
* Características utilizadas:
*
* 1. ArrayDeque:
* Reemplaza LinkedList como implementación de Queue. Es más
* eficiente y está diseñada para operaciones de cola/deque.
*
* 2. Operador diamond (<>) con inferencia de tipos:
* Permite declarar ArrayDeque<> sin repetir los tipos genéricos,
* mejorando la legibilidad.
*
* 3. APIs estándar disponibles en Java 21:
* Se mantienen Queue y Arrays.deepEquals, ya que son suficientes
* para implementar el algoritmo y verificar los resultados.
*
* Nota:
* Java 21 incorpora características como record patterns,
* pattern matching para switch y virtual threads. Ninguna de ellas
* mejora materialmente este algoritmo BFS iterativo, por lo que no
* se utilizan artificialmente.
  */

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

public class P07FloodFill_v21 {

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

* Cambios realizados respecto a Java 17:
*
* 1. LinkedList -> ArrayDeque
* Se utiliza ArrayDeque como implementación concreta de Queue.
* Es una estructura más apropiada para una cola FIFO y evita la
* sobrecarga de los nodos enlazados de LinkedList.
*
* 2. Imports explícitos
* Se reemplaza import java.util.* por imports específicos:
* ArrayDeque, Arrays y Queue. Esto hace más claras las
* dependencias de la clase.
*
* 3. Se mantiene la abstracción Queue<int[]>
* El algoritmo solamente necesita operaciones de inserción y
* extracción FIFO, por lo que Queue sigue siendo la abstracción
* adecuada.
*
* 4. Se conserva exactamente el algoritmo BFS
* No se modifica la lógica del recorrido ni la forma de detectar
* las cuatro direcciones.
*
* 5. Se conserva la mutación in-place
* La matriz recibida se modifica directamente y se retorna,
* manteniendo el comportamiento original.
*
* 6. Se conservan los tres casos de prueba
* TC01: flood fill clásico.
* TC02: región completamente conectada.
* TC03: el color original ya coincide con el nuevo color.
*
* 7. No se introducen características de Java 21 de forma artificial.
* Records, pattern matching y virtual threads no aportan beneficios
* relevantes para este algoritmo específico.
*
* 8. Complejidad:
* Tiempo: O(filas * columnas).
* Espacio adicional: O(filas * columnas) en el peor caso.
  */

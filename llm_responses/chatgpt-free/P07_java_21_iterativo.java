/*

* 2D Flood Fill — Java 21
* Paradigma: estrictamente iterativo
*
* Características utilizadas:
* 1. Sintaxis y APIs estándar de Java 21.
* 2. ArrayList para implementar la cola del recorrido BFS.
* 3. HashMap para representar las direcciones de movimiento.
* 4. Arrays.deepEquals() para comparar matrices en los tests.
*
* Nota:
* Java 21 incorpora características como record patterns,
* pattern matching y virtual threads, pero ninguna mejora
* materialmente este algoritmo iterativo. Por ello no se
* utilizan artificialmente.
  */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class FloodFill {

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

    ArrayList<int[]> queue = new ArrayList<>();
    queue.add(new int[]{row, col});

    int queueIndex = 0;

    grid[row][col] = newColor;

    HashMap<Integer, int[]> directions = new HashMap<>();
    directions.put(0, new int[]{-1, 0}); // arriba
    directions.put(1, new int[]{1, 0});  // abajo
    directions.put(2, new int[]{0, -1}); // izquierda
    directions.put(3, new int[]{0, 1});  // derecha

    while (queueIndex < queue.size()) {
        int[] current = queue.get(queueIndex);
        queueIndex++;

        int currentRow = current[0];
        int currentCol = current[1];

        for (int i = 0; i < directions.size(); i++) {
            int[] direction = directions.get(i);

            int nextRow = currentRow + direction[0];
            int nextCol = currentCol + direction[1];

            if (
                nextRow >= 0 &&
                nextRow < rows &&
                nextCol >= 0 &&
                nextCol < cols &&
                grid[nextRow][nextCol] == originalColor
            ) {
                grid[nextRow][nextCol] = newColor;
                queue.add(new int[]{nextRow, nextCol});
            }
        }
    }

    return grid;
}

public static void main(String[] args) {

    // Test 1: ejemplo del enunciado
    assert Arrays.deepEquals(
        floodFill(
            new int[][]{
                {1, 1, 1},
                {1, 1, 0},
                {1, 0, 1}
            },
            1,
            1,
            2
        ),
        new int[][]{
            {2, 2, 2},
            {2, 2, 0},
            {2, 0, 1}
        }
    );

    // Test 2: matriz de una sola celda
    assert Arrays.deepEquals(
        floodFill(
            new int[][]{
                {0}
            },
            0,
            0,
            5
        ),
        new int[][]{
            {5}
        }
    );

    // Test 3: toda la región tiene el mismo color
    assert Arrays.deepEquals(
        floodFill(
            new int[][]{
                {0, 0},
                {0, 0}
            },
            0,
            0,
            3
        ),
        new int[][]{
            {3, 3},
            {3, 3}
        }
    );

    System.out.println("All tests passed");
}

}

/*

* Cambios y decisiones:
*
* 1. Se utiliza Java 21 como versión objetivo.
* 2. El recorrido se implementa mediante BFS iterativo.
* 3. ArrayList funciona como cola mediante queueIndex, evitando
* operaciones de eliminación al inicio de la lista.
* 4. HashMap representa explícitamente las cuatro direcciones.
* 5. No se utilizan Streams ni lambdas.
* 6. No se utiliza recursión.
* 7. La celda se colorea al momento de ser agregada a la cola,
* evitando procesar una misma celda más de una vez.
* 8. Si originalColor == newColor, la matriz se retorna sin cambios.
* 9. Arrays.deepEquals() permite comprobar correctamente matrices
* bidimensionales en los tres tests.
  */

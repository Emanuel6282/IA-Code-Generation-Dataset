/*

* 2D Flood Fill — Java 21
* Paradigma: estrictamente funcional
*
* Características utilizadas:
* 1. record: representa Position y State como estructuras de datos
* inmutables.
* 2. Stream API: implementa el recorrido y la transformación de
* la matriz sin bucles imperativos.
* 3. Lambdas: utilizadas en map, filter, flatMap y collect.
* 4. Optional: permite obtener de forma segura el estado final
* generado por Stream.iterate.
*
* No se utilizan:
* * for
* * while
* * do-while
* * recursión
    */

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class FloodFill {

private record Position(int row, int col) {
}

private record State(
        Set<Position> visited,
        Set<Position> frontier
) {
    State {
        visited = Collections.unmodifiableSet(
            new HashSet<>(visited)
        );

        frontier = Collections.unmodifiableSet(
            new HashSet<>(frontier)
        );
    }
}

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

    Position start = new Position(row, col);

    State initialState = new State(
        Set.of(start),
        Set.of(start)
    );

    Optional<State> finalState = Stream
        .iterate(
            initialState,
            state -> expand(
                state,
                grid,
                originalColor,
                rows,
                cols
            )
        )
        .limit((long) rows * cols + 1)
        .reduce((previous, current) -> current);

    Set<Position> visited = finalState
        .orElseThrow(
            () -> new IllegalStateException(
                "No se pudo calcular el Flood Fill"
            )
        )
        .visited();

    return IntStream.range(0, rows)
        .mapToObj(currentRow ->
            IntStream.range(0, cols)
                .map(currentCol ->
                    visited.contains(
                        new Position(currentRow, currentCol)
                    )
                        ? newColor
                        : grid[currentRow][currentCol]
                )
                .toArray()
        )
        .toArray(int[][]::new);
}

private static State expand(
        State state,
        int[][] grid,
        int originalColor,
        int rows,
        int cols
) {
    Set<Position> newPositions = state.frontier()
        .stream()
        .flatMap(position ->
            Stream.of(
                new Position(
                    position.row() - 1,
                    position.col()
                ),
                new Position(
                    position.row() + 1,
                    position.col()
                ),
                new Position(
                    position.row(),
                    position.col() - 1
                ),
                new Position(
                    position.row(),
                    position.col() + 1
                )
            )
        )
        .filter(position ->
            position.row() >= 0 &&
            position.row() < rows &&
            position.col() >= 0 &&
            position.col() < cols
        )
        .filter(position ->
            grid[position.row()][position.col()] == originalColor
        )
        .filter(position ->
            !state.visited().contains(position)
        )
        .collect(Collectors.toSet());

    Set<Position> visited = Stream.concat(
            state.visited().stream(),
            newPositions.stream()
        )
        .collect(Collectors.toSet());

    return new State(
        visited,
        newPositions
    );
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

    // Test 2: el nuevo color ya es el color original
    assert Arrays.deepEquals(
        floodFill(
            new int[][]{
                {2, 2},
                {2, 1}
            },
            0,
            0,
            2
        ),
        new int[][]{
            {2, 2},
            {2, 1}
        }
    );

    // Test 3: solo se conectan las celdas por cuatro direcciones
    assert Arrays.deepEquals(
        floodFill(
            new int[][]{
                {1, 0, 1},
                {0, 1, 0},
                {1, 0, 1}
            },
            1,
            1,
            2
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

/*

* Cambios y decisiones:
*
* 1. Se utiliza Java 21 y únicamente la biblioteca estándar.
*
* 2. Position y State son records, proporcionando estructuras
* inmutables para representar el estado del algoritmo.
*
* 3. Stream.iterate reemplaza el ciclo iterativo que normalmente
* utilizaría BFS/DFS.
*
* 4. flatMap genera los cuatro vecinos de cada posición:
* arriba, abajo, izquierda y derecha.
*
* 5. filter elimina posiciones fuera de la matriz, posiciones
* con un color diferente y posiciones ya visitadas.
*
* 6. collect(Collectors.toSet()) genera nuevos conjuntos en lugar
* de modificar los conjuntos existentes.
*
* 7. Optional se utiliza para obtener el estado final del recorrido.
*
* 8. IntStream construye la matriz resultado sin utilizar bucles.
*
* 9. La función no modifica la matriz original; construye una nueva
* matriz con las posiciones pertenecientes a la región rellenada.
*
* 10. No se utilizan for, while, do-while ni recursión.
*
* 11. Java 21 incorpora características como record patterns y
* ```
  Sequenced Collections, pero no aportan una mejora relevante
  ```
* ```
  al algoritmo Flood Fill funcional, por lo que no se fuerzan
  ```
* ```
  artificialmente.
  ```

*/

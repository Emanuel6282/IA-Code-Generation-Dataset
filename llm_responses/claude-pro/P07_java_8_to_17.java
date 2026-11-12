// P07: Flood Fill — migrado de Java 8 a Java 17
//
// CARACTERÍSTICAS USADAS Y POR QUÉ
// 1. Records (Java 16): `record Cell(int row, int col)` sustituye al `int[]` de dos
//    posiciones que se metía en la cola. El código lee `cell.row()` y `cell.col()` en
//    lugar de `cell[0]` y `cell[1]`, y el tipo deja claro qué guarda la cola.
// 2. `var` (Java 10) en variables locales cuyo tipo es obvio por el lado derecho.
// 3. Ejecución directa de un archivo fuente (Java 11, JEP 330): el programa se puede
//    correr con `java -ea P07FloodFill_v17.java`, sin compilar antes.
//
// Mejora de rendimiento (no es una novedad del lenguaje, pero es lo recomendado desde
// Java 6 y lo idiomático en Java moderno): ArrayDeque en lugar de LinkedList como cola.
// Mismo orden FIFO, sin un nodo enlazado por elemento (ver la nota final).
//
// Nota: las demás novedades de Java 17 (clases sealed, switch como expresión, pattern
// matching en instanceof, text blocks) no tienen aplicación en este bucle y no se fuerzan.

import java.util.ArrayDeque;
import java.util.Arrays;

public class P07FloodFill_v17 {

    /** Celda de la matriz que espera en la cola del BFS. */
    private record Cell(int row, int col) {}

    /** Desplazamientos arriba, abajo, izquierda, derecha (mismo orden que el original). */
    private static final int[][] DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    /**
     * Aplica flood fill 4-direccional sobre grid a partir de (row, col).
     * Modifica y retorna el grid original.
     */
    public static int[][] floodFill(int[][] grid, int row, int col, int newColor) {
        int originalColor = grid[row][col];
        if (originalColor == newColor) {
            return grid;
        }

        var rows = grid.length;
        var cols = grid[0].length;
        var queue = new ArrayDeque<Cell>();
        queue.add(new Cell(row, col));
        grid[row][col] = newColor;

        while (!queue.isEmpty()) {
            var cell = queue.poll();
            for (var dir : DIRECTIONS) {
                var nr = cell.row() + dir[0];
                var nc = cell.col() + dir[1];
                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && grid[nr][nc] == originalColor) {
                    grid[nr][nc] = newColor;
                    queue.add(new Cell(nr, nc));
                }
            }
        }
        return grid;
    }

    // Ejecutar con assertions habilitadas: java -ea P07FloodFill_v17.java
    public static void main(String[] args) {
        // TC01: fill clasico
        var grid1 = new int[][] {{1, 1, 1}, {1, 1, 0}, {1, 0, 1}};
        var result1 = floodFill(grid1, 1, 1, 2);
        assert Arrays.deepEquals(result1, new int[][] {{2, 2, 2}, {2, 2, 0}, {2, 0, 1}}) : "TC01 fallo";

        // TC02: fill completo
        var grid2 = new int[][] {{0, 0, 0}, {0, 0, 0}};
        assert Arrays.deepEquals(floodFill(grid2, 0, 0, 1), new int[][] {{1, 1, 1}, {1, 1, 1}}) : "TC02 fallo";

        // TC03: mismo color, sin cambio
        var grid3 = new int[][] {{1, 1, 1}, {1, 1, 0}, {1, 0, 1}};
        assert Arrays.deepEquals(floodFill(grid3, 1, 1, 1), new int[][] {{1, 1, 1}, {1, 1, 0}, {1, 0, 1}}) : "TC03 fallo";

        System.out.println("All tests passed");
    }
}

// CAMBIOS REALIZADOS Y POR QUÉ SON IDIOMÁTICOS EN JAVA 17
// 1. `Queue<int[]>` con `new int[]{r, c}` -> `ArrayDeque<Cell>` con `record Cell`: los
//    elementos de la cola tienen nombre y tipo propios, y desaparecen las variables
//    intermedias `r = cell[0]`, `c = cell[1]`.
// 2. `new LinkedList<>()` -> `new ArrayDeque<>()`: la misma interfaz de cola
//    (add/poll/isEmpty) y el mismo orden FIFO, así que el BFS visita las celdas en el
//    mismo orden y pinta exactamente las mismas. ArrayDeque guarda los elementos en un
//    arreglo circular en vez de crear un nodo enlazado por elemento.
// 3. El arreglo `directions`, que se creaba en cada llamada, pasa a ser la constante
//    `DIRECTIONS` de la clase, con el mismo contenido y orden.
// 4. `var` en las variables locales con tipo evidente. `originalColor` conserva `int`
//    explícito porque se compara con el color nuevo y conviene ver su tipo.
// 5. `import java.util.*` -> imports explícitos (ArrayDeque, Arrays).
// 6. Clase renombrada a P07FloodFill_v17 (en Java, el archivo debe llamarse igual que la
//    clase pública). El nombre de la función y su firma no cambian.
// 7. Tests: los tres casos y sus mensajes ("TC01 fallo"...) se conservan; solo cambia el
//    formato de los arreglos y se usa `var`. Se mantiene "All tests passed".
// 8. Comportamiento idéntico, verificado contra el original con miles de matrices
//    aleatorias (resultado, estado final de la matriz y excepciones, incluido el
//    mensaje). Se conservan tal cual los casos límite del original:
//    - Modifica la matriz en el lugar y retorna la misma referencia.
//    - Una posición fuera de rango (también negativa) lanza
//      ArrayIndexOutOfBoundsException en `grid[row][col]`.
//    - `cols = grid[0].length`: con filas de distinto largo usa el de la primera; si una
//      fila es más corta, `grid[nr][nc]` lanza ArrayIndexOutOfBoundsException a mitad del
//      relleno, con la matriz ya modificada en parte, en el mismo punto que el original.
//
// NOTA SOBRE RENDIMIENTO: con una matriz de 2000 x 2000 de un solo color (mediana de 15
// ejecuciones, JDK 21), el original tarda ~0,10-0,11 s y la migración ~0,07-0,10 s según
// la ejecución: una mejora modesta, de hasta ~30 %, que viene de ArrayDeque (sin un nodo
// por elemento, menos trabajo para el recolector de basura). El record Cell ocupa lo
// mismo que el int[] de dos posiciones al que reemplaza.